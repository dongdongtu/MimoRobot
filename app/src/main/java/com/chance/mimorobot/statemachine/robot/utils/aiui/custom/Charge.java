package com.chance.mimorobot.statemachine.robot.utils.aiui.custom;

import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.utils.aiui.constants.MoveActionConstants;

/**
 * Created by Administrator on 2018/5/25.
 */

public class Charge extends Default {
    @Override
    public void doCommand(String normValue) {
        super.doCommand(normValue);
    }

    @Override
    public void doAction(String normValue) {
        switch (normValue) {
            case MoveActionConstants.dance:
            case MoveActionConstants.turn_around:
            case MoveActionConstants.forward:
            case MoveActionConstants.back:
            case MoveActionConstants.turn_left:
            case MoveActionConstants.turn_right:
//                Output.changeExpression(ExpressionType.sleep);
                Output.speak("我有点累，让我休息一下");
                break;
            case MoveActionConstants.eat:
                Output.speak("我正吃着呢，不过我么你口味不同");
                break;
            case MoveActionConstants.goodbye:
            case MoveActionConstants.come_here:
//                Output.changeExpression(ExpressionType.sleep);
                Output.speak("我有点累，让我休息一下");
                break;
            case MoveActionConstants.sick:
//                Output.changeExpression(ExpressionType.sleep);
                Output.speak("哎呀，我这里有药/快去医院吧，生病了我会心疼的");
//                hardwareInterface.doAction(ActionType.hug);
                break;
            default:
//                super.doAction(normValue);
                break;
        }
    }
}
