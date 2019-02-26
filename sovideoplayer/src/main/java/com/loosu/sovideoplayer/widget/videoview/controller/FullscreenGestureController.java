package com.loosu.sovideoplayer.widget.videoview.controller;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loosu.sovideoplayer.R;
import com.loosu.sovideoplayer.util.KLog;
import com.loosu.sovideoplayer.widget.SoProgressBar;
import com.loosu.sovideoplayer.widget.videoview.detector.FullscreenBrightnessDetector;
import com.loosu.sovideoplayer.widget.videoview.detector.FullscreenClickDetector;
import com.loosu.sovideoplayer.widget.videoview.detector.FullscreenVolumeDetector;


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
    private View mBtnFullscreenExit;

    private String mTileStr;

    private OnBackClickListener mBackClickListener;

    public FullscreenGestureController(@NonNull Context context, String title) {
        super(context);
        makeControllerView();
        setTitle(title);
        setFocusable(true);

        addGestureDetector(new FullscreenBrightnessDetector(context, this));
        addGestureDetector(new FullscreenVolumeDetector(context, this));
        addGestureDetector(new FullscreenClickDetector(context, this));
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
        mBtnFullscreenExit = root.findViewById(R.id.btn_fullscreen_exit);

        mBtnBack.setOnClickListener(mOnClickListener);
        mBtnPlay.setOnClickListener(mOnClickListener);
        mBtnFullscreenExit.setOnClickListener(mOnClickListener);
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
            } else if (viewId == R.id.btn_fullscreen_exit) {
                onClickBtnBack(v);
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

    public void setVolume(@FloatRange(from = 0.0, to = 1.0) float percent, boolean showProgress) {
        if (percent < 0) {
            percent = 0;
        } else if (percent > 1) {
            percent = 1;
        }

        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int newVolume = (int) (percent * maxVolume);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);

        if (showProgress) {
            if (mProgressVolume.getVisibility() != VISIBLE) {
                mProgressVolume.setVisibility(VISIBLE);
            }

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
    }

    public void hideVolume() {
        mProgressVolume.setVisibility(GONE);
    }

    public void setBright(@FloatRange(from = 0.0, to = 1.0) float percent, boolean show) {
        if (percent < 0) {
            percent = 0;
        } else if (percent > 1) {
            percent = 1;
        }

        Window window = ((Activity) getContext()).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.screenBrightness = percent;
        if (params.screenBrightness > 1) {
            params.screenBrightness = 1;
        } else if (params.screenBrightness < 0) {
            params.screenBrightness = 0;
        }
        window.setAttributes(params);

        if (show) {
            if (mProgressBrightness.getVisibility() != VISIBLE) {
                mProgressBrightness.setVisibility(VISIBLE);
            }
            mProgressBrightness.setProgress((int) (mProgressBrightness.getMax() * percent));
        }
    }

    public void hideBrightChange() {
        mProgressBrightness.setVisibility(GONE);
    }


    public interface OnBackClickListener {
        public void onBackClick();
    }
}
