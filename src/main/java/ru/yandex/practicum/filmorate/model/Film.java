package ru.yandex.practicum.filmorate.model;

import lombok.Data;


import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    @NotNull(message = "Не корректный Id")
    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;        //название фильма
    @Size(min = 1, max = 200, message = "Не корректная длинна строки min 1, max 200")
    private String description; //описание
    private LocalDate releaseDate; //дата релиза
    @Min(value = 1, message = "Продолжительность не меньше 1")
    private long duration;      //продолжительность фильма

    private Set<Integer> likeFilm; // лайки фильма

    private Set<Genre> genres; //жанр

    @NotNull
    private Mpa mpa; //возрастное ограничение

    public Film(){
    }

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likeFilm = new HashSet<>();
        this.genres = new HashSet<>();
    }

    public Film(int id,String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likeFilm = new HashSet<>();
        this.genres = new HashSet<>();
    }

    public Film(int id,String name, String description, LocalDate releaseDate, long duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likeFilm = new HashSet<>();
        this.mpa = mpa;
        this.genres = new HashSet<>();
    }



    public Set<Integer> getLikeFilm() {
        return likeFilm;
    }

    public void setLikeFilm(int id) {
        likeFilm.add(id);
    }
}