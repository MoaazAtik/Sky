package com.example.weatherapiapp;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityListViewHolder> {

    private static final String TAG = "CityListAdapter";

//    public class CityListViewHolder extends RecyclerView.ViewHolder {
    public class CityListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView txtTemp;
        private TextView txtHTemp;
        private TextView txtLTemp;
        private TextView txtCityCountry;
        private TextView txtCondition;
        private ImageView imgCondition;

//        MyItemTouchHandler m = new MyItemTouchHandler()
        public CityListViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTemp = itemView.findViewById(R.id.txt_city_temp);
            txtHTemp = itemView.findViewById(R.id.txt_city_h_temp);
            txtLTemp = itemView.findViewById(R.id.txt_city_l_temp);
            txtCityCountry = itemView.findViewById(R.id.txt_city_country);
            txtCondition = itemView.findViewById(R.id.txt_city_condition);
            imgCondition = itemView.findViewById(R.id.img_city_condition);
            itemView.setOnCreateContextMenuListener(this);
//            itemView.setOnCreateContextMenuListener(mOnCreateContextMenuListener);
            //            itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) new MyItemTouchHandler());
        }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            // take this to the handler or adapter/ no move it to the adapter
            menu.add(0, 0, 0, "Setd as home")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            Log.d(TAG, "onMenuItemClick: adapter 0");
                            return true;
                        }
                    });
            menu.add(0, 0, 1, "Removed city")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            Log.d(TAG, "onMenuItemClick: adapter 1");

//                            int position = recyclerView.getChildAdapterPosition(childView);
                            position = getAdapterPosition();
                            Log.d(TAG, "onMenuItemClick: position " + position);
                            removeItem(position);
                            return true;
                        }
                    });
    }

    //        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
////            menu.add(0, 0, getAdapterPosition(), "Set as home");
////            menu.add(this.getAdapterPosition(), v.getId(), 0, "Call");
////            menu.add("Set as home");
////            menu.add("Remove city");
//            menu.add(0, 0, 0, "Set as home");
//            menu.add(0, 0, 1, "Remove city");
//            Log.d(TAG, "onCreateContextMenu: ");
////            View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
////            if (childView != null) {
////                int position = recyclerView.getChildAdapterPosition(childView);
//            position = getAdapterPosition();
//
//        }
    }
    private int position;
    public int getPosition() {
        return position;
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
        String cityCountry = weatherReportModel.getCity() + ", " + weatherReportModel.getCountry();
        holder.txtCityCountry.setText(cityCountry);
        String conditionDescription = weatherReportModel.getConditionDescription();
        holder.txtCondition.setText(conditionDescription);
        int conditionImageId = weatherReportModel.getConditionImageId();
        Glide.with(mContext).load(conditionImageId).into(holder.imgCondition);
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

//
//    private View.OnCreateContextMenuListener mOnCreateContextMenuListener = new View.OnCreateContextMenuListener() {
//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            // take this to the handler or adapter/ no move it to the adapter
//            menu.add(0, 0, 0, "Setd as home")
//                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(@NonNull MenuItem item) {
//                            Log.d(TAG, "onMenuItemClick: adapter 0");
//                            return true;
//                        }
//                    });
//            menu.add(0, 0, 1, "Removed city")
//                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(@NonNull MenuItem item) {
//                            Log.d(TAG, "onMenuItemClick: adapter 1");
//
////                            int position = recyclerView.getChildAdapterPosition(childView);
//                            position = getAdapterPosition();
//                            removeItem();
//                            return true;
//                        }
//                    });
//        }
//    };
////    @Override
////    public boolean onMenuItemClick(@NonNull MenuItem item) {
////        switch (item.getOrder()) {
////            case 0:
////                Log.d(TAG, "onContextItemSelected: 0");
////                return true;
////            case 1:
////                Log.d(TAG, "onContextItemSelected: 1");
//////                Log.d(TAG, "Current rv item position " + itemTouchHandler.getCurrentViewPosition());
//////                removeItem(itemTouchHandler.getCurrentViewPosition());
////                int p = adapter.getPosition();
////                Log.d(TAG, "Current rv item position " + p);
////                removeItem(p);
////                return true;
////        }
////        Log.d(TAG, "onContextItemSelected: default");
////        return false;
////    }
    private void removeItem(int position) {
        citiesList.remove(position);
//        adapter.notifyItemRemoved(position);
        notifyItemRemoved(position);
        // Show a confirmation message or perform other actions
    }
//    //        holder.view.setOnCreateContextMenuListener { contextMenu, _, _ ->
////                contextMenu.add("Add").setOnMenuItemClickListener {
////            longToast("I'm pressed for the item at position => $position")
////            true
////        }
////        }
//    MenuItem.OnMenuItemClickListener m = new MenuItem.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(@NonNull MenuItem item) {
//            return false;
//        }
//    };

}
