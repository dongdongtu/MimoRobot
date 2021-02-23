package com.chance.mimorobot.slam.slamware;

import android.graphics.RectF;
import android.util.Log;

import com.chance.mimorobot.slam.event.ActionStatusGetEvent;
import com.chance.mimorobot.slam.event.ConnectedEvent;
import com.chance.mimorobot.slam.event.ConnectionLostEvent;
import com.chance.mimorobot.slam.event.GetCompositeMapEvent;
import com.chance.mimorobot.slam.event.HomePoseGetEvent;
import com.chance.mimorobot.slam.event.LaserScanGetEvent;
import com.chance.mimorobot.slam.event.MapGetEvent;
import com.chance.mimorobot.slam.event.MapUpdataEvent;
import com.chance.mimorobot.slam.event.RemainingMilestonesGetEvent;
import com.chance.mimorobot.slam.event.RemainingPathGetEvent;
import com.chance.mimorobot.slam.event.RobotHealthInfoEvent;
import com.chance.mimorobot.slam.event.RobotInfoEvent;
import com.chance.mimorobot.slam.event.RobotLocationGetEvent;
import com.chance.mimorobot.slam.event.RobotPoseGetEvent;
import com.chance.mimorobot.slam.event.RobotStatusGetEvent;
import com.chance.mimorobot.slam.event.TrackGetEvent;
import com.chance.mimorobot.slam.event.WallGetEvent;
import com.slamtec.slamware.AbstractSlamwarePlatform;
import com.slamtec.slamware.action.ActionStatus;
import com.slamtec.slamware.action.IMoveAction;
import com.slamtec.slamware.action.MoveDirection;
import com.slamtec.slamware.action.Path;
import com.slamtec.slamware.discovery.DeviceManager;
import com.slamtec.slamware.exceptions.ConnectionFailException;
import com.slamtec.slamware.exceptions.ConnectionTimeOutException;
import com.slamtec.slamware.exceptions.ParseInvalidException;
import com.slamtec.slamware.exceptions.RequestFailException;
import com.slamtec.slamware.exceptions.UnauthorizedRequestException;
import com.slamtec.slamware.exceptions.UnsupportedCommandException;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.robot.CompositeMap;
import com.slamtec.slamware.robot.HealthInfo;
import com.slamtec.slamware.robot.LaserScan;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Map;
import com.slamtec.slamware.robot.MapKind;
import com.slamtec.slamware.robot.MoveOption;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.robot.RecoverLocalizationMovement;
import com.slamtec.slamware.robot.RecoverLocalizationOptions;
import com.slamtec.slamware.robot.Rotation;
import com.slamtec.slamware.robot.SystemParameters;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.slamtec.slamware.action.ActionStatus.FINISHED;
import static com.slamtec.slamware.robot.ArtifactUsage.ArtifactUsageVirtualTrack;
import static com.slamtec.slamware.robot.MapType.BITMAP_8BIT;

public class SlamwareAgent {
    private final static String TAG = "SlamwareAgent";

    private final static int ROBOT_PORT = 1445;
    private final static int NAVIGATION_MODE_FREE = 0;
    private final static int NAVIGATION_MODE_TRACK = 1;
    private final static int NAVIGATION_MODE_TRACK_OA = 2;

    private AbstractSlamwarePlatform mRobotPlatform;
    private String mIp;
    private int mNavigationMode;

    private ThreadManager mManager;
    private ThreadManager.ThreadPoolProxy mPoolProxy;



    private static TaskSetHomePose sTaskSetHomePose;
    private static TaskRecoverLocalization sTaskRecoverLocalization;
    private static TaskConnect sTaskConnect;
    private static TaskCancelAllActions sTaskCancelAllActions;
    private static TaskGetMoveAction sTaskGetMoveAction;
    private static TaskDisconnect sTaskDisconnect;
    private static TaskGoHome sTaskGoHome;
    private static TaskMoveBy sTaskMoveBy;
    private static TaskMoveTo sTaskMoveTo;
    private static TaskMoveToLocations sTaskMoveToLocations;
    private static TaskRotateTo sTaskRotateTo;
    private static TaskGetLaserScan sTaskGetLaserScan;
    private static TaskGetMapUpdata sTaskGetIsMapUpdata;
    private static TaskToggleMapUpdata sTaskToggleMapUpdata;
    private static TaskGetMap sTaskGetMap;
    private static TaskGetRobotPose sTaskGetRobotPose;
    private static TaskGetRobotLocation sTaskGetRobotLocation;
    private static TaskGetHomePose sTaskGetHomePose;
    private static TaskGetStatus sTaskGetStatus;
    private static TaskGetRobotInfo sTaskGetRobotInfo;
    private static TaskGetWalls sTaskGetWalls;
    private static TaskGetTracks sTaskGetTracks;
    private static TaskGetRobotHealth sTaskGetRobotHealth;
    private static TaskClearRobotHealth sTaskClearRobotHealth;
    private static TaskgetCompositeMap sTaskgetCompositeMap;
    private static TaskSetCompositeMap sTaskSetCompositeMap;
    private static TaskSetSpeed sTaskSetSpeed;
    private static TaskClearMap sTaskClearMap;
//    private static TaskCancelAction sTaskCancelAction;


    private static  SlamwareAgent  slamwareAgent= null;

    public int getSpeed() {
        return speed;
    }

    private int speed=5;

    public static SlamwareAgent getNewInstance(){
        if (slamwareAgent==null){
            synchronized (SlamwareAgent.class) {
                slamwareAgent = new SlamwareAgent();
            }
        }
        return slamwareAgent;
    }


    public SlamwareAgent() {
        mManager = ThreadManager.getInstance();
        mPoolProxy = mManager.createLongPool();

        sTaskClearMap=new TaskClearMap();
        sTaskRecoverLocalization=new TaskRecoverLocalization();
        sTaskSetSpeed=new TaskSetSpeed();
        sTaskConnect = new TaskConnect();
        sTaskCancelAllActions = new TaskCancelAllActions();
        sTaskDisconnect = new TaskDisconnect();
        sTaskGoHome = new TaskGoHome();
        sTaskMoveBy = new TaskMoveBy();
        sTaskRotateTo=new TaskRotateTo();
        sTaskMoveToLocations=new TaskMoveToLocations();
        sTaskGetLaserScan = new TaskGetLaserScan();
        sTaskGetIsMapUpdata = new TaskGetMapUpdata();
        sTaskToggleMapUpdata = new TaskToggleMapUpdata();
        sTaskGetMap = new TaskGetMap();
        sTaskGetRobotPose = new TaskGetRobotPose();
        sTaskGetRobotLocation=new TaskGetRobotLocation();
        sTaskGetHomePose = new TaskGetHomePose();
        sTaskGetStatus = new TaskGetStatus();
        sTaskGetRobotInfo = new TaskGetRobotInfo();
        sTaskGetWalls = new TaskGetWalls();
        sTaskGetTracks = new TaskGetTracks();
        sTaskGetRobotHealth = new TaskGetRobotHealth();
        sTaskClearRobotHealth = new TaskClearRobotHealth();
        sTaskGetMoveAction = new TaskGetMoveAction();
        sTaskMoveTo = new TaskMoveTo();
        sTaskgetCompositeMap = new TaskgetCompositeMap();
        sTaskSetCompositeMap = new TaskSetCompositeMap();
        sTaskSetHomePose=new TaskSetHomePose();
//        sTaskCancelAction=new TaskCancelAction();

        mNavigationMode = NAVIGATION_MODE_FREE;
    }
    public AbstractSlamwarePlatform getRobotPlatform() {
        return mRobotPlatform;
    }


    public void connectTo(String ip) {
        mIp = ip;
        pushTask(sTaskConnect);
    }

    public void reconnect() {
        String ip;

        synchronized (this) {
            ip = mIp;
        }

        if (ip.isEmpty()) return;

        connectTo(ip);
    }

    public void setNavigationMode(int mode) {
        mNavigationMode = mode;
    }

    public int getNavigationMode() {
        return mNavigationMode;
    }

    public void recoverLocalization(){
        pushTask(sTaskRecoverLocalization);
    }


    public void disconnect() {
        pushTask(sTaskDisconnect);
    }

    public void getRobotPose() {
        pushTask(sTaskGetRobotPose);
    }

    public void getRobotLocation(){
        pushTask(sTaskGetRobotLocation);
    }

    public void clearMap(){
        pushTask(sTaskClearMap);
    }

    public void getMap() {
        pushTask(sTaskGetMap);
    }

    public void getHomePose() {
        pushTask(sTaskGetHomePose);
    }

    public void getLaserScan() {
        pushTask(sTaskGetLaserScan);
    }

    public void getRobotStatus() {
        pushTask(sTaskGetStatus);
    }

    public void getMoveAction() {
        pushTask(sTaskGetMoveAction);
    }

    public void clearRobotHealth(List<Integer> errors) {
        sTaskClearRobotHealth.setErrorCodes(errors);
        pushTask(sTaskClearRobotHealth);
    }


    public void setSpeed(int i){
        speed=i;
        Log.e("speed","speed = "+i);
        sTaskSetSpeed.setSpeed(i);
        pushTask(sTaskSetSpeed);
    }

    public void moveTo(Location location) {
        sTaskMoveTo.setlocation(location);
        pushTaskHead(sTaskMoveTo);
    }

    public void moveToLocations(List<Location> locations){
        sTaskMoveToLocations.setlocation(locations);
        pushTaskHead(sTaskMoveToLocations);
    }

    public void moveBy(MoveDirection direction) {
        sTaskMoveBy.setMoveDirection(direction);
        pushTask(sTaskMoveBy);
    }

    public void rotateTo(Rotation rotation){
        sTaskRotateTo.setRotation(rotation);
        pushTaskHead(sTaskRotateTo);
    }

    public void getWalls() {
        pushTask(sTaskGetWalls);
    }

    public void getTracks() {
        pushTask(sTaskGetTracks);
    }

    public void goHome() {
        pushTaskHead(sTaskGoHome);
    }

    public void toggleMapUpdata() {
        pushTaskHead(sTaskToggleMapUpdata);
    }

    public void cancelAllActions() {
        pushTaskHead(sTaskCancelAllActions);
    }

    public void getMapUpdata() {
        pushTaskHead(sTaskGetIsMapUpdata);
    }

    public void getRobotHealth() {
        pushTask(sTaskGetRobotHealth);
    }

    public void getGetRobotInfo() {
        pushTask(sTaskGetRobotInfo);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private synchronized void pushTaskHead(Runnable Task) {
        mPoolProxy.execute(Task);
    }

    private synchronized void pushTask(Runnable Task) {
        mPoolProxy.execute(Task);
    }

    private void onRequestError(Exception e) {
        Log.e(TAG, e.getMessage());

        synchronized (this) {
            mPoolProxy.cancleAll();
            mRobotPlatform = null;
        }

        EventBus.getDefault().post(new ConnectionLostEvent());
    }

    public void saveCompositeMap() {
        pushTaskHead(sTaskgetCompositeMap);
    }

    public void setCompositeMap(CompositeMap compositeMap, Pose pose) {
        sTaskSetCompositeMap.setCompositeMap(compositeMap);
        sTaskSetCompositeMap.setPose(pose);
        pushTaskHead(sTaskSetCompositeMap);
    }
    public void setHomePose( Pose pose) {
        sTaskSetHomePose.setPose(pose);
        pushTaskHead(sTaskSetHomePose);
    }

    //////////////////////////////////// Runnable //////////////////////////////////////////////////



    private class TaskSetSpeed implements Runnable{
        private String speed;

        public TaskSetSpeed() {
        }

        public void setSpeed(int s) {
            this.speed = s*0.04+0.3+"";
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                mRobotPlatform.setSystemParameter(SystemParameters.SYSPARAM_ROBOT_SPEED,speed);
            } catch (RequestFailException e) {
                e.printStackTrace();
            } catch (ConnectionFailException e) {
                e.printStackTrace();
            } catch (ConnectionTimeOutException e) {
                e.printStackTrace();
            } catch (UnauthorizedRequestException e) {
                e.printStackTrace();
            } catch (UnsupportedCommandException e) {
                e.printStackTrace();
            } catch (ParseInvalidException e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskClearMap implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            try {
                platform.clearMap();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }
        }
    }

    private class TaskConnect implements Runnable {

        @Override
        public void run() {
            try {
                if (mIp == null || mIp.isEmpty()) {
                    onRequestError(new Exception("robot ip is empty"));
                    return;
                }

                synchronized (this) {
                    mRobotPlatform = DeviceManager.connect(mIp, ROBOT_PORT);
                }

            } catch (Exception exception) {
                onRequestError(exception);
                return;
            }

            EventBus.getDefault().post(new ConnectedEvent());
        }
    }

    private class TaskDisconnect implements Runnable {
        @Override
        public void run() {
            synchronized (this) {
                if (mRobotPlatform == null) {
                    return;
                }
                mPoolProxy.cancleAll();
                mRobotPlatform.disconnect();
                mRobotPlatform = null;
            }
        }
    }


    private class TaskRecoverLocalization implements Runnable {

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                synchronized (this) {
                    platform = mRobotPlatform;
                }

                if (platform == null) {
                    return;
                }

                try{
                    RectF area = new RectF();
                    RecoverLocalizationOptions recoverLocalizationOptions=new RecoverLocalizationOptions();
                    recoverLocalizationOptions.setMaxRecoverTimeInMilliSeconds(200000);
                    recoverLocalizationOptions.setRecoverMovementType(RecoverLocalizationMovement.RotateOnly);
                    platform.recoverLocalization(area,recoverLocalizationOptions);
                }catch (Exception e){
                    onRequestError(e);
                }
            }
        }
    }

    private class TaskGetMap implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            Map map = null;

            try {
                RectF area = platform.getKnownArea(BITMAP_8BIT, MapKind.EXPLORE_MAP);
                map = platform.getMap(BITMAP_8BIT, MapKind.EXPLORE_MAP, area);
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new MapGetEvent(map));
        }
    }

    private class TaskGetRobotPose implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            Pose pose;

            try {
                pose = platform.getPose();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new RobotPoseGetEvent(pose));
        }
    }


    private class TaskGetRobotLocation implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            Pose location;

            try {
                location = platform.getPose();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new RobotLocationGetEvent(location));
        }
    }

    private class TaskGetHomePose implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) return;
            }

            Pose homePose;

            try {
                homePose = platform.getHomePose();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new HomePoseGetEvent(homePose));
        }
    }

    private class TaskGetLaserScan implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) return;
            }

            LaserScan laserScan;

            try {
                laserScan = platform.getLaserScan();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new LaserScanGetEvent(laserScan));
        }
    }

    private class TaskGetMapUpdata implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            boolean isMapUpdata;

            try {
                isMapUpdata = platform.getMapUpdate();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new MapUpdataEvent(isMapUpdata));
        }
    }


    private class TaskToggleMapUpdata implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            try {
                boolean mapUpdate = platform.getMapUpdate();
                platform.setMapUpdate(!mapUpdate);
            } catch (Exception e) {
                onRequestError(e);
                return;
            }
        }
    }

    private class TaskGetStatus implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            boolean isCharging;
            int batteryPercentage;
            int localizationQuality;

            try {
                batteryPercentage = platform.getBatteryPercentage();
                isCharging = platform.getBatteryIsCharging();
                localizationQuality = platform.getLocalizationQuality();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new RobotStatusGetEvent(batteryPercentage, isCharging, localizationQuality));
        }
    }

    private class TaskGetRobotInfo implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            int modelId;
            String hardwareVersion;
            String softwareVersion;
            String modelName;

            try {
                modelId = platform.getModelId();
                hardwareVersion = platform.getHardwareVersion();
                softwareVersion = platform.getSoftwareVersion();
                modelName = platform.getModelName();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new RobotInfoEvent(modelId, hardwareVersion, softwareVersion, modelName));
        }
    }

    private class TaskGetMoveAction implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) return;
            }

            Path remainingMilestones = null;
            Path remainingPath = null;
            ActionStatus actionStatus = FINISHED;

            try {
                IMoveAction moveAction = platform.getCurrentAction();

                if (moveAction != null) {
                    remainingMilestones = moveAction.getRemainingMilestones();
                    remainingPath = moveAction.getRemainingPath();
                     actionStatus = moveAction.getStatus();
                }

            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new RemainingMilestonesGetEvent(remainingMilestones));
            EventBus.getDefault().post(new RemainingPathGetEvent(remainingPath));
            EventBus.getDefault().post(new ActionStatusGetEvent(actionStatus));
        }
    }

    private class TaskMoveTo implements Runnable {
        private Location location;

        public TaskMoveTo() {
        }

        public void setlocation(Location location) {
            this.location = location;
        }


        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null || location == null) return;
            }

            try {
                MoveOption moveOption = new MoveOption();

                switch (mNavigationMode) {
                    case NAVIGATION_MODE_FREE:
                        break;

                    case NAVIGATION_MODE_TRACK:
                        moveOption.setKeyPoints(true);
                        break;

                    case NAVIGATION_MODE_TRACK_OA:
                        moveOption.setKeyPoints(true);
                        moveOption.setTrackWithOA(true);
                        break;

                    default:
                        break;
                }

                platform.moveTo(location, moveOption, 0f);

            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskMoveToLocations implements Runnable {
        private List<Location> locations;

        public TaskMoveToLocations() {
        }

        public void setlocation(List<Location> locations) {
            this.locations = locations;
        }


        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null || locations == null) return;
            }

            try {
                MoveOption moveOption = new MoveOption();

                switch (mNavigationMode) {
                    case NAVIGATION_MODE_FREE:
                        break;

                    case NAVIGATION_MODE_TRACK:
                        moveOption.setKeyPoints(true);
                        break;

                    case NAVIGATION_MODE_TRACK_OA:
                        moveOption.setKeyPoints(true);
                        moveOption.setTrackWithOA(true);
                        moveOption.setMilestone(true);
                        break;

                    default:
                        break;
                }

                platform.moveTo(locations, moveOption, 0f);

            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }
 private class TaskRotateTo implements Runnable {
        private Rotation rotation;

        public TaskRotateTo() {
        }

        public void setRotation(Rotation rotation) {
            this.rotation = rotation;
        }


        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null || rotation == null) return;
            }

            try {

                platform.rotateTo(rotation);
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }


    private class TaskMoveBy implements Runnable {
        MoveDirection moveDirection;

        public TaskMoveBy() {
            moveDirection = null;
        }

        public void setMoveDirection(MoveDirection moveDirection) {
            this.moveDirection = moveDirection;
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null || moveDirection == null) {
                    return;
                }
            }

            try {
                platform.moveBy(moveDirection);
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskGetTracks implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            List<Line> tracks;
            try {
                tracks = platform.getLines(ArtifactUsageVirtualTrack);
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new TrackGetEvent(tracks));
        }
    }

    private class TaskGetWalls implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            Vector<Line> walls;

            try {
                walls = platform.getWalls();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().post(new WallGetEvent(walls));
        }
    }

    private class TaskGoHome implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }
            if (platform == null) return;

            try {
                platform.goHome();
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskCancelAllActions implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) return;

            try {
                IMoveAction moveAction = platform.getCurrentAction();
                if (moveAction != null) {
                    moveAction.cancel();
                }
            } catch (Exception e) {
                onRequestError(e);
            }

            mPoolProxy.cancleAll();
        }
    }

    private class TaskGetRobotHealth implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            String errorMsg = "";
            List<Integer> errorList = new ArrayList<>();

            try {
                HealthInfo info = platform.getRobotHealth();

                if (info.isWarning() || info.isError() || info.isFatal() || (info.getErrors() != null && info.getErrors().size() > 0)) {

                    for (HealthInfo.BaseError error : info.getErrors()) {
                        String level;
                        switch (error.getErrorLevel()) {
                            case HealthInfo.BaseError.BaseErrorLevelWarn:
                                level = "Warning";
                                break;
                            case HealthInfo.BaseError.BaseErrorLevelError:
                                level = "Error";
                                break;
                            case HealthInfo.BaseError.BaseErrorLevelFatal:
                                level = "Fatal";
                                break;
                            default:
                                level = "Unknown";
                                break;
                        }
                        String component;
                        switch (error.getErrorComponent()) {
                            case HealthInfo.BaseError.BaseErrorComponentUser:
                                component = "User";
                                break;
                            case HealthInfo.BaseError.BaseErrorComponentMotion:
                                component = "Motion";
                                break;
                            case HealthInfo.BaseError.BaseErrorComponentPower:
                                component = "Power";
                                break;
                            case HealthInfo.BaseError.BaseErrorComponentSensor:
                                component = "Sensor";
                                break;
                            case HealthInfo.BaseError.BaseErrorComponentSystem:
                                component = "System";
                                break;
                            default:
                                component = "Unknown";
                                break;
                        }

                        errorList.add(error.getErrorCode());
                        errorMsg += String.format("Error ID: %d\nError level: %s\nError Component: %s\nError message: %s\nError ErrorCode: %d\n------\n", error.getId(), level, component, error.getErrorMessage(), error.getErrorCode());
                        EventBus.getDefault().post(new RobotHealthInfoEvent(errorMsg, errorList));
                    }
                }
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskgetCompositeMap implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            CompositeMap compositeMap = null;
            Map map = null;
            try {
                compositeMap = platform.getCompositeMap();
                RectF area = platform.getKnownArea(BITMAP_8BIT, MapKind.EXPLORE_MAP);
                map = platform.getMap(BITMAP_8BIT, MapKind.EXPLORE_MAP, area);
            } catch (Exception e) {
                onRequestError(e);
                return;
            }
            EventBus.getDefault().post(new GetCompositeMapEvent(compositeMap,map));
        }
    }

    private class TaskSetCompositeMap implements Runnable {

        private CompositeMap compositeMap;
        private Pose pose;

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.setCompositeMap(compositeMap, pose);
            } catch (Exception e) {
                onRequestError(e);
            }
        }

        public CompositeMap getCompositeMap() {
            return compositeMap;
        }

        public void setCompositeMap(CompositeMap compositeMap) {
            this.compositeMap = compositeMap;
        }

        public Pose getPose() {
            return pose;
        }

        public void setPose(Pose pose) {
            this.pose = pose;
        }
    }
    private class TaskSetHomePose implements Runnable {


        private Pose pose;

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.setHomePose(pose);
            } catch (Exception e) {
                onRequestError(e);
            }
        }


        public Pose getPose() {
            return pose;
        }

        public void setPose(Pose pose) {
            this.pose = pose;
        }
    }

    private class TaskClearRobotHealth implements Runnable {
        List<Integer> errors;

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                for (Integer error : errors) {
                    platform.clearRobotHealth(error);
                }
            } catch (Exception e) {
                onRequestError(e);
            }
        }

        public void setErrorCodes(List<Integer> errors) {
            this.errors = errors;
        }
    }
}
