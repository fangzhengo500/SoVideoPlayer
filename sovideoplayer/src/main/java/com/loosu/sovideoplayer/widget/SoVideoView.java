package com.loosu.sovideoplayer.widget;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loosu.sovideoplayer.R;
import com.loosu.sovideoplayer.playermanger.IPlayerManager;
import com.loosu.sovideoplayer.playermanger.SoPlayerManager;
import com.loosu.sovideoplayer.util.KLog;
import com.loosu.sovideoplayer.util.PixelFormatUtil;

import java.util.Locale;

public class SoVideoView extends FrameLayout {
    private static final String TAG = "SoVideoView";

    private AutoFixSurfaceView mSurfaceView;

    private SeekBar mSbProgress;
    private ImageView mBtnPlay;
    private TextView mTvProgress;

    private String mDataSource;

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

        mSbProgress = findViewById(R.id.sb_progress);
        mBtnPlay = findViewById(R.id.btn_play);
        mTvProgress = findViewById(R.id.tv_progress);

        mSurfaceView.getHolder().addCallback(mSurfaceCallback);

        mSbProgress.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        mBtnPlay.setOnClickListener(mOnClickListener);
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

    private IPlayerManager.Listener mPlayerLisenter = new IPlayerManager.Listener() {
        @Override
        public void onBufferingUpdate(int percent) {

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

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            IPlayerManager playerManager = SoPlayerManager.getInstance();

            long seek = (long) (playerManager.getCurrentVideoDuration() * progress * 1f / seekBar.getMax());
            playerManager.seekTo(seek);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SoPlayerManager playerManager = SoPlayerManager.getInstance();
            playerManager.reset();
            playerManager.setDataSource(mDataSource);
            playerManager.prepare();
            playerManager.setDisPlay(mSurfaceView.getHolder());
            playerManager.setListener(mPlayerLisenter);
        }
    };
}
