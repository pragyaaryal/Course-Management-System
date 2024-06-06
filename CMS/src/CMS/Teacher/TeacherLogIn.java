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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import CMS.Welcome;

public class TeacherLogIn extends JFrame {

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
                    TeacherLogIn frame = new TeacherLogIn();
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
    public TeacherLogIn() {
        setTitle("Teacher Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 372, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Enter your teacher ID");
        lblNewLabel.setBounds(34, 38, 224, 16);
        contentPane.add(lblNewLabel);

        txtID = new JTextField();
        txtID.setBounds(29, 66, 229, 26);
        contentPane.add(txtID);
        txtID.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(34, 111, 224, 16);
        contentPane.add(lblPassword);

        txtPass = new JPasswordField();
        txtPass.setBounds(34, 139, 229, 26);
        contentPane.add(txtPass);

        JButton btnNewButton = new JButton("Log in");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String teacherID = txtID.getText();
                String pass = new String(txtPass.getPassword());

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "");
                    System.out.println("Database connection established");

                    String sql = "SELECT * FROM Teachers WHERE TeacherID=? AND Password=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, teacherID);
                    stmt.setString(2, pass);

                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        System.out.println("Login successful!");
                        JOptionPane.showMessageDialog(null, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                        TeacherDashboard d = new TeacherDashboard(teacherID);
                        dispose();
                        d.setVisible(true); 
                        

                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (SQLException e1) {
                    System.out.println("SQL Exception: " + e1.getMessage());
                }
            }

        });
        btnNewButton.setBounds(121, 187, 117, 29);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Don't have an account?");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TeacherSignUp s = new TeacherSignUp();
                s.setVisible(true);
                dispose();
            }
        });
        btnNewButton_1.setBounds(74, 228, 216, 29);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Home");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Welcome w = new Welcome();
                dispose();
                w.setVisible(true);
                
            }
        });
        btnNewButton_2.setBounds(265, 6, 65, 26);
        contentPane.add(btnNewButton_2);
    }
}
