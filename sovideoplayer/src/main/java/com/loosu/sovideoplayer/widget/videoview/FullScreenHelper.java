package com.loosu.sovideoplayer.widget.videoview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;

import com.loosu.sovideoplayer.util.SystemUiUtil;

public class FullScreenHelper {

    private volatile static FullScreenHelper mInstance;

    private Activity mActivity;
    private boolean mActionBarShowing;
    private boolean mSupportActionBarShowing;
    private int mOrientation;
    private int mSystemUi;

    public static FullScreenHelper getDefault() {
        if (mInstance == null) {
            synchronized (FullScreenHelper.class) {
                if (mInstance == null) {
                    mInstance = new FullScreenHelper();
                }
            }
        }
        return mInstance;
    }

    public void fullscreen(Activity activity) {
        if (mActivity != null) {
            throw new IllegalStateException("is fullscreen now！！！");
        }

        mActivity = activity;

        // 保存 toolbar 状态
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                mActionBarShowing = actionBar.isShowing();
                actionBar.hide();
            }
            android.support.v7.app.ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (supportActionBar != null) {
                mSupportActionBarShowing = supportActionBar.isShowing();
                supportActionBar.hide();
            }

        } else {
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                mActionBarShowing = actionBar.isShowing();
                actionBar.hide();
            }
        }

        // 保存 screen orientation 状态
        mOrientation = activity.getRequestedOrientation();
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);


        // 保存 SystemUi 状态
        mSystemUi = activity.getWindow().getDecorView().getSystemUiVisibility();
        SystemUiUtil.hideSystemUi(activity);
    }

    public void fullscreenExit() {
        final Activity activity = mActivity;
        mActivity = null;

        // 恢复 toolbar
        if (activity instanceof AppCompatActivity) {
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                if (mActionBarShowing) {
                    actionBar.show();
                } else {
                    actionBar.hide();
                }
            }

            android.support.v7.app.ActionBar supportActionBar = ((AppCompatActivity) activity).getSupportActionBar();
            if (supportActionBar != null) {
                if (mSupportActionBarShowing) {
                    supportActionBar.show();
                } else {
                    supportActionBar.hide();
                }
            }

        } else {
            ActionBar actionBar = activity.getActionBar();
            if (actionBar != null) {
                if (mActionBarShowing) {
                    actionBar.show();
                } else {
                    actionBar.hide();
                }
            }
        }

        // 恢复 screen orientation
        activity.setRequestedOrientation(mOrientation);

        // 恢复 SystemUi 状态
        activity.getWindow().getDecorView().setSystemUiVisibility(mSystemUi);
    }
}
