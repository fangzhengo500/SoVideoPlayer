package com.loosu.sovideoplayer.playermanger;


import android.view.SurfaceHolder;

import com.loosu.sovideoplayer.listeners.IjkMediaPlayerListener;
import com.loosu.sovideoplayer.util.IjkMediaPlayerUtil;
import com.loosu.sovideoplayer.util.KLog;

import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

public abstract class AbsPlayerManger implements IPlayerManager {
    private static final String TAG = "AbsPlayerManger";

    private IMediaPlayer mMediaPlayer = null;
    private boolean isPlayerRelease = true;


    AbsPlayerManger(IMediaPlayer mediaPlayer) {
        mediaPlayer.setOnPreparedListener(mMediaPlayerListener);
        mediaPlayer.setOnCompletionListener(mMediaPlayerListener);
        mediaPlayer.setOnBufferingUpdateListener(mMediaPlayerListener);
        mediaPlayer.setOnSeekCompleteListener(mMediaPlayerListener);
        mediaPlayer.setOnVideoSizeChangedListener(mMediaPlayerListener);
        mediaPlayer.setOnErrorListener(mMediaPlayerListener);
        mediaPlayer.setOnInfoListener(mMediaPlayerListener);
        mediaPlayer.setOnTimedTextListener(mMediaPlayerListener);

        mMediaPlayer = mediaPlayer;
    }

    @Override
    public IMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    @Override
    public boolean isPlayerRelease() {
        return isPlayerRelease;
    }

    @Override
    public void reset() {
        try {
            getMediaPlayer().reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        try {
            getMediaPlayer().release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDisPlay(SurfaceHolder holder) {
        try {
            getMediaPlayer().setDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataSource(String path) {
        try {
            getMediaPlayer().setDataSource(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepare() {
        try {
            getMediaPlayer().prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            getMediaPlayer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            getMediaPlayer().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        try {
            getMediaPlayer().pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void seekTo(long seek) {
        try {
            getMediaPlayer().seekTo(seek);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getCurrentVideoDuration() {
        try {
            return getMediaPlayer().getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public long getCurrentVideoPosition() {
        try {
            return getMediaPlayer().getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    protected void bufferingUpdate(int percent) {
    }

    protected void completion() {
    }

    protected boolean error(int what, int extra) {
        return false;
    }

    protected boolean info(int what, int extra) {
        return false;
    }

    protected void prepared() {
    }

    protected void seekComplete() {
    }

    protected void videoSizeChanged(int width, int height, int sar_num, int sar_den) {
    }

    private IjkMediaPlayerListener mMediaPlayerListener = new IjkMediaPlayerListener() {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            KLog.d(TAG, "percent = " + percent);
            bufferingUpdate(percent);
        }

        @Override
        public void onCompletion(IMediaPlayer mp) {
            KLog.i(TAG, "");
            completion();
        }

        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            boolean result = error(what, extra);
            KLog.e(TAG, String.format(Locale.US, "what = %s, extra = %d, result = %s", IjkMediaPlayerUtil.errorToString(what), extra, result));
            return result;
        }

        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            boolean result = info(what, extra);
            KLog.i(TAG, String.format(Locale.US, "what = %s, extra = %d, result = %s", IjkMediaPlayerUtil.infoToString(what), extra, result));
            return result;
        }

        @Override
        public void onPrepared(IMediaPlayer mp) {
            KLog.i(TAG, "");
            prepared();
        }

        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            KLog.i(TAG, "");
            seekComplete();
        }

        @Override
        public void onTimedText(IMediaPlayer mp, IjkTimedText text) {
            KLog.i(TAG, "");
        }

        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            KLog.i(TAG, String.format(Locale.US, "width = %d, height = %d, sar_num = %d, sar_den = %d", width, height, sar_num, sar_den));
            videoSizeChanged(width, height, sar_num, sar_den);
        }
    };
}
