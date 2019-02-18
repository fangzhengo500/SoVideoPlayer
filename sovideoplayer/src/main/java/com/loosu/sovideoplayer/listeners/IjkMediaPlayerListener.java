package com.loosu.sovideoplayer.listeners;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface IjkMediaPlayerListener extends IMediaPlayer.OnPreparedListener,
            IMediaPlayer.OnCompletionListener, IMediaPlayer.OnBufferingUpdateListener,
            IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnVideoSizeChangedListener,
            IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener,
            IMediaPlayer.OnTimedTextListener {
}