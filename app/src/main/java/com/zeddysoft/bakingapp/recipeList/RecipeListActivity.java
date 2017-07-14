package com.zeddysoft.bakingapp.recipeList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Ingredient;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.network.ApiClient;
import com.zeddysoft.bakingapp.network.ApiInterface;
import com.zeddysoft.bakingapp.recipeDetail.RecipeDetailActivity;
import com.zeddysoft.bakingapp.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeClickListener {

    @BindView(R.id.recipe_list)
    RecyclerView recipeList;

    @BindView(R.id.recipe_list_progress_bar)
    ProgressBar progressBar;

    private RecipeListAdapter recipeListAdapter;
    private ArrayList<Recipe> recipies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.recipe_list_state))) {
            progressBar.setVisibility(View.GONE);
            recipies = savedInstanceState.getParcelableArrayList(getString(R.string.recipe_list_state));
            showRecipeList();
        } else {
            showRecipeList();
        }
    }

    private void showRecipeList() {

        if (isPhoneConnectedToInternet()) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            final Call<ArrayList<Recipe>> recipeCall = apiService.getRecipies();

            recipeCall.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                    progressBar.setVisibility(View.GONE);

                    RecyclerView.LayoutManager mLayoutManager = getLayoutManager();
                    recipeList.setLayoutManager(mLayoutManager);

                    recipies = response.body();
                    loadRecipeToView();

                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    showMessage(R.string.loading_error);
                }
            });
        } else {
            showMessage(R.string.no_network_message);
        }
    }

    private void loadRecipeToView() {
        recipeList.setItemAnimator(new DefaultItemAnimator());
        recipeListAdapter = new RecipeListAdapter(RecipeListActivity.this, recipies);
        recipeList.setAdapter(recipeListAdapter);
    }

    private void showMessage(int messageId) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(messageId));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "RETRY",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showRecipeList();
                        dialog.dismiss();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.recipe_list_state), recipies);
        super.onSaveInstanceState(outState);
    }

    public boolean isPhoneConnectedToInternet() {
        return NetworkUtils.isPhoneConnectedToInternet();
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        cacheRecipeIngredient(recipe);
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(getString(R.string.recipe_key), recipe);
        startActivity(intent);
    }


    private void cacheRecipeIngredient(Recipe recipe) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.ingredient_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String ingredients = getFormattedIngredient(recipe.getIngredients());
        editor.putString(getString(R.string.ingredient_key), ingredients);
        editor.apply();
    }

    private String getFormattedIngredient(List<Ingredient> ingredientList) {
        StringBuilder builder = new StringBuilder();
        builder.append("Ingredients lists \n\n");

        for (int i = 0; i < ingredientList.size(); ++i) {
            Ingredient singleIngredient = ingredientList.get(i);
            builder.append("\t " + (i + 1) + ". " + singleIngredient.getIngredient()
                    + " (" + singleIngredient.getQuantity() + " "
                    + singleIngredient.getMeasure() + ")\n");
        }
        builder.append("\n");
        return builder.toString();
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        boolean isTabletSize = getResources().getBoolean(R.bool.isTablet);
        int currentOrientation = getResources().getConfiguration().orientation;
        boolean isLandScape = currentOrientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isTabletSize || isLandScape) {
            int columnSpan = 2;
            return new GridLayoutManager(this, columnSpan);
        }
        return linearLayoutManager;

    }

    public boolean hasDataFinishedLoading() {
        return recipeListAdapter != null;
    }
}