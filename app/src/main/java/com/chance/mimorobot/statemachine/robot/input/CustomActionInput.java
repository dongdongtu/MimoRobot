package com.chance.mimorobot.statemachine.robot.input;


import com.chance.mimorobot.statemachine.InputContent;

/**
 * created by Lynn
 * on 2019/11/11
 */
public class CustomActionInput extends InputContent {

    private boolean start;

    public CustomActionInput(boolean start) {
        this.start = start;
    }

    public boolean isStart() {
        return start;
    }
}
