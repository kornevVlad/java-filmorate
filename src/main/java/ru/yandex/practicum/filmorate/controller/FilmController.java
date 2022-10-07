package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private int id = 1;
    private Map<Integer, Film> films = new HashMap<>();
    private final int STRING_LENGTH = 200;
    private final LocalDate releaseDate = LocalDate.of(1895,12,28);

    @GetMapping("/films") //вывод списка фильмов
    public Collection<Film> getFilms(){
        return films.values();
    }

    @PostMapping(value = "/films") //добавление фильма
    public Film createFilm(@RequestBody Film film){
        log.info("Поступил Post запрос");
        validationFilm(film);
        film.setId(id);
        films.put(id, film);
        id++;
        log.debug("Фильм сохранен '{}'",films.get(film.getId()));
        return film;
    }

    @PutMapping(value = "/films") //обновление фильма
    public Film updateFilm(@RequestBody Film film){
        log.info("Поступил Put запрос");
        if (films.containsKey(film.getId())){
            validationFilm(film);
            films.put(film.getId(), film);
            log.debug("Фильм обновлен '{}'", films.get(film.getId()));
        }else {
            throw new ValidationException("ID не найден");
        }
        return film;
    }

    private void validationFilm(Film film){
        if(film.getId() < 0){
            log.info("Отрицательный ID");
            throw new ValidationException("Отрицательное значение ID");

        }else if(film.getName().isBlank() || film.getName() == null){
            log.info("Пустое имя");
            throw new ValidationException("Назнавание фильма не может быть пустым");

        }else if(film.getDescription().length()>STRING_LENGTH){
            log.info("Description max 200");
            throw new ValidationException("Описание превышает 200 символов");

        }else if(film.getDuration()<0){
            log.info("Отрицательная продолжительность");
            throw new ValidationException("Продолжительность не может быть отрицательной");

        }else if(releaseDate.isAfter(film.getReleaseDate())) {
            log.info("Не верная дата");
            throw new ValidationException("Дата релиза указана раньше 28.Декабря.1895");
        }
        log.info("Валидация пройдена");
    }
}
