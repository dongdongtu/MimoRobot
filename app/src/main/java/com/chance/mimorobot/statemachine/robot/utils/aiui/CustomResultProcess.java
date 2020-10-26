package com.chance.mimorobot.statemachine.robot.utils.aiui;

import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.chance.mimorobot.statemachine.robot.utils.aiui.custom.Default;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.chance.mimorobot.statemachine.robot.utils.aiui.custom.Charge;
import com.chance.mimorobot.statemachine.robot.utils.aiui.custom.Dance;
import com.chance.mimorobot.statemachine.robot.utils.aiui.custom.Default;
import com.chance.mimorobot.statemachine.robot.utils.aiui.custom.ReCharge;
import com.chance.mimorobot.statemachine.robot.utils.aiui.custom.Sleep;
import com.chance.mimorobot.statemachine.robot.utils.aiui.custom.StandBy;
import com.chance.mimorobot.statemachine.robot.utils.aiui.custom.Video;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2018/5/14.
 */

public class CustomResultProcess extends CommonResultProcess {
    @Override
    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine) {
        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
        }.getType();
        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);
//        AiuiResultEntity aiuiResultEntity = new AiuiResultEntityJsonMapper().transformAiuiResultEntity(aiuiResult);
        onProcessAiuiResult(aiuiResultEntity, robotStateMachine);
    }

    @Override
    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
        Default aDefault = null;
        switch (robotStateMachine.getCurrentState()) {
            case RobotStateMachine.SLEEP_STATE:
                aDefault = new Sleep();
                break;
            case RobotStateMachine.STAND_BY_STATE:
                aDefault = new StandBy();
                break;
            case RobotStateMachine.WATCHING_STATE:
            case RobotStateMachine.VIDEO_STATE:
                aDefault = new Video();
                break;
            case RobotStateMachine.CHARGE_STATE:
                aDefault = new Charge();
                break;
            case RobotStateMachine.RECHARGE_STATE:
                aDefault = new ReCharge();
                break;
            case RobotStateMachine.DANCE_STATE:
                aDefault = new Dance();
                break;
            default:
                aDefault = new StandBy();
                break;
        }
        aDefault.onProcess(aiuiResultEntity);
    }
}
