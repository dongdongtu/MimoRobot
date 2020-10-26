package com.chance.mimorobot.statemachine.robot.utils.aiui;

import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.statemachine.robot.RobotStateMachine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.List;

/**
 * created by Lynn
 * on 2019/7/29
 */
public class TelephoneResultProcess extends CommonResultProcess {

    private final String TAG = "TelephoneResultProcess";
    private String name;

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
        if (aiuiResultEntity.getSemantic() != null && !aiuiResultEntity.getSemantic().isEmpty()) {
//            if (StringUtils.equals(aiuiResultEntity.getSemantic().get(0).getIntent(), "DIAL")) {
//                //打电话服务
//                //获取name 有两种结构
//                if (aiuiResultEntity.getData() != null && aiuiResultEntity.getData().getResult() != null && !aiuiResultEntity.getData().getResult().isEmpty()) {
//                    name = aiuiResultEntity.getData().getResult().get(0).getName();
//
//                }
//                if (StringUtils.isEmpty(name)) {
//                    if (aiuiResultEntity.getSemantic().get(0).getSlots() != null && !aiuiResultEntity.getSemantic().get(0).getSlots().isEmpty() && aiuiResultEntity.getSemantic().get(0).getSlots().get(0) != null) {
//                        name = aiuiResultEntity.getSemantic().get(0).getSlots().get(0).getValue();
//                    }
//                }
//                Log.i(TAG, "telephoneName = " + name);
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            String response = getContactListFromApi();
//                            Log.i(TAG, "contactList = " + response);
//                            if (response != null) {
//                                ResponseArray responseArray = new ResponseArrayJsonMapper().transformResponseArray(response);
//                                if (responseArray.getCode() == Constant.SUCCESS_ERROR_CODE) {
//                                    List<ContactEntity> contactList = new ContactEntityJsonMapper().transformContactEntityCollection(responseArray.getData().toString());
//                                    boolean findUser = false;
//                                    StringBuilder uploadString = new StringBuilder();
//
//                                    //先遍历一遍 将联系人同步给讯飞的电话服务
//                                    for (ContactEntity contactEntity : contactList) {
//                                        String userName = contactEntity.getUsername();
//                                        String userPhone = contactEntity.getPhone();
//                                        Log.i(TAG, "userName = " + userName + ",userPhone = " + userPhone);
//                                        uploadString = uploadString.append(String.format("{\"name\": \"%s\", \"phoneNumber\": \"%s\" }\n", userName, userPhone));
//                                    }
//                                    AiuiManager.getInstance().uploadContactList(uploadString.toString());
//
//                                    //再遍历一遍 查找用户
//                                    for (ContactEntity contactEntity : contactList) {
//                                        String userName = contactEntity.getUsername();
//                                        if (StringUtils.equals(userName, name)) {
//                                            findUser = true;
//                                            String clientID = contactEntity.getClientId();
//                                            int loginState = contactEntity.getLoginState();
//
//                                            if (loginState == 1) {
//                                                new Navigator().navigateToVideoOut(ActivityUtils.getTopActivity().getBaseContext(), true, clientID, contactEntity.getContactId());
//                                            } else {
//                                                Output.speak("该用户不在线");
//                                            }
//                                        }
//                                    }
//
//                                    if (!findUser) {
//                                        Output.speak("没有找到该用户。请确认要拨打的联系人");
//                                    }
//                                } else {
//                                    Output.speak("没有找到该用户。请确认要拨打的联系人");
//                                }
//                            } else {
//                                Output.speak("没有找到该用户。请确认要拨打的联系人");
//                            }
//                        } catch (Exception err) {
//                            Log.i(TAG, "call error:", err);
//                        }
//                    }
//                }).start();
//            } else {
//                Output.speak("好的，你要给谁打电话呢");
//                if (ActivityUtils.getTopActivity() instanceof ContactActivity) {
//                    return;
//                }
//                Output.navigatorActivity(ContactActivity.class);
//            }

        }
    }

//    private String getContactListFromApi() throws MalformedURLException, JSONException {
//
//        JSONObject data = new JSONObject();
//        data.put("robot_id", RobotToken.getInstance().getId());
//
//        JSONObject info = new JSONObject();
//        info.put("data", data);
//
//        return MyApiConnection.createPOST(Constant.BASE_URL + "api/indexUser", info.toString()).requestSyncCall();
//    }
}
