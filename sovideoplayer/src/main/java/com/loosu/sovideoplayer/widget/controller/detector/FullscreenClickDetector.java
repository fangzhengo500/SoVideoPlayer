package com.loosu.sovideoplayer.widget.controller.detector;

import android.content.Context;
import android.view.MotionEvent;

import com.loosu.sovideoplayer.util.KLog;
import com.loosu.sovideoplayer.widget.controller.FullscreenGestureController;


public class FullscreenClickDetector extends AbsGestureDetector<FullscreenGestureController> {
    private static final String TAG = "FullscreenClickDetector";

    public FullscreenClickDetector(Context context, FullscreenGestureController controller) {
        super(context, controller);
    }

    @Override
    public void onControllerSizeChanged(int w, int h, int oldw, int oldh) {
        mTriggerRect.set(0, 0, w, h);
        mControlRect.set(0, 0, w, h);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        KLog.w(TAG, "");
        if (mController.isShowing()) {
            mController.hide();
        } else {
            mController.show();
        }
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        KLog.e(TAG, "");
        mController.doPauseResume();
        return true;
    }
}
