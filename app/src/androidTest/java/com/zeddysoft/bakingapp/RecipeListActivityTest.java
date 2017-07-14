package com.zeddysoft.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zeddysoft.bakingapp.idlingResource.BakingIdlingResource;
import com.zeddysoft.bakingapp.recipeList.RecipeListActivity;

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
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
    public void displayCheesecakeInToolBar(){
        int cheesecakePosition = 3;
        String CHEESECAKE_TITLE = "Cheesecake";
        onView(withId(R.id.recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(cheesecakePosition, click()));
        onView(withText(CHEESECAKE_TITLE) ).check(matches(isDisplayed()));
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
