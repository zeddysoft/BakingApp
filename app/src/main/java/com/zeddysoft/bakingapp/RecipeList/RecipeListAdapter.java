package com.zeddysoft.bakingapp.RecipeList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeddysoft.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Azeez.Taiwo on 7/5/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeListAdapter.RecipeListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder {

        public RecipeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
