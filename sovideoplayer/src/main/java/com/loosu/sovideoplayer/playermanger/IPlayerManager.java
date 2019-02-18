package com.loosu.sovideoplayer.playermanger;

import android.support.annotation.IntRange;
import android.view.SurfaceHolder;

import com.loosu.sovideoplayer.util.IjkMediaPlayerUtil;
import com.loosu.sovideoplayer.util.KLog;

import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

public interface IPlayerManager {

    public IMediaPlayer getMediaPlayer();

    public boolean isPlayerRelease();

    public void reset();

    public void release();

    public void setDisPlay(SurfaceHolder holder);

    public void setDataSource(String path);

    public void prepare();

    public void start();

    public void stop();

    public void pause();

    public void seekTo(long seek);

    public long getCurrentVideoDuration();

    public long getCurrentVideoPosition();

    public interface Listener {

        public void onBufferingUpdate(@IntRange(from = 0, to = 100) int percent);

        public void onCompletion();

        public boolean onError(int what, int extra);

        public boolean onInfo(int what, int extra);

        public void onPrepared();

        public void onSeekComplete();

        public void onVideoSizeChanged(int width, int height, int sar_num, int sar_den);
    }
}
