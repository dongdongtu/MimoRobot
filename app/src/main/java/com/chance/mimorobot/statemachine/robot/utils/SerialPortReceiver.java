package com.chance.mimorobot.statemachine.robot.utils;

import android.util.Log;

import com.chance.mimorobot.statemachine.robot.Output;

/**
 * Created by lyn on 2018/5/16.
 * 处理串口输入
 */
public class SerialPortReceiver {
    private static final String HEAD_TOUCH_TOP_SPEAK = "摸头长不高哦";
    private static final String HEAD_TOUCH_LEFT_SPEAK = "轻点，头好晕啊";
    private static final String HEAD_TOUCH_RIGHT_SPEAK = "哎呀，晃得我好晕啊";
    private static final String BODY_TOUCH_AHEAD_SPEAK = "好啦好啦 我自己会走的";
    private static final String BODY_TOUCH_LEFT_SPEAK = BODY_TOUCH_AHEAD_SPEAK;
    private static final String BODY_TOUCH_RIGHT_SPEAK = "左手牵右手，可别弄疼我啦";


    /**
     * 执行串口输入
     *
     * @param type {@link com.joyrry.android.czrobot.presentation.helper.SerialParser}
     */
//    public static void processSerialPort(int type) {
//        Log.i("SerialPortReceiver",CustomActionManager.getInstance().isDoingAction()+".");
//        switch (type) {
//            case SerialParser.HEAD_LEFT:
//                if (!CustomActionManager.getInstance().isDoingAction()) {
//                    Output.changeExpression(ExpressionType.annoyed);
//                    Output.forceSpeak(HEAD_TOUCH_LEFT_SPEAK, false);
//                }
//                break;
//            case SerialParser.HEAD_RIGHT:
//                if (!CustomActionManager.getInstance().isDoingAction()) {
//                    Output.changeExpression(ExpressionType.annoyed);
//                    Output.forceSpeak(HEAD_TOUCH_RIGHT_SPEAK, false);
//                }
//                break;
//            case SerialParser.HEAD_MIDDLE:
//                if (!CustomActionManager.getInstance().isDoingAction()) {
//                    Output.changeExpression(ExpressionType.annoyed);
//                    Output.forceSpeak(HEAD_TOUCH_TOP_SPEAK, false);
//                }
//                break;
//            case SerialParser.HEAD_ALL:
//                Output.changeExpression(ExpressionType.annoyed);
////                Output.forceSpeak(HEAD_TOUCH_SPEAK);
//                break;
//            case SerialParser.HEAD_LEFT_MIDDLE:
//                Output.changeExpression(ExpressionType.annoyed);
////                Output.forceSpeak(HEAD_TOUCH_SPEAK);
//                break;
//            case SerialParser.HEAD_LEFT_RIGHT:
//                Output.changeExpression(ExpressionType.annoyed);
////                Output.forceSpeak(HEAD_TOUCH_SPEAK);
//                break;
//            case SerialParser.HEAD_RIGHT_MIDDLE:
//                Output.changeExpression(ExpressionType.annoyed);
////                Output.forceSpeak(HEAD_TOUCH_SPEAK);
//                break;
//
//            case SerialParser.BODY_LEFT:
//                if (!CustomActionManager.getInstance().isDoingAction()) {
//                    Output.changeExpression(ExpressionType.annoyed);
//                    Output.forceSpeak(BODY_TOUCH_LEFT_SPEAK, false);
//                }
//                break;
//            case SerialParser.BODY_RIGHT:
//                if (!CustomActionManager.getInstance().isDoingAction()) {
//                    Output.changeExpression(ExpressionType.annoyed);
//                    Output.forceSpeak(BODY_TOUCH_RIGHT_SPEAK, false);
//                }
//                break;
//            case SerialParser.BODY_MIDDLE:
//                if (!CustomActionManager.getInstance().isDoingAction()) {
//                    Output.changeExpression(ExpressionType.annoyed);
//                    Output.forceSpeak(BODY_TOUCH_AHEAD_SPEAK, false);
//                }
//                break;
//            case SerialParser.BODY_ALL:
//                Output.changeExpression(ExpressionType.annoyed);
////                Output.forceSpeak(BODY_TOUCH_SPEAK);
//                break;
//            case SerialParser.BODY_LEFT_MIDDLE:
//                Output.changeExpression(ExpressionType.annoyed);
////                Output.forceSpeak(BODY_TOUCH_SPEAK);
//                break;
//            case SerialParser.BODY_LEFT_RIGHT:
//                Output.changeExpression(ExpressionType.annoyed);
////                Output.forceSpeak(BODY_TOUCH_SPEAK);
//                break;
//            case SerialParser.BODY_RIGHT_MIDDLE:
//                Output.changeExpression(ExpressionType.annoyed);
////                Output.forceSpeak(BODY_TOUCH_SPEAK);
//                break;
//        }
//    }
}
