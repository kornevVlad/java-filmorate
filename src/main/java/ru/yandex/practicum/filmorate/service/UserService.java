package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class UserService implements UserStorage {

    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage){
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public void createUser(User user) {
        inMemoryUserStorage.createUser(user);
    }

    @Override
    public void updateUser(User user) {
        inMemoryUserStorage.updateUser(user);
    }

    @Override
    public Collection<User> getUsers() {
        return inMemoryUserStorage.getUsers();
    }

    @Override
    public User getUserId(int id) {
        return inMemoryUserStorage.getUserId(id);
    }

    public void createUserFriends(int id, int friendsId) { // Создание Друзей
        if(id < 0 || friendsId < 0) {
            throw new NotFoundException("Отрицательные значения");
        }else if (id > 0 && friendsId > 0) {
                for (User user : inMemoryUserStorage.getUsers()) { // Присваивание User его Friend
                    if (user.getId() == id) {
                        for (User userFriend : inMemoryUserStorage.getUsers()) {
                            if (userFriend.getId() == friendsId) {
                                user.setFriends(friendsId);
                                userFriend.setFriends(id);
                            }
                        }
                    }
                }
        }
    }

    public void deleteUserFriends(int id, int friendsId){ // Удаление User и Friend
        for ( User user : inMemoryUserStorage.getUsers()){
            if(user.getId() == id){
                user.getFriends().remove(friendsId);
            }else if(user.getId() == friendsId){
                user.getFriends().remove(id);
            }
        }
    }

    public Collection<User> getCollectionFriends(int id){ // Получение Списка userFriend определенного User
        List<User> userFriends = new ArrayList<>();
       for(User user : inMemoryUserStorage.getUsers()){
           if(user.getId() == id){
               for (int i: user.getFriends()){
                  userFriends.add(inMemoryUserStorage.getUserId(i));
               }
           }
       }
       return userFriends;
    }

    public Collection<User> getUserFriendAndOtherUserFriend(int id, int otherId){ // Список друзей, общих с другим пользователем.
        List<User> jointFriends = new ArrayList<>();
        User userId = null;
        User userOtherId = null;

        for(User user : inMemoryUserStorage.getUsers()){
            if(user.getId() == id) {
                userId = user;
            }
            if(user.getId() == otherId){
                userOtherId = user;
            }
        }

        if(userId != null && userOtherId != null) {
            for (int i : userId.getFriends()) {
                for (int j : userOtherId.getFriends()) {
                    if (i == j) {
                        jointFriends.add(inMemoryUserStorage.getUserId(i));
                    }
                }
            }
        }
        return jointFriends;
    }
}
