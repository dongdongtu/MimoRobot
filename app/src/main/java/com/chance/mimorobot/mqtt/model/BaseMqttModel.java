package com.chance.mimorobot.mqtt.model;

import java.io.Serializable;

public class BaseMqttModel<T> implements Serializable {

    /**
     * code : 1
     * from : 12314
     * to : platform
     * data : {"RobotStatusCode":0,"TobotStatusText":"停止","RobotElectricQuantity":"30","RobotSpeed":"1","RobotSpeedUnit":"m/s"}
     * md5 :
     * CodeName : hreatbeat
     */

    private int code;
    private String from;
    private String to;
    private T data;
    private String md5;
    private String CodeName;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getCodeName() {
        return CodeName;
    }

    public void setCodeName(String CodeName) {
        this.CodeName = CodeName;
    }


}
