package com.loosu.sovideoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.loosu.sovideoplayer.util.IjkMediaPlayerUtil;
import com.loosu.sovideoplayer.util.KLog;
import com.loosu.sovideoplayer.util.PixelFormatUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class IjkMediaPlayerTestActivity extends AppCompatActivity {

    private static final String TAG = "IjkMediaPlayerTestActivity";
    private static final String KEY_DATA_SOURCE = "key_data_source";

    private SurfaceView mSurfaceView;
    private TextView mTvInfo;
    private TextView mTvProgress;
    private SeekBar mSbProgress;

    private Handler mHandler = new Handler();

    private IMediaPlayer mMediaPlayer = new IjkMediaPlayer();
    private String mDataSource;

    public static Intent getStartIntent(Context context, String dataSource) {
        Intent intent = new Intent(context, IjkMediaPlayerTestActivity.class);
        intent.putExtra(KEY_DATA_SOURCE, dataSource);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_media_player);
        init(savedInstanceState);
        findView(savedInstanceState);
        initView(savedInstanceState);
        initListener(savedInstanceState);
    }

    private void findView(Bundle savedInstanceState) {
        mSurfaceView = findViewById(R.id.surface_view);
        mTvInfo = findViewById(R.id.tv_info);

        mTvProgress = findViewById(R.id.tv_progress);
        mSbProgress = findViewById(R.id.sb_progress);
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mDataSource = intent.getStringExtra(KEY_DATA_SOURCE);
    }

    private void initView(Bundle savedInstanceState) {
        updateInfo(mMediaPlayer);
    }

    private void initListener(Bundle savedInstanceState) {
        mSurfaceView.getHolder().addCallback(mSurfaceCallback);

        mMediaPlayer.setOnPreparedListener(new IjkMediaPlayerListener());
        mMediaPlayer.setOnCompletionListener(new IjkMediaPlayerListener());
        mMediaPlayer.setOnBufferingUpdateListener(new IjkMediaPlayerListener());
        mMediaPlayer.setOnSeekCompleteListener(new IjkMediaPlayerListener());
        mMediaPlayer.setOnVideoSizeChangedListener(new IjkMediaPlayerListener());
        mMediaPlayer.setOnErrorListener(new IjkMediaPlayerListener());
        mMediaPlayer.setOnInfoListener(new IjkMediaPlayerListener());
        mMediaPlayer.setOnTimedTextListener(new IjkMediaPlayerListener());
    }

    private void showMsg(String msg) {
        KLog.w(TAG, msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void updateInfo(IMediaPlayer mp) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n ================ player info ================ \n")
                .append("data source: ").append(mp.getDataSource()).append('\n')
                .append("w: ").append(mp.getVideoWidth()).append(", h: ").append(mp.getVideoHeight()).append('\n')
                .append("is playing: ").append(mp.isPlaying()).append('\n')
                .append("is looping: ").append(mp.isLooping()).append('\n')
                .append("time: ").append(formatDuration(mp.getCurrentPosition())).append(" / ").append(formatDuration(mp.getDuration())).append('\n')
                .append("audio session id: ").append(mp.getAudioSessionId()).append('\n')
                .append("sar num: ").append(mp.getVideoSarNum()).append(", sar den: ").append(mp.getVideoSarDen()).append('\n')
                .append('\n');

        sb.append("================ media info ================ \n");
        MediaInfo mediaInfo = mp.getMediaInfo();
        sb.append(IjkMediaPlayerUtil.mediaInfoToString(mediaInfo));


        sb.append("================ track info ================ \n");
        ITrackInfo[] trackInfos = mp.getTrackInfo();
        sb.append(IjkMediaPlayerUtil.trackInfosToString(trackInfos));


        String text = sb.toString();
        mTvInfo.setText(text);
        //KLog.d(TAG, text);
    }

    public String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static String formatDuration(long duration) {
        long totalSeconds = duration / 1000;

        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_reset) {
            onClickReset(view);

        } else if (i == R.id.btn_release) {
            onClickRelease(view);

        } else if (i == R.id.btn_set_data) {
            onClickSetDataSource(view);

        } else if (i == R.id.btn_prepare) {
            onClickPrepareAsync(view);

        } else if (i == R.id.btn_start) {
            onClickStart(view);

        } else if (i == R.id.btn_stop) {
            onClickStop(view);

        } else if (i == R.id.btn_pause) {
            onClickPause(view);

        }
        updateInfo(mMediaPlayer);
    }

    public void onClickSetDataSource(View view) {
        try {
            mMediaPlayer.setDataSource(mDataSource);
        } catch (Exception e) {
            showMsg(getTrace(e));
        }
    }

    public void onClickPrepareAsync(View view) {
        try {
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            showMsg(getTrace(e));
        }
    }

    public void onClickStart(View view) {
        try {
            mMediaPlayer.start();
            mHandler.post(mUpdateProgressRunable);
        } catch (Exception e) {
            showMsg(getTrace(e));
        }
    }

    public void onClickStop(View view) {
        try {
            mMediaPlayer.stop();
        } catch (Exception e) {
            showMsg(getTrace(e));
        }
        mHandler.removeCallbacks(mUpdateProgressRunable);
    }

    public void onClickPause(View view) {
        try {
            mMediaPlayer.pause();
        } catch (Exception e) {
            showMsg(getTrace(e));
        }
        mHandler.removeCallbacks(mUpdateProgressRunable);
    }

    public void onClickReset(View view) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDisplay(mSurfaceView.getHolder());
        } catch (Exception e) {
            showMsg(getTrace(e));
        }
        mHandler.removeCallbacks(mUpdateProgressRunable);
    }

    public void onClickRelease(View view) {
        try {
            mMediaPlayer.release();
        } catch (Exception e) {
            showMsg(getTrace(e));
        }
        mHandler.removeCallbacks(mUpdateProgressRunable);
    }

    private SurfaceHolder.Callback2 mSurfaceCallback = new SurfaceHolder.Callback2() {
        @Override
        public void surfaceRedrawNeeded(SurfaceHolder holder) {
            KLog.d(TAG, "holder = " + holder);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            KLog.i(TAG, "holder = " + holder);
            mMediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            KLog.d(TAG, String.format(Locale.US, "holder = %s, format = %s, width = %d, height = %d",
                    holder, PixelFormatUtil.formatToString(format), width, height));
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            KLog.w(TAG, "holder = " + holder);
            mMediaPlayer.release();
        }
    };

    private class IjkMediaPlayerListener implements IMediaPlayer.OnPreparedListener,
            IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener,
            IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnVideoSizeChangedListener,
            IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener,
            IMediaPlayer.OnTimedTextListener {
        @Override
        public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            KLog.d(TAG, "percent = " + percent);
            updateInfo(mMediaPlayer);
        }

        @Override
        public void onCompletion(IMediaPlayer mp) {
            KLog.i(TAG, "");
            updateInfo(mMediaPlayer);
        }

        @Override
        public boolean onError(IMediaPlayer mp, int what, int extra) {
            mHandler.removeCallbacks(mUpdateProgressRunable);
            boolean result = false;
            KLog.e(TAG, String.format(Locale.US, "what = %s, extra = %d, result = %s",
                    IjkMediaPlayerUtil.errorToString(getApplicationContext(), what), extra, result));
            updateInfo(mMediaPlayer);
            return result;
        }

        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {
            boolean result = false;
            KLog.i(TAG, String.format(Locale.US, "what = %s, extra = %d, result = %s",
                    IjkMediaPlayerUtil.infoToString(getApplicationContext(), what), extra, result));
            updateInfo(mMediaPlayer);
            return result;
        }

        @Override
        public void onPrepared(IMediaPlayer mp) {
            KLog.i(TAG, "");
            updateInfo(mMediaPlayer);
        }

        @Override
        public void onSeekComplete(IMediaPlayer mp) {
            KLog.i(TAG, "");
            updateInfo(mMediaPlayer);
            mHandler.removeCallbacks(mUpdateProgressRunable);
        }

        @Override
        public void onTimedText(IMediaPlayer mp, IjkTimedText text) {
            KLog.i(TAG, "");
            updateInfo(mMediaPlayer);
        }

        @Override
        public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
            KLog.i(TAG, String.format(Locale.US, "width = %d, height = %d, sar_num = %d, sar_den = %d"
                    , width, height, sar_num, sar_den));
            updateInfo(mMediaPlayer);
        }
    }

    private Runnable mUpdateProgressRunable = new Runnable() {
        @Override
        public void run() {
            long currentPosition = mMediaPlayer.getCurrentPosition();
            long duration = mMediaPlayer.getDuration();

            String cur = formatDuration(currentPosition);
            String dur = formatDuration(duration);

            mSbProgress.setMax(Integer.parseInt(dur));
            mSbProgress.setProgress(Integer.parseInt(cur));

            mTvProgress.setText(String.format("%s/%s", cur, dur));

            mHandler.postDelayed(this, 300);
        }
    };

}
