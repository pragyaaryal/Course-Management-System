package CMS.Admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManageModule extends JFrame {

    private JPanel contentPane;
    private JTextField ModuleID;
    private JTextField ModuleName;
    private JComboBox<String> CourseID;
    private JComboBox<String> moduleChoiceComboBox; // Added moduleChoiceComboBox
    private String selectedCourse;
    private String adminID;
    private String adminName;


    public ManageModule(String selectedCourse, String adminID, String adminName) {
        this.selectedCourse = selectedCourse;
        this.adminID = adminID;
        this.adminName = adminName;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 454, 316);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        contentPane.setLayout(null);

        JButton btnNewButton = new JButton("Back");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Module editCourseFrame = new Module(selectedCourse, adminID, adminName);
                dispose(); // Close the AddModule frame
                editCourseFrame.setVisible(true);
            }
        });
        btnNewButton.setBounds(363, 6, 87, 29);
        contentPane.add(btnNewButton);

        JLabel lblNewLabel = new JLabel("Module Name ");
        lblNewLabel.setBounds(24, 89, 111, 16);
        contentPane.add(lblNewLabel);

        ModuleID = new JTextField();
        ModuleID.setBounds(161, 39, 217, 31);
        contentPane.add(ModuleID);
        ModuleID.setColumns(10);

        ModuleName = new JTextField();
        ModuleName.setColumns(10);
        ModuleName.setBounds(161, 82, 217, 31);
        contentPane.add(ModuleName);

        JLabel lblModuleId = new JLabel("Module ID");
        lblModuleId.setBounds(24, 39, 111, 31);
        contentPane.add(lblModuleId);

        JLabel lblLevel = new JLabel("Level");
        lblLevel.setBounds(24, 163, 111, 16);
        contentPane.add(lblLevel);

        JLabel lblCourseId = new JLabel("Course ID");
        lblCourseId.setBounds(24, 201, 111, 16);
        contentPane.add(lblCourseId);

        JComboBox<String> level = new JComboBox<String>();
        level.setModel(new DefaultComboBoxModel<String>(new String[]{"4", "5", "6"}));
        level.setBounds(161, 159, 217, 27);
        contentPane.add(level);

        CourseID = new JComboBox<String>();
        CourseID.setBounds(161, 197, 217, 27);
        contentPane.add(CourseID);

        // Populate the CourseID combo box with available course IDs
        fetchCourseIDs();

        JButton AddModuleButton = new JButton("Add Module ");
        AddModuleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the values entered by the user
                String moduleID = ModuleID.getText();
                String moduleName = ModuleName.getText();
                String selectedLevel = (String) level.getSelectedItem();
                String selectedCourseID = (String) CourseID.getSelectedItem();
                String selectedModuleChoice = (String) moduleChoiceComboBox.getSelectedItem(); // Get selected module choice

                // Insert module details into the Modules table
                insertModule(moduleID, moduleName, selectedLevel, selectedCourseID, selectedModuleChoice);
            }
        });
        AddModuleButton.setBounds(97, 240, 117, 29);
        contentPane.add(AddModuleButton);

        JButton btnNewButton_1 = new JButton("Delete Module");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the values entered by the user
                String moduleID = ModuleID.getText();
                String moduleName = ModuleName.getText();
                String selectedLevel = (String) level.getSelectedItem();
                String selectedCourseID = (String) CourseID.getSelectedItem();
                String selectedModuleChoice = (String) moduleChoiceComboBox.getSelectedItem(); // Get selected module choice

                // Check if all necessary fields are filled
                if (moduleID.isEmpty() || moduleName.isEmpty() || selectedLevel.isEmpty() || selectedCourseID.isEmpty()) {
                    JOptionPane.showMessageDialog(contentPane, "Please fill all fields before deleting.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Confirm deletion with user
                int option = JOptionPane.showConfirmDialog(contentPane, "Are you sure you want to delete this module?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // Proceed with deletion
                    deleteModule(moduleID);
                }
            }
        });
        btnNewButton_1.setBounds(243, 240, 117, 29);
        contentPane.add(btnNewButton_1);

        JLabel lblNewLabel_1 = new JLabel("Module Choice");
        lblNewLabel_1.setBounds(23, 124, 111, 16);
        contentPane.add(lblNewLabel_1);

        // Added moduleChoiceComboBox
        moduleChoiceComboBox = new JComboBox();
        moduleChoiceComboBox.setModel(new DefaultComboBoxModel(new String[]{"Optional", "Compulsory"}));
        moduleChoiceComboBox.setBounds(161, 120, 211, 27);
        contentPane.add(moduleChoiceComboBox);
    }

    // Method to fetch course IDs from the database and populate the CourseID combo box
    private void fetchCourseIDs() {
        // Your database connection details
        String url = "jdbc:mysql://localhost:3306/CMS";
        String username = "root";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT CourseID FROM Courses";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String courseID = rs.getString("CourseID");
                CourseID.addItem(courseID);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to insert module details into the Modules table
 // Method to insert module details into the Modules table and automatically enroll students if the module is compulsory
    private void insertModule(String moduleID, String moduleName, String level, String courseID, String moduleChoice) {
        // Your database connection details
        String url = "jdbc:mysql://localhost:3306/CMS";
        String username = "root";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String checkIfExistsQuery = "SELECT * FROM Modules WHERE ModuleID = ? OR ModuleName = ?";
            PreparedStatement checkIfExistsStmt = conn.prepareStatement(checkIfExistsQuery);
            checkIfExistsStmt.setString(1, moduleID);
            checkIfExistsStmt.setString(2, moduleName);
            ResultSet rs = checkIfExistsStmt.executeQuery();

            if (rs.next()) {
                // Module with the same ID or Name already exists
                // Display an error message
                JOptionPane.showMessageDialog(this, "Module ID or Name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Module does not exist, proceed with insertion
                String insertQuery = "INSERT INTO Modules (ModuleID, ModuleName, Level, CourseID, ModuleChoice, Enroll) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, moduleID);
                insertStmt.setString(2, moduleName);
                insertStmt.setString(3, level);
                insertStmt.setString(4, courseID);
                insertStmt.setString(5, moduleChoice); // Insert module choice
                
                // Automatically enroll students if the module is compulsory
                if (moduleChoice.equalsIgnoreCase("Compulsory")) {
                    insertStmt.setString(6, "Yes");
                } else {
                    insertStmt.setString(6, "No");
                }
                
                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Module added successfully
                    // Display a success message
                    JOptionPane.showMessageDialog(this, "Module added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Clear the fields
                    ModuleID.setText("");
                    ModuleName.setText("");
                } else {
                    // Something went wrong with the insertion
                    // Display an error message
                    JOptionPane.showMessageDialog(this, "Failed to add module.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                insertStmt.close();
            }

            rs.close();
            checkIfExistsStmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to delete module from the database
    private void deleteModule(String moduleID) {
        // Your database connection details
        String url = "jdbc:mysql://localhost:3306/CMS";
        String username = "root";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String deleteQuery = "DELETE FROM Modules WHERE ModuleID = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
            deleteStmt.setString(1, moduleID);
            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected > 0) {
                // Module deleted successfully
                JOptionPane.showMessageDialog(contentPane, "Module deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear the fields
                ModuleID.setText("");
                ModuleName.setText("");
            } else {
                // Module with the given ID not found
                JOptionPane.showMessageDialog(contentPane, "Module not found with ID: " + moduleID, "Error", JOptionPane.ERROR_MESSAGE);
            }

            deleteStmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
