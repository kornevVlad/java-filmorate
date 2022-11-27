package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;


@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 1;

    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) { // Сохранение Film
        log.info("Поступил Post запрос");
        log.info("Успешная валидация");
        film.setId(id);
        films.put(id, film);
        id++;
        log.debug("Фильм сохранен '{}'",films.get(film.getId()));
        return film;
    }

    @Override
    public void updateFilm(Film film) { // Обновление Film
        log.info("Поступил Put запрос");
        if (films.containsKey(film.getId())){
            films.put(film.getId(), film);
            log.debug("Фильм обновлен '{}'", films.get(film.getId()));
        }else {
            throw new NotFoundException("ID не найден");
        }
    }

    @Override
    public List<Film> getFilm() { // Получение списка Films
        log.info("Поступил Get запрос списка Films");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmId(int id){ //получение фильма по id
        if (!films.containsKey(id)){
            throw new NotFoundException("Данные не найдены");
        }
        return films.get(id);
    }

    @Override
    public void createLikeFilm(int id, int userId) { // Добавление Like в Film
        log.info("Put добавление лайка");
        Film film = films.get(id);
        film.setLikeFilm(userId);
        log.info("Лайк добавлен");
    }

    @Override
    public void deleteLikeFilm(int id, int userId) { // Удалаение Like в Film
        log.info("DELETE удаление лайка");
        Film film = films.get(id);
        film.getLikeFilm().remove(userId);
        log.info("Лайк удален");
    }

    @Override
    public List<Film> bestPopular10(int count){ // Вывод списка по популярности
        log.info("Get вывод Film по полулярности");

        List<Film> best = getFilm().stream().
                sorted(Comparator.comparingInt(film -> film.getLikeFilm().size())).collect(Collectors.toList());
        Collections.reverse(best);
        log.info("Список полулярности отсортирован");

        return best.stream().limit(count).collect(Collectors.toList());
    }
}
