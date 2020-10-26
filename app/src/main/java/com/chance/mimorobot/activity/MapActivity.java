package com.chance.mimorobot.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.chance.mimorobot.ISocketService;
import com.chance.mimorobot.R;
import com.chance.mimorobot.adapter.ExplainPointAdapter;
import com.chance.mimorobot.adapter.MapNameListAdapter;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.manager.SharedPreferencesManager;
import com.chance.mimorobot.manager.SlamManager;
import com.chance.mimorobot.model.BaseResponseModel;
import com.chance.mimorobot.model.MapListResponse;
import com.chance.mimorobot.model.MapPoint;
import com.chance.mimorobot.model.MapPointListResponse;
import com.chance.mimorobot.model.MapPointRequest;
import com.chance.mimorobot.model.MapPointResponse;
import com.chance.mimorobot.model.MapResponse;
import com.chance.mimorobot.model.RobotMap;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.service.PngIntentService;
import com.chance.mimorobot.slam.event.ActionStatusGetEvent;
import com.chance.mimorobot.slam.event.ConnectionLostEvent;
import com.chance.mimorobot.slam.event.GetCompositeMapEvent;
import com.chance.mimorobot.slam.event.HomePoseGetEvent;
import com.chance.mimorobot.slam.event.LaserScanGetEvent;
import com.chance.mimorobot.slam.event.MapGetEvent;
import com.chance.mimorobot.slam.event.RemainingMilestonesGetEvent;
import com.chance.mimorobot.slam.event.RemainingPathGetEvent;
import com.chance.mimorobot.slam.event.RobotLocationGetEvent;
import com.chance.mimorobot.slam.event.RobotPoseGetEvent;
import com.chance.mimorobot.slam.event.TrackGetEvent;
import com.chance.mimorobot.slam.event.WallGetEvent;
import com.chance.mimorobot.slam.mapview.MapView;
import com.chance.mimorobot.slam.mapview.utils.RadianUtil;
import com.chance.mimorobot.slam.slamware.SlamwareAgent;
import com.google.gson.Gson;
import com.slamtec.slamware.action.Path;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.robot.LaserScan;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Map;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.sdp.CompositeMapHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.chance.mimorobot.constant.Constant.ACTION_DOWNLOAD_CODE;
import static com.chance.mimorobot.constant.Constant.ACTION_DOWNLOAD_EXTRA;
import static com.chance.mimorobot.constant.Constant.ACTION_PNG_CODE;

public class MapActivity extends TitleBarActivity {
    @BindView(R.id.tv_mapname)
    TextView tvMapname;
    private String TAG = MapActivity.class.getSimpleName();
    @BindView(R.id.tv_clear_map)
    TextView tvClearMap;
    @BindView(R.id.tv_recover)
    TextView tvRecover;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.tv_addmap)
    TextView tvAddmap;
    @BindView(R.id.rv_map)
    RecyclerView rvMap;
    @BindView(R.id.tv_addpoint)
    TextView tvAddpoint;
    @BindView(R.id.rv_point)
    RecyclerView rvPoint;
    private String mapname, pointname = "";
    private Map map;
    private String fileName, pngName;
    private AutoCompleteTextView et_name;
    private AlertDialog dialog;
    private boolean ismap = true;
    private SlamwareAgent mAgent;

    private MapNameListAdapter mapNameListAdapter;
    private ExplainPointAdapter explainPointAdapter;
    private List<MapPoint> pointList;
    private List<RobotMap> robotMapList;

    @Override
    int getContentLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        initView();
        Intent intent = new Intent(this, PngIntentService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        pointList = new ArrayList<>();
        robotMapList = new ArrayList<>();
        mapNameListAdapter = new MapNameListAdapter(this);
        rvMap.setLayoutManager(new LinearLayoutManager(this));
        rvMap.setHasFixedSize(true);
        rvMap.setAdapter(mapNameListAdapter);

        explainPointAdapter = new ExplainPointAdapter(this);
        rvPoint.setLayoutManager(new LinearLayoutManager(this));
        rvPoint.setHasFixedSize(true);
        rvPoint.setAdapter(explainPointAdapter);


        explainPointAdapter.setOnPointItemLongClickListener(new ExplainPointAdapter.OnPointItemLongClickListener() {
            @Override
            public void onWifiItemClicked(MapPoint mapPoint) {
                showSureDialog(mapPoint);
            }
        });

        mapNameListAdapter.setOnPointItemClickListener(new MapNameListAdapter.OnPointItemClickListener() {
            @Override
            public void onWifiItemClicked(RobotMap robotMap) {

            }
        });

        mapNameListAdapter.setOnPointItemLongClickListener(new MapNameListAdapter.OnPointItemLongClickListener() {
            @Override
            public void onWifiItemClicked(RobotMap robotMap) {
                showSureDialog(robotMap);
            }
        });


        mAgent = SlamwareAgent.getNewInstance();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PNG_CODE);
        intentFilter.addAction(ACTION_DOWNLOAD_CODE);
        registerReceiver(broadcastReceiver, intentFilter);
        tvMapname.setText(SharedPreferencesManager.newInstance().getMapName());

        AlertDialog.Builder editDialog = new AlertDialog.Builder(MapActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MapActivity.this);
        View v = inflater.inflate(R.layout.dialog_edittext, null);
        et_name = v.findViewById(R.id.rd_name);
        editDialog.setView(v);
        editDialog.setCancelable(true);
        editDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if (ismap) {
                        mapname = et_name.getText().toString().trim();
                        mAgent.saveCompositeMap();
                    } else {
                        pointname = et_name.getText().toString().trim();
                        mAgent.getRobotLocation();
                    }
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog = editDialog.create();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        unregisterReceiver(broadcastReceiver);
    }

    private void deletePoint(MapPoint mapPoint) {
        ApiManager.getInstance().getRobotServer().deletePoint(Globle.robotId, SharedPreferencesManager.newInstance().getMapID(), mapPoint.getId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResponseModel>() {
            @Override
            public void accept(BaseResponseModel deleteMapResponse) throws Exception {
                if (deleteMapResponse.getCode() == 200) {
                    pointList.remove(mapPoint);
                    explainPointAdapter.setWifiCollection(pointList);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    private void deleteMap(RobotMap robotMap) {
        ApiManager.getInstance().getRobotServer().deleteMap(Globle.robotId, robotMap.getId())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResponseModel>() {
            @Override
            public void accept(BaseResponseModel deleteMapResponse) throws Exception {
                if (deleteMapResponse.getCode() == 200) {
                    robotMapList.remove(robotMap);
                    mapNameListAdapter.setMapCollection(robotMapList);
                    if (robotMap.getId() == SharedPreferencesManager.newInstance().getMapID()) {
                        mAgent.clearMap();
                        SharedPreferencesManager.newInstance().setMapName("");
                        SharedPreferencesManager.newInstance().setMapID(-1);
                        pointList.clear();
                        explainPointAdapter.setWifiCollection(pointList);
                        tvMapname.setText("");
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }

    @OnClick({R.id.tv_clear_map, R.id.tv_recover, R.id.tv_addmap, R.id.tv_addpoint})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clear_map:
                mAgent.clearMap();
                break;
            case R.id.tv_recover:
                mAgent.recoverLocalization();
                break;
            case R.id.tv_addmap:
                ismap = true;
                dialog.show();
                break;
            case R.id.tv_addpoint:
                ismap = false;
                dialog.show();
                break;
        }
    }


    private ISocketService iService;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            iService = (ISocketService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_PNG_CODE.equals(action)) {
                File fileSource = new File(fileName);
                File filePng = new File(pngName);
                ArrayList<File> list = new ArrayList<File>();
                list.add(fileSource);
                list.add(filePng);

                List<MultipartBody.Part> parts = new ArrayList<>(list.size());
                for (File file : list) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);
                    parts.add(part);
                }
                RequestBody RobotNo = RequestBody.create(MediaType.parse("multipart/form-data"), Globle.robotId);
                RequestBody MapName = RequestBody.create(MediaType.parse("multipart/form-data"), mapname);
                RequestBody MapDes = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                RequestBody MapHeight = RequestBody.create(MediaType.parse("multipart/form-data"), map.getDimension().getHeight() + "");
                RequestBody MapWidth = RequestBody.create(MediaType.parse("multipart/form-data"), map.getDimension().getWidth() + "");
                RequestBody MapOriginX = RequestBody.create(MediaType.parse("multipart/form-data"), map.getOrigin().getX() + "");
                RequestBody MapOriginY = RequestBody.create(MediaType.parse("multipart/form-data"), map.getOrigin().getY() + "");
                RequestBody ResolutionX = RequestBody.create(MediaType.parse("multipart/form-data"), map.getResolution().getX() + "");
                RequestBody ResolutionY = RequestBody.create(MediaType.parse("multipart/form-data"), map.getResolution().getY() + "");
                ApiManager.getInstance().getRobotServer().postMap(RobotNo, MapName, MapDes, MapHeight, MapWidth, MapOriginX, MapOriginY, ResolutionX, ResolutionY
                        , parts).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<MapResponse>() {
                            @Override
                            public void accept(MapResponse mapResponse) throws Exception {
                                if (mapResponse.getCode() == 200) {
                                    SharedPreferencesManager.newInstance().setMapID(mapResponse.getMapid());
                                    SharedPreferencesManager.newInstance().setMapPath(fileName);
                                    SharedPreferencesManager.newInstance().setMapName(mapname);
                                    setMap(mapResponse.getMapid());
                                    RobotMap robotMap = new RobotMap();
                                    robotMap.setId(mapResponse.getMapid());
                                    robotMap.setMapName(mapname);
                                    robotMapList.add(robotMap);
                                    mapNameListAdapter.setMapCollection(robotMapList);
                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("RobotMapActivity", "" + throwable.getMessage());
                                throwable.printStackTrace();
                            }
                        });

            } else if (ACTION_DOWNLOAD_CODE.equals(intent.getAction())) {
                String mapPath = intent.getStringExtra(ACTION_DOWNLOAD_EXTRA);
                Log.e(TAG, "mapPath = " + mapPath);
                SharedPreferencesManager.newInstance().setMapPath(mapPath);
                SlamManager.getInstance().setMap(mapPath);
            }
        }
    };

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
    protected void onPause() {
        super.onPause();
        stopUpdate();
    }

    @Override
    public void onResume() {
        super.onResume();
        startUpdate();
        Log.e(TAG, Globle.robotId);
        ApiManager.getInstance().getRobotServer().getMapList(Globle.robotId, -1)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MapListResponse>() {
                    @Override
                    public void accept(MapListResponse mapResponse) throws Exception {
                        if (mapResponse.getCode() == 200) {
//                            listMapDates.addAll(mapResponse.getRobotMapList());
//                            mapAdapter.notifyDataSetChanged();
                            robotMapList.clear();
                            if (mapResponse.getMapList() != null && mapResponse.getMapList().size() > 0) {
                                robotMapList.addAll(mapResponse.getMapList());
                                Log.e("Httplog", "Httplog");
                                mapNameListAdapter.setMapCollection(robotMapList);
                            }

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
        Log.e(TAG, "" + SharedPreferencesManager.newInstance().getMapID());
        ApiManager.getInstance().getRobotServer().getMapPointList(SharedPreferencesManager.newInstance().getMapID())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MapPointListResponse>() {
                    @Override
                    public void accept(MapPointListResponse mapPointResponse) throws Exception {
                        if (mapPointResponse.getCode() == 200) {
//                            listDates.addAll(mapPointResponse.getPoints());
//                            mAdapter.notifyDataSetChanged();
                            pointList.clear();
                            if (mapPointResponse.getPoints() != null && mapPointResponse.getPoints().size() > 0) {
                                pointList.addAll(mapPointResponse.getPoints());
                                explainPointAdapter.setWifiCollection(pointList);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private void startUpdate() {
        mRobotStateUpdateThread = new Thread(mRobotStateUpdateRunnable);
        mRobotStateUpdateThread.start();
    }

    private void stopUpdate() {
        if (mRobotStateUpdateThread != null && !mRobotStateUpdateThread.isInterrupted()) {
            mRobotStateUpdateThread.interrupt();
        }
    }


    private Runnable mRobotStateUpdateRunnable = new Runnable() {
        int cnt;

        @Override
        public void run() {
            cnt = 0;
            mAgent.getGetRobotInfo();

            while (true) {
                if (mRobotStateUpdateRunnable == null || !mRobotStateUpdateThread.isAlive() || mRobotStateUpdateThread.isInterrupted()) {
                    break;
                }

                if ((cnt % 30) == 0) {
                    mAgent.getLaserScan();
                }

                if ((cnt % 30) == 0) {
                    mAgent.getMap();
                    mAgent.getWalls();
                    mAgent.getTracks();
                    mAgent.getMoveAction();
                    mAgent.getRobotStatus();
                }

                if ((cnt % 30) == 0) {
                    mAgent.getHomePose();
                    mAgent.getRobotHealth();
//                    mAgent.getMapUpdata();
                }

                SystemClock.sleep(33);
                cnt++;
            }
        }
    };

    Thread mRobotStateUpdateThread = new Thread(mRobotStateUpdateRunnable);


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MapGetEvent event) {
        Map map = event.getMap();
//        Log.e("MapGetEvent",map.getDimension().getWidth()+"   "+map.getDimension().getHeight()+"   "+map.getOrigin().getX()+"   "+map.getOrigin().getY()+"    "+map.getResolution().getX()+"   "+map.getResolution().getY());
        mMapView.setMap(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LaserScanGetEvent event) {
        LaserScan laserScan = event.getLaserScan();
        mMapView.setLaserScan(laserScan);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WallGetEvent event) {
        List<Line> walls = event.getWalls();
        mMapView.setVwalls(walls);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TrackGetEvent event) {
        List<Line> tracks = event.getTracks();
        mMapView.setVtracks(tracks);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RobotPoseGetEvent event) {
        Pose pose = event.getPose();
        mMapView.setRobotPose(pose);
        if (pose != null) {
            String s = String.format("机器位姿  [%.2f, %.2f, %.2f]", pose.getX(), pose.getY(), RadianUtil.toAngel(pose.getYaw()));
//            mRobotLocation.setText(s);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RobotLocationGetEvent event) {
        Pose location = event.getLocation();
        MapPoint mapPoint = new MapPoint();
        mapPoint.setX(location.getX());
        mapPoint.setY(location.getY());
        mapPoint.setZ(location.getYaw());
        mapPoint.setPointName(pointname);

        ApiManager.getInstance().getRobotServer().postMapPoint(new MapPointRequest(Globle.robotId, SharedPreferencesManager.newInstance().getMapID(), mapPoint))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MapPointResponse>() {
                    @Override
                    public void accept(MapPointResponse mapPointResponse) throws Exception {
                        if (mapPointResponse.getCode() == 200) {
//                            if (listDates != null) {
//                                listDates.add(mapPointResponse.getPoint());
//                                mAdapter.notifyDataSetChanged();
//                            }
                            pointList.add(mapPointResponse.getPoints().get(0));
                            explainPointAdapter.setWifiCollection(pointList);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HomePoseGetEvent event) {
        Pose pose = event.getHomePose();
        mMapView.setHomePose(pose);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RemainingMilestonesGetEvent event) {
        Path remainingMilestones = event.getRemainingMilestones();
        mMapView.setRemainingMilestones(remainingMilestones);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RemainingPathGetEvent event) {
        Path remainingPath = event.getRemainingPath();
        mMapView.setRemainingPath(remainingPath);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ActionStatusGetEvent event) {
        if (event.getActionStatus().toString().equals("FINISHED")) {
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectionLostEvent event) {
        mAgent.reconnect();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBackThread(GetCompositeMapEvent event) {
        try {
            Log.e("RobotMapActivity", "event");
            String date = String.valueOf(new Date().getTime());
            fileName = Globle.MAP_PATH + "/MapSource_" + Globle.robotId + "_" + date + ".stcm";
            CompositeMapHelper compositeMapHelper = new CompositeMapHelper();
            String filePath = compositeMapHelper.saveFile(fileName, event.getCompositeMap());
            if (filePath == null) {
                Toast.makeText(this, "地图保存在" + fileName, Toast.LENGTH_SHORT).show();
            }
            pngName = Globle.MAP_PATH + "/RobotMap_" + Globle.robotId + "_" + date + ".png";
            map = event.getMap();
            iService.sendMessage(new Gson().toJson(map), pngName);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public void showSureDialog(MapPoint mapPoint) {
        AlertDialog suredialog = new AlertDialog.Builder(MapActivity.this)
                .setTitle("请确认")
                .setMessage("确认删除" + mapPoint.getPointName() + "?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePoint(mapPoint);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                }).create();
        suredialog.show();
    }

    public void showSureDialog(RobotMap robotMap) {
        AlertDialog suredialog = new AlertDialog.Builder(MapActivity.this)
                .setTitle("请确认")
                .setMessage("请选择操作" + robotMap.getMapName() + "?")
                .setPositiveButton("删除地图", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMap(robotMap);
                    }
                })
                .setNeutralButton("设置地图", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                }).create();
        suredialog.show();
    }


    public void setMap(int id) {
        ApiManager.getInstance().getRobotServer().setMap(Globle.robotId, id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Consumer<BaseResponseModel>() {
                    @Override
                    public void accept(BaseResponseModel baseResponse) throws Exception {
                        if (baseResponse.getCode() == 200) {
                            SharedPreferencesManager.newInstance().setMapID(id);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }
        );
    }
}
