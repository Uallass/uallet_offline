package com.example.uallas.uallet;

import android.content.Context;
import android.widget.EditText;

import com.example.uallas.uallet.lib.CurrencyTextWatcher;
import com.example.uallas.uallet.lib.TextFormatter;

import org.junit.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Uallas on 10/03/2018.
 */

public class FormatTest {

    @Test
    public void formatCurrencyCanada() {

        assertEquals("CAD10,53", TextFormatter.formatCurrencyFromString("10,53", Locale.CANADA));
    }

    @Test
    public void formatCurrencyUsa() {

        // When the value doesn't have decimals, it has to format this way because the CurrencyTextWatcher need it.
        assertEquals("US$10,00", TextFormatter.formatCurrencyFromString("1000", Locale.US));
    }

    @Test
    public void formatCurrencyBrazil() {

        assertEquals("R$1.043,00", TextFormatter.formatCurrencyFromString("1043,00", new Locale("pt", "BR")));
    }

    @Test
    public void formatCurrencyFromDouble() throws ParseException {

        assertEquals("US$1.000,00", TextFormatter.formatCurrencyFromDouble(1000, Locale.US));
    }

    @Test
    public void cleanCurrency() throws ParseException {

        assertEquals(12.45, TextFormatter.cleanCurrency("CAD$12,45"));
    }


}
