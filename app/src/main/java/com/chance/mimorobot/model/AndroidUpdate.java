package com.chance.mimorobot.model;

import java.io.Serializable;

public class AndroidUpdate implements Serializable {

    /**
     * data : {"VersionCode":1,"VersionName":"sample string 2","ApkUrl":"sample string 3","Cdate":"2020-09-19 12:14:43","IsImposed":true,"Des":"sample string 4"}
     * code : 1
     * res : true
     * msg : sample string 3
     */

    private DataBean data;
    private int code;
    private boolean res;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * VersionCode : 1
         * VersionName : sample string 2
         * ApkUrl : sample string 3
         * Cdate : 2020-09-19 12:14:43
         * IsImposed : true
         * Des : sample string 4
         */

        private int VersionCode;
        private String VersionName;
        private String ApkUrl;
        private String Cdate;
        private boolean IsImposed;
        private String Des;

        public int getVersionCode() {
            return VersionCode;
        }

        public void setVersionCode(int VersionCode) {
            this.VersionCode = VersionCode;
        }

        public String getVersionName() {
            return VersionName;
        }

        public void setVersionName(String VersionName) {
            this.VersionName = VersionName;
        }

        public String getApkUrl() {
            return ApkUrl;
        }

        public void setApkUrl(String ApkUrl) {
            this.ApkUrl = ApkUrl;
        }

        public String getCdate() {
            return Cdate;
        }

        public void setCdate(String Cdate) {
            this.Cdate = Cdate;
        }

        public boolean isIsImposed() {
            return IsImposed;
        }

        public void setIsImposed(boolean IsImposed) {
            this.IsImposed = IsImposed;
        }

        public String getDes() {
            return Des;
        }

        public void setDes(String Des) {
            this.Des = Des;
        }
    }
}
