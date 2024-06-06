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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import CMS.Welcome;

public class AdminLogIn extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtID;
    private JPasswordField txtPass;

    // Constructor
    public AdminLogIn() {
        setTitle("Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 380, 284);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Enter your Admin Id");
        lblNewLabel.setBounds(43, 34, 225, 26);
        contentPane.add(lblNewLabel);
        
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(43, 100, 225, 26);
        contentPane.add(lblPassword);
        
        JButton btnNewButton = new JButton("Log in");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = txtID.getText();
                char[] passwordChars = txtPass.getPassword();
                String pass = new String(passwordChars);

                try {
                    // Database connection
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
                    System.out.println("Database connection established");

                    // Prepare the SQL statement using a prepared statement
                    String sql = "SELECT * FROM admin WHERE AdminID=? AND Password=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, user);
                    stmt.setString(2, pass);
                    
                    // Execute the query
                    ResultSet rs = stmt.executeQuery();
                    
                    if (rs.next()) {
                        System.out.println("Login successful!");
                        JOptionPane.showMessageDialog(null, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Retrieve the Admin Name from the result set
                        String adminName = rs.getString("FullName");
                      
                        // Pass the logged-in user's ID and name to AdminDashboard
                        String adminID = rs.getString("AdminID");
                        AdminDashboard d = new AdminDashboard(adminID, adminName); // Pass the ID and name to AdminDashboard constructor
                        dispose();
                        d.setVisible(true); 
                        
                        // Close the login frame
                        dispose();
                        
                    } else {
                        // Invalid credentials, display a message
                        JOptionPane.showMessageDialog(null, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    
                    // Close the connections
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e1) {
                    System.out.println("SQL Exception: " + e1.getMessage());
                    e1.printStackTrace();
                }
            }
        });
        btnNewButton.setBounds(115, 174, 117, 29);
        contentPane.add(btnNewButton);
        
        JButton btnSignUp = new JButton("Don't have an account?");
        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AdminSignUp s = new AdminSignUp();
                s.setVisible(true);
                dispose();
            }
        });
        btnSignUp.setBounds(76, 215, 204, 29);
        contentPane.add(btnSignUp);
        
        txtID = new JTextField();
        txtID.setBounds(43, 62, 225, 26);
        contentPane.add(txtID);
        txtID.setColumns(10);
        
        JButton btnNewButton_1 = new JButton("Home");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Welcome w = new Welcome();
                w.setVisible(true);
                dispose();
            }
        });
        btnNewButton_1.setBounds(278, 16, 77, 16);
        contentPane.add(btnNewButton_1);
        
        txtPass = new JPasswordField();
        txtPass.setBounds(43, 126, 225, 26);
        contentPane.add(txtPass);
    }

    // Main method to launch the application
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminLogIn frame = new AdminLogIn();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
