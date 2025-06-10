
package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) throws ClassNotFoundException {
        //ensures we get 2 inputs
        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.hca.jdbc.UsingDriverManager <username> " +
                            "<password>");
            System.exit(1);
        }

        // get the user name and password from the command line args
        String username = args[0];
        String password = args[1];

        // Establish the variables with null outside the try scope
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // create the connection and prepared statement
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", username, password);

            //initialize the prepared statement
            preparedStatement = connection.prepareStatement(
                    "SELECT first_name, last_name FROM customer " +
                            "WHERE last_name LIKE ? ORDER BY first_name");

            // set the parameters for the prepared statement
            preparedStatement.setString(1, "Sa%");

            // execute the query
            resultSet = preparedStatement.executeQuery();

            // loop thru the results
            while (resultSet.next()) {
                // process the data
                System.out.printf("first_name = %s, last_name = %s;\n",
                        resultSet.getString(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            //maybe print a nicer message to the user
            //maybe store the real error in a log file
            //make gracefully exit instead of crashing
            //maybe we want it to crash so we know what will happen
            //decide what to do if things fail
            e.printStackTrace();
        } finally {
            // close the resources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
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
    }
}
