package com.zeddysoft.bakingapp.idlingResource;

import android.support.test.espresso.IdlingResource;

import com.zeddysoft.bakingapp.recipeList.RecipeListActivity;


/**
 * Created by Azeez.Taiwo on 7/14/2017.
 */

public class BakingIdlingResource implements IdlingResource {

    private RecipeListActivity activity;
    private IdlingResource.ResourceCallback callback;

    public BakingIdlingResource(RecipeListActivity activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return BakingIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        Boolean idle = isIdle();
        if (idle) callback.onTransitionToIdle();
        return idle;
    }

    public boolean isIdle() {
        return activity != null && callback != null && activity.hasDataFinishedLoading();
    }

    @Override
    public void registerIdleTransitionCallback(IdlingResource.ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}
