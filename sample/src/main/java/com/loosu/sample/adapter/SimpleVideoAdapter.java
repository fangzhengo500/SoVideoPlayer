package com.loosu.sample.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loosu.sample.R;
import com.loosu.sample.adapter.base.recyclerview.ARecyclerAdapter;
import com.loosu.sample.adapter.base.recyclerview.RecyclerHolder;
import com.loosu.sample.domain.VideoEntry;
import com.loosu.sample.utils.TimeUtil;

import java.util.List;

public class SimpleVideoAdapter extends ARecyclerAdapter<VideoEntry> {
    public SimpleVideoAdapter(@Nullable List<VideoEntry> datas) {
        super(datas);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_simple_video;
    }

    @Override
    protected void onBindViewData(RecyclerHolder holder, int position, List<VideoEntry> datas) {
        Context context = holder.itemView.getContext();

        VideoEntry videoEntry = getItem(position);

        holder.setText(R.id.tv_title, videoEntry.getDisplayName());
        holder.setText(R.id.tv_bottom_duration, TimeUtil.formatDuration(videoEntry.getDuration()));
        holder.setText(R.id.tv_size, Formatter.formatShortFileSize(context, videoEntry.getSize()));
        holder.setText(R.id.tv_display_size, videoEntry.getWidth() + "X" + videoEntry.getHeight());

        Glide.with(holder.itemView)
                .load(videoEntry.getData())
                .into((ImageView) holder.getView(R.id.iv_cover));
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
