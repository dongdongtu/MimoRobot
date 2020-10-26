package com.chance.mimorobot.mqtt.model;

import java.io.Serializable;

public class MqttUpdateModel implements Serializable {

    /**
     * Versionid : 1
     * VersionCode :
     * VersionName :
     * SingleChipUrl :
     * IsImposed : true
     * IsTest : true
     * Des :
     */

    private int Versionid;
    private String VersionCode;
    private String VersionName;
    private String SingleChipUrl;
    private boolean IsImposed;
    private boolean IsTest;
    private String Des;

    public int getVersionid() {
        return Versionid;
    }

    public void setVersionid(int Versionid) {
        this.Versionid = Versionid;
    }

    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String VersionCode) {
        this.VersionCode = VersionCode;
    }

    public String getVersionName() {
        return VersionName;
    }

    public void setVersionName(String VersionName) {
        this.VersionName = VersionName;
    }

    public String getSingleChipUrl() {
        return SingleChipUrl;
    }

    public void setSingleChipUrl(String SingleChipUrl) {
        this.SingleChipUrl = SingleChipUrl;
    }

    public boolean isIsImposed() {
        return IsImposed;
    }

    public void setIsImposed(boolean IsImposed) {
        this.IsImposed = IsImposed;
    }

    public boolean isIsTest() {
        return IsTest;
    }

    public void setIsTest(boolean IsTest) {
        this.IsTest = IsTest;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String Des) {
        this.Des = Des;
    }
}
