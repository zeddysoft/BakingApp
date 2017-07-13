package com.zeddysoft.bakingapp.recipeDetail;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.recipeVideo.VideoPlayActivity;
import com.zeddysoft.bakingapp.recipeVideo.VideoPlayFragment;

import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailAdapter.OnStepClickListener {

    private boolean isTablet;
    private Recipe recipe;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        isTablet = findViewById(R.id.detail_container) != null;

        recipe = getIntent().getExtras().getParcelable(getString(R.string.recipe_key));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipe.getName());
        }
        Fragment detailFragment = new RecipeDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.recipe_key), recipe);
        detailFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.recipe_detail_container, detailFragment).commit();
    }

    @Override
    public void onStepClick(int stepPosition) {

        if (!isTablet) {
            Intent intent = new Intent(this, VideoPlayActivity.class);
            intent.putExtra(getString(R.string.recipe_key), recipe);
            intent.putExtra(getString(R.string.position_key), stepPosition);
            startActivity(intent);
        } else {
            VideoPlayFragment videoPlayFragment = new VideoPlayFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.recipe_key), recipe);
            bundle.putInt(getString(R.string.position_key), stepPosition);
            videoPlayFragment.setArguments(bundle);

            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.detail_container, videoPlayFragment).commit();
        }
    }
}