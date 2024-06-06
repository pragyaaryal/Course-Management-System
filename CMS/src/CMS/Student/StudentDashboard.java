package CMS.Student;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import CMS.Welcome;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudentDashboard extends JFrame {

    private JPanel contentPane;
    private JTable moduleTable;
    private JLabel lblStudentID;
    private JLabel lblStudentName;
    private JLabel lblCourseID;
    private JLabel lblLevel;
    private String studentID;
    private String courseID;
    private String level;
    private JButton btnEnrollOptionalModule;
    private JButton btnNewButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StudentDashboard frame = new StudentDashboard("YourStudentIDHere");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public StudentDashboard(String studentID) {
        this.studentID = studentID;
        initializeComponents();
        retrieveStudentInformation();
        populateModules();
    }

    private void initializeComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        lblStudentID = new JLabel("Student ID: ");
        lblStudentID.setBounds(16, 54, 300, 16);
        contentPane.add(lblStudentID);

        lblStudentName = new JLabel("Student Name: ");
        lblStudentName.setBounds(16, 92, 300, 16);
        contentPane.add(lblStudentName);

        lblCourseID = new JLabel("Course ID: ");
        lblCourseID.setBounds(494, 54, 300, 16);
        contentPane.add(lblCourseID);

        lblLevel = new JLabel("Level: ");
        lblLevel.setBounds(494, 92, 300, 16);
        contentPane.add(lblLevel);

        moduleTable = new JTable();
        moduleTable.setBounds(16, 168, 750, 300);
        contentPane.add(moduleTable);

        btnEnrollOptionalModule = new JButton("Enroll Optional Module");
        btnEnrollOptionalModule.setBounds(266, 502, 211, 29);
        contentPane.add(btnEnrollOptionalModule);
        
        btnNewButton = new JButton("Home");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Welcome w = new Welcome();
        		w.setVisible(true);
        		dispose();
        	}
        });
        btnNewButton.setBounds(677, 6, 117, 29);
        contentPane.add(btnNewButton);
    }

    private void retrieveStudentInformation() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            String query = "SELECT StudentName, CourseID, Level FROM student WHERE StudentID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, studentID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String studentName = rs.getString("StudentName");
                courseID = rs.getString("CourseID");
                level = rs.getString("Level");

                lblStudentID.setText("Student ID: " + studentID);
                lblStudentName.setText("Student Name: " + studentName);
                lblCourseID.setText("Course ID: " + courseID);
                lblLevel.setText("Level: " + level);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateModules() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Module ID");
        model.addColumn("Module Name");
        model.addColumn("Teacher ID");
        model.addColumn("Teacher Name");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            String query = "SELECT m.ModuleID, m.ModuleName, t.TeacherID, t.TeacherName " +
                    "FROM Modules m " +
                    "LEFT JOIN Teachers t ON m.TeacherID = t.TeacherID " +
                    "WHERE m.CourseID = ? AND m.Level = ? AND m.Enroll = 'Yes'";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, courseID);
            pstmt.setString(2, level);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String moduleID = rs.getString("ModuleID");
                String moduleName = rs.getString("ModuleName");
                String teacherID = rs.getString("TeacherID");
                String teacherName = rs.getString("TeacherName");

                model.addRow(new Object[]{moduleID, moduleName, teacherID, teacherName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        moduleTable.setModel(model);

        // Enable "Enroll Optional Module" button only for level 6 students
        if ("6".equals(level)) {
            btnEnrollOptionalModule.setEnabled(true);
        } else {
            btnEnrollOptionalModule.setEnabled(false);
        }

        // Add ActionListener for the "Enroll Optional Module" button
        btnEnrollOptionalModule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnrollModule m = new EnrollModule();
                m.setVisible(true);
                dispose();
            }
        });
    }
}
