package cn.chuangze.robot.aiuilibrary;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

import cn.chuangze.robot.aiuilibrary.listener.STTEventCallBack;
import cn.chuangze.robot.aiuilibrary.params.SpeechParams;

public class AIUIWrapper {
    private static String TAG = AIUIWrapper.class.getSimpleName();
    private Context context;

    private STTEventCallBack mSttEventCallBack;

    private static AIUIWrapper mAIUIWrapper = null;

    private AIUIAgent mAIUIAgent = null;
    /**
     * 当前aiui状态
     */
    private int mAIUIState = AIUIConstant.STATE_IDLE;

    //AIUI当前录音状态，避免连续两次startRecordAudio时的录音失败
    private boolean mAudioRecording = false;

    //TTS结束回调
    private Runnable mTTSCallback;
    /**
     * 是不是在听写
     */
    private boolean isPause = false;

    /**
     * 是不是再合成语音
     */
    private boolean isTTS = false;

    /**
     * 语音合成参数
     */
    private SpeechParams speechParams;

    /********************   以下参数，在使用msc sdk时 有效*******************************/

    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    // 默认发音人
    private String voicer = "xiaoyan";

    // 默认本地发音人
    public static String voicerLocal = "xiaofeng";


    private int currentSpeechSpeed = 80;

    private AIUIWrapper(Context context) {
        this.context = context.getApplicationContext();

        if (null == mAIUIAgent) {
            Log.e(TAG, "create aiui agent");
            mAIUIAgent = AIUIAgent.createAgent(context, getAIUIParams(context), mAIUIListener);
        }
        // 初始化合成对象
//        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);
        if (null == mAIUIAgent) {
            Log.e(TAG, "创建AIUIAgent失败");
        } else {
            Log.e(TAG, "创建AIUIAgent成功");
        }
        speechParams = new SpeechParams();
    }

    public static AIUIWrapper getInstance(Context context) {
        if (mAIUIWrapper == null) {
            synchronized (AIUIWrapper.class) {
                if (mAIUIWrapper == null)
                    mAIUIWrapper = new AIUIWrapper(context);
            }
        }
        return mAIUIWrapper;
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {

            } else {

            }
        }
    };

    private AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            switch (event.eventType) {
                case AIUIConstant.EVENT_CONNECTED_TO_SERVER:
                    String uid = event.data.getString("uid");
                    if (mSttEventCallBack != null) {
                        mSttEventCallBack.onConnectServer(uid);
                    }
                    break;

                case AIUIConstant.EVENT_SERVER_DISCONNECTED:
                    Log.e(TAG, "EVENT_SERVER_DISCONNECTED");
                    if (mSttEventCallBack != null) {
                        mSttEventCallBack.onDisconnectServer();
                    }
                    break;

                case AIUIConstant.EVENT_TTS:
                    //tts  事件
                    switch (event.arg1) {
                        case AIUIConstant.TTS_SPEAK_COMPLETED:
                            Log.e(TAG, "TTS_SPEAK_COMPLETED");
                            isPause = false;
                            isTTS = false;
                            if (mTTSCallback != null) mTTSCallback.run();
                            break;
                        case AIUIConstant.TTS_SPEAK_BEGIN:
                            Log.e(TAG, "TTS_SPEAK_BEGIN");
                            isPause = false;
                            isTTS = true;
                            break;
                    }
                    break;

                case AIUIConstant.EVENT_WAKEUP:
                    Log.e(TAG, "EVENT_WAKEUP");

//                    try {
                    Log.e(TAG,"mAudioRecording = "+mAudioRecording);
                    startRecordAudio();
//                    Log.e(TAG, "processResult:" + event.info);
//
//                        JSONObject wakeInfo = new JSONObject(event.info);
//
//                        int wakeAngle = wakeInfo.getInt("angle");
//                        int number = wakeInfo.getInt("beam");
//                        Log.i(TAG,"语音唤醒 角度：" + wakeAngle + "  波束号:" + number);
                        if (mSttEventCallBack!=null){
                            mSttEventCallBack.onWakeUp(0);
                        }
//                    }catch (Exception e){
//
//                    }
                    break;
                case AIUIConstant.EVENT_SLEEP:
                    Log.e(TAG, "EVENT_SLEEP");

                    stopRecordAudio();

                    if (mSttEventCallBack != null) {
                        mSttEventCallBack.onSleep(event.arg1);
                    }
                    break;

                case AIUIConstant.EVENT_RESULT:
                    Log.e(TAG,"EVENT_RESULT");
                    processResult(event);
                    break;

                case AIUIConstant.EVENT_ERROR:
                    Log.e(TAG,"EVENT_RESULT");
                    if (mSttEventCallBack != null)
                        mSttEventCallBack.onError(event.arg1, event.info);

                    break;
                case AIUIConstant.EVENT_STATE: {
                    mAIUIState = event.arg1;
                    Log.e(TAG, "EVENT_STATE:" + mAIUIState);
                    if (mSttEventCallBack != null)
                        mSttEventCallBack.onState(mAIUIState);
                }
                break;
                case AIUIConstant.EVENT_VAD:
                    /**
                     * 获取mic声音大小
                     */
                    if (event.eventType == AIUIConstant.EVENT_VAD) {
                        if (event.arg1 == AIUIConstant.VAD_VOL) {
                            if (mSttEventCallBack != null)
                                mSttEventCallBack.onVolume(5000 + 8000 * event.arg2 / 100);
                        }
                    }
                    break;
                case AIUIConstant.EVENT_CMD_RETURN:
                    //  某条CMD命令对应的返回事件
                    if (mSttEventCallBack != null)
                        mSttEventCallBack.onCmdReturn(event.arg1, event.arg2, event.info);
                    break;
                case AIUIConstant.EVENT_PRE_SLEEP:
                    //准备休眠事件
                    if (mSttEventCallBack != null)
                        mSttEventCallBack.onPreSleep();
                    break;
                default:
                    Log.e(TAG, "EVENT_TYPE:" + event.eventType);
                    break;
            }
        }
    };

    /**
     * 开始录音
     */
    public void startRecordAudio() {
        if (!mAudioRecording) {
            // 打开AIUI内部录音机，开始录音。若要使用上传的个性化资源增强识别效果，则在参数中添加pers_param设置
            // 个性化资源使用方法可参见http://doc.xfyun.cn/aiui_mobile/的用户个性化章节
            // 在输入参数中设置tag，则对应结果中也将携带该tag，可用于关联输入输出

            String params = "sample_rate=16000,data_type=audio,pers_param={\"uid\":\"\"},tag=audio-tag";
            //流式识别
//            params += ",dwa=wpgs";
            sendMessage(new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, params, null));
            mAudioRecording = true;
        }
    }

    /**
     * 停止录音
     */
    public void stopRecordAudio() {
        if (mAudioRecording) {
            sendMessage(new AIUIMessage(AIUIConstant.CMD_STOP_RECORD, 0, 0, "data_type=audio,sample_rate=16000", null));
            mAudioRecording = false;
        }
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (mTTSCallback != null) mTTSCallback.run();
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
        }
    };

    /**
     * 开始合成
     *
     * @param text       合成文本
     * @param onComplete 合成完成回调
     */
    public void startTTS(String text, Runnable onComplete) {
        if (TextUtils.isEmpty(text)) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        mTTSCallback = onComplete;

        String params = speechParams.build();

        Log.e(TAG, "startTTS:params" + params);
        AIUIMessage startTts = new AIUIMessage(AIUIConstant.CMD_TTS, AIUIConstant.START, 0, params, text.getBytes());
        sendMessage(startTts);

//        setParam();
//        int code = mTts.startSpeaking(text, mTtsListener);
    }


    public void startTTS1(String text, Runnable onComplete) {
        if (TextUtils.isEmpty(text)) {
            if (onComplete != null) {
                onComplete.run();
            }
            return;
        }

        mTTSCallback = onComplete;

        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
    }

    private void setParam() {
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener);

        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            mTts.setParameter(SpeechConstant.EMOT, "happy");
            if (voicer.equals("mengmeng")) {
                mTts.setParameter(SpeechConstant.EMOT, "happy");
            }
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);
            //设置发音人资源路径
            mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
        }
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "100");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    public  void setSpeed(int speed){
        mTts.setParameter(SpeechConstant.SPEED, String.valueOf(speed));
    }

    //获取发音人资源路径
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/" + voicerLocal + ".jet"));
        return tempBuffer.toString();
    }

    public SpeechParams getSpeechParams() {
        return speechParams;
    }

    public void setSpeechParams(SpeechParams speechParams) {
        this.speechParams = speechParams;
    }

    /**
     * 恢复合成
     */
    public void resumeTTS() {
        if (isPause) {
            isPause = false;
            sendMessage(new AIUIMessage(AIUIConstant.CMD_TTS, AIUIConstant.RESUME, 0, "", null));
        }
    }

    /**
     * 暂停合成
     */
    public void pauseTTS() {
        if (isTTS) {
            isPause = true;
            sendMessage(new AIUIMessage(AIUIConstant.CMD_TTS, AIUIConstant.PAUSE, 0, "", null));
        }
    }

    /**
     * 停止合成播放
     */
    public void stopTTS() {
        if (isTTS) {
            isPause = false;
            isTTS = false;
            sendMessage(new AIUIMessage(AIUIConstant.CMD_TTS, AIUIConstant.CANCEL, 0, "", null));
        }
    }


    /**
     * 用于自定义aiui message
     * 参数参考AIUIMessage
     * https://aiui.xfyun.cn/docs/access_docs
     *
     * @param i  msgType（消息类型）
     * @param i1
     * @param i2
     * @param s
     * @param bt
     */
    public void sendCustomAIUIMsg(int i, int i1, int i2, String s, byte[] bt) {
        sendMessage(new AIUIMessage(i, i1, i2, s, bt));
    }

    /**
     * 动态设置aiui参数
     * String setParams = "{\"global\":{\"scene\":\"main\"}}";
     */
    public void setAIUIParams(String setParams) {
        Log.e(TAG, "setAIUIParams" + setParams);
        AIUIMessage setMsg = new AIUIMessage(AIUIConstant.CMD_SET_PARAMS, 0, 0, setParams, null);
        sendMessage(setMsg);
    }

    /**
     * 交互参数
     *
     * @param interact 结果超时（单位：ms）
     *                 即检测到语音后端点后一段时间内无结果返回则抛出10120错误码。
     *                 默认值：5000。
     * @param result   交互超时(单位：ms)
     *                 即唤醒之后，如果在这段时间内无有效交互则重新进入待唤醒状态，取值：[10000,180000)。
     *                 默认为1min。
     */
    public void setInteract(long interact, long result) {
        interact = interact < 10000 ? 10000 : interact;
        result = result < 1000 ? 1000 : result;

        result = result > 8000 ? 8000 : result;
        String str = "{\"interact\":{\"interact_timeout\":\"" + interact + "\",\"result_timeout\":\"" + result + "\"}}";

        setAIUIParams(str);
    }

    /**
     * 手动唤醒
     */
    public void wakeUp(int arg1) {
        if (null == mAIUIAgent) return;
        Log.e(TAG, "wakeUp  = "+arg1);
        AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, arg1, 0, "", null);
        sendMessage(wakeupMsg);
    }

    /**
     * 休息
     */
    public void sleep() {
        Log.e(TAG, "sleep");
        AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_RESET_WAKEUP, 0, 0, "", null);
        sendMessage(wakeupMsg);
    }

    /**
     * 设置拾音波束号
     */
    public void setBeam(int beam) {
        AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_SET_BEAM, beam, 0, "", null);
        sendMessage(wakeupMsg);
    }

    /**
     * 重置ack
     */
    private void resetACK() {
        AIUIMessage a = new AIUIMessage(AIUIConstant.CMD_RESULT_VALIDATION_ACK, 0, 0, "", null);
        sendMessage(a);
    }

    /**
     * 发送aiui 消息
     *
     * @param message msg
     */
    private void sendMessage(AIUIMessage message) {
        if (null == mAIUIAgent) return;
        mAIUIAgent.sendMessage(message);
    }

    /**
     * 解析结果事件
     *
     * @param event
     */
    private void processResult(AIUIEvent event) {
        try {
            JSONObject bizParamJson = new JSONObject(event.info);
            JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
            JSONObject params = data.getJSONObject("params");
            JSONObject content = data.getJSONArray("content").getJSONObject(0);

            if (content.has("cnt_id")) {
                String cnt_id = content.getString("cnt_id");

                if (!TextUtils.isEmpty(cnt_id) && !TextUtils.isEmpty(new String(event.data.getByteArray(cnt_id), "utf-8"))) {
                    JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cnt_id), "utf-8"));
                    String sub = params.optString("sub");
                    if ("nlp".equals(sub)) {
                        // 解析得到语义结果
                        String resultStr = cntJson.optString("intent");
                        Log.i(TAG, resultStr);
                        if (mSttEventCallBack != null) {
                            mSttEventCallBack.onResultAIUI(resultStr);
                        }
                    }
                    if ("iat".equals(sub)) {
                        JSONObject text = cntJson.getJSONObject("text");
                        JSONArray ws = text.getJSONArray("ws");
                        String iatText = "";
                        for (int i = 0; i < text.getJSONArray("ws").length(); i++) {
                                iatText += ws.getJSONObject(i).getJSONArray("cw").getJSONObject(0).getString("w");

                        }
                        Log.i(TAG, "iatText = "+iatText);
                        if (mSttEventCallBack != null &&!TextUtils.isEmpty(iatText)) {
                            mSttEventCallBack.startCheck("");
                        }
                    }
                }
//                byte temp[] = event.data.getByteArray(cnt_id);
//                if (temp == null || temp.length <= 0) return;
//
//                String cntStr = new String(temp, "utf-8");
//
///*                // 获取该路会话的id，将其提供给支持人员，有助于问题排查
//                // 也可以从Json结果中看到
//                String sid = event.data.getString("sid");
//                String tag = event.data.getString("tag");
//                // 获取从数据发送完到获取结果的耗时，单位：ms
//                // 也可以通过键名"bos_rslt"获取从开始发送数据到获取结果的耗时
//                long eosRsltTime = event.data.getLong("eos_rslt", -1); */
//
//                if (TextUtils.isEmpty(cntStr)) return;
//
//                JSONObject cntJson = new JSONObject(cntStr);
//                String sub = params.optString("sub");
//
//                Log.e(TAG, "processResult:" + sub + "  " + cntJson);
//                switch (sub) {
//                    case "nlp":  // 解析得到语义结果
//                        String resultStr = cntJson.optString("intent");
//                        if (!TextUtils.isEmpty(resultStr)) {
//                            JSONObject textRes = new JSONObject(resultStr);
//                            String t = textRes.optString("text");
//
//                            if (TextUtils.isEmpty(t))
//                                return;
//                            resetACK();
//                            if (mSttEventCallBack != null)
//                                mSttEventCallBack.onResultAIUI(t);
//                        }
//                        break;
//                    case "asr":  // 离线命令词识别
//                        break;
//                    case "iat":    //听写结果解析
//                        JSONObject text = cntJson.getJSONObject("text");
//                        JSONArray ws = text.getJSONArray("ws");
//                        String iatText = "";
//                        for (int i = 0; i < text.getJSONArray("ws").length(); i++) {
//                            iatText += ws.getJSONObject(i).getJSONArray("cw").getJSONObject(0).getString("w");
//                        }
//                        if (!TextUtils.isEmpty(iatText)) {
////                            if (mSttEventCallBack != null)
////                                mSttEventCallBack.onResult(iatText);
//                        }
//                        break;
//                }

            }
        } catch (Throwable e) {
//            e.printStackTrace();
        }
    }

    public void setSTTEventCallBack(STTEventCallBack mSttEventCallBack) {
        this.mSttEventCallBack = mSttEventCallBack;
    }

    /**
     * 销毁AIUI对象
     */
    public void onDestroy() {
        if (null != mAIUIAgent) {
            AIUIMessage stopMsg = new AIUIMessage(AIUIConstant.CMD_STOP, 0, 0, null, null);
            mAIUIAgent.sendMessage(stopMsg);
            mAIUIAgent.destroy();
            mAIUIAgent = null;
        }
    }

    /**
     * 获取aiui当前状态
     *
     * @return aiui当前状态
     * <p>
     * public static final int STATE_IDLE = 1; STATE_IDLE（空闲状态）
     * public static final int STATE_READY = 2; STATE_READY（就绪状态，待唤醒）
     * public static final int STATE_WORKING = 3; STATE_WORKING（工作状态，已唤醒）
     */
    public int getAIUIState() {
        return mAIUIState;
    }

    public boolean isPause() {
        return isPause;
    }

    public boolean isTTS() {
        return isTTS;
    }

    /**
     * 获取aiui默认参数
     *
     * @param context context
     * @return json
     */
    private String getAIUIParams(Context context) {
        String params = "";

        AssetManager assetManager = context.getResources().getAssets();
        try (InputStream ins = assetManager.open("cfg/aiui_phone.cfg")) {
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);

            JSONObject paramsJson = new JSONObject(params);

            params = paramsJson.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, params);
        return params;
    }

}
