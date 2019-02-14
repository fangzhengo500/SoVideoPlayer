package com.loosu.sovideoplayer.playermanger;

public abstract class AbsPlayerManger implements IPlayerManager {

    private boolean isPlayerRelease = true;

    @Override
    public boolean isPlayerRelease() {
        return isPlayerRelease;
    }
}
