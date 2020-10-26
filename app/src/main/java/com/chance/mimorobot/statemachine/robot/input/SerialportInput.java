package com.chance.mimorobot.statemachine.robot.input;


import com.chance.mimorobot.statemachine.InputContent;

/**
 * Created by Administrator on 2018/5/16.
 */

public class SerialportInput extends InputContent {
    public int type;

    public SerialportInput(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
