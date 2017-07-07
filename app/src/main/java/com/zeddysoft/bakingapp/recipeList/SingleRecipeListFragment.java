package com.zeddysoft.bakingapp.recipeList;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.network.ApiClient;
import com.zeddysoft.bakingapp.network.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleRecipeListFragment extends Fragment implements RecipeListAdapter.RecipeClickListener{

    @BindView(R.id.recipe_list)
    RecyclerView recipeList;

    @BindView(R.id.recipe_list_progress_bar)
    ProgressBar progressBar;

    private RecipeListAdapter recipeListAdapter;
    private RecipeListAdapter.RecipeClickListener mCallback;

    public SingleRecipeListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (RecipeListAdapter.RecipeClickListener) context;
        }catch (ClassCastException exception){
            throw new ClassCastException(context.toString() +
            "must implement RecipeclickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_single_recipe_list, container, false);
        ButterKnife.bind(this,view);
        displayRecipies();
        return view;
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
                        (getActivity());
                recipeList.setLayoutManager(mLayoutManager);

                recipeList.setItemAnimator(new DefaultItemAnimator());
                recipeListAdapter = new RecipeListAdapter(getActivity(), response.body());
                recipeList.setAdapter(recipeListAdapter);

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        mCallback.onRecipeClicked(recipe);
    }
}
