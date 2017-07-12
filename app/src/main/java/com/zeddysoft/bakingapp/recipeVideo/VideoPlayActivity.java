package com.zeddysoft.bakingapp.recipeVideo;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.net.Uri.*;

public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.videoView)
    SimpleExoPlayerView playerView;

    @BindView(R.id.step_instruction)
    TextView instructionView;

    @BindView(R.id.previous)
    TextView previousView;

    @BindView(R.id.back)
    TextView backView;

    @BindView(R.id.next)
    TextView nextView;

    @BindView(R.id.video_unavailable)
    ImageView videoUnavailable;

    Step singleStep;

    Recipe recipe;
    private int currentStepPosition;
    SimpleExoPlayer player;

    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;
    private List<Step> steps;

    private static String LOG = "bakingApp/Zeddysoft";
    private String videoUrl;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);

        recipe = getIntent().getExtras().getParcelable(getString(R.string.recipe_key));
        steps = recipe.getSteps();
        currentStepPosition = getIntent().getExtras().getInt(getString(R.string.position_key));
        displayCurrentStepInstruction();
        setupClickListeners();

        if (currentStepPosition == 0 || (currentStepPosition == recipe.getSteps().size() -1)) {
            changeViewAppearance(previousView, false);
        }

    }

    void changeViewAppearance(TextView textView, boolean isClickable) {
        if (!isClickable) {
            textView.setTextColor(ContextCompat.getColor(this, R.color.light_grey));
        }
    }

    private void setupClickListeners() {
        previousView.setOnClickListener(this);
        backView.setOnClickListener(this);
        nextView.setOnClickListener(this);
    }

    private void initializePlayer(String videoUrl) {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        Uri uri = parse(recipe.getSteps().get(currentStepPosition).getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    void displayCurrentStepInstruction() {
        Step step = recipe.getSteps().get(currentStepPosition);
        instructionView.setText(step.getDescription());
    }

    void changeVideo() {
        if(currentStepPosition == recipe.getSteps().size() -1){
            nextView.setTextColor(ContextCompat.getColor(this, R.color.light_grey));
        }else if(currentStepPosition == 0){
            previousView.setTextColor(ContextCompat.getColor(this, R.color.light_grey));
        }else{
            nextView.setTextColor(ContextCompat.getColor(this, R.color.grey));
            previousView.setTextColor(ContextCompat.getColor(this, R.color.grey));
        }

        singleStep = steps.get(currentStepPosition);
        videoUrl = singleStep.getVideoURL();
        description = singleStep.getDescription();

        if(description != null){
            instructionView.setText(description);
        }

        if ((!TextUtils.isEmpty(videoUrl))) {
            videoUnavailable.setVisibility(View.INVISIBLE);
            playerView.setVisibility(View.VISIBLE);
            initializePlayer(videoUrl);
        }else{

                videoUnavailable.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.INVISIBLE);

        }

        Uri uri = parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:

                if( currentStepPosition >= 0){
                    --currentStepPosition;
                    changeVideo();
                }

                break;

            case R.id.back:
                finish();
                break;

            case R.id.next:

                if( currentStepPosition >= 0 && currentStepPosition < steps.size() -1){
                    ++currentStepPosition;
                    changeVideo();
                }
                break;
        }
    }
}