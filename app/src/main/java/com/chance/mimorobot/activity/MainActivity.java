package com.chance.mimorobot.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.face.ActiveFileInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.VersionInfo;
import com.arcsoft.face.enums.RuntimeABI;
import com.chance.mimorobot.BaseActivity;
import com.chance.mimorobot.MyApplication;
import com.chance.mimorobot.R;
import com.chance.mimorobot.adapter.ActionRecycleViewAdapter;
import com.chance.mimorobot.adapter.RoomRecycleViewAdapter;
import com.chance.mimorobot.constant.Constant;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.manager.SerialControlManager;
import com.chance.mimorobot.manager.SharedPreferencesManager;
import com.chance.mimorobot.manager.SlamManager;
import com.chance.mimorobot.model.InitModel;
import com.chance.mimorobot.model.InitRequest;
import com.chance.mimorobot.model.MapListResponse;
import com.chance.mimorobot.mqtt.MqttCoreService;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.retrofit.model.ActionItemModel;
import com.chance.mimorobot.retrofit.model.BaseModel;
import com.chance.mimorobot.retrofit.model.GetActionListResponse;
import com.chance.mimorobot.service.DownLoadMapService;
import com.chance.mimorobot.service.FaceInfoService;
import com.chance.mimorobot.service.FaceUpdateService;
import com.chance.mimorobot.slam.event.ConnectedEvent;
import com.chance.mimorobot.slam.event.ConnectionLostEvent;
import com.chance.mimorobot.slam.slamware.SlamwareAgent;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.update.CustomUpdateParser;
import com.chance.mimorobot.utils.HardwareInfo;
import com.chance.mimorobot.utils.SystemUtils;
import com.chance.mimorobot.widget.WaveView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuexiang.xupdate.XUpdate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.chuangze.robot.aiuilibrary.AIUIWrapper;
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

import static com.blankj.utilcode.util.ActivityUtils.getTopActivity;


public class MainActivity extends BaseActivity implements ActionRecycleViewAdapter.OnActionItemClick, RoomRecycleViewAdapter.OnActionRoomItemClick {

    private final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.tv_speak)
    TextView tvSpeak;
    @BindView(R.id.wave_speak)
    WaveView waveSpeak;
    @BindView(R.id.iv_iv_mic)
    ImageView IvMic;
    @BindView(R.id.action_list)
    RecyclerView actionList;
    @BindView(R.id.hello)
    TextView hello;
    @BindView(R.id.room_list)
    RecyclerView roomList;
    @BindView(R.id.video_1)
    TextView video1;
    @BindView(R.id.video_2)
    TextView video2;
    @BindView(R.id.room_detail)
    ImageView roomDetail;

    private boolean isFirst = true;
    private Intent intentService;
    TextView textView;
    @BindView(R.id.business)
    ImageView business;
    @BindView(R.id.face)
    ImageView face;
    @BindView(R.id.temperature)
    ImageView temperature;
//    @BindView(R.id.lineup)
//    ImageView lineup;

    @BindView(R.id.identify)
    ImageView identify;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    private AIUIWrapper aiuiWrapper;

    //    SpeechModule speechControl;
    boolean libraryExists = true;
    // Demo 所需的动态库文件
    private static final String[] LIBRARIES = new String[]{
            // 人脸相关
            "libarcsoft_face_engine.so",
            "libarcsoft_face.so",
            // 图像库相关
            "libarcsoft_image_util.so",
    };

    private SerialControlManager serialControlManager;

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    // 在线激活所需的权限
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };

    private int mapid = -1;

    private List<ActionItemModel> listData;
    private List<ActionItemModel> listRoomData;
    private ActionRecycleViewAdapter actionRecycleViewAdapter;
    private RoomRecycleViewAdapter roomRecycleViewAdapter;

    private Dialog showAlertDialog;

    private boolean IsFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        requestPermissions();
        SlamManager.getInstance().init(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter("INIT_MAP");
        intentFilter.addAction("DIALOG_DISPLAY");

        registerReceiver(broadcastReceiver, intentFilter);
        serialControlManager = SerialControlManager.newInstance();
        if (!libraryExists) {
            Log.e("TAG", getString(R.string.library_not_found));
        } else {
            VersionInfo versionInfo = new VersionInfo();
            int code = FaceEngine.getVersion(versionInfo);
            Log.i(TAG, "onCreate: getVersion, code is: " + code + ", versionInfo is: " + versionInfo);
        }
        libraryExists = checkSoFile(LIBRARIES);
//        ((MyApplication)getApplication()).initDir();
        SpannableStringBuilder style = new SpannableStringBuilder("请用'你好，安安'唤醒我");
        style.setSpan(new ForegroundColorSpan(Color.RED), 3, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        hello.setText(style);
        init();
    }

    private void init() {
//        ((MyApplication)getApplication()).initDir();
//        speechControl = SpeechModule.getInstance();
        showAlertDialog = new Dialog(this, R.style.Dialog_Fullscreen);
        showAlertDialog.setContentView(R.layout.dialog_main_show);
        showAlertDialog.setCancelable(true);
        showAlertDialog.setCanceledOnTouchOutside(true);


        listData = new ArrayList<>();
        actionRecycleViewAdapter = new ActionRecycleViewAdapter(listData, this);
        actionRecycleViewAdapter.setOnActionItemClick(this::onClick);
        actionList.setLayoutManager(new GridLayoutManager(this, 1));
        actionList.setAdapter(actionRecycleViewAdapter);

        listRoomData = new ArrayList<>();
        roomRecycleViewAdapter = new RoomRecycleViewAdapter(listRoomData, this);
        roomRecycleViewAdapter.setOnActionRoomItemClick(this::onRoomClick);
        roomList.setLayoutManager(new GridLayoutManager(this, 2));
        roomList.setAdapter(roomRecycleViewAdapter);
        aiuiWrapper = AIUIWrapper.getInstance(getApplicationContext());
        initApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            if (((MyApplication) getApplication()).isWakeUp()) {
                startWave();
            } else {
                stopWave();
                setSpeakText("您可以对我说:'介绍安泰国际广场''今天天气如何？'");
            }
            isFirst = false;
        }
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.pref_key_speech), true)) {
            setHelloText1();
        } else {
            setHelloText2();
        }
        if (!Globle.robotId.equals("-1")) {
            getActionList(Globle.robotId);
        }
    }

    private void initApi() {
        InitRequest initRequest = new InitRequest(HardwareInfo.getDeviceSN(), HardwareInfo.getAppVersionCode(getApplicationContext()) + "", "1", "qijie001");
        ApiManager.getInstance().getRobotServer().initRobot(initRequest)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<InitModel>() {
            @Override
            public void accept(InitModel initModel) throws Exception {
                Log.e(TAG, initModel.getMsg() + "   initApi");
                if (initModel.getCode() == 200) {
                    mapid = initModel.getMapid();
                    Globle.robotId = initModel.getRobotNo();
                    getActionList(initModel.getRobotNo());
                    XUpdate.newBuild(MainActivity.this)
                            .updateUrl("http://47.110.149.187:10031/api/RobotInit/GetNewAndroidVesion?RobotNo=" + Globle.robotId + "&IsTest=0")
                            .updateParser(new CustomUpdateParser(SystemUtils.getAppVersionCode(MainActivity.this))) //设置自定义的版本更新解析器
                            .update();
                    activeEngine();
                    SlamwareAgent.getNewInstance().connectTo("192.168.11.1");
                    Flowable.timer(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            Log.e(TAG, "startService");
                            if (!Globle.robotId.equals("-1")) {
                                intentService = new Intent(MainActivity.this, MqttCoreService.class);
                                startService(intentService);
                                startUpdateFaceService();
                            }
                        }
                    });
                    Log.e(TAG, "getMapid =" + initModel.getMapid());
                } else {
                    Flowable.timer(10, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            initApi();
                        }
                    });
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e(TAG, throwable.getMessage());
                Flowable.timer(10, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        initApi();
                    }
                });
            }
        });
    }

    public void getActionList(String riobotid) {
        ApiManager.getInstance().getRobotServer().getActionList(riobotid)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetActionListResponse>() {
                    @Override
                    public void accept(GetActionListResponse getActionListResponse) throws Exception {
                        if (getActionListResponse.getCode() == 200) {
                            listData.clear();
                            listData.addAll(getActionListResponse.getData());
                            actionRecycleViewAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
//        ApiManager.getInstance().getRobotServer().getRoomList(riobotid)
//                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<GetActionListResponse>() {
//                    @Override
//                    public void accept(GetActionListResponse getActionListResponse) throws Exception {
//                        if (getActionListResponse.getCode() == 200) {
//                            listRoomData.clear();
//                            listRoomData.addAll(getActionListResponse.getData());
//                            roomRecycleViewAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });
    }

    /**
     * 根据唤醒角度计算麦克风波束号
     */
    private int getBeamFromAngle(int angle) {
        int beam = 0;
        if (angle > 30 && angle <= 90) {
            beam = 1;
        } else if (angle > 90 && angle <= 150) {
            beam = 2;
        } else if (angle > 150 && angle <= 210) {
            beam = 3;
        } else if (angle > 210 && angle <= 270) {
            beam = 4;
        } else if (angle > 270 && angle <= 330) {
            beam = 5;
        }
        return beam;
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectionLostEvent event) {
        SlamwareAgent.getNewInstance().reconnect();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectedEvent event) {
        Log.e(TAG, "mapid     = " + mapid);
        if (IsFirstLoad) {
            IsFirstLoad = false;
            Intent intent = new Intent("INIT_MAP");
            sendBroadcast(intent);
        }
    }


    public void startUpdateFaceService() {
        Intent intent = new Intent(getApplicationContext(), FaceUpdateService.class);
        intent.putExtra("type", "start");
        startService(intent);
    }

    public boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    public void activeEngine() {
        if (!libraryExists) {
            Toasty.error(MainActivity.this, getString(R.string.library_not_found)).show();
            return;
        }
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }
//        if (view != null) {
//            view.setClickable(false);
//        }
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) {
                RuntimeABI runtimeABI = FaceEngine.getRuntimeABI();
                Log.i(TAG, "subscribe: getRuntimeABI() " + runtimeABI);

                long start = System.currentTimeMillis();
                int activeCode = FaceEngine.activeOnline(MainActivity.this, Constant.APP_ID, Constant.SDK_KEY);
                Log.i(TAG, "subscribe cost: " + (System.currentTimeMillis() - start));
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
//                            Log.e(TAG, "MOK ");
                            startService(new Intent(MainActivity.this, FaceInfoService.class));
//                            show(getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            Log.e(TAG, "ALREADY_ACTIVATED ");
//                            show(getString(R.string.already_activated));
                            startService(new Intent(MainActivity.this, FaceInfoService.class));
                        } else {
                            Log.e(TAG, "引擎激活失败，错误码为 " + activeCode);
//                            Toasty.error(currentActivity,getString(R.string.active_failed, activeCode), Toast.LENGTH_SHORT).show();
                        }

//                        if (view != null) {
//                            view.setClickable(true);
//                        }
                        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
                        int res = FaceEngine.getActiveFileInfo(MainActivity.this, activeFileInfo);
                        if (res == ErrorInfo.MOK) {
                            Log.i(TAG, activeFileInfo.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.error(MainActivity.this, e.getMessage()).show();
//                        if (view != null) {
//                            view.setClickable(true);
//                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 检查能否找到动态链接库，如果找不到，请修改工程配置
     *
     * @param libraries 需要的动态链接库
     * @return 动态库是否存在
     */
    private boolean checkSoFile(String[] libraries) {
        File dir = new File(getApplicationInfo().nativeLibraryDir);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return false;
        }
        List<String> libraryNameList = new ArrayList<>();
        for (File file : files) {
            libraryNameList.add(file.getName());
        }
        boolean exists = true;
        for (String library : libraries) {
            exists &= libraryNameList.contains(library);
        }
        return exists;
    }

    private void getMapDetail(int mapId) {
        ApiManager.getInstance().getRobotServer().getMapList(Globle.robotId, mapId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MapListResponse>() {
            @Override
            public void accept(MapListResponse mapListResponse) throws Exception {
//                Log.e(TAG, "getMapName ="+mapListResponse.getMapList().get(0).getMapName());
                if (mapListResponse.getCode() == 200) {
                    SharedPreferencesManager.newInstance().setMapID(mapId);
                    SharedPreferencesManager.newInstance().setMapName(mapListResponse.getMapList().get(0).getMapName());
                    DownLoadMapService.startActionFoo(MainActivity.this, mapListResponse.getMapList().get(0).getMapSourceUrl());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toasty.warning(MainActivity.this, "获取地图出错", Toast.LENGTH_SHORT).show();
                Flowable.timer(2, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        getMapDetail(mapId);
                    }
                });
                throwable.printStackTrace();
            }
        });
    }

    public void startWave() {
        Log.e(TAG, "startWave");
        IvMic.setVisibility(View.GONE);
        waveSpeak.setVisibility(View.VISIBLE);
        waveSpeak.startAnim();
//        waveSpeak.setVolume(30);
    }

    public void setSpeakText(String text) {
        tvSpeak.setText(text);
    }

    public void setHelloText1() {
        SpannableStringBuilder style = new SpannableStringBuilder("请用'你好，安安'唤醒我");
        style.setSpan(new ForegroundColorSpan(Color.RED), 3, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        hello.setText(style);
    }

    public void setHelloText2() {
        SpannableStringBuilder style = new SpannableStringBuilder("请用'你好，安安'或点击麦克风唤醒我");
        style.setSpan(new ForegroundColorSpan(Color.RED), 3, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.RED), 10, 15, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        hello.setText(style);
    }

    public void stopWave() {
        waveSpeak.stopAnim();
        IvMic.setVisibility(View.VISIBLE);
        waveSpeak.setVisibility(View.GONE);
    }

    @OnClick({R.id.business, R.id.face, R.id.temperature, R.id.identify, R.id.iv_more, R.id.iv_iv_mic, R.id.video_1, R.id.video_2, R.id.room_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.business:
                if (!Globle.robotId.equals("-1"))
                    ApiManager.getInstance().getRobotServer().doAction(33, 24, 1, Globle.robotId)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<BaseModel>() {
                                @Override
                                public void accept(BaseModel baseModel) throws Exception {

                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {

                                }
                            });
                break;
            case R.id.face:
                ApiManager.getInstance().getRobotServer().doAction(34, 25, 1, Globle.robotId)
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseModel>() {
                            @Override
                            public void accept(BaseModel baseModel) throws Exception {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
//                stopService(new Intent(MainActivity.this, FaceInfoService.class));
//                startActivity(new Intent(MainActivity.this, FaceDiscernActivity.class));
                break;
            case R.id.temperature:
                stopService(new Intent(MainActivity.this, FaceInfoService.class));
                startActivity(new Intent(MainActivity.this, TempActivity.class));
                break;
//            case R.id.lineup:
//                stopService(new Intent(MainActivity.this, FaceInfoService.class));
//                startActivity(new Intent(MainActivity.this, OpenDoorActivity.class));


//            break;
//            case R.id.explain:
//                startActivity(new Intent(MainActivity.this, ExplainActivty.class));
//                break;
            case R.id.identify:
                startActivity(new Intent(MainActivity.this, IdentifyActivity.class));
                break;
            case R.id.iv_more:
                startActivity(new Intent(MainActivity.this, MoreItemActivity.class));
                break;
            case R.id.iv_iv_mic:
                if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(getString(R.string.pref_key_speech), true)) {
                    startWave();
                    aiuiWrapper.wakeUp(0);
                    serialControlManager.setBeam(1);
                }
                break;
            case R.id.video_1:
                Output.navigatorActivity(VideoActivity.getIntent(getTopActivity().getApplicationContext(), "http://www.qijie.mimm.co/files/video/%E6%97%A5%E7%85%A7%E4%B8%AD%E5%BF%83.mp4"));
                break;
            case R.id.video_2:
                Output.navigatorActivity(VideoActivity.getIntent(getTopActivity().getApplicationContext(), "http://www.qijie.mimm.co/files/video/safe.mp4"));
                break;
            case R.id.room_detail:
                startActivity(new Intent(MainActivity.this, RoomDetailActivity.class));
                break;
        }
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(MainActivity.this);
        //请求权限全部结果
        rxPermission.request(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            Toasty.warning(MainActivity.this, "App未能获取全部需要的相关权限，部分功能可能不能正常使用.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("INIT_MAP")) {
                Log.e(TAG, " mapid ==      " + SharedPreferencesManager.newInstance().getMapID());
                if (mapid == -1) {
                    Log.e(TAG, " mapid =-1");
                    SharedPreferencesManager.newInstance().setMapID(-1);
                    SharedPreferencesManager.newInstance().setMapPath("");
                } else if (SharedPreferencesManager.newInstance().getMapID() == mapid) {

                    if (TextUtils.isEmpty(SharedPreferencesManager.newInstance().getMapPath())) {
                        getMapDetail(mapid);
                    } else {
                        File file = new File(SharedPreferencesManager.newInstance().getMapPath());
                        if (file.exists()) {
                            SlamManager.getInstance().setMap(SharedPreferencesManager.newInstance().getMapPath());
                        } else {
                            getMapDetail(mapid);
                        }
                    }
                } else {
                    Log.e(TAG, " mapid =-111");
                    getMapDetail(mapid);
                }
            } else if ("DIALOG_DISPLAY".equals(action)) {
                if (intent.getIntExtra("CODE", 1) == 1) {
                    if (showAlertDialog != null) {
                        showAlertDialog.dismiss();
                    }
                } else {
                    if (showAlertDialog != null) {
                        if (!((MyApplication) getApplication()).isActoin()) {
                            showAlertDialog.show();
                        }
                    }
                }
            }
        }


    };

    @Override
    public void onClick(int position) {
        ApiManager.getInstance().getRobotServer().doAction(listData.get(position).getId(), listData.get(position).getYituid(), listData.get(position).getType(), Globle.robotId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseModel>() {
                    @Override
                    public void accept(BaseModel baseModel) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    public void onRoomClick(int position) {
        ApiManager.getInstance().getRobotServer().doAction(listRoomData.get(position).getId(), listRoomData.get(position).getYituid(), listRoomData.get(position).getType(), Globle.robotId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseModel>() {
                    @Override
                    public void accept(BaseModel baseModel) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }
}