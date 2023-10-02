package com.example.weatherapiapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityListViewHolder> {

    public class CityListViewHolder extends RecyclerView.ViewHolder {

        public TextView time;
        public ImageView image;

        public CityListViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.txt_city_temp);
            image = itemView.findViewById(R.id.img_city_condition);
        }
    }

    private Context mContext;
    private List<WeatherReportModel> citiesList;

    public CityListAdapter(Context context, List<WeatherReportModel> citiesList) {
        this.mContext = context;
        this.citiesList = citiesList;
    }

    @NonNull
    @Override
    public CityListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_widget, parent, false);
        return new CityListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CityListViewHolder holder, int position) {
        WeatherReportModel weatherReportModel = citiesList.get(position);
        holder.time.setText(weatherReportModel.getTime());
        Glide.with(mContext).load(R.drawable.plus_button).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

}
