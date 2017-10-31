package com.example.tomdong.sanity;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.support.test.espresso.contrib.DrawerMatchers;

import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by fansang on 10/30/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MenuActivityTest {
    @Rule
    public IntentsTestRule<MainActivity> menuActivityIntentsTestRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void test01_menu_clickOverview() throws Exception {
        // Open menu
        Espresso.onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Open log out dialogue
        Espresso.onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_overview));
        Thread.sleep(2000);

        //intended(hasComponent(MenuActivity.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.overview_pie)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void test02_menu_clickManageBudgets() throws Exception {
        // Open menu
        Espresso.onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Open log out dialogue
        Espresso.onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_mng_bgt));
        Thread.sleep(2000);

        //intended(hasComponent(MenuActivity.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.myBudgets_textview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void test03_menu_clickManageCategories() throws Exception {
        // Open menu
        Espresso.onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Open log out dialogue
        Espresso.onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_mng_cat));
        Thread.sleep(2000);

        //intended(hasComponent(CategoryFragment.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.my_cat_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void test04_clickManageTransactions() throws Exception {
        // Open menu
        Espresso.onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Open log out dialogue
        Espresso.onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_mng_trans));
        Thread.sleep(2000);

        //intended(hasComponent(CategoryFragment.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.trans_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
