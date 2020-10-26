package com.chance.mimorobot.constant;


import android.os.Environment;

import java.io.File;

/**
 * Created by Shao Shizhang on 2018/2/9.
 */

public class Constant {


    public static String PHOTO_PATH_DIRECTORY = Environment.getExternalStorageDirectory().getPath()
            + File.separator + Environment.DIRECTORY_DCIM + File.separator
            + "Robot/";

    public static String PHOTO_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + Environment.DIRECTORY_DCIM + File.separator
            + "Robot/jpeg";

    public static String GET_PHOTO_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + Environment.DIRECTORY_DCIM + File.separator
            + "Robot/jpeg";


    public static final String LOG_TAG = "czRobot";

    //获取wifi部分
    public static final String KEY_WPA_MODE = "WPA";
    public static final String KEY_WPA2_MODE = "WPA2-PSK";
    public static final String WPA_MODE = "WPA";
    public static final String WPA2_MODE = "WPA2 PSK";

    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

    public static final String CENTIGRADE = "℃";


    public static final String SHARED_PREFERENCE_SETTING = "SHARED_SETTING";
    public static final String SHARED_PREFERENCE_SETTING_DISTANCE_WARNING = "DISTANCE_WARNING";

    public static final int DEFAULT_WARNING_DISTANCE = 25;  //默认息屏距离
    public static final int MIN_WARNING_DISTANCE = 20;      //最小息屏距离
    public static final int WARNING_DISTANCE_STEP = 5;      //息屏距离调节步长
    public static final int MAX_WARNING_DISTANCE_SEEK_BAR = 8;//最大息屏距离，在调节条上的点数


    public static String ACTION_CHANGE_TIME_FORMAT = "ACTION_CHANGE_TIME_FORMAT";
    public static String ACTION_SHOW_STATUS_BAR = "SHOW_STATUS_BAR";
    public static String ACTION_HIDE_STATUS_BAR = "HIDE_STATUS_BAR";


    public static final String ACTION_BLUETOOTH_SEND_DATA = "ACTION_BLUETOOTH_SEND_DATA";
    public static final String EXTRAS_BLUETOOTH_SEND_DATA = "EXTRAS_BLUETOOTH_SEND_DATA";

    public static final String USER_FIRST_RUN_DATA = "user_first_run_data_test30";
    public static final String USER_FIRST_RUN_KEY = "user_first_run_key";

    public static final String KUGO_CONSTANTS_FILE_NAME = "kugo_constants_file_name";
    public static final String KEY_KUGO_LOGIN_TOKEN = "key_kugo_login_token";
    public static final String KEY_KUGO_LOGIN_UID = "key_kugo_login_uid";
    public static final int REQUEST_CODE_KUGO_LOGIN = 165;

    public static final String CZROBOT_LOG_FILE_NAME = "czrobot_log_file_name";
    public static final String KEY_CZROBOT_AIUI_LOG = "key_czrobot_aiui_log";
    public static final String ACTION_SEND_CZROBOT_AIUI_LOG = "action_send_czrobot_aiui_log";

    public final static String ACTION_PNG_CODE = "ACTION_PNG_CODE";
    public final static String ACTION_PNG_EXTRA = "ACTION_PNG_EXTRA";

    public final static String ACTION_START_CODE = "ACTION_START_CODE";
    public final static String ACTION_START_EXTRA = "ACTION_START_EXTRA";




    public final static String ACTION_DOWNLOAD_CODE = "ACTION_DOWNLOAD_CODE";
    public final static String ACTION_DOWNLOAD_EXTRA = "ACTION_DOWNLOAD_EXTRA";



    //    public final static String MQTT_IP="192.168.31.244";
    public final static String MQTT_IP="47.110.149.187";
    public final static int MQTT_PORT=10032;

    public final static String APP_ID="2ek7HQoLiqicBj1ajcpo6NjzfAukbGgSvqbyuBZLKuw1";
    public final static String SDK_KEY="8uGCuaAaXHnPtqn1d4M9hzoGCmkQuh9mXBDpSeV2uZYr";

}
