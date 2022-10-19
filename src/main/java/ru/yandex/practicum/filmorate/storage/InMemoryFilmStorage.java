package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    private int id = 1;
    private final LocalDate releaseDate = LocalDate.of(1895,12,28);

    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public void createFilm(Film film) { // Сохранение Film
        log.info("Поступил Post запрос");
        validationFilm(film);
        log.info("Успешная валидация");
        film.setId(id);
        films.put(id, film);
        id++;
        log.debug("Фильм сохранен '{}'",films.get(film.getId()));
    }

    @Override
    public void updateFilm(Film film) { // Обновление Film
        log.info("Поступил Put запрос");
        if (films.containsKey(film.getId())){
            validationFilm(film);
            films.put(film.getId(), film);
            log.debug("Фильм обновлен '{}'", films.get(film.getId()));
        }else {
            throw new NotFoundException("ID не найден");
        }
    }

    @Override
    public Collection<Film> getFilm() { // Получение списка Films
        log.info("Поступил Get запрос списка Films");
        return films.values();
    }

    @Override
    public Film getFilmId(int id){
        if (!films.containsKey(id)){
            throw new NotFoundException("Данные не найдены");
        }
        return films.get(id);
    }

    private void validationFilm(Film film){
        if(releaseDate.isAfter(film.getReleaseDate())) {
            log.info("Не верная дата");
            throw new ValidationException("Дата релиза указана раньше 28.Декабря.1895");
        }
        log.info("Валидация пройдена");
    }
}
