package com.chance.mimorobot.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.chance.mimorobot.MyApplication;
import com.chance.mimorobot.R;
import com.chance.mimorobot.constant.Constant;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.statemachine.RobotStatus;
import com.chance.mimorobot.widget.CustomStatusBarView;
import com.chance.mimorobot.widget.StatusBar;

import java.util.Calendar;

import io.reactivex.disposables.Disposable;


/**
 * 自定义状态栏
 */
public class CustomStatusBarService extends StatusBarService implements CustomStatusBarView {

    private String TAG = "CustomStatusBarService";
    private ViewHolder mHolder = new ViewHolder();
    private int hour;
    private int minute;
    private int year;
    private int month;
    private int day;
    private IntentFilter timeFilter;
    private final static int totalBattery = 100;    //电量的总数
    private final static int batteryIconLenthDp = 34;   //电量图标长度 单位dp
    private int[] WifiRes = {R.mipmap.icon_wifi_bar_1, R.mipmap.icon_wifi_bar_2, R.mipmap.icon_wifi_bar_3, R.mipmap.icon_wifi_bar_4};

    private final static int MORNING_RES = R.string.time_am;
    private final static int AFTERNOON_RES = R.string.time_pm;
    private final static int COLON_RES = R.string.time_colon;

    private Disposable rxCloseDisposable;
    private Disposable rxCloseClockDisposable;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApplication) getApplication()).setStatusBarView(this);
        init();
        initTime();
        registerReceiver();
    }


    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }


    private void initTime() {
        updateHourAndMinute();
    }

    private void init() {
        timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);           //每走一分钟接收广播
        registerReceiver(timeReceiver, timeFilter);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_CHANGE_TIME_FORMAT);       //监听显示时间的格式是否变化
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);    //监听wifi状态变化
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);          //监听wifi连接强度变化
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); //监听网络变化
        filter.addAction(Constant.ACTION_HIDE_STATUS_BAR);          //隐藏STATUS_BAR
        filter.addAction(Constant.ACTION_SHOW_STATUS_BAR);          //显示STATUS_BAR
        registerReceiver(Receiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(timeReceiver);
        unregisterReceiver(Receiver);
    }

    @Override
    protected int getStatusBarLayoutId() {
        //Xml创建的状态栏
        return R.layout.status_bar;
    }

    @Override
    protected void initStatusBar(StatusBar statusBar) {
        //查找从Xml加载的状态栏
        mHolder.ivBattery = (ImageView) statusBar.findViewById(R.id.iv_battery);
        mHolder.ivWifiIcon = (ImageView) statusBar.findViewById(R.id.iv_wifi_icon);
        mHolder.tvBattery = (TextView) statusBar.findViewById(R.id.tv_battery);
        mHolder.tvTime = (TextView) statusBar.findViewById(R.id.tv_time);
        mHolder.rlStatusBar = (RelativeLayout) statusBar.findViewById(R.id.rl_status_bar);
    }

    @Override
    protected int getStatusBarGravity() {
        return Gravity.TOP;
    }

    private BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH) + 1;
                day = c.get(Calendar.DAY_OF_MONTH);

                changeTimeFormatResult();
                checkAlarmClock();
            }
        }
    };

    private void changeTimeFormatResult() {
        String timeString = "";
        String minuteString = "" + minute;
        if (minute < 10) {
            minuteString = "0" + minute;
        }

        if (hour >= 0 && hour < 12) {
            timeString = hour + getString(COLON_RES) + minuteString + getString(MORNING_RES);
        } else {
            timeString = ((hour - 12) == 0 ? hour : hour - 12) + getString(COLON_RES) + minuteString + getString(AFTERNOON_RES);
        }

        mHolder.tvTime.setText(timeString);
    }


    private BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                WifiStateChange(intent);
            } else if (WifiManager.RSSI_CHANGED_ACTION.equals(intent.getAction())) {
                WifiRSSIChange();
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetStateChange(intent);
            } else if (Constant.ACTION_HIDE_STATUS_BAR.equals(intent.getAction())) {
                mHolder.rlStatusBar.setVisibility(View.INVISIBLE);
            } else if (Constant.ACTION_SHOW_STATUS_BAR.equals(intent.getAction())) {
                mHolder.rlStatusBar.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 电量变化
     *
     * @param batteryNum
     */
    @Override
    public void setBatteryChange(int batteryNum) {
        ViewGroup.LayoutParams para = mHolder.ivBattery.getLayoutParams();

        //得到系统当前电量
        String Batterystring = batteryNum + "%";
        para.width = (batteryNum * dip2px(CustomStatusBarService.this, batteryIconLenthDp)) / totalBattery;//修改宽度
        mHolder.ivBattery.setLayoutParams(para);
        mHolder.tvBattery.setText(Batterystring);

        if (RobotStatus.getInstance().isCharging()) {
            //充电时电量变为绿色
            para.width = dip2px(CustomStatusBarService.this, batteryIconLenthDp);
            mHolder.ivBattery.setLayoutParams(para);
            mHolder.ivBattery.setBackgroundResource(R.mipmap.icon_battery_charging);
        } else {
            mHolder.ivBattery.setBackgroundResource(R.mipmap.icon_battery);
            if (batteryNum < 20) {
                //低电量时电量变为红色
                mHolder.ivBattery.setBackgroundResource(R.mipmap.icon_low_power);
            }
        }
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 网络状态变化
     *
     * @param intent
     */
    private void NetStateChange(Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
            mHolder.ivWifiIcon.setVisibility(View.INVISIBLE);
        } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
            mHolder.ivWifiIcon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * wifi强度变化
     */
    private int wifiLastLevel = -1;

    private void WifiRSSIChange() {
        int level = Math.abs(((WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE)).getConnectionInfo().getRssi());
        level = WifiManager.calculateSignalLevel(level, 4);
        Log.i("RSSI_CHANGED_ACTION", "changed level = " + level);
        if (level != wifiLastLevel) {
            wifiLastLevel = level;
            mHolder.ivWifiIcon.setImageResource(WifiRes[level]);
        }
    }

    /**
     * wifi状态变化
     *
     * @param intent
     */
    private void WifiStateChange(Intent intent) {
        int wifi_state = intent.getIntExtra("wifi_state", 0);
        switch (wifi_state) {
            case WifiManager.WIFI_STATE_DISABLING:
                mHolder.ivWifiIcon.setVisibility(View.INVISIBLE);
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                mHolder.ivWifiIcon.setVisibility(View.INVISIBLE);
                break;
            case WifiManager.WIFI_STATE_ENABLING:
                break;
            case WifiManager.WIFI_STATE_ENABLED:
                mHolder.ivWifiIcon.setVisibility(View.VISIBLE);
                break;
            case WifiManager.WIFI_STATE_UNKNOWN:
                break;
        }
    }


    static class ViewHolder {
        TextView tvTime;
        ImageView ivWifiIcon;
        ImageView ivBattery;
        TextView tvBattery;
        RelativeLayout rlStatusBar;
    }


    private void updateHourAndMinute() {
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        changeTimeFormatResult();
    }


    private void checkAlarmClock() {
//        List<AlarmBellModel> alarmBellModelList = RobotHotSettings.getInstance().getAlarmBellModelList();
//        if (alarmBellModelList == null || alarmBellModelList.size() == 0) {
//            return;
//        }
//        for (AlarmBellModel alarmBellModel : alarmBellModelList) {
//            Log.i("checkAlarmClock", alarmBellModel.getMonth() + "," + month + "," + day + "," + year);
//            if (alarmBellModel.getHour() == hour && alarmBellModel.getMinute() == minute) {
//                if (StringUtils.isEmpty(alarmBellModel.getRepeat()) || alarmBellModel.getRepeat().equals("0000000")) {
//                    //指定日期的闹钟 不可重复
//                    if (alarmBellModel.getYear() == year && alarmBellModel.getMonth() == month && alarmBellModel.getDay() == day) {
//                        runNoRepeatAlarm(alarmBellModel.getDrawStatus(), alarmBellModel);
//                        break;
//                    }
//                } else {
//                    if (alarmBellModel.checkWeekRepeat(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))) {
//                        runAlarmClock(alarmBellModel.getDrawStatus(), alarmBellModel);
//                        break;
//                    }
//                }
//            }
//
//        }
    }

//    private void runNoRepeatAlarm(int drawStatus, AlarmBellModel alarmBellModel) {
//        runAlarmClock(drawStatus, alarmBellModel);
//        rxCloseClockDisposable = new MyApiImpl(getApplicationContext()).switchClock(alarmBellModel.getalarmBellId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//
//                    }
//                });
//    }

//    private void runAlarmClock(int drawStatus, AlarmBellModel alarmBellModel) {
//        if (drawStatus == 1) {
//            HardwareManager.getInstance().switchDrawer(true);
//            rxCloseDisposable = Flowable.timer(15, TimeUnit.SECONDS)
//                    .subscribe(new Consumer<Long>() {
//                        @Override
//                        public void accept(Long aLong) throws Exception {
//                            HardwareManager.getInstance().switchDrawer(false);
//                        }
//                    });
//        }
//        Intent intent = new Intent(getApplicationContext(), AlarmBellActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (alarmBellModel.getIndication() != null && alarmBellModel.getIndication().length() > 0) {
//            intent.putExtra("indication", alarmBellModel.getIndication());
//        } else {
//            intent.putExtra("indication", "");
//        }
//        getApplicationContext().startActivity(intent);
//    }
}
