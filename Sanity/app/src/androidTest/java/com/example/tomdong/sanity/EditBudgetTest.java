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
import static android.support.test.espresso.action.ViewActions.clearText;
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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

/**
 * Created by fansang on 10/30/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditBudgetTest {
    @Rule
    public IntentsTestRule<MainActivity> menuActivityIntentsTestRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void test01_checkEditButton() throws Exception {
        Thread.sleep(2000);
        //Espresso.onData(Matchers.allOf(is(instanceOf(String.class)), is("testBgt"))).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.overview_pie)).perform(click());
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.bgt_edit_fab)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.edit_catgory_name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void test02_submitEditBudgetForm_withDifferentCatAmount() throws Exception {
        Thread.sleep(2000);
        Espresso.onView(ViewMatchers.withId(R.id.overview_pie)).perform(click());
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.bgt_edit_fab)).perform(click());
        Espresso.onData(instanceOf(String.class)).inAdapterView(withId(R.id.budget_edit_catgoryList)).atPosition(2).perform(click());
        Espresso.onView(withId(R.id.budget_edit_catamount)).perform(clearText(), typeText("1"), closeSoftKeyboard());
        Espresso.onView(withId(R.id.edit_bgt_date_button)).perform(click());
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 12, 1));
        Espresso.onView(withText("OK")).perform(click());
        Espresso.onView(withId(R.id.edit_bgt_period)).perform(typeText("17"), closeSoftKeyboard());
        Thread.sleep(1000);
        Espresso.onView(withText("Submit"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        Thread.sleep(1000);
    }

    @Test
    public void test03_checkBudgetEdited() throws Exception {
        Thread.sleep(2000);
        Espresso.onView(ViewMatchers.withId(R.id.overview_pie)).perform(click());
        Thread.sleep(2000);
        Espresso.onData(instanceOf(Category_card.class))
                .inAdapterView(withId(R.id.category_listview))
                .atPosition(2)
                .check(matches(hasDescendant(withText(endsWith("1.0")))));
        Thread.sleep(1000);
    }
}
