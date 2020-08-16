package com.example.sqllitedatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sqllitedatabase.models.Car;

import java.util.List;

@Dao
public interface CarDao {

    //Будет содержать методы для манипуляции над объектами
    //Create  - Read - Update - Delete

    @Insert
    public long addCar(Car car);

    @Update
    public void updateCar(Car car);

    @Delete
    public void deleteCar(Car car);

    //Read - Car - {id}
    //Read - List<Car>
    @Query("select * from cars")
    public List<Car> getAllCars();

    @Query("select * from cars where car_id ==:carId")
    public Car getCar(long carId);

}
