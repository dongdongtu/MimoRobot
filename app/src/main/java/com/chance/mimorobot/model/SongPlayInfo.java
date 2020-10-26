package com.chance.mimorobot.model;

import java.io.Serializable;

/**
 * 歌曲播放必需的信息。
 *
 * @author <a href="http://www.xfyun.cn">讯飞开放平台</a>
 * @date 2016年7月11日 下午4:19:30
 */
public class SongPlayInfo implements Serializable {

    /**
     * 歌曲名字
     */
    private String songName = "";

    /**
     * 歌手名字
     */
    private String singerName = "";

    /**
     * 网络路径
     */
    private String playUrl = "";

    /**
     * 发布时间？？
     */
    private long publishtime;

    /**
     * 封面路径
     */
    private String coverPath;

    private String answerText;

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public long getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(long publishtime) {
        this.publishtime = publishtime;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
