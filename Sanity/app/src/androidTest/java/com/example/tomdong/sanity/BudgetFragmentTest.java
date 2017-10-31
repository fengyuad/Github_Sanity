package com.example.tomdong.sanity;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.support.test.espresso.contrib.DrawerMatchers;
import android.widget.DatePicker;

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
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
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
public class BudgetFragmentTest {
    @Rule
    public IntentsTestRule<MainActivity> menuActivityIntentsTestRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);



    @Test
    public void test01_showAddBudgetForm() throws Exception {
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
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.add_budget_fab)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.add_bgt_name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void test02_submitAddBudgetForm() throws Exception {
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
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.add_budget_fab)).perform(click());
        Espresso.onView(withId(R.id.add_bgt_name)).perform(typeText("testBgt6"));

        Espresso.onView(withId(R.id.add_bgt_date_button)).perform(click());
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 11, 2));
        Espresso.onView(withText("OK")).perform(click());

        Espresso.onView(withId(R.id.add_bgt_period)).perform(typeText("15"));

        Espresso.onView(withText("Add"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        Thread.sleep(2000);
    }

    @Test
    public void test03_checkBudgetAdded() throws Exception {
        // Open menu
        Espresso.onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Open manage budget
        Espresso.onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_mng_bgt));
        Thread.sleep(2000);

        Espresso.onData(instanceOf(Budget_card.class))
                .inAdapterView(withId(R.id.my_budgets_listview))
                .atPosition(1)
                .check(matches(hasDescendant(withText("testBgt6"))));
    }
}
