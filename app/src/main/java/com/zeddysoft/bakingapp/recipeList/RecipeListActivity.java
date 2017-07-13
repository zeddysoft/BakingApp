package com.zeddysoft.bakingapp.recipeList;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.network.ApiClient;
import com.zeddysoft.bakingapp.network.ApiInterface;
import com.zeddysoft.bakingapp.recipeDetail.RecipeDetailActivity;
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
    private RecipeListAdapter.RecipeClickListener mCallback;

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

                RecyclerView.LayoutManager mLayoutManager = getLayoutManager();
                recipeList.setLayoutManager(mLayoutManager);

                recipeList.setItemAnimator(new DefaultItemAnimator());
                recipeListAdapter = new RecipeListAdapter(RecipeListActivity.this, response.body());
                recipeList.setAdapter(recipeListAdapter);

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }


    private RecyclerView.LayoutManager getLayoutManager(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        boolean isTabletSize = getResources().getBoolean(R.bool.isTablet);
        int currentOrientation = getResources().getConfiguration().orientation;
        boolean isLandScape = currentOrientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isTabletSize || isLandScape) {
            int columnSpan = 2;
            return new GridLayoutManager(getApplicationContext(), columnSpan);
        }
        return  linearLayoutManager;

    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(getString(R.string.recipe_key), recipe);
            startActivity(intent);
        }
    }
