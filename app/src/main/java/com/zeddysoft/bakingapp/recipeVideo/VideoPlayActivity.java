package com.zeddysoft.bakingapp.recipeVideo;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.zeddysoft.bakingapp.R;
import com.zeddysoft.bakingapp.model.Recipe;
import com.zeddysoft.bakingapp.model.Step;

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

    Recipe recipe;
    private int currentStepPosition;
    SimpleExoPlayer player;

    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ButterKnife.bind(this);

        recipe = getIntent().getExtras().getParcelable(getString(R.string.recipe_key));
        currentStepPosition = getIntent().getExtras().getInt(getString(R.string.position_key));
        displayCurrentStepInstruction();
        setupClickListeners();

        if (currentStepPosition == 0) {
            changeViewAppearance(previousView, false);
        } else if (currentStepPosition == recipe.getSteps().size() - 1) {
            changeViewAppearance(nextView, false);
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

    private void initializePlayer() {
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
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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

    void changeVideo(String videoUrl) {
        Uri uri = parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                Log.d("previous", "called");
                if (currentStepPosition > 0) {
                    --currentStepPosition;
                } else {
                    changeViewAppearance(previousView, false);
                    currentStepPosition = -1;
                }

                if (currentStepPosition >= 0) {
                    changeVideo(recipe.getSteps().get(currentStepPosition).getVideoURL());
                }

                break;

            case R.id.back:
                finish();
                break;

            case R.id.next:

                break;
        }
    }
}