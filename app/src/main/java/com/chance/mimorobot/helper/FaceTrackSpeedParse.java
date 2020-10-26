package com.chance.mimorobot.helper;

/**
 * Created by Administrator on 2018/5/18.
 *
 */

public class FaceTrackSpeedParse {
    private static final float CENTER = 0.5f;

    private static final float X_SLOT = 0.1f;
    private static final float Y_SLOT = 0.1f;

    private static final float X_SPEED = 0.5f;
    private static final float Y_SPEED = 0.5f;
    private static final float Z_SPEED = 0.5f;

    private static final float WIDTH = 0.16f;
    private static final float WIDTH_SLOT = 0.05f;
    /**
     * 根据人脸x方向的位置确定x方向的速度并返回  左转右转
     *
     * @param x
     * @return
     */
    public static float getVx(float x) {
        float dix = x - CENTER;
        if (dix > X_SLOT) {
            return X_SPEED;
        }
        if (dix < -X_SLOT) {
            return -X_SPEED;
        }
        return 0;
    }

    /**
     * 根据人脸y方向的位置确定y方向的速度并返回  上下俯仰
     *
     * @param y
     * @return
     */
    public static float getVy(float y) {
        float dif = y - CENTER;
        if (dif > Y_SLOT) {
            return Y_SPEED;
        }
        if (dif < -Y_SLOT) {
            return -Y_SPEED;
        }
        return 0;
    }

    /**
     * 计算机器人的前进后退速度  前进后退
     *
     * @param width
     * @return
     */
    public static float getVz(float width) {
        float dif = width - WIDTH;
        if (dif > WIDTH_SLOT) {//说明人脸占屏幕很大  后退
            return Z_SPEED;
        }
        if (dif < -WIDTH_SLOT) {//说明人脸占屏幕小  前进
            return -Z_SPEED;
        }
        return 0;
    }
}
