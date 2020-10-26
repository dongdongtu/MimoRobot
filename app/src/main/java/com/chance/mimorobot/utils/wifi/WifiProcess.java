package com.chance.mimorobot.utils.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;


import com.chance.mimorobot.utils.wifi.entities.WifiEntity;
import com.chance.mimorobot.utils.wifi.enums.WifiSecutityEnum;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




public class WifiProcess implements WifiProcessInterface {

    // 定义WifiManager对象
    private WifiManager mWifiManager;
    /**
     * Android 对WiFi电源管理的代码主要在WifiService.java中。如果应用程序想在屏幕被关掉后继续使用WiFi则可以调用 acquireWifiLock来锁住WiFi，
     * 该操作会阻止WiFi进入睡眠状态。当应用程序不再使用WiFi时需要调用 releaseWifiLock来释放WiFi。之后WiFi可以进入睡眠状态以节省电源。
     */
    private WifiLock mWifiLock;
    private Context mContext;
    private boolean disableOthers;
    private WifiInfoTransform wifiInfoTransform;
    private List<WifiEntity> wifiEntityList;
    private Timer mTimer;


    /**
     * 初始化各种对象
     *
     * @param context
     */
    @Override
    public void init(Context context) {
        if (mContext != null)   //只初始化一次
            return;
        mContext = context;
        disableOthers = true;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfoTransform = new WifiInfoTransform();
        wifiInfoTransform.init(mWifiManager, this);
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        cancelRecyclerScan();
    }


    /**
     * 打开wifi
     */
    @Override
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭wifi
     */
    @Override
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 判断wifi是否可用
     *
     * @return
     */
    @Override
    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 检查当前WIFI状态
     *
     * @return WIFI_STATE_DISABLING    0   WIFI网卡正在关闭
     * WIFI_STATE_DISABLED     1   WIFI网卡不可用
     * WIFI_STATE_ENABLING     2   WIFI网卡正在打开
     * WIFI_STATE_ENABLED      3   WIFI网卡可用
     * WIFI_STATE_UNKNOWN      4   WIFI网卡状态不可知
     */
    @Override
    public int getCheckState() {
        return mWifiManager.getWifiState();
    }

    /**
     * 锁定WifiLock,能够阻止wifi进入睡眠状态，使wifi一直处于活跃状态
     *
     * @param tag
     */
    @Override
    public void acquireWifiLock(String tag) {
        if (mWifiLock == null)
            mWifiLock = mWifiManager.createWifiLock(tag);
        mWifiLock.acquire();
    }

    /**
     * 解锁WifiLock
     */
    @Override
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

    /**
     * 循环扫描
     */
    @Override
    public void recyclerScan(long peroidTime) {
        peroidTime = peroidTime < 10 ? peroidTime * 1000 : peroidTime;
        peroidTime = peroidTime < 500 ? 500 : peroidTime;
        peroidTime = peroidTime > 10000 ? 10000 : peroidTime;
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startScan();
                }
            }, 0, peroidTime);
        }
    }

    /**
     * 取消循环扫描
     */
    @Override
    public void cancelRecyclerScan() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 扫描周围wifi信息  synchronized  表示不能同时调用startScan方法
     *
     * @return 1. 当没有打开wifi时，执行的结果：
     * mWifiList = []
     * mWifiList.size() = 0
     * 2. 当打开wifi ,不管有没有连接时，执行的结果：
     * mWifiList = 很多wifi 列表信息,可以得到搜索到的SSID，BSSID等
     * mWifiList.size() = 所扫到的wifi 数量
     */
    @Override
    public synchronized void startScan() {
        boolean scan = mWifiManager.startScan();
        if (scan) {
            List<ScanResult> wifiList = getWifiList();// 得到扫描结果
            wifiEntityList = wifiInfoTransform.get(wifiList);
        }
    }


    /**
     * 根据netId连接已保存的wifi
     *
     * @param netId
     */
    @Override
    public void connectWifi(int netId) {
        mWifiManager.enableNetwork(netId, disableOthers);
    }

    /**
     * 连接知道账号密码和加密方式的wifi
     *
     * @param SSID
     * @param password
     * @param type
     */
    @Override
    public int connectWifi(String SSID, String password, WifiSecutityEnum type) {
        WifiConfiguration wifiConfiguration = createWifiInfo(SSID, password, type);
        int netId = mWifiManager.addNetwork(wifiConfiguration);
        if (netId != -1) {
            mWifiManager.enableNetwork(netId, disableOthers);
        }
        return netId;
    }

    /**
     * 断开当前wifi
     */
    @Override
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    /**
     * 根据netId删除保存的wifi
     *
     * @param netId
     */
    @Override
    public void removeWifi(int netId) {
        try {
            Method forget = mWifiManager.getClass().getDeclaredMethod("forget", int.class, Class.forName("android.net.wifi.WifiManager$ActionListener"));
            if (forget != null) {
                forget.setAccessible(true);
                forget.invoke(mWifiManager, netId, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断指定的wifi是否保存
     *
     * @param SSID
     * @return
     */
    @Override
    public WifiConfiguration isWifiSave(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                    return existingConfig;
                }
            }
        }
        return null;
    }


    /**
     * 是否处于wifi连接的状态
     *
     * @return
     */
    @Override
    public boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo == null) return false;

        return wifiNetworkInfo.isConnected();
    }

    /**
     * 判断是否与指定wifi连接
     *
     * @param SSID
     * @return
     */
    @Override
    public boolean isGivenWifiConnect(String SSID) {
        return isWifiConnected() && wifiInfoTransform.getCurentWifiSSID().equals(SSID);
    }

    /**
     * 得到扫描后的全部wifiList
     *
     * @return
     */
    @Override
    public List<ScanResult> getWifiList() {
        return mWifiManager.getScanResults();
    }

    /**
     * 得到已经保存过的wifi配置信息
     *
     * @return
     */
    @Override
    public List<WifiConfiguration> getConfiguredNetworks() {
        return mWifiManager.getConfiguredNetworks();
    }

    /**
     * 得到自己定义的wifi信息实体类
     *
     * @return
     */
    @Override
    public List<WifiEntity> getWifiEntityList() {
        return wifiEntityList;
    }

    /**
     * 得到WifiInfoTransform实例
     *
     * @return
     */
    @Override
    public WifiInfoTransform getWifiInfoTransform() {
        return wifiInfoTransform;
    }


    /**
     * 创建有密码的wifi的config
     *
     * @param SSID
     * @param password
     * @param type
     * @return
     */
    private WifiConfiguration createWifiInfo(String SSID, String password, WifiSecutityEnum type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        // WifiConfiguration tempConfig = this.isWifiSave(SSID);
        WifiConfiguration tempConfig = isWifiSave(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        switch (type) {
            case NOON:
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
            case WEP:
                config.hiddenSSID = true;
                config.wepKeys[0] = "\"" + password + "\"";
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;
            case WPA_AND_WPA2:
                config.preSharedKey = "\"" + password + "\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.status = WifiConfiguration.Status.ENABLED;
                break;
        }
        return config;
    }
}
