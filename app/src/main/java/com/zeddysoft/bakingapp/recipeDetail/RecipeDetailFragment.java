package com.zeddysoft.bakingapp.recipeDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.recipeVideo.VideoPlayActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements RecipeDetailAdapter.OnStepClickListener{

    @BindView(R.id.fragment_recipe_detail_RV)
    RecyclerView detailRV;

    private RecipeDetailAdapter adapter;
    private RecipeDetailAdapter.OnStepClickListener stepClickListener;

    Recipe recipe;

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recipe = getArguments().getParcelable(getString(R.string.recipe_key));

        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);
        displayDetailView();
        return view;
    }



    private void displayDetailView() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager
                (getActivity());
        detailRV.setLayoutManager(mLayoutManager);

        detailRV.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecipeDetailAdapter(getActivity(), recipe, this);
        detailRV.setAdapter(adapter);
    }

    @Override
    public void onStepClick(int currentStepPosition) {
        Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
        intent.putExtra(getString(R.string.recipe_key), recipe);
        intent.putExtra(getString(R.string.position_key), currentStepPosition);
        startActivity(intent);
    }
}