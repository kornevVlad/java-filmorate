package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void createFilm(Film film) {
        filmStorage.createFilm(film);
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    public List<Film> getFilm() {
        return filmStorage.getFilm();
    }

    public Film getFilmId(int id) {
        return filmStorage.getFilmId(id);
    }

    public void createLikeFilm(int id, int userId) { // Добавление Like в Film
        log.info("Put добавление лайка");
        validationId(id, userId);
        Film film = filmStorage.getFilmId(id);
            film.setLikeFilm(userId);
            log.info("Лайк добавлен");
    }

    public void deleteLikeFilm(int id, int userId) { // Удалаение Like в Film
        log.info("DELETE удаление лайка");
        validationId(id, userId);
        Film film = filmStorage.getFilmId(id);
            film.getLikeFilm().remove(userId);
            log.info("Лайк удален");
    }

    public List<Film> bestPopular10(int count){ // Вывод списка по популярности
        log.info("Get вывод Film по полулярности");

        List<Film> best = filmStorage.getFilm().stream().
                sorted(Comparator.comparingInt(film -> film.getLikeFilm().size())).collect(Collectors.toList());
        Collections.reverse(best);
        log.info("Список полулярности отсортирован");

        return best.stream().limit(count).collect(Collectors.toList());
    }

    private void validationId(int id, int userId){
        if(id < 0 || userId < 0){
            throw new NotFoundException("Входящие данные не корретны");
        }
    }
}