package com.example.uallas.uallet.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.uallas.uallet.R;
import com.example.uallas.uallet.db.Controller.UserController;
import com.example.uallas.uallet.db.CreateDB;
import com.example.uallas.uallet.model.User;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Uallas on 24/06/2017.
 */

public class IntroActivity extends AppCompatActivity implements IntroFragment.OnButtonClickListener {

    private ViewPager viewPager;
    private LinearLayout llIntroLogo;
    private UserController userController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean hasDbCreated;

        if (doesDatabaseExist(getApplicationContext())) {
            hasDbCreated = true;
        } else {
            hasDbCreated = false;

            userController = new UserController(getApplicationContext());
            User user = new User(1, "", true, true);

            Long result = userController.insert(user);
        }

        setContentView(R.layout.intro);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        llIntroLogo = (LinearLayout) findViewById(R.id.ll_intro_logo);

        if(hasDbCreated) {
            viewPager.setVisibility(View.GONE);
            llIntroLogo.setVisibility(View.VISIBLE);

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startDashboard();
                }
            }, 1000);

        } else {
            viewPager.setVisibility(View.VISIBLE);
            llIntroLogo.setVisibility(View.GONE);

            viewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));
            viewPager.setPageTransformer(false, new IntroPageTransformer());
        }

    }

    @Override
    public void onButtonClicked(View view, int page, boolean start) {
        if(start) {
            startDashboard();
        } else {
            viewPager.setCurrentItem(page, true);
        }
    }

    private void startDashboard() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        this.startActivity(intent);
        finish();
    }

    private static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath(CreateDB.DATABASE_NAME);
        return dbFile.exists();
    }
}
