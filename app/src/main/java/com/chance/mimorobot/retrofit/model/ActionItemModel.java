package com.chance.mimorobot.retrofit.model;

import java.io.Serializable;

public class ActionItemModel implements Serializable {
    /**
     * id : 1
     * type : 1
     * name : sample string 1
     * des : sample string 2
     * yituid : 1
     */

    private int id;
    private int type;
    private String name;
    private String des;
    private int yituid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getYituid() {
        return yituid;
    }

    public void setYituid(int yituid) {
        this.yituid = yituid;
    }
}
