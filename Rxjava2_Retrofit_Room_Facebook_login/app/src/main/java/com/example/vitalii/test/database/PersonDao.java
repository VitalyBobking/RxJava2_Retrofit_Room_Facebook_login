package com.example.vitalii.test.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.example.vitalii.test.model.Person;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert
    void insertAll(List<Person> person);

    @Query("SELECT * FROM person")
    List<Person> getAll();
}
