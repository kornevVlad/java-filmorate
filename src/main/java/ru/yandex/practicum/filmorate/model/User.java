package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
public class User {
    @NotNull(message = "Не корректный Id")
    private int id;
    private String email;       //почта
    private String login;          //логин
    private String name;        //имя
    private LocalDate birthday;    //дата рождения

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}