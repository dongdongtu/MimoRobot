package com.chance.mimorobot.statemachine.robot.utils.aiui;


import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;

/**
 * 预设问答回应
 * Created by Shao Shizhang on 2018/5/28.
 */

public class PreinstalledAnswerProcess extends CommonResultProcess {

    @Override
    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
//        String text = aiuiResultEntity.getText();
//        String textResult = RobotHotSettings.getInstance().getVocalAnswer(text); //获取预设问答的结果
//        speakAnswer(textResult);
    }
}
