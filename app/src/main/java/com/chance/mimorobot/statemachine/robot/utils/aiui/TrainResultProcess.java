package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.chance.mimorobot.activity.FlightActivity;
import com.chance.mimorobot.activity.TrainActivity;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class TrainResultProcess extends BaseReslutProcess {
    @Override
    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
        if (aiuiResultEntity.getAnswer() != null && aiuiResultEntity.getAnswer().getText() != null)
            Output.speak(aiuiResultEntity.getAnswer().getText());

        if (aiuiResultEntity == null || aiuiResultEntity.getData() == null) {
            return;
        }

        AiuiResultEntity.DataBean dataBean = aiuiResultEntity.getData();
        if (dataBean.getResult() == null || dataBean.getResult().size() == 0 || dataBean.getResult().get(0) == null) {
            return;
        }

        List<AiuiResultEntity.SemanticBean> semanticBean = aiuiResultEntity.getSemantic();
        if (semanticBean == null || semanticBean.size() == 0) {
            return;
        }

        if (semanticBean.get(0).getSlots() == null || semanticBean.get(0).getSlots().size() == 0) {
            return;
        }

        String dateTime = "";

        String data = new Gson().toJson(dataBean.getResult());

        Output.navigatorActivity(TrainActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), data));

    }

    @Override
    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine) {
        super.onProcessAiuiResult(aiuiResult, robotStateMachine);
        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
        }.getType();

        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);

        if (aiuiResultEntity.getAnswer() != null && aiuiResultEntity.getAnswer().getText() != null) {
            Output.speak(aiuiResultEntity.getAnswer().getText());
        }
        if (aiuiResultEntity == null || aiuiResultEntity.getData() == null) {
            return;
        }

        AiuiResultEntity.DataBean dataBean = aiuiResultEntity.getData();
        if (dataBean.getResult() == null || dataBean.getResult().size() == 0 || dataBean.getResult().get(0) == null) {
            return;
        }

        List<AiuiResultEntity.SemanticBean> semanticBean = aiuiResultEntity.getSemantic();
        if (semanticBean == null || semanticBean.size() == 0) {
            return;
        }

        if (semanticBean.get(0).getSlots() == null || semanticBean.get(0).getSlots().size() == 0) {
            return;
        }

        String data = "";

        try {
            data = new JSONObject(aiuiResult).getJSONObject("data").getJSONArray("result").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Output.navigatorActivity(TrainActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), data));

    }
}
