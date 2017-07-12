package com.zeddysoft.bakingapp.recipeList;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.recipeDetail.RecipeDetailActivity;
import com.zeddysoft.bakingapp.recipeDetail.RecipeDetailAdapter;
import com.zeddysoft.bakingapp.recipeDetail.RecipeDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeClickListener {

//    private RecipeListAdapter recipeListAdapter;
    FragmentManager fragmentManager;
    private boolean mTwoPaneMode;


    @BindView(R.id.ingredientTV)
    TextView ingredients;

    @BindView(R.id.fragment_recipe_detail_RV)
    RecyclerView detailRV;

    private RecipeDetailAdapter adapter;
    private RecipeDetailAdapter.OnStepClickListener stepClickListener;

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        if (findViewById(R.id.tabContainer) != null) {
            mTwoPaneMode = true;

        } else {
            mTwoPaneMode = false;
        }

    }


    @Override
    public void onRecipeClicked(Recipe recipe) {

        if (mTwoPaneMode) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.recipe_key), recipe);
            Fragment detailFragment = new RecipeDetailFragment();
            detailFragment.setArguments(bundle);

            fragmentManager.beginTransaction().
                    replace(R.id.recipe_detail_container, detailFragment).commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(getString(R.string.recipe_key), recipe);
            startActivity(intent);
        }
    }
}