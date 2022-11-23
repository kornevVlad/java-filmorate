package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @NotNull(message = "Не корректный Id")
    private int id;
    @Email(message = "Email не корректен")
    @NotBlank(message = "Email не может быть пустым")
    private String email;       //почта
    @NotBlank(message = "Логин не может быть пустым")
    private String login;          //логин
    @Size(max = 20)
    private String name;        //имя
    private LocalDate birthday;    //дата рождения

    private Set<Integer> friends; // друзья пользователя

    public User(){
    }

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }


    public void setFriends(int id){
        friends.add(id);
    }

    public Set<Integer> getFriends() {
        return friends;
    }
}