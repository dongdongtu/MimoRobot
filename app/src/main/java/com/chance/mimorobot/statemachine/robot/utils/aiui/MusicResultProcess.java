package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.RobotStatus;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * created by Lynn
 * on 2019/7/8
 */
public class MusicResultProcess extends BaseReslutProcess {

    private int count = 0;

    @Override
    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
    }

//    @Override
//    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine) {
//        if (StringUtils.isEmpty(aiuiResult)) {
//            return;
//        }
//        Log.i("MusicResultProcess", "status = " + RobotStatus.getInstance().isMusicActivated());
//
//        if (!RobotStatus.getInstance().isMusicActivated()) {
//            Output.speak("登录后就可以尽情听歌啦");
//            ActivityUtils.getTopActivity().startActivityForResult(new Intent(ActivityUtils.getTopActivity().getApplicationContext(), KugouLoginActivity.class), Constant.REQUEST_CODE_KUGO_LOGIN);
//            return;
//        }
//
////        AiuiResultEntity aiuiResultEntity = new AiuiResultEntityJsonMapper().transformAiuiResultEntity(aiuiResult);
//        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
//        }.getType();
//        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);
//        String answer = StringUtils.isEmpty(aiuiResultEntity.getText()) ? "" : aiuiResultEntity.getText();
//        if (answer.contains("继续播放")) {
//            AIUIPlayerHelper.getInstance().resume();
//        } else if (answer.contains("下一首")) {
//            AIUIPlayerHelper.getInstance().playNext();
//        } else if (answer.contains("暂停播放")) {
//            AIUIPlayerHelper.getInstance().pause();
//        } else if (answer.contains("上一首")) {
//            AIUIPlayerHelper.getInstance().playPrevious();
//        } else if (answer.contains("停止播放")) {
//            AIUIPlayerHelper.getInstance().reset();
//        } else if (aiuiResultEntity.getRc() == 0 && aiuiResultEntity.getData() != null && aiuiResultEntity.getData().getResult() != null) {
//            count = 0;
//            String textAnswer = "好的";
//            if (aiuiResultEntity.getAnswer() != null) {
//                textAnswer = aiuiResultEntity.getAnswer().getText();
//            }
//            Output.playAfterSpeak(textAnswer, new VocalSpeakManager.OnSpeakListener() {
//                @Override
//                public void onStart() {
//
//                }
//
//                @Override
//                public void onCompleted() {
//                    if (count != 0) {
//                        return;
//                    }
//                    count++;
//                    Log.i("aiuiPlayer", "speak finish,start player");
//                    JSONObject jsonObject;
//                    try {
//                        jsonObject = new JSONObject(aiuiResult);
//                        AIUIPlayerHelper.getInstance().play(jsonObject.getJSONObject("data").getJSONArray("result"), aiuiResultEntity.getService(), aiuiResultEntity.getSid());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    Output.clearSpeakListener();
//                }
//            });
//
//        } else if (aiuiResultEntity.getRc() == 3) {
//            //未找到资源或者信源有问题
//            SharedPreferences sharedPreferences = ActivityUtils.getTopActivity().getApplicationContext().getSharedPreferences(Constant.KUGO_CONSTANTS_FILE_NAME, Context.MODE_PRIVATE);
//            String uid = sharedPreferences.getString(Constant.KEY_KUGO_LOGIN_UID, "");
//            String token = sharedPreferences.getString(Constant.KEY_KUGO_LOGIN_TOKEN, "");
//
//            //fix 听到类似休息的指令调用music服务的sleep 当回调是sleep时不做操作
//            if (aiuiResultEntity.getSemantic() != null && aiuiResultEntity.getSemantic().size() != 0) {
//                AiuiResultEntity.SemanticBean semanticBean = aiuiResultEntity.getSemantic().get(0);
//                if (semanticBean.getSlots() != null && semanticBean.getSlots().size() != 0) {
//                    AiuiResultEntity.SemanticBean.SlotsBean slotsBean = semanticBean.getSlots().get(0);
//                    if (slotsBean != null) {
//                        if (StringUtils.equals(slotsBean.getValue(), "sleep")) {
//                            AIUIPlayerHelper.getInstance().pause();
//                            return;
//                        }
//                    }
//                }
//            }
//
//            if (!aiuiResultEntity.getText().contains("关机") || !aiuiResultEntity.getText().contains("休息")) {
//                if (!RobotStatus.getInstance().isMusicActivated() && (StringUtils.isEmpty(uid) || StringUtils.isEmpty(token))) {
//                    Output.speak("登录后就可以尽情听歌啦");
//                    if (!(ActivityUtils.getTopActivity() instanceof KugouLoginActivity)) {
//                        ActivityUtils.getTopActivity().startActivityForResult(new Intent(ActivityUtils.getTopActivity().getApplicationContext(), KugouLoginActivity.class), Constant.REQUEST_CODE_KUGO_LOGIN);
//                    }
//                } else {
//                    if (aiuiResultEntity.getAnswer() != null) {
//                        Output.speak(aiuiResultEntity.getAnswer().getText());
//                    }
//                }
//            }
//        }
//    }
}
