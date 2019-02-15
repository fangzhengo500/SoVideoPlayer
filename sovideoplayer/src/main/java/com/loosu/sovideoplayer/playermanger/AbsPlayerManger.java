package com.loosu.sovideoplayer.playermanger;


import android.view.SurfaceHolder;

public abstract class AbsPlayerManger implements IPlayerManager {

    private boolean isPlayerRelease = true;

    @Override
    public boolean isPlayerRelease() {
        return isPlayerRelease;
    }

    @Override
    public void reset() {
        try {
            getMediaPlayer().reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        try {
            getMediaPlayer().release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDisPlay(SurfaceHolder holder) {
        try {
            getMediaPlayer().setDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setDataSource(String path) {
        try {
            getMediaPlayer().setDataSource(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prepare() {
        try {
            getMediaPlayer().prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            getMediaPlayer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            getMediaPlayer().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        try {
            getMediaPlayer().pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void seekTo(long seek) {
        try {
            getMediaPlayer().seekTo(seek);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
