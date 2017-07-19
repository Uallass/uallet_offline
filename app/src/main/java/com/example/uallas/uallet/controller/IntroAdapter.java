package com.example.uallas.uallet.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Uallas on 24/06/2017.
 */

public class IntroAdapter extends FragmentPagerAdapter {

    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroFragment.newInstance(position);
            default:
                return IntroFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
