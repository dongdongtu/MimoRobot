package com.chance.mimorobot.model;

public class ActionItem {

    /**
     * ActionID : 1
     * ActionName : sample string 1
     * ActionType : 1
     * ActionHttpUrl : sample string 2
     * Parameter : sample string 3
     */

    private int ActionID;
    private String ActionName;
    private int ActionType;
    private String ActionHttpUrl;
    private String Parameter;

    public int getActionID() {
        return ActionID;
    }

    public void setActionID(int ActionID) {
        this.ActionID = ActionID;
    }

    public String getActionName() {
        return ActionName;
    }

    public void setActionName(String ActionName) {
        this.ActionName = ActionName;
    }

    public int getActionType() {
        return ActionType;
    }

    public void setActionType(int ActionType) {
        this.ActionType = ActionType;
    }

    public String getActionHttpUrl() {
        return ActionHttpUrl;
    }

    public void setActionHttpUrl(String ActionHttpUrl) {
        this.ActionHttpUrl = ActionHttpUrl;
    }

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String Parameter) {
        this.Parameter = Parameter;
    }
}
