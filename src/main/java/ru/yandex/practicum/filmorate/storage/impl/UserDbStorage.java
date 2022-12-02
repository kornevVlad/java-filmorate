package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) { // добавление пользователя
        String sql = "INSERT INTO user_list(login, user_name, email, birthday) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public void updateUser(User user) { //обновление пользователя
        log.info("БД обновление user");
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM user_list WHERE user_id = ?", user.getId());

        if (userRows.next()) {
            String sql = "UPDATE user_list SET " +
                    "user_name = ?, login = ?, email = ?, birthday = ? " +
                    "where user_id = ?";
            jdbcTemplate.update(sql,
                    user.getName(),
                    user.getLogin(),
                    user.getEmail(),
                    user.getBirthday(),
                    user.getId());
            log.info("user обновлен");
        } else {
            log.info("Пользователь с идентификатором {} не найден.", user.getId());
            throw new NotFoundException("Данные не найдены");
        }
    }

    @Override
    public List<User> getUsers() { //список пользователей
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM user_list");
        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            setFriendsCollection(user);
            users.add(user);
        }
        return users;
    }


    @Override
    public User getUserId(int id) { //получение по ID

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM user_list " + " WHERE user_id = ?", id);

        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            log.info("Найден пользователь: {}", user.getId());
            setFriendsCollection(user);
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Данные не найдены");
        }
    }

    @Override
    public void createUserFriends(int id, int friendsId) { //создание друзей
        String sql = "INSERT INTO user_friends(user_id, friend_id) VALUES (?,?)";
        jdbcTemplate.update(sql, id, friendsId);
        log.info("Первый дружит со вторым");
    }

    @Override
    public void deleteUserFriends(int id, int friendsId) { //удаление друзей
        String sql = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendsId);
    }

    @Override
    public Collection<User> getCollectionFriends(int id) { //список друзей пользователя
        List<User> userFriends = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT us.user_id, us.email, us.login," +
                " us.user_name, us.birthday " +
                " FROM user_list AS us " +
                "INNER JOIN user_friends AS fr ON  fr.user_id = ?" +
                " WHERE us.user_id = fr.friend_id", id);
        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            setFriendsCollection(user);
            userFriends.add(user);
        }
        return userFriends;
    }

    @Override
    public List<User> getUserFriendAndOtherUserFriend(int id, int otherId) { //список общих друзей
        List<User> commonFriends = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT * FROM user_list u, user_friends f, user_friends o " +
                        " WHERE u.user_id = f.friend_id AND u.user_id = o.friend_id " +
                        " AND f.user_id = ? AND o.user_id = ?", id, otherId);

        while (userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("user_name"),
                    userRows.getDate("birthday").toLocalDate()
            );
            commonFriends.add(user);
        }
        return commonFriends;
    }

    private User setFriendsCollection(User user) { // заполнение id друзей
        SqlRowSet idFriendRows = jdbcTemplate.queryForRowSet("SELECT friend_id FROM user_friends " +
                " WHERE user_id = ?", user.getId());
        while (idFriendRows.next()) {
            int id = idFriendRows.getInt("friend_id");
            user.setFriends(id);
        }
        return user;
    }
}