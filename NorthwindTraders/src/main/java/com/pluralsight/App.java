package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {

    static Scanner myScanner = new Scanner(System.in);

    public static void main(String[] args) {

        Connection connection = login();

        boolean homeRunning = true;
        while (homeRunning) {
            int homeChoice = homeScreen();
            switch (homeChoice) {
                case 1:
                    viewProducts(connection);
                    break;
                case 2:
                    viewCustomer(connection);
                    break;
                case 0:
                    System.out.println("Exiting the app");
                    homeRunning = false;
                    break;
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public static Connection login() {

        Connection connection = null;


        while (true) {
            System.out.println("Please enter your username");
            String username = myScanner.nextLine();
            System.out.println("Please enter your password");
            String password = myScanner.nextLine();
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/northwind", username, password);
                System.out.println("Login Successful!");
                return connection;

            } catch (SQLException e) {
                System.out.println("Login failed, please try again!");

            }
        }


    }

    public static int homeScreen() {
        int choice;
        System.out.println("What do you want to do?");
        System.out.println("\t1) Display all products");
        System.out.println("\t2) Display all customers");
        System.out.println("\t0) Exit");
        System.out.println("Select an option");
        choice = myScanner.nextInt();
        myScanner.nextLine();

        return choice;

    }

    public static void viewProducts(Connection connection) {
        PreparedStatement pStatement = null;
        ResultSet results = null;
        try {

            // create statement
            // the statement is tied to the open connection
            pStatement = connection.prepareStatement(
                    "SELECT " +
                            "ProductID, ProductName, UnitPrice, UnitsInStock " +
                            "FROM products "
            );


            // 2. Execute your query
            results = pStatement.executeQuery();


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


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // close the resources
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pStatement != null) {
                try {
                    pStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void viewCustomer(Connection connection) {
        PreparedStatement pStatement = null;
        ResultSet results = null;
        try {

            // create statement
            // the statement is tied to the open connection
            pStatement = connection.prepareStatement(
                    "SELECT ContactName, CompanyName, City, Country, Phone " +
                            "FROM customers " +
                            "WHERE Country IS NOT NULL " +
                            "ORDER BY Country "
            );


            // 2. Execute your query
            results = pStatement.executeQuery();


            // process the results
            while (results.next()) {
                String name = results.getString("ContactName");
                String companyName = results.getString("CompanyName");
                String city = results.getString("City");
                String country = results.getString("Country");
                String phone = results.getString("Phone");

                System.out.println("Contact Name: " + name);
                System.out.println("Company Name: " + companyName);
                System.out.println("City: " + city);
                System.out.println("Country:  " + country);
                System.out.println("Phone Number:  " + phone);
                System.out.println("-------------------------------------------------");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // close the resources
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (pStatement != null) {
                try {
                    pStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
