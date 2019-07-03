package com.example.itigao.Media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import java.io.IOException;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JzvdMgr;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * Created by Nathen on 2017/11/18.
 */

public class JZMediaIjkplayer extends JZMediaInterface implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnTimedTextListener {
    IjkMediaPlayer ijkMediaPlayer;

    @Override
    public void start() {
        ijkMediaPlayer.start();
    }

    @Override
    public void prepare() {
        ijkMediaPlayer = new IjkMediaPlayer();
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 1024 * 1024);

        ijkMediaPlayer.setOnPreparedListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnVideoSizeChangedListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnCompletionListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnErrorListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnInfoListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnBufferingUpdateListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnSeekCompleteListener(JZMediaIjkplayer.this);
        ijkMediaPlayer.setOnTimedTextListener(JZMediaIjkplayer.this);

        //某些视频在SeekTo的时候，会跳回到拖动前的位置，这是因为视频的关键帧的问题，通俗一点就是FFMPEG不兼容，视频压缩过于厉害，seek只支持关键帧，出现这个情况就是原始的视频文件中i 帧比较少
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        //播放前的探测Size，默认是1M, 改小一点会出画面更快
        //ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024 * 10);
        //播放重连次数
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER,"reconnect",5);
        //
        ijkMediaPlayer.setOption(1, "analyzemaxduration", 100L);
        ijkMediaPlayer.setOption(1, "probesize", 10240L);
        ijkMediaPlayer.setOption(1, "flush_packets", 1L);
        ijkMediaPlayer.setOption(4, "packet-buffering", 0L);
        ijkMediaPlayer.setOption(4, "framedrop", 1L);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
        try {
            ijkMediaPlayer.setDataSource(jzDataSource.getCurrentUrl().toString());
            ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            ijkMediaPlayer.setScreenOnWhilePlaying(true);
            ijkMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        ijkMediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return ijkMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        ijkMediaPlayer.seekTo(time);
    }

    @Override
    public void release() {
        if (ijkMediaPlayer != null)
            ijkMediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        return ijkMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return ijkMediaPlayer.getDuration();
    }

    @Override
    public void setSurface(Surface surface) {
        ijkMediaPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        ijkMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void setSpeed(float speed) {
        ijkMediaPlayer.setSpeed(speed);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        ijkMediaPlayer.start();
        if (jzDataSource.getCurrentUrl().toString().toLowerCase().contains("mp3")) {
            JZMediaManager.instance().mainThreadHandler.post(() -> {
                if (JzvdMgr.getCurrentJzvd() != null) {
                    JzvdMgr.getCurrentJzvd().onPrepared();
                }
            });
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        JZMediaManager.instance().currentVideoWidth = iMediaPlayer.getVideoWidth();
        JZMediaManager.instance().currentVideoHeight = iMediaPlayer.getVideoHeight();
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onVideoSizeChanged();
            }
        });
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onAutoCompletion();
            }
        });
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onError(what, extra);
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    JzvdMgr.getCurrentJzvd().onPrepared();
                } else {
                    JzvdMgr.getCurrentJzvd().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, final int percent) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().setBufferProgress(percent);
            }
        });
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        JZMediaManager.instance().mainThreadHandler.post(() -> {
            if (JzvdMgr.getCurrentJzvd() != null) {
                JzvdMgr.getCurrentJzvd().onSeekComplete();
            }
        });
    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {

    }
}
