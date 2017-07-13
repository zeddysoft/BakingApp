package com.zeddysoft.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeIngredientLisProvider extends AppWidgetProvider {

    private final static String NO_DESIRED_RECIPE_INGREDIENT = "You have no desired recipe ingredient yet!";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences
                (context.getString(R.string.ingredient_key), Context.MODE_PRIVATE);

        String desiredRecipeIngredient = sharedPreferences.getString(context.getString(R.string.ingredient_key),
                NO_DESIRED_RECIPE_INGREDIENT);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_lis_provider);
        views.setTextViewText(R.id.appwidget_text, desiredRecipeIngredient);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

