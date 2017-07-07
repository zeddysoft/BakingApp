package com.zeddysoft.bakingapp.recipeDetail;

import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Ingredient;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.recipeList.RecipeListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.ingredientTV)
    TextView ingredients;

    @BindView(R.id.fragment_recipe_detail_RV)
    RecyclerView detailRV;

    private RecipeDetailAdapter adapter;

    Recipe recipe;

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recipe = getArguments().getParcelable(getString(R.string.recipe_key));

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);
        setIngredientsOnView();
        displaySteps();
        return view;
    }

    private void displaySteps() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
                (getActivity());
        detailRV.setLayoutManager(mLayoutManager);

        detailRV.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecipeDetailAdapter(getActivity(), recipe.getSteps());
        Log.d("Checking recipies", recipe.getSteps().size()+"");
        detailRV.setAdapter(adapter);
    }

    private void setIngredientsOnView() {

        StringBuilder builder = new StringBuilder();
        List<Ingredient> ingredientList = recipe.getIngredients();
        builder.append("Ingredients lists \n\n");

        for (Ingredient singleIngredient : ingredientList) {
            builder.append("\t"+singleIngredient.getIngredient()
                    + " (" + singleIngredient.getQuantity() + " "
                    + singleIngredient.getMeasure() + ")\n");
        }
        builder.append("\n");

        ingredients.setText(builder.toString());

    }
}
