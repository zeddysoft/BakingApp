package com.zeddysoft.bakingapp;

import android.graphics.Color;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zeddysoft.bakingapp.idlingResource.BakingIdlingResource;
import com.zeddysoft.bakingapp.recipeList.RecipeListActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.view.View;
import android.widget.EditText;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * Created by Azeez.Taiwo on 7/14/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);
    private BakingIdlingResource idlingResource;
    @Before
    public void registerIntentServiceIdlingResource() {
        RecipeListActivity activity = mActivityTestRule.getActivity();
        idlingResource = new BakingIdlingResource(activity);
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void displayCheesecakeInToolBar_showDescriptionOfNextStepAfterClickingNext(){
        int cheesecakePosition = 3;
        String CHEESECAKE_TITLE = "Cheesecake";
        onView(withId(R.id.recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(cheesecakePosition, click()));
        onView(withText(CHEESECAKE_TITLE) ).check(matches(isDisplayed()));

        onView(withId(R.id.fragment_recipe_detail_RV))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withIndex(withId(R.id.videoView),0)).check(matches(isDisplayed()));

        onView(withId(R.id.next)).perform(click());
        onView(withId(R.id.step_instruction)).check(matches(withText(containsString("1"))));

    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    public static Matcher<View> withTextColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, EditText>(EditText.class) {
            @Override
            public boolean matchesSafely(EditText warning) {
                return color == warning.getCurrentTextColor();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: ");
            }
        };
    }



    @Test
    public void displayNutellaPieInToolBar(){
        int nutellaPiePosition = 0;
        String NUTELLA_PIE_TITLE = "Nutella Pie";
        onView(withId(R.id.recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(nutellaPiePosition, click()));
        onView(withText(NUTELLA_PIE_TITLE) ).check(matches(isDisplayed()));
    }


    @After
    public void unregisterIntentServiceIdlingResource() {
        Espresso.unregisterIdlingResources(idlingResource);

    }
}
