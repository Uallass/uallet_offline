package com.example.uallas.uallet;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.uallas.uallet.controller.AddTravelActivity;
import com.example.uallas.uallet.controller.DashboardActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DashboardActivityTest {

    @Rule
    public ActivityTestRule<DashboardActivity> myActivityRule = new ActivityTestRule<DashboardActivity>(DashboardActivity.class);

    @Test
    public void addNewTravel() throws Exception {

            onView(withId(R.id.bt_add_travel)).perform(click());
            onView(withId(R.id.sp_country)).check(matches(isDisplayed()));
    }


}
