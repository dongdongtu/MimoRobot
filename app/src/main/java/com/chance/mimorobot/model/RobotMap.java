package com.chance.mimorobot.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RobotMap implements Serializable {

    /**
     * id : 1
     * Robotid : 1
     * MapUrl : sample string 2
     * MapName : sample string 3
     * Des : sample string 4
     * Cdate : 2020-06-20 12:03:26
     */

    private int id;
    private int Robotid;
    private String MapUrl;
    private String MapName;
    private String Des;
    private String Cdate;
    /**
     * MapSourceUrl : sample string 3
     */

    private String MapSourceUrl;
    /**
     * MapWidth : 1
     * MapHeight : 1
     * MapOriginX : 1.1
     * MapOriginY : 1.1
     * ResolutionX : 1.1
     * ResolutionY : 1.1
     */

    private int MapWidth;
    private int MapHeight;
    /**
     * MapOriginX : 1.1
     * MapOriginY : 1.1
     * ResolutionX : 1.1
     * ResolutionY : 1.1
     */

    @SerializedName("MapOriginX")
    private double MapOriginXX;
    @SerializedName("MapOriginY")
    private double MapOriginYX;
    @SerializedName("ResolutionX")
    private double ResolutionXX;
    @SerializedName("ResolutionY")
    private double ResolutionYX;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRobotid() {
        return Robotid;
    }

    public void setRobotid(int Robotid) {
        this.Robotid = Robotid;
    }

    public String getMapUrl() {
        return MapUrl;
    }

    public void setMapUrl(String MapUrl) {
        this.MapUrl = MapUrl;
    }

    public String getMapName() {
        return MapName;
    }

    public void setMapName(String MapName) {
        this.MapName = MapName;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String Des) {
        this.Des = Des;
    }

    public String getCdate() {
        return Cdate;
    }

    public void setCdate(String Cdate) {
        this.Cdate = Cdate;
    }

    public String getMapSourceUrl() {
        return MapSourceUrl;
    }

    public void setMapSourceUrl(String MapSourceUrl) {
        this.MapSourceUrl = MapSourceUrl;
    }

    public int getMapWidth() {
        return MapWidth;
    }

    public void setMapWidth(int MapWidth) {
        this.MapWidth = MapWidth;
    }

    public int getMapHeight() {
        return MapHeight;
    }

    public void setMapHeight(int MapHeight) {
        this.MapHeight = MapHeight;
    }

    public double getMapOriginXX() {
        return MapOriginXX;
    }

    public void setMapOriginXX(double MapOriginXX) {
        this.MapOriginXX = MapOriginXX;
    }

    public double getMapOriginYX() {
        return MapOriginYX;
    }

    public void setMapOriginYX(double MapOriginYX) {
        this.MapOriginYX = MapOriginYX;
    }

    public double getResolutionXX() {
        return ResolutionXX;
    }

    public void setResolutionXX(double ResolutionXX) {
        this.ResolutionXX = ResolutionXX;
    }

    public double getResolutionYX() {
        return ResolutionYX;
    }

    public void setResolutionYX(double ResolutionYX) {
        this.ResolutionYX = ResolutionYX;
    }
}
