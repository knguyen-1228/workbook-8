package com.pluralsight;

import java.sql.*;


public class App {
    public static void main(String[] args) {
        // load the MySQL Driver
        //Class.forName("com.mysql.cj.jdbc.Driver");


        // 1. open a connection to the database
        // use the database URL to point to the correct database
        try {
            Connection connection;
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "yearup");


            // create statement
            // the statement is tied to the open connection
            Statement statement = connection.createStatement();


            // define your query
            String query = "SELECT Title FROM Film ";


            // 2. Execute your query
            ResultSet results = statement.executeQuery(query);
            // process the results
            while (results.next()) {
                String title = results.getString("title");
                System.out.println(title);
            }


            // 3. Close the connection
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
