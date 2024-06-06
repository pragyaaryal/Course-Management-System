package CMS.Admin;

import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ApproveResult extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField studentIDField;
    private JComboBox<String> approveComboBox;

    /**
     * Create the frame.
     */
    public ApproveResult() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 405, 183);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Student ID :");
        lblNewLabel.setBounds(22, 20, 103, 16);
        contentPane.add(lblNewLabel);

        JButton btnApprove = new JButton("Result Slip");
        btnApprove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                approveResult();
            }
        });
        btnApprove.setBounds(126, 99, 170, 29);
        contentPane.add(btnApprove);

        studentIDField = new JTextField();
        studentIDField.setBounds(126, 15, 191, 26);
        contentPane.add(studentIDField);
        studentIDField.setColumns(10);

        JLabel lblNewLabel_4 = new JLabel("Approve :");
        lblNewLabel_4.setBounds(22, 65, 61, 16);
        contentPane.add(lblNewLabel_4);

        approveComboBox = new JComboBox<String>();
        approveComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "Yes", "No" }));
        approveComboBox.setBounds(126, 60, 191, 27);
        contentPane.add(approveComboBox);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ManageResult m = new ManageResult();
                m.setVisible(true);
                dispose();
            }
        });
        btnBack.setBounds(329, 6, 70, 29);
        contentPane.add(btnBack);
    }

    private void approveResult() {
        String studentID = studentIDField.getText();
        String approvalStatus = (String) approveComboBox.getSelectedItem();

        if (!validateInput(studentID)) {
            return;
        }

        // Retrieve module data from the database
        Object[][] moduleData = fetchModuleData(studentID);

        if (moduleData != null) {
            // Approve result in the database
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
                String updateSql = "UPDATE Marks SET Approve = ? WHERE StudentID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, approvalStatus);
                    updateStmt.setString(2, studentID);
                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        // Show result slip
                        showResultSlip(studentID, approvalStatus, moduleData);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to approve result.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to approve result. Error: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No module data found for the student.");
        }
    }

    private Object[][] fetchModuleData(String studentID) {
        Object[][] moduleData = null;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            String query = "SELECT Modules.ModuleID, Modules.ModuleName, Marks.Marks " +
                            "FROM Modules " +
                            "INNER JOIN Marks ON Modules.ModuleID = Marks.ModuleID " +
                            "WHERE Marks.StudentID = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, studentID);

                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Object[]> rows = new ArrayList<>();
                    while (resultSet.next()) {
                        String moduleID = resultSet.getString("ModuleID");
                        String moduleName = resultSet.getString("ModuleName");
                        int marks = resultSet.getInt("Marks");

                        Object[] rowData = { moduleID, moduleName, marks };
                        rows.add(rowData);
                    }

                    moduleData = new Object[rows.size()][3];
                    for (int i = 0; i < rows.size(); i++) {
                        moduleData[i] = rows.get(i);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch module data. Error: " + e.getMessage());
        }

        return moduleData;
    }


    private void showResultSlip(String studentID, String approvalStatus, Object[][] moduleData) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            String query = "SELECT s.StudentName, s.CourseID, c.CourseName FROM student s INNER JOIN Courses c ON s.CourseID = c.CourseID WHERE s.StudentID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, studentID);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String studentName = rs.getString("StudentName");
                        String courseID = rs.getString("CourseID");
                        String courseName = rs.getString("CourseName");

                        ResultSlip resultSlip = new ResultSlip();
                        resultSlip.updateInformation(studentID, studentName, courseID, approvalStatus, moduleData);
                        resultSlip.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Student not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve student information. Error: " + e.getMessage());
        }
    }

    private boolean validateInput(String studentID) {
        if (studentID.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter the student ID.");
            return false;
        }
        return true;
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ApproveResult frame = new ApproveResult();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
