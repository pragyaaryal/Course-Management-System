package CMS.Student;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import CMS.Welcome;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class StudentSignUp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtID;
    private JTextField txtName;
    private JPasswordField txtPass;
    private JTextField txtContact;
    private JComboBox<String> comboBoxCourse;
    private JComboBox<String> comboBoxLevel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    StudentSignUp frame = new StudentSignUp();
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
    public StudentSignUp() {
        setTitle("Student Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 485, 374);
        contentPane = new JPanel();
        contentPane.setBackground(Color.LIGHT_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblStudentID = new JLabel("Student ID");
        lblStudentID.setFont(new Font("Arial", Font.BOLD, 14));
        lblStudentID.setBounds(54, 25, 86, 42);
        contentPane.add(lblStudentID);
        
        txtID = new JTextField();
        txtID.setFont(new Font("Arial", Font.PLAIN, 13));
        txtID.setHorizontalAlignment(SwingConstants.LEFT);
        txtID.setColumns(10);
        txtID.setBounds(199, 30, 191, 26);
        contentPane.add(txtID);
        
        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setFont(new Font("Arial", Font.BOLD, 14));
        lblFullName.setBounds(58, 57, 86, 42);
        contentPane.add(lblFullName);
        
        txtName = new JTextField();
        txtName.setFont(new Font("Arial", Font.PLAIN, 13));
        txtName.setColumns(10);
        txtName.setBounds(200, 65, 189, 26);
        contentPane.add(txtName);
        
        JLabel lblLevel = new JLabel("Level");
        lblLevel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        lblLevel.setBounds(56, 105, 86, 24);
        contentPane.add(lblLevel);
       
        
        comboBoxLevel = new JComboBox();
        comboBoxLevel.setModel(new DefaultComboBoxModel(new String[] {"4", "5", "6"}));
        comboBoxLevel.setBounds(196, 103, 194, 27);
        contentPane.add(comboBoxLevel);
        
        JLabel lblCourse = new JLabel("Course");
        lblCourse.setFont(new Font("Arial", Font.BOLD, 14));
        lblCourse.setBounds(57, 145, 61, 16);
        contentPane.add(lblCourse);
        
        comboBoxCourse = new JComboBox<String>();
        comboBoxCourse.setBounds(196, 139, 186, 27);
        contentPane.add(comboBoxCourse);
        populateCourseComboBox();
        
        JLabel lblCreatePassword = new JLabel("Create Password");
        lblCreatePassword.setFont(new Font("Times New Roman", Font.BOLD, 14));
        lblCreatePassword.setBounds(58, 213, 118, 42);
        contentPane.add(lblCreatePassword);
        
        txtPass = new JPasswordField();
        txtPass.setBounds(200, 221, 180, 26);
        contentPane.add(txtPass);
        
        JLabel lblContact = new JLabel("Contact");
        lblContact.setBounds(60, 186, 61, 16);
        contentPane.add(lblContact);
        
        txtContact = new JTextField();
        txtContact.setColumns(10);
        txtContact.setBounds(203, 178, 179, 26);
        contentPane.add(txtContact);
        
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new ActionListener() {
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
                    if(txtID.getText().isEmpty() || txtName.getText().isEmpty() || txtContact.getText().isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Check if the student already exists in the database
                        String checkIfExistsQuery = "SELECT * FROM Student WHERE StudentID = ?";
                        PreparedStatement checkIfExistsStmt = conn.prepareStatement(checkIfExistsQuery);
                        checkIfExistsStmt.setString(1, txtID.getText());
                        ResultSet resultSet = checkIfExistsStmt.executeQuery();

                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(null, "Student with ID " + txtID.getText() + " already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                           

                            // Insert the student data into the database
                            String insertQuery = "INSERT INTO Student (StudentID, StudentName, Level, CourseID, Contact, Password) VALUES (?, ?, ?, ?, ?, ?)";
                            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                            insertStmt.setString(1, txtID.getText());
                            insertStmt.setString(2, txtName.getText());
                            insertStmt.setString(3, comboBoxLevel.getSelectedItem().toString());
                            
                         // Get the course ID based on the selected course name
                            String courseName = comboBoxCourse.getSelectedItem().toString();
                            String courseId = getCourseId(conn, courseName); // Ensure courseId is a String
                            insertStmt.setString(4, courseId); // Set the courseId as a String
                            
                            insertStmt.setString(5, txtContact.getText());
                            insertStmt.setString(6, password);

                            // Execute the insert statement
                            int rowsAffected = insertStmt.executeUpdate();

                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(null, "Student signed up successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                // Show login page
                                StudentLogIn l = new StudentLogIn();
                                dispose();
                                l.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to sign up student", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    conn.close();
                } catch (Exception e1) {
                    System.out.println(e1);
                }
            }
        });
        btnSubmit.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        btnSubmit.setBounds(97, 259, 109, 26);
        contentPane.add(btnSubmit);
        
        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtID.setText("");
                txtName.setText("");
                txtContact.setText("");
                txtPass.setText("");
            }
        });
        btnReset.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        btnReset.setBounds(263, 263, 109, 26);
        contentPane.add(btnReset);
        
        JButton btnLogin = new JButton("Already have an account?");
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setBackground(Color.GRAY);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StudentLogIn l = new StudentLogIn();
                l.setVisible(true);    
                dispose();
            }
        });
        btnLogin.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        btnLogin.setBounds(155, 301, 184, 26);
        contentPane.add(btnLogin);
        
        JButton btnHome = new JButton("Home");
        btnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Welcome w = new Welcome();
                w.setVisible(true);
                dispose();
            }
        });
        btnHome.setBounds(406, 15, 70, 18);
        contentPane.add(btnHome);
        
    }

    // Method to populate course names in the JComboBox
    private void populateCourseComboBox() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
            String query = "SELECT CourseName FROM Courses";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
            while (rs.next()) {
                String courseName = rs.getString("CourseName");
                model.addElement(courseName);
            }

            comboBoxCourse.setModel(model);

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to get the course ID based on the course name
    private String getCourseId(Connection conn, String courseName) throws SQLException {
        String query = "SELECT CourseID FROM Courses WHERE CourseName = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, courseName);
        ResultSet rs = pstmt.executeQuery();

        String courseId = "";
        if (rs.next()) {
            courseId = rs.getString("CourseID");
        }

        rs.close();
        pstmt.close();

        return courseId;
    }
}
