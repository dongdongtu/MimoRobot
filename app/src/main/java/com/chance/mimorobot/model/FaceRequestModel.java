package com.chance.mimorobot.model;

import java.io.Serializable;

public class FaceRequestModel implements Serializable {

    /**
     * faceid : 1
     * name : sample string 1
     * robotno : sample string 2
     * clockdate : 2020-09-22 20:48:04
     */

    private Long faceid;
    private String name;
    private String robotno;
    private String clockdate;

    public Long getFaceid() {
        return faceid;
    }

    public void setFaceid(Long faceid) {
        this.faceid = faceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRobotno() {
        return robotno;
    }

    public void setRobotno(String robotno) {
        this.robotno = robotno;
    }

    public String getClockdate() {
        return clockdate;
    }

    public void setClockdate(String clockdate) {
        this.clockdate = clockdate;
    }
}
