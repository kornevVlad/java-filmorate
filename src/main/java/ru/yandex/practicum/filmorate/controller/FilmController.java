package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping() //вывод списка фильмов
    public Collection<Film> getFilms(){
        return filmService.getFilm();
    }

    @PostMapping() //добавление фильма
    public Film createFilm(@Valid @RequestBody Film film){
        filmService.createFilm(film);
        return film;
    }

    @PutMapping() //обновление фильма
    public Film updateFilm(@Valid @RequestBody Film film){
        filmService.updateFilm(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}") // User ставит like Film
    public void createFilmLike(@PathVariable int id, @PathVariable int userId){
        filmService.createLikeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}") // Удаление Like
    public void deleteLike(@PathVariable int id, @PathVariable int userId){
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/popular") // Вывод фильмов по приоритету
    public List<Film> getBestPopular(@RequestParam(defaultValue = "10") int count){
      return filmService.bestPopular10(count);
    }

    @GetMapping("/{id}") // Получение фильма по ID
    public Film getFilmId(@PathVariable int id){
        return filmService.getFilmId(id);
    }
}