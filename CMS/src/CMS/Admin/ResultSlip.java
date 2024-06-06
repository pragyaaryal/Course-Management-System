package CMS.Admin;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class ResultSlip extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable ModuleMarksTable;
    private JLabel lblStudentID;
    private JLabel lblStudentName;
    private JLabel lblCourseID;
    private JLabel lblApprovalStatus;
    private JButton btnBack;

    /**
     * Create the frame.
     */
    public ResultSlip() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        lblStudentID = new JLabel("Student ID : ");
        lblStudentID.setBounds(26, 20, 250, 16);
        contentPane.add(lblStudentID);
        
        lblStudentName = new JLabel("Student Name : ");
        lblStudentName.setBounds(26, 48, 250, 16);
        contentPane.add(lblStudentName);
        
        lblCourseID = new JLabel("Course ID: ");
        lblCourseID.setBounds(281, 20, 250, 16);
        contentPane.add(lblCourseID);
        
        lblApprovalStatus = new JLabel("Approval Status: ");
        lblApprovalStatus.setBounds(281, 48, 250, 16);
        contentPane.add(lblApprovalStatus);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(26, 93, 550, 250);
        contentPane.add(scrollPane);
        
        ModuleMarksTable = new JTable();
        scrollPane.setViewportView(ModuleMarksTable);
        
        btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Return to the ManageResult frame when the "Back" button is clicked
                ManageResult m = new ManageResult();
                m.setVisible(true);
                dispose();
            }
        });
        btnBack.setBounds(510, 0, 84, 29);
        contentPane.add(btnBack);
    }
    
    // Method to update the displayed information
    public void updateInformation(String studentID, String studentName, String courseID, String approvalStatus, Object[][] moduleData) {
        lblStudentID.setText("Student ID : " + studentID);
        lblStudentName.setText("Student Name : " + studentName);
        lblCourseID.setText("Course ID : " + courseID);
        lblApprovalStatus.setText("Approval Status : " + approvalStatus);
        
        // Create a table model to hold the module data
        DefaultTableModel tableModel = new DefaultTableModel();
        
        // Add columns to the table model
        tableModel.addColumn("Module ID");
        tableModel.addColumn("Module Name");
        tableModel.addColumn("Marks");
        
        // Add rows to the table model using the moduleData
        if (moduleData != null) {
            for (Object[] rowData : moduleData) {
                tableModel.addRow(rowData);
            }
        }
        
        // Set the table model to the ModuleMarksTable
        ModuleMarksTable.setModel(tableModel);
    }
}
