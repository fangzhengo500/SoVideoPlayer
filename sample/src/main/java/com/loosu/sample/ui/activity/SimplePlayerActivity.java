package com.loosu.sample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.loosu.sample.R;
import com.loosu.sample.domain.VideoEntry;
import com.loosu.sovideoplayer.widget.SoVideoView;


public class SimplePlayerActivity extends AppCompatActivity {

    private static final String KEY_VIDEO_ENTRY = "key_video_entry";

    private SoVideoView mVideoView;
    private VideoEntry mVideoEntry;

    private MediaPlayer mPlayer;

    public static Intent getStartIntent(Context context, VideoEntry videoEntry) {
        Intent intent = new Intent(context, SimplePlayerActivity.class);
        intent.putExtra(KEY_VIDEO_ENTRY, videoEntry);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_player_activity);
        init(savedInstanceState);
        findView(savedInstanceState);
        initView(savedInstanceState);
    }

    private void findView(Bundle savedInstanceState) {
        mVideoView = findViewById(R.id.video_view);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mVideoEntry = intent.getParcelableExtra(KEY_VIDEO_ENTRY);
        mPlayer = new MediaPlayer();
    }

    private void initView(Bundle savedInstanceState) {

    }
}
