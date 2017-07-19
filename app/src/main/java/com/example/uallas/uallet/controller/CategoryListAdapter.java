package com.example.uallas.uallet.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uallas.uallet.R;
import com.example.uallas.uallet.lib.TextFormatter;
import com.example.uallas.uallet.model.Category;
import com.example.uallas.uallet.model.TipoDado;
import com.example.uallas.uallet.model.Travel;

import java.util.List;

/**
 * Created by Uallas on 15/06/2017.
 */

public class CategoryListAdapter extends BaseAdapter {

    private List<Category> categories;
    private Context context;

    public CategoryListAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
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
            rootView = View.inflate(context, R.layout.category_list_item, null);

            holder.ivImage = (ImageView) rootView.findViewById(R.id.iv_image);
            holder.tvDescription = (TextView) rootView.findViewById(R.id.tv_description);

            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }

        holder.ivImage.setImageResource(rootView.getResources().getIdentifier(categories.get(position).getImageName(), "drawable", context.getPackageName()));
        holder.tvDescription.setText(categories.get(position).getDescCategory());

        return rootView;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
