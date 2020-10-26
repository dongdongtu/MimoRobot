package com.chance.mimorobot.mqtt;

import android.text.TextUtils;

import com.ibm.micro.client.mqttv3.MqttCallback;
import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttConnectOptions;
import com.ibm.micro.client.mqttv3.MqttDefaultFilePersistence;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttException;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;

import java.util.ArrayList;
import java.util.List;

public class MQTTConnect {
    /**
     * 传输质量  0 最多一次  1最少一次 2传输质量最高
     */
    private int qos = 1;
    /**
     * MQTT服务器地址
     */
    private String ip = "";
    /**
     * MQTT的服务器端口号
     */
    private int port = 1883;
    private MqttClient mClient;
    /**
     * 当前客户端所注册的名称
     */
    private String clientId;
    private String url;
    /**
     * 注册Topic的列表
     */
    List<String> subscribeTopics = new ArrayList<>();
    MqttCallback callback;

    public MQTTConnect(String ip, int port, String clientId) {
        this.ip = ip;
        this.port = port;
        this.clientId = clientId;
        init();
    }

    public MQTTConnect(String ip, int port, String clientId, int qos) {
        this.ip = ip;
        this.port = port;
        this.clientId = clientId;
        this.qos = qos;
        init();
    }

    public void setCallback(MqttCallback callback) {
        this.callback = callback;
        try {
            mClient.setCallback(callback);
        } catch (MqttException e) {
        }
    }

    /**
     * 初始化数据
     */
    private void init() {
        try {
            url = "tcp://" + ip + ":" + port;
            String tmpDir = System.getProperty("java.io.tmpdir");
            MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
            mClient = new MqttClient(url, clientId, dataStore);
        } catch (MqttException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return mClient.isConnected();
    }

    /**
     * 订阅指定的Topics
     *
     * @param subscribeTopic
     * @throws MqttException
     */
    public void subcribeTopic(String subscribeTopic) {
        try {
            if (mClient != null && mClient.isConnected() && !TextUtils.isEmpty(subscribeTopic)) {
                mClient.subscribe(subscribeTopic, qos);
            }
        } catch (MqttException e) {
        }
    }

    /**
     * 取消订阅的Topic
     */
    public void unSubcribeTopic(String subscribeTopic) {
        try {
            if (mClient != null && mClient.isConnected() && !TextUtils.isEmpty(subscribeTopic)) {
                mClient.unsubscribe(subscribeTopic);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消所有订阅的Topic
     */
    public void unSubcribeAllTopics() {
        try {
            if (mClient != null && mClient.isConnected()) {
                for (String string : subscribeTopics) {
                    mClient.unsubscribe(string);
                }
            }
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接MQTT的方法
     */
    public void connect() throws MqttException {
        mClient.connect();
    }

    /**
     * 断开连接T的方法
     */
    public void disconnect() {
        try {
            unSubcribeAllTopics();
            if (mClient.isConnected())
                mClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接需要验证的MQTT的方法
     *
     * @param user
     * @param psw
     * @throws MqttException
     */
    public void connect(String user, String psw) throws Exception {
        MqttConnectOptions op = new MqttConnectOptions();
        op.setUserName(user);
        op.setPassword(new String(psw).toCharArray());
        mClient.connect(op);
    }

    /**
     * 向指定的Topic发送Topic的方法
     *
     * @param topic
     * @param sendMes
     * @throws MqttException
     */
    public void publish(String topic, String sendMes) {
        publish(topic, sendMes.getBytes());
    }

    /**
     * 向指定的Topic发送Topic的方法
     *
     * @param topic
     * @throws MqttException
     */
    public void publish(String topic, byte[] sendBytes) {
        try {
            if (mClient != null && mClient.isConnected() && !TextUtils.isEmpty(topic)) {
                MqttTopic mTopic = mClient.getTopic(topic);
                MqttMessage message = new MqttMessage(sendBytes);
                message.setQos(qos);
                MqttDeliveryToken token = mTopic.publish(message);
                token.waitForCompletion();
            }
        } catch (Exception e) {

        }
    }
}
