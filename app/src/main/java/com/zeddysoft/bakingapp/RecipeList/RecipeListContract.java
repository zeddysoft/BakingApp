package com.zeddysoft.bakingapp.RecipeList;

import com.zeddysoft.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by azeez on 7/4/17.
 */

public class RecipeListContract {
   public interface View {

        void fetchRecipeList(List<Recipe> recipies);
    }

   public interface UserActionsListener {

        void fetchRecipeList();
    }
}
