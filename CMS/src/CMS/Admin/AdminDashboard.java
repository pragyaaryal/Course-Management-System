package CMS.Admin;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import CMS.Welcome;

import javax.swing.JMenuBar;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
public class AdminDashboard extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel lblAdminID;
    private JLabel lblAdminName;
    private JComboBox<String> courseComboBox;
    

    /**
     * Create the frame.
     */
    public AdminDashboard(String adminID, String adminName) {
    	setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 599, 380);
        getContentPane().setLayout(null);
        
        JButton btnNewButton_1 = new JButton("Teachers");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Teacher t = new Teacher(adminID, adminName);
        		dispose();
        		t.setVisible(true);
        		
        	}
        });
        btnNewButton_1.setBounds(50, 262, 200, 48);
        getContentPane().add(btnNewButton_1);
        
        JButton btnNewButton_2 = new JButton("Result slip");
        btnNewButton_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ManageResult m = new ManageResult();
        		m.setVisible(true);
        		dispose();
        	}
        });
        btnNewButton_2.setBounds(358, 259, 187, 54);
        getContentPane().add(btnNewButton_2);
        
        lblAdminID = new JLabel("Admin ID: " + adminID);
        lblAdminID.setBounds(22, 25, 166, 27);
        getContentPane().add(lblAdminID);
        
        lblAdminName = new JLabel("Admin Name: " + adminName);
        lblAdminName.setBounds(200, 30, 200, 16);
        getContentPane().add(lblAdminName);
        
        JLabel lblNewLabel = new JLabel("Manage Course");
        lblNewLabel.setBounds(187, 81, 166, 29);
        getContentPane().add(lblNewLabel);
        
        JLabel lblManageTeachers = new JLabel("Manage Teachers");
        lblManageTeachers.setBounds(95, 232, 166, 29);
        getContentPane().add(lblManageTeachers);
        
        JLabel lblManageResults = new JLabel("Manage Results");
        lblManageResults.setBounds(379, 231, 166, 29);
        getContentPane().add(lblManageResults);
        
        courseComboBox = new JComboBox<String>();
        courseComboBox.setBounds(19, 136, 187, 27);
        getContentPane().add(courseComboBox);
     // Fetch course names from the database and add them to the combo box
        fetchCourseNames();
        
        JButton btnNewButton = new JButton("Modules");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 String selectedCourse = (String) courseComboBox.getSelectedItem();
        	        if (selectedCourse != null) {
        	            Module editCourseFrame = new Module(selectedCourse, adminID, adminName);
        	            dispose(); // Close the AdminDashboard frame
        	            editCourseFrame.setVisible(true);
        	        } else {
        	            JOptionPane.showMessageDialog(AdminDashboard.this, "Please select a course first.", "Error", JOptionPane.ERROR_MESSAGE);
        	        }
        	}
        });
        btnNewButton.setBounds(218, 131, 117, 37);
        getContentPane().add(btnNewButton);
        
        
        
        JButton btnNewButton_3 = new JButton("Add/Delete course");
        btnNewButton_3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Add_DeleteCourse a = new Add_DeleteCourse ();
        		a.setVisible(true);
        		dispose();
        		
        	}
        });
        btnNewButton_3.setBounds(347, 131, 206, 37);
        getContentPane().add(btnNewButton_3);
        
        JButton btnNewButton_4 = new JButton("Home");
        btnNewButton_4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Welcome w = new Welcome();
        		w.setVisible(true);
        		dispose();
        	}
        });
        btnNewButton_4.setBounds(528, 6, 71, 27);
        getContentPane().add(btnNewButton_4);
    }

    private void fetchCourseNames() {
        try {
        	// Database connection
		    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
		    System.out.println("Database connection established");


            // Prepare SQL statement
            String sql = "SELECT CourseName FROM courses";
            PreparedStatement stmt = conn.prepareStatement(sql);
         // Execute the query
		    ResultSet rs = stmt.executeQuery();

           
            // Populate the combo box with course names
            while (rs.next()) {
                String courseName = rs.getString("CourseName");
                courseComboBox.addItem(courseName);
            }

            rs.close();
		    stmt.close();
		    conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch course names from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
            
}
