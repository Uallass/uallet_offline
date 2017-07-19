package com.example.uallas.uallet.lib;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CurrencyTextWatcher implements TextWatcher {
	EditText editText;

	public static Map<String, String> MYCURRENCIES;

	/**
	 * 
	 */
	public CurrencyTextWatcher(EditText et) {
        MYCURRENCIES = new HashMap<String, String>();
		Locale[] locs = Locale.getAvailableLocales();

		for(Locale locale : locs) {
			try {
				Currency currency = Currency.getInstance(locale);
				if (currency != null) {
					MYCURRENCIES.put(currency.getCurrencyCode(), currency.getSymbol());
				}
			} catch (Exception e) {
			}
		}
		editText = et;
	}

	public void afterTextChanged(Editable s) {
		if (s != null) {

			editText.removeTextChangedListener(this);

			// Coloca a formatação nos campos de faturamento
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(
					Locale.getDefault());
			otherSymbols.setDecimalSeparator(',');
			otherSymbols.setGroupingSeparator('.');
			Currency currency = Currency.getInstance(Locale.getDefault());
			String curSymbol = MYCURRENCIES.get(currency.getCurrencyCode());

			DecimalFormat formatter = new DecimalFormat(curSymbol + " #,##0.00",
					otherSymbols);

			String formated = formatter.format(limparString(s.toString()));

			editText.setText(formated);
			editText.setSelection(formated.length());

			editText.addTextChangedListener(this);
		}

	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	/**
	 * Método para se extrair um número Double de uma String. Se a String for
	 * vazia, retorna 0.0
	 * 
	 * @param valor
	 * @return
	 */
	public static Double limparString(String valor) {
		String cleanString = valor.replaceAll("[R$,.\\s]", "");
		Double valorRetorno;
		// se não for vazia
		if (!cleanString.equals("")) {
			valorRetorno = (Double.parseDouble(cleanString) / 100.0);
		} else {
			valorRetorno = 0.0;
		}

		return valorRetorno;
	}

}
