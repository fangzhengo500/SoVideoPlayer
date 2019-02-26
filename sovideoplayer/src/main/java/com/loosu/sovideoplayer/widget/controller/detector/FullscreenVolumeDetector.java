package com.loosu.sovideoplayer.widget.controller.detector;

import android.content.Context;
import android.media.AudioManager;
import android.view.MotionEvent;

import com.loosu.sovideoplayer.widget.controller.FullscreenGestureController;


public class FullscreenVolumeDetector extends AbsGestureDetector<FullscreenGestureController> {

    private int mVolume;    // 记录按下时--系统声量
    private int mMaxVolume; // 记录按下时--系统最大声量

    public FullscreenVolumeDetector(Context context, FullscreenGestureController controller) {
        super(context, controller);
    }

    @Override
    public void onControllerSizeChanged(int w, int h, int oldw, int oldh) {
        mTriggerRect.set(w / 2, 0, w, h);
        mControlRect.set(0, 0, w, h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mHandling = false;
                mController.hideVolumeChange();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        super.onDown(e);
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int streamType = AudioManager.STREAM_MUSIC;
        mVolume = am.getStreamVolume(streamType);
        mMaxVolume = am.getStreamMaxVolume(streamType);

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent downEvent, MotionEvent event, float distanceX, float distanceY) {
        float downEventX = downEvent.getX();
        float downEventY = downEvent.getY();

        float moveX = event.getX() - downEventX;
        float moveY = event.getY() - downEventY;

        float xDiff = Math.abs(moveX);
        float yDiff = Math.abs(moveY);

        if (!mHandling) {
            if (yDiff > xDiff && mTriggerRect.contains(downEventX, downEventY)) {
                mHandling = true;
            }
        } else {
            int viewHeight = mController.getHeight();
            int dVol = 0;
            if (viewHeight > 0) {
                dVol = (int) (-moveY * mMaxVolume * 3 / viewHeight);
            } else {
                dVol = (int) (-moveY * 0.02);
            }
            int newVol = mVolume + dVol;

            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, newVol, 0);

            mController.showVolumeChange(newVol * 1f / mMaxVolume);
        }

        return true;
    }
}
