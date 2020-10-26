package com.chance.mimorobot.model;

import java.io.Serializable;

public class MapPointRequest implements Serializable {

    public MapPointRequest(String robotNo, int mapid, MapPoint point) {
        RobotNo = robotNo;
        Mapid = mapid;
        Point = point;
    }

    public MapPointRequest() {
    }

    /**
     * RobotNo : sample string 1
     * Mapid : 1
     * Points : [{"PointX":"sample string 1","PointY":"sample string 2","PointZ":"sample string 3","PointName":"sample string 4"},{"PointX":"sample string 1","PointY":"sample string 2","PointZ":"sample string 3","PointName":"sample string 4"}]
     */

    private String RobotNo;
    private int Mapid;
    private MapPoint Point;

    public String getRobotNo() {
        return RobotNo;
    }

    public void setRobotNo(String RobotNo) {
        this.RobotNo = RobotNo;
    }

    public int getMapid() {
        return Mapid;
    }

    public void setMapid(int Mapid) {
        this.Mapid = Mapid;
    }

    public MapPoint getPoint() {
        return Point;
    }

    public void setPoint(MapPoint Point) {
        this.Point = Point;
    }

}
