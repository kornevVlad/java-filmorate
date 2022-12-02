package ru.yandex.practicum.filmorate.validation;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDbStorageTest {

    private final UserDbStorage userDbStorage;


    @Test
    @Order(1)
    @DisplayName("Создание пользователя")
    void testCreateUser(){
        User user1 =  new User("Ya@ya.ru","User1","User1",
                LocalDate.of(1990,2,20));
        userDbStorage.createUser(user1);

        assertEquals(userDbStorage.getUsers().size(), 1);
        assertEquals(userDbStorage.getUserId(1), user1);
    }

    @Test
    @Order(2)
    @DisplayName("Обновление пользователя")
    void testUpdateUser(){
        User user2 =  new User("Ya@mail.ru","User2","User2",
                LocalDate.of(1990,2,20));
        userDbStorage.createUser(user2);

        assertEquals(userDbStorage.getUserId(2), user2);

        User userUpdate2 =  new User(2,"Ya@google.ru","User2update","User2update",
                LocalDate.of(2000,1,10));
        userDbStorage.updateUser(userUpdate2);

        assertEquals(userDbStorage.getUserId(2),userUpdate2);
    }

    @Test
    @Order(3)
    @DisplayName("Создание друзей")
    void testCreateFriend(){
        userDbStorage.createUserFriends(1,2);

        assertEquals(userDbStorage.getCollectionFriends(1).size(),1);
    }

    @Test
    @Order(4)
    @DisplayName("Удаление друзей")
    void testDeleteFriend(){
        userDbStorage.deleteUserFriends(1,2);

        assertEquals(userDbStorage.getCollectionFriends(1).size(),0);
    }
}