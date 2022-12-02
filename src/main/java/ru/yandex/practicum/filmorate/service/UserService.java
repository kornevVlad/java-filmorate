package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;


@Service
@Slf4j
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public void createUser(User user) {
        validationUser(user);
        userStorage.createUser(user);
    }

    public void updateUser(User user) {
        validationUser(user);
        userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserId(int id) {
        return userStorage.getUserId(id);
    }

    public void createUserFriends(int id, int friendsId) { // Создание Друзей
        validationId(id, friendsId);
        userStorage.createUserFriends(id, friendsId);
    }

    public void deleteUserFriends(int id, int friendsId){ // Удаление User и Friend
        validationId(id, friendsId);
        userStorage.deleteUserFriends(id, friendsId);
    }

    public Collection<User> getCollectionFriends(int id){ // Получение Списка userFriend определенного User
       return userStorage.getCollectionFriends(id);
    }

    public List<User> getUserFriendAndOtherUserFriend(int id, int otherId){ // Список друзей, общих с другим пользователем.
        validationId(id, otherId);
        return userStorage.getUserFriendAndOtherUserFriend(id, otherId);
    }


    private void validationId(int id, int otherId){
        if(id < 0 || otherId < 0) {
            log.info("Валидация ID не пройдена");
            throw new NotFoundException("Отрицательные значения");
        }
    }

    private void validationUser(User user){ // Верификация User
        if(user.getLogin().contains(" ")) {
            log.info("Логин содержит пробелы");
            throw new ValidationException("Логин содержит пробелы");

        }else if(LocalDate.now().isBefore(user.getBirthday())){
            log.info("Не верная дата рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");

        }else if(user.getName() == null){
            user.setName(user.getLogin());
        }else if(user.getName().isBlank() ){ // присваивание имени если оно пустое
            user.setName(user.getLogin());
        }
        log.info("Валидация пройдена");
    }
}