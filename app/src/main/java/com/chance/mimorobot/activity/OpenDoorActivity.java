package com.chance.mimorobot.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.enums.DetectFaceOrientPriority;
import com.arcsoft.face.enums.DetectMode;
import com.bumptech.glide.Glide;
import com.chance.mimorobot.R;
import com.chance.mimorobot.arcface.CompareResult;
import com.chance.mimorobot.arcface.FaceServer;
import com.chance.mimorobot.arcface.model.DrawInfo;
import com.chance.mimorobot.arcface.model.FacePreviewInfo;
import com.chance.mimorobot.arcface.utils.DrawHelper;
import com.chance.mimorobot.arcface.utils.camera.CameraHelper;
import com.chance.mimorobot.arcface.utils.camera.CameraListener;
import com.chance.mimorobot.arcface.utils.face.FaceHelper;
import com.chance.mimorobot.arcface.utils.face.FaceListener;
import com.chance.mimorobot.arcface.utils.face.LivenessType;
import com.chance.mimorobot.arcface.utils.face.RecognizeColor;
import com.chance.mimorobot.arcface.utils.face.RequestFeatureStatus;
import com.chance.mimorobot.arcface.utils.face.RequestLivenessStatus;
import com.chance.mimorobot.arcface.widget.FaceRectView;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.db.DBManager;
import com.chance.mimorobot.db.dao.FaceEntityDao;
import com.chance.mimorobot.db.entity.FaceEntity;
import com.chance.mimorobot.manager.SharedPreferencesManager;
import com.chance.mimorobot.model.BaseResponseModel;
import com.chance.mimorobot.model.FaceRequestModel;
import com.chance.mimorobot.model.IDCardModel;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.service.FaceInfoService;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.utils.ImageTools;
import com.hdos.usbdevice.publicSecurityIDCardLib;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OpenDoorActivity extends TitleBarActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    @BindView(R.id.tv_idcard)
    TextView tvIdcard;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_nature)
    TextView tvNature;
    @BindView(R.id.tv_idnum)
    TextView tvIdnum;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_date)
    TextView tvDate;
    private String TAG = OpenDoorActivity.class.getSimpleName();
    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
    @BindView(R.id.single_camera_texture_preview)
    TextureView previewView;
    /**
     * 绘制人脸框的控件
     */
    @BindView(R.id.single_camera_face_rect_view)
    FaceRectView faceRectView;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_speak)
    TextView tvSpeak;

    private static final int MAX_DETECT_NUM = 10;
    /**
     * 当FR成功，活体未成功时，FR等待活体的时间
     */
    private static final int WAIT_LIVENESS_INTERVAL = 100;
    /**
     * 失败重试间隔时间（ms）
     */
    private static final long FAIL_RETRY_INTERVAL = 1000;
    /**
     * 出错重试最大次数
     */
    private static final int MAX_RETRY_TIME = 3;

    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    /**
     * 优先打开的摄像头，本界面主要用于单目RGB摄像头设备，因此默认打开前置
     */
    private Integer rgbCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;

    /**
     * VIDEO模式人脸检测引擎，用于预览帧人脸追踪
     */
    private FaceEngine ftEngine;
    /**
     * 用于特征提取的引擎
     */
    private FaceEngine frEngine;
    /**
     * IMAGE模式活体检测引擎，用于预览帧人脸活体检测
     */
    private FaceEngine flEngine;

    private int ftInitCode = -1;
    private int frInitCode = -1;
    private int flInitCode = -1;
    private FaceHelper faceHelper;
    private List<CompareResult> compareResultList;

    /**
     * 用于记录人脸识别相关状态
     */
    private ConcurrentHashMap<Integer, Integer> requestFeatureStatusMap = new ConcurrentHashMap<>();
    /**
     * 用于记录人脸特征提取出错重试次数
     */
    private ConcurrentHashMap<Integer, Integer> extractErrorRetryMap = new ConcurrentHashMap<>();
    /**
     * 用于存储活体值
     */
    private ConcurrentHashMap<Integer, Integer> livenessMap = new ConcurrentHashMap<>();
    /**
     * 用于存储活体检测出错重试次数
     */
    private ConcurrentHashMap<Integer, Integer> livenessErrorRetryMap = new ConcurrentHashMap<>();

    private CompositeDisposable getFeatureDelayedDisposables = new CompositeDisposable();
    private CompositeDisposable delayFaceTaskCompositeDisposable = new CompositeDisposable();


    /**
     * 活体检测的开关
     */
    private boolean livenessDetect = true;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 识别阈值
     */
    private static final float SIMILAR_THRESHOLD = 0.7F;


    private publicSecurityIDCardLib iDCardDevice;

    private Bitmap bitmap;
    byte[] BmpFile = new byte[38556];

    public static boolean isStop = false;
    private int ret = 0;

    String pkName;

    public static boolean isStart = false;
    private int state = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().setAttributes(attributes);
        }

        // Activity启动后就锁定为启动时的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //本地人脸库初始化
        FaceServer.getInstance().init(this);
        pkName = this.getPackageName();
        iDCardDevice = new publicSecurityIDCardLib(OpenDoorActivity.this);
        initView();
    }

    private void initView() {
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        compareResultList = new ArrayList<>();
    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_opendoor;
    }

    @Override
    public void onGlobalLayout() {
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        initEngine();
        initCamera();
    }

    /**
     * 初始化引擎
     */
    private void initEngine() {
        ftEngine = new FaceEngine();
        ftInitCode = ftEngine.init(this, DetectMode.ASF_DETECT_MODE_VIDEO, SharedPreferencesManager.newInstance().getFtOrient(),
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_GENDER | FaceEngine.ASF_LIVENESS);

        frEngine = new FaceEngine();
        frInitCode = frEngine.init(this, DetectMode.ASF_DETECT_MODE_IMAGE, DetectFaceOrientPriority.ASF_OP_0_ONLY,
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_RECOGNITION);

        flEngine = new FaceEngine();
        flInitCode = flEngine.init(this, DetectMode.ASF_DETECT_MODE_IMAGE, DetectFaceOrientPriority.ASF_OP_0_ONLY,
                16, MAX_DETECT_NUM, FaceEngine.ASF_LIVENESS);

        Log.i(TAG, "initEngine:  init: " + ftInitCode);

        if (ftInitCode != ErrorInfo.MOK) {
            String error = getString(R.string.specific_engine_init_failed, "ftEngine", ftInitCode);
            Log.i(TAG, "initEngine: " + error);
            showToast(error);
        }
        if (frInitCode != ErrorInfo.MOK) {
            String error = getString(R.string.specific_engine_init_failed, "frEngine", frInitCode);
            Log.i(TAG, "initEngine: " + error);
            showToast(error);
        }
        if (flInitCode != ErrorInfo.MOK) {
            String error = getString(R.string.specific_engine_init_failed, "flEngine", flInitCode);
            Log.i(TAG, "initEngine: " + error);
            showToast(error);
        }
    }

    /**
     * 销毁引擎，faceHelper中可能会有特征提取耗时操作仍在执行，加锁防止crash
     */
    private void unInitEngine() {
        if (ftInitCode == ErrorInfo.MOK && ftEngine != null) {
            synchronized (ftEngine) {
                int ftUnInitCode = ftEngine.unInit();
                Log.i(TAG, "unInitEngine: " + ftUnInitCode);
            }
        }
        if (frInitCode == ErrorInfo.MOK && frEngine != null) {
            synchronized (frEngine) {
                int frUnInitCode = frEngine.unInit();
                Log.i(TAG, "unInitEngine: " + frUnInitCode);
            }
        }
        if (flInitCode == ErrorInfo.MOK && flEngine != null) {
            synchronized (flEngine) {
                int flUnInitCode = flEngine.unInit();
                Log.i(TAG, "unInitEngine: " + flUnInitCode);
            }
        }
    }


    @Override
    protected void onDestroy() {

        if (cameraHelper != null) {
            cameraHelper.release();
            cameraHelper = null;
        }

        unInitEngine();
        if (getFeatureDelayedDisposables != null) {
            getFeatureDelayedDisposables.clear();
        }
        if (delayFaceTaskCompositeDisposable != null) {
            delayFaceTaskCompositeDisposable.clear();
        }
        if (faceHelper != null) {
//            ConfigUtil.setTrackedFaceCount(this, faceHelper.getTrackedFaceCount());
            faceHelper.release();
            faceHelper = null;
        }
        isStop = true;
        if(iDCardDevice != null)  iDCardDevice.closeDevice();
        FaceServer.getInstance().unInit();
        startService(new Intent(OpenDoorActivity.this, FaceInfoService.class));

        super.onDestroy();
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        final FaceListener faceListener = new FaceListener() {
            @Override
            public void onFail(Exception e) {
                Log.e(TAG, "onFail: " + e.getMessage());
            }

            //请求FR的回调
            @Override
            public void onFaceFeatureInfoGet(@Nullable final FaceFeature faceFeature, final Integer requestId, final Integer errorCode) {
                //FR成功
                if (faceFeature != null) {
//                    Log.i(TAG, "onPreview: fr end = " + System.currentTimeMillis() + " trackId = " + requestId);
                    Integer liveness = livenessMap.get(requestId);
                    //不做活体检测的情况，直接搜索
                    if (!livenessDetect) {
                        Log.i(TAG, "searchFace");
                        searchFace(faceFeature, requestId);
                    }
                    //活体检测通过，搜索特征
                    else if (liveness != null && liveness == LivenessInfo.ALIVE) {
                        Log.i(TAG, "searchFace1");
                        searchFace(faceFeature, requestId);
                    }
                    //活体检测未出结果，或者非活体，延迟执行该函数
                    else {
                        if (requestFeatureStatusMap.containsKey(requestId)) {
                            Observable.timer(WAIT_LIVENESS_INTERVAL, TimeUnit.MILLISECONDS)
                                    .subscribe(new Observer<Long>() {
                                        Disposable disposable;

                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            disposable = d;
                                            getFeatureDelayedDisposables.add(disposable);
                                        }

                                        @Override
                                        public void onNext(Long aLong) {
                                            onFaceFeatureInfoGet(faceFeature, requestId, errorCode);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                            getFeatureDelayedDisposables.remove(disposable);
                                        }
                                    });
                        }
                    }

                }
                //特征提取失败
                else {
                    if (increaseAndGetValue(extractErrorRetryMap, requestId) > MAX_RETRY_TIME) {
                        extractErrorRetryMap.put(requestId, 0);

                        String msg;
                        // 传入的FaceInfo在指定的图像上无法解析人脸，此处使用的是RGB人脸数据，一般是人脸模糊
                        if (errorCode != null && errorCode == ErrorInfo.MERR_FSDK_FACEFEATURE_LOW_CONFIDENCE_LEVEL) {
                            msg = getString(R.string.low_confidence_level);
                        } else {
                            msg = "ExtractCode:" + errorCode;
                        }

//                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, msg));
                        // 在尝试最大次数后，特征提取仍然失败，则认为识别未通过
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
                        retryRecognizeDelayed(requestId);
                    } else {
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.TO_RETRY);
                    }
                }
            }

            @Override
            public void onFaceLivenessInfoGet(@Nullable LivenessInfo livenessInfo, final Integer requestId, Integer errorCode) {
                if (livenessInfo != null) {
                    int liveness = livenessInfo.getLiveness();
                    livenessMap.put(requestId, liveness);
                    // 非活体，重试
                    if (liveness == LivenessInfo.NOT_ALIVE) {
//                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, "NOT_ALIVE"));
                        // 延迟 FAIL_RETRY_INTERVAL 后，将该人脸状态置为UNKNOWN，帧回调处理时会重新进行活体检测
                        retryLivenessDetectDelayed(requestId);
                    }
                } else {
                    if (increaseAndGetValue(livenessErrorRetryMap, requestId) > MAX_RETRY_TIME) {
                        livenessErrorRetryMap.put(requestId, 0);
                        String msg;
                        // 传入的FaceInfo在指定的图像上无法解析人脸，此处使用的是RGB人脸数据，一般是人脸模糊
                        if (errorCode != null && errorCode == ErrorInfo.MERR_FSDK_FACEFEATURE_LOW_CONFIDENCE_LEVEL) {
                            msg = getString(R.string.low_confidence_level);
                        } else {
                            msg = "ProcessCode:" + errorCode;
                        }
//                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, msg));
                        retryLivenessDetectDelayed(requestId);
                    } else {
                        livenessMap.put(requestId, LivenessInfo.UNKNOWN);
                    }
                }
            }


        };


        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                Camera.Size lastPreviewSize = previewSize;
                camera.setDisplayOrientation(180);
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewSize.width, previewSize.height, previewView.getWidth(), previewView.getHeight(), 180
                        , cameraId, isMirror, false, false);
                Log.i(TAG, "onCameraOpened: " + drawHelper.toString());
                // 切换相机的时候可能会导致预览尺寸发生变化
                if (faceHelper == null ||
                        lastPreviewSize == null ||
                        lastPreviewSize.width != previewSize.width || lastPreviewSize.height != previewSize.height) {
                    Integer trackedFaceCount = null;
                    // 记录切换时的人脸序号
                    if (faceHelper != null) {
                        trackedFaceCount = faceHelper.getTrackedFaceCount();
                        faceHelper.release();
                    }
                    faceHelper = new FaceHelper.Builder()
                            .ftEngine(ftEngine)
                            .frEngine(frEngine)
                            .flEngine(flEngine)
                            .frQueueSize(MAX_DETECT_NUM)
                            .flQueueSize(MAX_DETECT_NUM)
                            .previewSize(previewSize)
                            .faceListener(faceListener)
                            .trackedFaceCount(trackedFaceCount == null ? SharedPreferencesManager.newInstance().getTrackedFaceCount() : trackedFaceCount)
                            .build();
                }
            }


            @Override
            public void onPreview(final byte[] nv21, Camera camera) {
                if (faceRectView != null) {
                    faceRectView.clearFaceInfo();
                }
                List<FacePreviewInfo> facePreviewInfoList = faceHelper.onPreviewFrame(nv21);
                if (facePreviewInfoList != null && faceRectView != null && drawHelper != null) {
                    drawPreviewInfo(facePreviewInfoList);
                }
//                registerFace(nv21, facePreviewInfoList);
                clearLeftFace(facePreviewInfoList);

                if (facePreviewInfoList != null && facePreviewInfoList.size() > 0 && previewSize != null) {
                    for (int i = 0; i < facePreviewInfoList.size(); i++) {
                        Integer status = requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId());
                        /**
                         * 在活体检测开启，在人脸识别状态不为成功或人脸活体状态不为处理中（ANALYZING）且不为处理完成（ALIVE、NOT_ALIVE）时重新进行活体检测
                         */
                        if (livenessDetect && (status == null || status != RequestFeatureStatus.SUCCEED)) {
                            Integer liveness = livenessMap.get(facePreviewInfoList.get(i).getTrackId());
                            if (liveness == null
                                    || (liveness != LivenessInfo.ALIVE && liveness != LivenessInfo.NOT_ALIVE && liveness != RequestLivenessStatus.ANALYZING)) {
                                livenessMap.put(facePreviewInfoList.get(i).getTrackId(), RequestLivenessStatus.ANALYZING);
                                faceHelper.requestFaceLiveness(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId(), LivenessType.RGB);
                            }
                        }
                        /**
                         * 对于每个人脸，若状态为空或者为失败，则请求特征提取（可根据需要添加其他判断以限制特征提取次数），
                         * 特征提取回传的人脸特征结果在{@link FaceListener#onFaceFeatureInfoGet(FaceFeature, Integer, Integer)}中回传
                         */
                        if (status == null
                                || status == RequestFeatureStatus.TO_RETRY) {
                            requestFeatureStatusMap.put(facePreviewInfoList.get(i).getTrackId(), RequestFeatureStatus.SEARCHING);
                            faceHelper.requestFaceFeature(nv21, facePreviewInfoList.get(i).getFaceInfo(), previewSize.width, previewSize.height, FaceEngine.CP_PAF_NV21, facePreviewInfoList.get(i).getTrackId());
//                            Log.i(TAG, "onPreview: fr start = " + System.currentTimeMillis() + " trackId = " + facePreviewInfoList.get(i).getTrackedFaceCount());
                        }
                    }
                }
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };

        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraID != null ? rgbCameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        cameraHelper.start();
    }


    /**
     * 将map中key对应的value增1回传
     *
     * @param countMap map
     * @param key      key
     * @return 增1后的value
     */
    public int increaseAndGetValue(Map<Integer, Integer> countMap, int key) {
        if (countMap == null) {
            return 0;
        }
        Integer value = countMap.get(key);
        if (value == null) {
            value = 0;
        }
        countMap.put(key, ++value);
        return value;
    }

    private void searchFace(final FaceFeature frFace, final Integer requestId) {
        Observable
                .create(new ObservableOnSubscribe<CompareResult>() {
                    @Override
                    public void subscribe(ObservableEmitter<CompareResult> emitter) {
//                        Log.i(TAG, "subscribe: fr search start = " + System.currentTimeMillis() + " trackId = " + requestId);
                        CompareResult compareResult = FaceServer.getInstance().getTopOfFaceLib(frFace);
//                        Log.i(TAG, "subscribe: fr search end = " + System.currentTimeMillis() + " trackId = " + requestId);
                        emitter.onNext(compareResult);

                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompareResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CompareResult compareResult) {
                        if (compareResult == null || compareResult.getUserName() == null) {
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
//                            faceHelper.setName(requestId, "VISITOR " + requestId);
                            return;
                        }

//                        Log.i(TAG, "onNext: fr search get result  = " + System.currentTimeMillis() + " trackId = " + requestId + "  similar = " + compareResult.getSimilar());
                        if (compareResult.getSimilar() > SIMILAR_THRESHOLD) {
                            Log.i(TAG, "compareResult1");
                            boolean isAdded = false;
                            if (compareResultList == null) {
                                requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
//                                faceHelper.setName(requestId, "VISITOR " + requestId);
                                return;
                            }
                            Log.i(TAG, "compareResult2");
                            for (CompareResult compareResult1 : compareResultList) {
                                if (compareResult1.getTrackId() == requestId) {
                                    isAdded = true;
                                    break;
                                }
                            }
                            Log.i(TAG, "compareResult3");
                            if (!isAdded) {
                                //对于多人脸搜索，假如最大显示数量为 MAX_DETECT_NUM 且有新的人脸进入，则以队列的形式移除
                                if (compareResultList.size() >= MAX_DETECT_NUM) {
                                    compareResultList.remove(0);
//                                    adapter.notifyItemRemoved(0);
                                }
                                //添加显示人员时，保存其trackId
                                compareResult.setTrackId(requestId);
                                compareResultList.add(compareResult);
//                                adapter.notifyItemInserted(compareResultList.size() - 1);
                            }
                            Log.i(TAG, "compareResult4" + compareResult.getUserName());
                            requestFeatureStatusMap.put(requestId, RequestFeatureStatus.SUCCEED);
                            if (DBManager.getInstance().getmDaoSession().getFaceEntityDao().queryBuilder().where(FaceEntityDao.Properties.Faceid.eq(compareResult.getUserName())).unique() != null) {
                                Log.e(TAG, "compareResult.getUserName()");
                                FaceEntity faceEntity = DBManager.getInstance().getmDaoSession().getFaceEntityDao().queryBuilder().where(FaceEntityDao.Properties.Faceid.eq(compareResult.getUserName())).unique();
                                File imgFile = new File(FaceServer.ROOT_PATH + File.separator + FaceServer.SAVE_IMG_DIR + File.separator + compareResult.getUserName() + FaceServer.IMG_SUFFIX);
                                Glide.with(OpenDoorActivity.this)
                                        .load(imgFile)
                                        .into(ivImg);
                                tvName.setText(faceEntity.getName());
                                tvSpeak.setText(faceEntity.getSayHelloText());
                                Output.speak(faceEntity.getSayHelloText());
                                uploadFace(faceEntity);
                            }

//                            faceHelper.setName(requestId, getString(R.string.recognize_success_notice, compareResult.getUserName()));

                        } else {
//                            faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, "NOT_REGISTERED"));
                            retryRecognizeDelayed(requestId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        faceHelper.setName(requestId, getString(R.string.recognize_failed_notice, "NOT_REGISTERED"));
                        retryRecognizeDelayed(requestId);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void uploadFace(FaceEntity faceEntity) {
        FaceRequestModel faceRequestModel = new FaceRequestModel();
        faceRequestModel.setFaceid(faceEntity.getFaceid());
        faceRequestModel.setName(faceEntity.getName());
        faceRequestModel.setRobotno(Globle.robotId);
        ApiManager.getInstance().getRobotServer().upLoadFace(faceRequestModel).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResponseModel>() {
            @Override
            public void accept(BaseResponseModel baseResponseModel) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }


    /**
     * 删除已经离开的人脸
     *
     * @param facePreviewInfoList 人脸和trackId列表
     */
    private void clearLeftFace(List<FacePreviewInfo> facePreviewInfoList) {
        if (compareResultList != null) {
            for (int i = compareResultList.size() - 1; i >= 0; i--) {
                if (!requestFeatureStatusMap.containsKey(compareResultList.get(i).getTrackId())) {
                    compareResultList.remove(i);
                }
            }
        }
        if (facePreviewInfoList == null || facePreviewInfoList.size() == 0) {
            requestFeatureStatusMap.clear();
            livenessMap.clear();
            livenessErrorRetryMap.clear();
            extractErrorRetryMap.clear();
            if (getFeatureDelayedDisposables != null) {
                getFeatureDelayedDisposables.clear();
            }
            return;
        }
        Enumeration<Integer> keys = requestFeatureStatusMap.keys();
        while (keys.hasMoreElements()) {
            int key = keys.nextElement();
            boolean contained = false;
            for (FacePreviewInfo facePreviewInfo : facePreviewInfoList) {
                if (facePreviewInfo.getTrackId() == key) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                requestFeatureStatusMap.remove(key);
                livenessMap.remove(key);
                livenessErrorRetryMap.remove(key);
                extractErrorRetryMap.remove(key);
            }
        }


    }

    private void drawPreviewInfo(List<FacePreviewInfo> facePreviewInfoList) {
        List<DrawInfo> drawInfoList = new ArrayList<>();
        for (int i = 0; i < facePreviewInfoList.size(); i++) {
            String name = faceHelper.getName(facePreviewInfoList.get(i).getTrackId());
            Integer liveness = livenessMap.get(facePreviewInfoList.get(i).getTrackId());
            Integer recognizeStatus = requestFeatureStatusMap.get(facePreviewInfoList.get(i).getTrackId());

            // 根据识别结果和活体结果设置颜色
            int color = RecognizeColor.COLOR_UNKNOWN;
            if (recognizeStatus != null) {
                if (recognizeStatus == RequestFeatureStatus.FAILED) {
                    color = RecognizeColor.COLOR_FAILED;
                }
                if (recognizeStatus == RequestFeatureStatus.SUCCEED) {
                    color = RecognizeColor.COLOR_SUCCESS;
                }
            }
            if (liveness != null && liveness == LivenessInfo.NOT_ALIVE) {
                color = RecognizeColor.COLOR_FAILED;
            }


            List<AgeInfo> ageInfoList = new ArrayList<>();
            List<GenderInfo> genderInfoList = new ArrayList<>();
            List<LivenessInfo> faceLivenessInfoList = new ArrayList<>();
            int ageCode = ftEngine.getAge(ageInfoList);
            int genderCode = ftEngine.getGender(genderInfoList);
            if ((ageCode | genderCode) != ErrorInfo.MOK) {
                Log.e(TAG, ageCode + "  " + genderCode);
                return;
            }
            drawInfoList.add(new DrawInfo(drawHelper.adjustRect(facePreviewInfoList.get(i).getFaceInfo().getRect()),
                    genderInfoList.get(i).getGender(), ageInfoList.get(i).getAge(), liveness == null ? LivenessInfo.UNKNOWN : liveness, color,
                    " "));
        }
        drawHelper.draw(faceRectView, drawInfoList);
    }

    /**
     * 延迟 FAIL_RETRY_INTERVAL 重新进行活体检测
     *
     * @param requestId 人脸ID
     */
    private void retryLivenessDetectDelayed(final Integer requestId) {
        Observable.timer(FAIL_RETRY_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        delayFaceTaskCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        // 将该人脸状态置为UNKNOWN，帧回调处理时会重新进行活体检测
                        if (livenessDetect) {
                            faceHelper.setName(requestId, Integer.toString(requestId));
                        }
                        livenessMap.put(requestId, LivenessInfo.UNKNOWN);
                        delayFaceTaskCompositeDisposable.remove(disposable);
                    }
                });
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 延迟 FAIL_RETRY_INTERVAL 重新进行人脸识别
     *
     * @param requestId 人脸ID
     */
    private void retryRecognizeDelayed(final Integer requestId) {
        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.FAILED);
        Observable.timer(FAIL_RETRY_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        delayFaceTaskCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        // 将该人脸特征提取状态置为FAILED，帧回调处理时会重新进行活体检测
                        faceHelper.setName(requestId, Integer.toString(requestId));
                        requestFeatureStatusMap.put(requestId, RequestFeatureStatus.TO_RETRY);
                        delayFaceTaskCompositeDisposable.remove(disposable);
                    }
                });
    }


    /**
     * 获取身份证阅读器状态
     */
    private void getSAMStatus() {
        try {
            ret = iDCardDevice.getSAMStatus();
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (ret == 0x90) {
            state = 1;
        } else {
            Flowable.timer(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iDCardDevice = new publicSecurityIDCardLib(OpenDoorActivity.this);
                            getSAMStatus();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSAMStatus();
        Flowable.timer(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (state == 1) {
                    isStart = true;
                    getIDDate();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStart = false;
        isStop=true;
    }


    public void getIDDate() {

        String[] decodeInfo = new String[13];
        Arrays.fill(BmpFile, (byte) 0x00);
        if (iDCardDevice == null) {
            iDCardDevice = new publicSecurityIDCardLib(OpenDoorActivity.this);
        }
//				ret =iDCardDevice.readBaseMsgToStr(pkName,BmpFile,FpMsg,name,sex,nation, birth, address, IDNo, Department,
//						EffectDate, ExpireDate);
        try {
            ret = iDCardDevice.PICC_Reader_ForeignerIDCard(decodeInfo, BmpFile, pkName);

            if (ret < 0) {
                Log.e("TAG", "读卡错误，原因：" + decodeInfo[12] + isStop);
                if (!isStop) {
                    getSAMStatus();
                    Flowable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            getIDDate();
                        }
                    });
                } else {
                    isStop = false;
                    return;
                }
//                return;
            } else if (ret == 0) {
                isStart = false;
                isStop = false;
                int[] colors = iDCardDevice.convertByteToColor(BmpFile);
//                showString("读取成功");
//                        showString("中华人民共和国居民身份证" );
//                        showString("姓名：" + decodeInfo[0] + "\n" + "性别：" + decodeInfo[1] + "\n" + "民族：" + decodeInfo[2] + "\n"
//                                + "出生日期：" + decodeInfo[3] + "\n" + "地址：" + decodeInfo[4] + "\n" + "身份号码：" + decodeInfo[5] + "\n"
//                                + "签发机关：" + decodeInfo[6] + "\n" + "有效期限：" + decodeInfo[7] + "-" + decodeInfo[8] + "\n"
//                                + decodeInfo[9] + "\n");

                Bitmap bm = Bitmap.createBitmap(colors, 102, 126, Bitmap.Config.ARGB_8888);

                bitmap = Bitmap.createScaledBitmap(bm,
                        (int) (102 * 2), (int) (126 * 2), false);

                tvName.setText("姓名：" + decodeInfo[0]);
                tvGender.setText("性别：" + decodeInfo[1]);
                tvNature.setText("民族：" + decodeInfo[2]);
                tvIdnum.setText("身份证号码：" + decodeInfo[5]);
                tvAddress.setText("签发机关：" + decodeInfo[6]);
                tvDate.setText("有效期限：" + decodeInfo[7] + "-" + decodeInfo[8]);
                ivImg.setImageBitmap(bitmap);
                IDCardModel idCardModel = new IDCardModel();
                idCardModel.setRobotNo(Globle.robotId);
                idCardModel.setName(decodeInfo[0].trim());
                idCardModel.setSex(decodeInfo[1].trim());
                idCardModel.setNation(decodeInfo[2].trim());
                idCardModel.setBirthday(decodeInfo[3].trim());
                idCardModel.setVisaAuthority(decodeInfo[6].trim());
                idCardModel.setAddress(decodeInfo[4].trim());
                idCardModel.setTermOfValidityB(decodeInfo[7].trim());
                idCardModel.setTermOfValidityE(decodeInfo[8].trim());
                idCardModel.setIDCard(decodeInfo[5].trim());
                idCardModel.setPicFromIDCard(ImageTools.convertIconToString(bitmap));
                ApiManager.getInstance().getRobotServer().postIDcard(idCardModel).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResponseModel>() {
                    @Override
                    public void accept(BaseResponseModel baseResponseModel) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


            } else if (ret == 1) {
                isStop = false;
                isStart = false;
                int[] colors = iDCardDevice.convertByteToColor(BmpFile);
                Bitmap bm = Bitmap.createBitmap(colors, 102, 126, Bitmap.Config.ARGB_8888);
                Bitmap bm1 = Bitmap.createScaledBitmap(bm,
                        (int) (102 * 2), (int) (126 * 2), false); //这里你可以自定义它的大小
            } else {
                isStop = false;
                getSAMStatus();
                Flowable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        getIDDate();
                    }
                });
            }


        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}