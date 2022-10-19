package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;


import java.util.*;
import java.util.stream.Collectors;



@Service
@Slf4j
public class FilmService implements FilmStorage {

    InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @Override
    public void createFilm(Film film) {
        inMemoryFilmStorage.createFilm(film);
    }


    @Override
    public void updateFilm(Film film) {
        inMemoryFilmStorage.updateFilm(film);
    }

    @Override
    public Collection<Film> getFilm() {
        return inMemoryFilmStorage.getFilm();
    }

    @Override
    public Film getFilmId(int id) {
        return inMemoryFilmStorage.getFilmId(id);
    }

    public void createLikeFilm(int id, int userId) { // Добавление Like в Film
        log.info("Put добавление лайка");
        for (Film film : inMemoryFilmStorage.getFilm()) {
            if (film.getId() == id) {
                film.setLikeFilm(userId);
                log.info("Лайк добавлен");
            }
        }
    }

    public void deleteLikeFilm(int id, int userId) { // Удалаение Like в Film
        log.info("DELETE удаление лайка");
        if(id < 0 || userId < 0){
            throw new NotFoundException("Входящие данные не корретны");
        }
        for (Film film : inMemoryFilmStorage.getFilm()) {
            if (film.getId() == id) {
                film.getLikeFilm().remove(userId);
                log.info("Лайк удален");
            }
        }
    }

    public List<Film> bestPopular10(int count){ // Вывод списка по популярности
        log.info("Get вывод Film по полулярности");
        List<Film> films = new ArrayList<>();
        for(Film film : inMemoryFilmStorage.getFilm()){
            films.add(film);
        }
        List<Film> best =
        films.stream().sorted(Comparator.comparingInt(film -> film.getLikeFilm().size())).collect(Collectors.toList());
        Collections.reverse(best);
        log.info("Список полулярности отсортирован");

        return best.stream().limit(count).collect(Collectors.toList());
    }
}