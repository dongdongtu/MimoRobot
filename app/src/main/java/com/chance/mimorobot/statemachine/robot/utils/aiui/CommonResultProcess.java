package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2018/5/14.
 */

public class CommonResultProcess extends BaseReslutProcess {
    @Override
    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine) {
        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
        }.getType();
        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);
//        AiuiResultEntity aiuiResultEntity = new AiuiResultEntityJsonMapper().transformAiuiResultEntity(aiuiResult);
        onProcessAiuiResult(aiuiResultEntity, robotStateMachine);
    }

    @Override
    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
        String textAnswer = "";
        String url = "";
        try {
            textAnswer = aiuiResultEntity.getAnswer().getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        textAnswer = textAnswer == null ? "" : textAnswer;

        Log.i("CommonResultProcess:", textAnswer);

        if (aiuiResultEntity.getData() != null && aiuiResultEntity.getData().getResult() != null && !aiuiResultEntity.getData().getResult().isEmpty()) {
            try {
                url = aiuiResultEntity.getData().getResult().get(0).getUrl();
                if (StringUtils.isEmpty(url)) {
                    url = aiuiResultEntity.getData().getResult().get(0).getPlayUrl();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            url = url == null ? "" : url;
        }

        boolean b = playSoundUrl(url);
        if (!b) {
            speakAnswer(textAnswer);
        }
    }

    /**
     * 把回答用语音合成出来
     *
     * @param answer
     */
    protected boolean speakAnswer(String answer) {
        if (!answer.isEmpty()) {
            Output.speak(answer);
            return true;
        }
        return false;
    }

    protected boolean playSoundUrl(String url) {
        if (urlCanPlay(url)) {
            Output.playUrl(url);
            return true;
        }
        return false;
    }

    protected void playUrlAfterSpeak(String answer, String url) {
        if (urlCanPlay(url) && !answer.isEmpty()) {
            Output.playUrlAfterSpeak(answer, url);
        } else {
            speakAnswer(answer);
            playSoundUrl(url);
        }
    }

    private boolean urlCanPlay(String url) {
        return url.contains("mp3") || url.contains("m4a");
    }

}
