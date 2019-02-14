package com.loosu.sovideoplayer.playermanger;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class SoPlayerManager extends AbsPlayerManger {

    private volatile static SoPlayerManager sInstance;

    private IMediaPlayer mMediaPlayer;

    private SoPlayerManager() {
        mMediaPlayer = new IjkMediaPlayer();
    }

    public static IPlayerManager getInstance() {
        if (sInstance == null) {
            synchronized (SoPlayerManager.class) {
                if (sInstance == null) {
                    sInstance = new SoPlayerManager();
                }
            }
        }
        return sInstance;
    }


    @Override
    public IMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }
}
