package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void createFilm(Film film); // Сохранение

    void updateFilm(Film film); // Обновление

    List<Film> getFilm(); // Получение списка

    Film getFilmId(int id); // Получение фильма по ID
}