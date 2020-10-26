package com.chance.mimorobot.model;

import java.io.Serializable;

public class InitRequest implements Serializable {

    public InitRequest(String pid, String androidVersion, String singleClipVersion, String robotType) {
        Pid = pid;
        AndroidVersion = androidVersion;
        SingleClipVersion = singleClipVersion;
        RobotType = robotType;
    }

    /**
     * Pid : sample string 1
     * AndroidVersion : sample string 2
     * SingleClipVersion : sample string 3
     * RobotType : sample string 4
     */

    private String Pid;
    private String AndroidVersion;
    private String SingleClipVersion;
    private String RobotType;

    public String getPid() {
        return Pid;
    }

    public void setPid(String Pid) {
        this.Pid = Pid;
    }

    public String getAndroidVersion() {
        return AndroidVersion;
    }

    public void setAndroidVersion(String AndroidVersion) {
        this.AndroidVersion = AndroidVersion;
    }

    public String getSingleClipVersion() {
        return SingleClipVersion;
    }

    public void setSingleClipVersion(String SingleClipVersion) {
        this.SingleClipVersion = SingleClipVersion;
    }

    public String getRobotType() {
        return RobotType;
    }

    public void setRobotType(String RobotType) {
        this.RobotType = RobotType;
    }
}
