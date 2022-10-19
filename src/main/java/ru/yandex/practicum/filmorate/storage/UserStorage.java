package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    void createUser(User user); // Сохранение

    void updateUser(User user); // Обновление

    Collection<User> getUsers(); // Получение списка

    User getUserId(int id); // Получение данных о User
}
