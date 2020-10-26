package com.chance.mimorobot.widget.neteasedisc.model;

import java.io.Serializable;

/**
 * Created by AchillesL on 2016/11/15.
 */

public class MusicData implements Serializable {
    /*音乐资源id*/
    private String mMusicRes;
    /*专辑图片id*/
    private String mMusicPicRes;
    /*音乐名称*/
    private String mMusicName;
    /*作者*/
    private String mMusicAuthor;

    public MusicData(String mMusicRes, String mMusicPicRes, String mMusicName, String mMusicAuthor) {
        this.mMusicRes = mMusicRes;
        this.mMusicPicRes = mMusicPicRes;
        this.mMusicName = mMusicName;
        this.mMusicAuthor = mMusicAuthor;
    }

    public String getMusicRes() {
        return mMusicRes;
    }

    public String getMusicPicRes() {
        return mMusicPicRes;
    }

    public String getMusicName() {
        return mMusicName;
    }

    public String getMusicAuthor() {
        return mMusicAuthor;
    }
}
