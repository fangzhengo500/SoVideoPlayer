package com.loosu.sovideoplayer.widget.controller;

import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loosu.sovideoplayer.R;
import com.loosu.sovideoplayer.util.KLog;
import com.loosu.sovideoplayer.widget.SoProgressBar;
import com.loosu.sovideoplayer.widget.controller.detector.FullscreenClickDetector;
import com.loosu.sovideoplayer.widget.controller.detector.FullscreenVolumeDetector;


public class FullscreenGestureController extends AbsGestureController {
    private static final String TAG = "FullscreenGestureContro";

    private View mRoot;
    private View mLayoutTop;
    private View mBtnBack;
    private TextView mTvTitle;

    private ImageView mBtnPlay;
    private SoProgressBar mProgressBrightness;
    private SoProgressBar mProgressVolume;

    private View mLayoutBottom;
    private TextView mTvProgress;
    private TextView mTvDuration;
    private SeekBar mSbProgress;

    private String mTileStr;

    private OnBackClickListener mBackClickListener;

    public FullscreenGestureController(@NonNull Context context, String title) {
        super(context);
        makeControllerView();
        setTitle(title);
        setFocusable(true);

        addGestureDetector(new FullscreenClickDetector(context, this));
        addGestureDetector(new FullscreenVolumeDetector(context, this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        KLog.d(TAG, "keyCode = " + keyCode + ", event = " + event);

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mBackClickListener != null) {
                mBackClickListener.onBackClick();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected View makeControllerView() {
        mRoot = LayoutInflater.from(getContext()).inflate(R.layout.view_fullscreen_gesture_controller, this, true);
        initController(mRoot);
        return mRoot;
    }

    @Override
    public void show(long timeout) {
        super.show(timeout);
        mLayoutTop.setVisibility(VISIBLE);
        mBtnPlay.setVisibility(VISIBLE);
        mLayoutBottom.setVisibility(VISIBLE);
    }

    @Override
    public void hide() {
        super.hide();
        mLayoutTop.setVisibility(GONE);
        mBtnPlay.setVisibility(GONE);
        mLayoutBottom.setVisibility(GONE);
    }

    private void initController(View root) {
        mLayoutTop = root.findViewById(R.id.layout_top);
        mBtnBack = root.findViewById(R.id.btn_back);
        mTvTitle = root.findViewById(R.id.tv_title);

        mBtnPlay = root.findViewById(R.id.btn_play);
        mProgressBrightness = root.findViewById(R.id.progress_brightness);
        mProgressVolume = root.findViewById(R.id.progress_volume);

        mLayoutBottom = root.findViewById(R.id.layout_bottom);
        mTvProgress = root.findViewById(R.id.tv_progress);
        mTvDuration = root.findViewById(R.id.tv_duration);
        mSbProgress = root.findViewById(R.id.sb_progress);

        mBtnBack.setOnClickListener(mOnClickListener);
        mBtnPlay.setOnClickListener(mOnClickListener);
        mSbProgress.setOnSeekBarChangeListener(mSeekBarChangeListener);
        this.setOnClickListener(mOnClickListener);
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

    public void setBackClickListener(OnBackClickListener backClickListener) {
        mBackClickListener = backClickListener;
    }

    public void setTitle(String title) {
        mTileStr = title;
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    private void onClickBtnBack(View v) {
        if (mBackClickListener != null) {
            mBackClickListener.onBackClick();
        }
    }

    private void onClickBtnPlay() {
        doPauseResume();
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.btn_back) {
                onClickBtnBack(v);
            } else if (viewId == R.id.btn_play) {
                onClickBtnPlay();
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
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

    public void showVolumeChange(@FloatRange(from = 0.0, to = 1.0) float percent) {
        if (percent < 0) {
            percent = 0;
        } else if (percent > 1) {
            percent = 1;
        }

        mProgressVolume.setVisibility(VISIBLE);
        mProgressVolume.setProgress((int) (mProgressVolume.getMax() * percent));

        if (percent <= 0) {
            mProgressVolume.setIconImageRes(R.drawable.ic_volume_off);
        } else if (percent <= 0.3) {
            mProgressVolume.setIconImageRes(R.drawable.ic_volume_mute);
        } else if (percent < 0.6) {
            mProgressVolume.setIconImageRes(R.drawable.ic_volume_down);
        } else {
            mProgressVolume.setIconImageRes(R.drawable.ic_volume_up);
        }
    }

    public void hideVolumeChange() {
        mProgressVolume.setVisibility(GONE);
    }


    public interface OnBackClickListener {
        public void onBackClick();
    }
}
