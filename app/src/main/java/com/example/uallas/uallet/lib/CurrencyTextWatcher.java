package com.example.uallas.uallet.lib;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Locale;

public class CurrencyTextWatcher implements TextWatcher {
    EditText editText;

    public Locale locale;

    /**
     *
     */
    public CurrencyTextWatcher(EditText et, Locale locale) {
        this.locale = locale;

        editText = et;
    }

    public void afterTextChanged(Editable s) {
        if (s != null) {

            editText.removeTextChangedListener(this);

            String formatted = TextFormatter.formatCurrencyFromString(s.toString(), locale);

            editText.setText(formatted);
            editText.setSelection(editText.getText().length());

            editText.addTextChangedListener(this);
        }

    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }


}
