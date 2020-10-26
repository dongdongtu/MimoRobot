package com.chance.mimorobot.model;

import java.util.List;

public class SemanticAciton {

    /**
     * excutenum : 1
     * steptotal : 1
     * nextActionLists : [[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}],[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}]]
     */

    private int excutenum;
    private int steptotal;
    private List<List<ActionItem>> nextActionLists;


    public List<List<ActionItem>> getNextActionLists() {
        return nextActionLists;
    }

    public void setNextActionLists(List<List<ActionItem>> nextActionLists) {
        this.nextActionLists = nextActionLists;
    }

    public int getExcutenum() {
        return excutenum;
    }

    public void setExcutenum(int excutenum) {
        this.excutenum = excutenum;
    }

    public int getSteptotal() {
        return steptotal;
    }

    public void setSteptotal(int steptotal) {
        this.steptotal = steptotal;
    }

}
