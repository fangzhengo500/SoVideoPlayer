package com.loosu.sovideoplayer.widget.videoview;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.loosu.sovideoplayer.util.SystemUiUtil;
import com.loosu.sovideoplayer.widget.videoview.controller.MediaController;

public class FullScreenHelper {
    private static final String TAG = "FullScreenHelper";

    private volatile static FullScreenHelper mInstance;

    private Activity mActivity;
    private boolean mActionBarShowing;
    private boolean mSupportActionBarShowing;
    private int mRequestOrientation;
    private int mOrientation;

    private int mSystemUi;

    private View mFullscreenView;

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

    public void fullscreen(Activity activity, MediaController.MediaPlayerControl player,  SoVideoView fullscreenVideo) {
        if (mActivity != null) {
            throw new IllegalStateException("is fullscreen now！！！");
        }
        mActivity = activity;
        mFullscreenView = fullscreenVideo;
        saveActivityState(activity);
        SystemUiUtil.hideSystemUi(activity);

        fullscreenVideo.setFocusable(true);
        fullscreenVideo.setFocusableInTouchMode(true);
        fullscreenVideo.requestFocus();
        findActivityContent(activity).addView(fullscreenVideo);

        player.pause();
        fullscreenVideo.start();
    }

    public void fullscreenExit() {
        final Activity activity = mActivity;
        mActivity = null;
        restoreActivityState(activity);
        findActivityContent(activity).removeView(mFullscreenView);
    }

    /**
     * 保存Activity状态
     */
    private void saveActivityState(Activity activity) {
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
        mRequestOrientation = activity.getRequestedOrientation();
        mOrientation = activity.getResources().getConfiguration().orientation;
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        // 保存 SystemUi 状态
        mSystemUi = activity.getWindow().getDecorView().getSystemUiVisibility();
    }

    /**
     * 恢复Activity状态
     */
    private void restoreActivityState(Activity activity) {
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
        if (mRequestOrientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            // 先根据之前的Activity方向指定具体方向
            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            // 再设置会原本的标志
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            activity.setRequestedOrientation(mRequestOrientation);
        }

        // 恢复 SystemUi 状态
        activity.getWindow().getDecorView().setSystemUiVisibility(mSystemUi);
    }

    private ViewGroup findActivityContent(Activity activity) {
        return activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }
}
