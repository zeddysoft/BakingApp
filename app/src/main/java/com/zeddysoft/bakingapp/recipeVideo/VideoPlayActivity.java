package com.zeddysoft.bakingapp.recipeVideo;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;

import butterknife.ButterKnife;

public class VideoPlayActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);



        Recipe recipe = getIntent().getExtras().getParcelable(getString(R.string.recipe_key));
        int currentStepPosition = getIntent().getExtras().getInt(getString(R.string.position_key));

        VideoPlayFragment videoPlayFragment = VideoPlayFragment.newInstance(
                getString(R.string.recipe_key), recipe,
                getString(R.string.position_key), currentStepPosition
        );


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.video_container, videoPlayFragment).commit();

    }

    void hideToolbar(){
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }
}