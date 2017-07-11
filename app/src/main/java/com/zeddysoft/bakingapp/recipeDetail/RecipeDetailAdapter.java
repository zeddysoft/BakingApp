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
import com.zeddysoft.bakingapp.model.Step;
import com.zeddysoft.bakingapp.recipeList.RecipeListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Azeez.Taiwo on 7/7/2017.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeDetailViewHolder> {

    Context context;
    List<Step> steps;
    OnStepClickListener stepClickListener;

    public RecipeDetailAdapter(Context context, List<Step> steps, OnStepClickListener stepClickListener) {
        this.context = context;
        this.steps = steps;
        this.stepClickListener = stepClickListener;
    }

    @Override
    public RecipeDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);

        return new RecipeDetailAdapter.RecipeDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeDetailViewHolder holder, int position) {
        Step step = steps.get(position);

        holder.stepContent.setText(step.getShortDescription());

        if (!TextUtils.isEmpty(step.getThumbnailURL()))
            Picasso.with(context).load(step.getThumbnailURL()).into(holder.stepThumbnail);
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

        @Override
        public void onClick(View v) {
            stepClickListener.onStepClick(getLayoutPosition());
        }
    }

    public interface OnStepClickListener {
        void onStepClick(int stepPosition);
    }
}