package com.chance.mimorobot.manager;

import android.util.Log;

import com.chance.mimorobot.callback.OnSerialDataCallBack;

import java.io.File;
import java.io.IOException;

import cn.chuangze.robot.serial.SerialPort;

public class HardwareManager {
    private final String TAG= SerialControlManager.class.getSimpleName();
    public static final String DEVICE = "ttyS0";
    // 波特率
    public static final int BANDRATE = 115200;

    private OnSerialDataCallBack onDataBackListeners;

    private SerialPort serialPort;

    private static HardwareManager hardwareManager = null;

    public static HardwareManager newInstance() {
        if (hardwareManager == null) {
            hardwareManager = new HardwareManager();
        }
        return hardwareManager;
    }

    private HardwareManager() {
        try {
            serialPort = new SerialPort(new File("/dev/" + DEVICE), BANDRATE, 0);
            serialPort.startListening(seriallistener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDeviceEventListener(OnSerialDataCallBack onDataBackListener) {
        this.onDataBackListeners=(onDataBackListener);
    }

    SerialPort.OnReceiveListener seriallistener = new SerialPort.OnReceiveListener() {
        @Override
        public void onReceived(byte[] data) {
            parseData(data, onDataBackListeners);
        }
    };

    /**
     * 向串口发送数据
     *
     * @param
     */
    public void send() {
        if (serialPort != null) {
//            Log.e(TAG,getByte(f,g,h).length+"");
            serialPort.send(getByte());
        }
    }

    /**
     * 向串口发送数据
     *
     * @param
     */
    public void setBeam(int a){
        String s="BEAM "+a;
        Log.e(TAG,s);
        serialPort.send(s.getBytes());
    }



    /***
     发送字节数组
     */
    private byte[] getByte() {
        byte[] sd = new byte[7];
        sd[0] = (byte) 0xFF;
        sd[1] = (byte) 0xFF;
        sd[2] = (byte) 0x03;
        sd[3] = (byte) 0x03;
        sd[4] = (byte) 0x03;
        sd[5] = (byte) 0x03;
        sd[6] = (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4]^ sd[5]);
        return sd;
    }


    public void destroy() {
        if (serialPort != null) {
            serialPort.stopListening();
            serialPort = null;
        }
    }

    public  void parseData(byte[] data,OnSerialDataCallBack iTouchAction) {
        if (data == null || iTouchAction == null)
            return;
        String s=new String (data).trim();
        if (s.startsWith("WAKE UP!")){
            String num= s.split(":")[1].substring(0,s.split(":")[1].indexOf(" "));
            Log.e(TAG,num);
            iTouchAction.wakeup(Integer.parseInt(num));
        }
    }
}
