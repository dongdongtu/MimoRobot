package com.chance.mimorobot.model;

import java.io.Serializable;

public class InitModel implements Serializable {

    /**
     * RobotNo : sample string 1
     * code : 2
     * res : true
     * msg : sample string 4
     */

    private String RobotNo;
    private int code;
    private boolean res;
    private String msg;

    private int Mapid;

    public int getMapid() {
        return Mapid;
    }

    public void setMapid(int mapid) {
        Mapid = mapid;
    }

    public String getRobotNo() {
        return RobotNo;
    }

    public void setRobotNo(String RobotNo) {
        this.RobotNo = RobotNo;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
