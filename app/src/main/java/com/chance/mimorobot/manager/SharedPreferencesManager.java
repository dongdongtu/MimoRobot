package com.chance.mimorobot.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.arcsoft.face.enums.DetectFaceOrientPriority;

/**
 * @Description 本地存储对象管理器
 * @Author wj
 * @Time 2017/7/27 16:12
 */

public class SharedPreferencesManager {



    SharedPreferences sharedPreferences;

    private static SharedPreferencesManager sharedPreferencesManager;

    public static SharedPreferencesManager newInstance() {
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = new SharedPreferencesManager();
        }
        return sharedPreferencesManager;
    }

    public void initShare(Context context){
        sharedPreferences = context.getSharedPreferences("robot", Context.MODE_PRIVATE);
    }

    private SharedPreferencesManager() {
    }

    /**
     * 设置地图的初始化状态
     *
     * @param isInit
     */
    public void setInitMap(boolean isInit) {
        sharedPreferences.edit().putBoolean("isMapInit", isInit).commit();
    }

    /**
     * 获取SLAM地图的初始化状态
     *
     * @return
     */
    public boolean getInitMap() {
        return sharedPreferences.getBoolean("isMapInit", false);
    }


    /**
     * 设置首页图片地址
     *
     * @param path
     */
    public void setSplashPath(String path) {
        sharedPreferences.edit().putString("SplashPath", path).commit();
    }

    /**
     * 获取首页图片地址
     *
     * @return
     */
    public String getSplashPath() {
        return sharedPreferences.getString("SplashPath", "");
    }

    /**
     * 设置休息图片地址
     *
     * @param path
     */
    public void setRestPath(String path) {
        sharedPreferences.edit().putString("RestPath", path).commit();
    }

    /**
     * 获取休息页面图片地址
     *
     * @return
     */
    public String getRestPath() {
        return sharedPreferences.getString("RestPath", "");
    }

    public boolean isUploadTotalMils() {
        return sharedPreferences.getBoolean("isUploadTotalMils", false);
    }

    public void setUploadTotalMils(boolean flag) {
        sharedPreferences.edit().putBoolean("isUploadTotalMils", flag).commit();
    }

    /**
     * 获取保存的定位信息
     *
     * @return
     */
    public String getGps() {
        return sharedPreferences.getString("gps", "");
    }

    /**
     * 设置当前设备的定位信息
     *
     * @param gps
     */
    public void setGps(String gps) {
        sharedPreferences.edit().putString("gps", gps).commit();
    }



    /**
     * ��ȡ��������
     * @return
     */
    public  int getPID() {
        int mpid = sharedPreferences.getInt("PID", 22336);
        return mpid;
    }

    /**
     * ����PID
     * @return
     */
    public void savePID(int mpid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("PID", mpid);
        editor.commit();
    }

    /**
     * ��ȡVID
     * @return
     */
    public  int getVID() {
        int mvid = sharedPreferences.getInt("VID", 1155);
        return mvid;
    }

    /**
     * ����VID
     * @return
     */
    public  void saveVID(int mvid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("VID", mvid);
        editor.commit();
    }


    public void setFtOrient(DetectFaceOrientPriority ftOrient){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ftOrientPriority", ftOrient.name());
        editor.commit();
    }

    public DetectFaceOrientPriority getFtOrient() {
        return DetectFaceOrientPriority.valueOf(sharedPreferences.getString("ftOrientPriority", DetectFaceOrientPriority.ASF_OP_180_ONLY.name()));
    }

    public  void setTrackedFaceCount(Context context, int trackedFaceCount) {
        sharedPreferences.edit()
                .putInt("trackedFaceCount", trackedFaceCount)
                .commit();
    }

    public  int getTrackedFaceCount() {
        return sharedPreferences.getInt("trackedFaceCount", 0);
    }


    /**
     * 设置地图ID
     *
     * @param path
     */
    public void setMapID(int path) {
        sharedPreferences.edit().putInt("mapId", path).commit();
    }

    /**
     * 获取地图ID
     *
     * @return
     */
    public int getMapID() {
        return sharedPreferences.getInt("mapId",-1);
    }

    /**
     * 设置地图地址
     *
     * @param path
     */
    public void setMapPath(String path) {
        sharedPreferences.edit().putString("MapPath", path).commit();
    }

    /**
     * 获取地图地址
     *
     * @return
     */
    public String getMapPath() {
        return sharedPreferences.getString("MapPath","");
    }


    /**
     * 设置地图名称
     *
     * @param path
     */
    public void setMapName(String path) {
        sharedPreferences.edit().putString("MapName", path).commit();
    }

    /**
     * 获取地图名称
     *
     * @return
     */
    public String getMapName() {
        return sharedPreferences.getString("MapName","");
    }

}
