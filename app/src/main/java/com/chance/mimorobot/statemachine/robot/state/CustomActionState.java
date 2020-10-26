package com.chance.mimorobot.statemachine.robot.state;


import com.chance.mimorobot.statemachine.robot.RobotStateMachine;

/**
 * created by Lynn
 * on 2019/11/11
 */
public class CustomActionState extends DefaultState {

    public CustomActionState(RobotStateMachine stateMachine) {
        super(stateMachine);
    }

    protected void onProcessSerialInput(int type) {
        //在执行自定义动作状态中，不响应触摸
    }
}
