package com.example.sqllitedatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqllitedatabase.models.Car;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CarsAdapter carsAdapter;
    private ArrayList<Car> carArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    CarsDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SQL Room Library
        //@Database - точка для получения к базе данных
        //@Entity - Таблица которая должна содержать классы
        //@Dao - методы для управления данными таблицы

        // CRUD
        // Create - Read - Update - Delete
        //Create Database - Car
        //Data
        //CRUD operations

        recyclerView = findViewById(R.id.recyclerView);

        database = Room.databaseBuilder(getApplicationContext(),
                CarsDatabase.class,"CarDB")
//                .allowMainThreadQueries()
                .build();

        new GetAllCarsAsyncTask().execute(); //здесь вызываем метод

        carsAdapter = new CarsAdapter(this, carArrayList,MainActivity.this);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(carsAdapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAndEditCars(false,null,-1); //если мы хотим добавить новую машину
            }
        });
    }
    public void addAndEditCars(final boolean isUpdate, final Car car, final int position){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.layout_add_car,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        TextView newCarTitle = view.findViewById(R.id.newCarTitle);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText priceEditText = view.findViewById(R.id.priceEditText);

        newCarTitle.setText(!isUpdate ? "Add Car" : "Edit Car"); //тернарный оператор если true Add Car,иначе Edit Car

        if(isUpdate && car != null){
            nameEditText.setText(car.getName());
            priceEditText.setText(car.getPrice());
        }

        builder.setCancelable(false)
                .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton(isUpdate ? "Delete" : "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int id) {
                        if(isUpdate){
                            deleteCar(car,position);
                        } else {
                            dialogBox.cancel();
                        }
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameEditText.getText().toString())){
                    Toast.makeText(MainActivity.this,"Enter car name!",Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(priceEditText.getText().toString())){
                    Toast.makeText(MainActivity.this,"Enter car price!",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                //если человек хочет обновить
                if(isUpdate && car != null){
                    updateCar(nameEditText.getText().toString(),priceEditText.getText().toString(),position);
                } else {
                    createCar(nameEditText.getText().toString(),priceEditText.getText().toString());
                }
            }
        });
    }
    private void deleteCar(Car car, int position){
        carArrayList.remove(position);
        new DeleteCarAsyncTask().execute(car);
    }
    private void updateCar(String name, String price, int position){
        Car car = carArrayList.get(position);
        car.setName(name);
        car.setPrice(price);

        carArrayList.set(position,car);
        new UpdateCarAsyncTask().execute(car);
    }
    private void createCar(String name, String price){
        new CreateCarAsyncTask().execute(new Car(0,name,price));
    }
    private class CreateCarAsyncTask extends AsyncTask<Car, Void, Void>{
        @Override
        protected Void doInBackground(Car... cars) {
            long id = database.getCarDao().addCar(cars[0]);
            Car car = database.getCarDao().getCar(id);
            if(car != null) {
                carArrayList.add(0, car);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carsAdapter.notifyDataSetChanged();
        }
    }
    private class GetAllCarsAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            //Делает работу асинхронно на вторичном потоке
            carArrayList.addAll(database.getCarDao().getAllCars());
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Метод срабатывает как только метод doInBackground заканчивает свою работу
            carsAdapter.notifyDataSetChanged();
        }
    }
    private class DeleteCarAsyncTask extends AsyncTask<Car, Void, Void>{
        @Override
        protected Void doInBackground(Car... cars) {
            database.getCarDao().deleteCar(cars[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carsAdapter.notifyDataSetChanged();
        }
    }
    private class UpdateCarAsyncTask extends AsyncTask<Car, Void, Void>{
        @Override
        protected Void doInBackground(Car... cars) {
            database.getCarDao().updateCar(cars[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            carsAdapter.notifyDataSetChanged();
        }
    }
}
