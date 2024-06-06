package CMS.Teacher;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class AssignMarks extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> studentComboBox;
    private JTextField moduleIDTextField;
    private JTextField studentIDTextField;
    private JTextField courseIDTextField;
    private JTextField marksTextField;

    // Variables to store teacher ID and module ID
    private static String teacherId;
    private String moduleID;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AssignMarks frame = new AssignMarks(teacherId);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AssignMarks(String teacherId) {
        this.teacherId = teacherId;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Student Name");
        lblNewLabel.setBounds(26, 27, 92, 16);
        contentPane.add(lblNewLabel);

        studentComboBox = new JComboBox<>();
        studentComboBox.setBounds(165, 23, 161, 27);
        contentPane.add(studentComboBox);

        JLabel lblNewLabel_1 = new JLabel("Student ID: ");
        lblNewLabel_1.setBounds(26, 68, 92, 16);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Module ID");
        lblNewLabel_2.setBounds(26, 148, 102, 16);
        contentPane.add(lblNewLabel_2);

        moduleIDTextField = new JTextField();
        moduleIDTextField.setBounds(167, 143, 161, 26);
        contentPane.add(moduleIDTextField);
        moduleIDTextField.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("Course ID");
        lblNewLabel_3.setBounds(26, 107, 92, 16);
        contentPane.add(lblNewLabel_3);

        studentIDTextField = new JTextField();
        studentIDTextField.setColumns(10);
        studentIDTextField.setBounds(167, 62, 161, 26);
        contentPane.add(studentIDTextField);

        courseIDTextField = new JTextField();
        courseIDTextField.setColumns(10);
        courseIDTextField.setBounds(167, 105, 161, 26);
        contentPane.add(courseIDTextField);

        JLabel lblNewLabel_2_1 = new JLabel("Marks");
        lblNewLabel_2_1.setBounds(26, 190, 102, 16);
        contentPane.add(lblNewLabel_2_1);

        marksTextField = new JTextField();
        marksTextField.setColumns(10);
        marksTextField.setBounds(165, 181, 161, 26);
        contentPane.add(marksTextField);

        JButton updateBtn = new JButton("Update Marks");
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateMarks();
            }
        });
        updateBtn.setBounds(175, 222, 117, 29);
        contentPane.add(updateBtn);

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TeacherDashboard t = new TeacherDashboard(teacherId);
                t.setVisible(true);
                dispose();
            }
        });
        backBtn.setBounds(369, 6, 75, 29);
        contentPane.add(backBtn);

        // Fetch student names based on the teacher ID
        fetchStudentNames();
    }

    // Method to fetch student names assigned to the module corresponding to the teacher ID
    private void fetchStudentNames() {
        studentComboBox.removeAllItems();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            String studentSql = "SELECT StudentName FROM student WHERE CourseID IN (SELECT CourseID FROM Teachers WHERE TeacherID = ?)";
            try (PreparedStatement studentStmt = conn.prepareStatement(studentSql)) {
                studentStmt.setString(1, teacherId);
                try (ResultSet studentRs = studentStmt.executeQuery()) {
                    while (studentRs.next()) {
                        String studentName = studentRs.getString("StudentName");
                        studentComboBox.addItem(studentName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update marks in the database
    private void updateMarks() {
        String studentName = studentComboBox.getSelectedItem().toString();
        String studentID = studentIDTextField.getText();
        String courseID = courseIDTextField.getText();
        String moduleID = moduleIDTextField.getText();
        String marks = marksTextField.getText();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            // Insert marks into the Marks table
            String insertSql = "INSERT INTO Marks (StudentID, ModuleID, CourseID, Marks) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, studentID);
                insertStmt.setString(2, moduleID);
                insertStmt.setString(3, courseID);
                insertStmt.setString(4, marks);
                insertStmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Marks updated successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to set module ID
    public void setModuleID(String moduleId) {
        this.moduleID = moduleId;
    }
}
