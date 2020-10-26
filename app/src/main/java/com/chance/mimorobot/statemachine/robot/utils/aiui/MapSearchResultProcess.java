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
 * created by Lynn
 * on 2019/7/25
 */
public class MapSearchResultProcess extends CommonResultProcess {
    @Override
    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine) {
//        AiuiResultEntity aiuiResultEntity = new AiuiResultEntityJsonMapper().transformAiuiResultEntity(aiuiResult);
        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
        }.getType();
        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);
        onProcessAiuiResult(aiuiResultEntity, robotStateMachine);
    }

    private String searchContent;

    @Override
    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
        if (aiuiResultEntity != null && aiuiResultEntity.getSemantic() != null && !aiuiResultEntity.getSemantic().isEmpty()) {
            AiuiResultEntity.SemanticBean semanticBean = aiuiResultEntity.getSemantic().get(0);
            if (semanticBean != null && semanticBean.getSlots() != null && !semanticBean.getSlots().isEmpty()) {

                String distance = "", unit = "", spot = "";

                for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : semanticBean.getSlots()) {
                    if (slotsBean.getName() != null) {
                        switch (slotsBean.getName()) {
                            case "distance":
                                distance = slotsBean.getValue();
                                break;
                            case "unit":
                                unit = slotsBean.getValue();
                                break;
                            case "spot":
                                spot = slotsBean.getValue();
                                break;
                        }
                    }
                }
                searchContent = "";
                if (StringUtils.isEmpty(distance) || StringUtils.isEmpty(unit)) {
                    if (aiuiResultEntity.getText() != null &&
                            (aiuiResultEntity.getText().contains("附近的") || aiuiResultEntity.getText().contains("周围的"))) {
                        searchContent = "附近的" + spot;
                    } else {
                        searchContent = spot;
                    }
                } else {
                    searchContent = distance + unit + spot;
                }
                Log.i("MapSearchResultProcess", "searchContent=" + searchContent);

//                Output.playAfterSpeak("好的，这就为您查找", new VocalSpeakManager.OnSpeakListener() {
//                    @Override
//                    public void onStart() {
//
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        Output.navigatorToBaiduMap(searchContent);
//                        Output.clearSpeakListener();
//                    }
//                });
            }
        }
    }
}
