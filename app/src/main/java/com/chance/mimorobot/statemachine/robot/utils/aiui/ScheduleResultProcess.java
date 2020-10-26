package com.chance.mimorobot.statemachine.robot.utils.aiui;


import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * created by Lynn
 * on 2019/6/14
 */
public class ScheduleResultProcess extends CommonResultProcess {
//
//    private Disposable rxAlarmDisposable, rxGetAlarmDisposable, rxDeleteAlarmDisposable;
//
//    @Override
//    public void onProcessAiuiResult(String aiuiResult, RobotStateMachine robotStateMachine) {
////        AiuiResultEntity aiuiResultEntity = new AiuiResultEntityJsonMapper().transformAiuiResultEntity(aiuiResult);
//        Type aiuiResultEntityType = new TypeToken<AiuiResultEntity>() {
//        }.getType();
//        AiuiResultEntity aiuiResultEntity= new Gson().fromJson(aiuiResult, aiuiResultEntityType);
//        onProcessAiuiResult(aiuiResultEntity, robotStateMachine);
//    }
//
//    @Override
//    public void onProcessAiuiResult(AiuiResultEntity aiuiResultEntity, RobotStateMachine robotStateMachine) {
//        List<AiuiResultEntity.SemanticBean> semanticList = aiuiResultEntity.getSemantic();
//        if (semanticList == null || semanticList.size() == 0) return;
//
//        AiuiResultEntity.SemanticBean semanticBean = semanticList.get(0);
//        if (semanticBean == null) return;
//
//        if ("CREATE".equals(semanticBean.getIntent())) {
//            String normValue = "", indication = "";
//            boolean repeat = false;
//            for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : semanticBean.getSlots()) {
//                if ("datetime".equals(slotsBean.getName())) {
//                    normValue = slotsBean.getNormValue();
//                } else if ("content".equals(slotsBean.getName())) {
//                    indication = slotsBean.getValue();
//                } else if ("repeat".equals(slotsBean.getName())) {
//                    repeat = true;
//                }
//            }
//            String textAnswer = "";
//            if (aiuiResultEntity.getAnswer() != null && aiuiResultEntity.getAnswer().getText() != null) {
//                textAnswer = aiuiResultEntity.getAnswer().getText();
//            }
//            if (StringUtils.isEmpty(normValue)) {
//                Output.speak(textAnswer);
//            } else {
//                setAlarm(normValue, indication, textAnswer, repeat);
//            }
//        } else if ("CANCEL".equals(semanticBean.getIntent())) {
//            if (aiuiResultEntity.getText().contains("关闭")) {
//                return;
//            }
//            String normValue = "", value = "", repeat = "";
//            for (AiuiResultEntity.SemanticBean.SlotsBean slotsBean : semanticBean.getSlots()) {
//                if ("repeat".equals(slotsBean.getName())) {
//                    repeat = slotsBean.getValue();
//                }
//                if ("datetime".equals(slotsBean.getName())) {
//                    normValue = slotsBean.getNormValue();
//                    value = slotsBean.getValue();
//                }
//            }
//
//            String week = dealWithWeekNumber(value);
//            if (StringUtils.isEmpty(week)) {
//                week = dealWithRepeatValue(repeat);
//            }
//            if (StringUtils.isEmpty(normValue)) {
//                Output.speak("您要进行什么操作呢？");
//                if (!(ActivityUtils.getTopActivity() instanceof ClockActivity)) {
//                    Output.navigatorActivity(ClockActivity.class);
//                }
//            } else {
//                deleteAlarm(normValue, week);
//            }
//        }
//    }
//
//    private void deleteAlarm(String normValue, String week) {
//        getAlarmList(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                if (s.length() > 0) {
//                    try {
//                        JSONTokener jsonParser = new JSONTokener(s);
//                        JSONObject response = (JSONObject) jsonParser.nextValue();
//                        if (response.getInt("code") == Constant.SUCCESS_ERROR_CODE) {
//                            List<ClockEntity> clockEntities = new ClockEntityJsonMapper().transformClockEntityCollection(response.getJSONArray("data").toString());
//                            List<AlarmBellModel> alarmBellModels = changeToAlarmBellModel(clockEntities);
//                            RobotHotSettings.getInstance().setAlarmBellModelList(alarmBellModels);
//
//                            boolean findFlag = false;
//                            JsonParser parser = new JsonParser();
//                            JsonObject object = parser.parse(normValue).getAsJsonObject();
//
//                            if (!StringUtils.isEmpty(week)) {
//                                //先对比重复日期
//                                for (int i = 0; i < clockEntities.size(); i++) {
//                                    if (StringUtils.equals(week, clockEntities.get(i).getRepeat())) {
//                                        if (object.has("suggestDatetime")) {
//                                            String clockTime = object.get("suggestDatetime").toString();
//                                            int Tindex = clockTime.indexOf("T");
//                                            String time = clockTime.substring(Tindex + 1, clockTime.length() - 1);
//                                            if (StringUtils.equals(clockEntities.get(i).getTime(), time)) {
//                                                //相同闹钟 去删除
//                                                findFlag = true;
//                                                deleteAlarm(clockEntities.get(i).getClockId());
//                                                break;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (!findFlag && object.has("datetime")) {
//                                //重复日期对比失败
//                                String dateTime = object.get("datetime").toString();
//                                int Tindex = dateTime.indexOf("T");
//                                String date = dateTime.substring(1, Tindex);
//                                String time = dateTime.substring(Tindex + 1, dateTime.length() - 1);
//                                if (isBeforeToday(date)) {
//                                    //要删除的是今天之前的闹钟 取dateTime的值做对比
//                                    for (int i = 0; i < clockEntities.size(); i++) {
//                                        String alarmDate = clockEntities.get(i).getDate();
//                                        String alarmTime = clockEntities.get(i).getTime();
//
//                                        if (StringUtils.equals(alarmDate, date) && StringUtils.equals(alarmTime, time)) {
//                                            //相同闹钟 删除
//                                            findFlag = true;
//                                            deleteAlarm(clockEntities.get(i).getClockId());
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//
//                            if (!findFlag && object.has("suggestDatetime")) {
//                                //重复对比失败且dateTime对比失败 取suggestDatetime的值做对比
//                                String clockTime = object.get("suggestDatetime").toString();
//                                int Tindex = clockTime.indexOf("T");
//                                String date = clockTime.substring(1, Tindex);
//                                String time = clockTime.substring(Tindex + 1, clockTime.length() - 1);
//                                for (int i = 0; i < clockEntities.size(); i++) {
//                                    String alarmDate = clockEntities.get(i).getDate();
//                                    String alarmTime = clockEntities.get(i).getTime();
//                                    if (StringUtils.equals(alarmDate, date) && StringUtils.equals(alarmTime, time)) {
//                                        //相同闹钟 去删除
//                                        findFlag = true;
//                                        deleteAlarm(clockEntities.get(i).getClockId());
//                                        break;
//                                    }
//                                }
//                            }
//
//                            if (!findFlag) {
//                                //都对比失败
//                                Output.speak("请确认您要操作的闹钟准确日期和时间");
//                                if (!(ActivityUtils.getTopActivity() instanceof ClockActivity)) {
//                                    Output.navigatorActivity(ClockActivity.class);
//                                }
//                            }
//                        }
//                    } catch (Exception error) {
//                        Log.i("ScheduleResultProcess", "try catch=" + error.toString());
//                        Output.speak("操作失败，请稍后重试");
//                        error.printStackTrace();
//                    }
//                } else {
//                    Log.i("ScheduleResultProcess", "get alarm failed,s.length == 0");
//                    Output.speak("操作失败，请稍后重试");
//                }
//            }
//        });
//    }
//
//    private void deleteAlarm(int clockId) {
//        rxDeleteAlarmDisposable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                String response = deleteClockFromApi(clockId);
//                if (response != null) {
//                    e.onNext(response);
//                } else {
//                    e.onNext("");
//                }
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                if (s.length() > 0) {
//                    JSONTokener jsonParser = new JSONTokener(s);
//                    JSONObject response = (JSONObject) jsonParser.nextValue();
//                    if (response.getInt("code") == Constant.SUCCESS_ERROR_CODE) {
//                        //delete success
//                        Output.speak("操作成功");
//                        refreshAlarmClock();
//                    } else {
//                        Log.i("ScheduleResultProcess", "delete alarm failed,code = " + response.getInt("code"));
//                        Output.speak("操作失败，请稍后重试");
//                    }
//                } else {
//                    Log.i("ScheduleResultProcess", "delete alarm failed,s.length == 0");
//                    Output.speak("操作失败，请稍后重试");
//                }
//            }
//        });
//    }
//
//    private boolean isBeforeToday(String date) {
//        if (StringUtils.isEmpty(date)) {
//            return false;
//        }
//        try {
//            String format = "yyyy-MM-dd";
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
//            Date checkDate = simpleDateFormat.parse(date);
//            Date today = new Date();
//            return checkDate.before(today);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    private void setAlarm(String normValue, final String indication, final String textAnswer, boolean repeat) {
//        JsonParser parser = new JsonParser();
//        try {
//            JsonObject object = parser.parse(normValue).getAsJsonObject();
//            if (object.has("suggestDatetime")) {
//                final String clockTime = object.get("suggestDatetime").toString();
//
//                int Tindex = clockTime.indexOf("T");
//                final String date = clockTime.substring(1, Tindex);
//                final String time = clockTime.substring(Tindex + 1, clockTime.length() - 1);
//
//                final String timeNew = time.substring(0, time.length() - 2) + "00";
//
//                rxAlarmDisposable = Observable.create(new ObservableOnSubscribe<String>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<String> e) throws Exception {
//                        int drawerStatus = 0;
//                        if (indication.contains("打开") && (indication.contains("抽屉") || indication.contains("药箱") || indication.contains("药盒"))) {
//                            drawerStatus = 1;
//                        }
//                        String response = addClockFromApi(timeNew, repeat ? "1111111" : "0000000", "起床", "响铃", drawerStatus, indication, repeat ? "" : date);
//                        if (response != null) {
//                            e.onNext(response);
//                        } else {
//                            e.onNext("");
//                        }
//                    }
//                })
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String s) throws Exception {
//                                if (s.length() == 0) {
//                                    Output.speak("设置失败，请稍后重试");
//                                } else {
//                                    ResponseObject responseObject = new ResponseObjectJsonMapper().transformResponseObject(s);
//                                    if (responseObject.getCode() == Constant.SUCCESS_ERROR_CODE) {
//                                        Output.speak(textAnswer == null ? "好的,我会提醒您" : textAnswer);
//                                        refreshAlarmClock();
//                                    } else if (responseObject.getCode() == Constant.FAIL_ALARM_EXISTS) {
//                                        Output.speak("您已经设置过这个闹钟了");
//                                    } else {
//                                        Output.speak("设置失败，请稍后重试");
//                                    }
//                                }
//                            }
//                        });
//            }
//        } catch (Exception error) {
//            error.printStackTrace();
//        }
//    }
//
//    private void refreshAlarmClock() {
//        getAlarmList(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                if (s.length() > 0) {
//                    JSONTokener jsonParser = new JSONTokener(s);
//                    JSONObject response = (JSONObject) jsonParser.nextValue();
//                    if (response.getInt("code") == Constant.SUCCESS_ERROR_CODE) {
//                        List<ClockEntity> clockEntities = new ClockEntityJsonMapper().transformClockEntityCollection(response.getJSONArray("data").toString());
//                        List<AlarmBellModel> alarmBellModels = changeToAlarmBellModel(clockEntities);
//                        RobotHotSettings.getInstance().setAlarmBellModelList(alarmBellModels);
//                        ActivityUtils.getTopActivity().sendBroadcast(new Intent("refresh_clock"));
//                    }
//                }
//            }
//        });
//    }
//
//    private void getAlarmList(Consumer<String> consumer) {
//        rxGetAlarmDisposable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                String response = getClockListFromApi();
//                if (response != null) {
//                    e.onNext(response);
//                } else {
//                    e.onNext("");
//                }
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(consumer);
//    }
//
//    private String addClockFromApi(String time, String repeat, String type, String voice, int drawStatus, String indication, String date) throws MalformedURLException, JSONException {
//        JSONObject data = new JSONObject();
//        data.put("robot_id", RobotToken.getInstance().getId());
//        data.put("alarm_time", time);
//        data.put("alarm_repeat", repeat);
//        data.put("alarm_type", type);
//        data.put("alarm_voice", voice);
//        data.put("draw_status", drawStatus);
//        data.put("alarm_indication", indication);
//        data.put("alarm_date", date);
//
//        JSONObject info = new JSONObject();
//        info.put("data", data);
//
//        return MyApiConnection.createPOST(Constant.BASE_URL + "api/saveAlarm", info.toString()).requestSyncCall();
//    }
//
//    private String getClockListFromApi() throws JSONException, MalformedURLException {
//        JSONObject data = new JSONObject();
//        data.put("robot_id", RobotToken.getInstance().getId());
//
//        JSONObject info = new JSONObject();
//        info.put("data", data);
//
//        return MyApiConnection.createPOST(Constant.BASE_URL + "api/indexAlarm", info.toString()).requestSyncCall();
//    }
//
//    private List<AlarmBellModel> changeToAlarmBellModel(List<ClockEntity> clockEntities) {
//        List<AlarmBellModel> alarmBellModels = new ArrayList<>();
//        for (ClockEntity clock : clockEntities) {
//            AlarmBellModel alarmBellModel = new AlarmBellModel(clock.getClockId());
//            alarmBellModel.setRepeat(clock.getRepeat());
//            alarmBellModel.setType(clock.getType());
//            alarmBellModel.setVoice(clock.getVoice());
//            String time = clock.getTime();
//            alarmBellModel.setHour(Integer.parseInt(time.substring(0, 2)));
//            alarmBellModel.setMinute(Integer.parseInt(time.substring(3, 5)));
//            alarmBellModel.setDrawStatus(clock.getDrawStatus());
//            alarmBellModel.setIndication(clock.getIndication());
//
//            if (!StringUtils.isEmpty(clock.getDate())) {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = null;
//                try {
//                    date = simpleDateFormat.parse(clock.getDate());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                alarmBellModel.setYear(calendar.get(Calendar.YEAR));
//                alarmBellModel.setMonth(calendar.get(Calendar.MONTH) + 1);
//                alarmBellModel.setDay(calendar.get(Calendar.DAY_OF_MONTH));
//            } else {
//                alarmBellModel.setYear(-1);
//                alarmBellModel.setMonth(-1);
//                alarmBellModel.setDay(-1);
//            }
//            alarmBellModels.add(alarmBellModel);
//        }
//        return alarmBellModels;
//    }
//
//    private String deleteClockFromApi(int id) throws MalformedURLException, JSONException {
//        JSONObject data = new JSONObject();
//        data.put("alarm_id", id);
//
//        JSONObject info = new JSONObject();
//        info.put("data", data);
//
//        return MyApiConnection.createPOST(Constant.BASE_URL + "api/deleteAlarm", info.toString()).requestSyncCall();
//    }
//
//    private String dealWithRepeatValue(String repeat) {
//        switch (repeat) {
//            case "EVERYDAY":
//                return "1111111";
//            case "W1":
//                return "1000000";
//            case "W2":
//                return "0100000";
//            case "W3":
//                return "0010000";
//            case "W4":
//                return "0001000";
//            case "W5":
//                return "0000100";
//            case "W6":
//                return "0000010";
//            case "W7":
//                return "0000001";
//            default:
//                return "";
//        }
//    }
//
//    private String dealWithWeekNumber(String value) {
//        String key = "";
//        if (value.contains("星期")) {
//            key = "星期";
//        } else if (value.contains("周")) {
//            key = "周";
//        }
//        if (StringUtils.isEmpty(key)) {
//            return "";
//        } else {
//            int i = 0;
//            String weekNumber = "0000000";
//            while (value.indexOf(key, i) != -1) {
//                int weekIndex = value.indexOf(key, i) + key.length();
//                char week = value.charAt(weekIndex);
//                int index = -1;
//                switch (week) {
//                    case '一':
//                        index = 0;
//                        break;
//                    case '二':
//                        index = 1;
//                        break;
//                    case '三':
//                        index = 2;
//                        break;
//                    case '四':
//                        index = 3;
//                        break;
//                    case '五':
//                        index = 4;
//                        break;
//                    case '六':
//                        index = 5;
//                        break;
//                    case '七':
//                    case '天':
//                    case '日':
//                        index = 6;
//                        break;
//                }
//                if (index != -1) {
//                    weekNumber = transformWeek(index, weekNumber);
//                    i = i + key.length() + 1;
//                } else {
//                    i = i + key.length();
//                }
//            }
//            Log.i("asfafsd", weekNumber);
//            return weekNumber;
//        }
//    }
//
//    private String transformWeek(int index, String week) {
//        if (StringUtils.isEmpty(week)) {
//            return null;
//        }
//        String newString = "";
//        for (int i = 0; i < week.length(); i++) {
//            if (i == index) {
//                newString = newString + "1";
//            } else {
//                newString = newString + week.charAt(i);
//            }
//        }
//        return newString;
//    }
//
}
