package com.example.uallas.uallet.lib;

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

}
