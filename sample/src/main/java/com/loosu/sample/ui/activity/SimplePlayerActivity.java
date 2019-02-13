package com.loosu.sample.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.loosu.sample.R;
import com.loosu.sample.domain.VideoEntry;

import java.io.IOException;

public class SimplePlayerActivity extends AppCompatActivity {

    private static final String KEY_VIDEO_ENTRY = "key_video_entry";

    private SurfaceView mSurfaceView;
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
        mSurfaceView = findViewById(R.id.surface_view);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mVideoEntry = intent.getParcelableExtra(KEY_VIDEO_ENTRY);
        mPlayer = new MediaPlayer();
    }

    private void initView(Bundle savedInstanceState) {
        mSurfaceView.getHolder().addCallback(mSurfaceCallback);
    }

    private SurfaceHolder.Callback2 mSurfaceCallback = new SurfaceHolder.Callback2() {
        @Override
        public void surfaceRedrawNeeded(SurfaceHolder holder) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mPlayer.setDisplay(holder);
                mPlayer.setDataSource(mVideoEntry.getData());
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mPlayer.release();
        }
    };
}
