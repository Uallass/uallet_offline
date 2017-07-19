/**
 * 
 */
package com.example.uallas.uallet.lib;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author rfonseca
 * 
 */
public class PhoneTextWatcher implements TextWatcher {
	private EditText editText;
	private String current;

	public PhoneTextWatcher(EditText et) {
		this.editText = et;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable s) {
		if (s != null && !s.toString().equals(current)) {
			editText.removeTextChangedListener(this);

			String formated = TextFormatter.formatPhoneNumber(s.toString());

			current = formated.toString();
			editText.setText(current);
			editText.setSelection(current.length());

			editText.addTextChangedListener(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence,
	 * int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int,
	 * int, int)
	 */
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

}
