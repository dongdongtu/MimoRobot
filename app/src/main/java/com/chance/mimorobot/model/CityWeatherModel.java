package com.chance.mimorobot.model;

/**
 * created by Lynn
 * on 2019/9/18
 */
public class CityWeatherModel {

    private String city;  //城市
    private String tempReal; //当前温度
    private String tempRange; //温度范围
    private String weather; //天气状况（晴  多云）
    private String airQuality; //（空气质量）优
    private String wind; //风（西风3~4级）
    private String tempHigh;
    private String tempLow;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTempReal() {
        return tempReal;
    }

    public void setTempReal(String tempReal) {
        this.tempReal = tempReal;
    }

    public String getTempRange() {
        return tempRange;
    }

    public void setTempRange(String tempRange) {
        this.tempRange = tempRange;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getTempLow() {
        return tempLow;
    }

    public void setTempLow(String tempLow) {
        this.tempLow = tempLow;
    }

    public String getTempHigh() {
        return tempHigh;
    }

    public void setTempHigh(String tempHigh) {
        this.tempHigh = tempHigh;
    }
}
