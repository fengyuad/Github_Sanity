package com.example.tomdong.sanity;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by tomdong on 10/30/17.
 */

public class DeleteBudgetTest {
    @Rule
    public IntentsTestRule<MainActivity> menuActivityIntentsTestRule =
            new IntentsTestRule<MainActivity>(MainActivity.class);

    @Test
    public void test08_clickAddTransactionButton() throws Exception {
        Thread.sleep(2000);
        Espresso.onView(withId(R.id.fab)).perform(click());
        //intended(hasComponent(CategoryFragment.class.getName()));
        Espresso.onView(ViewMatchers.withId(R.id.add_trans_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
