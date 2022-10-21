package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public void createUser(User user) {
        userStorage.createUser(user);
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserId(int id) {
        return userStorage.getUserId(id);
    }

    public void createUserFriends(int id, int friendsId) { // Создание Друзей
        validationId(id, friendsId); // ИСПРАВЛЕНО

        User user = userStorage.getUserId(id);
        User userFriend = userStorage.getUserId(friendsId);
        user.setFriends(friendsId);
        userFriend.setFriends(id);
    }


    public void deleteUserFriends(int id, int friendsId){ // Удаление User и Friend
        validationId(id, friendsId);

        User user = userStorage.getUserId(id);
            user.getFriends().remove(friendsId);

        User userFriends = userStorage.getUserId(friendsId);
            userFriends.getFriends().remove(id);
    }

    public Collection<User> getCollectionFriends(int id){ // Получение Списка userFriend определенного User
        List<User> userFriends = new ArrayList<>();
        User user = userStorage.getUserId(id);
            for (int i: user.getFriends()){
                 userFriends.add(userStorage.getUserId(i));
            }
        return userFriends;
    }

    public List<User> getUserFriendAndOtherUserFriend(int id, int otherId){ // Список друзей, общих с другим пользователем.
        List<User> jointFriends = new ArrayList<>();
        validationId(id, otherId);
        User userId = userStorage.getUserId(id);
        User userOtherId = userStorage.getUserId(otherId);

        List<Integer> listId = userId.getFriends().stream().filter(userOtherId.getFriends()::contains).
                collect(Collectors.toList());
        for (Integer i : listId){
            jointFriends.add(userStorage.getUserId(i));
        }
        return jointFriends;
    }

    private void validationId(int id, int otherId){
        if(id < 0 || otherId < 0) {
            log.info("Валидация ID не пройдена");
            throw new NotFoundException("Отрицательные значения");
        }
    }
}