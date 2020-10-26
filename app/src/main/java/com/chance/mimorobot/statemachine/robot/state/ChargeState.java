package com.chance.mimorobot.statemachine.robot.state;


import com.chance.mimorobot.statemachine.robot.RobotStateMachine;

/**
 * Created by Administrator on 2018/5/12.
 */

public class ChargeState extends DefaultState {
    public static int WIRE = 0;
    public static int NO_WIRE = 1;
    public ChargeState(RobotStateMachine stateMachine) {
        super(stateMachine);
    }

    protected void onProcessChargeInput(boolean isStart) {
        if (!isStart) {
            //结束充电
            stateMachine.navigationNewState(RobotStateMachine.STAND_BY_STATE);//返回默认活跃状态
        }
    }
}
