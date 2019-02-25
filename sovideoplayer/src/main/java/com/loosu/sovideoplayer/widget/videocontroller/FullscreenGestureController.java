package com.loosu.sovideoplayer.widget.videocontroller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.loosu.sovideoplayer.R;


public class FullscreenGestureController extends AbsGestureController {
    private View mRoot;
    private View mLayoutTop;
    private View mBtnBack;
    private TextView mTvTitle;

    private ImageView mBtnPlay;

    private View mLayoutBottom;
    private TextView mTvProgress;
    private TextView mTvDuration;
    private SeekBar mSbProgress;

    private String mTileStr;

    private OnBackClickListener mBackClickListener;

    public FullscreenGestureController(@NonNull Context context, String title) {
        super(context);
        makeControllerView();
        setTitle(title);
    }

    @Override
    protected View makeControllerView() {
        mRoot = LayoutInflater.from(getContext()).inflate(R.layout.view_fullscreen_gesture_controller, this, true);
        initController(mRoot);
        return mRoot;
    }

    @Override
    public void show(long timeout) {
        super.show(timeout);
        mLayoutTop.setVisibility(VISIBLE);
        mBtnPlay.setVisibility(VISIBLE);
        mLayoutBottom.setVisibility(VISIBLE);
    }

    @Override
    public void hide() {
        super.hide();
        mLayoutTop.setVisibility(GONE);
        mBtnPlay.setVisibility(GONE);
        mLayoutBottom.setVisibility(GONE);
    }

    private void initController(View root) {
        mLayoutTop = root.findViewById(R.id.layout_top);
        mBtnBack = root.findViewById(R.id.btn_back);
        mTvTitle = root.findViewById(R.id.tv_title);

        mBtnPlay = root.findViewById(R.id.btn_play);

        mLayoutBottom = root.findViewById(R.id.layout_bottom);
        mTvProgress = root.findViewById(R.id.tv_progress);
        mTvDuration = root.findViewById(R.id.tv_duration);
        mSbProgress = root.findViewById(R.id.sb_progress);

        mBtnBack.setOnClickListener(mOnClickListener);
        mBtnPlay.setOnClickListener(mOnClickListener);
        mSbProgress.setOnSeekBarChangeListener(mSeekBarChangeListener);
    }

    @Override
    protected void updatePausePlay() {
        final MediaPlayerControl player = getPlayer();
        if (player != null && player.isPlaying()) {
            mBtnPlay.setImageResource(R.drawable.btn_pause_drawable);
        } else {
            mBtnPlay.setImageResource(R.drawable.btn_play_drawable);
        }
    }

    public void setBackClickListener(OnBackClickListener backClickListener) {
        mBackClickListener = backClickListener;
    }

    public void setTitle(String title) {
        mTileStr = title;
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    private void onClickBtnBack(View v) {
        if (mBackClickListener != null) {
            mBackClickListener.onBackClick();
        }
    }

    private void onClickBtnPlay() {

        doPauseResume();
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.btn_back) {
                onClickBtnBack(v);
            } else if (viewId == R.id.btn_play) {
                onClickBtnPlay();
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                // We're not interested in programmatically generated changes to
                // the progress bar's position.
                return;
            }
            MediaPlayerControl player = getPlayer();
            long duration = player.getDuration();
            long newposition = (long) (duration * progress * 1f / seekBar.getMax());
            player.seekTo((int) newposition);
            if (mTvProgress != null)
                mTvProgress.setText(stringForTime((int) newposition));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            show(3600000);
            mDragging = true;
            removeCallbacks(mShowProgress);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show();
            post(mShowProgress);
        }
    };

    public interface OnBackClickListener {
        public void onBackClick();
    }
}
