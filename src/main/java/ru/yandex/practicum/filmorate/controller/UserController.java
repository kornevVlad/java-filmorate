package ru.yandex.practicum.filmorate.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;



import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping() //вывод списка пользователей
    public List<User> getUsers(){
        log.info("Получен запрос Get");
        return userService.getUsers();
    }

    @PostMapping() //создание пользователя
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос Post");
        userService.createUser(user);
        return user;
    }

    @PutMapping() //обновление пользователя
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос Put");
        userService.updateUser(user);
        return user;
    }

    @GetMapping("/{id}/friends") // Получение списка UserFriends
    public Collection<User> getUserFriends(@PathVariable int id){
        return userService.getCollectionFriends(id);
    }

    @GetMapping("/{id}") // Получени данных User по ID
    public User getUserId(@PathVariable int id){
        return userService.getUserId(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") // Список общих друзей
    public List<User> getUserAndOtherUserFriends(@PathVariable int id, @PathVariable int otherId){
       return userService.getUserFriendAndOtherUserFriend(id,otherId);
    }

    @PutMapping("/{id}/friends/{friendId}") // Изменение и добавление друзей
    public void createUserFriends(@PathVariable int id, @PathVariable int friendId){
        userService.createUserFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") // Удаление из друзей
    public void deleteUserFriends(@PathVariable int id, @PathVariable int friendId){
        userService.deleteUserFriends(id, friendId);
    }
}