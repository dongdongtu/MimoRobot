package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.helper.RobotFunctionSettings;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


/**
 * created by Lynn
 * on 2019/8/8
 */
public class CmdResultProcess extends CommonResultProcess {
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
        String normValue = "";

        if (aiuiResultEntity != null && aiuiResultEntity.getSemantic() != null && !aiuiResultEntity.getSemantic().isEmpty()
                && aiuiResultEntity.getSemantic().get(0).getSlots() != null && !aiuiResultEntity.getSemantic().get(0).getSlots().isEmpty()) {

            if (StringUtils.equals(aiuiResultEntity.getSemantic().get(0).getIntent(), "INSTRUCTION")) {
                for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : aiuiResultEntity.getSemantic().get(0).getSlots()) {
                    if (StringUtils.equals(slotsBean.getName(), "insType")) {
                        normValue = slotsBean.getValue();
                        break;
                    }
                }
            }
            Log.i("CmdResultProcess", "normValue=" + normValue);

            if (StringUtils.isEmpty(normValue)) {
                return;
            }

            switch (normValue) {
                case "sleep":
                    Output.stopSpeak();
                    break;
                case "volume_plus":
                    RobotFunctionSettings.getInstance().volumeUp();
                    break;
                case "volume_max":
                    RobotFunctionSettings.getInstance().volumeMax();
                    Output.speak("好的");
                    break;
                case "volume_min":
                    RobotFunctionSettings.getInstance().volumeMin();
                    Output.speak("好的");
                    break;
                case "mute":
                    RobotFunctionSettings.getInstance().volumeMute();
                    Output.speak("好的");

                    break;
            }

        }
    }
}
