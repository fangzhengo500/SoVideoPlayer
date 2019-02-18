package com.loosu.sovideoplayer.widget.videocontroller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;



public abstract class MediaController extends FrameLayout {
    private static final String TAG = "AbsSoVideoView";

    private MediaPlayerControl mPlayer;
    private ViewGroup mAnchor;

    protected boolean mShowing = true;

    public MediaController(@NonNull Context context) {
        super(context);
    }

    public boolean isShowing() {
        return mShowing;
    }

    public void show(long duration) {
        mShowing = true;
    }

    public void hide() {
        mShowing = false;
    }

    public void attachMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
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

    public interface MediaPlayerControl {
        void start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

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