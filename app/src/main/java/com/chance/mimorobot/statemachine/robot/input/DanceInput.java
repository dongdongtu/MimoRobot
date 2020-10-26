package com.chance.mimorobot.statemachine.robot.input;


import com.chance.mimorobot.statemachine.InputContent;

/**
 * 跳舞信号输入
 * Created by Shao Shizhang on 2018/5/31.
 */
public class DanceInput extends InputContent {

    private boolean start; //开始还是结束跳舞

    public DanceInput(boolean start) {
        this.start = start;
    }

    public boolean isStart() {
        return start;
    }
}
