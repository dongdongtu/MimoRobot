package com.chance.mimorobot.utils.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;


import com.chance.mimorobot.utils.wifi.entities.WifiEntity;
import com.chance.mimorobot.utils.wifi.enums.WifiSecutityEnum;

import java.util.List;


public interface WifiProcessInterface {
    /**
     * 初始化各种对象
     *
     * @param context
     */
    void init(Context context);

    /**
     * 释放资源
     */
    void release();

    /**
     * 打开wifi
     */
    void openWifi();

    /**
     * 关闭wifi
     */
    void closeWifi();

    /**
     * 判断wifi是否可用
     *
     * @return
     */
    boolean isWifiEnabled();

    /**
     * 检查当前WIFI状态
     *
     * @return
     */
    int getCheckState();

    /**
     * 锁定WifiLock,能够阻止wifi进入睡眠状态，使wifi一直处于活跃状态
     *
     * @param tag
     */
    void acquireWifiLock(String tag);

    /**
     * 解锁WifiLock
     */
    void releaseWifiLock();


    /**
     * 循环扫描
     */
    void recyclerScan(long peroidTime);

    /**
     * 取消循环扫描
     */
    void cancelRecyclerScan();

    /**
     * 扫描周围wifi信息
     */
    void startScan();



    /**
     * 根据netId连接已保存的wifi
     *
     * @param netId
     */
    void connectWifi(int netId);

    /**
     * 连接知道账号密码和加密方式的wifi
     *
     * @param SSID
     * @param password
     * @param type
     */
    int connectWifi(String SSID, String password, WifiSecutityEnum type);


    /**
     * 断开当前wifi
     */
    void disconnectWifi(int netId);
    /**
     * 根据netId删除保存的wifi
     *
     * @param netId
     */
    void removeWifi(int netId);

    /**
     * 判断指定的wifi是否保存
     *
     * @param SSID
     * @return
     */
    WifiConfiguration isWifiSave(String SSID);

    /**
     * 是否处于wifi连接的状态
     *
     * @return
     */
    boolean isWifiConnected();

    /**
     * 判断是否与指定wifi连接
     *
     * @param SSID
     * @return
     */
    boolean isGivenWifiConnect(String SSID);

    /**
     * 得到扫描后的全部wifiList
     *
     * @return
     */
    List<ScanResult> getWifiList();

    /**
     * 得到已经保存过的wifi配置信息
     *
     * @return
     */
    List<WifiConfiguration> getConfiguredNetworks();

    /**
     * 得到自己定义的wifi信息实体类
     *
     * @return
     */
    List<WifiEntity> getWifiEntityList();

    /**
     * 得到WifiInfoTransform实例
     * @return
     */
    WifiInfoTransform getWifiInfoTransform();
}
