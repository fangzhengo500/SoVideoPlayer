package com.loosu.sample.adapter;

import android.support.annotation.Nullable;

import com.loosu.sample.adapter.base.recyclerview.ARecyclerAdapter;
import com.loosu.sample.domain.VideoEntry;

import java.util.List;

public abstract class AbsVideoAdapter extends ARecyclerAdapter<VideoEntry> {

    public AbsVideoAdapter(@Nullable List<VideoEntry> datas) {
        super(datas);
    }

    @Override
    public void setDatas(List<VideoEntry> datas) {
        super.setDatas(datas);
        notifyDataSetChanged();
    }

    public void addDatas(List<VideoEntry> videos) {
        if (videos == null) {
            return;
        }
        int positionStart = getItemCount();
        int itemCount = videos.size();
        mDatas.addAll(videos);
        notifyItemRangeInserted(positionStart, itemCount);
    }
}
