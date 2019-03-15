package com.loosu.sample.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.loosu.sample.R;
import com.loosu.sample.adapter.base.recyclerview.ARecyclerAdapter;
import com.loosu.sample.adapter.base.recyclerview.RecyclerHolder;
import com.loosu.sample.domain.VideoEntry;
import com.loosu.sovideoplayer.widget.videoview.FullScreenHelper;
import com.loosu.sovideoplayer.widget.videoview.controller.FullscreenGestureController;
import com.loosu.sovideoplayer.widget.videoview.controller.MediaController;
import com.loosu.sovideoplayer.widget.videoview.controller.SimplePreviewController;
import com.loosu.sovideoplayer.widget.videoview.SoVideoView;

import java.util.List;

public class VideoViewAdapter extends AbsVideoAdapter implements SimplePreviewController.Listener {

    private final Activity mActivity;

    public VideoViewAdapter(Activity activity) {
        super(null);
        mActivity = activity;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_video_view;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerHolder holder = super.onCreateViewHolder(parent, viewType);
        SoVideoView videoView = holder.getView(R.id.video_view);
        SimplePreviewController controller = new SimplePreviewController(parent.getContext());
        controller.setListener(this);
        videoView.setMediaController(controller);
        return holder;
    }

    @Override
    protected void onBindViewData(RecyclerHolder holder, int position, List<VideoEntry> datas) {
        Context context = holder.itemView.getContext();
        SoVideoView videoView = holder.getView(R.id.video_view);

        VideoEntry videoEntry = getItem(position);
        videoView.setDataSource(videoEntry.getData());

    }

    @Override
    public VideoEntry getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public void onFullscreenClick(MediaController controller) {
        final Context context = mActivity;

        FullscreenGestureController fullscreenController = new FullscreenGestureController(context, "sadfasdf");
        fullscreenController.setBackClickListener(new FullscreenGestureController.OnBackClickListener() {
            @Override
            public void onBackClick() {
                FullScreenHelper.getDefault().fullscreenExit();
            }
        });
        SoVideoView fullscreenVideo = new SoVideoView(context, true);
        fullscreenVideo.setMediaController(fullscreenController);

        FullScreenHelper.getDefault().fullscreen(mActivity, (SoVideoView) controller.getPlayer(), fullscreenVideo);
    }


}
