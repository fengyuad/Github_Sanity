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
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
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
public class AddCategoryToBudgetTest {
    @Rule
    public IntentsTestRule<MainActivity> menuActivityIntentsTestRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void test01_checkAddCategoryToBudgetButton() throws Exception {
        Thread.sleep(2000);
        //Espresso.onData(Matchers.allOf(is(instanceOf(String.class)), is("testBgt"))).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.overview_pie)).perform(click());
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.add_catTobudget_fab)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.addCatToBgtText)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void test02_submitAddCategoryToBudgetForm() throws Exception {
        Thread.sleep(2000);
        Espresso.onView(ViewMatchers.withId(R.id.overview_pie)).perform(click());
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.add_catTobudget_fab)).perform(click());
        Espresso.onData(instanceOf(String.class)).inAdapterView(withId(R.id.cat_add_tobudget_dialog_listview)).atPosition(1).perform(click());
        Espresso.onView(withId(R.id.cat_addtobudget_dialog_catamount)).perform(typeText("200"), closeSoftKeyboard());
        Espresso.onView(withText("Add"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        Thread.sleep(1000);
    }

    @Test
    public void test03_checkCategoryAddedToBudget() throws Exception {
        Thread.sleep(2000);
        Espresso.onView(ViewMatchers.withId(R.id.overview_pie)).perform(click());
        Thread.sleep(2000);
        Espresso.onData(instanceOf(Budget_card.class))
                .inAdapterView(withId(R.id.cat_add_tobudget_dialog_listview))
                .atPosition(2)
                .check(matches(hasDescendant(withText("testCat4"))));
        Thread.sleep(1000);
    }
}
