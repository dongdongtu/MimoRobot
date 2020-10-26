package com.chance.mimorobot.statemachine.robot.state;


import com.chance.mimorobot.statemachine.robot.RobotStateMachine;

/**
 * 静止状态
 * Created by Shao Shizhang on 2018/4/8.
 */
public class SleepState extends DefaultState {

    public SleepState(RobotStateMachine stateMachine) {
        super(stateMachine);
    }

    @Override
    public void OnEnter(Object enterParam) {
    }

    @Override
    public void OnExit() {
    }


    /**
     * 是否可以在同一个状态不停的进入和退出
     *
     * @return false, 当前状态不能切换到当前状态。 true，当前状态可以重复进入当前状态
     */
    @Override
    public boolean canEnterMyself() {
        return false;
    }

    protected void onProcessAwakenInput(int angle) {
        stateMachine.navigationNewState(RobotStateMachine.STAND_BY_STATE);
    }

    protected void onProcessTouchInput() {
        stateMachine.navigationNewState(RobotStateMachine.STAND_BY_STATE);
    }

    protected void onProcessWatchingInput(boolean isStart) {
        if (isStart) {
            //开始监控的输入
            stateMachine.navigationNewState(RobotStateMachine.WATCHING_STATE);//进入监控状态
        }
    }

    protected void onProcessVideoInput(boolean isStart) {
        if (isStart) {
            //开始视频的输入
            stateMachine.navigationNewState(RobotStateMachine.VIDEO_STATE);//进入视频状态
        }
    }

}
