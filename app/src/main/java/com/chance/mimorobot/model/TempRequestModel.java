package com.chance.mimorobot.model;

import java.io.Serializable;

public class TempRequestModel implements Serializable {

    /**
     * Temperature : 1.1
     * RobotNo : sample string 1
     * Img : sample string 2
     */

    private double Temperature;
    private String RobotNo;
    private String Img;

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double Temperature) {
        this.Temperature = Temperature;
    }

    public String getRobotNo() {
        return RobotNo;
    }

    public void setRobotNo(String RobotNo) {
        this.RobotNo = RobotNo;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String Img) {
        this.Img = Img;
    }
}
