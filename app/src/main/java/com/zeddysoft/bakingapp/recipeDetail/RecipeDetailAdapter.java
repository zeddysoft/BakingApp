package com.zeddysoft.bakingapp.recipeDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Ingredient;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.model.Step;
import com.zeddysoft.bakingapp.recipeList.RecipeListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Azeez.Taiwo on 7/7/2017.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Step> steps;
    OnStepClickListener stepClickListener;
    private Recipe recipe;

    private int INGREDIENT_DEFAULT_POSITION = 0;

    public RecipeDetailAdapter(Context context, Recipe recipe, OnStepClickListener stepClickListener) {
        this.context = context;
        this.recipe = recipe;
        this.steps = recipe.getSteps();
        this.stepClickListener = stepClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == INGREDIENT_DEFAULT_POSITION) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
            return new IngredientViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, parent, false);
            return new RecipeDetailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position == INGREDIENT_DEFAULT_POSITION) {
            ((IngredientViewHolder) holder).bindView();
        } else {
            ((RecipeDetailViewHolder) holder).bindView(position);
        }

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class RecipeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_thumbnail)
        ImageView stepThumbnail;

        @BindView(R.id.step_content)
        TextView stepContent;

        public RecipeDetailViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(int position) {
            Step step = steps.get(position);

            stepContent.setText(step.getShortDescription());

            if (!TextUtils.isEmpty(step.getThumbnailURL()))
                Picasso.with(context).load(step.getThumbnailURL()).into(stepThumbnail);
        }

        @Override
        public void onClick(View v) {
            stepClickListener.onStepClick(getLayoutPosition());
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredientTV)
        TextView ingredientView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(){

            StringBuilder builder = new StringBuilder();
            List<Ingredient> ingredientList = recipe.getIngredients();
            builder.append("Ingredients lists \n\n");

            for (Ingredient singleIngredient : ingredientList) {
                builder.append("\t"+singleIngredient.getIngredient()
                        + " (" + singleIngredient.getQuantity() + " "
                        + singleIngredient.getMeasure() + ")\n");
            }
            builder.append("\n");

            ingredientView.setText(builder.toString());

        }

    }

    public interface OnStepClickListener {
        void onStepClick(int stepPosition);
    }
}