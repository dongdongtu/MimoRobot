package com.chance.mimorobot.manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Handler;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.chance.mimorobot.MyApplication;
import com.chance.mimorobot.activity.CameraActivity;
import com.chance.mimorobot.activity.ImageActivity;
import com.chance.mimorobot.activity.MusicActivity;
import com.chance.mimorobot.activity.VideoActivity;
import com.chance.mimorobot.activity.WebActivity;
import com.chance.mimorobot.model.ActionItem;
import com.chance.mimorobot.model.MapPoint;
import com.chance.mimorobot.slam.slamware.SlamwareAgent;
import com.chance.mimorobot.statemachine.robot.Output;
import com.google.gson.Gson;
import com.slamtec.slamware.AbstractSlamwarePlatform;
import com.slamtec.slamware.action.ActionStatus;
import com.slamtec.slamware.action.IMoveAction;
import com.slamtec.slamware.action.MoveDirection;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.MoveOption;
import com.slamtec.slamware.robot.Rotation;
import com.slamtec.slamware.sdp.CompositeMapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class ActionManager {
    private final String TAG = ActionManager.class.getSimpleName();
    private static ActionManager actionManager = null;

    private int num1, num2;
    private List<List<ActionItem>> nextActionLists;
    private SerialControlManager serialControlManager;
    private VocalSpeakInterface vocalSpeakInterface;
    SlamManager slamManager;


    private Context context;
    private SlamwareAgent agent;

    /**
     * 记录当前正在执行的命令单元
     */
    private ActionItem cmd;
    /**
     * 是不是在执行动作
     */
    boolean isDoingAction = false;

    boolean flag = false;
    public boolean isfinishspeak = true;
    public boolean isfinishslam = true;
    boolean istime = true;

    public static ActionManager getInstance() {
        if (actionManager == null) {
            synchronized (ActionManager.class) {
                if (actionManager == null)
                    actionManager = new ActionManager();
            }
        }
        return actionManager;
    }

    private ActionManager() {
        serialControlManager = SerialControlManager.newInstance();
        vocalSpeakInterface = VocalSpeakManager.getInstance();
        slamManager = SlamManager.getInstance();
        agent = SlamwareAgent.getNewInstance();
        nextActionLists = new ArrayList<>();
    }

    public void init(Context context) {
        this.context = context;
    }

    public void doActionList(List<ActionItem> listData) {
        if (listData == null)
            return;
        for (ActionItem actionItem : listData) {
            Log.e(TAG, "doAction");
            doAction(actionItem);
        }
    }

    public void doAction(ActionItem actionItem) {

        switch (actionItem.getActionID()) {
            case 1://前进
                moveByDirection(MoveDirection.FORWARD);
                break;
            case 2://后退
                moveByDirection(MoveDirection.BACKWARD);
                break;
            case 3://左转
                moveByDirection(MoveDirection.TURN_LEFT);
                break;
            case 4://前进
                this.isfinishslam = false;
                cmd = actionItem;
                Log.e(TAG, "slam mapPoint");
                MapPoint mapPoint = new Gson().fromJson(actionItem.getParameter(), MapPoint.class);

                try {
                    move(mapPoint, true, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    cancelAction();
                }
                break;
            case 5://展示图片
                Log.e("TAG","SSSSSSS");
                Output.navigatorActivity(ImageActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), actionItem.getParameter()));
                break;
            case 6://右转
                moveByDirection(MoveDirection.TURN_RIGHT);
                break;
            case 7://+ 左转至
                cmd = actionItem;
                try {
                    this. isfinishslam = false;
                    rotateTo((float) Math.toRadians(Double.parseDouble(actionItem.getParameter())), true);
                } catch (Exception e) {
                    e.printStackTrace();
                    cancelAction();
                }
                break;
            case 8://+ 左转
                this.isfinishslam = false;
                cmd = actionItem;
                rotate(Integer.parseInt(actionItem.getParameter()));
                break;
            case 9://-
                try {
                    cmd = actionItem;
                    this.isfinishslam = false;
                    rotateTo(-(float) Math.toRadians(Double.parseDouble(actionItem.getParameter())), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10://-
                this. isfinishslam = false;
                cmd = actionItem;
                rotate(-Integer.parseInt(actionItem.getParameter()));
                break;
            case 11://说话
                Log.e(TAG, "说话");
                this.isfinishspeak = false;
                vocalSpeakInterface.activateSpeakCallback(actionItem.getParameter(), new Runnable() {
                    @Override
                    public void run() {
                        isfinishspeak = true;
                        checkFinish();
                    }
                });
                break;
            case 12://表情变化
                serialControlManager.setFace(Integer.parseInt(actionItem.getParameter()));
                break;
            case 13://展示网页
                Output.navigatorActivity(WebActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), actionItem.getParameter()));
                break;
            case 14://抬左手
                SerialControlManager.newInstance().armLeftTurnUp(30);
                break;
            case 15://抬右手
                SerialControlManager.newInstance().armRightTurnUp(30);
                break;
            case 16://头部左转
                SerialControlManager.newInstance().headTurnLeft(5);
                break;
            case 17://头部右转
                SerialControlManager.newInstance().headTurnRight(5);
                break;
            case 18://灯管控制
                switch (actionItem.getParameter()) {
                    case "红":
                        SerialControlManager.newInstance().lightREDControl();
                        break;
                    case "绿":
                        SerialControlManager.newInstance().lightGREENControl();
                        break;
                    case "蓝":
                        SerialControlManager.newInstance().lightBLUEControl();
                        break;
                }
                break;
            case 19://播放音频
                Output.navigatorActivity(MusicActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), actionItem.getParameter()));
                break;
            case 20://视频播放
                Output.navigatorActivity(VideoActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), actionItem.getParameter()));
                break;
            case 21://打印小票

                break;
            case 22://拍照
                Output.navigatorActivity(CameraActivity.class);
                break;
            case 23:
                this.istime = false;
                checkWaitAction(Integer.parseInt(actionItem.getParameter()));
                break;
            case 24:
                SerialControlManager.newInstance().armLeftTurnDown(30);
                break;
            case 25:
                SerialControlManager.newInstance().armRightTurnDown(30);
                break;
        }
        checkFinish();
    }


    Runnable delayRunnable;

    /**
     * 执行等待命令单元
     */
    private void checkWaitAction(int time) {
        delayRunnable = new Runnable() {
            @Override
            public void run() {
                Log.e("tag", "执行等待命令完成:");
                istime = true;
                checkFinish();
            }
        };
        mHandler.postDelayed(delayRunnable, (long) (time * 1000));
    }

    /**
     * 机器人先位移 后转身， 解决转身角度不一致的问题
     *
     * @param mapPoint
     * @param withYaw
     * @param isCheck
     * @throws Exception
     */
    private void move(MapPoint mapPoint, boolean withYaw, final boolean isCheck) throws Exception {

        // 机器人移动的目标点
        Location targetLocation = new Location(mapPoint.getX(), mapPoint.getY(), mapPoint.getZ());
        // 开始移动
        MoveOption moveOption = getMoveOption();
        moveOption.setKeyPoints(true);
        moveOption.setTrackWithOA(true);
//        agent.getRobotPlatform().setMapLocalization(Globle.loadConfig(context).isMoveReLocation());
        IMoveAction iMoveAction = agent.getRobotPlatform().moveTo(targetLocation, moveOption, 0);
        flag = true;

        Runnable completeRunnable;
        if (withYaw) {
            // 位移到指定位置之后，再执行旋转命令
            completeRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
//                        logger.info("机器人位移命令结束:");
                        float yaw = mapPoint.getZ();
                        rotateTo(yaw, isCheck);
                    } catch (Exception e) {
                        onActionErr(e);
                    }
                }
            };
        } else {
            // 位移到指定位置之后，直接判断下一条命令
            completeRunnable = new Runnable() {
                @Override
                public void run() {
//                    logger.info("机器人位移命令结束:");
                    if (isCheck)
                        checkFinish();
                }
            };
        }
        new MoveThread(iMoveAction, completeRunnable).start();
    }


    public void startAction(List<List<ActionItem>> lsit) {
        if (!isDoingAction) {
            Log.e("tag","  "+lsit.size());
            isDoingAction = true;
            nextActionLists.clear();
            nextActionLists.addAll(lsit);
            checkFinish();
        }
    }


    public void checkFinish() {
        if (this.isfinishslam && this.isfinishspeak&&this.istime) {
            if (nextActionLists.size() > 0) {
                Log.e(TAG, "checkFinish  " + this.nextActionLists.size());
                doActionList(this.nextActionLists.remove(0));
            } else {
                Log.e(TAG, "checkFinish  finish");

                isDoingAction = false;
//                Intent intent=new Intent("ACTION_STOP");
//                ActivityUtils.getTopActivity().sendBroadcast(intent);
                nextActionLists.clear();
                vocalSpeakInterface.stopSpeaking();
                flag = false;
                ((MyApplication) context.getApplicationContext()).setActoin(false);
            }
        }
    }


    public void rotate(int angle) {

        try {
            IMoveAction iMoveAction = SlamwareAgent.getNewInstance().getRobotPlatform().rotate(new Rotation((float) Math.toRadians(angle)));
            flag = true;
            new MoveThread(iMoveAction, new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "机器人旋转动作结束:");
                    isfinishslam = true;
                    checkFinish();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void moveByDirection(MoveDirection moveDirection) {
        isfinishslam=false;
        try {
            IMoveAction iMoveAction = SlamwareAgent.getNewInstance().getRobotPlatform().moveBy(moveDirection);
            flag = true;
            new MoveThread(iMoveAction, new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "机器人旋转动作结束:");
                    isfinishslam = true;
                    checkFinish();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断移动状态的线程
     */
    class MoveThread extends Thread {
        IMoveAction iMoveAction;
        Runnable completeRunnable;

        public MoveThread(IMoveAction iMoveAction, Runnable completeRunnable) {
            this.iMoveAction = iMoveAction;
            this.completeRunnable = completeRunnable;
        }

        @Override
        public void run() {
            try {
                while (flag) {
                    ActionStatus action = iMoveAction.getStatus();
                    Log.e(TAG, getId() + "--> 动作:" + action.name());
                    switch (action) {
                        case WAITING_FOR_START:
                        case PAUSED:
                        case STOPPED:
                        case RUNNING:
                            break;
                        case ERROR:
                            flag = false;
                            Log.e("Error", "Error");
                            mHandler.postDelayed(retryRunnable, 400);
                            break;
                        case FINISHED:
                            flag = false;
                            agent.getRobotPlatform().setMapLocalization(true);
                            mHandler.postDelayed(completeRunnable, 400);
                            break;
                    }
                    sleep(300);
                }
            } catch (Exception e) {
                Log.e("Exception", "Exception");
            }
        }
    }

    /**
     * 机器人旋转到角度
     *
     * @param angle   弧度
     * @param isCheck
     * @throws Exception
     */
    private void rotateTo(float angle, final boolean isCheck) throws Exception {
        IMoveAction iMoveAction = agent.getRobotPlatform().rotateTo(new Rotation(angle));
        flag = true;
        new MoveThread(iMoveAction, new Runnable() {
            @Override
            public void run() {
//                logger.info("机器人旋转动作结束:");
                if (isCheck) {
                    isfinishslam = true;
                    checkFinish();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler();


    Runnable retryRunnable = new Runnable() {
        @Override
        public void run() {
            if (cmd != null)
                doAction(cmd);
        }
    };

    /**
     * 执行命令集的时候发生异常
     *
     * @param e
     */
    private void onActionErr(Exception e) {
        isDoingAction = false;
//        list.clear();
        flag = false;
//        cmd = null;
//        dismissPopWindow();
//        e.printStackTrace();
//        MobclickAgent.reportError(context, e);
//        logger.info("机器人执行任务出错，已中止:" + e.toString());
//        if (onActionListener != null) {
//            onActionListener.onError(e);
//        }
    }

    /**
     * 获取移动的选项
     *
     * @return
     */
    private MoveOption getMoveOption() {
        MoveOption moveOption = new MoveOption();
        // true 使用关键点   false 直接前往
        moveOption.setMilestone(true);
//        moveOption.setSpeed(0.6f);
        // 使用虚拟轨道
        moveOption.setKeyPoints(true);

        moveOption.setTrackWithOA(true);
        // 设置角度
//        moveOption.setYaw(yaw);
        //
//        moveOption.setReturnUnreachableDirectly(true);
        return moveOption;
    }

    public void cancelAction() {
        nextActionLists.clear();
        vocalSpeakInterface.stopSpeaking();
        flag = false;
        isDoingAction = false;
        isfinishspeak = true;
        isfinishslam = true;
        istime = true;
        SlamwareAgent.getNewInstance().cancelAllActions();
        Log.i(TAG, "机器人执行任务取消已中止:");
    }
}
