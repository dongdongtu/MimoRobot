package com.chance.mimorobot.model;

import java.io.Serializable;
import java.util.List;

public class ExplainModel implements Serializable {

    /**
     * data : [{"id":1,"pathname":"sample string 1","des":"sample string 2","point":[{"x":1.1,"y":1.1,"z":1.1,"PointName":"sample string 1"},{"x":1.1,"y":1.1,"z":1.1,"PointName":"sample string 1"}],"nextActionLists":[[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}],[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}]]},{"id":1,"pathname":"sample string 1","des":"sample string 2","point":[{"x":1.1,"y":1.1,"z":1.1,"PointName":"sample string 1"},{"x":1.1,"y":1.1,"z":1.1,"PointName":"sample string 1"}],"nextActionLists":[[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}],[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}]]}]
     * code : 1
     * res : true
     * msg : sample string 3
     */

    private int code;
    private boolean res;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * pathname : sample string 1
         * des : sample string 2
         * point : [{"x":1.1,"y":1.1,"z":1.1,"PointName":"sample string 1"},{"x":1.1,"y":1.1,"z":1.1,"PointName":"sample string 1"}]
         * nextActionLists : [[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}],[{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"},{"ActionID":1,"ActionName":"sample string 1","ActionType":1,"ActionHttpUrl":"sample string 2","Parameter":"sample string 3"}]]
         */

        private int id;
        private String pathname;
        private String des;
        private List<MapPoint> point;
        private List<List<ActionItem>> nextActionLists;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPathname() {
            return pathname;
        }

        public void setPathname(String pathname) {
            this.pathname = pathname;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public List<MapPoint> getPoint() {
            return point;
        }

        public void setPoint(List<MapPoint> point) {
            this.point = point;
        }

        public List<List<ActionItem>> getNextActionLists() {
            return nextActionLists;
        }

        public void setNextActionLists(List<List<ActionItem>> nextActionLists) {
            this.nextActionLists = nextActionLists;
        }




    }
}
