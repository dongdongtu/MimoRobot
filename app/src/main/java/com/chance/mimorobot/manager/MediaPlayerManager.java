package com.chance.mimorobot.manager;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/5/14.
 * MediaPlayer的管理类
 */

public class MediaPlayerManager {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private volatile boolean isPrepare;
    private final static long DELAY = 5000;
    private ObservableEmitter<Boolean> emitter;
    private Disposable disposable;


    public static MediaPlayerManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private final static class InstanceHolder {
        private static MediaPlayerManager INSTANCE = new MediaPlayerManager();
    }

    /**
     * 播放url资源
     *
     * @param url 资源
     * @return Observable
     */
    public Observable<Boolean> playUrlFeedback(final String url) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                emitter = e;
                startTimer();
                doPlayUrl(url);
            }
        });
    }

    public Observable<Boolean> playUrlFeedback(final String url, OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                emitter = e;
                startTimer();
                doPlayUrl(url);
            }
        });
    }

    /**
     * 播放url资源
     *
     * @param url 资源
     */
    public void playUrl(final String url) {
        playUrlFeedback(url).subscribeOn(Schedulers.computation()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
            }
        });

    }

    /**
     * 播放url资源的具体实现
     *
     * @param url 资源
     */
    private void doPlayUrl(String url) {
        if (isPlaying()) {
            stopPlay();
            mediaPlayer.reset();
        }
        if (isPrepare) {
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {

                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    isPrepare = false;
                    finishTimer();
                    emitter.onNext(true);
                    emitter.onComplete();
                }
            });
            mediaPlayer.setOnCompletionListener(
                    new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.reset();
                            if (onCompleteListener != null) {
                                onCompleteListener.onComplete();
                            }
                        }
                    });
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepareAsync(); // prepare自动播放
            isPrepare = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if (isPlaying()) {
            mediaPlayer.reset();
            mediaPlayer.stop();
        }
    }

    public void setOnCompleteListener(OnCompleteListener listener) {
        this.onCompleteListener = listener;
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * 设置TimeOut
     */
    private void startTimer() {
        finishTimer();
        if (disposable == null) {
            disposable = Observable.timer(DELAY, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            emitter.onNext(false);
                            emitter.onComplete();
                        }
                    });
        }
    }

    /**
     * 销毁TimeOut
     */
    private void finishTimer() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        disposable = null;
    }

    public interface OnCompleteListener {
        void onComplete();
    }

    private OnCompleteListener onCompleteListener;
}
