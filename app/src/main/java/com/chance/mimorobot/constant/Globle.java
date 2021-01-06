package com.chance.mimorobot.constant;

import com.baidu.location.BDLocation;

import java.text.SimpleDateFormat;

public class Globle {

    /**
     * 机器人名字
     */
    public static String ROBOT_NAME = "安安";
    /**
     * 机器人ID
     */
    public static String robotId = "-1";
    /**
     * SD卡根目录
     */
    public static String SDCARD_PATH = "";
    /**
     * 程序根目录
     */
    public static String ROOT_PATH = "/Robot";
    /**
     * 程序临时文件目录
     */
    public static String TEMP_PATH = "/temp";
    /**
     * 程序人脸图像
     */
    public static String FACE_PATH = "/face";
    /**
     * 程序地图相关文件保存目录
     */
    public static String MAP_PATH = "/map";
    /**
     * 首页图片文件文件保存目录
     */
    public static String PIC_PATH = "/pic";
    /**
     * 下载文件保存目录
     */
    public static String DOWNLOAD_PATH = "/download";

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    /**
     * 语音功能是否可用，包括唤醒 识别 休息等功能
     */
    public static boolean isSpeechEnable = true;
    public static String musicPicRes = "";

    public static int ONE_MINUTE = 60 * 1000;
    /**
     * 当前定位的信息
     */
    public static BDLocation location = null;


    public static  int charge;

    /**
     * 自动充电阈值设置的偏移量
     */
    public static final int AUTO_CHARGE_OFFSET = 20;
}
