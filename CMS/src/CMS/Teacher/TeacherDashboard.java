package CMS.Teacher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import CMS.Welcome;

public class TeacherDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private String teacherId;
    private JLabel lblTeacherId;
    private JLabel lblTeacherName;
    private JLabel lblModuleId;
    private JLabel lblCourseId;
    private String teacherName;
    private String moduleID;
    private String courseID;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TeacherDashboard frame = new TeacherDashboard(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TeacherDashboard(String teacherId) {
        this.teacherId = teacherId;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Fetch teacher's name, module ID, and course ID
        fetchTeacherDetails();

        lblTeacherId = new JLabel("Teacher ID: " + teacherId);
        lblTeacherId.setBounds(17, 21, 182, 24);
        contentPane.add(lblTeacherId);

        lblTeacherName = new JLabel("Teacher Name: " + teacherName);
        lblTeacherName.setBounds(17, 57, 200, 16);
        contentPane.add(lblTeacherName);

        JButton btnAssignMarks = new JButton("Assign Marks");
        btnAssignMarks.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAssignMarksPage();
            }
        });
        btnAssignMarks.setBounds(286, 337, 117, 29);
        contentPane.add(btnAssignMarks);

        lblModuleId = new JLabel("Module ID: " + moduleID);
        lblModuleId.setBounds(386, 25, 200, 16);
        contentPane.add(lblModuleId);

        lblCourseId = new JLabel("Course ID: " + courseID);
        lblCourseId.setBounds(386, 57, 200, 16);
        contentPane.add(lblCourseId);

        // Initialize the JTable
        initializeTable();
        
        JButton btnHome = new JButton("Home");
        btnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Welcome w = new Welcome();
                w.setVisible(true);
                dispose();
            }
        });
        btnHome.setBounds(598, 6, 79, 24);
        contentPane.add(btnHome);
    }

    private void fetchTeacherDetails() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            String teacherSql = "SELECT TeacherName, ModuleID, CourseID FROM Teachers WHERE TeacherID=?";
            try (PreparedStatement teacherStmt = conn.prepareStatement(teacherSql)) {
                teacherStmt.setString(1, teacherId);
                try (ResultSet teacherRs = teacherStmt.executeQuery()) {
                    if (teacherRs.next()) {
                        teacherName = teacherRs.getString("TeacherName");
                        moduleID = teacherRs.getString("ModuleID");
                        courseID = teacherRs.getString("CourseID");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
    }

    private void initializeTable() {
        // Initialize the JTable with student data
        table = new JTable();
        table.setBounds(17, 91, 660, 230);
        contentPane.add(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(17, 91, 660, 230);
        contentPane.add(scrollPane);

        fetchStudentData();
    }

    private void fetchStudentData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            String studentSql = "SELECT s.StudentID, s.StudentName, COALESCE(m.Marks, 'Not Assigned') AS Marks " +
                                "FROM student s " +
                                "LEFT JOIN Marks m ON s.StudentID = m.StudentID AND m.ModuleID = ? AND m.CourseID = ? " +
                                "WHERE s.CourseID = (SELECT CourseID FROM Teachers WHERE TeacherID = ? AND ModuleID = ?)";
            try (PreparedStatement studentStmt = conn.prepareStatement(studentSql)) {
                studentStmt.setString(1, moduleID);
                studentStmt.setString(2, courseID);
                studentStmt.setString(3, teacherId);
                studentStmt.setString(4, moduleID);
                try (ResultSet studentRs = studentStmt.executeQuery()) {
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("Student ID");
                    model.addColumn("Student Name");
                    model.addColumn("Marks");

                    while (studentRs.next()) {
                        String studentId = studentRs.getString("StudentID");
                        String studentName = studentRs.getString("StudentName");
                        String marks = studentRs.getString("Marks");

                        model.addRow(new Object[]{studentId, studentName, marks});
                    }

                    table.setModel(model);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while fetching student data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    


    public void openAssignMarksPage() {
        AssignMarks assignMarks = new AssignMarks(teacherId);
        assignMarks.setVisible(true);
        dispose();
    }
}
