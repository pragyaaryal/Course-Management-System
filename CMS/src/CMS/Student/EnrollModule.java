package CMS.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EnrollModule extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JComboBox<String> comboBox;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EnrollModule frame = new EnrollModule();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public EnrollModule() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 192);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Optional Module name :");
        lblNewLabel.setBounds(23, 72, 160, 16);
        contentPane.add(lblNewLabel);

        comboBox = new JComboBox<>();
        comboBox.setBounds(195, 68, 181, 27);
        contentPane.add(comboBox);

        JLabel lblStudentId = new JLabel("Student ID");
        lblStudentId.setBounds(23, 33, 160, 16);
        contentPane.add(lblStudentId);

        textField = new JTextField();
        textField.setBounds(204, 30, 160, 26);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("Enroll ");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentID = textField.getText();
                String selectedModule = ((String) comboBox.getSelectedItem()).split(" - ")[0];
                enrollModule(studentID, selectedModule);
                dispose();
                StudentDashboard studentDashboard = new StudentDashboard(studentID);
                studentDashboard.setVisible(true);
            }
        });
        btnNewButton.setBounds(152, 121, 117, 29);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Back");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                StudentDashboard studentDashboard = new StudentDashboard(textField.getText());
                studentDashboard.setVisible(true);
            }
        });
        btnNewButton_1.setBounds(376, 6, 74, 29);
        contentPane.add(btnNewButton_1);

        populateComboBox();
    }

    private void enrollModule(String studentID, String selectedModule) {
        String url = "jdbc:mysql://localhost:3306/CMS";
        String username = "root"; // Change to your MySQL username
        String password = ""; // Change to your MySQL password

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String updateQuery = "UPDATE Modules SET Enroll = 'Yes' WHERE ModuleID = ? AND CourseID IN (SELECT CourseID FROM student WHERE StudentID = ?)";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, selectedModule);
                updateStmt.setString(2, studentID);
                updateStmt.executeUpdate();
            }

            String insertQuery = "INSERT INTO studentmodules (StudentID, ModuleID) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setString(1, studentID);
                pstmt.setString(2, selectedModule);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void populateComboBox() {
        String url = "jdbc:mysql://localhost:3306/CMS";
        String username = "root"; 
        String password = ""; 

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT ModuleID, ModuleName FROM Modules WHERE ModuleChoice = 'Optional'";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String moduleID = rs.getString("ModuleID");
                        String moduleName = rs.getString("ModuleName");
                        comboBox.addItem(moduleID + " - " + moduleName);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
