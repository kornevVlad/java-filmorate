package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id = 1;

    protected Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) { //Сохранение User
        user.setId(id);
        users.put(id, user);
        id++;
        log.debug("Объект добавлен '{}'",users.size());
        return getUserId(id);
    }

    @Override
    public void updateUser(User user) { // Обновление User
        if(users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        }else {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public List<User> getUsers() { // Получение списка Users
        return new ArrayList<>(users.values()); //ИСПРАВЛЕНО
    }

    @Override
    public User getUserId(int id){ //получение user по id
        log.info("Get запрос на получение User по Id");
        if(!users.containsKey(id)){
            throw new NotFoundException("Данные не найдены");
        }
        return users.get(id);
    }

    @Override
    public void createUserFriends(int id, int friendsId) { //создание друзей
        User user = users.get(id);
        User userFriend = users.get(friendsId);
        user.setFriends(friendsId);
        userFriend.setFriends(id);
    }

    @Override
    public void deleteUserFriends(int id, int friendsId) { // Удаление User и Friend
        User user = users.get(id);
        user.getFriends().remove(friendsId);

        User userFriends = users.get(friendsId);
        userFriends.getFriends().remove(id);
    }

    @Override
    public Collection<User> getCollectionFriends(int id) { // Получение Списка userFriend определенного User
        List<User> userFriends = new ArrayList<>();
        User user = users.get(id);
        for (int i: user.getFriends()){
            userFriends.add(users.get(i));
        }
        return userFriends;
    }


    @Override
    public List<User> getUserFriendAndOtherUserFriend(int id, int otherId){ // Список друзей, общих с другим пользователем.
        List<User> jointFriends = new ArrayList<>();
        User userId = users.get(id);
        User userOtherId = users.get(otherId);

        List<Integer> listId = userId.getFriends().stream().filter(userOtherId.getFriends()::contains).
                collect(Collectors.toList());
        for (Integer i : listId){
            jointFriends.add(users.get(i));
        }
        return jointFriends;
    }
}
