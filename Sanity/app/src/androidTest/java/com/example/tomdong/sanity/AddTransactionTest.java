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
import android.view.Menu;

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
public class AddTransactionTest {
    @Rule
    public IntentsTestRule<MainActivity> menuActivityIntentsTestRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void test01_clickAddTransactionButton() throws Exception {
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.fab)).perform(click());
        //intended(hasComponent(CategoryFragment.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.add_trans_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void test02_selectTransactionSpinnerText() throws Exception {
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.fab)).perform(click());
        Espresso.onView(withId(R.id.bgt_spinner)).perform(click());
        Espresso.onData(Matchers.allOf(is(instanceOf(String.class)), is("testBgt"))).inRoot(isPlatformPopup()).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.cat_spinner)).perform(click());
        Espresso.onData(Matchers.allOf(is(instanceOf(String.class)), is("testCat3"))).inRoot(isPlatformPopup()).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.bgt_spinner)).check(matches(withSpinnerText(containsString("testBgt"))));
        Espresso.onView(withId(R.id.cat_spinner)).check(matches(withSpinnerText(containsString("testCat3"))));
    }

    @Test
    public void test03_addTransactionForm() throws Exception {
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.fab)).perform(click());
        Espresso.onView(withId(R.id.bgt_spinner)).perform(click());
        Espresso.onData(Matchers.allOf(is(instanceOf(String.class)), is("testBgt"))).inRoot(isPlatformPopup()).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.cat_spinner)).perform(click());
        Espresso.onData(Matchers.allOf(is(instanceOf(String.class)), is("testCat3"))).inRoot(isPlatformPopup()).perform(click());
        Thread.sleep(1000);
        Espresso.onView(withId(R.id.trans_amt)).perform(typeText("200"));
        Espresso.onView(withId(R.id.trans_note)).perform(typeText("this is a test transaction"));
        Espresso.onView(withText("Add"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        Thread.sleep(2000);
    }
}
