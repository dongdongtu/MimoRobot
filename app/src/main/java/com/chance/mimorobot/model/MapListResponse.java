package com.chance.mimorobot.model;

import java.io.Serializable;
import java.util.List;

public class MapListResponse implements Serializable {

    /**
     * robotMapList : [{"id":1,"Robotid":1,"MapUrl":"sample string 2","MapName":"sample string 3","Des":"sample string 4","Cdate":"2020-06-20 12:03:26"},{"id":1,"Robotid":1,"MapUrl":"sample string 2","MapName":"sample string 3","Des":"sample string 4","Cdate":"2020-06-20 12:03:26"}]
     * code : 1
     * res : true
     * msg : sample string 3
     */

    private int code;
    private boolean res;
    private String msg;
    private List<RobotMap> MapList;

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

    public List<RobotMap> getMapList() {
        return MapList;
    }

    public void setMapList(List<RobotMap> robotMapList) {
        this.MapList = robotMapList;
    }


}
