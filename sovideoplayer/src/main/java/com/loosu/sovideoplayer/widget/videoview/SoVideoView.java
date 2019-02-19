package com.loosu.sovideoplayer.widget.videoview;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;

import com.loosu.sovideoplayer.R;
import com.loosu.sovideoplayer.playermanger.IPlayerManager;
import com.loosu.sovideoplayer.playermanger.SoPlayerManager;
import com.loosu.sovideoplayer.util.KLog;
import com.loosu.sovideoplayer.util.PixelFormatUtil;
import com.loosu.sovideoplayer.widget.AutoFixSurfaceView;
import com.loosu.sovideoplayer.widget.videocontroller.MediaController;

import java.util.Locale;

public class SoVideoView extends FrameLayout implements MediaController.MediaPlayerControl {
    private static final String TAG = "SoVideoView";

    private AutoFixSurfaceView mSurfaceView;

    private MediaController mMediaController;

    private String mDataSource;
    private int mPercent;

    public SoVideoView(@NonNull Context context) {
        this(context, null);
    }

    public SoVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_so_video, this, true);
        mSurfaceView = findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(mSurfaceCallback);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        KLog.i(TAG, this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KLog.w(TAG, this);
    }

    public void setDataSource(Uri uri) {
        try {
            mDataSource = uri.toString();
        } catch (Exception e) {
            e.printStackTrace();
            mDataSource = null;
        }
    }

    public void setDataSource(String path) {
        mDataSource = path;
    }

    public String getDataSource() {
        return mDataSource;
    }

    public void setMediaController(MediaController controller) {
        if (mMediaController != null) {
            mMediaController.detachMediaPlayer();
        }
        mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (mMediaController != null) {
            mMediaController.setMediaPlayer(this);
            mMediaController.setAnchorView(this);
            mMediaController.setEnabled(true);
        }
    }

    @Override
    public void start() {
        SoPlayerManager pm = SoPlayerManager.getInstance();
        pm.reset();
        pm.setDataSource(mDataSource);
        pm.prepare();
        pm.setDisPlay(mSurfaceView.getHolder());
        pm.setListener(mPlayerLisenter);
    }

    @Override
    public void pause() {
        SoPlayerManager.getInstance().pause();
    }

    @Override
    public long getDuration() {
        return SoPlayerManager.getInstance().getCurrentVideoDuration();
    }

    @Override
    public long getCurrentPosition() {
        return SoPlayerManager.getInstance().getCurrentVideoPosition();
    }

    @Override
    public void seekTo(long pos) {
        SoPlayerManager.getInstance().seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return SoPlayerManager.getInstance().isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return mPercent;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private IPlayerManager.Listener mPlayerLisenter = new IPlayerManager.Listener() {
        @Override
        public void onBufferingUpdate(int percent) {
            mPercent = percent;
        }

        @Override
        public void onCompletion() {

        }

        @Override
        public boolean onError(int what, int extra) {
            return true;
        }

        @Override
        public boolean onInfo(int what, int extra) {
            return true;
        }

        @Override
        public void onPrepared() {

        }

        @Override
        public void onSeekComplete() {

        }

        @Override
        public void onVideoSizeChanged(int width, int height, int sar_num, int sar_den) {
            mSurfaceView.setAspectRatio(width, height);
        }
    };

    private SurfaceHolder.Callback2 mSurfaceCallback = new SurfaceHolder.Callback2() {
        @Override
        public void surfaceRedrawNeeded(SurfaceHolder holder) {
            KLog.d(TAG, "holder = " + holder);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            KLog.i(TAG, "holder = " + holder);
//            KLog.d(TAG, "surface 设置显示");
//            SoPlayerManager pm = SoPlayerManager.getInstance();
//            pm.setDisPlay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.d(TAG, String.format(Locale.US, "holder = %s, format = %s, width = %d, height = %d",
                    holder, PixelFormatUtil.formatToString(format), width, height));
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.w(TAG, "holder = " + holder);
        }
    };

}
