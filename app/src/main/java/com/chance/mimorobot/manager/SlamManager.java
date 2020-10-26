package com.chance.mimorobot.manager;


import android.content.Context;
import android.os.Handler;
import android.util.Log;


import com.chance.mimorobot.slam.slamware.SlamwareAgent;
import com.slamtec.slamware.action.ActionStatus;
import com.slamtec.slamware.action.IMoveAction;
import com.slamtec.slamware.action.MoveDirection;
import com.slamtec.slamware.robot.CompositeMap;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.MoveOption;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.robot.Rotation;
import com.slamtec.slamware.sdp.CompositeMapHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class SlamManager {
    private final static String TAG=SlamManager.class.getSimpleName();
    private SlamwareAgent mAgent;
    private static SlamManager mSlamManager = null;
    private CompositeMapHelper compositeMapHelper;

    private boolean isLoop=false;

    /**
     * 是不是在执行动作
     */
    boolean isDoingAction = false;


    private FinishListener mFinishListener;
    private Handler mHandler ;
    boolean flag = false;
    // private ActionManager() { }
    public static SlamManager getInstance() {
        if (mSlamManager == null) {
            synchronized (SlamManager.class) {
                if (mSlamManager == null)
                    mSlamManager = new SlamManager();
            }
        }
        return mSlamManager;
    }
    private SlamManager() {
        this.mAgent=SlamwareAgent.getNewInstance();
        this.compositeMapHelper=new CompositeMapHelper();
    }
    public void init(Context mContext){
        this.mHandler = new Handler(mContext.getMainLooper());
    }
    public void setmFinishListener(FinishListener mFinishListener) {
        this.mFinishListener = mFinishListener;
    }
    public void setMap(String path){
        CompositeMap compositeMap=compositeMapHelper.loadFile(path);
        Pose pose = new Pose();
        mAgent.setCompositeMap(compositeMap, pose);
        mAgent.setNavigationMode(2);
//        mAgent.recoverLocalization();
    }

    public boolean isDoingAction() {
        return isDoingAction;
    }

    public void setDoingAction(boolean doingAction) {
        isDoingAction = doingAction;
    }

    public void move(MoveDirection direction){
        mAgent.moveBy(direction);
    }


    public void moveByLocation(Location location, boolean withYaw){
        isLoop=false;
        isDoingAction=true;
        MoveOption moveOption = new MoveOption();
        moveOption.setKeyPoints(true);
        moveOption.setTrackWithOA(true);
        try {
            IMoveAction iMoveAction=mAgent.getRobotPlatform().moveTo(location, moveOption, 0f);
            flag = true;
            Runnable completeRunnable;
            if (withYaw) {
                // 位移到指定位置之后，再执行旋转命令
                completeRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i(TAG, "机器人位移命令结束:");
                            Rotation rotation = new Rotation(location.getZ());
                            rotateTo(rotation);
                        } catch (Exception e) {
                            onActionErr(e);
                        }
                    }
                };
            }else {
                // 位移到指定位置之后，直接判断下一条命令
                completeRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG,"机器人位移命令结束:");
                        if (mFinishListener != null) {
                            mFinishListener.finished();
                        }
                    }
                };
            }
            new MoveThread(iMoveAction, completeRunnable).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void goHome(){
        isLoop=false;
        isDoingAction=true;
        try {
            IMoveAction iMoveAction=mAgent.getRobotPlatform().goHome();
            flag = true;
            Runnable completeRunnable;
                // 位移到指定位置之后，再执行旋转命令
                completeRunnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i(TAG, "机器人位移命令结束:");
                            Rotation rotation = new Rotation(0);
                            rotateTo(rotation);
                        } catch (Exception e) {
                            onActionErr(e);
                        }
                    }
                };
            new MoveThread(iMoveAction, completeRunnable).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rotateTo(Rotation rotation){
        isLoop=false;
        isDoingAction=true;
        try {
            IMoveAction iMoveAction = SlamwareAgent.getNewInstance().getRobotPlatform().rotateTo(rotation);
            flag = true;
            new MoveThread(iMoveAction, new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG,"机器人旋转动作结束:");
                    if (mFinishListener != null) {
                        mFinishListener.finished();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void rotate(Rotation rotation){
        isLoop=false;
        isDoingAction=true;
        try {
            IMoveAction iMoveAction = SlamwareAgent.getNewInstance().getRobotPlatform().rotate(rotation);
            flag = true;
            new MoveThread(iMoveAction, new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG,"机器人旋转动作结束:");
                    if (mFinishListener != null) {
                        mFinishListener.finished();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMap(){

    }

    public void moveByLocations(List<Location> locations, int i){
        isDoingAction=true;
        isLoop=true;
        MoveOption moveOption = new MoveOption();
        moveOption.setKeyPoints(true);
        moveOption.setTrackWithOA(true);
        try {
            IMoveAction iMoveAction = mAgent.getRobotPlatform().moveTo(locations, moveOption, 0f);
            flag = true;
            new MoveThread(iMoveAction, new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG,"机器人巡逻结束:");
                    if (mFinishListener != null) {
                        mFinishListener.finished();
                    }
                    if (i>0&&isLoop){
                        Flowable.timer(i, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                moveByLocations(locations,i);

                            }
                        });
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setSpeed(int i){
        mAgent.setSpeed(i);
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
                            if (mFinishListener != null) {
                                mFinishListener.error();
                            }
                            isDoingAction=false;
                            Log.e("Error", "Error");
//                            mHandler.postDelayed(retryRunnable, 400);
                            break;
                        case FINISHED:
                            flag = false;

                            isDoingAction=false;
                            SlamwareAgent.getNewInstance().getRobotPlatform().setMapLocalization(true);
                            mHandler.postDelayed(completeRunnable, 400);
                            break;
                    }
                    sleep(300);
                }
            } catch (Exception e) {
                Log.e("Exception", "Exception="+e.getMessage());
            }

        }
    }

    /**
     * 执行命令集的时候发生异常
     *
     * @param e
     */
    private void onActionErr(Exception e) {
        flag = false;
        isDoingAction=false;
        e.printStackTrace();
        Log.i(TAG,"机器人执行任务出错，已中止:" + e.toString());
        if (mFinishListener != null) {
            mFinishListener.error();
        }
    }

    public void cancelAction(){
        isLoop=false;
        flag = false;
        isDoingAction=false;
        Log.i(TAG,"机器人执行任务取消已中止:" );
    }
    public interface FinishListener{
        void finished();
        void error();
    }

}
