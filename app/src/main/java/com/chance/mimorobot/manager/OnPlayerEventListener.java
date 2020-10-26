package com.chance.mimorobot.manager;

import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.model.SongPlayInfo;


/**
 * 播放进度监听器
 */
public interface OnPlayerEventListener {

    /**
     * 切换歌曲
     */
    void onChange(AiuiResultEntity.DataBean.ResultBean song);


    void onChange(SongPlayInfo songPlayInfo);
    /**
     * 继续播放
     */
    void onPlayerStart();

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 更新进度
     */
    void onPublish(int progress);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);
}
