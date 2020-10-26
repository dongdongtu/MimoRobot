package com.chance.mimorobot.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chance.mimorobot.activity.MainActivity;
import com.hnj.dp_nusblist.USBFactory;

import java.util.HashMap;
import java.util.Iterator;

import static androidx.core.content.ContextCompat.getSystemService;


public class PrintManager {
    private final int CONNECTRESULT= 0x05;
    private static PrintManager printManager;
    private UsbManager mUsbManager ;

    public  USBFactory usbfactory;

    private Handler mHandler;
    private boolean t=false;
    private Context mContext;
    private boolean accredit=false;
    private boolean isconnecting=false;//是否正在搜索连接中
    private boolean isfind=false;//是否找到对应打印机
    private String VID,PID;
    private Activity normalprintactivity=null;
    private HashMap<String, UsbDevice> deviceList;
    private Iterator<UsbDevice> deviceIterator ;
    public  boolean ispage=false;

    public static PrintManager newInstance(Context context) {
        if (printManager == null) {
            printManager = new PrintManager(context);
        }
        return printManager;

    }

    public PrintManager(Context context) {
        this.mContext=context;
        mHandler=new MHandler();
        usbfactory=USBFactory.getUsbFactory(mHandler);
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mHandler.sendEmptyMessageDelayed(2, 500);
    }

    class MHandler extends Handler {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECTRESULT: {
                    if(usbfactory!=null)
                    {
                        if(usbfactory.getconnectstate())
                        {
                            usbfactory.is_connectdevice();
                            mHandler.removeMessages(CONNECTRESULT);
                            mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
//							 Log.e("---------", "==============连接成功=======1111");
                        }else {
                            mHandler.removeMessages(2);
                            mHandler.sendEmptyMessageDelayed(2,1000);
                            Log.e("---------", "==============连接失败=======000");
                        }
                    }

                    break;
                }
                case 2:{
                    AutomaticConnection();//自动连接
                    break;
                }
                case USBFactory.CHECKPAGE_RESULT:{
                    if(msg.getData().getString("state").equals("1"))
                    {
                        ispage=true;
//							Toast_Util.ToastString(getApplicationContext(), "printer has paper");//打印机有纸

                    }else {
                        ispage=false;
//							Toast_Util.ToastString(getApplicationContext(), "printer has no paper");//打印机没有纸
                    }
                    break;
                }
                case USBFactory.PRINTSTATE:{
                    boolean printstate=msg.getData().getBoolean("printstate");
                    if(printstate)
                    {
//                        Toast_Util.ToastString(mContext.getApplicationContext(), "print completed");//打印完成
                    }else
                    {
//                        Toast_Util.ToastString(getApplicationContext(), "Print failure");//打印失败
                    }

                    break;
                }

            }
        }

        public boolean isNoPaper(){
            return ispage;
        }

        public USBFactory getUSBFactory(){
            return usbfactory;
        }

        private void AutomaticConnection() {
            int mvid=SharedPreferencesManager.newInstance().getVID();//获取上次连接成功打印机的VID
            int mpid=SharedPreferencesManager.newInstance().getPID();//获取上次连接成功打印机的PID
            if(isconnecting||usbfactory.connectstate||mvid==0)
            {
                return;
            }
            isfind=false;
            deviceList = mUsbManager.getDeviceList();
            deviceIterator = deviceList.values().iterator();
            if (deviceList.size() > 0) {
                // 初始化选择对话框布局，并添加按钮和事件
                while (deviceIterator.hasNext()) { // 这里是if不是while，说明我只想支持一种device
                    final UsbDevice device = deviceIterator.next();
                    device.getInterfaceCount();
                    int vid=device.getVendorId();
                    int pid=device.getProductId();

//				if(vid==4070&&pid==33054)//判断是不是打印机编号
                    Log.e("============", mvid+"======VID======"+vid);
//				Log.e("============", mpid+"======PID======"+pid);
                    if(vid==mvid&&pid==mpid)//判断是不是打印机编号
//				if(vid==4070&&pid==33054)//判断是不是打印机编号
                    {
                        isfind=true;
                        PendingIntent mPermissionIntent ;
                        if(normalprintactivity!=null)
                        {
                            mPermissionIntent = PendingIntent.getBroadcast(normalprintactivity, 0, new Intent(mContext.getApplicationInfo().packageName), 0);
                        }else {
                            mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(mContext.getApplicationInfo().packageName), 0);
                        }
                        Log.e("============", mpid+"=====对比成功====="+pid);
                        if (!mUsbManager.hasPermission(device)) {
                            if(!accredit)
                            {
                                mUsbManager.requestPermission(device,mPermissionIntent);
                                accredit=true;
                            }
//						Log.e("============", mpid+"====6666666666666666====="+pid);
                            mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
                        } else {
                            accredit=false;
                            isconnecting=true;
                            t=usbfactory.connectUsb(mUsbManager, device);
                            isconnecting=false;
                            if(t)
                            {
                                VID=mvid+"";
                                PID=mpid+"";
                                mHandler.removeMessages(2);

                                //连接成功在这里调用打印的方法
                            }else {
//                                usbconnt_tv.setText(R.string.unconnected);
                            }
                            mHandler.removeMessages(CONNECTRESULT);
                            mHandler.sendEmptyMessageDelayed(CONNECTRESULT, 2000);
                        }
                    }
                }
                if(!isfind)//没有搜索到对应打印机继续搜索
                {
                    mHandler.removeMessages(2);
                    mHandler.sendEmptyMessageDelayed(2, 1000);
                }
            }
        }
    }
}
