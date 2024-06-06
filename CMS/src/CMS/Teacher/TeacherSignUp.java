package CMS.Teacher;

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
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import CMS.Welcome;

public class TeacherSignUp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtID;
    private JTextField txtName;
    private JTextField txtContact;
    private JPasswordField txtPass;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TeacherSignUp frame = new TeacherSignUp();
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
    public TeacherSignUp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Teacher ID");
        lblNewLabel.setBounds(49, 37, 84, 16);
        contentPane.add(lblNewLabel);

        txtID = new JTextField();
        txtID.setBounds(175, 32, 207, 26);
        contentPane.add(txtID);
        txtID.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Full Name");
        lblNewLabel_1.setBounds(49, 75, 92, 16);
        contentPane.add(lblNewLabel_1);

        txtName = new JTextField();
        txtName.setBounds(175, 70, 207, 26);
        contentPane.add(txtName);
        txtName.setColumns(10);

        JLabel lblNewLabel_1_1 = new JLabel("Contact");
        lblNewLabel_1_1.setBounds(49, 119, 92, 16);
        contentPane.add(lblNewLabel_1_1);

        txtContact = new JTextField();
        txtContact.setColumns(10);
        txtContact.setBounds(175, 114, 207, 26);
        contentPane.add(txtContact);

        txtPass = new JPasswordField();
        txtPass.setBounds(175, 153, 207, 26);
        contentPane.add(txtPass);

        JLabel lblNewLabel_1_1_1 = new JLabel("Create Password");
        lblNewLabel_1_1_1.setBounds(49, 158, 119, 16);
        contentPane.add(lblNewLabel_1_1_1);

        JButton btnNewButton = new JButton("Sign up");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Connecting to database.....");
                try {
                    // Database connection
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
                    System.out.println("Connected to database");

                    // Get password as a String
                    char[] passwordChars = txtPass.getPassword();
                    String password = new String(passwordChars);

                    // Check if all fields are properly filled
                    if (txtID.getText().isEmpty() || txtName.getText().isEmpty() || txtContact.getText().isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Check if the teacher already exists in the database
                        String checkIfExistsQuery = "SELECT * FROM Teachers WHERE TeacherID = ?";
                        PreparedStatement checkIfExistsStmt = conn.prepareStatement(checkIfExistsQuery);
                        checkIfExistsStmt.setString(1, txtID.getText());
                        ResultSet resultSet = checkIfExistsStmt.executeQuery();

                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(null, "Teacher with ID " + txtID.getText() + " already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Insert the teacher data into the database
                            String insertQuery = "INSERT INTO Teachers (TeacherID, TeacherName, Contact, Password) VALUES (?, ?, ?, ?)";
                            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                            insertStmt.setString(1, txtID.getText());
                            insertStmt.setString(2, txtName.getText());
                            insertStmt.setString(3, txtContact.getText());
                            insertStmt.setString(4, password);

                            // Execute the insert statement
                            int rowsAffected = insertStmt.executeUpdate();

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, "Teacher signed up successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                // Show login page
                                TeacherLogIn l = new TeacherLogIn();
                                dispose();
                                l.setVisible(true);
                                
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to sign up teacher", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (Exception e1) {
                    System.out.println(e1);
                }
            }
        });
        btnNewButton.setBounds(167, 196, 117, 29);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Already have an account?");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TeacherLogIn w = new TeacherLogIn();
                w.setVisible(true);
                dispose();
            }
        });
        btnNewButton_1.setBounds(121, 237, 207, 29);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Home");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Welcome w = new Welcome();
                w.setVisible(true);
                dispose();
            }
        });
        btnNewButton_2.setBounds(380, 6, 64, 20);
        contentPane.add(btnNewButton_2);
    }
}
