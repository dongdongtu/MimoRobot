package com.chance.mimorobot.model;

import com.chance.mimorobot.db.entity.FaceEntity;

import java.io.Serializable;
import java.util.List;

public class FaceListModel implements Serializable {
    private static final long serialVersionUID = 7892208221834884132L;


    /**
     * AddList : [{"faceid":1,"robotno":"sample string 1","operate":1,"cdate":"2020-09-16 19:26:12","FaceImageUrl":"sample string 2","SayHelloText":"sample string 3","Name":"sample string 4","Des":"sample string 5"},{"faceid":1,"robotno":"sample string 1","operate":1,"cdate":"2020-09-16 19:26:12","FaceImageUrl":"sample string 2","SayHelloText":"sample string 3","Name":"sample string 4","Des":"sample string 5"}]
     * DelList : [{"faceid":1,"robotno":"sample string 1","operate":1,"cdate":"2020-09-16 19:26:12","FaceImageUrl":"sample string 2","SayHelloText":"sample string 3","Name":"sample string 4","Des":"sample string 5"},{"faceid":1,"robotno":"sample string 1","operate":1,"cdate":"2020-09-16 19:26:12","FaceImageUrl":"sample string 2","SayHelloText":"sample string 3","Name":"sample string 4","Des":"sample string 5"}]
     * EditList : [{"faceid":1,"robotno":"sample string 1","operate":1,"cdate":"2020-09-16 19:26:12","FaceImageUrl":"sample string 2","SayHelloText":"sample string 3","Name":"sample string 4","Des":"sample string 5"},{"faceid":1,"robotno":"sample string 1","operate":1,"cdate":"2020-09-16 19:26:12","FaceImageUrl":"sample string 2","SayHelloText":"sample string 3","Name":"sample string 4","Des":"sample string 5"}]
     * code : 1
     * res : true
     * msg : sample string 3
     */

    private int code;
    private boolean res;
    private String msg;
    private List<FaceEntity> AddList;
    private List<FaceEntity> DelList;
    private List<FaceEntity> EditList;
    private List<FaceEntity> DataList;

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

    public List<FaceEntity> getAddList() {
        return AddList;
    }

    public void setAddList(List<FaceEntity> AddList) {
        this.AddList = AddList;
    }

    public List<FaceEntity> getDelList() {
        return DelList;
    }

    public void setDelList(List<FaceEntity> DelList) {
        this.DelList = DelList;
    }

    public List<FaceEntity> getEditList() {
        return EditList;
    }

    public void setEditList(List<FaceEntity> EditList) {
        this.EditList = EditList;
    }

    public List<FaceEntity> getDataList() {
        return DataList;
    }

    public void setDataList(List<FaceEntity> DataList) {
        this.DataList = DataList;
    }

}
