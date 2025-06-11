package com.pluralsight;

import com.pluralsight.Dao.ActorDao;
import com.pluralsight.Dao.FilmDao;
import com.pluralsight.Models.Actor;
import com.pluralsight.Models.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {
    //created static scanner
    static Scanner myScanner = new Scanner(System.in);
    //creating a static Basic data source
    static BasicDataSource dataSource = new BasicDataSource();
    //creating a static database
    static {
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
    }
    static ActorDao actorManager = new ActorDao(dataSource);
    static FilmDao filmManager = new FilmDao(dataSource);


    public static void main(String[] args) throws SQLException {
            //prompt user to login and establish database connection if correct info
            Connection connection = login();
            //home menu loop
            boolean homeRunning = true;
            while (homeRunning) {
                int homeChoice = homeScreen();
                switch (homeChoice) {
                    case 1:
                        //search actor by last name
                        searchActor(connection);
                        break;
                    case 2:
                        //search movie by actor
                        searchMovie(connection);
                        break;
                    case 0:
                        //exit
                        System.out.println("Exiting the app");
                        homeRunning = false;
                        break;
                }
            }


    }
    //home screen menu
    public static int homeScreen(){
        int choice = -1;

        while(true) {
            System.out.println("What would you like to do?");
            System.out.println("\t1) Search actors by name");
            System.out.println("\t2) Search movies by the actor id");
            System.out.println("\t0) Exit");
            System.out.println("Please select an option");

            if(myScanner.hasNextInt()){
                choice = myScanner.nextInt();
                myScanner.nextLine();
                if(choice <=2 && choice >= 0){
                    return choice;
                }else{
                    System.out.println("Invalid choice! Try again!");
                }
            }else{
                System.out.println("Please enter a number!");
                myScanner.nextLine();
            }
        }
    }
    //prompt user for login
    public static Connection login(){

        while (true) {
            System.out.println("Please enter your username");
            String username = myScanner.nextLine();
            System.out.println("Please enter your password");
            String password = myScanner.nextLine();

            dataSource.setUsername(username);
            dataSource.setPassword(password);
            try {
                System.out.println("Login Successful!");
                return dataSource.getConnection();
            } catch (SQLException e) {
                System.out.println("Login Failed! Try Again!");
            }

            //Use basic data source for connection pooling
            /*try (BasicDataSource dataSource = new BasicDataSource();){
                dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                //creating a connection if user input is correct
                Connection connection = dataSource.getConnection();
                System.out.println("Login Successful!");
                return connection;

            } catch (SQLException e) {
                System.out.println("Login failed, please try again!");

            }*/
        }
    }

    public static void searchActor(Connection connection){
        List<Actor> actors = actorManager.searchActor();

        for(Actor actor: actors){

            System.out.println("Actor Id: " + actor.getActorId());
            System.out.println("Actor First Name: " + actor.getFirstName());
            System.out.println("Actor Last Name: " + actor.getLastName());
            System.out.println("-------------------------------------------------");

        }



        /*System.out.println("Please enter the last name of the actor you want to search?");
        String lastName = myScanner.nextLine();
        //prepared statement fo SQL
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
            //using like for partial matching
            pStatement.setString(1,lastName + "%");
            //execute query
            try(ResultSet result = pStatement.executeQuery()){
                while(result.next()){

                    System.out.println("Last Name: " + result.getString("last_name"));
                    System.out.println("First Name: " + result.getString("first_name"));
                    System.out.println("-------------------------------------------------");

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

    }

    public static void searchMovie(Connection connection){
        List<Film> films = filmManager.getAllFilm();

        for(Film film: films){
            System.out.println("Film Id: " + film.getFilmId());
            System.out.println("Title: " + film.getTitle());
            System.out.println("Description: " + film.getDescription());
            System.out.println("Release Year: " + film.getReleaseYear());
            System.out.println("Length: " + film.getLength());
            System.out.println("-------------------------------------------------");

        }
        /*System.out.println("Please enter the first name of the actor");
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
            //using partial matching
            pStatement.setString(1,actor + "%");
            //run the query
            try(ResultSet result = pStatement.executeQuery()) {
                while(result.next()){
                    System.out.println("Movie Titles: " + result.getString("title"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        */
    }


}

