package com.chance.mimorobot.model;

import java.io.Serializable;

public class LineUpRequestModel implements Serializable {

    /**
     * robotno : sample string 1
     * typename : sample string 2
     * number : 1
     */

    private String robotno;
    private String typename;
    private int number;

    public String getRobotno() {
        return robotno;
    }

    public void setRobotno(String robotno) {
        this.robotno = robotno;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
