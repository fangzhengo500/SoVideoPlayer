package com.loosu.sample.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loosu.sample.R;
import com.loosu.sample.adapter.AbsVideoAdapter;
import com.loosu.sample.adapter.SimpleVideoAdapter;
import com.loosu.sample.adapter.VideoViewAdapter;
import com.loosu.sample.adapter.base.recyclerview.ARecyclerAdapter;
import com.loosu.sample.adapter.base.recyclerview.IRecyclerItemClickListener;
import com.loosu.sample.domain.VideoEntry;
import com.loosu.sample.ui.activity.SimplePlayerActivity;
import com.loosu.sample.utils.DataHelper;
import com.loosu.sovideoplayer.IjkMediaPlayerTestActivity;
import com.loosu.sovideoplayer.util.KLog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VideoListFragment extends Fragment implements IRecyclerItemClickListener, OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = "VideoListFragment";

    private static final int PAGE_SIZE = 3;

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mViewList;

    private AbsVideoAdapter mAdapter;


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
        mAdapter = new SimpleVideoAdapter();
        //refreshData();
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
        mRefreshLayout.setOnLoadMoreListener(this);
        mAdapter.setItemClickListener(this);
    }


    /**
     * 列表条目点击
     */
    @Override
    public void onItemClick(RecyclerView parent, int position, RecyclerView.ViewHolder holder, View view) {
        VideoEntry item = (VideoEntry) mAdapter.getItem(position);
        Intent intent = SimplePlayerActivity.getStartIntent(getContext(), item);
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
        logVideos(videos);
        // 加一个假数据
        VideoEntry videoEntry1 = new VideoEntry();
        videoEntry1.setData("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
        VideoEntry videoEntry2 = new VideoEntry();
        videoEntry2.setData("http://ivytest.i-weiying.com/74ba/video/20181222/20181222cceea418d72ed7ba9af883349500cba91545449583751.mp4?auth_key=1551327186-0-0-8e89d5683e02500bbb96c8b0b2ffe0f2");
        videos.add(0, videoEntry2);
        videos.add(0, videoEntry1);
        mAdapter.setDatas(videos);

        Toast.makeText(context, "更新 " + videos.size() + " 条数据", Toast.LENGTH_SHORT).show();
    }

    /**
     * 下拉刷新时回调
     */
    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        mRefreshLayout.finishRefresh(500);
        refreshData();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore(500);
        //List<VideoEntry> videos = DataHelper.getVideos(getContext(), mAdapter.getItemCount(), PAGE_SIZE);
        //mAdapter.addDatas(videos);
    }

    private void logVideos(List<VideoEntry> videoEntries) {
        if (videoEntries == null) {
            KLog.d(TAG, "videos is null.");
        } else {
            for (VideoEntry videoEntry : videoEntries) {
                KLog.d(TAG, videoEntry.toString());
            }
        }
    }

}
