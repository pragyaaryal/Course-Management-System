package CMS.Admin;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class Add_DeleteCourse extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private String adminID;
    private String adminName;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Add_DeleteCourse frame = new Add_DeleteCourse();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public Add_DeleteCourse() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 235);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Course ID");
        lblNewLabel.setBounds(37, 32, 99, 26);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(156, 32, 130, 26);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Course Name");
        lblNewLabel_1.setBounds(37, 81, 99, 16);
        contentPane.add(lblNewLabel_1);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(156, 76, 130, 26);
        contentPane.add(textField_1);

        JButton btnNewButton = new JButton("Back");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		AdminDashboard a = new AdminDashboard(adminID, adminName);
        		a.setVisible(true);
        		dispose();
        	}
        });
        btnNewButton.setBounds(327, 6, 117, 29);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Add Course");
        btnNewButton_1.setBounds(64, 133, 117, 29);
        contentPane.add(btnNewButton_1);
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCourse();
            }
        });

        JButton btnNewButton_1_1 = new JButton("Delete Course");
        btnNewButton_1_1.setBounds(217, 133, 117, 29);
        contentPane.add(btnNewButton_1_1);
        btnNewButton_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteCourse();
            }
        });
    }

    private void addCourse() {
        String courseID = textField.getText();
        String courseName = textField_1.getText();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
            String query = "SELECT * FROM Courses WHERE CourseID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, courseID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Course with ID " + courseID + " already exists.");
            } else {
                String insertQuery = "INSERT INTO Courses (CourseID, CourseName) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, courseID);
                insertStmt.setString(2, courseName);
                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Course added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add course.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void deleteCourse() {
        String courseID = textField.getText();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
            String query = "SELECT * FROM Courses WHERE CourseID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, courseID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String deleteQuery = "DELETE FROM Courses WHERE CourseID = ?";
                PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                deleteStmt.setString(1, courseID);
                int rowsAffected = deleteStmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Course deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete course.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Course with ID " + courseID + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
