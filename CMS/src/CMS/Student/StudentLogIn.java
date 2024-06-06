package CMS.Student;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import CMS.Welcome;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class StudentLogIn extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtID;
	private JPasswordField txtPass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentLogIn frame = new StudentLogIn();
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
	public StudentLogIn() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 396, 297);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Enter your Student ID");
		lblNewLabel.setBounds(43, 34, 225, 26);
		contentPane.add(lblNewLabel);
		
		txtID = new JTextField();
		txtID.setBounds(37, 62, 231, 26);
		contentPane.add(txtID);
		txtID.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(43, 100, 225, 26);
		contentPane.add(lblPassword);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(43, 124, 225, 26);
		contentPane.add(txtPass);
		
		
		JButton btnNewButton = new JButton("Log in");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String user = txtID.getText();
				char[] passwordChars = txtPass.getPassword();
				String pass = new String(passwordChars);

				try {
				    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
				    System.out.println("Database connection established");

				    // Prepare the SQL statement using a prepared statement
				    String sql = "SELECT * FROM student WHERE StudentID=? AND Password=?";
				    PreparedStatement stmt = conn.prepareStatement(sql);
				    stmt.setString(1, user);
				    stmt.setString(2, pass);
				    
				    // Execute the query
				    ResultSet rs = stmt.executeQuery();
				    
				    
				    if (rs.next()) {
				        System.out.println("Login successful!");
				        JOptionPane.showMessageDialog(null, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
				        
				        // Open StudentDashboard if login is successful
				        StudentDashboard d = new StudentDashboard(txtID.getText()); // Pass the student ID
				        dispose();
				        d.setVisible(true); 
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
				}
			}
		});
		btnNewButton.setBounds(122, 177, 117, 29);
		contentPane.add(btnNewButton);
		
		JButton btnSignUp = new JButton("Dont have an account?");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StudentSignUp s = new StudentSignUp();
				s.setVisible(true);
				dispose();
				
			}
		});
		btnSignUp.setBounds(82, 218, 206, 29);
		contentPane.add(btnSignUp);
		
		JButton btnNewButton_1 = new JButton("Home");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Welcome w = new Welcome();
				w.setVisible(true);
				dispose();
			}
		});
		btnNewButton_1.setBounds(315, 6, 74, 29);
		contentPane.add(btnNewButton_1);
		
		
	}
}
