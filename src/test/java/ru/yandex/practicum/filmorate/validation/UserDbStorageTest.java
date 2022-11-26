package ru.yandex.practicum.filmorate.validation;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserDbStorage userDbStorage ;

    @BeforeEach
    public void value(){
        userDbStorage.createUser(new User("Y@yandex.ru","Vl","VL", LocalDate.of(1990,2,20)));
    }

    @Test
    @DisplayName("Проверка сохраненного User")
    public void equalityUser(){

        for(User user : userDbStorage.getUsers()){
            assertEquals(user.getId(), 1);
            assertEquals(user.getLogin(), "Vl");
            assertEquals(user.getName(), "Vl");
            assertEquals(user.getEmail(), "Y@yandex.ru");
            assertEquals(user.getBirthday(), LocalDate.of(1990,2,20));
        }
    }
}
