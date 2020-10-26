package com.chance.mimorobot.statemachine.robot.utils.aiui;


import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;

/**
 * Created by Administrator on 2018/5/14.
 */

public abstract class BaseReslutProcess {
    /**
     * 处理语义结果
     *
     * @param aiuiResultEntity
     * @param robotStateMachine
     */
    public abstract void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine);

    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine){};

//    public abstract void onProcessQuestion(String question);
//    /**
//     * 处理回答
//     *
//     * @param answer
//     */
//    public abstract void onProcessAnswer(String answer);
//
//    /**
//     * \处理mp3
//     * @param mp3Url
//     */
//    public abstract void onProcessMp3(String mp3Url);
}
