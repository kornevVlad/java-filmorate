package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film); // Сохранение

    void updateFilm(Film film); // Обновление

    List<Film> getFilm(); // Получение списка

    Film getFilmId(int id); // Получение фильма по ID

    void createLikeFilm(int id, int userId); // Добавление Like в Film

    void deleteLikeFilm(int id, int userId); // Удалаение Like в Film

    List<Film> bestPopular10(int count); // Вывод списка по популярности
}