package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;


    @Test
    @Order(1)
    @DisplayName("Создание и сохранение фильма")
    public void testCreateFilm() {

        Mpa mpa1 = new Mpa(1, "G");
        Film film1 =
                new Film("Фильм", "Описание_Тест",
                        LocalDate.of(1990, 2, 20), 120);
        film1.setMpa(mpa1);
        filmDbStorage.createFilm(film1);

        assertEquals(filmDbStorage.getFilm().size(), 1);
        assertEquals(filmDbStorage.getFilmId(1), film1);
    }

    @Test
    @Order(2)
    @DisplayName("Обновление фильма")
    public void testUpdateFilm() {

        Mpa mpa2 = new Mpa(2,"PG");
        Film film2 =
                new Film("Фильм2","Описание тест 2",
                        LocalDate.of(2000,12,1), 90);
        film2.setMpa(mpa2);
        filmDbStorage.createFilm(film2);

        assertEquals(filmDbStorage.getFilm().size(),2);
        assertEquals(filmDbStorage.getFilmId(2),film2);

        Genre genre = new Genre(1,"Комедия");
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);
        film2.setGenres(genres);
        filmDbStorage.updateFilm(film2);

        assertEquals(film2, filmDbStorage.getFilmId(2));
    }

    @Test
    @Order(3)
    @DisplayName("Получение фильма по ID и размер списка")
    void testGetFilmId(){
        Mpa mpa3 = new Mpa(2,"PG");
        Film film3 =
                new Film("Фильм3","Описание 3",
                        LocalDate.of(2000,12,1), 90);
        film3.setMpa(mpa3);
        filmDbStorage.createFilm(film3);

        assertEquals(filmDbStorage.getFilmId(3), film3);
        assertEquals(filmDbStorage.getFilm().size(), 3);
    }
}