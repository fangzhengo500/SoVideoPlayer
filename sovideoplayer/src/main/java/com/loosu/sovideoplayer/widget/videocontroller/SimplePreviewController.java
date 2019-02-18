package com.loosu.sovideoplayer.widget.videocontroller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loosu.sovideoplayer.R;

public class SimplePreviewController extends MediaController {

    private View mRoot;
    private TextView mTvTitle;
    private ImageView mBtnPlay;
    private TextView mTvProgress;
    private TextView mTvDuration;
    private SeekBar mSbProgress;
    private View mBtnFullscreen;

    public SimplePreviewController(@NonNull Context context) {
        super(context);
        makeControllerView();
    }

    protected View makeControllerView() {
        mRoot = LayoutInflater.from(getContext())
                .inflate(R.layout.view_simple_preview_controller, this, true);
        initControllerView(mRoot);
        return mRoot;
    }

    private void initControllerView(View root) {
        mTvTitle = root.findViewById(R.id.tv_title);
        mBtnPlay = root.findViewById(R.id.btn_play);
        mTvProgress = root.findViewById(R.id.tv_progress);
        mTvDuration = root.findViewById(R.id.tv_duration);
        mSbProgress = root.findViewById(R.id.sb_progress);
        mBtnFullscreen = root.findViewById(R.id.btn_fullscreen);

        mBtnPlay.setOnClickListener(mBtnPlayClickListener);
        mBtnFullscreen.setOnClickListener(mBtnFullscreenClickListener);
    }

    private void doPauseResume() {

    }

    private final View.OnClickListener mBtnPlayClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
        }
    };

    private final View.OnClickListener mBtnFullscreenClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
