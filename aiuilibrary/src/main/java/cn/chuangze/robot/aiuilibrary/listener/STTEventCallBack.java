package cn.chuangze.robot.aiuilibrary.listener;

import com.iflytek.aiui.AIUIEvent;

public interface STTEventCallBack {


    /**
     * 语义识别结果
     *
     * @param result 语音结果
     */
    void onResultAIUI(String result);


    /**
     * 语义识别结果
     *
     * @param result 语音结果
     */
    void startCheck(String result);

    /**
     * 语音识别结果
     *
     * @param result 语音结果
     */
    void onResult(String result);

    /**
     * 出错事件
     *
     * @param arg1 错误码
     * @param arg2 错误描述信息
     */
    void onError(int arg1, String arg2);

    /**
     * 抛出休眠事件
     * 此时需要主动向串口发送休眠命令让麦克风阵列休息
     *
     * @arg1 0 => TYPE_AUTO（自动休眠，即交互超时）
     * 1 => TYPE_COMPEL (外部强制休眠，即发送CMD_RESET_WAKEUP)。
     */
    void onSleep(int arg1);

    /**
     * 返回麦克风声音
     *
     * @param db 声音大小
     */
    void onVolume(int db);

    /**
     * 某条CMD命令对应的返回事件
     *
     * @param arg1 对应的CMD命令
     * @param arg2 返回值，0表示成功
     * @param info 描述信息
     */
    void onCmdReturn(int arg1, int arg2, String info);

    /**
     * 服务状态事件
     *
     * @param arg1
     */
    void onState(int arg1);

    /**
     * 准备休眠事件
     * 当出现交互超时，服务会先抛出准备休眠事件，
     * 用户可在接收到该事件后的10s内继续交互。若10s内存在有效交互，则重新开始交互计时；若10s内不存在有效交互，则10s后进入休眠状态。
     */
    void onPreSleep();

    /**
     * 与服务端建立起连接事件
     *
     * @param uid
     */
    void onConnectServer(String uid);

    /**
     * 与服务端断开连接事件
     */
    void onDisconnectServer();

    /**
     * 唤醒
     */
    void onWakeUp(int angle);
}
