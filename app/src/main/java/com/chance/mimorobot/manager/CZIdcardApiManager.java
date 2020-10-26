package com.chance.mimorobot.manager;

import android.os.Handler;
import android.util.Log;


import com.chance.mimorobot.model.IDCardData;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import cn.chuangze.robot.serial.SerialPort;


/**
 * @Author wj
 * @Date 2018/8/1
 * @Desc
 * @Url
 */

public class CZIdcardApiManager {

    private final String TAG= CZIdcardApiManager.class.getSimpleName();

    private final String xcard = " aa aa aa 96 69 00 08 00 00 9f 00 00 00 00 97";
    private final String xxcard = " aa aa aa 96 69 00 0c 00 00 90 00 00 00 00 00 00 00 00 9c";
    private final String errcard = " aa aa aa 96 69 00 04 00 00 80 84";
    private final String readcard = " aa aa aa 96 69 05 08 00 00 90";
    final static String errorcard = " aa aa aa 96 69 00 04 00 00 81 85";
    final static String readerror = " aa aa aa 96 69 00 04 00 00 41 45";
    final static String finderror = " aa aa aa 96 69 00 04 00 00 31 35";

    private byte x20 = 0x20, x01 = 0x01, x22 = 0x22, x02 = 0x02, x21 = 0x21, x30 = 0x30, x32 = 0x32;
    private boolean isReadIDCard = false;
    private byte bt[] = new byte[0];

    private static CZIdcardApiManager idCardHelper;

    private SerialPort serialPort;

    private IDCardDataListener cardDataListener;

    public static CZIdcardApiManager createCZIdcardApi() {
        if (idCardHelper == null) {
            synchronized (CZIdcardApiManager.class) {
                if (idCardHelper == null)
                    idCardHelper = new CZIdcardApiManager();
            }
        }
        return idCardHelper;
    }

    private CZIdcardApiManager() {
        try {
            serialPort = new SerialPort(new File("/dev/ttyS4"), 115200, 0);
            serialPort.startListening(seriallistener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    SerialPort.OnReceiveListener seriallistener = new SerialPort.OnReceiveListener() {
        @Override
        public void onReceived(byte[] data) {
            String returnxcard = byte2hex(data);
            if (returnxcard.contains(readcard)) {
                isReadIDCard = true;
            }
            if (isReadIDCard) {
                bt = addBytes(bt, data);
                // 数据的总长度  len1 05  len2 08 + Header 长度
                if (bt.length >= (256 * 5 + 8 + 7)) {
                    byte[] iData = Arrays.copyOfRange(bt, 10, bt.length);
                    if (cardDataListener != null) {
                        IDCardData idCardData = new IDCardData(iData);
                        cardDataListener.Success(idCardData);
                    }
                    isReadIDCard = false;
                    bt = new byte[0];
                }
            } else {
                switch (returnxcard) {
                    case xcard:
                        serialPort.send(getByte(x20, x02, x21));
                        break;
                    case xxcard:
                        // 讯卡
                        serialPort.send(getByte(x30, x01, x32));
                        break;
                    case readerror:
                        Log.e("TAG", "读证/卡操作失败");
                    case errorcard:
                        Log.e("TAG", " 选取证/卡失败");
                    case errcard:
                        Log.e("TAG", " 寻找证/卡失败");
                    case finderror:
                        //请重新放入刷卡区
                        Log.e("TAG", "证/卡认证 SAM_V 失败");
                        mHandler.postDelayed(sendRunnable, 500);
                        break;
                    default:
                        if (cardDataListener != null) {
                            cardDataListener.Error("读卡失败");
                        }
                        break;
                }
            }
        }
    };

    Handler mHandler = new Handler();

    Runnable sendRunnable = new Runnable() {
        @Override
        public void run() {
            serialPort.send(getByte((byte) 0x20, (byte) 0x01, (byte) 0x22));
        }
    };

    /**
     * 字节数组输出
     */
    private static String byte2hex(byte[] buffer) {
        String h = "";

        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + " " + temp;
        }

        return h;

    }

    /**
     * 合并两个字节数组
     *
     * @param data1
     * @param data2
     * @return
     */
    private byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /***
     发送字节数组
     */
    private byte[] getByte(byte f, byte g, byte h) {
        byte[] sd = new byte[10];
        sd[0] = (byte) 0xaa;
        sd[1] = (byte) 0xaa;
        sd[2] = (byte) 0xaa;
        sd[3] = (byte) 0x96;
        sd[4] = (byte) 0x69;
        sd[5] = (byte) 0x00;
        sd[6] = (byte) 0x03;
        sd[7] = f;
        sd[8] = g;
        sd[9] = h;
        return sd;
    }

    /**
     * 开始读卡
     */
    public void startReadIDCard(IDCardDataListener dataListener) {
        Log.e(TAG,"startReadIDCard");
        if (dataListener != null) {
            this.cardDataListener = dataListener;
            mHandler.post(sendRunnable);
        }
    }

    /**
     * 停止读卡
     */
    public void stopReadIDCard() {
        isReadIDCard = false;
        cardDataListener = null;
        mHandler.removeCallbacks(sendRunnable);
    }

    /**
     * 销毁资源
     */
    public void destroy() {
        if (serialPort != null) {
            serialPort.stopListening();
            serialPort = null;
        }
    }

    public interface IDCardDataListener {
        void Success(IDCardData cardData);

        void Error(String str);
    }

}