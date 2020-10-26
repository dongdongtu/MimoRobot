package com.chance.mimorobot.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.model.SongPlayInfo;


import java.io.IOException;

/**
 * 音乐播放服务
 */
public class PlayMusicManager implements MediaPlayer.OnCompletionListener {
    private static final String TAG = "Service";
    private static final long TIME_UPDATE = 300L;

    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private final Handler mHandler = new Handler();
    private AudioFocusManager mAudioFocusManager;
    private MediaPlayer mPlayer = null;
    private OnPlayerEventListener mListener;
    private AiuiResultEntity.DataBean.ResultBean mPlayingMusic;
    private SongPlayInfo mPlayingMusic1;
    private int mPlayState = STATE_IDLE;

    private static PlayMusicManager playMusicManager;
    Context context;

    public static PlayMusicManager newInstanse(Context context) {
        if (playMusicManager == null) {
            playMusicManager = new PlayMusicManager(context);
        }
        return playMusicManager;
    }

    public Context getContext() {
        return context;
    }

    public PlayMusicManager(Context context) {
        this.context = context;
        mAudioFocusManager = new AudioFocusManager(this);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    public OnPlayerEventListener getOnPlayEventListener() {
        return mListener;
    }

    public void setOnPlayEventListener(OnPlayerEventListener listener) {
        mListener = listener;
    }

    public void play(AiuiResultEntity.DataBean.ResultBean music) {
        mPlayingMusic = music;
        try {
            mPlayer.reset();
            Log.e("TAG", "播放路径: " + music.getPlayUrl());
            mPlayer.setDataSource(music.getPlayUrl());
            mPlayer.prepareAsync();
            mPlayState = STATE_PREPARING;
            mPlayer.setOnPreparedListener(mPreparedListener);
            mPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            if (mListener != null) {
                mListener.onChange(music);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(SongPlayInfo music) {
        mPlayingMusic1 = music;
        try {
            mPlayer.reset();
            Log.e("TAG", "播放路径: " + music.getPlayUrl());
            mPlayer.setDataSource(music.getPlayUrl());
            mPlayer.prepareAsync();
            mPlayState = STATE_PREPARING;
            mPlayer.setOnPreparedListener(mPreparedListener);
            mPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            if (mListener != null) {
                mListener.onChange(music);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (isPreparing()) {
                start();
            }
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (mListener != null) {
                mListener.onBufferingUpdate(percent);
            }
        }
    };

    public void playPause() {
        if (isPreparing()) {
            stop();
        } else if (isPlaying()) {
            pause();
        } else if (isPausing()) {
            start();
        } else {
            if (mPlayingMusic != null)
                play(mPlayingMusic);
            else
                play(mPlayingMusic1);
        }
    }

    void start() {
        if (!isPreparing() && !isPausing()) {
            return;
        }

        if (mAudioFocusManager.requestAudioFocus()) {
            mPlayer.start();
            mPlayState = STATE_PLAYING;
            mHandler.post(mPublishRunnable);

            if (mListener != null) {
                mListener.onPlayerStart();
            }
        }
    }

    void pause() {
        if (!isPlaying()) {
            return;
        }

        mPlayer.pause();
        mPlayState = STATE_PAUSE;
        mHandler.removeCallbacks(mPublishRunnable);

        if (mListener != null) {
            mListener.onPlayerPause();
        }
    }

    public void stop() {
        if (isIdle()) {
            return;
        }

        pause();
        mPlayer.reset();
        mPlayState = STATE_IDLE;
    }

    public void next() {
    }

    public void prev() {

    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPausing()) {
            mPlayer.seekTo(msec);
            if (mListener != null) {
                mListener.onPublish(msec);
            }
        }
    }

    public boolean isPlaying() {
        return mPlayState == STATE_PLAYING;
    }

    public boolean isPausing() {
        return mPlayState == STATE_PAUSE;
    }

    public boolean isPreparing() {
        return mPlayState == STATE_PREPARING;
    }

    public boolean isIdle() {
        return mPlayState == STATE_IDLE;
    }

    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying() && mListener != null) {
                mListener.onPublish(mPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    public void onDestroy() {
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        mAudioFocusManager.abandonAudioFocus();
        Log.i(TAG, "onDestroy: " + getClass().getSimpleName());
    }

}
