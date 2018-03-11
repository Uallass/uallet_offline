package com.example.uallas.uallet.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uallas.uallet.R;
import com.example.uallas.uallet.db.Controller.CountryController;
import com.example.uallas.uallet.db.Controller.TravelController;
import com.example.uallas.uallet.lib.TextFormatter;
import com.example.uallas.uallet.model.Country;
import com.example.uallas.uallet.model.Travel;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Activity activity;
    private TextView tvLocation;
    private TextView tvBudget;
    private TextView tvSpending;
    private TextView tvBalance;
    private ImageView ivFlag;
    private CardView cvCurrentTravel;
    private ListView lvMyTravels;
    private LinearLayout btAddTravel;
    private RelativeLayout rlNoTravelYet;
    private RelativeLayout rlCurrentTravel;
    private MyTravelsAdapter myTravelsAdapter;
    private List<Travel> travels;
    private Locale travelCurrencyLocale;

    // Popup About views
    private PopupWindow popupAbout;
    private View layoutPopupAbout;

    private TravelController travelController;
    private CountryController countryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        travelController = new TravelController(getApplicationContext());
        countryController = new CountryController(getApplicationContext());
        setContentView(R.layout.dashboard_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        travelCurrencyLocale = Locale.getDefault();

        buildViews();
        setViews();
    }

    private void buildViews() {
        tvLocation = (TextView) findViewById(R.id.tv_current_travel);
        ivFlag = (ImageView) findViewById(R.id.iv_flag);
        tvBudget = (TextView) findViewById(R.id.tv_budget);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvSpending = (TextView) findViewById(R.id.tv_spending);
        cvCurrentTravel = (CardView) findViewById(R.id.cv_current_travel);
        lvMyTravels = (ListView) findViewById(R.id.lv_my_travels);
        btAddTravel = (LinearLayout) findViewById(R.id.bt_add_travel);
        rlNoTravelYet = (RelativeLayout) findViewById(R.id.rl_no_travel_yet);
        rlCurrentTravel = (RelativeLayout) findViewById(R.id.rl_current_travel);
    }

    private void setViews() {

        final Travel travel = travelController.getCurrentTravel();

        // If there isn't a travel registered yet, it hides the current travel view
        // and shows the add first travel button
        if(travel.getId() > 0) {
            rlCurrentTravel.setVisibility(View.VISIBLE);
            rlNoTravelYet.setVisibility(View.GONE);

            tvLocation.setText(travel.getLocation());
            Country country = countryController.loadById(travel.getCountry());
            ivFlag.setImageResource(getResources().getIdentifier(country.getImageName(), "drawable", getPackageName()));

            Double budget = travelController.getBudget(travel.getId());
            try {
                tvBudget.setText(TextFormatter.formatCurrencyFromDouble(budget, travelCurrencyLocale));
                Double spending = travelController.getExpense(travel.getId());
                tvSpending.setText(TextFormatter.formatCurrencyFromDouble(spending, travelCurrencyLocale));
                Double balance = budget - spending;
                tvBalance.setText(TextFormatter.formatCurrencyFromDouble(balance, travelCurrencyLocale));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cvCurrentTravel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity.getApplicationContext(), TravelActivity.class);
                    intent.putExtra("idTravel", travel.getId());
                    activity.startActivity(intent);
                }
            });

        } else {
            rlCurrentTravel.setVisibility(View.GONE);
            rlNoTravelYet.setVisibility(View.VISIBLE);

            cvCurrentTravel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity.getApplicationContext(), AddTravelActivity.class);
                    activity.startActivity(intent);
                }
            });

        }

        TravelController travelController = new TravelController(getApplicationContext());
        travels = travelController.load();
        myTravelsAdapter = new MyTravelsAdapter(getApplicationContext(), travels);
        lvMyTravels.setAdapter(myTravelsAdapter);

        myTravelsAdapter.notifyDataSetChanged();

        lvMyTravels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(activity.getApplicationContext(), TravelActivity.class);
                intent.putExtra("idTravel", travels.get(position).getId());
                activity.startActivity(intent);
            }
        });

        btAddTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), AddTravelActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_travel) {
            // Handle the camera action
        } else if (id == R.id.nav_about) {
            buildPopupAbout();
//        } else if (id == R.id.database) {
//            Intent intent = new Intent(activity.getApplicationContext(), AndroidDatabaseManager.class);
//            activity.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void buildPopupAbout() {
        //This is the main view in the popup layout
        RelativeLayout viewGroup = (RelativeLayout) this.findViewById(
                R.id.rl_about_wrap);
        LayoutInflater layoutInflater = (LayoutInflater) this
                .getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        //inflate the layout
        layoutPopupAbout = layoutInflater.inflate(
                R.layout.about, viewGroup);
        RelativeLayout popupBack = (RelativeLayout) layoutPopupAbout.findViewById(R.id.rl_about_wrap);
        LinearLayout popupContent = (LinearLayout) layoutPopupAbout.findViewById(R.id.ll_about);

        // Creating the PopupWindow in full screen
        popupAbout = new PopupWindow(layoutPopupAbout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);
        popupAbout.setFocusable(true);

        // Clear the default translucent background
        popupAbout.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popupAbout.showAtLocation(layoutPopupAbout, Gravity.CENTER, 0, 0);

        // Dismiss the popup when the transparent background is clicked
        popupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAbout.dismiss();
            }
        });

        // set nothing to the popup content click because it can't dismiss when clicked
        popupContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView tvVersion = (TextView) layoutPopupAbout.findViewById(R.id.tv_app_version);
        try {
            String appVersion = getResources().getString(R.string.version) + ": " +getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText(appVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setViews();
    }
}
