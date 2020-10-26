package com.chance.mimorobot.statemachine;


import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.chance.mimorobot.statemachine.robot.input.AiuiResultInput;
import com.chance.mimorobot.statemachine.robot.input.AwakenInput;
import com.chance.mimorobot.statemachine.robot.input.ChargeInput;
import com.chance.mimorobot.statemachine.robot.input.CustomActionInput;
import com.chance.mimorobot.statemachine.robot.input.DanceInput;
import com.chance.mimorobot.statemachine.robot.input.SerialportInput;
import com.chance.mimorobot.statemachine.robot.input.TouchInput;
import com.chance.mimorobot.statemachine.robot.input.VideoInput;
import com.chance.mimorobot.statemachine.robot.input.WatchingInput;

/**
 * Created by Shao Shizhang on 2018/4/13.
 */

public class StateMachineManager {
    private RobotStateMachine mStateMachine; //状态机

    public static StateMachineManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static StateMachineManager INSTANCE = new StateMachineManager();
    }


    private StateMachineManager() {
    }

    public void init() {
        mStateMachine = new RobotStateMachine(); //建立状态机
    }

    /**
     * 输入：唤醒
     *
     * @param angle 唤醒角度数值
     */
    public void inputAwaken(int angle) {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new AwakenInput(angle));
        }
    }

    /**
     * 输入：唤醒 （无角度值）
     */
    public void inputAwaken() {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new AwakenInput(0));
        }
    }

    /**
     * 输入：触碰屏幕任意位置
     */
    public void inputTouch() {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new TouchInput());
        }
    }

    /**
     * @param type{@link SerialParser}
     */
    public void inputSerialport(int type) {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new SerialportInput(type));
        }
    }

    /**
     * 输入：监控
     *
     * @param start 开始或结束
     */
    public void inputWatching(boolean start) {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new WatchingInput(start));
        }
    }

    /**
     * 输入：视频通话
     *
     * @param start 开始或结束
     */
    public void inputVideo(boolean start) {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new VideoInput(start));
        }
    }

    /**
     * 输入：充电状态改变
     *
     * @param start 开始充电或结束充电
     */
    public void inputCharge(boolean start) {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new ChargeInput(start));
        }
    }

    /**
     * 输入：跳舞状态改变
     *
     * @param start 开始充电或结束跳舞
     */
    public void inputDance(boolean start) {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new DanceInput(start));
        }
    }

    public void inputCustomAction(boolean start) {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new CustomActionInput(start));
        }
    }

    public void inputAiuiResult(AiuiResultEntity aiuiResultEntity) {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new AiuiResultInput(aiuiResultEntity));
        }
    }

    public void inputAiuiResult(String aiuiResult) {
        if (mStateMachine != null) {
            mStateMachine.onProcess(new AiuiResultInput(aiuiResult));
        }
    }

//    public RobotStateMachine getStateMachine() {
//        return mStateMachine;
//    }

    public int getCurrentState() {
        return mStateMachine.getCurrentState();
    }
}
