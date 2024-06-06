package CMS;

import java.sql.*;

public class connectionCheck {
 
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String jdbcUrl = "jdbc:mysql://localhost:3306/group3";
        String username = "root";
        String password = "";
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
 
            // Establish a connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
 
            // Check if the connection is successful
            if (connection != null) {
                System.out.println("Database connection is successful!");
            }
 
            // Close the connection
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            System.out.println("Database connection failed!");
        }
    }
 
}
