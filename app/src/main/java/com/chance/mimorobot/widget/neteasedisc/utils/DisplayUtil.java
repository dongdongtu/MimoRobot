package com.chance.mimorobot.widget.neteasedisc.utils;

import android.content.Context;

/**
 * Created by AchillesL on 2016/11/16.
 */

public class DisplayUtil {

    /*手柄起始角度*/
    public static final float ROTATION_INIT_NEEDLE = -30;

    /*截图屏幕宽高*/
    //1280 × 800
    private static final float BASE_SCREEN_WIDTH = (float) 1280.0;
    private static final float BASE_SCREEN_HEIGHT = (float) 750.0;

    /*唱针宽高、距离等比例*/
    public static final float SCALE_NEEDLE_WIDTH = (float) (176.0 / BASE_SCREEN_WIDTH);
    public static final float SCALE_NEEDLE_MARGIN_LEFT = (float) (400.0 / BASE_SCREEN_WIDTH);
    public static final float SCALE_NEEDLE_PIVOT_X = (float) (43.0 / BASE_SCREEN_WIDTH);
    public static final float SCALE_NEEDLE_PIVOT_Y = (float) (43.0 / BASE_SCREEN_WIDTH);
    public static final float SCALE_NEEDLE_HEIGHT = (float) (200.0 / BASE_SCREEN_HEIGHT);
    public static final float SCALE_NEEDLE_MARGIN_TOP = (float) (1 / BASE_SCREEN_HEIGHT);

    /*唱盘比例*/
    public static final float SCALE_DISC_SIZE = (float) (406.5 / BASE_SCREEN_WIDTH);
    public static final float SCALE_DISC_MARGIN_TOP = (float) (70 / BASE_SCREEN_HEIGHT);

    /*专辑图片比例*/
    public static final float SCALE_MUSIC_PIC_SIZE = (float) (266.5 / BASE_SCREEN_WIDTH);

    /*设备屏幕宽度*/
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /*设备屏幕高度*/
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
