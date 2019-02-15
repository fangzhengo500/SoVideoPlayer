package com.loosu.sample.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.loosu.sample.R;
import com.loosu.sample.adapter.base.recyclerview.ARecyclerAdapter;
import com.loosu.sample.adapter.base.recyclerview.RecyclerHolder;
import com.loosu.sample.domain.VideoEntry;
import com.loosu.sovideoplayer.widget.SoVideoView;

import java.util.List;

public class VideoViewAdapter extends ARecyclerAdapter<VideoEntry> {
    public VideoViewAdapter(@Nullable List<VideoEntry> datas) {
        super(datas);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_video_view;
    }

    @Override
    protected void onBindViewData(RecyclerHolder holder, int position, List<VideoEntry> datas) {
        Context context = holder.itemView.getContext();

        VideoEntry videoEntry = getItem(position);

        SoVideoView videoView = holder.getView(R.id.video_view);
        videoView.setDataSource(videoEntry.getData());
    }

    @Override
    public VideoEntry getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public void setDatas(List<VideoEntry> datas) {
        super.setDatas(datas);
        notifyDataSetChanged();
    }
}
