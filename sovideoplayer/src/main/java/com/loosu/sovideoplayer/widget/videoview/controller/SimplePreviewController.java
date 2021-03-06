package com.loosu.sovideoplayer.widget.videoview.controller;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loosu.sovideoplayer.R;

public class SimplePreviewController extends MediaController {
    private static final String TAG = "SimplePreviewController";

    private View mRoot;
    private TextView mTvTitle;
    private ImageView mBtnPlay;
    private View mLayoutBottom;
    private TextView mTvProgress;
    private TextView mTvDuration;
    private SeekBar mSbProgress;
    private View mBtnFullscreen;

    private Listener mListener;

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
        mLayoutBottom = root.findViewById(R.id.layout_bottom);
        mTvProgress = root.findViewById(R.id.tv_progress);
        mTvDuration = root.findViewById(R.id.tv_duration);
        mSbProgress = root.findViewById(R.id.sb_progress);
        mBtnFullscreen = root.findViewById(R.id.btn_fullscreen);

        mBtnPlay.setOnClickListener(mBtnPlayClickListener);
        mBtnFullscreen.setOnClickListener(mBtnFullscreenClickListener);
        mSbProgress.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        setOnClickListener(mEmptyClickListener);
    }

    @Override
    public void show(long timeout) {
        super.show(timeout);
        mTvTitle.setVisibility(VISIBLE);
        mBtnPlay.setVisibility(VISIBLE);
        mLayoutBottom.setVisibility(VISIBLE);
    }

    @Override
    public void hide() {
        super.hide();
        mTvTitle.setVisibility(GONE);
        mBtnPlay.setVisibility(GONE);
        mLayoutBottom.setVisibility(GONE);
    }

    @Override
    protected void updatePausePlay() {
        final MediaPlayerControl player = getPlayer();
        if (player != null && player.isPlaying()) {
            mBtnPlay.setImageResource(R.drawable.btn_pause_drawable);
        } else {
            mBtnPlay.setImageResource(R.drawable.btn_play_drawable);
        }
    }

    @Override
    protected int setProgress() {
        final MediaPlayerControl player = getPlayer();
        if (player == null || mDragging) {
            return 0;
        }
        long position = player.getCurrentPosition();
        long duration = player.getDuration();

        if (mSbProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                int pos = (int) (mSbProgress.getMax() * position / duration);
                mSbProgress.setProgress(pos);
            }
            // percent (1-100)
            int percent = player.getBufferPercentage();
            mSbProgress.setSecondaryProgress(percent);
        }

        if (mTvProgress != null) {
            mTvProgress.setText(stringForTime(position));
        }
        if (mTvDuration != null) {
            mTvDuration.setText(stringForTime(duration));
        }
        return (int) position;
    }

    public Listener getListener() {
        return mListener;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private final View.OnClickListener mBtnPlayClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
            show();
        }
    };

    private final View.OnClickListener mBtnFullscreenClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onFullscreenClick(SimplePreviewController.this);
            }
        }
    };

    private final View.OnClickListener mEmptyClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isShowing()) {
                hide();
            } else {
                show();
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }
            MediaPlayerControl player = getPlayer();
            long duration = player.getDuration();
            long newposition = (long) (duration * progress * 1f / seekBar.getMax());
            player.seekTo((int) newposition);
            if (mTvProgress != null)
                mTvProgress.setText(stringForTime((int) newposition));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            show(3600000);
            mDragging = true;
            removeCallbacks(mShowProgress);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show();
            post(mShowProgress);
        }
    };

    public interface Listener{
        public void onFullscreenClick(MediaController controller);
    }
}
