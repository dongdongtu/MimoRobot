package com.chance.mimorobot.statemachine.robot.state;


import android.util.Log;

import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.InputContent;
import com.chance.mimorobot.statemachine.RobotStatus;
import com.chance.mimorobot.statemachine.robot.Output;
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
import com.chance.mimorobot.statemachine.robot.utils.SerialPortReceiver;
import com.chance.mimorobot.statemachine.robot.utils.aiui.AiuiResultUtil;

/**
 * Created by Administrator on 2018/5/12.
 */

public class DefaultState implements BaseState {

    protected RobotStateMachine stateMachine;

    public DefaultState(RobotStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public void OnEnter(Object enterParam) {
    }

    @Override
    public void OnExit() {
    }

    @Override
    public void onProcess(InputContent inputContent) {
        if (inputContent instanceof AwakenInput) {
            onProcessAwakenInput(((AwakenInput) inputContent).getAngle());
        } else if (inputContent instanceof TouchInput) {
            onProcessTouchInput();
        } else if (inputContent instanceof WatchingInput) {
            onProcessWatchingInput(((WatchingInput) inputContent).isStart());
        } else if (inputContent instanceof VideoInput) {
            onProcessVideoInput(((VideoInput) inputContent).isStart());
        } else if (inputContent instanceof AiuiResultInput) {
            onProcessAiuiResultInput(((AiuiResultInput) inputContent).getAiuiResult());
        } else if (inputContent instanceof SerialportInput) {
            onProcessSerialInput(((SerialportInput) inputContent).getType());
        } else if (inputContent instanceof ChargeInput) {
            onProcessChargeInput(((ChargeInput) inputContent).isStart());
        } else if (inputContent instanceof DanceInput) {
            onProcessDanceInput(((DanceInput) inputContent).isStart());
        } else if (inputContent instanceof CustomActionInput) {
            onProcessCustomActionInput(((CustomActionInput) inputContent).isStart());
        }
    }

    /**
     * 是否可以在同一个状态不停的进入和退出
     *
     * @return false, 当前状态不能切换到当前状态。 true，当前状态可以重复进入当前状态
     */
    @Override
    public boolean canEnterMyself() {
        return false;
    }


    protected void onProcessAwakenInput(int angle) {
        Output.stopSpeak();
        Output.stopPlayUrl();
    }

    protected void onProcessTouchInput() {

    }

    protected void onProcessWatchingInput(boolean isStart) {
        //如果在活跃状态可以监控，做相应处理
        if (isStart) {
            //开始监控的输入
            stateMachine.navigationNewState(RobotStateMachine.WATCHING_STATE);//进入监控状态
        }
    }

    protected void onProcessVideoInput(boolean isStart) {
        if (isStart) {
            //开始视频的输入
            stateMachine.navigationNewState(RobotStateMachine.VIDEO_STATE);//进入视频状态
        }
    }

    protected void onProcessChargeInput(boolean isStart) {
        if (isStart) {
            //开始充电的输入
            stateMachine.navigationNewState(RobotStateMachine.CHARGE_STATE);//进入充电状态
        }
    }

    protected void onProcessAiuiResultInput(AiuiResultEntity aiuiResultEntity) {
        AiuiResultUtil.onProcessAiuiResult(stateMachine, aiuiResultEntity);
    }

    protected void onProcessAiuiResultInput(String aiuiResult) {
        Log.e("DefaultState","aiuiResult= "+aiuiResult);
        AiuiResultUtil.onProcessAiuiResult(stateMachine, aiuiResult);
    }

    protected void onProcessSerialInput(int type) {
//        SerialPortReceiver.processSerialPort(type);
    }

    protected void onProcessDanceInput(boolean isStart) {
        if (isStart) {
            stateMachine.navigationNewState(RobotStateMachine.DANCE_STATE);
        }
    }

    protected void onProcessCustomActionInput(boolean isStart) {
        if (isStart) {
            stateMachine.navigationNewState(RobotStateMachine.CUSTOM_ACTION_STATE);
        } else {
            if (RobotStatus.getInstance().isCharging()) {
                stateMachine.navigationNewState(RobotStateMachine.CHARGE_STATE);
            } else {
                stateMachine.navigationNewState(RobotStateMachine.STAND_BY_STATE);
            }
        }
    }
}
