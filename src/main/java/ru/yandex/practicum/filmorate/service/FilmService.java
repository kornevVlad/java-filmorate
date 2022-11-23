package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.time.LocalDate;
import java.util.*;


@Service
@Slf4j
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void createFilm(Film film) {
        validationFilm(film);
        filmStorage.createFilm(film);
    }

    public void updateFilm(Film film) {
        validationFilm(film);
        filmStorage.updateFilm(film);
    }

    public List<Film> getFilm() {
        return filmStorage.getFilm();
    }

    public Film getFilmId(int id) {
        return filmStorage.getFilmId(id);
    }

    public void createLikeFilm(int id, int userId) { // Добавление Like в Film
        validationId(id, userId);
        filmStorage.createLikeFilm(id, userId);
    }

    public void deleteLikeFilm(int id, int userId) { // Удалаение Like в Film
        validationId(id, userId);
        filmStorage.deleteLikeFilm(id, userId);
    }

    public List<Film> bestPopular10(int count){ // Вывод списка по популярности
        return filmStorage.bestPopular10(count);
    }


    private void validationId(int id, int userId){
        if(id < 0 || userId < 0){
            throw new NotFoundException("Входящие данные не корретны");
        }
    }

    private void validationFilm(Film film){
        final LocalDate releaseDate = LocalDate.of(1895,12,28);
        if(releaseDate.isAfter(film.getReleaseDate())) {
            log.info("Не верная дата");
            throw new ValidationException("Дата релиза указана раньше 28.Декабря.1895");
        }
        log.info("Валидация пройдена");
    }
}