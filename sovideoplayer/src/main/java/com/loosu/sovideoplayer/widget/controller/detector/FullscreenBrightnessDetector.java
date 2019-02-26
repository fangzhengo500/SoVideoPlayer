package com.loosu.sovideoplayer.widget.controller.detector;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.loosu.sovideoplayer.widget.controller.FullscreenGestureController;


public class FullscreenBrightnessDetector extends AbsGestureDetector<FullscreenGestureController> {

    private float mScreenBrightness;

    public FullscreenBrightnessDetector(Context context, FullscreenGestureController controller) {
        super(context, controller);
    }

    @Override
    public void onControllerSizeChanged(int w, int h, int oldw, int oldh) {
        mTriggerRect.set(0, 0, w / 2, h);
        mControlRect.set(0, 0, w, h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mHandling = false;
                mController.hideBrightChange();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        super.onDown(e);
        WindowManager.LayoutParams windowParams = ((Activity) mContext).getWindow().getAttributes();
        mScreenBrightness = windowParams.screenBrightness;
        if (mScreenBrightness == -1) {
            mScreenBrightness = getSysScreenBrightnessInFloat(mScreenBrightness);
        }
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
            float changeBright;
            if (viewHeight > 0) {
                changeBright = -moveY * 3f / viewHeight;
            } else {
                changeBright = (float) (-moveY * 0.002);
            }
            mController.setBright(mScreenBrightness + changeBright, true);
        }

        return true;
    }

    /**
     * @param defaultBrightness
     * @return [0 ~ 1]
     */
    private float getSysScreenBrightnessInFloat(float defaultBrightness) {
        float result = defaultBrightness;
        try {
            int sysBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            result = sysBrightness * 1f / 255;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
