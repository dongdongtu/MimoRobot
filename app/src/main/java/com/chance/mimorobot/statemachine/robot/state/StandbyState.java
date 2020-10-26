package com.chance.mimorobot.statemachine.robot.state;


import com.chance.mimorobot.statemachine.robot.RobotStateMachine;

/**
 * 活动状态
 * Created by Shao Shizhang on 2018/4/8.
 */
public class StandbyState extends DefaultState {

    public StandbyState(RobotStateMachine stateMachine) {
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
}
