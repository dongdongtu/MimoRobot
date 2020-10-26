package com.chance.mimorobot.statemachine.robot.input;

import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.InputContent;

/**
 * Created by Administrator on 2018/5/12.
 */

public class AiuiResultInput extends InputContent {
    private String aiuiResult;
    private AiuiResultEntity aiuiResultEntity;

    public AiuiResultInput(AiuiResultEntity aiuiResultEntity) {
        this.aiuiResultEntity = aiuiResultEntity;
    }

    public AiuiResultInput(String aiuiResult) {
        this.aiuiResult = aiuiResult;
    }

    public String getAiuiResult(){
        return aiuiResult;
    }

    public AiuiResultEntity getAiuiResultEntity() {
        return aiuiResultEntity;
    }
}
