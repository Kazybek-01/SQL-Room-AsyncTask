//package com.example.sqllitedatabase.data;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import androidx.annotation.Nullable;
//
//import com.example.sqllitedatabase.models.Car;
//import com.example.sqllitedatabase.utils.Util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DatabaseHandler extends SQLiteOpenHelper {
//
//
//    public DatabaseHandler(@Nullable Context context) {
//        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
//    }
//
//    //создание базы данных
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        //SQL - Structured Query Language - язык структурированных запросов
//        String CREATE_CARS_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
//                + Util.KEY_ID + " INTEGER PRIMARY KEY,"
//                + Util.KEY_NAME + " TEXT,"
//                + Util.KEY_PRICE + " TEXT" + ")";
//
//        db.execSQL(CREATE_CARS_TABLE);
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME); //если таблица существует то полностью его распусти
//        //как только он распустит мы вызываем onCreate нашу базу
//        onCreate(db);
//    }
//
//    //CRUD
//    //Create Read Update Delete
//
//    public void addCar(Car car){
//        SQLiteDatabase db = this.getWritableDatabase(); //так как мы собираемся записать
//
//        ContentValues contentValues = new ContentValues(); //каждое значение по колонам сопоставить
//        contentValues.put(Util.KEY_NAME, car.getName());
//        contentValues.put(Util.KEY_PRICE, car.getPrice());
//
//        db.insert(Util.TABLE_NAME,null,contentValues); //добавляем
//        db.close();
//    }
//
//    //Read - {id} -> Car, List<Car>
//
//    //получаем данные по id
//    public Car getCar(int id){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        //ПОИСК
//        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID, Util.KEY_NAME,
//                Util.KEY_PRICE}, Util.KEY_ID + "=?", new String[]{String.valueOf(id)},
//                null,null,null,null);
//
//        Car car = new Car();
//        if(cursor != null){
//            try {
//                cursor.moveToFirst(); //позволяет получить наши данные
//                car = new Car(Integer.parseInt(cursor.getString(0)), //0 индекс-id, 1 индекс-name, 2индекс-price
//                        cursor.getString(1),
//                        cursor.getString(2));
//            }finally {
//                cursor.close();
//            }
//        }
//        return car;
//    }
//
//    public List<Car> getAllCars(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        List<Car> carList = new ArrayList<>();
//
//        String selectAllCars = "SELECT * FROM " + Util.TABLE_NAME;
//        Cursor cursor = db.rawQuery(selectAllCars,null); //доавление всех существ машин
//
//        //разобрать каждый объект и добавить в carList
//        if(cursor.moveToFirst()){ //если он содержит какие-то данные для получения произведи внутри цикл
//            try {
//                do{
//                    Car car = new Car();
//                    car.setId(Integer.parseInt(cursor.getString(0)));
//                    car.setName(cursor.getString(1));
//                    car.setPrice(cursor.getString(2));
//
//                    carList.add(car);
//                }while (cursor.moveToNext()); //пока он содержит следующий элемент
//            }finally {
//                cursor.close();
//            }
//        }
//        return carList;
//    }
//
//    public int updateCar(Car car){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(Util.KEY_NAME,car.getName());
//        contentValues.put(Util.KEY_PRICE,car.getPrice());
//
//        //берет таблицу, объект кар, возвращает id которую находит
//        return db.update(Util.TABLE_NAME, contentValues, Util.KEY_ID + "=?", //"=?" ищет объект по id
//                new String[]{String.valueOf(car.getId())}); //если этот объект есть мы
//    }
//
//    // Удаление машины
//    // Удаление всех машин
//
//    public void deleteCar(Car car){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Util.TABLE_NAME,Util.KEY_ID + "=?",
//                new String[]{String.valueOf(car.getId())});
//        db.close();
//    }
//    public void deleteAllCars(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM " + Util.TABLE_NAME); //удалит все строки
//        db.close();
//    }
//
//}
