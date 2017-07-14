package com.zeddysoft.bakingapp.recipeList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Ingredient;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.recipeDetail.RecipeDetailActivity;
import com.zeddysoft.bakingapp.util.NetworkUtils;

import java.util.List;

public class RecipeListActivity extends FragmentActivity implements RecipeListAdapter.RecipeClickListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        showRecipeList();
    }

    private void showRecipeList() {
        if (isPhoneConnectedToInternet()) {
            fragmentManager = getSupportFragmentManager();
            Fragment singleFrag = new SingleRecipeListFragment();
            fragmentManager.beginTransaction().
                    add(R.id.single_list_container, singleFrag).commit();
        } else {
            showMessage(R.string.no_network_message);
        }

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
        alertDialog.show();


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
}