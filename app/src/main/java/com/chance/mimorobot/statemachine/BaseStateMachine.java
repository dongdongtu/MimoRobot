package com.chance.mimorobot.statemachine;


import com.chance.mimorobot.statemachine.robot.state.BaseState;

/**
 * 基本状态机
 * Created by Shao Shizhang on 2018/4/8.
 */

public class BaseStateMachine {

    protected BaseState mCurBaseState;

    public BaseStateMachine() {


    }

    /**
     * 用于转换状态到目标状态
     *
     * @param targetState 需要转换到的状态
     */
    protected void setState(BaseState targetState, Object enterParam) {
        mCurBaseState.OnExit();
        mCurBaseState = targetState;
        mCurBaseState.OnEnter(enterParam);
    }

    /**
     * 用于产生的输出进入状态机
     *
     * @param inputContent
     */
    public void onProcess(InputContent inputContent) {
        mCurBaseState.onProcess(inputContent);
    }

    /**
     * 创建所有状态，并且返回一个初始状态。
     *
     * @return BaseState 返回一个默认的状态
     */
    protected BaseState initStates() {
        return null;
    }

    /**
     * 重置状态机
     * 重置状态机会直接调用 initStates();
     */
    protected void resetStates() {
        if (mCurBaseState != null) {
            mCurBaseState.OnExit();
            mCurBaseState = null;
        }
        //构造函数里面进行状态初始化。
        BaseState state = initStates();
        setDefaultState(state);
    }


    /**
     * 用于设置初始默认的状态机
     *
     * @param state 每次设置新的状态机都要强制调用
     */
    private void setDefaultState(BaseState state) {
        mCurBaseState = state;
        state.OnEnter(null);
    }

}
