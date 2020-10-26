package com.chance.mimorobot.manager;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ConvertUtils;
import com.chance.mimorobot.callback.OnSerialDataCallBack;

import org.greenrobot.greendao.annotation.Convert;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import cn.chuangze.robot.serial.SerialPort;


/**
 * @Description 串口命令控制器
 * @Author zzd
 * @Time 2020/7/19 10:51
 */

public class SerialControlManager {

    private final String TAG= SerialControlManager.class.getSimpleName();
    public static final String DEVICE = "ttyS0";
    // 波特率
    public static final int BANDRATE = 115200;

    private OnSerialDataCallBack onDataBackListeners;

    private SerialPort serialPort;

    private static SerialControlManager serialControlManager = null;


    public static SerialControlManager newInstance() {
        if (serialControlManager == null) {
            serialControlManager = new SerialControlManager();
        }
        return serialControlManager;
    }

    private SerialControlManager() {
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
     * 唤醒mic
     *
     * @param
     */
    public void setBeam(int a){
        byte[] sd = new byte[6];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x02;
        sd[3] = (byte) 0x0A;
        sd[4] = (byte) a;
        sd[5] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4]);
        serialPort.send(sd);
    }

    /**
     * 唤醒mic
     *
     * @param
     */
    public void sleep(){
        byte[] sd = new byte[5];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x01;
        sd[3] = (byte) 0x0B;
        sd[4] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]);
        serialPort.send(sd);
    }


    /**
     * 设置表情
     *
     * @param
     */
    public void setFace(int a){
        Log.e(TAG,a+"");
        byte[] sd = new byte[6];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x02;
        sd[3] = (byte) 0x09;
        sd[4] = (byte) a;
        sd[5] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4]);
        serialPort.send(sd);
    }

    /**
     *头左转
     */
    public void headTurnLeft(int time){
        byte[] sd = new byte[10];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x06;
        sd[3] = (byte) 0x01;//命令
        sd[4] = (byte) 0x02;//电机号
        sd[5] = (byte) 0x01;   //方向
        sd[6] = (byte) 0x00;   //速度
        sd[7] = (byte) 0xc7;   //速度
        sd[8] = (byte) time;   //时间
        sd[9] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]);
        serialPort.send(sd);
    }
    /**
     *头右转
     */
    public void headTurnRight(int time){
        byte[] sd = new byte[10];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x06;
        sd[3] = (byte) 0x01;//命令
        sd[4] = (byte) 0x02;//电机号
        sd[5] = (byte) 0x02;   //方向
        sd[6] = (byte) 0x00;   //速度
        sd[7] = (byte) 0xc7;   //速度
        sd[8] = (byte) time;   //时间
        sd[9] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]);
        serialPort.send(sd);
    }

    /**
     *头向上
     */
    public void headTurnUp(int time){
        byte[] sd = new byte[10];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x06;
        sd[3] = (byte) 0x01;//命令
        sd[4] = (byte) 0x01;//电机号
        sd[5] = (byte) 0x01;   //方向
        sd[6] = (byte) 0x00;   //速度
        sd[7] = (byte) 0xc7;   //速度
        sd[8] = (byte) time;   //时间
        sd[9] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]);
        serialPort.send(sd);
    }


    /**
     *头向下
     */
    public void headTurnDown(int time){
        byte[] sd = new byte[10];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x06;
        sd[3] = (byte) 0x01;//命令
        sd[4] = (byte) 0x01;//电机号
        sd[5] = (byte) 0x02;   //方向
        sd[6] = (byte) 0x00;   //速度
        sd[7] = (byte) 0xc7;   //速度
        sd[8] = (byte) time;   //时间
        sd[9] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]);
        serialPort.send(sd);
    }


    /**
     *左胳膊向上
     */
    public void armLeftTurnUp(int time){
        byte[] sd = new byte[10];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x06;
        sd[3] = (byte) 0x01;//命令
        sd[4] = (byte) 0x04;//电机号
        sd[5] = (byte) 0x01;   //方向
        sd[6] = (byte) 0x00;   //速度
        sd[7] = (byte) 0xc7;   //速度
        sd[8] = (byte) time;   //时间
        sd[9] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]);
        serialPort.send(sd);
    }


    /**
     *左胳膊向下
     */
    public void armLeftTurnDown(int time){
        byte[] sd = new byte[10];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x06;
        sd[3] = (byte) 0x01;//命令
        sd[4] = (byte) 0x04;//电机号
        sd[5] = (byte) 0x02;   //方向
        sd[6] = (byte) 0x00;   //速度
        sd[7] = (byte) 0xc7;   //速度
        sd[8] = (byte) time;   //时间
        sd[9] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]);
        serialPort.send(sd);
    }


    /**
     *右胳膊向上
     */
    public void armRightTurnUp(int time){
        byte[] sd = new byte[10];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x06;
        sd[3] = (byte) 0x01;//命令
        sd[4] = (byte) 0x03;//电机号
        sd[5] = (byte) 0x01;   //方向
        sd[6] = (byte) 0x00;   //速度
        sd[7] = (byte) 0xc7;   //速度
        sd[8] = (byte) time;   //时间
        sd[9] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]);
        serialPort.send(sd);
    }


    /**
     *右胳膊向下
     */
    public void armRightTurnDown(int time){
        byte[] sd = new byte[10];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x06;
        sd[3] = (byte) 0x01;//命令
        sd[4] = (byte) 0x03;//电机号
        sd[5] = (byte) 0x02;   //方向
        sd[6] = (byte) 0x00;   //速度
        sd[7] = (byte) 0xc7;   //速度
        sd[8] = (byte) time;   //时间
        sd[9] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]);
        serialPort.send(sd);
    }



    public void lightGREENControl(){
        byte[] sd = new byte[12];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x08;
        sd[3] = (byte) 0x04;//命令
        sd[4] = (byte) 0x01;//灯号
        sd[5] = (byte) 0x00;//R 关闭
        sd[6] = (byte) 0x05;//R 开庆
        sd[7] = (byte) 0x00;//G 关闭
        sd[8] = (byte) 0x00;//G 开启
        sd[9] = (byte) 0x00;//B 关闭
        sd[10] = (byte) 0x00;//B 开启
        sd[11] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]^ sd[9]^ sd[10]);
        serialPort.send(sd);
    }


    public void lightBLUEControl(){
        byte[] sd = new byte[12];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x08;
        sd[3] = (byte) 0x04;//命令
        sd[4] = (byte) 0x01;//灯号
        sd[5] = (byte) 0x00;//R 关闭
        sd[6] = (byte) 0x00;//R 开庆
        sd[7] = (byte) 0x00;//G 关闭
        sd[8] = (byte) 0x05;//G 开启
        sd[9] = (byte) 0x00;//B 关闭
        sd[10] = (byte) 0x00;//B 开启
        sd[11] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]^ sd[9]^ sd[10]);
        serialPort.send(sd);
    }

    public void lightREDControl(){
        byte[] sd = new byte[12];
        sd[0] = (byte) 0xAA;
        sd[1] = (byte) 0xBB;
        sd[2] = (byte) 0x08;
        sd[3] = (byte) 0x04;//命令
        sd[4] = (byte) 0x01;//灯号
        sd[5] = (byte) 0x00;//R 关闭
        sd[6] = (byte) 0x00;//R 开庆
        sd[7] = (byte) 0x00;//G 关闭
        sd[8] = (byte) 0x00;//G 开启
        sd[9] = (byte) 0x00;//B 关闭
        sd[10] = (byte) 0x05;//B 开启
        sd[11] = (byte) (byte) (sd[0] ^ sd[1]^ sd[2]^ sd[3]^ sd[4] ^ sd[5] ^ sd[6] ^ sd[7] ^ sd[8]^ sd[9]^ sd[10]);
        serialPort.send(sd);
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
//        String s=new String (data).trim();
//        if (s.startsWith("WAKE UP!")){
//           String num= s.split(":")[1].substring(0,s.split(":")[1].indexOf(" "));
//           Log.e(TAG,num);
//           iTouchAction.wakeup(Integer.parseInt(num));
//        }

        if (data.length<3)
            return;
        switch (data[3]){
            case 0x07:
                Log.e(TAG,"ANGLE = "+data[4]+ "  "+data[5]);
                iTouchAction.wakeup(((data[4]&0xff)*255)+(data[5]&0xff));
                break;
            case 0x08:
                iTouchAction.temp((((data[4]&0xff)*255)+(data[5]&0xff)),((((data[6]&0xff)*255))+(data[7]&0xff))/100);
                break;
        }
    }
}
