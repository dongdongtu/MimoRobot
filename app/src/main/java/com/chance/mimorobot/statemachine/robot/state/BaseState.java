package com.chance.mimorobot.statemachine.robot.state;


import com.chance.mimorobot.statemachine.InputContent;

/**
 * 基本状态
 * Created by Shao Shizhang on 2018/4/8.
 */
public interface BaseState {

    /**
     * 进入状态要做的操作
     * @param enterParam    进入该状态可以传入一些参数。比如要初始化跳什么舞之类的
     */
    void OnEnter(Object enterParam);
    /**
     * 离开状态需要的操作
     */
    void OnExit();

    /**
     *  状态机接受输入做下一步操作
     * @param inputContent  输入的类容
     */
    void onProcess(InputContent inputContent);

    /**
     *  是否可以在同一个状态不停的进入和退出
     * @return  false,当前状态不能切换到当前状态。 true，当前状态可以重复进入当前状态
     */
   boolean canEnterMyself();

}
