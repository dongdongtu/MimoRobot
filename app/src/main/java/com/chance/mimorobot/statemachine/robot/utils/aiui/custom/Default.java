package com.chance.mimorobot.statemachine.robot.utils.aiui.custom;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;


import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.activity.CameraActivity;
import com.chance.mimorobot.activity.SettingActivity;
import com.chance.mimorobot.activity.WifiActivity;
import com.chance.mimorobot.helper.RobotFunctionSettings;
import com.chance.mimorobot.manager.SerialControlManager;
import com.chance.mimorobot.manager.SlamManager;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.service.FaceInfoService;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.utils.aiui.constants.AiuiConstants;
import com.chance.mimorobot.statemachine.robot.utils.aiui.constants.MoveActionConstants;
import com.chance.mimorobot.statemachine.robot.utils.aiui.constants.OtherPageConstants;
import com.slamtec.slamware.action.MoveDirection;


import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.chuangze.robot.aiuilibrary.AIUIWrapper;
import cn.chuangze.robot.serial.api.SerialControl;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

import static com.blankj.utilcode.util.ActivityUtils.getTopActivity;


/**
 * Created by Administrator on 2018/5/25.
 */

public class Default {
    //    HardwareInterface hardwareInterface = HardwareManager.getInstance();
//    AiuiInterface aiuiInterface = AiuiManager.getInstance();
//    DanceInterface danceInterface = DanceManager.getInstance();
    private String textAnswer = "";
    private final String LogTag = "Default sound";

    public void onProcess(AiuiResultEntity aiuiResultEntity) {
        if (aiuiResultEntity == null) return;

        String intent = "", normValue = "";
        if (aiuiResultEntity.getSemantic() != null && !aiuiResultEntity.getSemantic().isEmpty() && aiuiResultEntity.getSemantic().get(0) != null) {
            intent = aiuiResultEntity.getSemantic().get(0).getIntent();
            if (aiuiResultEntity.getSemantic().get(0).getSlots() != null && !aiuiResultEntity.getSemantic().get(0).getSlots().isEmpty() && aiuiResultEntity.getSemantic().get(0).getSlots().get(0) != null) {
                normValue = aiuiResultEntity.getSemantic().get(0).getSlots().get(0).getNormValue();
            }
        }
        if (aiuiResultEntity.getAnswer() != null) {
            textAnswer = aiuiResultEntity.getAnswer().getText();
            Log.i("customAction", "textAnswer = " + textAnswer);
        }

        switch (intent) {
            case AiuiConstants.IntentConstants.ACTION_INTENT:
                for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : aiuiResultEntity.getSemantic().get(0).getSlots()) {
                    if (slotsBean.getName().equals(AiuiConstants.SemanticConstants.MOVE_ACTION_SEMANTIC)) {
                        normValue = slotsBean.getNormValue();
                        break;
                    }
                }
                Log.i("Default Process", "doAction=" + normValue);
                doAction(normValue);
                break;
            case AiuiConstants.IntentConstants.COMMAND_INTENT:
                for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : aiuiResultEntity.getSemantic().get(0).getSlots()) {
                    if (slotsBean.getName().equals(AiuiConstants.SemanticConstants.COMMAND_SEMANTIC)) {
                        normValue = slotsBean.getNormValue();
                        break;
                    }
                }
                Log.i("Default Process", "doCommand=" + normValue);
                doCommand(normValue);
                break;
            case AiuiConstants.IntentConstants.DEFAULT_INTENT:
                for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : aiuiResultEntity.getSemantic().get(0).getSlots()) {
                    if (slotsBean.getName().equals(AiuiConstants.SemanticConstants.COMMAND_SEMANTIC)) {
                        normValue = slotsBean.getNormValue();
                        break;
                    }
                }
                Log.i("Default Process", "doCommand=" + normValue);
                doCommand(normValue);
                break;
            case AiuiConstants.IntentConstants.OTHER_PAGE_INTENT:
                for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : aiuiResultEntity.getSemantic().get(0).getSlots()) {
                    if (slotsBean.getName().equals(AiuiConstants.SemanticConstants.OTHER_PAGE_SEMANTIC)) {
                        normValue = slotsBean.getNormValue();
                        break;
                    }
                }
                Log.i("Default Process", "openOtherPage=" + normValue);
                openOtherPage(normValue);
                break;
            default:
                break;
        }
    }

//    private void backToFaceDoAction(String normValue) {
//        switch (normValue) {
//            case MoveActionConstants.sick:
//                Output.changeExpression(ExpressionType.shocked);
//                textAnswer = "ok";
//                break;
//            case MoveActionConstants.shake:
//                Output.changeExpression(ExpressionType.laugh);
//                hardwareInterface.doAction(ActionType.shakeHead);
//                textAnswer = "ok";
//                break;
//            case MoveActionConstants.smile:
//                Output.changeExpression(ExpressionType.laugh);
//                break;
//            case MoveActionConstants.stay_with_me:
//                Output.changeExpression(ExpressionType.pathetic);
//                hardwareInterface.doAction(ActionType.hug);
//                textAnswer = "ok";
//                break;
//            case MoveActionConstants.are_you_happy:
//                Output.changeExpression(ExpressionType.cute);
//                hardwareInterface.doAction(ActionType.hug);
//                textAnswer = "ok";
//                break;
//            case MoveActionConstants.handshake:
//                Output.changeExpression(ExpressionType.laugh);
//                hardwareInterface.doAction(ActionType.shakeHand);
//                break;
//            case MoveActionConstants.turn_around:
//                Output.changeExpression(ExpressionType.laugh);
//                hardwareInterface.turnAround(360);
//                break;
//            case MoveActionConstants.dance:
//                Output.changeExpression(ExpressionType.laugh);
//                List<MusicListModel> list = RobotHotSettings.getInstance().getDanceMusics();
//                if (list != null && list.size() != 0) {
//                    MusicListModel model = list.get(new Random().nextInt(list.size()));
//                    if (model != null) {
//                        danceInterface.startDance(model.getUrl());
//                    }
//                }
//                break;
//            case MoveActionConstants.hug:
//                Output.changeExpression(ExpressionType.cute);
//                hardwareInterface.doAction(ActionType.hug);
//                break;
//            case MoveActionConstants.eat:
//                Output.changeExpression(ExpressionType.laugh);
//                break;
//            case MoveActionConstants.weep:
//                Output.changeExpression(ExpressionType.weep);
//                break;
//            case MoveActionConstants.you_are_beautiful:
//                Output.changeExpression(ExpressionType.laugh);
//
//                break;
//        }
//    }

    public void doAction(final String normValue) {
        Log.i(LogTag, "doAction: " + normValue);
        boolean changeSpeakingFace = true;
        switch (normValue) {
            case MoveActionConstants.sick:
            case MoveActionConstants.shake:
            case MoveActionConstants.smile:
                SerialControlManager.newInstance().setFace(8);
                break;
            case MoveActionConstants.stay_with_me:
            case MoveActionConstants.are_you_happy:
                break;
            case MoveActionConstants.handshake:
                Log.e("handshake", "handshake");
                SerialControlManager.newInstance().armRightTurnUp(30);
                Flowable.timer(6, TimeUnit.SECONDS).subscribe(
                        new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                SerialControlManager.newInstance().armRightTurnDown(30);
                            }
                        }
                );
                break;
            case MoveActionConstants.arm_left_down:
                SerialControlManager.newInstance().armLeftTurnDown(30);
                break;
            case MoveActionConstants.arm_left_up:
                SerialControlManager.newInstance().armLeftTurnUp(30);
                break;
            case MoveActionConstants.arm_light_down:
                SerialControlManager.newInstance().armRightTurnDown(30);
                break;
            case MoveActionConstants.arm_light_up:
                SerialControlManager.newInstance().armRightTurnUp(30);
                break;
            case MoveActionConstants.turn_around:
            case MoveActionConstants.dance:
            case MoveActionConstants.hug:
            case MoveActionConstants.eat:
                break;
            case MoveActionConstants.weep:
                SerialControlManager.newInstance().setFace(1);
                break;
            case MoveActionConstants.you_are_beautiful:
                changeSpeakingFace = false;
//                Output.navigatorActivity(FaceActivity.class);
                //修复
                //问题：在功能界面 触发换脸的指令 无法回到face页面  解决：先回到Face页面 延时换脸
//                Flowable.timer(1, TimeUnit.SECONDS)
//                        .subscribe(new Consumer<Long>() {
//                            @Override
//                            public void accept(Long aLong) throws Exception {
//                                backToFaceDoAction(normValue);
//                            }
//                        });
                break;
            case MoveActionConstants.are_you_sad:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.doAction(ActionType.shakeHead);
                break;
            case MoveActionConstants.come_here:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.doAction(ActionType.stepAhead);
                break;
            case MoveActionConstants.goodbye:
//                hardwareInterface.robotFreeWalk();
                break;
            case MoveActionConstants.look_me:
//                Output.changeExpression(ExpressionType.main);
                break;
            case MoveActionConstants.look_down:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.doAction(ActionType.headDown);
                break;
            case MoveActionConstants.look_up:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.doAction(ActionType.headUp);
                break;
            case MoveActionConstants.look_right:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.doAction(ActionType.headRight);
                break;
            case MoveActionConstants.look_left:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.doAction(ActionType.headLeft);
                break;
            case MoveActionConstants.nod:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.doAction(ActionType.nod);
                break;
            case MoveActionConstants.photo:
                if (ActivityUtils.getTopActivity() instanceof CameraActivity) {
                    ((CameraActivity) ActivityUtils.getTopActivity()).takeShot();
                }
                break;
            case MoveActionConstants.forward:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.doAction(ActionType.stepAhead);
                SlamManager.getInstance().move(MoveDirection.FORWARD);
                break;
            case MoveActionConstants.hello:
//                Output.changeExpression(ExpressionType.main);
                break;
            case MoveActionConstants.to_recharge:
                textAnswer = "好的";
//                hardwareInterface.doAction(ActionType.toReCharge);
                break;
            case MoveActionConstants.back:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.doAction(ActionType.stepBack);
                SlamManager.getInstance().move(MoveDirection.BACKWARD);
                break;
            case MoveActionConstants.turn_left:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.turnAround(90);
                SlamManager.getInstance().move(MoveDirection.TURN_LEFT);
                break;
            case MoveActionConstants.turn_right:
//                Output.changeExpression(ExpressionType.main);
//                hardwareInterface.turnAround(-90);
                SlamManager.getInstance().move(MoveDirection.TURN_RIGHT);
                break;
            case MoveActionConstants.open_drawer:
//                hardwareInterface.doAction(ActionType.openDrawer);
//                if (ActivityUtils.getTopActivity() instanceof FunctionActivity) {
//                    ((FunctionActivity) ActivityUtils.getTopActivity()).changeDrawerIcon();
//                }
                break;
            case MoveActionConstants.close_drawer:
//                hardwareInterface.doAction(ActionType.closeDrawer);
//                if (ActivityUtils.getTopActivity() instanceof FunctionActivity) {
//                    ((FunctionActivity) ActivityUtils.getTopActivity()).changeDrawerIcon();
//                }
                break;
            case MoveActionConstants.salute:
//                hardwareInterface.doAction(ActionType.salute);
                break;
            case MoveActionConstants.surrender:
//                hardwareInterface.doAction(ActionType.surrender);
                break;
            case MoveActionConstants.volume_up:
                RobotFunctionSettings.getInstance().volumeUp();
                break;
            case MoveActionConstants.volume_down:
                RobotFunctionSettings.getInstance().volumeDown();
                break;
            case MoveActionConstants.speech_speed_up:
                int speed = RobotFunctionSettings.getInstance().speechSpeedUp();
                if (speed < 100) {
                    textAnswer = "好的，已为您将语速调快";
                } else if (speed == 100) {
                    textAnswer = "当前已经是最快语速啦";
                }
                break;
            case MoveActionConstants.speech_speed_down:
                int speed1 = RobotFunctionSettings.getInstance().speechSpeedDown();
                if (speed1 < 100 && speed1 > 10) {
                    textAnswer = "好的，已为您将语速调慢";
                } else if (speed1 == 10) {
                    textAnswer = "当前已经是最慢语速啦";
                }
                break;

//            case MoveActionConstants.quit:
//                textAnswer = "好的";
//                Output.navigatorActivity(FaceActivity.class);
//                Output.killEducationActivity();
//                break;
//            case MoveActionConstants.inside_temperature:
//                hardwareInterface.readTemperature().subscribe(new Consumer<Float>() {
//                    @Override
//                    public void accept(Float aFloat) {
//                        Output.speak("你好，为您检测到当前室内温度是" + (int) (aFloat - 1) + "摄氏度");
//                    }
//                });
//                break;
//            case MoveActionConstants.inside_humidity:
//                hardwareInterface.readHumidity().subscribe(new Consumer<Float>() {
//                    @Override
//                    public void accept(Float aFloat) throws Exception {
//                        int humidity = (int) (float) aFloat;
//                        Output.speak("你好，为您检测到当前室内湿度是百分之" + humidity);
//                    }
//                });
//                break;
            case MoveActionConstants.check_update:
//                Beta.checkUpgrade();
                break;
            default:
                break;
        }
        Output.speak(textAnswer, changeSpeakingFace);
    }

    public void doCommand(String normValue) {
        switch (normValue) {
            case "stop":
                Output.disableCanCallback();
                Output.stopSpeak();
                Output.stopPlayUrl();
//                hardwareInterface.doAction(ActionType.stop);
                break;
            case "stop_speak":
                Output.disableCanCallback();
                Output.stopSpeak();
                Output.stopPlayUrl();
                break;
            case "sleep":
                Output.disableCanCallback();
                Output.stopSpeak();
                Output.stopPlayUrl();
//                hardwareInterface.doAction(ActionType.stop);
//                aiuiInterface.sleep();
//                Output.navigatorActivity(FaceActivity.class);
                textAnswer = "";
                Output.speak(textAnswer, false);
                Output.goSleep();
                break;
            default:
                break;
        }
    }

    private void openOtherPage(String normValue) {
        if (StringUtils.isEmpty(normValue)) return;
        Log.i(LogTag, "other_page_intent, normValue = " + normValue);

        if (StringUtils.isEmpty(textAnswer)) {
            textAnswer = "好";
        }

        Activity currentActivity = ActivityUtils.getTopActivity();
        switch (normValue) {

            case OtherPageConstants.SETTING_FACE_RECORD:
                if (ActivityUtils.getTopActivity() instanceof CameraActivity) {
                    Output.speak(textAnswer);
                    return;
                }
                getTopActivity().stopService(new Intent( getTopActivity().getApplicationContext(), FaceInfoService.class));
                Output.navigatorActivity(CameraActivity.class);
                break;

            case OtherPageConstants.WANT_TAKE_PHOTO:
                if (ActivityUtils.getTopActivity() instanceof CameraActivity) {
                    ((CameraActivity) ActivityUtils.getTopActivity()).takeShot();
                } else {
                    getTopActivity().stopService(new Intent( getTopActivity().getApplicationContext(), FaceInfoService.class));
                    Output.navigatorActivity(CameraActivity.class);
                }
                break;
            case OtherPageConstants.STOP_CAMERA:
                if (ActivityUtils.getTopActivity() instanceof CameraActivity) {
                    ActivityUtils.getTopActivity().finish();
                }
                break;
        }
        if (!StringUtils.isEmpty(textAnswer)) {
            Output.speak(textAnswer);
        }
    }
}
