package ru.yandex.practicum.filmorate.validation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;


public class UserControllerTest {
    UserController userController = new UserController();

    @BeforeEach
    public void value(){
        User user = new User("Y@yandex.ru)","Vl","", LocalDate.of(1990,02,20));
        userController.createUser(user);
    }

    @Test
    @DisplayName("Сохранение User")
    public void createUser(){
        assertEquals(userController.getUsers().size(),1);
    }

    @Test
    @DisplayName("Обновление User")
    public void updateUser(){
        User user =  new User("d@ya.ru","AD","",LocalDate.of(1980,10,15));
        user.setId(1);
        userController.updateUser(user);

        for (User test :userController.getUsers()){
            assertEquals(test,user);
            assertEquals(test.getName(),"AD");
        }
    }

    @Test
    @DisplayName("Не валидные значения")
    public void notValidateUser(){
        User notValidEmail = new User("yandex.ru","Vlad","Влад", LocalDate.of(1990,02,20));
        User notValidDate = new User("Y@yandex.ru","Vlad","Влад", LocalDate.of(2050,02,20));
        User notValidLogin = new User("Y@yandex.ru","V l a d","Влад", LocalDate.of(2050,02,20));

        try {
            userController.createUser(notValidEmail);
        }catch (ValidationException v){
            assertNotEquals("",v.getMessage());
        }

        try {
            userController.createUser(notValidDate);
        }catch (ValidationException v){
            assertNotEquals("",v.getMessage());
        }

        try {
            userController.createUser(notValidLogin);
        }catch (ValidationException v){
            assertNotEquals("",v.getMessage());
        }
    }
}