package com.chance.mimorobot.mqtt.model;

import java.io.Serializable;

public class ReturnModel implements Serializable {

    /**
     * ReturnCode : 1
     * ReturnMessage : 收到心跳
     * ServerTime : 2020-06-17 15:01:01
     */

    private int ReturnCode;
    private String ReturnMessage;
    private String ServerTime;
    private int Jobid;
    private int Versionid;

    public int getVersionid() {
        return Versionid;
    }

    public void setVersionid(int versionid) {
        Versionid = versionid;
    }

    public Long getFaceid() {
        return Faceid;
    }

    public void setFaceid(Long faceid) {
        Faceid = faceid;
    }

    private Long Faceid;

    public int getJobid() {
        return Jobid;
    }

    public void setJobid(int jobid) {
        Jobid = jobid;
    }

    public int getReturnCode() {
        return ReturnCode;
    }

    public void setReturnCode(int ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String ServerTime) {
        this.ServerTime = ServerTime;
    }
}
