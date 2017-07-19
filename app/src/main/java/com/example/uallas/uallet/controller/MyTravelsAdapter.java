package com.example.uallas.uallet.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uallas.uallet.R;
import com.example.uallas.uallet.db.Controller.CountryController;
import com.example.uallas.uallet.lib.TextFormatter;
import com.example.uallas.uallet.model.Country;
import com.example.uallas.uallet.model.TipoDado;
import com.example.uallas.uallet.model.Travel;

import java.util.List;

/**
 * Created by Uallas on 15/06/2017.
 */

public class MyTravelsAdapter extends BaseAdapter {

    private List<Travel> travels;
    private Context context;

    public MyTravelsAdapter(Context context, List<Travel> travels) {
        this.context = context;
        this.travels = travels;
    }

    public static class ViewHolder {
        public ImageView ivFlag;
        public TextView tvCountry;
        public TextView tvDate;
    }

    @Override
    public View getView(int position, View rootView, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if(rootView == null) {
            holder = new ViewHolder();
            rootView = View.inflate(context, R.layout.travel_list_item, null);

            holder.ivFlag = (ImageView) rootView.findViewById(R.id.iv_flag);
            holder.tvCountry = (TextView) rootView.findViewById(R.id.tv_country);
            holder.tvDate = (TextView) rootView.findViewById(R.id.tv_date);

            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }

        CountryController countryController = new CountryController(context);
        Country country = countryController.loadById(travels.get(position).getCountry());

        holder.tvCountry.setText(travels.get(position).getLocation() + " - " + country.getDescription());
        holder.tvDate.setText(TextFormatter.formatDate(travels.get(position).getDateBeginning(), TipoDado.DATA));
        holder.ivFlag.setImageResource(context.getResources().getIdentifier(country.getImageName(), "drawable", context.getPackageName()));

        return rootView;
    }

    @Override
    public int getCount() {
        return travels.size();
    }

    @Override
    public Travel getItem(int i) {
        return travels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
