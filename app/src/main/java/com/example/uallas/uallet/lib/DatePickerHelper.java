/**
 * 
 */
package com.example.uallas.uallet.lib;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.uallas.uallet.model.TipoDado;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Uallas on 17/06/2017.
 */

public class DatePickerHelper implements
		DatePickerDialog.OnDateSetListener {

	private Activity activity;
	private EditText editText;

	private boolean dateChanged;

	public DatePickerHelper(Activity activity, EditText editText) {
		this.activity = activity;
		this.editText = editText;
	}

	public Dialog createDialog() {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(activity, this,
                year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {

		if (dateChanged) {
			return;
		} else {
            Date date = new GregorianCalendar(year, month, day)
                    .getTime();
            editText.setText(TextFormatter.formatDate(date, TipoDado.DATA));
			dateChanged = true;
		}
	}

}
