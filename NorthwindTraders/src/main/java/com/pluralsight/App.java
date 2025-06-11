package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

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
                case 3:
                    viewCategory(connection);
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

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");

        while (true) {
            System.out.println("Please enter your username");
            String username = myScanner.nextLine();
            System.out.println("Please enter your password");
            String password = myScanner.nextLine();

            dataSource.setUsername(username);
            dataSource.setPassword(password);

            try {
                Connection connection = dataSource.getConnection();
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
        System.out.println("\t3) Display all category");
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

    public static void viewCategory(Connection connection) {
        try (
                //Created query to get id and name
                PreparedStatement pStatement = connection.prepareStatement(
                        """
                                SELECT
                                    CategoryID,
                                    CategoryName
                                FROM
                                    categories
                                """);
                //execute query and get result set
                ResultSet results = pStatement.executeQuery();
        ) {
            //loop through and display all categories
            while (results.next()) {
                System.out.println("Category ID: " + results.getInt("CategoryID"));
                System.out.println("Category Name: " + results.getString("CategoryName"));
                System.out.println("-------------------------------------------------");
            }
            //ask user for category id to show products
            System.out.println("Enter a category id to view products");
            int userInput = myScanner.nextInt();
            myScanner.nextLine();

            //retrieve the name of selected category
            String categoryName = null;
            try (PreparedStatement categoryNameStatement = connection.prepareStatement(
                    """
                            SELECT
                                CategoryName
                            FROM
                                categories
                            WHERE
                                CategoryID = ?
                            """);
            ) {
                categoryNameStatement.setInt(1, userInput);
                try (ResultSet cResult = categoryNameStatement.executeQuery()) {
                    if (cResult.next()) {
                        categoryName = cResult.getString("CategoryName");
                    }
                }
            }
            //retrieve all products under selected category
            try (PreparedStatement productStatement = connection.prepareStatement(
                    """
                            SELECT
                                ProductID,
                                ProductName,
                                UnitPrice,
                                UnitsInStock
                            FROM
                                products P
                            JOIN categories C
                            ON P.CategoryID = C.CategoryID
                            WHERE
                                P.CategoryID = ?
                            """);
            ) {
                productStatement.setInt(1, userInput);
                try (ResultSet pResult = productStatement.executeQuery()) {
                    System.out.println("Here are the products under " + categoryName + ":");
                    while (pResult.next()) {
                        System.out.println("Product ID: " + pResult.getInt("ProductID"));
                        System.out.println("Product Name: " + pResult.getString("ProductName"));
                        System.out.printf("Price: %.2f\n", pResult.getDouble("UnitPrice"));
                        System.out.println("Units In Stock: " + pResult.getInt("UnitsInStock"));
                        System.out.println("-------------------------------------------------");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
