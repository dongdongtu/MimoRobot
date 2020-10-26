package com.chance.mimorobot.model;

/**
 * @Description 航班信息
 * @Author wj
 * @Time 2017/9/21 14:56
 */

public class FlightResult {

    /**
     * starttimestamp : 1505976900
     * departCity : 上海
     * aPort : 首都国际机场
     * cabinInfo : 经济舱
     * endtimestamp : 1505985600
     * endtime_for_voice : 今天17: 20: 00
     * flight : MU272
     * arriveTimeStamp : 1505985600
     * price : 1230
     * rate : 0.99
     * takeOffTimeStamp : 1505976900
     * arriveTime : 2017-09-2117: 20: 00
     * standardPrice : 1240
     * starttime_for_voice : 今天14: 55: 00
     * quantity : 10
     * dPort : 浦东国际机场
     * takeOffTime : 2017-09-2114: 55: 00
     * arriveCity : 北京
     * airline : 中国东方航空股份有限公司
     */
    private String aPort;
    private String airline;
    private String arriveActualTime;
    private String arriveCity;
    private String arriveReadyTime;
    private String arriveTime;
    private String arriveTimeStamp;
    private String arriveTimezone;
    private String cabinInfo;
    private String dPort;
    private String departActualTime;
    private String departCity;
    private String departReadyTime;
    private String departTimezone;
    private String endtime_for_voice;
    private int endtimestamp;
    private String flight;
    private String flightState;
    private String ontimeRate;
    private String price;
    private String quantity;
    private String rate;
    private String standardPrice;

    public String getArriveActualTime() {
        return arriveActualTime;
    }

    public void setArriveActualTime(String arriveActualTime) {
        this.arriveActualTime = arriveActualTime;
    }

    public String getArriveReadyTime() {
        return arriveReadyTime;
    }

    public void setArriveReadyTime(String arriveReadyTime) {
        this.arriveReadyTime = arriveReadyTime;
    }

    public String getArriveTimezone() {
        return arriveTimezone;
    }

    public void setArriveTimezone(String arriveTimezone) {
        this.arriveTimezone = arriveTimezone;
    }

    public String getDepartActualTime() {
        return departActualTime;
    }

    public void setDepartActualTime(String departActualTime) {
        this.departActualTime = departActualTime;
    }

    public String getDepartReadyTime() {
        return departReadyTime;
    }

    public void setDepartReadyTime(String departReadyTime) {
        this.departReadyTime = departReadyTime;
    }

    public String getDepartTimezone() {
        return departTimezone;
    }

    public void setDepartTimezone(String departTimezone) {
        this.departTimezone = departTimezone;
    }

    public String getFlightState() {
        return flightState;
    }

    public void setFlightState(String flightState) {
        this.flightState = flightState;
    }

    public String getOntimeRate() {
        return ontimeRate;
    }

    public void setOntimeRate(String ontimeRate) {
        this.ontimeRate = ontimeRate;
    }

    public String getTodayTimeRate() {
        return todayTimeRate;
    }

    public void setTodayTimeRate(String todayTimeRate) {
        this.todayTimeRate = todayTimeRate;
    }

    private String starttime_for_voice;
    private int starttimestamp;
    private String takeOffTime;
    private String takeOffTimeStamp;
    private String todayTimeRate;

    public int getStarttimestamp() {
        return starttimestamp;
    }

    public void setStarttimestamp(int starttimestamp) {
        this.starttimestamp = starttimestamp;
    }

    public String getDepartCity() {
        return departCity;
    }

    public void setDepartCity(String departCity) {
        this.departCity = departCity;
    }

    public String getaPort() {
        return aPort;
    }

    public void setaPort(String aPort) {
        this.aPort = aPort;
    }

    public String getCabinInfo() {
        return cabinInfo;
    }

    public void setCabinInfo(String cabinInfo) {
        this.cabinInfo = cabinInfo;
    }

    public int getEndtimestamp() {
        return endtimestamp;
    }

    public void setEndtimestamp(int endtimestamp) {
        this.endtimestamp = endtimestamp;
    }

    public String getEndtime_for_voice() {
        return endtime_for_voice;
    }

    public void setEndtime_for_voice(String endtime_for_voice) {
        this.endtime_for_voice = endtime_for_voice;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public String getArriveTimeStamp() {
        return arriveTimeStamp;
    }

    public void setArriveTimeStamp(String arriveTimeStamp) {
        this.arriveTimeStamp = arriveTimeStamp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTakeOffTimeStamp() {
        return takeOffTimeStamp;
    }

    public void setTakeOffTimeStamp(String takeOffTimeStamp) {
        this.takeOffTimeStamp = takeOffTimeStamp;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(String standardPrice) {
        this.standardPrice = standardPrice;
    }

    public String getStarttime_for_voice() {
        return starttime_for_voice;
    }

    public void setStarttime_for_voice(String starttime_for_voice) {
        this.starttime_for_voice = starttime_for_voice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getdPort() {
        return dPort;
    }

    public void setdPort(String dPort) {
        this.dPort = dPort;
    }

    public String getTakeOffTime() {
        return takeOffTime;
    }

    public void setTakeOffTime(String takeOffTime) {
        this.takeOffTime = takeOffTime;
    }

    public String getArriveCity() {
        return arriveCity;
    }

    public void setArriveCity(String arriveCity) {
        this.arriveCity = arriveCity;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }
}
