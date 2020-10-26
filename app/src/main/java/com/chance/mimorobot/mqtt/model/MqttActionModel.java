package com.chance.mimorobot.mqtt.model;

import com.slamtec.slamware.robot.Location;

import java.io.Serializable;

public class MqttActionModel implements Serializable {

    /**
     * mapid : 1
     * pathid : 1
     * point : {"x":123,"y":231,"z":123.41}
     * arm : left
     * rgb :
     * face : 高兴
     */

    private int mapid;
    private int pathid;
    private Location point;
    private String arm;
    private String rgb;
    private String face;

    public int getMapid() {
        return mapid;
    }

    public void setMapid(int mapid) {
        this.mapid = mapid;
    }

    public int getPathid() {
        return pathid;
    }

    public void setPathid(int pathid) {
        this.pathid = pathid;
    }

    public Location getPoint() {
        return point;
    }

    public void setPoint(Location point) {
        this.point = point;
    }

    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

}
