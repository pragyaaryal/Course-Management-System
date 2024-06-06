package CMS;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import CMS.Admin.AdminSignUp;
import CMS.Student.StudentSignUp;
import CMS.Teacher.TeacherSignUp;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Welcome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // Constructor
    public Welcome() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblNewLabel = new JLabel("Course Management System ");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblNewLabel.setBounds(98, 37, 277, 29);
        contentPane.add(lblNewLabel);
        
        // Button for Student Signup
        JButton btnNewButton = new JButton("Student");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    StudentSignUp sign = new StudentSignUp();
                    sign.setVisible(true);  
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }               
        });
        btnNewButton.setBounds(149, 137, 117, 29); 
        contentPane.add(btnNewButton);
        
        // Button for Admin Signup
        JButton btnNewButton_1 = new JButton("Admin");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    AdminSignUp sign = new AdminSignUp();
                    sign.setVisible(true);  
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnNewButton_1.setBounds(19, 137, 117, 29);
        contentPane.add(btnNewButton_1);
        
        // Button for Teacher Signup
        JButton btnTeacher = new JButton("Teacher");
        btnTeacher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    TeacherSignUp s = new TeacherSignUp();
                    s.setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnTeacher.setBounds(285, 137, 117, 29);
        contentPane.add(btnTeacher);
        
        JLabel lblNewLabel_1 = new JLabel("Sign up as");
        lblNewLabel_1.setFont(new Font("Lucida Grande", Font.ITALIC, 16));
        lblNewLabel_1.setBounds(177, 97, 75, 28);
        contentPane.add(lblNewLabel_1);
    }
    
    // Main method to launch the application
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Welcome frame = new Welcome();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
