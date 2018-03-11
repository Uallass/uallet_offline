package com.example.uallas.uallet.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uallas.uallet.R;
import com.example.uallas.uallet.db.Controller.CountryController;
import com.example.uallas.uallet.db.Controller.TransactionController;
import com.example.uallas.uallet.db.Controller.TravelController;
import com.example.uallas.uallet.lib.CurrencyTextWatcher;
import com.example.uallas.uallet.lib.DatePickerHelper;
import com.example.uallas.uallet.lib.ParserHelper;
import com.example.uallas.uallet.lib.TextFormatter;
import com.example.uallas.uallet.model.Country;
import com.example.uallas.uallet.model.Direction;
import com.example.uallas.uallet.model.TipoDado;
import com.example.uallas.uallet.model.Transaction;
import com.example.uallas.uallet.model.Travel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Uallas on 15/06/2017.
 */

public class AddTravelActivity extends AppCompatActivity {

    private AppCompatActivity activity;
    private Locale travelCurrencyLocale;

    private Spinner spCountry;
    private EditText etLocation;
    private EditText etBeginning;
    private EditText etEnd;
    private EditText etInicialBudget;

    private Travel travel;
    private List<Country> countries;

    private TravelController travelController;
    private TransactionController transactionController;
    private CountryController countryController;

    private int idTravel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activity = this;

        travelController = new TravelController(getApplicationContext());
        countryController = new CountryController(getApplicationContext());
        transactionController = new TransactionController(getApplicationContext());

        Intent intent = getIntent();
        if(intent != null) {
            idTravel = intent.getIntExtra("idTravel", 0);
        }

        setContentView(R.layout.add_travel_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        travelCurrencyLocale = Locale.getDefault();

        buildViews();
        setViews();
    }

    private void buildViews() {

        spCountry = (Spinner) findViewById(R.id.sp_country);
        etLocation = (EditText) findViewById(R.id.et_location);
        etBeginning = (EditText) findViewById(R.id.et_beginning);
        etEnd = (EditText) findViewById(R.id.et_end);
        etInicialBudget = (EditText) findViewById(R.id.et_inicial_budget);
    }

    private void setViews() {

        countries = new ArrayList<Country>();
        countries.add(new Country(0, "", getResources().getString(R.string.select), ""));
        countries.addAll(countryController.load());
        CountryListAdapter adapter = new CountryListAdapter(getApplicationContext(), countries);
        spCountry.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spCountry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

        etBeginning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerHelper datePicker = new DatePickerHelper(activity, etBeginning);
                datePicker.createDialog().show();
            }
        });
        etEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerHelper datePicker = new DatePickerHelper(activity, etEnd);
                datePicker.createDialog().show();
            }
        });

        etInicialBudget.addTextChangedListener(new CurrencyTextWatcher(etInicialBudget, travelCurrencyLocale));
        etInicialBudget.setText("0");
        // if it is editing a travel
        if(idTravel > 0) {
            TransactionController transactionController = new TransactionController(getApplicationContext());
            travel = travelController.loadById(idTravel);

            for(int i = 0; i < countries.size(); i++) {
                if(travel.getCountry() == countries.get(i).getId()) {
                    spCountry.setSelection(i);
                }
            }

            etLocation.setText(travel.getLocation());
            etBeginning.setText(TextFormatter.formatDate(travel.getDateBeginning(), TipoDado.DATA));
            etEnd.setText(travel.getDateEnd() != null ? TextFormatter.formatDate(travel.getDateEnd(), TipoDado.DATA) : "");

            try {
                etInicialBudget.setText(TextFormatter.formatCurrencyFromDouble(transactionController.loadInicialBudget(idTravel), travelCurrencyLocale));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            etInicialBudget.setEnabled(false);
        } else {
            travel = new Travel();
        }

    }

    @SuppressWarnings("deprecation")
    private boolean validateFields() {
        boolean isOk = true;
        String msg = "";
        int backgroundHighlight = R.drawable.bg_highlight_field;
        int backgroundNormal = android.R.drawable.editbox_background_normal;

        spCountry.setBackgroundResource(0);
        etLocation.setBackgroundResource(backgroundNormal);
        etBeginning.setBackgroundResource(backgroundNormal);
        etEnd.setBackgroundResource(backgroundNormal);
        etInicialBudget.setBackgroundResource(backgroundNormal);

        if(spCountry.getSelectedItemPosition() == 0) {
            isOk = false;
            spCountry.setBackgroundResource(backgroundHighlight);
        }

        if(etLocation.getText().toString().isEmpty()) {
            isOk = false;
            etLocation.setBackgroundResource(backgroundHighlight);
        }

        if(etBeginning.getText().toString().isEmpty()) {
            isOk = false;
            etBeginning.setBackgroundResource(backgroundHighlight);
        }

        if(etEnd.getText().toString().isEmpty()) {
            isOk = false;
            etEnd.setBackgroundResource(backgroundHighlight);
        }

        if(!etBeginning.getText().toString().isEmpty() &&
                !etEnd.getText().toString().isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", getResources().getConfiguration().locale);
                Date dateBeginning = dateFormat.parse(etBeginning.getText().toString());
                Date dateEnd = dateFormat.parse(etEnd.getText().toString());

                if(dateBeginning.getTime() > dateEnd.getTime()) {
                    isOk = false;

                    etBeginning.setBackgroundResource(backgroundHighlight);
                    etEnd.setBackgroundResource(backgroundHighlight);

                    msg += System.getProperty("line.separator") + getResources().getString(R.string.date_end_smaller_than_beginning);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            if(etInicialBudget.getText().toString().isEmpty() ||
                    (TextFormatter.cleanCurrency(etInicialBudget.getText().toString()).equals(0))) {
                isOk = false;
                etInicialBudget.setBackgroundResource(backgroundHighlight);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(msg.isEmpty()) {
            msg = getResources().getString(R.string.fill_fields);
        }

        if(!isOk) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(msg)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.create();
            builder.show();
        }

        return isOk;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_travel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.done) {
            if(validateFields()) {
                prepareForSending();
                // if it is editing a travel
                if (idTravel > 0) {
                    updateTravel();
                } else {
                    insertTravel();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareForSending() {
        travel.setCountry(countries.get(spCountry.getSelectedItemPosition()).getId());
        travel.setDateBeginning(ParserHelper.parseDate(etBeginning.getText().toString(), TipoDado.DATA));
        travel.setDateEnd(ParserHelper.parseDate(etEnd.getText().toString(), TipoDado.DATA));
        travel.setLocation(etLocation.getText().toString());
        travel.setFinished(false);
    }

    private void updateTravel() {
        int returnId = travelController.update(travel);
        if (returnId > -1) {
            Toast.makeText(getApplicationContext(), getText(R.string.success_update), Toast.LENGTH_LONG).show();
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.error_update), Toast.LENGTH_LONG).show();
        }
    }

    private void insertTravel() {
        Long returnId = travelController.insert(travel);
        if (returnId > -1) {

            Toast.makeText(getApplicationContext(), getText(R.string.success_insert), Toast.LENGTH_LONG).show();

            Transaction transaction = new Transaction();
            transaction.setCodTravel(returnId.intValue());
            transaction.setCodCategory(13);
            transaction.setDirection(Direction.INICIAL_BUDGET);
            transaction.setDescription(getResources().getString(R.string.inicial_budget));
            transaction.setDate(travel.getDateBeginning());
            try {
                transaction.setValue(TextFormatter.cleanCurrency(etInicialBudget.getText().toString()).doubleValue());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Long returnTransaction = transactionController.insert(transaction);

            if(returnTransaction < 0) {
                Toast.makeText(getApplicationContext(), getText(R.string.error_inicial_budget), Toast.LENGTH_LONG).show();
            }

            finish();

        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.error_insert), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

}
