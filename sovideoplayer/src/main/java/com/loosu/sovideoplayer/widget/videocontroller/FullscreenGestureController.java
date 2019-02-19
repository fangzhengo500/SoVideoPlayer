package com.loosu.sovideoplayer.widget.videocontroller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.loosu.sovideoplayer.R;

public class FullscreenGestureController extends AbsGestureController {

    private View mRoot;

    public FullscreenGestureController(@NonNull Context context) {
        super(context);
    }

    @Override
    protected View makeControllerView() {
        return mRoot = LayoutInflater.from(getContext()).inflate(R.layout.view_simple_preview_controller, this, true);
    }
}
