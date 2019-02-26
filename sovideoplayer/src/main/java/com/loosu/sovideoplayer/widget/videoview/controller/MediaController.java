package com.loosu.sovideoplayer.widget.videoview.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loosu.sovideoplayer.util.KLog;

import java.util.Locale;


public abstract class MediaController extends FrameLayout {
    private static final String TAG = "AbsSoVideoView";

    private MediaPlayerControl mPlayer;
    private ViewGroup mAnchor;

    protected boolean mShowing = true;
    protected boolean mDragging = false;
    private static final int sDefaultTimeout = 3000;

    public MediaController(@NonNull Context context) {
        super(context);
    }

    public boolean isShowing() {
        return mShowing;
    }

    public boolean isDragging() {
        return mDragging;
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    public void detachMediaPlayer() {
        mPlayer = null;
    }

    public void setAnchorView(ViewGroup view) {
        if (mAnchor != null) {
            mAnchor.removeView(this);
        }

        mAnchor = view;
        if (mAnchor != null) {
            mAnchor.addView(this,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    protected abstract View makeControllerView();

    public void show() {
        show(sDefaultTimeout);
    }

    public void show(long timeout) {
        if (!mShowing) {
            setProgress();
            mShowing = true;
        }
        updatePausePlay();
        post(mShowProgress);

        if (timeout > 0) {
            removeCallbacks(mFadeOut);
            postDelayed(mFadeOut, timeout);
        }
    }

    public void hide() {
        if (mShowing) {
            removeCallbacks(mFadeOut);
            mShowing = false;
        }
    }

    public void doPauseResume() {
        final MediaPlayerControl player = mPlayer;
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
        }
        updatePausePlay();
    }

    protected void updatePausePlay() {
        // 子类实现
    }

    protected int setProgress() {
        // 子类实现
        return 0;
    }

    protected String stringForTime(long timeMs) {
        int totalSeconds = (int) (timeMs / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    public MediaPlayerControl getPlayer() {
        return mPlayer;
    }

    private final Runnable mFadeOut = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    protected final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            KLog.d(TAG, "更新进度 mDragging = " + mDragging + ", mShowing = " + mShowing);
            int pos = setProgress();
            if (!mDragging && mShowing) {
                postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    public interface MediaPlayerControl {
        void start();

        void pause();

        long getDuration();

        long getCurrentPosition();

        void seekTo(long pos);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        /**
         * Get the audio session id for the player used by this VideoView. This can be used to
         * apply audio effects to the audio track of a video.
         *
         * @return The audio session, or 0 if there was an error.
         */
        int getAudioSessionId();
    }
}