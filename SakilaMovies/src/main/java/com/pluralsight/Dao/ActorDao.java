package com.pluralsight.Dao;

import com.pluralsight.Models.Actor;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ActorDao {

    private BasicDataSource dataSource;
    static Scanner myScanner = new Scanner(System.in);

    public ActorDao(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Actor> searchActor() {

        List<Actor> actorList = new ArrayList<>();

        System.out.println("Please enter the name of the actor you want to search?");
        String userInput = myScanner.nextLine();

        String sql = """
                SELECT
                   actor_id,
                   first_name,
                   last_name
                FROM
                   actor
                WHERE
                   first_name LIKE ?
                """;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(sql);

        ) {

            pStatement.setString(1, userInput + "%");

            try(ResultSet result = pStatement.executeQuery()){
                while(result.next()){

                    int id = result.getInt("actor_id");
                    String firstName = result.getString("first_name");
                    String lastName = result.getString("last_name");

                    Actor actor = new Actor(id,firstName,lastName);

                    actorList.add(actor);
                }
            }
            return actorList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
