package com.example.uallas.uallet.controller;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.example.uallas.uallet.R;
import com.example.uallas.uallet.db.Controller.CountryController;
import com.example.uallas.uallet.db.Controller.TravelController;
import com.example.uallas.uallet.lib.TextFormatter;
import com.example.uallas.uallet.model.Country;
import com.example.uallas.uallet.model.Travel;

import java.text.ParseException;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class UalletWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        TravelController travelController = new TravelController(context);
        final Travel travel = travelController.getCurrentTravel();
        CountryController countryController = new CountryController(context);
        Country country = countryController.loadById(travel.getCountry());

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.uallet_widget);
        Locale travelCurrencyLocale = Locale.getDefault();

        if (travel.getId() > 0) {
            views.setViewVisibility(R.id.rl_no_travel_yet, View.GONE);
            views.setViewVisibility(R.id.rl_current_travel, View.VISIBLE);

            views.setTextViewText(R.id.tv_current_travel, travel.getLocation());
            views.setImageViewResource(R.id.iv_flag, context.getResources().getIdentifier(country.getImageName(),
                    "drawable", context.getPackageName()));

            Double budget = travelController.getBudget(travel.getId());
            try {
                views.setTextViewText(R.id.tv_budget, TextFormatter.formatCurrencyFromDouble(budget, travelCurrencyLocale));
                Double spending = travelController.getExpense(travel.getId());
                views.setTextViewText(R.id.tv_spending, TextFormatter.formatCurrencyFromDouble(spending, travelCurrencyLocale));
                Double balance = budget - spending;
                views.setTextViewText(R.id.tv_balance, TextFormatter.formatCurrencyFromDouble(balance, travelCurrencyLocale));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(context, TravelActivity.class);
            intent.putExtra("idTravel", travel.getId());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.rl_current_travel, pendingIntent);

        } else {
            views.setViewVisibility(R.id.rl_no_travel_yet, View.VISIBLE);
            views.setViewVisibility(R.id.rl_current_travel, View.GONE);

            Intent intent = new Intent(context, AddTravelActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.rl_no_travel_yet, pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

