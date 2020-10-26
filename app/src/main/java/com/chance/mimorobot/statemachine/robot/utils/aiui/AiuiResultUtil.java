package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.util.Log;


import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.chance.mimorobot.statemachine.robot.utils.aiui.constants.AiuiConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2018/5/12.
 */

public class AiuiResultUtil {

    public static void onProcessAiuiResult(RobotStateMachine stateMachine, AiuiResultEntity aiuiResultEntity) {
        BaseReslutProcess reslutProcesser = valueOf(aiuiResultEntity);
        String text = aiuiResultEntity.getText();
        if (text == null) {
            return;
        }

        //对跳舞状态做特殊处理，只接受部分指令（如停止）
        if (stateMachine.getCurrentState() == RobotStateMachine.DANCE_STATE) {
            if (!(reslutProcesser instanceof CustomResultProcess)) {
                return;
            }
        }
        reslutProcesser.onProcessAiuiResult(aiuiResultEntity, stateMachine);
    }

    public static void onProcessAiuiResult(RobotStateMachine stateMachine, String aiuiResult) {
        BaseReslutProcess reslutProcesser = valueOf(aiuiResult);
        try {
            JSONObject jsonObject = new JSONObject(aiuiResult);
            if (jsonObject.getString("text") == null) {
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //对跳舞状态做特殊处理，只接受部分指令（如停止）
        if (stateMachine.getCurrentState() == RobotStateMachine.DANCE_STATE) {
            if (!(reslutProcesser instanceof CustomResultProcess)) {
                return;
            }
        }
        reslutProcesser.onProcessAiuiResult(aiuiResult, stateMachine);
    }

    private static BaseReslutProcess valueOf(AiuiResultEntity aiuiResultEntity) {
        String text = aiuiResultEntity.getText();
//        String textResult = RobotHotSettings.getInstance().getVocalAnswer(text); //获取预设问答的结果
//        if (textResult != null) {
//            return new PreinstalledAnswerProcess();//预设问答
//        }

        String service = aiuiResultEntity.getService();
        String inentType = aiuiResultEntity.getIntentType();
        String category = aiuiResultEntity.getCategory();
        int rc = aiuiResultEntity.getRc();

        if (rc == 4) {
            return new ErrorResultProcess();
        }

        Log.e("AiuiResultUtil", "service = " + service + ",intentType = " + inentType + ",category = " + category);
        //天气服务
        if (StringUtils.equals(AiuiConstants.ServiceConstants.WEATHER_SERVICE, service)) {
            return new WeatherResultProcess();
        }
        if (StringUtils.equals(AiuiConstants.ServiceConstants.FLIGHT, service)) {
            return new FlightResultProcess();
        }
        //音乐服务
        if (StringUtils.equals(AiuiConstants.ServiceConstants.MUSIC_SERVICE, service)) {
            return new MusicResultProcess();
        }
        //火车
        if (StringUtils.equals(AiuiConstants.ServiceConstants.TRAIN, service)) {
            return new TrainResultProcess();
        }
        //新闻服务
        if (StringUtils.equals(AiuiConstants.ServiceConstants.NEWS_SERVICE, service)) {
            return new NewsResultProcess();
        }
        //闹钟服务
        if (StringUtils.equals(AiuiConstants.ServiceConstants.SCHEDULE_SERVICE, service)) {
            return new ScheduleResultProcess();
        }
        //电话服务
        if (StringUtils.equals(AiuiConstants.CategoryConstants.IFLY_TELEPHONE, category)) {
            return new TelephoneResultProcess();
        }
        //自定义技能
        if (StringUtils.equals(AiuiConstants.IntentTypeConstants.CUSTOM_INTENT_TYPE, inentType)) {
            switch (category) {
//                case "JOYRRY.test":
//                    return new IssueMachineResultProcess();
                case AiuiConstants.CategoryConstants.CUSTOM_CATEGORY:
                    return new CustomResultProcess();
                case AiuiConstants.CategoryConstants.CUSTOM_CATEGORYCONTROL:
                    return new CustomResultProcess();
                case AiuiConstants.CategoryConstants.CUSTOM_EDU_CATEGORY:
                    //教育资源
                    return new EducationResultProcess();
                case AiuiConstants.CategoryConstants.CUSTOM_MAP_CATEGORY:
                    //地图服务
                    return new MapSearchResultProcess();
            }
        }
        if (StringUtils.equals(AiuiConstants.ServiceConstants.CMD_SERVICE, service)) {
            return new CmdResultProcess();
        }
        return new CommonResultProcess();
    }

    private static BaseReslutProcess valueOf(String aiuiResult) {
        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
        }.getType();
        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);
//        AiuiResultEntity aiuiResultEntity = new AiuiResultEntityJsonMapper().transformAiuiResultEntity(aiuiResult);
        return valueOf(aiuiResultEntity);
    }
}
