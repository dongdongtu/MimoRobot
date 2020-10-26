package com.chance.mimorobot.statemachine.robot.state;

import com.chance.mimorobot.statemachine.InputContent;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.chance.mimorobot.statemachine.robot.input.AwakenInput;
import com.chance.mimorobot.statemachine.robot.input.WatchingInput;


/**
 * 监控中状态（已废弃，职能并入VideoState）
 * Created by Shao Shizhang on 2018/4/14.
 */
public class WatchingState implements BaseState {

    private RobotStateMachine stateMachine;

    public WatchingState(RobotStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public void OnEnter(Object enterParam) {
    }

    @Override
    public void OnExit() {
    }

    @Override
    public void onProcess(InputContent inputContent) {
        if (inputContent instanceof AwakenInput)
            onProcessAwakenInput(((AwakenInput) inputContent).getAngle());
        else if (inputContent instanceof WatchingInput)
            onProcessWatchingInput(((WatchingInput) inputContent).isStart());
    }


    @Override
    public boolean canEnterMyself() {
        return false;
    }

    protected void onProcessAwakenInput(int angle) {

    }

    protected void onProcessWatchingInput(boolean isStart) {
        if (!isStart) {
            //结束监控的输入
            stateMachine.navigationNewState(stateMachine.getPreState());//返回上一状态
        }
    }

}
