package com.chance.mimorobot.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chance.mimorobot.MyApplication;
import com.chance.mimorobot.R;
import com.chance.mimorobot.adapter.ExplainNameAdapter;
import com.chance.mimorobot.adapter.ExplainPointAdapter;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.manager.ActionManager;
import com.chance.mimorobot.manager.SlamManager;
import com.chance.mimorobot.manager.VocalSpeakInterface;
import com.chance.mimorobot.manager.VocalSpeakManager;
import com.chance.mimorobot.model.ExplainModel;
import com.chance.mimorobot.model.MapPoint;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.slam.event.ConnectionLostEvent;
import com.chance.mimorobot.slam.event.HomePoseGetEvent;
import com.chance.mimorobot.slam.event.LaserScanGetEvent;
import com.chance.mimorobot.slam.event.MapGetEvent;
import com.chance.mimorobot.slam.event.RemainingMilestonesGetEvent;
import com.chance.mimorobot.slam.event.RemainingPathGetEvent;
import com.chance.mimorobot.slam.event.RobotPoseGetEvent;
import com.chance.mimorobot.slam.event.TrackGetEvent;
import com.chance.mimorobot.slam.event.WallGetEvent;
import com.chance.mimorobot.slam.mapview.MapView;
import com.chance.mimorobot.slam.slamware.SlamwareAgent;
import com.slamtec.slamware.action.Path;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.robot.LaserScan;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Map;
import com.slamtec.slamware.robot.Pose;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ExplainActivty extends TitleBarActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_point)
    RecyclerView rvPoint;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.robotMap)
    MapView mMapView;
    private String TAG = ExplainActivty.class.getSimpleName();


    private MaterialDialog dialog;
    private AlertDialog suredialog;
    private ExplainPointAdapter explainPointAdapter;
    private ExplainNameAdapter explainNameAdapter;
    private boolean ismap = true;

    private ExplainModel.DataBean dataBean;

    private SlamwareAgent mAgent;

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
                    mAgent.getMapUpdata();
                }

                SystemClock.sleep(33);
                cnt++;
            }
        }
    };

    Thread mRobotStateUpdateThread = new Thread(mRobotStateUpdateRunnable);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAgent = SlamwareAgent.getNewInstance();
        if (((MyApplication)getApplication()).isActoin()) {
            tvStart.setText("停止");
        } else {
            tvStart.setText("开始讲解");
        }
        setupRecyclerView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_explain;
    }

    @Override
    public void onResume() {
        super.onResume();
        startUpdate();
        ApiManager.getInstance().getRobotServer().getExplainPath(Globle.robotId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ExplainModel>() {
            @Override
            public void accept(ExplainModel explainModel) throws Exception {
                if (explainModel.getCode() == 200 && explainModel.getData().size() > 0) {
                    explainPointAdapter.setWifiCollection(explainModel.getData().get(0).getPoint());
                    tvTitle.setText(explainModel.getData().get(0).getPathname());
                    explainNameAdapter.setCollection(explainModel.getData());
                    dataBean = explainModel.getData().get(0);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopUpdate();
    }

    public void showDialog() {
        dialog = new MaterialDialog.Builder(this)
//                .title("请刷身份证或扫码")
//                .content(R.string.dialog_finish_navigation_content)
                .adapter(explainNameAdapter, new LinearLayoutManager(this))
                .positiveText("取消")
                .autoDismiss(true)
                .build();
        dialog.show();
    }


    private void setupRecyclerView() {
        explainPointAdapter = new ExplainPointAdapter(ExplainActivty.this);
        explainNameAdapter = new ExplainNameAdapter(ExplainActivty.this);
        rvPoint.setLayoutManager(new LinearLayoutManager(this));
        rvPoint.setHasFixedSize(true);
//        rvPoint.addItemDecoration(new DividerItemDecoration(ExplainActivty.this, DividerItemDecoration.VERTICAL));

        explainPointAdapter.setOnPointItemClickListener(new ExplainPointAdapter.OnPointItemClickListener() {
            @Override
            public void onWifiItemClicked(MapPoint wifiEntity) {
                Log.e(TAG, "explainPointAdapter");
                showSureDialog(wifiEntity);
            }
        });
        rvPoint.setAdapter(explainPointAdapter);
        explainNameAdapter.setOnItemClickListener(new ExplainNameAdapter.OnItemClickListener() {
            @Override
            public void onWifiItemClicked(ExplainModel.DataBean wifiEntity) {
                Log.e(TAG, "explainNameAdapter");
                explainPointAdapter.setWifiCollection(wifiEntity.getPoint());
                tvTitle.setText(wifiEntity.getPathname());
                dataBean = wifiEntity;
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

    @OnClick({R.id.tv_title, R.id.rv_point, R.id.tv_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                showDialog();
                break;
            case R.id.tv_start:
                if (tvStart.getText().toString().equals("停止")) {
//                    SlamManager.getInstance().cancelAction();
                    ActionManager.getInstance().cancelAction();
                    ((MyApplication)getApplication()).setActoin(false);
                    tvStart.setText("开始讲解");
                } else if (tvStart.getText().toString().equals("开始讲解")) {
                    showSurePathDialog();
                }
                break;
        }
    }

    public void showSureDialog(MapPoint mapPoint) {
        suredialog = new AlertDialog.Builder(ExplainActivty.this)
                .setTitle("请确认")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SlamManager.getInstance().moveByLocation(new Location(mapPoint.getX(), mapPoint.getY(), mapPoint.getZ()), true);
                        tvStart.setText("停止");
                        dialog.dismiss();
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

    public void showSurePathDialog() {
        suredialog = new AlertDialog.Builder(ExplainActivty.this)
                .setTitle("请确认")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dataBean != null) {
                            ActionManager.getInstance().startAction(dataBean.getNextActionLists());
                            tvStart.setText("停止");
                            ((MyApplication)getApplication()).setActoin(true);
                            VocalSpeakManager.getInstance().sleep();
                            finish();
                        } else {
                            Toasty.warning(ExplainActivty.this, "指令为空", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
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
//        if (pose != null) {
//            String s = String.format("机器位姿  [%.2f, %.2f, %.2f]", pose.getX(), pose.getY(), RadianUtil.toAngel(pose.getYaw()));
////            mRobotLocation.setText(s);
//        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectionLostEvent event) {
        mAgent.reconnect();

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



}
