package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class GenreDb implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(int genreId) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE genre_id = ?", genreId);

        if(mpaRows.next()) {
            Genre genre = new Genre(
                    mpaRows.getInt("genre_id"),
                    mpaRows.getString("genre_name")
            );

            return genre;
        } else {
            throw new NotFoundException("Данные не найдены");
        }
    }

    @Override
    public Collection<Genre> getAllGenre() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre");
        while (mpaRows.next()){
            Genre genre = new Genre(
                    mpaRows.getInt("genre_id"),
                    mpaRows.getString("genre_name")
            );
            genres.add(genre);
        }
        return genres;
    }
}
