package com.example.vitalii.test.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.vitalii.test.model.Person;

@Database(entities = {Person.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
}
