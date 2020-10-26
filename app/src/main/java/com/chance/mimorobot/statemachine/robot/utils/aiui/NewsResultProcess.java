package com.chance.mimorobot.statemachine.robot.utils.aiui;



import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/5/14.
 */

public class NewsResultProcess extends CommonResultProcess {
    public static final String preStr = "为您找到：";

    @Override
    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine) {
//        AiuiResultEntity aiuiResultEntity = new AiuiResultEntityJsonMapper().transformAiuiResultEntity(aiuiResult);
        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
        }.getType();
        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);
        onProcessAiuiResult(aiuiResultEntity, robotStateMachine);
    }

    @Override
    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
        List<AiuiResultEntity.DataBean.ResultBean> results = aiuiResultEntity.getData().getResult();
        Random random = new Random();
        int index = random.nextInt(results.size());
        playUrlAfterSpeak(preStr + results.get(index).getTitle(), results.get(index).getUrl());
//        playSoundUrl(results.get(index).getUrl());
    }

}
