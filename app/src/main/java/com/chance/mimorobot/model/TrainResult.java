package com.chance.mimorobot.model;

import java.util.List;

/**
 * AIUI火车票实体类
 *
 * @create by intkilow
 * @time 2018年09月25日15:24:27
 */
public class TrainResult {

    /**
     * arrivalTime : 2019-03-07 13:06
     * endtime_for_voice : 明天13:06
     * endtimestamp : 1551935160
     * originCity : 北京市
     * originStation : 北京南
     * price : [{"name":"二等座","value":"553"},{"name":"一等座","value":"933"},{"name":"商务座","value":"1748"}]
     * runTime : 5时6分
     * startTime : 2019-03-07 08:00
     * startTimeStamp : 1551916800
     * starttime_for_voice : 明天08:00
     * starttimestamp : 1551916800
     * terminalCity : 上海市
     * terminalStation : 上海虹桥
     * trainNo : G11
     * trainType : 高铁
     */

    private String arrivalTime;
    private String endtime_for_voice;
    private int endtimestamp;
    private String originCity;
    private String originStation;
    private String runTime;
    private String startTime;
    private String startTimeStamp;
    private String starttime_for_voice;
    private int starttimestamp;
    private String terminalCity;
    private String terminalStation;
    private String trainNo;
    private String trainType;
    private List<PriceBean> price;

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getEndtime_for_voice() {
        return endtime_for_voice;
    }

    public void setEndtime_for_voice(String endtime_for_voice) {
        this.endtime_for_voice = endtime_for_voice;
    }

    public int getEndtimestamp() {
        return endtimestamp;
    }

    public void setEndtimestamp(int endtimestamp) {
        this.endtimestamp = endtimestamp;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getOriginStation() {
        return originStation;
    }

    public void setOriginStation(String originStation) {
        this.originStation = originStation;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(String startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public String getStarttime_for_voice() {
        return starttime_for_voice;
    }

    public void setStarttime_for_voice(String starttime_for_voice) {
        this.starttime_for_voice = starttime_for_voice;
    }

    public int getStarttimestamp() {
        return starttimestamp;
    }

    public void setStarttimestamp(int starttimestamp) {
        this.starttimestamp = starttimestamp;
    }

    public String getTerminalCity() {
        return terminalCity;
    }

    public void setTerminalCity(String terminalCity) {
        this.terminalCity = terminalCity;
    }

    public String getTerminalStation() {
        return terminalStation;
    }

    public void setTerminalStation(String terminalStation) {
        this.terminalStation = terminalStation;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public List<PriceBean> getPrice() {
        return price;
    }

    public void setPrice(List<PriceBean> price) {
        this.price = price;
    }

    public static class PriceBean {
        /**
         * name : 二等座
         * value : 553
         */

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
