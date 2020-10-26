package com.chance.mimorobot.statemachine.robot.input;


import com.chance.mimorobot.statemachine.InputContent;

/**
 * 唤醒输入
 * Created by Shao Shizhang on 2018/4/8.
 */
public class AwakenInput extends InputContent {
    private int angle;

    public AwakenInput(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
