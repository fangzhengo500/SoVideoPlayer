package com.loosu.sovideoplayer.widget;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.loosu.sovideoplayer.R;
import com.loosu.sovideoplayer.util.KLog;
import com.loosu.sovideoplayer.util.PixelFormatUtil;

import java.util.Locale;

public class SoVideoView extends FrameLayout {
    private static final String TAG = "SoVideoView";

    private SurfaceView mSurfaceView;

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
}
