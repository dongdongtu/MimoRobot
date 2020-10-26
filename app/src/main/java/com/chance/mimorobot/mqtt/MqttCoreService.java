package com.chance.mimorobot.mqtt;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;


import com.chance.mimorobot.MyApplication;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.manager.SharedPreferencesManager;
import com.chance.mimorobot.mqtt.model.BaseMqttModel;
import com.chance.mimorobot.mqtt.model.HeartBeat;
import com.google.gson.Gson;
import com.ibm.micro.client.mqttv3.MqttCallback;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttException;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.chance.mimorobot.constant.Constant.MQTT_IP;
import static com.chance.mimorobot.constant.Constant.MQTT_PORT;


/**
 * MQTT 核心控制服务
 */
public class MqttCoreService extends Service {

    private static final String TAG = MqttCoreService.class.getSimpleName();


    public static final String ACTION_SEND_DATA = "ACTION_SEND_DATA";
    public static final String EXTRAS_SEND_DATA = "EXTRAS_SEND_DATA";
    public static final String ACTION_REV_DATA = "ACTION_REV_DATA";
    public static final String EXTRAS_REV_DATA = "EXTRAS_REV_DATA";

    MQTTConnect mqttConnect;
    String user = "mmrobot";
    String pwd = "123#321";


    private boolean isFirst=true;
    private Disposable disposableHeat;


    boolean flag = true;

    String subcribeTopic, publishTopic;

    public MqttCoreService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mqttConnect = new MQTTConnect(MQTT_IP, MQTT_PORT, Globle.robotId);
        mqttConnect.setCallback(mqttCallback);
        registerReceiver();
        subcribeTopic = "/robot/" + Globle.robotId;
        publishTopic = "/platform" ;
        Log.e(TAG, "Topic:" + subcribeTopic + " " + publishTopic);
        new ConnThread().start();
    }

    MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable throwable) {
            Log.e(TAG, "MQTT 断开连接:" + throwable.toString());
            // 连接断开后重连
            if (flag) {
                new ConnThread().start();
            }
        }

        @Override
        public void messageArrived(MqttTopic mqttTopic, MqttMessage mqttMessage) {
            try {
                String data = new String(mqttMessage.getPayload());
//                Log.e(TAG, "接收到数据：" + mqttTopic.getName() + "  --> " + data);
                sendBroadCastData(data);
                // 数据返回
//                mqttConnect.publish(publishTopic, data);
            } catch (Exception e) {
            }
        }

        @Override
        public void deliveryComplete(MqttDeliveryToken mqttDeliveryToken) {

        }
    };

    /**
     * 将接收到的数据通过广播发送出去
     *
     * @param data
     */
    private void sendBroadCastData(String data) {
        Intent intent = new Intent(ACTION_REV_DATA);
        intent.putExtra(EXTRAS_REV_DATA, data);
        sendBroadcast(intent);
    }

    /**
     * 连接MQTT的线程
     */
    class ConnThread extends Thread {
        @Override
        public void run() {
            while (flag && !mqttConnect.isConnected()) {
                try {
                    mqttConnect.connect(user, pwd);
                    // 连接成功后 注册Topic
                    mqttConnect.subcribeTopic(subcribeTopic);
                    Log.e(TAG, "MQTT 连接成功");
                    if (disposableHeat==null||disposableHeat.isDisposed()){
                        startBeat();
                    }
                } catch (MqttException e) {
//                    LogUtil.error(TAG, e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy -- -- --");
        flag = false;
        mqttConnect.disconnect();
        unregisterReceiver();
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter(ACTION_SEND_DATA);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 注销广播
     */
    private void unregisterReceiver() {
        unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_SEND_DATA.equals(action)) {
                String data = intent.getStringExtra(EXTRAS_SEND_DATA);
                try {
                    mqttConnect.publish(publishTopic,data );
//                    Log.e(TAG,"MQTT 发送数据:" + data);
                } catch (Exception e) {

                }
            }
        }
    };

    public void startBeat() {
        if (isFirst){
            isFirst=false;

            disposableHeat = Flowable.interval(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            BaseMqttModel baseModel = new BaseMqttModel<HeartBeat>();
                            baseModel.setCode(1);
                            baseModel.setFrom(Globle.robotId);
                            baseModel.setTo("platform");
                            baseModel.setMd5("");
                            baseModel.setCodeName("heartbeat");
                            HeartBeat heartBeat = new HeartBeat();
                            heartBeat.setMapID(SharedPreferencesManager.newInstance().getMapID());
                            heartBeat.setRobotStatusCode(Globle.charge);
                            heartBeat.setRobotElectricQuantity("");
                            heartBeat.setRobotSpeed("1");
                            heartBeat.setRobotSpeedUnit("m/s");
                            heartBeat.setPoint(MyApplication.location);
                            baseModel.setData(heartBeat);
                            if(baseModel.getFrom().equals("-1"))
                                return;
                            mqttConnect.publish(publishTopic,new Gson().toJson(baseModel));
                        }
                    });
        }
    }

}
