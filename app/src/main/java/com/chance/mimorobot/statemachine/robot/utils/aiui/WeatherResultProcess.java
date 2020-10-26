package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.activity.WeatherActivity;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * created by Lynn
 * on 2019/9/17
 */
public class WeatherResultProcess extends BaseReslutProcess {

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
        for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : semanticBean.get(0).getSlots()) {
            if (slotsBean.getName().equals("datetime") && !StringUtils.isEmpty(slotsBean.getNormValue())) {
                JsonObject object = new JsonParser().parse(slotsBean.getNormValue()).getAsJsonObject();
                if (object.has("suggestDatetime")) {
                    int a=object.get("suggestDatetime").toString().length();
                    Log.e("aaaa",object.get("suggestDatetime").toString());
                    dateTime = object.get("suggestDatetime").toString().replace("\"", "");
                }
            }
            break;
        }
        Log.e("aaaa",dateTime);
        if (!StringUtils.isEmpty(dateTime)) {
            for (int i = 0; i < dataBean.getResult().size(); i++) {
                AiuiResultEntity.DataBean.ResultBean resultBean = dataBean.getResult().get(i);
                if (StringUtils.equals(resultBean.getDate(), dateTime)) {
                    String data = new Gson().toJson(resultBean);
                    Log.i("WeatherResultProcess", "data = \n" + data);

                    if (ActivityUtils.getTopActivity() instanceof WeatherActivity) {
                        ActivityUtils.getTopActivity().finish();
                    }

                    Output.navigatorActivity(WeatherActivity.getIntent(ActivityUtils.getTopActivity().getApplicationContext(), data));
                    break;
                }
            }
        }
    }
}
