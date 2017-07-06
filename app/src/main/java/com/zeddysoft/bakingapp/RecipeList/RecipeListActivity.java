package com.zeddysoft.bakingapp.RecipeList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import retrofit2.Call;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.network.ApiInterface;
import com.zeddysoft.bakingapp.network.ApiClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends AppCompatActivity {

    @BindView(R.id.recipe_list)
    RecyclerView recipe_list;

    @BindView(R.id.recipe_list_progress_bar)
    ProgressBar progressBar;
    private RecipeListAdapter recipeListAdapter;

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

        final Call<List<Recipe>> recipeCall = apiService.getRecipies();

        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                progressBar.setVisibility(View.GONE);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
                        (RecipeListActivity.this.getApplicationContext());
                recipe_list.setLayoutManager(mLayoutManager);

                recipe_list.setItemAnimator(new DefaultItemAnimator());
                recipeListAdapter = new RecipeListAdapter(RecipeListActivity.this, response.body());
                recipe_list.setAdapter(recipeListAdapter);

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }


    public void fetchRecipeList(List<Recipe> recipies) {

    }
}