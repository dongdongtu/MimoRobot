package com.chance.mimorobot.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.chance.mimorobot.arcface.utils.face.RecognizeColor;
import com.chance.mimorobot.arcface.utils.face.RequestFeatureStatus;
import com.chance.mimorobot.arcface.widget.FaceRectView;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.db.entity.FaceEntity;
import com.chance.mimorobot.helper.CameraSaver;
import com.chance.mimorobot.helper.FaceTrackSpeedParse;
import com.chance.mimorobot.manager.SerialControlManager;
import com.chance.mimorobot.manager.SharedPreferencesManager;
import com.chance.mimorobot.model.BaseResponseModel;
import com.chance.mimorobot.model.TempRequestModel;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.service.FaceInfoService;
import com.chance.mimorobot.service.FaceUpdateService;
import com.chance.mimorobot.statemachine.robot.Output;
import com.chance.mimorobot.utils.ImageTools;
import com.chance.mimorobot.widget.CircleImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CameraActivity extends TitleBarActivity implements ViewTreeObserver.OnGlobalLayoutListener, CameraSaver.CameraSaverCallback {

    @BindView(R.id.iv_shot)
    ImageView ivShot;
    @BindView(R.id.iv_picture)
    CircleImageView ivPicture;
    @BindView(R.id.tv_camera_text)
    TextView tvCameraText;
    private String TAG = CameraActivity.class.getSimpleName();


    /**
     * 相机预览显示的控件，可为SurfaceView或TextureView
     */
    @BindView(R.id.single_camera_texture_preview)
    TextureView previewView;
    @BindView(R.id.single_camera_face_rect_view)
    FaceRectView faceRectView;
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
    private boolean livenessDetect = true;
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
    private int name = -1;
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

    private Bitmap bestBitmap;
    private int mWidth;
    private int mHeight;

    private float temp;

    private boolean isStart = false;
    private int time = 0;

    private MaterialDialog mConfirmDialog;

    private EditText nameEditText;


    private int num = 0;

    private float vx, vy = 0;

    private Disposable moveDisposable = null;


    @Override
    int getContentLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().setAttributes(attributes);
        }

        // Activity启动后就锁定为启动时的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        ivPicture.bringToFront();
    }

    @Override
    public void onGlobalLayout() {
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        initEngine();
        initCamera();
    }

    @OnClick({R.id.iv_shot, R.id.iv_picture, R.id.iv_title_bar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                finish();
                break;
            case R.id.iv_shot:
                takeShot();
                break;
            case R.id.iv_picture:
//                ((CameraActivity) getActivity()).navToAlbum();
                break;
            default:
                break;
        }
    }


    /**
     * 初始化引擎
     */
    private void initEngine() {
        ftEngine = new FaceEngine();
        ftInitCode = ftEngine.init(this, DetectMode.ASF_DETECT_MODE_VIDEO, SharedPreferencesManager.newInstance().getFtOrient(),
                16, MAX_DETECT_NUM, FaceEngine.ASF_FACE_DETECT);

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

        FaceServer.getInstance().unInit();
        startService(new Intent(CameraActivity.this, FaceInfoService.class));

        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        moveDisposable.dispose();
        moveDisposable = null;
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

            }

            @Override
            public void onFaceLivenessInfoGet(@Nullable LivenessInfo livenessInfo, final Integer requestId, Integer errorCode) {

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
//                if(name!=(facePreviewInfoList.get(0).getTrackId())){
//                    facePreviewInfoList.get(0).getFaceInfo().getRect().left
//                }
                if (facePreviewInfoList.size() > 0) {
//                    int hor = (facePreviewInfoList.get(0).getFaceInfo().getRect().left + facePreviewInfoList.get(0).getFaceInfo().getRect().right) / 2;
//                    int ver = (facePreviewInfoList.get(0).getFaceInfo().getRect().top + facePreviewInfoList.get(0).getFaceInfo().getRect().bottom) / 2;
//                    if (hor > 800 && hor < 1100 && ver > 400 && ver < 600 && name != facePreviewInfoList.get(0).getTrackId()) {
//                        name = facePreviewInfoList.get(0).getTrackId();
//                        Log.e(TAG, "POST");
//
////                        upLoadResult(bestBitmap);
//                    }
                    trackFaceMove(camera, facePreviewInfoList.get(0));
                }
                if (isStart && time == 1) {

                    isStart = false;
                    time++;
                    num = facePreviewInfoList.size();
                    try {
                        bestBitmap = ImageTools.convertNv21ToBmp(nv21, 180, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    new Handler().post(new CameraSaver(CameraActivity.this, CameraActivity.this, bestBitmap));
                }
//                registerFace(nv21, facePreviewInfoList);
                clearLeftFace(facePreviewInfoList);

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


    public void takeShot() {
        isStart = true;
        time = 1;
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

    public void upLoadResult(Bitmap bitmap) {
        TempRequestModel tempRequestModel = new TempRequestModel();
        tempRequestModel.setImg(ImageTools.convertIconToString(bitmap));
        tempRequestModel.setRobotNo(Globle.robotId);
        tempRequestModel.setTemperature(temp);
        ApiManager.getInstance().getRobotServer().uploadTemp(tempRequestModel).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResponseModel>() {
            @Override
            public void accept(BaseResponseModel baseResponseModel) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }


    private void confirmPhoto(String path) {
        if (mConfirmDialog != null && mConfirmDialog.isShowing()) {
            return;
        }
        Output.speak("需要添加人脸吗？");
        mConfirmDialog = new MaterialDialog.Builder(CameraActivity.this)
                .title("需要添加人脸吗？")
                .customView(R.layout.single_image, true)
                .cancelable(false)
                .autoDismiss(false)
                .positiveText("好的")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (nameEditText != null && !TextUtils.isEmpty(nameEditText.getText().toString()))
                            surePhoto(path);
                        else
                            showToast("请输入名字");
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mConfirmDialog.dismiss();
                    }
                })
                .show();

//        Log.i("onComplete", path);
        View customView = mConfirmDialog.getCustomView();
        if (customView != null) {
            ImageView image = customView.findViewById(R.id.image);
            Glide.with(CameraActivity.this).load(path).override(640, 400).into(image);
            nameEditText = customView.findViewById(R.id.ed_text);
        }
    }

    public void surePhoto(String path) {
        mConfirmDialog.dismiss();
        //满意上传云端
        String name = path.substring(path.lastIndexOf("/") + 1, path.length() - 4);
        Log.e(TAG, "PATH = " + name);
        FaceEntity faceEntity = new FaceEntity();
        faceEntity.setFaceid(Long.valueOf(name));
        faceEntity.setName(nameEditText.getText().toString().trim());
        faceEntity.setRobotno(Globle.robotId);
        faceEntity.setSayHelloText("你好" + nameEditText.getText().toString().trim());
        updateFaceService("camera", faceEntity);
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

            drawInfoList.add(new DrawInfo(drawHelper.adjustRect(facePreviewInfoList.get(i).getFaceInfo().getRect()),
                    GenderInfo.UNKNOWN, AgeInfo.UNKNOWN_AGE, liveness == null ? LivenessInfo.UNKNOWN : liveness, color, ""));
        }
        drawHelper.draw(faceRectView, drawInfoList);
    }

    @Override
    public void onComplete(String path) {
        if (num > 0) {
            confirmPhoto(path);
        }
        Glide.with(CameraActivity.this).load(path).skipMemoryCache(true).override(300, 300)
                .centerCrop().into(ivPicture);
    }

    public void updateFaceService(String type, FaceEntity faceEntity) {
        Intent intent = new Intent(getApplicationContext(), FaceUpdateService.class);
        intent.putExtra("type", type);
        intent.putExtra("message", faceEntity);
        startService(intent);
    }


    /**
     * 跟随人脸运动
     *
     * @param
     */
    private void trackFaceMove(Camera camera, FacePreviewInfo facePreviewInfo) {
        float centerX = (float) facePreviewInfo.getFaceInfo().getRect().centerX() / (float) camera.getParameters().getPreviewSize().width;
        float centerY = (float) facePreviewInfo.getFaceInfo().getRect().centerY() / (float) camera.getParameters().getPreviewSize().height;
        float width = (float) facePreviewInfo.getFaceInfo().getRect().width() / (float) camera.getParameters().getPreviewSize().width;
        vx = FaceTrackSpeedParse.getVx(centerX);
        vy = FaceTrackSpeedParse.getVy(centerY);
        float vz = FaceTrackSpeedParse.getVz(width);
//        Log.e("CameraFragment", "vx=" + vx + ",vy=" + vy);
//        cameraPresenter.trackFaceMove(vx, vy);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (moveDisposable == null) {
            moveDisposable = Flowable.interval(500, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    if (vx < 0) {
                        Log.e(TAG, "vx=" + vx + ",vy=" + vy);
                        SerialControlManager.newInstance().headTurnLeft(3);
                    } else if (vx > 0) {
                        Log.e(TAG, "vx=" + vx + ",vy=" + vy);
                        SerialControlManager.newInstance().headTurnRight(3);
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
