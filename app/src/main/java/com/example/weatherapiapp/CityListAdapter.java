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

        private TextView txtTemp;
        private TextView txtHTemp;
        private TextView txtLTemp;
        private TextView txtCityCountry;
        private TextView txtCondition;
        private ImageView imgCondition;

        public CityListViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTemp = itemView.findViewById(R.id.txt_city_temp);
            txtHTemp = itemView.findViewById(R.id.txt_city_h_temp);
            txtLTemp = itemView.findViewById(R.id.txt_city_l_temp);
            txtCityCountry = itemView.findViewById(R.id.txt_city_country);
            txtCondition = itemView.findViewById(R.id.txt_city_condition);
            imgCondition = itemView.findViewById(R.id.img_city_condition);
        }
    }

    private Context mContext;
    private List<WeatherReportModelShort> citiesList;

    public CityListAdapter(Context context, List<WeatherReportModelShort> citiesList) {
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

        WeatherReportModelShort weatherReportModel = citiesList.get(position);
        String temp = (int) weatherReportModel.getTemperature_2m() + "°";
        holder.txtTemp.setText(temp);
        String hTemp = "H:" + (int) weatherReportModel.getTemperature_2m_max() + "°";
        holder.txtHTemp.setText(hTemp);
        String lTemp = "L:" + (int) weatherReportModel.getTemperature_2m_min() + "°";
        holder.txtLTemp.setText(lTemp);
        String cityCountry = "cityy"; // = ....
        holder.txtCityCountry.setText(cityCountry);
        String condition = weatherReportModel.getCondition();
        holder.txtCondition.setText(condition);
        Glide.with(mContext).load(R.drawable.plus_button).into(holder.imgCondition);
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

}
