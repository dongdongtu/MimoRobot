package com.chance.mimorobot.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.arcsoft.imageutil.ArcSoftImageFormat;
import com.arcsoft.imageutil.ArcSoftImageUtil;
import com.arcsoft.imageutil.ArcSoftImageUtilError;
import com.chance.mimorobot.arcface.FaceServer;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.db.DBManager;
import com.chance.mimorobot.db.dao.FaceEntityDao;
import com.chance.mimorobot.db.entity.FaceEntity;
import com.chance.mimorobot.model.BaseResponseModel;
import com.chance.mimorobot.model.FaceListModel;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.chance.mimorobot.arcface.FaceServer.SAVE_FEATURE_DIR;
import static com.chance.mimorobot.arcface.FaceServer.SAVE_IMG_DIR;

public class FaceUpdateService extends IntentService {
    private String TAG=FaceUpdateService.class.getSimpleName();
    private static final String REGISTER_DIR = Globle.FACE_PATH;
    private static final String REGISTER_FAILED_DIR = Globle.TEMP_PATH;
    private List<FaceEntity> addlist;
    private List<FaceEntity> deleteList;
    private List<FaceEntity> faillist;
    private FaceEntity receiverFacePhoto;
    private ExecutorService executorService;
    private String type = "";



    public FaceUpdateService(String name) {
        super(name);
    }

    public FaceUpdateService() {
        super("FaceUpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        addlist = new ArrayList<>();
        deleteList = new ArrayList<>();
        faillist = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FaceServer.getInstance().init(this);
        type = intent.getStringExtra("type");
        if (!type.equals("start")) {
            receiverFacePhoto = (FaceEntity) intent.getSerializableExtra("message");
        }
        switch (type) {
            case "start":
                updateFace();
                break;
            case "add":
                addlist.add(receiverFacePhoto);
                addListFace();
                break;
            case "delete":
                deleteList.add(receiverFacePhoto);
                deleteListFace();
                break;
            case "update":

                break;
            case "camera":
                if (doRegisterFormCameara(receiverFacePhoto)) {
                    DBManager.getInstance().getmDaoSession().getFaceEntityDao().insertOrReplace(receiverFacePhoto);
                    addListFace();
                }
                break;
            default:
                break;
        }
    }

    private void updateFace() {
        if(DBManager.getInstance().getmDaoSession().getFaceEntityDao().loadAll().size()>0){
            ApiManager.getInstance().getRobotServer().getFace(Globle.robotId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<FaceListModel>() {
                @Override
                public void accept(FaceListModel faceListModel) throws Exception {
                    addlist.clear();
                    deleteList.clear();
                    addlist.addAll(faceListModel.getAddList());
                    deleteList.addAll(faceListModel.getDelList());
                    addListFace();
                    deleteListFace();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {

                }
            });
        }else {
            ApiManager.getInstance().getRobotServer().getAllFace(Globle.robotId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<FaceListModel>() {
                @Override
                public void accept(FaceListModel faceListModel) throws Exception {
                    addlist.clear();
                    addlist.addAll(faceListModel.getDataList());
                    addListFace();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {

                }
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void addFace(FaceEntity faceEntity) {
        if (DBManager.getInstance().getmDaoSession().getFaceEntityDao().queryBuilder().where(FaceEntityDao.Properties.Faceid.eq(faceEntity.getFaceid())).unique() != null) {
            addListFace();
        }else{
            ApiManager.getInstance().getRobotServer().downloadFileWithDynamicUrlSync(faceEntity.getFaceImageUrl()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                Log.e(TAG,"addFace");
                if (FileUtils.writeResponseBodyToDisk(responseBody, Globle.DOWNLOAD_PATH + File.separator + faceEntity.getFaceid() + ".jpg")) {
                    if (doRegister(faceEntity)) {
                        DBManager.getInstance().getmDaoSession().getFaceEntityDao().insertOrReplace(faceEntity);
                        addListFace();
                    } else {
                        addListFace();
                    }
                } else {
                    faillist.add(faceEntity);
                    addListFace();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
        }
    }

    public void deleteFace(FaceEntity faceEntity) {
        File featureFile = new File(Globle.FACE_PATH + File.separator + SAVE_FEATURE_DIR + File.separator + faceEntity.getFaceid() + ".jpg");
        if (featureFile.exists()) {
            featureFile.delete();
        }
        File imgFile = new File(Globle.FACE_PATH + File.separator + SAVE_IMG_DIR + File.separator + faceEntity.getFaceid() + ".jpg");
        if (featureFile.exists()) {
            imgFile.delete();
        }
        if (DBManager.getInstance().getmDaoSession().getFaceEntityDao().queryBuilder().where(FaceEntityDao.Properties.Faceid.eq(faceEntity.getFaceid())).unique() != null) {
            DBManager.getInstance().getmDaoSession().getFaceEntityDao().delete(DBManager.getInstance().getmDaoSession().getFaceEntityDao().queryBuilder().where(FaceEntityDao.Properties.Faceid.eq(faceEntity.getFaceid())).unique());
        }
        deleteListFace();
    }


    public void deleteListFace() {
        if (deleteList.size() > 0) {
            deleteFace(deleteList.remove(0));
        } else {
            FaceServer.getInstance().initFaceList(FaceUpdateService.this);
        }
    }


    public void addListFace() {
        if (addlist.size() > 0) {
            addFace(addlist.remove(0));
        } else {
            if (type.equals("start")){
                ApiManager.getInstance().getRobotServer().upDateAllFace(Globle.robotId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResponseModel>() {
                    @Override
                    public void accept(BaseResponseModel baseResponseModel) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
            }
        }
    }


    private boolean doRegister(FaceEntity faceEntity) {
        File jpgFile = new File(Globle.DOWNLOAD_PATH + File.separator + faceEntity.getFaceid() + ".jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(Globle.DOWNLOAD_PATH + File.separator + faceEntity.getFaceid() + ".jpg");
        if (bitmap == null) {
            File failedFile = new File(REGISTER_FAILED_DIR + File.separator + faceEntity.getFaceid() + ".jpg");
            if (!failedFile.getParentFile().exists()) {
                failedFile.getParentFile().mkdirs();
            }
            jpgFile.renameTo(failedFile);
            return false;
        }
        bitmap = ArcSoftImageUtil.getAlignedBitmap(bitmap, true);
        if (bitmap == null) {
            File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName() + ".jpg");
            if (!failedFile.getParentFile().exists()) {
                failedFile.getParentFile().mkdirs();
            }
            jpgFile.renameTo(failedFile);
            return false;
        }
        byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
        int transformCode = ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);
        if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
            return false;
        }
        boolean success = FaceServer.getInstance().registerBgr24(FaceUpdateService.this, bgr24, bitmap.getWidth(), bitmap.getHeight(),
                faceEntity.getFaceid()+"");
        if (!success) {
            File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName() + ".jpg");
            if (!failedFile.getParentFile().exists()) {
                failedFile.getParentFile().mkdirs();
            }
            jpgFile.renameTo(failedFile);
            return false;
        }
        return true;
    }

    private boolean doRegisterFormCameara(FaceEntity faceEntity) {
        File jpgFile = new File(Globle.PIC_PATH + File.separator + faceEntity.getFaceid() + ".jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(Globle.PIC_PATH + File.separator + faceEntity.getFaceid() + ".jpg");
        if (bitmap == null) {
            File failedFile = new File(REGISTER_FAILED_DIR + File.separator + faceEntity.getFaceid() + ".jpg");
            if (!failedFile.getParentFile().exists()) {
                failedFile.getParentFile().mkdirs();
            }
            jpgFile.renameTo(failedFile);
            return false;
        }
        bitmap = ArcSoftImageUtil.getAlignedBitmap(bitmap, true);
        if (bitmap == null) {
            File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName() + ".jpg");
            if (!failedFile.getParentFile().exists()) {
                failedFile.getParentFile().mkdirs();
            }
            jpgFile.renameTo(failedFile);
            return false;
        }
        byte[] bgr24 = ArcSoftImageUtil.createImageData(bitmap.getWidth(), bitmap.getHeight(), ArcSoftImageFormat.BGR24);
        int transformCode = ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24);
        if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
            return false;
        }
        boolean success = FaceServer.getInstance().registerBgr24(FaceUpdateService.this, bgr24, bitmap.getWidth(), bitmap.getHeight(),
                faceEntity.getFaceid()+"");
        if (!success) {
            File failedFile = new File(REGISTER_FAILED_DIR + File.separator + jpgFile.getName() + ".jpg");
            if (!failedFile.getParentFile().exists()) {
                failedFile.getParentFile().mkdirs();
            }
            jpgFile.renameTo(failedFile);
            return false;
        }
        return true;
    }


}
