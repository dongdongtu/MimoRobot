package com.chance.mimorobot.statemachine.robot.input;


import com.chance.mimorobot.statemachine.InputContent;

/**
 * 开始或结束充电的输入
 * Created by Shao Shizhang on 2018/5/28.
 */
public class ChargeInput extends InputContent {

    private boolean start; //开始还是结束充电

    public ChargeInput(boolean start) {
        this.start = start;
    }

    public boolean isStart() {
        return start;
    }
}
