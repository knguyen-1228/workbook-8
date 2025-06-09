package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        // 1. open a connection to the database
// use the database URL to point to the correct database
        if(args.length != 2){
            System.out.println("Application needs two arguments to run" + "java com.pluralsight.UsingDriverManager <username><password>");
            System.exit(1);
        }
        String username = args[0];
        String password = args[1];
        try {
            Connection connection;
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind", username, password);


            // create statement
            // the statement is tied to the open connection
            PreparedStatement pStatement = connection.prepareStatement(
                    "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products"
            );


            // 2. Execute your query
            ResultSet results = pStatement.executeQuery();


            // process the results
            while (results.next()) {
                int ID = results.getInt("ProductID");
                String name = results.getString("ProductName");
                double price = results.getDouble("UnitPrice");
                int stock = results.getInt("UnitsInStock");

                System.out.println("Product ID: " + ID);
                System.out.println("Product Name: " + name);
                System.out.printf("Price: %.2f\n", price);
                System.out.println("Units In Stock: " + stock);
                System.out.println("-------------------------------------------------");
            }


            // 3. Close the connection
            results.close();
            pStatement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
