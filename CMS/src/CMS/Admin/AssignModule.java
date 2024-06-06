// AssignModule.java
package CMS.Admin;

import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AssignModule extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField TeacherID;
    private JTextField TeacherName;
    private JComboBox<String> courseIDComboBox;
    private JComboBox<String> moduleIDComboBox;
    private Teacher teacherFrame;
    private static String adminID;
    private static String adminName;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AssignModule frame = new AssignModule(null); 
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
    public AssignModule(Teacher teacherFrame) {
        this.teacherFrame = teacherFrame;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 461, 309);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Teacher ID");
        lblNewLabel.setBounds(20, 32, 99, 31);
        contentPane.add(lblNewLabel);

        TeacherID = new JTextField();
        TeacherID.setBounds(131, 33, 214, 29);
        contentPane.add(TeacherID);
        TeacherID.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Teacher Name ");
        lblNewLabel_1.setBounds(20, 81, 112, 16);
        contentPane.add(lblNewLabel_1);

        TeacherName = new JTextField();
        TeacherName.setColumns(10);
        TeacherName.setBounds(131, 76, 214, 29);
        contentPane.add(TeacherName);

        JLabel lblNewLabel_1_1_1 = new JLabel("Course ID");
        lblNewLabel_1_1_1.setBounds(20, 124, 112, 16);
        contentPane.add(lblNewLabel_1_1_1);

        courseIDComboBox = new JComboBox<String>();
        courseIDComboBox.setBounds(131, 119, 214, 27);
        contentPane.add(courseIDComboBox);
        // Fetch course IDs from the database and add them to the combo box
        fetchCourseIDs();

        JLabel lblNewLabel_1_1_1_1 = new JLabel("Module ID");
        lblNewLabel_1_1_1_1.setBounds(20, 166, 112, 16);
        contentPane.add(lblNewLabel_1_1_1_1);

        moduleIDComboBox = new JComboBox<String>();
        moduleIDComboBox.setBounds(131, 161, 214, 27);
        contentPane.add(moduleIDComboBox);
        
        // Action listener to fetch and populate module IDs based on selected course ID
        courseIDComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCourseID = (String) courseIDComboBox.getSelectedItem();
                fetchModuleIDs(selectedCourseID);
            }
        });

        JButton UpdateBtn = new JButton("Update");
        UpdateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the input values
                String teacherID = TeacherID.getText();
                String teacherName = TeacherName.getText();
                String courseID = (String) courseIDComboBox.getSelectedItem();
                String moduleID = (String) moduleIDComboBox.getSelectedItem();

                // Check if the module exists for the selected course
                if (!moduleExists(moduleID, courseID)) {
                    JOptionPane.showMessageDialog(null, "Module does not exist for the selected course. Cannot update information.");
                    return;
                }

                // Update the module ID for the existing teacher
                updateTeacherModule(teacherID, teacherName, moduleID, courseID);
            }
        });
        UpdateBtn.setBounds(90, 218, 117, 29);
        contentPane.add(UpdateBtn);

       
        JButton DeleteBtn = new JButton("Delete");
        DeleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the input values
                String teacherID = TeacherID.getText();
                String teacherName = TeacherName.getText();

                // Check if the teacher exists
                if (teacherExists(teacherID, teacherName)) {
                    // Delete the assigned module from the teacher
                    deleteTeacherModule(teacherID, teacherName);
                } else {
                    // If the teacher doesn't exist, display an error message
                    JOptionPane.showMessageDialog(null, "Teacher does not exist. Cannot delete module.");
                }
            }
        });
        DeleteBtn.setBounds(228, 218, 117, 29);
        contentPane.add(DeleteBtn);

        JButton btnNewButton = new JButton("Back");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a new instance of the Teacher frame
                Teacher teacher = new Teacher(adminName, adminID);
                // Refresh the data in the Teacher frame
                teacher.refreshTeacherData();
                // Make the Teacher frame visible
                teacher.setVisible(true);
                // Dispose of the current AssignModule frame
                dispose();
            }
        });
        btnNewButton.setBounds(357, 6, 97, 29);
        contentPane.add(btnNewButton);

    }

    // Method to check if the teacher already exists in the database
    private boolean teacherExists(String teacherID, String teacherName) {
        String sql = "SELECT * FROM Teachers WHERE TeacherID = ? AND TeacherName = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacherID);
            stmt.setString(2, teacherName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If the result set has at least one row, the teacher exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to check if the module exists for the selected course
    private boolean moduleExists(String moduleID, String courseID) {
        String sql = "SELECT * FROM Modules WHERE ModuleID = ? AND CourseID = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, moduleID);
            stmt.setString(2, courseID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // If the result set has at least one row, the module exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

 // Method to update the module for an existing teacher
    private void updateTeacherModule(String teacherID, String teacherName, String moduleID, String courseID) {
        // Check if the teacher exists
        if (!teacherExists(teacherID, teacherName)) {
            JOptionPane.showMessageDialog(null, "Teacher does not exist. Cannot update module.");
            return;
        }

        // Update the Teachers table
        String updateTeacherSql = "UPDATE Teachers SET ModuleID = ?, CourseID = ? WHERE TeacherID = ? AND TeacherName = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
             PreparedStatement stmt = conn.prepareStatement(updateTeacherSql)) {
            stmt.setString(1, moduleID);
            stmt.setString(2, courseID);
            stmt.setString(3, teacherID);
            stmt.setString(4, teacherName);
            stmt.executeUpdate();

            // Insert or update the Modules table
            String moduleSql = "INSERT INTO Modules (ModuleID, CourseID, TeacherID) VALUES (?, ?, ?) " +
                               "ON DUPLICATE KEY UPDATE TeacherID = ?";
            try (PreparedStatement moduleStmt = conn.prepareStatement(moduleSql)) {
                moduleStmt.setString(1, moduleID);
                moduleStmt.setString(2, courseID);
                moduleStmt.setString(3, teacherID);
                moduleStmt.setString(4, teacherID); // Update TeacherID if duplicate key found
                moduleStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Teacher information updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to delete the assigned module from the teacher
    private void deleteTeacherModule(String teacherID, String teacherName) {
        // Check if the teacher exists
        if (!teacherExists(teacherID, teacherName)) {
            JOptionPane.showMessageDialog(null, "Teacher does not exist. Cannot delete module.");
            return;
        }

        String sql = "UPDATE Teachers SET ModuleID = NULL WHERE TeacherID = ? AND TeacherName = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, teacherID);
            stmt.setString(2, teacherName);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Module deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to fetch course IDs from the database and add them to the combo box
    private void fetchCourseIDs() {
        String sql = "SELECT CourseID FROM Courses";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String courseID = rs.getString("CourseID");
                courseIDComboBox.addItem(courseID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch course IDs from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to fetch module IDs based on the selected course ID and add them to the combo box
    private void fetchModuleIDs(String selectedCourseID) {
        String sql = "SELECT ModuleID FROM Modules WHERE CourseID = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, selectedCourseID);
            try (ResultSet rs = stmt.executeQuery()) {
                moduleIDComboBox.removeAllItems();
                while (rs.next()) {
                    String moduleID = rs.getString("ModuleID");
                    moduleIDComboBox.addItem(moduleID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch module IDs from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
