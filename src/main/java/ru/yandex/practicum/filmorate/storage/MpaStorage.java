package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {

    Mpa getMpa(int mpaId); //получение ограничения по id

    Collection<Mpa> getAllMpa(); // получение списка ограничений
}
