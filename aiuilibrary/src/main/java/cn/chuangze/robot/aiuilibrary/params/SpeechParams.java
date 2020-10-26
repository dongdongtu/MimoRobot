package cn.chuangze.robot.aiuilibrary.params;

import java.util.HashMap;
import java.util.Map;

public class SpeechParams {

    private HashMap<String, String> params = new HashMap<>();

    /**
     * 声音最小
     */
    private final int minVolume = 10;
    /**
     * 声音最大
     */
    private final int maxVolume = 100;

    /**
     * 增大减小量
     */
    private final int offsetVolume = 20;
    /**
     * 当前音量大小
     */
    private int currentVolume = 50;

    /**
     * 合成速度
     */
    private int speed = 50;

    /**
     * 合成音调
     */
    private int pitch = 50;

    /**
     * 合成发言人
     */
    private String vcn = "xiaoyan";

    public SpeechParams() {
        setParams();
    }

    public void init(String vcn, int speed, int pitch, int volume) {
        this.vcn = vcn;
        this.speed = speed;
        this.pitch = pitch;
        this.currentVolume = volume;

        setParams();
    }

    private void setParams() {
        params.put("vcn", vcn);
        params.put("speed", String.valueOf(speed));
        params.put("pitch", String.valueOf(pitch));
        params.put("volume", String.valueOf(currentVolume));
        if ("x_chongchong".equalsIgnoreCase(vcn)) {
            params.put("ent", "x_tts");
        }

        // String tag = "@" + System.currentTimeMillis();
        // params.append(",tag=" + tag);  //合成tag，方便追踪合成结束，暂未实现
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            stringBuilder.append(entry.getKey() + "=" + entry.getValue() + ",");
        }
        String tag = "@" + System.currentTimeMillis();
        stringBuilder.append("tag=" + tag);  //合成tag，方便追踪合成结束，暂未实现

        return stringBuilder.toString();
    }

    /**
     * 调整声音
     * 重新唤醒生效
     *
     * @param plus true 声音大一点
     *             false 声音小一点
     */
    public void volumePlus(boolean plus) {
        if (plus) {
            currentVolume += offsetVolume;
        } else {
            currentVolume -= offsetVolume;
        }
        setVolume();
    }

    /**
     * 调整声音
     * 重新唤醒生效
     *
     * @param max true 声最大
     *            false 声最小
     */
    public void volume(boolean max) {
        if (max) {
            currentVolume = maxVolume;
        } else {
            currentVolume = minVolume;
        }
        setVolume();
    }

    private void setVolume() {
        currentVolume = currentVolume > maxVolume ? maxVolume : currentVolume;
        currentVolume = currentVolume < minVolume ? minVolume : currentVolume;

        params.put("volume", String.valueOf(currentVolume));
    }

}
