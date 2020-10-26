package com.chance.mimorobot.model;

import java.io.Serializable;
import java.util.List;

public class MapPointResponse implements Serializable {

    /**
     * code : 1
     * res : true
     * msg : sample string 3
     */

    private int code;
    private boolean res;
    private String msg;

    private List<MapPoint> Points;


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

    public List<MapPoint> getPoints() {
        return Points;
    }

    public void setPoints(List<MapPoint> Points) {
        this.Points = Points;
    }

}
