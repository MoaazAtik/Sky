package com.example.weatherapiapp;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityListViewHolder> {

    private static final String TAG = "CityListAdapter";

    private Context mContext;
    private List<WeatherReportModelShort> citiesList;
    private RecyclerView mRecyclerView;

    public CityListAdapter(Context context, List<WeatherReportModelShort> citiesList) {
        this.mContext = context;
        this.citiesList = citiesList;
    }

    public class CityListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

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
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.add(0, 0, 0, "Set as home")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            Log.d(TAG, "onMenuItemClick: adapter 0");
                            return true;
                        }
                    });
            menu.add(0, 0, 1, "Remove city")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            Log.d(TAG, "onMenuItemClick: adapter 1");
                            Log.d(TAG, "onMenuItemClick: position " + getAdapterPosition());
                            removeItem(getAdapterPosition());
                            return true;
                        }
                    });
        }
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
        String cityCountry = weatherReportModel.getCity() + ", " + weatherReportModel.getCountry();
        holder.txtCityCountry.setText(cityCountry);
        String conditionDescription = weatherReportModel.getConditionDescription();
        holder.txtCondition.setText(conditionDescription);
        int conditionImageId = weatherReportModel.getConditionImageId();
        Glide.with(mContext).load(conditionImageId).into(holder.imgCondition);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    private void removeItem(int position) {
        citiesList.remove(position);
        notifyItemRemoved(position);
        Snackbar snackbar = Snackbar.make(mRecyclerView, "City removed", BaseTransientBottomBar.LENGTH_LONG);
        snackbar.show();
    }

}
