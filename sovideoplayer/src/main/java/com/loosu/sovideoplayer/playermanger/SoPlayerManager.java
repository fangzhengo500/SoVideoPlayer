package com.loosu.sovideoplayer.playermanger;


import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class SoPlayerManager extends AbsPlayerManger {

    private volatile static SoPlayerManager sInstance;


    private Listener mListener;

    private SoPlayerManager() {
        super(new IjkMediaPlayer());
    }

    public static SoPlayerManager getInstance() {
        if (sInstance == null) {
            synchronized (SoPlayerManager.class) {
                if (sInstance == null) {
                    sInstance = new SoPlayerManager();
                }
            }
        }
        return sInstance;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    protected void bufferingUpdate(int percent) {
        if (mListener != null) {
            mListener.onBufferingUpdate(percent);
        }
    }

    protected void completion() {
        if (mListener != null) {
            mListener.onCompletion();
        }
    }

    protected boolean error(int what, int extra) {
        if (mListener != null) {
            return mListener.onError(what, extra);
        }
        return false;
    }

    protected boolean info(int what, int extra) {
        if (mListener != null) {
            return mListener.onInfo(what, extra);
        }
        return false;
    }

    protected void prepared() {
        if (mListener != null) {
            mListener.onPrepared();
        }
    }

    protected void seekComplete() {
        if (mListener != null) {
            mListener.onSeekComplete();
        }
    }

    protected void videoSizeChanged(int width, int height, int sar_num, int sar_den) {
        if (mListener != null) {
            mListener.onVideoSizeChanged(width, height, sar_num, sar_den);
        }
    }
}
