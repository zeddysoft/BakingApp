package com.zeddysoft.bakingapp.RecipeList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.network.ApiInterface;
import com.zeddysoft.bakingapp.network.ApiClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements RecipeListContract.View {

    @BindView(R.id.recipe_list)
    RecyclerView recipe_list;

    @BindView(R.id.recipe_list_progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);
        displayRecipies();

    }

    private void displayRecipies() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
    }


    @Override
    public void fetchRecipeList(List<Recipe> recipies) {

    }
}