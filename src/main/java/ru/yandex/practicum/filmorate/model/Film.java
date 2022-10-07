package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private String name;        //название
    private String description; //описание
    private LocalDate releaseDate; //дата релиза
    private long duration;      //продолжительность фильма

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}