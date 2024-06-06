package CMS.Admin;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Module extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private String selectedCourse;
    private String adminID;
    private String adminName;

    public Module(String selectedCourse, String adminID, String adminName) {
        this.selectedCourse = selectedCourse;
        this.adminID = adminID;
        this.adminName = adminName;
        initComponents();
    }

    private void initComponents() {
        setTitle("Edit Course - " + selectedCourse);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 614, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel courseNameLabel = new JLabel("Course Name: " + selectedCourse);
        infoPanel.add(courseNameLabel);
        String courseID = fetchCourseID(selectedCourse);
        JLabel courseIDLabel = new JLabel("Course ID: " + courseID);
        infoPanel.add(courseIDLabel);
        contentPane.add(infoPanel, BorderLayout.NORTH);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        JButton manageModuleButton = new JButton("Manage Module");
        manageModuleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openManageModule();
            }
        });
        buttonPanel.add(manageModuleButton);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                goBackToDashboard();
            }
        });
        buttonPanel.add(backButton);

        loadDataIntoTable(courseID);
    }

    private String fetchCourseID(String selectedCourse) {
        String courseID = "";
        String url = "jdbc:mysql://localhost:3306/CMS";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT CourseID FROM courses WHERE CourseName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, selectedCourse);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        courseID = rs.getString("CourseID");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch course ID from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return courseID;
    }

    private void loadDataIntoTable(String courseID) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Module Name");
        model.addColumn("Module ID");
        model.addColumn("Level");

        String url = "jdbc:mysql://localhost:3306/CMS";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT ModuleName, ModuleID, Level FROM Modules WHERE CourseID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, courseID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String moduleName = rs.getString("ModuleName");
                        String moduleID = rs.getString("ModuleID");
                        String level = rs.getString("Level");
                        model.addRow(new Object[]{moduleName, moduleID, level});
                    }
                }
            }
            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch module data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openManageModule() {
        ManageModule manageModule = new ManageModule(selectedCourse, adminID, adminName);
        manageModule.setVisible(true);
        dispose();
    }

    private void goBackToDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard(adminID, adminName);
        adminDashboard.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                String selectedCourse = "Some Course";
                String adminID = "admin123";
                String adminName = "Admin Name";
                Module frame = new Module(selectedCourse, adminID, adminName);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
