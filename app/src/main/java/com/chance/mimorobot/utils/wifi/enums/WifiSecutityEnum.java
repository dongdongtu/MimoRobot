package com.chance.mimorobot.utils.wifi.enums;


import com.chance.mimorobot.constant.Constant;

public enum WifiSecutityEnum {
    NOON,
    WPA2,
    WPA_AND_WPA2,
    WEP;
    public static String getWifiSecutity(WifiSecutityEnum wifiSecutityEnum){
        switch (wifiSecutityEnum) {
            case NOON:
                return "";
            case WPA2:
                return Constant.WPA2_MODE;
            case WPA_AND_WPA2:
                return Constant.WPA_MODE + "/" + Constant.WPA2_MODE;
            default:
                return "";
        }
    }
}
