package com.chance.mimorobot.statemachine.robot;


import android.util.Log;

import com.chance.mimorobot.statemachine.BaseStateMachine;
import com.chance.mimorobot.statemachine.BaseStateMachine;
import com.chance.mimorobot.statemachine.robot.state.BaseState;
import com.chance.mimorobot.statemachine.robot.state.ChargeState;
import com.chance.mimorobot.statemachine.robot.state.CustomActionState;
import com.chance.mimorobot.statemachine.robot.state.DanceState;
import com.chance.mimorobot.statemachine.robot.state.RechargeState;
import com.chance.mimorobot.statemachine.robot.state.SleepState;
import com.chance.mimorobot.statemachine.robot.state.StandbyState;
import com.chance.mimorobot.statemachine.robot.state.VideoState;
import com.chance.mimorobot.statemachine.robot.state.WatchingState;

import java.util.ArrayList;
import java.util.List;




/**
 * 机器人状态机
 * Created by Shao Shizhang on 2018/4/8.
 */
public class RobotStateMachine extends BaseStateMachine {

    private final String TAG = getClass().getSimpleName();

    private int preState = 0; //前一个状态
    private volatile int currentState = 1;
    //    private int sleepTime = 5; //切入睡眠状态的时长，默认是5分钟
    public final static int SLEEP_STATE = 0;
    public final static int STAND_BY_STATE = SLEEP_STATE + 1;
    public final static int WATCHING_STATE = STAND_BY_STATE + 1;     //（已废弃）
    public final static int VIDEO_STATE = WATCHING_STATE + 1;
    public final static int CHARGE_STATE = VIDEO_STATE + 1;
    public final static int RECHARGE_STATE = CHARGE_STATE + 1;
    public final static int DANCE_STATE = RECHARGE_STATE + 1;
    public final static int SOUND_STATE = DANCE_STATE + 1;
    public final static int CUSTOM_ACTION_STATE = SOUND_STATE + 1;
    private List<BaseState> stateList = new ArrayList<>();

//    @Inject
    public RobotStateMachine() {
        resetStates();
    }

    @Override
    protected BaseState initStates() {
        addStates(new SleepState(this));
        addStates(new StandbyState(this));
        addStates(new WatchingState(this));
        addStates(new VideoState(this));
        addStates(new ChargeState(this));
        addStates(new RechargeState(this));
        addStates(new DanceState(this));
        addStates(new CustomActionState(this));
        return stateList.get(currentState);
    }

    private void addStates(BaseState baseState) {
        stateList.add(baseState);
    }

    private boolean canEnterSameState() {
        return mCurBaseState == null || mCurBaseState.canEnterMyself();
    }

    /**
     * 导航到新的状态
     *
     * @param stateIndex 需要导航到的新状态
     * @param enterParam 进入该状态需要的参数
     */
    public void navigationNewState(int stateIndex, Object enterParam) {
        if (currentState == stateIndex && !canEnterSameState()) {
            //cannot enter a same state
            return;
        }
        //如果同样状态转到相同状态，不要修改之前的状态状态，否则会丢失之前的状态信息
        if (currentState != stateIndex || mCurBaseState == null) {
            preState = currentState;
            currentState = stateIndex;
        }

        if (mCurBaseState != null) {
            mCurBaseState.OnExit();
        }
        if (currentState < 0 || currentState >= stateList.size()) {
//            this.SetState(new ToleranceState(), null);   //容错处理
            return;
        }
        mCurBaseState = stateList.get(currentState);
        mCurBaseState.OnEnter(enterParam);
        Log.i(TAG, "navigationNewState: " + mCurBaseState.getClass().getSimpleName());
    }

    /**
     * 导航到新的状态
     *
     * @param stateIndex 需要导航到的新状态
     */
    public void navigationNewState(int stateIndex) {
        //默认传null参数进入状态机
        navigationNewState(stateIndex, null);
    }

    /**
     * 初始化状态
     */
    @Override
    public void resetStates() {
        super.resetStates();
    }


    public int getPreState() {
        return preState;
    }

    public int getCurrentState() {
        return currentState;
    }

}
