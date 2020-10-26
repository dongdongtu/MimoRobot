package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.annotation.SuppressLint;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.Locale;


/**
 * created by Lynn
 * on 2019/8/14
 */
public class IssueMachineResultProcess extends CommonResultProcess {
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
        String normValue = "";
        if (aiuiResultEntity != null) {
            if (aiuiResultEntity.getSemantic() != null && !aiuiResultEntity.getSemantic().isEmpty()
                    && aiuiResultEntity.getSemantic().get(0).getSlots() != null && !aiuiResultEntity.getSemantic().get(0).getSlots().isEmpty()) {

                for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : aiuiResultEntity.getSemantic().get(0).getSlots()) {
                    if (slotsBean.getName().equals("aisle")) {
                        normValue = slotsBean.getNormValue();
                        break;
                    }
                }

                if (StringUtils.isEmpty(normValue)) {
                    return;
                }

                Log.i("IssueMachineResult", "normValue=" + normValue);

                switch (normValue) {
                    case "aisle_1":
                        requestMachine(1, 0);
                        break;
                    case "aisle_2":
                        requestMachine(2, 0);
                        break;
                    case "aisle_3":
                        requestMachine(3, 0);
                        break;
                    case "aisle_4":
                        requestMachine(4, 0);
                        break;
                    case "aisle_01":
                        requestMachine(0, 1);
                        break;
                    case "aisle_05":
                        requestMachine(0, 5);
                        break;
                    case "aisle_03":
                        requestMachine(0, 3);
                        break;

                }
            } else {
                if (aiuiResultEntity.getAnswer() != null) {
                    Output.speak(aiuiResultEntity.getAnswer().getText());
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private void requestMachine(int aisle, int machine) {
//        String url;
//        if (aisle != 0) {
//            url = String.format(Locale.US, "http://ffj.chuangze.cn/api/ControlMachineByRobot.ashx?sn=12345678&sfhm=371102199403217532&hdno=%d&yjdm=1", aisle);
//        } else {
//            url = String.format(Locale.US, "http://ffj.chuangze.cn/api/ControlMachineByRobot.ashx?sn=12345678&sfhm=371102199403217532&hdno=1&yjdm=%d", machine);
//        }
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//
//                String response = MyApiConnection.createGET(url).requestGetCall();
//                if (response != null) {
//                    e.onNext(response);
//                } else {
//                    e.onNext("");
//                }
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                JSONTokener jsonParser = new JSONTokener(s);
//                JSONObject object = (JSONObject) jsonParser.nextValue();
//                String answer = object.getString("msg");
//                Output.speak(answer);
//            }
//        });
    }
}
