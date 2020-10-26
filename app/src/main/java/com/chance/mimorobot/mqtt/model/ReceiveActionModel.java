package com.chance.mimorobot.mqtt.model;

import java.io.Serializable;

public class ReceiveActionModel implements Serializable {
    private static final long serialVersionUID = -8955043946510019715L;

    /**
     * CodeType : 1
     * data : {"mapid":1,"pathid":1,"point":{"x":123,"y":231,"z":123.41},"arm":"left","rgb":"","face":"高兴"}
     */

    private int CodeType;
    private MqttActionModel data;

    public int getCodeType() {
        return CodeType;
    }

    public void setCodeType(int codeType) {
        CodeType = codeType;
    }

    public MqttActionModel getData() {
        return data;
    }

    public void setData(MqttActionModel data) {
        this.data = data;
    }
}
