package com.zeddysoft.bakingapp.recipeList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Azeez.Taiwo on 7/5/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    Context context;
    List<Recipe> recipes;

    public RecipeListAdapter(Context context, List<Recipe> recipes) {
        this.recipes = recipes;
        this.context = context;
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeListAdapter.RecipeListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.recipeName.setText(recipe.getName());
        holder.serving.setText(recipe.getServings() + " Servings");

        if (!TextUtils.isEmpty(recipe.getImage()))
            Picasso.with(context).load(recipe.getImage()).into(holder.recipeImageView);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipeIV)
        ImageView recipeImageView;

        @BindView(R.id.recipe_name)
        TextView recipeName;

        @BindView(R.id.serving)
        TextView serving;

        public RecipeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ((RecipeClickListener) context).
                    onRecipeClicked(recipes.get(getLayoutPosition()));
        }
    }

    public interface RecipeClickListener {
        void onRecipeClicked(Recipe recipe);
    }
}