package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) { //создание фильма
        log.info("Создание фильма в БД");
        String sql = "INSERT INTO films(film_name, description, release_date," +
                " duration, mpa) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());

            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());

        String sqlMpa = "INSERT INTO mpa_film(film_id, mpa_id) VALUES(?,?)";
        jdbcTemplate.update(sqlMpa, film.getId(), film.getMpa().getId());

        setFilmMpa(film);

        Set<Genre> genres = film.getGenres();
        String sqlGenre = "INSERT INTO genre_film (film_id, genre_id) VALUES (?, ?)";
        if (genres != null) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sqlGenre, film.getId(), genre.getId());

                setFilmGenres(film);
            }
        }
        return film;
    }

    @Override
    public void updateFilm(Film film) { //обновление фильма
        log.info("обновление film в бд");

        // SELECT проверяет есть ли значение в бд
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?", film.getId());

        if(filmRows.next()) {
            String sql = "UPDATE films SET " +
                    "film_id = ?, film_name = ?, description = ?, release_date = ?, duration = ?, mpa = ? " +
                    "where film_id = ?";
            jdbcTemplate.update(sql,film.getId(),
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());

            String sqlMpa = "UPDATE mpa_film SET film_id = ?, mpa_id = ? ";
            jdbcTemplate.update(sqlMpa, film.getId(), film.getMpa().getId());

            setFilmMpa(film);

            //DELETE очищает все значения в бд данного фильма
            String sqlDeleteGenre = "DELETE FROM genre_film WHERE film_id = ?";
            jdbcTemplate.update(sqlDeleteGenre, film.getId());

            Set<Genre> genres = film.getGenres();
            String sqlGenre = "INSERT INTO genre_film (film_id, genre_id) VALUES (?, ?)";
            if (genres != null) {
                for (Genre genre : genres) {
                    jdbcTemplate.update(sqlGenre, film.getId(), genre.getId());
                }
            }

            setFilmGenres(film);
            log.info("film обновлен");
        } else {
            log.info("Фильм с идентификатором {} не найден.", film.getId());
            throw new NotFoundException("Данные не найдены");
        }
    }

    @Override
    public List<Film> getFilm() { //получение списка фильмов
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films");
        while (filmRows.next()){
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration")
            );

            setFilmMpa(film);
            setFilmGenres(film);

            films.add(film);
        }
        return films;
    }

    @Override
    public Film getFilmId(int id) { //получение фильма по id
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films " + " WHERE film_id = ?", id);

        if(filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration")
            );

            setFilmMpa(film);
            setFilmGenres(film);

            log.info("Найден фильм: {}", film.getId());
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Данные не найдены");
        }
    }

    @Override
    public void createLikeFilm(int filmId, int userId) { //добавление лайка для фильма
        String sql = "INSERT INTO likefilm(film_id, user_id) VALUES (?,?)";
        jdbcTemplate.update(sql,filmId, userId);
    }

    @Override
    public void deleteLikeFilm(int filmId, int userId) { //удаление лайка фильма
        String sql = "DELETE FROM likefilm WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);

    }

    @Override
    public List<Film> bestPopular10(int count) {
        List<Film>filmsPopular = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films f " +
                "LEFT OUTER JOIN likefilm l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id  ORDER BY COUNT(l.user_id)DESC LIMIT ?", count);
        while (filmRows.next()){
            Film film = new Film(
                    filmRows.getInt("film_id"),
                    filmRows.getString("film_name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration")
            );

            setFilmMpa(film);
            filmsPopular.add(film);
        }
        return  filmsPopular;
    }

    private Film setFilmMpa(Film film){ // запись mpa в film
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa   " +
                "WHERE mpa_id IN (SELECT mpa_id FROM mpa_film  WHERE film_id = ?)",film.getId()) ;
        while (mpaRows.next()){
            Mpa mpa = new Mpa(mpaRows.getInt("mpa_id"),
                    mpaRows.getString("mpa_name"));
            film.setMpa(mpa);
        }
        return film;
    }

    private Film setFilmGenres(Film film){ //запись genre в film
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre   " +
                "WHERE genre_id IN (SELECT genre_id FROM genre_film  WHERE film_id = ?)", film.getId());
        Set<Genre>genreList = new HashSet<>();
        while (genreRows.next()) {
            Genre getGenre = new Genre(genreRows.getInt("genre_id"),
                    genreRows.getString("genre_name"));
            genreList.add(getGenre);
        }
        film.setGenres(genreList);
        return film;
    }
}
