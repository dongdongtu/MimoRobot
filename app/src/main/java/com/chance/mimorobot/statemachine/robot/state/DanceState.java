package com.chance.mimorobot.statemachine.robot.state;


import com.chance.mimorobot.statemachine.robot.RobotStateMachine;

/**
 * Created by Administrator on 2018/5/12.
 */

public class DanceState extends DefaultState {
    public DanceState(RobotStateMachine stateMachine) {
        super(stateMachine);
    }

    protected void onProcessDanceInput(boolean isStart) {
        if (!isStart) {
            stateMachine.navigationNewState(RobotStateMachine.STAND_BY_STATE);
        }
    }

    protected void onProcessChargeInput(boolean isStart) {
        //在跳舞状态中，不能进入充电状态
    }

    protected void onProcessSerialInput(int type) {
        //在跳舞状态中，不响应触摸
    }

}
