package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;

import java.util.Random;

/**
 * Created by Administrator on 2018/5/14.
 */

public class ErrorResultProcess extends CommonResultProcess {

    private final static String[] ERROR_ARRAY = {"这个问题太难了，换个问题吧！",
            "你可以这样问我：今天北京的天气如何",
            "你可以对我说：百科搜索乾隆皇帝",
            "你可以问我：四十五的三次方是多少",
            "你可以对我说：来一首李白的诗",
            "你可以对我说：用鸦雀无声造句"};

    private static int count = 0;

    @Override
    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
        String text = "";
        if (aiuiResultEntity != null) {
            text = aiuiResultEntity.getText();
        }
        if (!StringUtils.isEmpty(text) && text.length() > 5) {
            if (count > 5) {
                count = 0;
                Random random = new Random();
                int index = random.nextInt(ERROR_ARRAY.length);
                speakAnswer(ERROR_ARRAY[index]);
            } else {
                count++;
            }
        }

        Log.i("ErrorResultProcess", "count =" + count + ",text=" + text);
    }
}
