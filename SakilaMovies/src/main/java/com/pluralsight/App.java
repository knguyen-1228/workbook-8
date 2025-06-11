package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class App {

    static Scanner myScanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {

            Connection connection = login();

            boolean homeRunning = true;
            while (homeRunning) {
                int homeChoice = homeScreen();
                switch (homeChoice) {
                    case 1:
                        searchLastName(connection);
                        break;
                    case 2:
                        searchMovie(connection);
                        break;
                    case 0:
                        System.out.println("Exiting the app");
                        homeRunning = false;
                        break;
                }
            }


    }

    public static int homeScreen(){
        int choice;

        System.out.println("What would you like to do?");
        System.out.println("\t1) Search actors by last name");
        System.out.println("\t2) Search movies by the actor first and last name");
        System.out.println("\t0) Exit");
        System.out.println("Please select an option");

        choice = myScanner.nextInt();
        myScanner.nextLine();

        return choice;
    }

    public static Connection login(){

        while (true) {
            System.out.println("Please enter your username");
            String username = myScanner.nextLine();
            System.out.println("Please enter your password");
            String password = myScanner.nextLine();


            try (BasicDataSource dataSource = new BasicDataSource();){
                dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                Connection connection = dataSource.getConnection();
                System.out.println("Login Successful!");
                return connection;

            } catch (SQLException e) {
                System.out.println("Login failed, please try again!");

            }
        }
    }

    public static void searchLastName(Connection connection){

        System.out.println("Please enter the last name of the actor you want to search?");
        String lastName = myScanner.nextLine();

        try(
                PreparedStatement pStatement = connection.prepareStatement(
                        """
                        SELECT 
                            last_name,
                            first_name
                        FROM
                            actor
                        WHERE
                            last_name LIKE ?
                        """
                );
        ) {

            pStatement.setString(1,lastName + "%");
            try(ResultSet result = pStatement.executeQuery()){
                while(result.next()){

                    System.out.println("Last Name: " + result.getString("last_name"));
                    System.out.println("First Name: " + result.getString("first_name"));
                    System.out.println("-------------------------------------------------");

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void searchMovie(Connection connection){
        System.out.println("Please enter the first name of the actor");
        String actor = myScanner.nextLine();

        try(
                PreparedStatement pStatement = connection.prepareStatement(
                        """
                        SELECT
                            title
                        FROM
                            film F
                        JOIN film_actor FA
                            ON F.film_id = FA.film_id
                        JOIN actor A
                            ON FA.actor_id = A.actor_id
                        WHERE
                            A.first_name LIKE ?
                        """
                )
        ){
            pStatement.setString(1,actor + "%");
            try(ResultSet result = pStatement.executeQuery()) {
                while(result.next()){
                    System.out.println("Movie Titles: " + result.getString("title"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}

