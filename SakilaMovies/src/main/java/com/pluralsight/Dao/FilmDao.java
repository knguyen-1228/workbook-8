package com.pluralsight.Dao;

import com.pluralsight.Models.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FilmDao {

    private BasicDataSource dataSource;
    static Scanner myScanner = new Scanner(System.in);

    public FilmDao(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Film> getAllFilm() {

        List<Film> filmList = new ArrayList<>();

        System.out.println("Please enter the actorId");
        int userInput = myScanner.nextInt();

        String sql = """
                SELECT
                    F.film_id,
                    F.title,
                    F.description,
                    F.release_year,
                    F.length
                FROM
                    film F
                JOIN film_actor FA
                    ON F.film_id = FA.film_id
                JOIN actor A
                    ON FA.actor_id = A.actor_id
                WHERE
                    A.actor_id = ?
                """;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement pStatement = connection.prepareStatement(sql);
        ) {
            pStatement.setInt(1,userInput);
            try(ResultSet result = pStatement.executeQuery()){
                while(result.next()){
                    int id = result.getInt("film_id");
                    String title = result.getString("title");
                    String desc = result.getString("description");
                    int releaseYear = result.getInt("release_year");
                    int length = result.getInt("length");

                    Film film = new Film(id,title,desc,releaseYear,length);
                    filmList.add(film);
                }
            }
            return filmList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
