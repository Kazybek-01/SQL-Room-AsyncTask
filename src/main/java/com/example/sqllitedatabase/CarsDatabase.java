package com.example.sqllitedatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.sqllitedatabase.models.Car;

@Database(entities = {Car.class},version = 1)
public abstract class CarsDatabase extends RoomDatabase {

    public abstract CarDao getCarDao();

}
