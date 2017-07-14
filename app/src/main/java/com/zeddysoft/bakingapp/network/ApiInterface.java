package com.zeddysoft.bakingapp.network;

import com.zeddysoft.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by azeez on 7/4/17.
 */

public interface ApiInterface {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getRecipies();
}
