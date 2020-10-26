package com.chance.mimorobot.statemachine.robot.input;


import com.chance.mimorobot.statemachine.InputContent;

/**
 * 触摸输入
 * Created by Shao Shizhang on 2018/4/14.
 */
public class VideoInput extends InputContent {

    private boolean start; //开始还是结束通话

    public VideoInput(boolean start) {
        this.start = start;
    }

    public boolean isStart() {
        return start;
    }
}
