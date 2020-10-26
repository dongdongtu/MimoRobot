package com.chance.mimorobot.mqtt.model;

import com.slamtec.slamware.robot.Location;

import java.io.Serializable;

public class HeartBeat implements Serializable {

    /**
     * RobotStatusCode : 0
     * RobotStatusText : 停止
     * RobotElectricQuantity : 30
     * RobotSpeed : 1
     * RobotSpeedUnit : m/s
     * MapID : 1
     */

    private int RobotStatusCode;
    private String RobotStatusText;
    private String RobotElectricQuantity;
    private String RobotSpeed;
    private String RobotSpeedUnit;
    private int MapID;
    /**
     * PathID : 1
     * Point : {"x":10,"y":12,"z":10.22}
     */

    private int PathID;
    private Location Point;

    public int getRobotStatusCode() {
        return RobotStatusCode;
    }

    public void setRobotStatusCode(int RobotStatusCode) {
        this.RobotStatusCode = RobotStatusCode;
    }

    public String getRobotStatusText() {
        return RobotStatusText;
    }

    public void setRobotStatusText(String RobotStatusText) {
        this.RobotStatusText = RobotStatusText;
    }

    public String getRobotElectricQuantity() {
        return RobotElectricQuantity;
    }

    public void setRobotElectricQuantity(String RobotElectricQuantity) {
        this.RobotElectricQuantity = RobotElectricQuantity;
    }

    public String getRobotSpeed() {
        return RobotSpeed;
    }

    public void setRobotSpeed(String RobotSpeed) {
        this.RobotSpeed = RobotSpeed;
    }

    public String getRobotSpeedUnit() {
        return RobotSpeedUnit;
    }

    public void setRobotSpeedUnit(String RobotSpeedUnit) {
        this.RobotSpeedUnit = RobotSpeedUnit;
    }

    public int getMapID() {
        return MapID;
    }

    public void setMapID(int MapID) {
        this.MapID = MapID;
    }

    public int getPathID() {
        return PathID;
    }

    public void setPathID(int PathID) {
        this.PathID = PathID;
    }

    public Location getPoint() {
        return Point;
    }

    public void setPoint(Location Point) {
        this.Point = Point;
    }


}
