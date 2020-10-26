package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.activity.FlightActivity;
import com.chance.mimorobot.activity.WeatherActivity;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class FlightResultProcess extends BaseReslutProcess {


    @Override
    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine) {
        super.onProcessAiuiResult(aiuiResult, robotStateMachine);
        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
        }.getType();
        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);

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

        String data = "";

        try {
             data = new JSONObject(aiuiResult).getJSONObject("data").getJSONArray("result").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Output.navigatorActivity(FlightActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), data));
    }

    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
        if (aiuiResultEntity == null || aiuiResultEntity.getData() == null) {
            return;
        }
        if (aiuiResultEntity.getAnswer() != null && aiuiResultEntity.getAnswer().getText() != null)
            Output.speak(aiuiResultEntity.getAnswer().getText());

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

        Output.navigatorActivity(FlightActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), data));

//        for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : semanticBean.get(0).getSlots()) {
//            if (slotsBean.getName().equals("datetime") && !StringUtils.isEmpty(slotsBean.getNormValue())) {
//
//                JsonObject object = new JsonParser().parse(slotsBean.getNormValue()).getAsJsonObject();
//                if (object.has("suggestDatetime")) {
//                    dateTime = object.get("suggestDatetime").toString().substring(1, object.get("suggestDatetime").toString().length() - 1);
//                }
//            }
//            break;
//        }

//        if (!StringUtils.isEmpty(dateTime)) {
//            for (int i = 0; i < dataBean.getResult().size(); i++) {
//                AiuiResultEntity.DataBean.ResultBean resultBean = dataBean.getResult().get(i);
//                if (StringUtils.equals(resultBean.getDate(), dateTime)) {
//                    String data = new Gson().toJson(resultBean);
//                    Log.i("WeatherResultProcess", "data = \n" + data);
//
//                    if (ActivityUtils.getTopActivity() instanceof WeatherActivity) {
//                        ActivityUtils.getTopActivity().finish();
//                    }
//                    Output.navigatorActivity(WeatherActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), data));
//                    break;
//                }
//            }
//        }
    }
}
