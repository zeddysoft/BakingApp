package com.zeddysoft.bakingapp.recipeDetail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.model.Step;

import java.util.List;

import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        Recipe recipe = (Recipe) getIntent().getExtras().getParcelable(getString(R.string.recipe_key));
        Fragment detailFragment = new RecipeDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.recipe_key), recipe);
        detailFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.recipe_detail_container, detailFragment).commit();
    }

}
