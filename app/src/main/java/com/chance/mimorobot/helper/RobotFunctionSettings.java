package com.chance.mimorobot.helper;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.chance.mimorobot.manager.VocalSpeakInterface;
import com.chance.mimorobot.manager.VocalSpeakManager;


/**
 * Created by Shao Shizhang on 2018/6/13.
 */

public class RobotFunctionSettings {

    private final String TAG = RobotFunctionSettings.class.getSimpleName();

    private static RobotFunctionSettings instance = null;

    public static RobotFunctionSettings getInstance() {
        if (instance == null) {
            instance = new RobotFunctionSettings();
        }
        return instance;
    }


    private Context mContext;

    /**
     * 初始化设置
     *
     * @param context 初始化所用Context
     */
    public void initSettings(Context context) {
        mContext = context;
    }

    public void volumeMute() {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            Log.i("RobotFunctionSettings", "volumemute =0");
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
        }
    }

    public void volumeMax() {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            Log.i("RobotFunctionSettings", "volumemax =" + am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
        }
    }

    public void volumeMin() {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            Log.i("RobotFunctionSettings", "volumemin =" + (int) (am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 0.15));
            am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 0.15), AudioManager.FLAG_SHOW_UI);
        }
    }

    /**
     * 调低音量
     */
    public void volumeDown() {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = 0;//得到音乐模式的当前值
        if (am != null) {
            streamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//得到音乐模式的最大值
            int deltVolume = (int) (maxVolume * 0.15);
            streamVolume = streamVolume - deltVolume;
            Log.i("RobotFunctionSettings", "volumedown =" + streamVolume);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, streamVolume, AudioManager.FLAG_SHOW_UI);
        }
    }

    /**
     * 调高音量
     */
    public void volumeUp() {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            int streamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);//得到音乐模式的当前值
            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//得到音乐模式的最大值
            int deltVolume = (int) (maxVolume * 0.15);
            streamVolume = streamVolume + deltVolume;
            Log.i("RobotFunctionSettings", "volumeup =" + streamVolume);
            am.setStreamVolume(AudioManager.STREAM_MUSIC, streamVolume, AudioManager.FLAG_SHOW_UI);
        }
    }

    public void setVolume(float volumeMultiple) {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//得到音乐模式的最大值
            am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVolume * volumeMultiple), AudioManager.FLAG_SHOW_UI);
        }
    }


    /**
     * 关闭音量
     */
    public void volumeOff() {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI);
        }
    }

    /**
     * 打开音量
     */
    public void volumeOn() {
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//得到音乐模式的当前值
            am.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume / 2, AudioManager.FLAG_SHOW_UI);
        }
    }

    public int speechSpeedUp() {
        VocalSpeakInterface vocalSpeakInterface = VocalSpeakManager.getInstance();
        int currentSpeed = vocalSpeakInterface.getCurrentSpeechSpeed();
        int speed = currentSpeed + 5;
        if (speed > 100) {
            speed = 100;
        }
        vocalSpeakInterface.setSpeechSpeed(speed);
        return speed;
    }

    public int speechSpeedDown() {
        VocalSpeakInterface vocalSpeakInterface = VocalSpeakManager.getInstance();
        int currentSpeed = vocalSpeakInterface.getCurrentSpeechSpeed();
        int speed = currentSpeed - 5;
        if (speed < 10) {
            speed = 10;
        }
        vocalSpeakInterface.setSpeechSpeed(speed);
        return speed;
    }

}
