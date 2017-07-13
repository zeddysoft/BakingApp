package com.zeddysoft.bakingapp.recipeVideo;


import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.net.Uri.parse;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoPlayFragment extends Fragment implements View.OnClickListener {

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


    public VideoPlayFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_play, container, false);
        ButterKnife.bind(this, view);

        int currentOrientation = getResources().getConfiguration().orientation;
        boolean isLandScape = currentOrientation == Configuration.ORIENTATION_LANDSCAPE;
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        if (isLandScape  && !tabletSize ) {
            ((VideoPlayActivity) getActivity()).hideToolbar();
        }
        recipe = getArguments().getParcelable(getString(R.string.recipe_key));

        steps = recipe.getSteps();
        currentStepPosition = getArguments().getInt(getString(R.string.position_key));

        displayCurrentStepInstruction();
        setupClickListeners();

        if (currentStepPosition == 0 || (currentStepPosition == recipe.getSteps().size() - 1)) {
            changeViewAppearance(previousView, false);
        }
        playNextStepVideo();

        return view;

    }

    void changeViewAppearance(TextView textView, boolean isClickable) {
        if (!isClickable) {
            textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        }
    }

    private void setupClickListeners() {
        previousView.setOnClickListener(this);
        backView.setOnClickListener(this);
        nextView.setOnClickListener(this);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
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
            player.stop();
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

    void playNextStepVideo() {
        if (currentStepPosition == recipe.getSteps().size() - 1) {
            nextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        } else if (currentStepPosition == 0) {
            previousView.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_grey));
        } else {
            nextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
            previousView.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
        }

        singleStep = steps.get(currentStepPosition);
        videoUrl = singleStep.getVideoURL();
        description = singleStep.getDescription();

        if (description != null) {
            instructionView.setText(description);
        }

        if ((!TextUtils.isEmpty(videoUrl))) {
            videoUnavailable.setVisibility(View.INVISIBLE);
            playerView.setVisibility(View.VISIBLE);
            initializePlayer();
        } else {

            videoUnavailable.setVisibility(View.VISIBLE);
            playerView.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:

                if (currentStepPosition >= 0) {
                    --currentStepPosition;
                    playNextStepVideo();
                }

                break;

            case R.id.back:
                getActivity().finish();
                break;

            case R.id.next:

                if (currentStepPosition >= 0 && currentStepPosition < steps.size() - 1) {
                    ++currentStepPosition;
                    playNextStepVideo();
                }
                break;
        }
    }
}