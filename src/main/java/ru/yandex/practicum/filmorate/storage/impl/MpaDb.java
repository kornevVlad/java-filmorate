package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class MpaDb implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDb(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Mpa getMpa(int mpaId){
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE mpa_id = ?", mpaId);

        if(mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("mpa_name")
            );

            return mpa;
        } else {
            throw new NotFoundException("Данные не найдены");
        }
    }

    @Override
    public Collection<Mpa> getAllMpa(){
        List<Mpa> mpaList = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa");
        while (mpaRows.next()){
            Mpa mpa = new Mpa(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("mpa_name")
            );
            mpaList.add(mpa);
        }
        return mpaList;
    }
}
