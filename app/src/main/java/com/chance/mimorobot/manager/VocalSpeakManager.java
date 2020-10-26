package com.chance.mimorobot.manager;

import android.content.Context;

import cn.chuangze.robot.aiuilibrary.AIUIWrapper;

public class VocalSpeakManager implements VocalSpeakInterface {

    private AIUIWrapper aiuiWrapper;
    private Context mContext;

    @Override
    public void onCreate(Context mContext) {
        this.mContext = mContext;
        aiuiWrapper=AIUIWrapper.getInstance(mContext);
    }

    public static VocalSpeakInterface getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static VocalSpeakInterface INSTANCE = new VocalSpeakManager();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void activateSpeak(String text) {
        aiuiWrapper.startTTS(text,null);
    }

    @Override
    public void activateSpeak(String text, boolean changeFace) {
        aiuiWrapper.startTTS(text,null);
    }

    @Override
    public void forceSpeak(String text, boolean changeFace, boolean interrupt) {

    }

    @Override
    public void forceSpeak(String text) {

    }

    @Override
    public void forceSpeak(String text, boolean changeFace) {

    }

    @Override
    public void activateSpeakCallback(String text, Runnable onSpeakListener) {
        aiuiWrapper.startTTS(text,onSpeakListener);
    }

    @Override
    public boolean isSpeaking() {
        return aiuiWrapper.isTTS();
    }

    @Override
    public void stopSpeaking() {
        aiuiWrapper.stopTTS();
    }

    @Override
    public void resumeTTS() {
        aiuiWrapper.resumeTTS();
    }

    @Override
    public void pauseTTS() {
        aiuiWrapper.pauseTTS();
    }

    @Override
    public void setSpeechSpeed(int speed) {
        aiuiWrapper.setSpeed(speed);
    }

    @Override
    public int getCurrentSpeechSpeed() {
        return 0;
    }

    @Override
    public void sleep() {
        aiuiWrapper.sleep();
    }
}
