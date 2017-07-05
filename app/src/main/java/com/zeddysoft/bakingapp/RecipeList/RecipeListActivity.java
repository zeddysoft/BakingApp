package com.zeddysoft.bakingapp.RecipeList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.network.ApiInterface;
import com.zeddysoft.bakingapp.network.ApiClient;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity implements RecipeListContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);


    }

    private void displayRecipies() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
    }


    @Override
    public void fetchRecipeList(List<Recipe> recipies) {

    }
}
