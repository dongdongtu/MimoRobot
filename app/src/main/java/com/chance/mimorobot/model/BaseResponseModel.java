package com.chance.mimorobot.model;

public class BaseResponseModel {

    /**
     * code : 1
     * res : true
     * msg : sample string 3
     */

    private int code;
    private boolean res;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
