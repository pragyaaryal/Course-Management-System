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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import CMS.Welcome;

public class AdminSignUp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtID;
    private JTextField txtName;
    private JTextField txtContact;
    private JPasswordField txtPass;

    // Constructor
    public AdminSignUp() {
        setTitle("Admin Sign up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Admin ID");
        lblNewLabel.setBounds(28, 34, 61, 16);
        contentPane.add(lblNewLabel);
        
        txtID = new JTextField();
        txtID.setBounds(150, 29, 184, 26);
        contentPane.add(txtID);
        txtID.setColumns(10);
        
        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setBounds(28, 75, 84, 16);
        contentPane.add(lblFullName);
        
        JLabel lblNewLabel_1_1 = new JLabel("Contact");
        lblNewLabel_1_1.setBounds(28, 120, 61, 16);
        contentPane.add(lblNewLabel_1_1);
        
        JLabel lblNewLabel_1_1_1 = new JLabel("Create Password");
        lblNewLabel_1_1_1.setBounds(28, 165, 110, 16);
        contentPane.add(lblNewLabel_1_1_1);
        
        txtName = new JTextField();
        txtName.setColumns(10);
        txtName.setBounds(150, 67, 184, 26);
        contentPane.add(txtName);
        
        txtContact = new JTextField();
        txtContact.setColumns(10);
        txtContact.setBounds(150, 115, 184, 26);
        contentPane.add(txtContact);
        
        JButton btnNewButton = new JButton("Sign up");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Database connection
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");

                    // Get password as a String
                    char[] passwordChars = txtPass.getPassword();
                    String password = new String(passwordChars);

                    // Check if all fields are properly filled
                    if(txtID.getText().isEmpty() || txtName.getText().isEmpty() || txtContact.getText().isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Check if the admin already exists in the database
                        String checkIfExistsQuery = "SELECT * FROM admin WHERE AdminID = ?";
                        PreparedStatement checkIfExistsStmt = conn.prepareStatement(checkIfExistsQuery);
                        checkIfExistsStmt.setString(1, txtID.getText());
                        ResultSet resultSet = checkIfExistsStmt.executeQuery();

                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(null, "Admin with ID " + txtID.getText() + " already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Insert the admin data into the database
                            String insertQuery = "INSERT INTO admin (AdminID, FullName, Contact, Password) VALUES (?, ?, ?, ?)";
                            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                            insertStmt.setString(1, txtID.getText());
                            insertStmt.setString(2, txtName.getText());
                            insertStmt.setString(3, txtContact.getText());
                            insertStmt.setString(4, password);

                            // Execute the insert statement
                            int rowsAffected = insertStmt.executeUpdate();

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, "Admin signed up successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                // Show login page
                                AdminLogIn l = new AdminLogIn();
                                l.setVisible(true);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to sign up admin", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                    // Close connections
                    conn.close();
                } catch (Exception e1) {
                    System.out.println(e1);
                }
            }
        });
        btnNewButton.setBounds(160, 198, 117, 29);
        contentPane.add(btnNewButton);
        
        JButton btnLogIn = new JButton("Already have an account?");
        btnLogIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminLogIn s = new AdminLogIn();
                dispose();
                s.setVisible(true);
            }
        });
        btnLogIn.setBounds(122, 237, 202, 29);
        contentPane.add(btnLogIn);
        
        txtPass = new JPasswordField();
        txtPass.setBounds(150, 160, 184, 26);
        contentPane.add(txtPass);
        
        JButton btnNewButton_1 = new JButton("Home");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Welcome w = new Welcome();
                w.setVisible(true);
                dispose();
            }
        });
        btnNewButton_1.setBounds(383, 6, 61, 26);
        contentPane.add(btnNewButton_1);
    }
}
