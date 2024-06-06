package CMS.Admin;

import java.awt.EventQueue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ManageResult extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JButton btnNewButton_1;
    private static String adminID;
    private static String adminName;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManageResult frame = new ManageResult();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ManageResult() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 738, 394);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(17, 50, 715, 268);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        JButton btnNewButton = new JButton("Approve Result");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ApproveResult a = new ApproveResult();
        		a.setVisible(true);
        		dispose();
        	}
        });
        btnNewButton.setBounds(308, 330, 199, 29);
        contentPane.add(btnNewButton);
        
        btnNewButton_1 = new JButton("Back");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		AdminDashboard a = new AdminDashboard(adminID, adminName);
        		a.setVisible(true);
        		dispose();
        	}
        });
        btnNewButton_1.setBounds(615, 9, 117, 29);
        contentPane.add(btnNewButton_1);

        // Fetch data from the database and populate the table
        fetchData();
    }

    private void fetchData() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CMS", "root", "")) {
            String sql = "SELECT marks.MarkID, student.StudentID, student.StudentName, courses.CourseName, " +
                         "modules.ModuleName, marks.Marks, marks.Approve " +
                         "FROM marks " +
                         "JOIN student ON marks.StudentID = student.StudentID " +
                         "JOIN courses ON marks.CourseID = courses.CourseID " +
                         "JOIN modules ON marks.ModuleID = modules.ModuleID";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // Create a table model with appropriate columns
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("S.N");
            model.addColumn("StudentID");
            model.addColumn("StudentName");
            model.addColumn("CourseName");
            model.addColumn("ModuleName");
            model.addColumn("Marks");
            model.addColumn("Approve");

            // Populate the table model with data from the ResultSet
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MarkID"),
                    rs.getString("StudentID"),
                    rs.getString("StudentName"),
                    rs.getString("CourseName"),
                    rs.getString("ModuleName"),
                    rs.getString("Marks"),
                    rs.getString("Approve")
                };
                model.addRow(row);
            }

            // Set the table model to the JTable
            table.setModel(model);

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
