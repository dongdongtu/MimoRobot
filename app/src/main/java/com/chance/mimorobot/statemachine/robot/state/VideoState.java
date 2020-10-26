package com.chance.mimorobot.statemachine.robot.state;

import com.chance.mimorobot.statemachine.RobotStatus;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;



/**
 * 视频中状态（现在包含监控）
 * Created by Shao Shizhang on 2018/4/14.
 */
public class VideoState extends DefaultState {

    public VideoState(RobotStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void OnEnter(Object enterParam) {
    }

    @Override
    public void OnExit() {
    }

    @Override
    public boolean canEnterMyself() {
        return false;
    }

    protected void onProcessAwakenInput(int angle) {

    }

    protected void onProcessVideoInput(boolean isStart) {
        if (!isStart) {
            //结束视频
            if(!RobotStatus.getInstance().isCharging()) {
                stateMachine.navigationNewState(RobotStateMachine.STAND_BY_STATE);//返回活跃状态
//            stateMachine.navigationNewState(stateMachine.getPreState());//返回上一个状态
            } else {
                stateMachine.navigationNewState(RobotStateMachine.CHARGE_STATE);
            }
        }
    }

    protected void onProcessChargeInput(boolean isStart) {
        //在视频状态中，不能进入充电状态
    }

}
