package com.chance.mimorobot.model;

import java.io.Serializable;

public class MapPoint implements Serializable {



    /**
     * mapid : 1
     * pointid : 3
     * x : 1.0487128
     * y : -0.70623183
     * z : 0.0
     */
    private String PointName;
    private String pointid;
    private float x;
    private float y;
    private float z;
    /**
     * id : 1
     * Mapid : 1
     * x : 1.1
     * y : 1.1
     * z : 1.1
     * Cdate : 2020-09-21 19:21:51
     * Des : sample string 3
     */

    private int id;
    private int Mapid;
    private String Cdate;
    private String Des;

    public String getPointName() {
        return PointName;
    }

    public void setPointName(String pointName) {
        PointName = pointName;
    }

    public String getPointid() {
        return pointid;
    }

    public void setPointid(String pointid) {
        this.pointid = pointid;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMapid() {
        return Mapid;
    }

    public void setMapid(int Mapid) {
        this.Mapid = Mapid;
    }

    public String getCdate() {
        return Cdate;
    }

    public void setCdate(String Cdate) {
        this.Cdate = Cdate;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String Des) {
        this.Des = Des;
    }
}
