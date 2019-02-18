package com.loosu.sovideoplayer.widget.videocontroller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.loosu.sovideoplayer.R;

public class SimplePreviewController extends MediaController {
    public SimplePreviewController(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context)
                .inflate(R.layout.view_simple_preview_controller,this,true);
    }
}
