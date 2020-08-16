package com.example.sqllitedatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqllitedatabase.models.Car;

import java.util.ArrayList;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Car> cars;
    private MainActivity mainActivity;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTextView;
        public TextView priceTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }

    public CarsAdapter(Context context, ArrayList<Car> cars, MainActivity mainActivity) {
        this.context = context;
        this.cars = cars;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsAdapter.MyViewHolder holder, final int position) {
        final Car car = cars.get(position);

        holder.nameTextView.setText(car.getName());
        holder.priceTextView.setText(car.getPrice() + "$");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addAndEditCars(true,car,position); //если мы хотим обновить машину
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}
