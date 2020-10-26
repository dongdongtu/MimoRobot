package com.chance.mimorobot.control;


import android.text.TextUtils;
import android.util.Log;


import com.chance.mimorobot.BaseFragment;


import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.model.GetSemanticModel;
import com.chance.mimorobot.model.SemanticDialog;
import com.chance.mimorobot.model.SemanticModel;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.retrofit.model.AIUIResponse;
import com.chance.mimorobot.statemachine.StateMachineManager;
import com.chance.mimorobot.utils.ObjectUtils;
import com.google.gson.Gson;



import java.util.List;
import java.util.Random;

import androidx.annotation.UiThread;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;





public class SpeechModule {

    private final String TAG=SpeechModule.class.getSimpleName();
    private Gson gson = new Gson();
    // 打招呼之前的称呼
    private String nickName = "";

    private SemanticDialog mSemanticDialog;

    private SpeechControlCallBack mSpeechControlCallBack;

    /**
     * <item>你好啊</item>
     * <item>你好，很高兴见到你</item>
     * <item>你好，见到你很高兴</item>
     * <item>你好，很开心见到你</item>
     * <item>你好，见到你好开心呀</item>
     * <item>你好，见到你真是太高兴了</item>
     * <item>你好，见到你真是太开心了</item>
     * <item>你好</item>
     * <item>你好，有什么需要帮助的吗?</item>
     * <item>你好，有什么可以帮你?</item>
     * <item>您好，很高兴为您服务</item>
     * <item>您好，有什么可以帮您</item>
     * <item>你好，我能为您做点什么?</item>
     * <item>你好呀，我能为您干点什么?</item>
     */
    String[] singleHellos = {"你好啊", "你好，很高兴见到你", "你好，见到你很高兴", "你好，很开心见到你",
            "你好，见到你好开心呀", "你好，见到你真是太高兴了", "你好，见到你真是太开心了",
            "你好", "你好，有什么需要帮助的吗?", "你好，有什么可以帮你?", "您好，很高兴为您服务",
            "您好，有什么可以帮您", "你好，我能为您做点什么?", "你好呀，我能为您干点什么?"};

    /**
     * <item>大家好</item>
     * <item>很高兴见到大家</item>
     * <item>见到大家很高兴</item>
     * <item>见到大家真开心</item>
     * <item>大家好啊</item>
     */
    String[] manyHellos = {"大家好", "很高兴见到大家", "见到大家很高兴", "见到大家真开心", "大家好啊"};

    private static SpeechModule mSpeechModule = null;

    private SpeechModule() {

    }

    public static SpeechModule getInstance() {
        if (mSpeechModule == null) {
            synchronized (SpeechModule.class) {
                if (mSpeechModule == null)
                    mSpeechModule = new SpeechModule();
            }
        }
        return mSpeechModule;
    }


    public void setSpeechControlCallBack(SpeechControlCallBack mSpeechControlCallBack) {
        this.mSpeechControlCallBack = mSpeechControlCallBack;
    }



    /**
     * 请求AIUI服务器进行语义解析
     *
     * @param  aiuiResultEntity
     */
    public void requestAIUIServer(AiuiResultEntity aiuiResultEntity) {
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), text.getBytes());

        if (TextUtils.isEmpty(aiuiResultEntity.getText())){
            return;
        }
        GetSemanticModel getSemanticModel=new GetSemanticModel();
        if (mSemanticDialog!=null){
            getSemanticModel.setDialog(mSemanticDialog);
        }
        getSemanticModel.setQueryText(aiuiResultEntity.getText());
        getSemanticModel.setRobotNo(Globle.robotId);
        ApiManager.getInstance().getRobotServer().getSemantic(getSemanticModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SemanticModel>() {
                    @Override
                    public void accept(SemanticModel semanticModel) {
                        Log.i(TAG,"Api返回数据:" + gson.toJson(semanticModel));

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(TAG,"Api请求异常:" + throwable.toString());
//                        requestPostFromTuling(text);
                    }
                });
    }

    public interface SpeechControlCallBack {


        /**
         * 只有语音
         *
         * @param speech 语音
         */
        @UiThread
        void notifyContent(String speech);

        /**
         * 语音识别结果
         *
         * @param result 语音识别结果
         */
        @UiThread
        void notifyResultContent(String result);

        /**
         * 返回讯飞兜底数据，包含图灵闲聊功能
         *
         * @param result          返回的结果
         * @param service         技能名称
         * @param serviceCategory 技能的类别
         */
        @UiThread
        void notifyChat(String result, String service, String serviceCategory);

        /**
         * 获取地点
         *
         * @return 地点
         */
        String city();

    }

    /**
     * 更新界面
     *
     * @param speech
     */
    public void notifyContent(String speech) {
        if (mSpeechControlCallBack != null) {
            mSpeechControlCallBack.notifyContent(speech);
        }
    }
}
