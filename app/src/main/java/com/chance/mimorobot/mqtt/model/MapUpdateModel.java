package com.chance.mimorobot.mqtt.model;


import java.io.Serializable;
import java.util.List;

public class MapUpdateModel implements Serializable {


    /**
     * Mapid : 1
     * MapName : 测试
     * MapDes :
     * MapSourceUrl : /Files/MapSource/RobotMapSource_20200804155643000_测试1596612768843.stcm
     * Points : [{"x":0.7811524,"y":-0.8222226,"z":-1.3141128,"name":"测试点1","des":""},{"x":0.30880862,"y":0.505585,"z":1.9074763,"name":"测试点2","des":""}]
     */

    private int Mapid;
    private String MapName;
    private String MapDes;
    private String MapSourceUrl;


    public int getMapid() {
        return Mapid;
    }

    public void setMapid(int Mapid) {
        this.Mapid = Mapid;
    }

    public String getMapName() {
        return MapName;
    }

    public void setMapName(String MapName) {
        this.MapName = MapName;
    }

    public String getMapDes() {
        return MapDes;
    }

    public void setMapDes(String MapDes) {
        this.MapDes = MapDes;
    }

    public String getMapSourceUrl() {
        return MapSourceUrl;
    }

    public void setMapSourceUrl(String MapSourceUrl) {
        this.MapSourceUrl = MapSourceUrl;
    }




}
