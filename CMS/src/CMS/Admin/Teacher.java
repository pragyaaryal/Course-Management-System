package CMS.Admin;

import java.awt.EventQueue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Teacher extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable teacherTable;
    private static String adminID;
    private static String adminName;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Teacher frame = new Teacher(adminID, adminName);
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
    public Teacher(String adminID, String adminName) {
        this.adminID = adminID;
        this.adminName = adminName;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 709, 445);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        teacherTable = new JTable();
        scrollPane.setViewportView(teacherTable);

        JButton assignModuleButton = new JButton("Assign Module");
        assignModuleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AssignModule assignModuleFrame = new AssignModule(Teacher.this);
                assignModuleFrame.setVisible(true);
                dispose();
            }
        });
        contentPane.add(assignModuleButton, BorderLayout.SOUTH);
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminDashboard adminDashboard = new AdminDashboard(adminID, adminName);
                adminDashboard.setVisible(true);
                dispose();
            }
        });
        contentPane.add(backButton, BorderLayout.NORTH);

        fetchTeacherData();
    }

    private void fetchTeacherData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Teacher Name");
        model.addColumn("Teacher ID");
        model.addColumn("Module ID");
        model.addColumn("Contact");
        model.addColumn("Course ID");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            String sql = "SELECT t.TeacherName AS TeacherName, t.TeacherID, t.ModuleID, t.Contact, c.CourseID FROM Teachers t LEFT JOIN Modules m ON t.ModuleID = m.ModuleID LEFT JOIN Courses c ON t.CourseID = c.CourseID";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String teacherName = rs.getString("TeacherName");
                    String teacherID = rs.getString("TeacherID");
                    String moduleID = rs.getString("ModuleID");
                    String contact = rs.getString("Contact");
                    String courseID = rs.getString("CourseID");
                    model.addRow(new Object[]{teacherName, teacherID, moduleID, contact, courseID});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch teacher data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        teacherTable.setModel(model);
    }

    public void refreshTeacherData() {
        fetchTeacherData();
    }
}
