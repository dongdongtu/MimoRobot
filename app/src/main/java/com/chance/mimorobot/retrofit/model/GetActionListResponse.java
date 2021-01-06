package com.chance.mimorobot.retrofit.model;

import java.io.Serializable;
import java.util.List;

public class GetActionListResponse implements Serializable {

    /**
     * data : [{"id":1,"type":1,"name":"sample string 1","des":"sample string 2","yituid":1},{"id":1,"type":1,"name":"sample string 1","des":"sample string 2","yituid":1}]
     * code : 1
     * res : true
     * msg : sample string 3
     */

    private int code;
    private boolean res;
    private String msg;
    private List<ActionItemModel> data;

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

    public List<ActionItemModel> getData() {
        return data;
    }

    public void setData(List<ActionItemModel> data) {
        this.data = data;
    }

}
