package com.example.uallas.uallet.lib;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.uallas.uallet.controller.UalletWidget;

import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Uallas on 10/03/2018.
 */

public class Utils {

    public static HashMap<String, String> getAllCurrencies() {

        HashMap<String, String> myCurrencies = new HashMap<>();

        Locale[] locs = Locale.getAvailableLocales();

        for (Locale local : locs) {
            try {
                Currency currency = Currency.getInstance(local);
                if (currency != null) {
                    myCurrencies.put(currency.getCurrencyCode(), currency.getSymbol());
                }
            } catch (Exception e) {
            }
        }

        return myCurrencies;
    }

    public static void updateWidget(Context context) {

        Intent intent = new Intent(context, UalletWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(new ComponentName(context, UalletWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }
}
