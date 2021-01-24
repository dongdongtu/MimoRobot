package com.chance.mimorobot;

import android.Manifest;
import android.app.Activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.VersionInfo;
import com.arcsoft.face.enums.RuntimeABI;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.chance.mimorobot.activity.FaceDiscernActivity;
import com.chance.mimorobot.activity.MainActivity;
import com.chance.mimorobot.activity.TempActivity;
import com.chance.mimorobot.callback.OnSerialDataCallBack;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.control.SpeechModule;
import com.chance.mimorobot.db.entity.FaceEntity;
import com.chance.mimorobot.helper.RobotFunctionSettings;
import com.chance.mimorobot.manager.ActionManager;
import com.chance.mimorobot.manager.SerialControlManager;
import com.chance.mimorobot.manager.SharedPreferencesManager;
import com.chance.mimorobot.manager.SlamManager;
import com.chance.mimorobot.manager.VocalSpeakInterface;
import com.chance.mimorobot.manager.VocalSpeakManager;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.model.GetSemanticModel;
import com.chance.mimorobot.model.InitModel;
import com.chance.mimorobot.model.InitRequest;
import com.chance.mimorobot.model.SemanticAciton;
import com.chance.mimorobot.model.SemanticDialog;
import com.chance.mimorobot.model.SemanticModel;
import com.chance.mimorobot.mqtt.model.BaseMqttModel;
import com.chance.mimorobot.mqtt.model.HeartBeat;
import com.chance.mimorobot.mqtt.model.MapUpdateModel;
import com.chance.mimorobot.mqtt.model.MqttActionModel;
import com.chance.mimorobot.mqtt.model.MqttUpdateModel;
import com.chance.mimorobot.mqtt.model.ReceiveActionModel;
import com.chance.mimorobot.mqtt.model.ReturnModel;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.service.CustomStatusBarService;
import com.chance.mimorobot.service.DownLoadMapService;
import com.chance.mimorobot.service.FaceUpdateService;
import com.chance.mimorobot.slam.event.ConnectedEvent;
import com.chance.mimorobot.slam.event.RobotPoseGetEvent;
import com.chance.mimorobot.slam.event.RobotStatusGetEvent;
import com.chance.mimorobot.slam.mapview.utils.RadianUtil;
import com.chance.mimorobot.slam.slamware.SlamwareAgent;
import com.chance.mimorobot.statemachine.RobotStatus;
import com.chance.mimorobot.statemachine.StateMachineManager;

import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.chance.mimorobot.update.CustomUpdateParser;
import com.chance.mimorobot.update.OKHttpUpdateHttpService;
import com.chance.mimorobot.utils.CoverLoader;
import com.chance.mimorobot.utils.HardwareInfo;
import com.chance.mimorobot.utils.ScreenUtils;
import com.chance.mimorobot.utils.SystemUtils;
import com.chance.mimorobot.widget.CustomStatusBarView;
import com.chance.mimorobot.widget.neteasedisc.service.MusicService;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.robot.Rotation;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate._XUpdate;
import com.xuexiang.xupdate.entity.DownloadEntity;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnInstallListener;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.utils.UpdateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cn.chuangze.robot.aiuilibrary.AIUIWrapper;
import cn.chuangze.robot.aiuilibrary.listener.STTEventCallBack;
import cn.chuangze.robot.aiuilibrary.params.SpeechParams;
import es.dmoral.toasty.Toasty;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener;
import tv.danmaku.ijk.media.exo2.ExoSourceManager;

import static com.chance.mimorobot.constant.Constant.ACTION_DOWNLOAD_CODE;
import static com.chance.mimorobot.constant.Constant.ACTION_DOWNLOAD_EXTRA;
import static com.chance.mimorobot.constant.Constant.ACTION_START_CODE;

import static com.chance.mimorobot.mqtt.MqttCoreService.ACTION_REV_DATA;
import static com.chance.mimorobot.mqtt.MqttCoreService.ACTION_SEND_DATA;
import static com.chance.mimorobot.mqtt.MqttCoreService.EXTRAS_REV_DATA;
import static com.chance.mimorobot.mqtt.MqttCoreService.EXTRAS_SEND_DATA;

public class MyApplication extends Application implements OnSerialDataCallBack, STTEventCallBack {

    private final String TAG = MyApplication.class.getSimpleName();
    public LocationClient mLocationClient = null;
//    private MyLocationListener myListener = new MyLocationListener();
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文第四步的说明
    private StateMachineManager stateMachineManager;
    private Gson gson = new Gson();
    private Activity currentActivity;
    private VocalSpeakInterface vocalSpeakInterface;   //管理语音合成
    private AIUIWrapper aiuiWrapper;
    private SerialControlManager serialControlManager;
    private SemanticDialog mSemanticDialog;
//    private SpeechModule speechControl;

    public boolean isWakeUp() {
        return isWakeUp;
    }

    public void setWakeUp(boolean wakeUp) {
        isWakeUp = wakeUp;
    }

    private boolean isWakeUp = false;
    private Intent serviceIntent;
    private CustomStatusBarView customStatusBarView;
    private SlamwareAgent mAgent;
    private static MyApplication mApplication;
    private Disposable disposableHeat;

    public static Location location;

    public boolean isActoin() {
        return isActoin;
    }

    public void setActoin(boolean actoin) {
        isActoin = actoin;
        Log.e(TAG, "setActoin:");

        if(currentActivity instanceof BaseActivity){
            ((BaseActivity) currentActivity).hideStop();
            Log.e(TAG, "currentActivity hideStop:");
        }
        if(!(currentActivity instanceof MainActivity)){
            Output.navigatorActivity(MainActivity.class);
        }
    }

    protected SharedPreferences sharedPreferences;
    private boolean isActoin = false;

    @Override
    public void onCreate() {
        super.onCreate();
//        initLocation();
        mApplication = this;
        location = new Location();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        ActionManager.getInstance().init(getApplicationContext());
        stateMachineManager = StateMachineManager.getInstance();
        stateMachineManager.init();
        vocalSpeakInterface = VocalSpeakManager.getInstance();
        vocalSpeakInterface.onCreate(this);
        aiuiWrapper = AIUIWrapper.getInstance(getApplicationContext());
        aiuiWrapper.setInteract(1 * Globle.ONE_MINUTE, 5000);
        if (sharedPreferences.getBoolean(getString(R.string.pref_key_speech), true)) {
            aiuiWrapper.speechParams(true);
        } else {
            aiuiWrapper.speechParams(false);
        }
        serialControlManager = SerialControlManager.newInstance();
        serialControlManager.setDeviceEventListener(this);
        serialControlManager.setFace(7);
        serialControlManager.lightREDControl();
        setSpeakerParams();


        Utils.init(this);
        RobotFunctionSettings.getInstance().initSettings(getApplicationContext());
        aiuiWrapper.setSTTEventCallBack(this);
//        initDir();
        ScreenUtils.init(this);
        CoverLoader.getInstance().init(this);
        serviceIntent = new Intent(MyApplication.this, CustomStatusBarService.class);
        startService(serviceIntent);
        Toasty.Config.getInstance()
                .setErrorColor(getResources().getColor(R.color.toast_error))
                .setInfoColor(getResources().getColor(R.color.toast_info))
                .setSuccessColor(getResources().getColor(R.color.toast_success))
                .setWarningColor(getResources().getColor(R.color.toast_warning))
                .apply();

        initUpdate();
        EventBus.getDefault().register(this);
        SharedPreferencesManager.newInstance().initShare(this);

        ApplicationInfo applicationInfo = getApplicationInfo();
        Log.i(TAG, "onCreate: " + applicationInfo.nativeLibraryDir);

//        initApi();·
        register();
    }

    private void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_START_CODE);
        intentFilter.addAction(ACTION_REV_DATA);
        intentFilter.addAction(ACTION_DOWNLOAD_CODE);
        intentFilter.addAction("ACTION_STOP");
        registerReceiver(broadcastReceiver, intentFilter);

        ExoSourceManager.setExoMediaSourceInterceptListener(new ExoMediaSourceInterceptListener() {
            @Override
            public MediaSource getMediaSource(String dataSource, boolean preview, boolean cacheEnable, boolean isLooping, File cacheDir) {
                //如果返回 null，就使用默认的
                return null;
            }

            /**
             * 通过自定义的 HttpDataSource ，可以设置自签证书或者忽略证书
             * demo 里的 GSYExoHttpDataSourceFactory 使用的是忽略证书
             * */
            @Override
            public HttpDataSource.BaseFactory getHttpDataSourceFactory(String userAgent, @Nullable TransferListener listener, int connectTimeoutMillis, int readTimeoutMillis, boolean allowCrossProtocolRedirects) {
                //如果返回 null，就使用默认的
                return null;
            }
        });
    }


    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }


    public void setStatusBarView(CustomStatusBarView customStatusBarView) {
        this.customStatusBarView = customStatusBarView;
    }

    public void setBatteryChange(int battery) {
        customStatusBarView.setBatteryChange(battery);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

//        option.setScanSpan(10*60*1000);
        option.setScanSpan(0);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        option.setNeedNewVersionRgc(true);
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        //注册监听函数
        mLocationClient.start();

    }


    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(broadcastReceiver);
        mLocationClient.stop();
        aiuiWrapper.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置发言人参数
     */
    private void setSpeakerParams() {
        SpeechParams speechParams = new SpeechParams();
        String speaker = sharedPreferences.getString(getString(R.string.pref_key_speaker), getString(R.string.default_speaker_params));
        int speed = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_key_tts_speed), "50"));
        int pitch = Integer.parseInt(sharedPreferences.getString(getString(R.string.pref_key_tts_pitch), "50"));
        int volume = 100;
        speechParams.init(speaker, speed, pitch, volume);
        aiuiWrapper.setSpeechParams(speechParams);
    }

    @Override
    public void wakeup(int angel) {
        Log.e(TAG, "angel = " + angel);

//        serialControlManager.setBeam(getBeamFromAngle(angel));
        Log.e(TAG, "wakeUp = " + (getBeamFromAngle(angel) - 1));

        aiuiWrapper.startTTS("你好", null);
        // 如果开启唤醒转身
        if (sharedPreferences.getBoolean(getString(R.string.pref_key_wakeup_rotate), true)) {
            serialControlManager.setBeam(1);
            aiuiWrapper.wakeUp(0);
            int wakeAngle = angel > 180 ? -(angel - 360) : -angel;
            if (RobotStateMachine.CHARGE_STATE != stateMachineManager.getCurrentState()) {
                Log.e("wakeUp","wakeUp"+wakeAngle);
                SlamManager.getInstance().rotate(new Rotation((float) Math.toRadians(wakeAngle)));
            }
        } else {
            serialControlManager.setBeam(getBeamFromAngle(angel));
            aiuiWrapper.wakeUp((getBeamFromAngle(angel) - 1));
        }
    }

    @Override
    public void temp(float x, float y) {
        if (currentActivity instanceof TempActivity) {
            ((TempActivity) currentActivity).setTemp(x / 100);
        }
    }

    @Override
    public void onResultAIUI(String result) {
        Log.e(TAG, "onResultAIUI");
        if (Globle.robotId.equals("-1")) {
            return;
        }


        Log.e(TAG, "onResultAIUI1");
        requestAIUIServer(result);
//        if ((currentActivity instanceof MainActivity) && !TextUtils.isEmpty(aiuiResultEntity.getText())) {
//            ((MainActivity) currentActivity).setSpeakText(aiuiResultEntity.getText());
//        }
    }

    @Override
    public void startCheck(String result) {

    }

    @Override
    public void onResult(String result) {

    }

    @Override
    public void onError(int arg1, String arg2) {

    }

    public static Context getInstance() {
        return mApplication.getApplicationContext();
    }

    @Override
    public void onSleep(int arg1) {
        isWakeUp = false;
        if (currentActivity instanceof MainActivity) {
            Log.e(TAG, "onSleep");
            ((MainActivity) currentActivity).stopWave();
            if (sharedPreferences.getBoolean(getString(R.string.pref_key_speech), true)) {
                ((MainActivity) currentActivity).setHelloText1();
                ((MainActivity) currentActivity).setSpeakText("您可以对我说:'你叫什么名字？''今天天气如何？'");
            } else {
                ((MainActivity) currentActivity).setHelloText2();
                ((MainActivity) currentActivity).setSpeakText("您可以对我说:'你叫什么名字？''今天天气如何？'");
            }
        }
        if (!isActoin && sharedPreferences.getBoolean(getString(R.string.pref_key_speech), true)) {
            aiuiWrapper.startTTS("进入休眠，有事再来叫我啊", null);
        }

    }

    @Override
    public void onVolume(int db) {

    }

    @Override
    public void onCmdReturn(int arg1, int arg2, String info) {

    }

    @Override
    public void onState(int arg1) {

    }

    @Override
    public void onPreSleep() {

    }

    @Override
    public void onConnectServer(String uid) {

    }

    @Override
    public void onDisconnectServer() {

    }

    @Override
    public void onWakeUp(int angle) {
        Log.e(TAG, "isWakeUp = " + isWakeUp);
        if (isWakeUp == false) {
            isWakeUp = true;
        }

        if ((currentActivity instanceof MainActivity)) {
            Log.e(TAG, "onWakeUp");
            ((MainActivity) currentActivity).startWave();
        }
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            String adcode = location.getAdCode();    //获取adcode
            String town = location.getTown();    //获取乡镇信息

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            Log.e(TAG, "latitude = " + latitude + " ,longitude = " + longitude + " 地址" + addr);
            int errorCode = location.getLocType();
            Globle.location = location;
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        }
    }


    /**
     * 根据唤醒角度计算麦克风波束号
     */
    private int getBeamFromAngle(int angle) {
        int beam = 1;
        if (angle > 30 && angle <= 90) {
            beam = 2;
        } else if (angle > 90 && angle <= 150) {
            beam = 3;
        } else if (angle > 150 && angle <= 210) {
            beam = 4;
        } else if (angle > 210 && angle <= 270) {
            beam = 5;
        } else if (angle > 270 && angle <= 330) {
            beam = 6;
        }
        return beam;
    }

    /**
     * 初始化程序目录
     */
    public void initDir() {
        String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        Globle.SDCARD_PATH = SDCARD_PATH;

        File file = new File(SDCARD_PATH + Globle.ROOT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        Globle.ROOT_PATH = file.getAbsolutePath();

        file = new File(Globle.ROOT_PATH + Globle.TEMP_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        Globle.TEMP_PATH = file.getAbsolutePath();

        file = new File(Globle.ROOT_PATH + Globle.MAP_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        Globle.MAP_PATH = file.getAbsolutePath();

        file = new File(Globle.ROOT_PATH + Globle.FACE_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        Globle.FACE_PATH = file.getAbsolutePath();

        file = new File(Globle.ROOT_PATH + Globle.PIC_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        Globle.PIC_PATH = file.getAbsolutePath();

        file = new File(Globle.ROOT_PATH + Globle.DOWNLOAD_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        Globle.DOWNLOAD_PATH = file.getAbsolutePath();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent.getAction().equals(ACTION_REV_DATA)) {
                String data = intent.getStringExtra(EXTRAS_REV_DATA);
                try {
                    JSONObject object = new JSONObject(data);
                    if (object.getString("from").equals("platform")) {
                        switch (object.getInt("code")) {
                            case 1:

                                break;
                            case 2:
                                ReceiveActionModel mqttActionModel = new Gson().fromJson(object.getJSONObject("data").toString(), ReceiveActionModel.class);
                                returnmessage(2, object.getString("CodeName"));
                                break;
                            case 3:
                                JSONObject jsonObject = new JSONObject(object.getJSONObject("data").toString());
                                aiuiWrapper.startTTS(jsonObject.optString("SayWord"), null);
                                returnmessage(3, object.getString("CodeName"));
                                break;
                            case 4:
                                MapUpdateModel mapUpdateModel = new Gson().fromJson(object.getJSONObject("data").toString(), MapUpdateModel.class);
                                SharedPreferencesManager.newInstance().setMapID(mapUpdateModel.getMapid());
//                            DownLoadMapService.startActionFoo(MainActivity.this, mapUpdateModel.getMapSourceUrl());
                                Intent intentsevice = new Intent(MyApplication.this, DownLoadMapService.class);
                                intentsevice.setAction("sevice.action.FOO");
                                intentsevice.putExtra("sevice.extra.PARAM1", mapUpdateModel.getMapSourceUrl());
                                startService(intentsevice);
                                returnmessage(4, object.getString("CodeName"));
//                            if (disposabletime != null)
//                                disposabletime.dispose();
                                break;
                            case 7:
                                SlamwareAgent.getNewInstance().goHome();
                                returnmessage(7, object.getString("CodeName"));
                                break;
                            case 8:
                                XUpdate.newBuild(currentActivity)
                                        .updateUrl("http://47.110.149.187:10031/api/RobotInit/GetNewAndroidVesion?RobotNo=" + Globle.robotId + "&IsTest=0")
                                        .updateParser(new CustomUpdateParser(SystemUtils.getAppVersionCode(currentActivity))) //设置自定义的版本更新解析器
                                        .update();
                                returnUpdateMessage(8, object.getString("CodeName"), object.getJSONObject("data").getInt("Versionid"));
                                break;
                            case 10:
                                if (!(currentActivity instanceof FaceDiscernActivity)) {
                                    Log.e(TAG,object.getJSONObject("data").toString());
                                    FaceEntity faceEntity = new Gson().fromJson(object.getJSONObject("data").toString(), FaceEntity.class);
                                    updateFaceService("add", faceEntity);
                                    returnmessage(10, object.getString("CodeName"), faceEntity.getFaceid());
                                }
                                break;
                            case 11:

                                break;
                            case 12:
                                if (!(currentActivity instanceof FaceDiscernActivity)) {
                                    Log.e(TAG,data);
                                    FaceEntity faceEntity1 = new Gson().fromJson(object.getJSONObject("data").toString(), FaceEntity.class);
                                    updateFaceService("delete", faceEntity1);
                                    returnmessage(12, object.getString("CodeName"), faceEntity1.getFaceid());
                                }
                                break;
                            case 13:
                                isActoin = true;
                                VocalSpeakManager.getInstance().sleep();
                                if (currentActivity instanceof BaseActivity) {
                                    ((BaseActivity) currentActivity).showStop();
                                }
                                SemanticAciton semanticAciton = new Gson().fromJson(object.getJSONObject("data").toString(), SemanticAciton.class);
                                ActionManager.getInstance().startAction(semanticAciton.getNextActionLists());
                                break;

                        }
                    }
                    if (object.getString("from").equals("pad")) {
                        JSONObject object1 = new JSONObject(data);
                        JSONObject mqttdata=new JSONObject(object1.optString("data"));
                        Log.e(TAG,object1.optString("data"));
                        switch (mqttdata.getInt("code")) {
                            case 1:
                                aiuiWrapper.sleep();
                                break;
                            case 2:
                                aiuiWrapper.startTTS(mqttdata.optString("data"), null);
                                break;
                            case 3:
                                int i = Integer.valueOf(mqttdata.getString("data"));
                                SerialControlManager.newInstance().setFace(i);
                                break;
                            case 4:
                                aiuiWrapper.speechParams(!sharedPreferences.getBoolean(getString(R.string.pref_key_speech), true));
                                break;
                            case 5:
                                Intent intentstop=new Intent("ACTION_STOP");
                                sendBroadcast(intentstop);
                                ActionManager.getInstance().cancelAction();
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(MusicService.ACTION_OPT_MUSIC_PAUSE));

                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (ACTION_DOWNLOAD_CODE.equals(intent.getAction())) {
                String mapPath = intent.getStringExtra(ACTION_DOWNLOAD_EXTRA);
                Log.e(TAG, "mapPath = " + mapPath);
                SharedPreferencesManager.newInstance().setMapPath(mapPath);
                SlamManager.getInstance().setMap(mapPath);
            } else if ("ACTION_STOP".equals(intent.getAction())) {
                if (currentActivity instanceof BaseActivity) {
                    ((BaseActivity) currentActivity).hideStop();
                }

                isActoin = false;
                Log.e(TAG, "ACTION_STOP = " + intent.getAction());
            }
        }


    };

    /**
     * 初始化版本更新的配置
     */
    private void initUpdate() {
        XUpdate.get()
                .isWifiOnly(false)     //默认设置只在wifi下检查版本更新
                .isGet(true)          //默认设置使用get请求检查版本
                .isAutoMode(false)    //默认设置非自动模式，可根据具体使用配置
                .param("VersionCode", UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
                .param("AppKey", getPackageName())
                .setApkCacheDir("/storage/emulated/0/Robot/download")     // 设置下载的缓存目录
                .debug(true)
                .setOnUpdateFailureListener(new OnUpdateFailureListener() {
                    @Override
                    public void onFailure(UpdateError error) {
                        if (error.getCode() != UpdateError.ERROR.CHECK_NO_NEW_VERSION) {          //对不同错误进行处理
                            Log.e("HTTP", "更新出错:" + error.getMessage());
                        }
                    }
                })
                .setOnInstallListener(new OnInstallListener() {
                    @Override
                    public boolean onInstallApk(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity) {
                        Log.e("HTTP", "更新" + apkFile.getAbsolutePath());
                        installApkNew(apkFile);
                        return true;
                    }

                    @Override
                    public void onInstallApkSuccess() {
                        Log.e("HTTP", "更新成功");
                    }
                })
                .supportSilentInstall(true)                                     //设置是否支持静默安装，默认是true
                .setIUpdateHttpService(new OKHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
                .init(this);   //这个必须初始化
    }

    public void returnmessage(int code, String s) {
        BaseMqttModel baseModel = new BaseMqttModel<ReturnModel>();
        baseModel.setCode(code);
        baseModel.setFrom(Globle.robotId);
        baseModel.setTo("platform");
        baseModel.setMd5("");
        baseModel.setCodeName(s);
        ReturnModel returnModel = new ReturnModel();
        returnModel.setReturnCode(1);
        returnModel.setReturnMessage("收到");
        baseModel.setData(returnModel);
        sendMqttMessage(new Gson().toJson(baseModel));
    }

    public void returnUpdateMessage(int code, String s, int versionid) {
        BaseMqttModel baseModel = new BaseMqttModel<ReturnModel>();
        baseModel.setCode(code);
        baseModel.setFrom(Globle.robotId);
        baseModel.setTo("platform");
        baseModel.setMd5("");
        baseModel.setCodeName(s);
        ReturnModel returnModel = new ReturnModel();
        returnModel.setReturnCode(1);
        returnModel.setReturnMessage("收到");
        returnModel.setVersionid(versionid);
        baseModel.setData(returnModel);
        sendMqttMessage(new Gson().toJson(baseModel));
    }

    public void returnmessage(int code, String s, Long faceid) {
        BaseMqttModel baseModel = new BaseMqttModel<ReturnModel>();
        baseModel.setCode(code);
        baseModel.setFrom(Globle.robotId);
        baseModel.setTo("platform");
        baseModel.setMd5("");
        baseModel.setCodeName(s);
        ReturnModel returnModel = new ReturnModel();
        returnModel.setFaceid(faceid);
        returnModel.setReturnCode(1);
        returnModel.setReturnMessage("收到");
        baseModel.setData(returnModel);
        sendMqttMessage(new Gson().toJson(baseModel));
    }


    /**
     * 请求AIUI服务器进行语义解析
     *
     * @param
     */
    public void requestAIUIServer(String result) {
        Log.e(TAG, "requestAIUIServer");
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), text.getBytes());
        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
        }.getType();
        AiuiResultEntity aiuiResultEntity = gson.fromJson(result, aiuiResultEntityType);
        if (TextUtils.isEmpty(aiuiResultEntity.getText())) {
            return;
        }

        if (aiuiResultEntity.getText().equals("嗯") || aiuiResultEntity.getText().equals("啊") || aiuiResultEntity.getText().equals("咦") || aiuiResultEntity.getText().equals("哎") || aiuiResultEntity.getText().equals("哦")) {
            return;
        }
        GetSemanticModel getSemanticModel = new GetSemanticModel();
        if (mSemanticDialog != null) {
            getSemanticModel.setDialog(mSemanticDialog);
        }
        if (currentActivity instanceof MainActivity) {
            ((MainActivity) currentActivity).setSpeakText(aiuiResultEntity.getText());
        }
        getSemanticModel.setQueryText(aiuiResultEntity.getText());
        getSemanticModel.setRobotNo(Globle.robotId);
        ApiManager.getInstance().getRobotServer().getSemantic(getSemanticModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SemanticModel>() {
                    @Override
                    public void accept(SemanticModel semanticModel) {
                        Log.i(TAG, "Api返回数据:" + gson.toJson(semanticModel));
                        if (semanticModel.isEnd()) {
                            mSemanticDialog = null;
                        } else {
                            mSemanticDialog = semanticModel.getDialog();
                        }
                        if (semanticModel.getCode() == 200) {
                            if (semanticModel.getNext().getNextActionLists().size() > 0) {
                                Log.e(TAG, "START ACTION");
                                aiuiWrapper.startTTS(semanticModel.getSayWord(), null);
                                isActoin = true;
                                VocalSpeakManager.getInstance().sleep();
                                if (currentActivity instanceof BaseActivity) {
                                    ((BaseActivity) currentActivity).showStop();
                                }
                                ActionManager.getInstance().startAction(semanticModel.getNext().getNextActionLists());
                            } else {
                                aiuiWrapper.startTTS(semanticModel.getSayWord(), null);
                            }
                        } else {
                            StateMachineManager.getInstance().inputAiuiResult(result);
                        }
                        if (!sharedPreferences.getBoolean(getString(R.string.pref_key_speech), true)) {
                            ((MainActivity) currentActivity).setHelloText2();
                            ((MainActivity) currentActivity).setSpeakText("您可以对我说:'你叫什么名字''今天天气如何？'");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG, "Api请求异常:" + throwable.toString());
//                        requestPostFromTuling(text);
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RobotStatusGetEvent event) {
        setBatteryChange(event.getBatteryPercentage());
        Globle.charge = event.getBatteryPercentage();
        if (event.isCharging()) {
            stateMachineManager.inputCharge(true);
            RobotStatus.getInstance().setCharging(event.isCharging());
        } else {
            stateMachineManager.inputCharge(false);
            int chargeValue = sharedPreferences.getInt(getString(R.string.pref_key_auto_charge_value), 20);
            if (sharedPreferences.getBoolean(getString(R.string.pref_key_auto_charge), false)) {
                if (event.getBatteryPercentage() < chargeValue && event.getBatteryPercentage() > 0) {
                    Log.e(TAG, "RobotStatusGetEvent = " + event.getBatteryPercentage());
                    if (!isActoin) {
                        SlamManager.getInstance().goHome();
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectedEvent event) {
        Flowable.interval(10, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                SlamwareAgent.getNewInstance().getRobotStatus();
            }
        });
        Flowable.interval(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                SlamwareAgent.getNewInstance().getRobotPose();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RobotPoseGetEvent event) {
        Pose pose = event.getPose();
        if (pose != null) {
//            String s = String.format("机器位姿  [%.2f, %.2f, %.2f]", pose.getX(), pose.getY(), RadianUtil.toAngel(pose.getYaw()));
//            mRobotLocation.setText(s);
            location.setX(pose.getX());
            location.setY(pose.getY());
            location.setZ(pose.getYaw());
        }
    }


    public void updateFaceService(String type, FaceEntity faceEntity) {
        Intent intent = new Intent(getApplicationContext(), FaceUpdateService.class);
        intent.putExtra("type", type);
        intent.putExtra("message", faceEntity);
        startService(intent);
    }

    public void sendMqttMessage(String data) {
        Intent intent = new Intent(ACTION_SEND_DATA);
        intent.putExtra(EXTRAS_SEND_DATA, data);
        sendBroadcast(intent);
    }


    //安装apk
    protected void installApkNew(File file) {
        //执行动作
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判读版本是否在7.0以上
            uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

}
