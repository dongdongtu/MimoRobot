package com.chance.mimorobot.statemachine;

/**
 * Created by Shao Shizhang on 2018/5/29.
 */

public class RobotStatus {

    private static RobotStatus instance = null;

    public static RobotStatus getInstance() {
        if (instance == null) {
            instance = new RobotStatus();
        }
        return instance;
    }

    private boolean isSleeping;
    private boolean isCharging;
    private boolean isSelfIntroducing;
    private boolean isAIUIPlayerReady;
    private boolean isPlayingMusic;
    private boolean isMusicActivated;
    private boolean isDownloading = false;
    private boolean isDrawerOpen = false;

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public boolean isSelfIntroducing() {
        return isSelfIntroducing;
    }

    public void setSelfIntroducing(boolean selfIntroducing) {
        isSelfIntroducing = selfIntroducing;
    }

    public boolean isSleeping() {
        return isSleeping;
    }

    public void setSleeping(boolean sleeping) {
        isSleeping = sleeping;
    }

    public boolean isAIUIPlayerReady() {
        return isAIUIPlayerReady;
    }

    public void setAIUIPlayerReady(boolean AIUIPlayerReady) {
        isAIUIPlayerReady = AIUIPlayerReady;
    }

    public boolean isPlayingMusic() {
        return isPlayingMusic;
    }

    public void setPlayingMusic(boolean playingMusic) {
        isPlayingMusic = playingMusic;
    }

    public boolean isMusicActivated() {
        return isMusicActivated;
    }

    public void setMusicActivated(boolean musicActivated) {
        isMusicActivated = musicActivated;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }

    public boolean isDrawerOpen() {
        return isDrawerOpen;
    }

    public void setDrawerOpen(boolean drawerOpen) {
        isDrawerOpen = drawerOpen;
    }
}
