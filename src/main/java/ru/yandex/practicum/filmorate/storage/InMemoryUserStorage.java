package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private int id = 1;

    protected Map<Integer, User> users = new HashMap<>();

    @Override
    public void createUser(User user) { //Сохранение User
        validationUser(user);
        user.setId(id);
        users.put(id, user);
        id++;
        log.debug("Объект добавлен '{}'",users.size());
    }

    @Override
    public void updateUser(User user) { // Обновление User
        if(users.containsKey(user.getId())) {
            validationUser(user);
            users.put(user.getId(), user);
        }else {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public Collection<User> getUsers() { // Получение списка Users
        return users.values();
    }

    @Override
    public User getUserId(int id){
        log.info("Get запрос на получение User по Id");
        if(!users.containsKey(id)){
            throw new NotFoundException("Данные не найдены");
        }
        return users.get(id);
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
