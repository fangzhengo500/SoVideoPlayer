package com.loosu.sample.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loosu.sample.R;
import com.loosu.sample.adapter.SimpleVideoAdapter;
import com.loosu.sample.adapter.VideoViewAdapter;
import com.loosu.sample.adapter.base.recyclerview.ARecyclerAdapter;
import com.loosu.sample.adapter.base.recyclerview.IRecyclerItemClickListener;
import com.loosu.sample.domain.VideoEntry;
import com.loosu.sample.ui.activity.SimplePlayerActivity;
import com.loosu.sample.utils.DataHelper;
import com.loosu.sovideoplayer.IjkMediaPlayerTestActivity;

import java.util.List;

public class VideoListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IRecyclerItemClickListener {
    private static final String TAG = "VideoListFragment";

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mViewList;

    private ARecyclerAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view, savedInstanceState);
        initView(view, savedInstanceState);
        initListener(view, savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        mAdapter = new VideoViewAdapter(getActivity());
        refreshData();
    }

    private void findView(View rootView, Bundle savedInstanceState) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mViewList = rootView.findViewById(R.id.view_list);
    }

    private void initView(View rootView, Bundle savedInstanceState) {
        final Context context = getContext();
        mViewList.setLayoutManager(new LinearLayoutManager(context));
        mViewList.setAdapter(mAdapter);
    }

    private void initListener(View rootView, Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setItemClickListener(this);
    }

    /**
     * 下拉刷新时回调
     */
    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshData();
                mRefreshLayout.setRefreshing(false);
            }
        }, 500);
    }

    /**
     * 列表条目点击
     */
    @Override
    public void onItemClick(RecyclerView parent, int position, RecyclerView.ViewHolder holder, View view) {
        Intent intent = SimplePlayerActivity.getStartIntent(getContext(), (VideoEntry) mAdapter.getItem(position));
        //Intent intent = IjkMediaPlayerTestActivity.getStartIntent(getContext(), mAdapter.getItem(position).getData());
        startActivity(intent);
    }

    private void refreshData() {
        final Context context = getContext();

        // 检查权限
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return;
        }

        List<VideoEntry> videos = DataHelper.getVideos(context);

        // 加一个假数据
        VideoEntry videoEntry = new VideoEntry();
        videoEntry.setData("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
        videos.add(0, videoEntry);

        mAdapter.setDatas(videos);
    }

}
