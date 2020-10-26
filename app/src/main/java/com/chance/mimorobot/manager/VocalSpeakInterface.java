package com.chance.mimorobot.manager;

import android.content.Context;

/**
 * Created by Shao Shizhang on 2018/4/23.
 */

public interface VocalSpeakInterface {

    void onCreate(Context mContext);

    void onDestroy();

    void activateSpeak(String text);

    void activateSpeak(String text, boolean changeFace);

    void forceSpeak(String text, boolean changeFace, boolean interrupt);

    void forceSpeak(String text);

    void forceSpeak(String text, boolean changeFace);

    void activateSpeakCallback(String text, Runnable onSpeakListener);


    boolean isSpeaking();

    void stopSpeaking();

    void resumeTTS();

    void pauseTTS();

    void setSpeechSpeed(int speed);

    int getCurrentSpeechSpeed();

    void sleep();
}
