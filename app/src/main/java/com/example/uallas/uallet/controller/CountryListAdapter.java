package com.example.uallas.uallet.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uallas.uallet.R;
import com.example.uallas.uallet.model.Category;
import com.example.uallas.uallet.model.Country;

import java.util.List;

/**
 * Created by Uallas on 15/06/2017.
 */

public class CountryListAdapter extends BaseAdapter {

    private List<Country> countries;
    private Context context;

    public CountryListAdapter(Context context, List<Country> countries) {
        this.context = context;
        this.countries = countries;
    }

    public static class ViewHolder {
        public ImageView ivImage;
        public TextView tvDescription;
    }

    @Override
    public View getView(int position, View rootView, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if(rootView == null) {
            holder = new ViewHolder();
            rootView = View.inflate(context, R.layout.country_list_item, null);

            holder.ivImage = (ImageView) rootView.findViewById(R.id.iv_flag);
            holder.tvDescription = (TextView) rootView.findViewById(R.id.tv_country);

            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }

        if(countries.get(position).getId() != 0) {
            holder.ivImage.setImageResource(rootView.getResources().getIdentifier(countries.get(position).getImageName(), "drawable", context.getPackageName()));
        }
        holder.tvDescription.setText(countries.get(position).getDescription());

        return rootView;
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Country getItem(int i) {
        return countries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
