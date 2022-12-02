package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User createUser(User user); // Сохранение

    void updateUser(User user); // Обновление

    List<User> getUsers(); // Получение списка

    User getUserId(int id); // Получение данных о User

    void createUserFriends(int id, int friendsId); // Создание Друзей

    void deleteUserFriends(int id, int friendsId); // Удаление User и Friend

    Collection<User> getCollectionFriends(int id); // Получение Списка userFriend определенного User

    List<User> getUserFriendAndOtherUserFriend(int id, int otherId); // Список друзей, общих с другим пользователем.
}
