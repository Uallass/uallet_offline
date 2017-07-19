package com.example.uallas.uallet.controller;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.uallas.uallet.R;
import com.example.uallas.uallet.db.Controller.CategoryController;
import com.example.uallas.uallet.lib.TextFormatter;
import com.example.uallas.uallet.model.Category;
import com.example.uallas.uallet.model.Direction;
import com.example.uallas.uallet.model.TipoDado;
import com.example.uallas.uallet.model.Transaction;
import com.example.uallas.uallet.model.TransactionGroup;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

/**
 * Created by Uallas on 18/06/2017.
 */

public class TravelHistoryAdapter extends BaseAdapter {

    private Context context;
    private List<TransactionGroup> transactions;
    private TravelHistoryListener listener;

    public TravelHistoryAdapter(Context context, List<TransactionGroup> transactions, TravelHistoryListener listener) {
        this.context = context;
        this.transactions = transactions;
        this.listener = listener;
    }

    public static class ViewHolder {
        public LinearLayout llHistoryListItem;
        public LinearLayout llTitulo;
        public TextView tvDate;
        public CardView cvHistory;
        public LinearLayout llHistory;
        public int ref;
    }

    @Override
    public View getView(int position, View rootView, ViewGroup viewGroup) {

        ViewHolder holder;

        if (rootView == null) {
            holder = new ViewHolder();
            rootView = View.inflate(context, R.layout.history_list_item, null);

            holder.llHistoryListItem = (LinearLayout) rootView.findViewById(R.id.ll_history_list_item);
            holder.llTitulo = (LinearLayout) rootView.findViewById(R.id.ll_titulo);
            holder.tvDate = (TextView) rootView.findViewById(R.id.tv_date);
            holder.cvHistory = (CardView) rootView.findViewById(R.id.cv_history_item);
            holder.llHistory = (LinearLayout) rootView.findViewById(R.id.ll_history_item);
            holder.ref = position;
            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }

        TransactionGroup trans = transactions.get(position);

        // create shadow below the last item of the day

        holder.tvDate.setText(TextFormatter.formatDate(trans.getDate(), TipoDado.DATA));

        LinearLayout.LayoutParams llParamsRow = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(76));
        LinearLayout llRow;

        holder.llHistory.removeAllViews();

        for (final Transaction transaction : trans.getTransactions()) {
            llRow = new LinearLayout(context);

            LinearLayout.LayoutParams llParamsCategory = new LinearLayout.LayoutParams(dpToPx(32), dpToPx(32));
            llParamsCategory.setMargins(0, 0, dpToPx(8), 0);
            ImageView ivCategory = new ImageView(context);
            ivCategory.setLayoutParams(llParamsCategory);

            if(transaction.getCodCategory() == 0) {
                ivCategory.setImageResource(R.drawable.cash);
            } else {
                CategoryController categoryController = new CategoryController(context);
                Category category = categoryController.loadById(transaction.getCodCategory(), Locale.getDefault().getLanguage());
                ivCategory.setImageResource(context.getResources().getIdentifier(category.getImageName(), "drawable", context.getPackageName()));
            }

            LinearLayout.LayoutParams llParamsDescription = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 100f);
            TextView tvDescription = new TextView(context);
            tvDescription.setLayoutParams(llParamsDescription);
            tvDescription.setText(transaction.getDescription());

            LinearLayout.LayoutParams llParamsValue = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llParamsValue.setMargins(dpToPx(4), 0, 0, 0);
            TextView tvValue = new TextView(context);
            tvValue.setLayoutParams(llParamsValue);
            if(transaction.getDirection().equals(Direction.INCOME) || transaction.getDirection().equals(Direction.INICIAL_BUDGET)) {
                tvValue.setText("+ " + TextFormatter.formatCurrency(transaction.getValue()));
                tvValue.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            } else {
                tvValue.setText("- " + TextFormatter.formatCurrency(transaction.getValue()));
                tvValue.setTextColor(context.getResources().getColor(R.color.redDark));
            }

            // llWrap is the row
            llRow.addView(ivCategory);
            llRow.addView(tvDescription);
            llRow.addView(tvValue);

            llRow.setGravity(Gravity.CENTER_VERTICAL);
            llRow.setOrientation(LinearLayout.HORIZONTAL);
            llRow.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
            llRow.setBackgroundResource(R.drawable.bg_list_item);

            // When long click the row, open a menu to edit or delete the data
            final LinearLayout finalLlRow = llRow;
            llRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popup = new PopupMenu(context, finalLlRow);
                    popup.inflate(R.menu.travel_history_menu_item);

                    // creating the actions
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getTitle().equals(context.getResources().getString(R.string.edit))) {
                                if (transaction.getDirection().equals(Direction.INCOME) ||
                                        transaction.getDirection().equals(Direction.INICIAL_BUDGET)) {
                                    listener.editIncome(transaction);
                                } else {
                                    listener.editOutcome(transaction);
                                }
                            } else if(item.getTitle().equals(context.getResources().getString(R.string.delete))) {
                                listener.deleteTransaction(transaction);
                            }
                            return true;
                        }
                    });

                    popup.show();
                    return true;
                }
            });

            holder.llHistory.addView(llRow, llParamsRow);
        }

        return rootView;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int i) {
        return transactions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public interface TravelHistoryListener {
        public void editIncome(Transaction transaction);
        public void editOutcome(Transaction transaction);
        public void deleteTransaction(Transaction transaction);
    }
}
