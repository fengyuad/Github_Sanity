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

import java.util.Map;

import Model.Category;


import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.BundleMatchers.hasEntry;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.EnumSet.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by fansang on 10/30/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryFragmentTest {
    @Rule
    public IntentsTestRule<MainActivity> menuActivityIntentsTestRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void test01_showAddCategoryForm() throws Exception {
        // Open menu
        Espresso.onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Open log out dialogue
        Espresso.onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_mng_cat));
        Thread.sleep(2000);

        Thread.sleep(1000);
        Espresso.onView(withId(R.id.add_cat_fab)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.add_cat_name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void test02_submitAddCategoryForm() throws Exception {
        // Open menu
        Espresso.onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Open log out dialogue
        Espresso.onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_mng_cat));
        Thread.sleep(2000);

        Thread.sleep(1000);
        Espresso.onView(withId(R.id.add_cat_fab)).perform(click());
        Espresso.onView(withId(R.id.add_cat_name)).perform(typeText("testCat6"));

        Espresso.onView(withText("Add"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        Thread.sleep(2000);
    }

    @Test
    public void test03_menu_checkCategoryAdded() throws Exception {
        // Open menu
        Espresso.onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Open manage budget
        Espresso.onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_mng_cat));
        Thread.sleep(2000);

        Espresso.onData(instanceOf(Category_card.class))
                .inAdapterView(withId(R.id.my_budgets_listview))
                .atPosition(3)
                .check(matches(hasDescendant(withText("testCat6"))));
    }

    @Test
    public void test07_menu_DeleteCategories() throws Exception {
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
        Espresso.onData(instanceOf(Category_card.class)).inAdapterView(withId(R.id.my_catgory_listview)).atPosition(3).perform(swipeLeft());
        Thread.sleep(2000);
        //Espresso.onData(Matchers.allOf(is(instanceOf(String.class)),is("Delete"))).perform(click());
        Espresso.onData(instanceOf(Category_card.class)).inAdapterView(withId(R.id.my_catgory_listview)).atPosition(3).perform(click());
       // Espresso.onData(instanceOf(Category_card.class)).inAdapterView(withId(R.id.my_catgory_listview)).atPosition(3).;
        // Espresso.onData(hasToString("Delete")).perform(click());
//        Espresso.onData(instanceOf(Category_card.class))
//                .inAdapterView(withId(R.id.my_catgory_listview))
//                .hasToString(startsWith("Delete"))
//                .perform(click());

    }
}
