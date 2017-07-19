package com.example.uallas.uallet.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uallas.uallet.R;
import com.example.uallas.uallet.db.Controller.CategoryController;
import com.example.uallas.uallet.db.Controller.CountryController;
import com.example.uallas.uallet.db.Controller.TransactionController;
import com.example.uallas.uallet.db.Controller.TravelController;
import com.example.uallas.uallet.lib.CurrencyTextWatcher;
import com.example.uallas.uallet.lib.DatePickerHelper;
import com.example.uallas.uallet.lib.ParserHelper;
import com.example.uallas.uallet.lib.TextFormatter;
import com.example.uallas.uallet.model.Category;
import com.example.uallas.uallet.model.Country;
import com.example.uallas.uallet.model.Direction;
import com.example.uallas.uallet.model.TipoDado;
import com.example.uallas.uallet.model.Transaction;
import com.example.uallas.uallet.model.TransactionGroup;
import com.example.uallas.uallet.model.Travel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Uallas on 17/06/2017.
 */

public class TravelActivity extends AppCompatActivity implements TravelHistoryAdapter.TravelHistoryListener {

    private Activity activity;
    private FragmentManager fragmentManager;

    private TextView tvLocation;
    private TextView tvBudget;
    private TextView tvSpending;
    private TextView tvBalance;
    private LinearLayout btAddMoney;
    private LinearLayout btAddExpense;
    private ImageView ivFlag;
    private ListView lvHistory;

    // Popup Expense views
    private PopupWindow popupAddExpense;
    private View layoutPopupAddExpense;
    private Spinner spCategoryExpense;
    private EditText etDateExpense;
    private EditText etAmountExpense;
    private EditText etDescriptionExpense;
    private LinearLayout btSaveExpense;

    // Popup Money views
    private PopupWindow popupAddMoney;
    private View layoutPopupAddMoney;
    private EditText etDateMoney;
    private EditText etAmountMoney;
    private EditText etDescriptionMoney;
    private LinearLayout btSaveMoney;

    private List<TransactionGroup> transactions;
    private TravelHistoryAdapter travelHistoryAdapter;

    private TravelController travelController;
    private TransactionController transactionController;
    private CountryController countryController;
    private CategoryController categoryController;

    private int idTravel;
    private Travel travel;
    private Transaction transaction;
    private List<Category> categories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        this.fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.travel_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        travelController = new TravelController(getApplicationContext());
        countryController = new CountryController(getApplicationContext());
        transactionController = new TransactionController(getApplicationContext());
        categoryController = new CategoryController(getApplicationContext());

        Intent intent = getIntent();
        if(intent != null) {
            idTravel = intent.getIntExtra("idTravel", 0);
        }

        travel = travelController.loadById(idTravel);

        buildViews();
        setViews();
    }

    private void buildViews() {
        tvLocation = (TextView) findViewById(R.id.tv_travel_location);
        ivFlag = (ImageView) findViewById(R.id.iv_flag);
        tvBudget = (TextView) findViewById(R.id.tv_budget);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvSpending = (TextView) findViewById(R.id.tv_spending);
        lvHistory = (ListView) findViewById(R.id.lv_history);
        btAddExpense = (LinearLayout) findViewById(R.id.bt_add_expense);
        btAddMoney = (LinearLayout) findViewById(R.id.bt_add_money);
    }

    private void setViews() {
        tvLocation.setText(travel.getLocation());
        Country country = countryController.loadById(travel.getCountry());
        ivFlag.setImageResource(getResources().getIdentifier(country.getImageName(), "drawable", getPackageName()));

        transactions = new ArrayList<>();
        travelHistoryAdapter = new TravelHistoryAdapter(activity.getApplicationContext(), transactions, this);
        lvHistory.setAdapter(travelHistoryAdapter);

        updateScreenData();

        btAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildPopupExpense();
                buildPopupExpenseView(false);
            }
        });

        btAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildPopupMoney();
                buildPopupMoneyView(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.travel_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {
            Intent intent = new Intent(getApplicationContext(), AddTravelActivity.class);
            intent.putExtra("idTravel", travel.getId());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        travel = travelController.loadById(idTravel);

        setViews();
    }
    private void buildPopupMoney() {
        //This is the main view in the popup layout
        RelativeLayout viewGroup = (RelativeLayout) this.findViewById(
                R.id.rl_add_money_popup_wrap);
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        //inflate the layout
        layoutPopupAddMoney = layoutInflater.inflate(
                R.layout.add_money_popup, viewGroup);
        RelativeLayout popupBack = (RelativeLayout) layoutPopupAddMoney.findViewById(R.id.rl_add_money_popup_wrap);
        LinearLayout popupContent = (LinearLayout) layoutPopupAddMoney.findViewById(R.id.ll_add_money_popup);

        // Creating the PopupWindow in full screen
        popupAddMoney = new PopupWindow(layoutPopupAddMoney, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,true);
        popupAddMoney.setFocusable(true);

        // Clear the default translucent background
        popupAddMoney.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popupAddMoney.showAtLocation(layoutPopupAddMoney, Gravity.CENTER, 0, 0);

        // Dismiss the popup when the transparent background is clicked
        popupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddMoney.dismiss();
            }
        });

        // set nothing to the popup content click because it can't dismiss when clicked
        popupContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void buildPopupMoneyView(final boolean editing) {

        etDateMoney = (EditText) layoutPopupAddMoney.findViewById(R.id.et_date);
        etAmountMoney = (EditText) layoutPopupAddMoney.findViewById(R.id.et_amount);
        etDescriptionMoney = (EditText) layoutPopupAddMoney.findViewById(R.id.et_description);
        btSaveMoney = (LinearLayout) layoutPopupAddMoney.findViewById(R.id.bt_save_money);

        etDateMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerHelper datePicker = new DatePickerHelper(etDateMoney);
                datePicker.show(fragmentManager, "");
            }
        });

        etAmountMoney.addTextChangedListener(new CurrencyTextWatcher(etAmountMoney));

        btSaveMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateMoneyFields()) {
                    prepareForSendingMoney(editing);

                    if(editing) {
                        updateTransaction();
                    } else {
                        insertTransaction();
                    }

                    popupAddMoney.dismiss();
                }
            }
        });

        if(editing) {
            etAmountMoney.setText(TextFormatter.formatCurrency(transaction.getValue()));
            etDescriptionMoney.setText(transaction.getDescription());
            etDateMoney.setText(TextFormatter.formatDate(transaction.getDate(), TipoDado.DATA));
        } else {
            Calendar date = Calendar.getInstance();
            etDateMoney.setText(TextFormatter.formatDate(date.getTime(), TipoDado.DATA));
            etDescriptionMoney.setText(getResources().getString(R.string.income));
        }
    }

    private void buildPopupExpense() {
        //This is the main view in the popup layout
        RelativeLayout viewGroup = (RelativeLayout) this.findViewById(
                R.id.rl_add_expense_popup_wrap);
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        //inflate the layout
        layoutPopupAddExpense = layoutInflater.inflate(
                R.layout.add_expense_popup, viewGroup);
        RelativeLayout popupBack = (RelativeLayout) layoutPopupAddExpense.findViewById(R.id.rl_add_expense_popup_wrap);
        LinearLayout popupContent = (LinearLayout) layoutPopupAddExpense.findViewById(R.id.ll_add_expense_popup);

        // Creating the PopupWindow in full screen
        popupAddExpense = new PopupWindow(layoutPopupAddExpense, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,true);
        popupAddExpense.setFocusable(true);

        // Clear the default translucent background
        popupAddExpense.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popupAddExpense.showAtLocation(layoutPopupAddExpense, Gravity.CENTER, 0, 0);

        // Dismiss the popup when the transparent background is clicked
        popupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddExpense.dismiss();
            }
        });

        // set nothing to the popup content click because it can't dismiss when clicked
        popupContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void buildPopupExpenseView(final boolean editing) {

        spCategoryExpense = (Spinner) layoutPopupAddExpense.findViewById(R.id.sp_category);
        etDateExpense = (EditText) layoutPopupAddExpense.findViewById(R.id.et_date_expense);
        etAmountExpense = (EditText) layoutPopupAddExpense.findViewById(R.id.et_amount);
        etDescriptionExpense = (EditText) layoutPopupAddExpense.findViewById(R.id.et_description);
        btSaveExpense = (LinearLayout) layoutPopupAddExpense.findViewById(R.id.bt_save_expense);

        categories = categoryController.load(Locale.getDefault().getLanguage());
        CategoryListAdapter categoryAdapter = new CategoryListAdapter(getApplicationContext(), categories);
        spCategoryExpense.setAdapter(categoryAdapter);

        etDateExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerHelper datePicker = new DatePickerHelper(etDateExpense);
                datePicker.show(fragmentManager, "");
            }
        });

        etAmountExpense.addTextChangedListener(new CurrencyTextWatcher(etAmountExpense));

        btSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateExpenseFields()) {
                    prepareForSendingExpense(editing);

                    if(editing) {
                        updateTransaction();
                    } else {
                        insertTransaction();
                    }

                    popupAddExpense.dismiss();
                }
            }
        });

        if(editing) {
            for(int i = 0; i < categories.size(); i++) {
                if(transaction.getCodCategory() == categories.get(i).getId()) {
                    spCategoryExpense.setSelection(i);
                }
            }
            etAmountExpense.setText(TextFormatter.formatCurrency(transaction.getValue()));
            etDescriptionExpense.setText(transaction.getDescription());
            etDateExpense.setText(TextFormatter.formatDate(transaction.getDate(), TipoDado.DATA));
        } else {
            Calendar date = Calendar.getInstance();
            etDateExpense.setText(TextFormatter.formatDate(date.getTime(), TipoDado.DATA));
        }
    }

    private boolean validateExpenseFields() {
        boolean isOk = true;
        String msg = "";
        int backgroundHighlight = R.drawable.bg_highlight_field;
        int backgroundNormal = android.R.drawable.editbox_background_normal;

        etAmountExpense.setBackgroundResource(backgroundNormal);
        etDescriptionExpense.setBackgroundResource(backgroundNormal);

        if(etDescriptionExpense.getText().toString().isEmpty()) {
            isOk = false;
            etDescriptionExpense.setBackgroundResource(backgroundHighlight);
        }

        try {
            if(etAmountExpense.getText().toString().isEmpty() ||
                    TextFormatter.cleanCurrency(etAmountExpense.getText().toString(), Locale.getDefault()).equals(0)) {
                isOk = false;
                etAmountExpense.setBackgroundResource(backgroundHighlight);
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

    private void prepareForSendingExpense(boolean editing) {
        if(!editing) {
            transaction = new Transaction();
        }
        transaction.setCodCategory(categories.get(spCategoryExpense.getSelectedItemPosition()).getId());
        transaction.setCodTravel(travel.getId());
        transaction.setDescription(etDescriptionExpense.getText().toString());
        transaction.setDate(ParserHelper.parseDate(etDateExpense.getText().toString(), TipoDado.DATA));
        transaction.setDirection(Direction.OUTCOME);
        try {
            transaction.setValue(TextFormatter.cleanCurrency(etAmountExpense.getText().toString(), Locale.getDefault()).doubleValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private boolean validateMoneyFields() {
        boolean isOk = true;
        String msg = "";
        int backgroundHighlight = R.drawable.bg_highlight_field;
        int backgroundNormal = android.R.drawable.editbox_background_normal;

        etAmountMoney.setBackgroundResource(backgroundNormal);
        etDescriptionMoney.setBackgroundResource(backgroundNormal);

        if(etDescriptionMoney.getText().toString().isEmpty()) {
            isOk = false;
            etDescriptionMoney.setBackgroundResource(backgroundHighlight);
        }

        try {
            if(etAmountMoney.getText().toString().isEmpty() ||
                    TextFormatter.cleanCurrency(etAmountMoney.getText().toString(), Locale.getDefault()).equals(0)) {
                isOk = false;
                etAmountMoney.setBackgroundResource(backgroundHighlight);
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

    private void prepareForSendingMoney(boolean editing) {
        if(!editing) {
            transaction = new Transaction();
        }
        transaction.setCodCategory(13); // code 13 is the income code
        transaction.setCodTravel(travel.getId());
        transaction.setDescription(etDescriptionMoney.getText().toString());
        transaction.setDate(ParserHelper.parseDate(etDateMoney.getText().toString(), TipoDado.DATA));
        transaction.setDirection(Direction.INCOME);
        try {
            transaction.setValue(TextFormatter.cleanCurrency(etAmountMoney.getText().toString(), Locale.getDefault()).doubleValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void insertTransaction() {
        Long returnId = transactionController.insert(transaction);
        if (returnId > -1) {
            Toast.makeText(getApplicationContext(), getText(R.string.success_insert), Toast.LENGTH_LONG).show();
            updateScreenData();
        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.error_insert), Toast.LENGTH_LONG).show();
        }
    }

    private void updateTransaction() {
        int returnId = transactionController.update(transaction);
        if (returnId > -1) {
            Toast.makeText(getApplicationContext(), getText(R.string.success_update), Toast.LENGTH_LONG).show();
            updateScreenData();
        } else {
            Toast.makeText(getApplicationContext(), getText(R.string.error_update), Toast.LENGTH_LONG).show();
        }
    }

    public void updateScreenData() {
        Double budget = travelController.getBudget(travel.getId());
        tvBudget.setText(TextFormatter.formatCurrency(budget));
        Double spending = travelController.getExpense(travel.getId());
        tvSpending.setText(TextFormatter.formatCurrency(spending));
        Double balance = budget - spending;
        tvBalance.setText(TextFormatter.formatCurrency(balance));

        organizeTransactions();
        travelHistoryAdapter.notifyDataSetChanged();
    }

    public void organizeTransactions() {
        List<Transaction> transactionsAll = transactionController.loadByTravel(travel.getId());

        // Organize all the transactions by date
        HashMap<Date, List<Transaction>> transactionMap = new HashMap<>();
        for(Transaction tran : transactionsAll) {
            if(transactionMap.containsKey(tran.getDate())) {
                transactionMap.get(tran.getDate()).add(tran);
            } else {
                List<Transaction> transactions = new ArrayList<>();
                transactions.add(tran);
                transactionMap.put(tran.getDate(), transactions);
            }
        }

        transactions.clear();
        TransactionGroup transGroup;
        for(Date dateTransaction : transactionMap.keySet()) {
            transGroup = new TransactionGroup();
            transGroup.setDate(dateTransaction);
            transGroup.setTransactions(transactionMap.get(dateTransaction));
            transactions.add(transGroup);
        }
    }

    @Override
    public void editIncome(Transaction transaction) {
        this.transaction = transaction;
        buildPopupMoney();
        buildPopupMoneyView(true);
    }

    @Override
    public void editOutcome(Transaction transaction) {
        this.transaction = transaction;
        buildPopupExpense();
        buildPopupExpenseView(true);
    }

    @Override
    public void deleteTransaction(final Transaction transaction) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(getResources().getString(R.string.confirm_delete_item))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int result = transactionController.delete(transaction.getId());
                        if(result > -1) {
                            Toast.makeText(getApplicationContext(), getText(R.string.success_delete), Toast.LENGTH_LONG).show();
                            updateScreenData();
                        } else {
                            Toast.makeText(getApplicationContext(), getText(R.string.error_delete), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builder.create();
        builder.show();
    }
}
