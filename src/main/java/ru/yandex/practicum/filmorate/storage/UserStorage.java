package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void createUser(User user); // Сохранение

    void updateUser(User user); // Обновление

    List<User> getUsers(); // Получение списка

    User getUserId(int id); // Получение данных о User
}
