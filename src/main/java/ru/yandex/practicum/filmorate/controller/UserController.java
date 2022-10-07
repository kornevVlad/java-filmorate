package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@Slf4j
public class UserController {

    private int id = 1;
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users") //вывод списка пользователей
    public Collection<User> getUsers(){
        log.info("Получен запрос Get");
        return users.values();
    }

    @PostMapping(value = "/users") //создание пользователя
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос Post");
        validationUser(user);
        user.setId(id);
        users.put(id, user);
        id++;
        log.debug("Объект добавлен '{}'",users.size());
        return user;
    }

    @PutMapping(value = "/users") //обновление пользователя
    public User updateUser(@RequestBody User user){
        log.info("Получен запрос Put");
        if(users.containsKey(user.getId())) {
            validationUser(user);
            users.put(user.getId(), user);
        }else {
            throw new ValidationException("Пользователь с таким ID не найден");
        }
        return user;
    }

    private void validationUser(User user){
        if(user.getId() < 0){
            log.info("Отрицательный ID");
            throw new ValidationException("Отрицательное значение ID");

        }else if(user.getEmail() == null || user.getEmail().isBlank()){
            log.info("Email пуст");
            throw new ValidationException("Email не может быть пустым");

        }else if (!user.getEmail().contains("@")){
            log.info("Email не содержит @");
            throw new ValidationException("Email должен содержать символ @");

        }else if(user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Логин пуст или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");

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