package com.chance.mimorobot.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.utils.ImageTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class CameraSaver implements Runnable {
    private Context mContext;

    private CameraSaverCallback mCallback;
    private Bitmap mImageReader;

    public CameraSaver(CameraSaverCallback callback, Context context, Bitmap bitmap) {
        this.mCallback = callback;
        this.mContext = context;
        this.mImageReader = bitmap;
    }
    @Override
    public void run() {
//        bestBitmap= ImageTools.convertNv21ToBmp(nv21, 0, camera.getParameters().getPreviewSize().width, camera.getParameters().getPreviewSize().height);
        File filePic;
        try {
            filePic = createJpeg();
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mImageReader.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            new UpdateMediaHelper().broadcastFile(mContext, filePic, true, false);
            mCallback.onComplete(filePic.getPath());
        } catch (IOException e) {
            Log.e("tag", "saveBitmap: " + e.getMessage());
            return;
        }
    }

    public interface CameraSaverCallback {
        void onComplete(String path);
    }

    /**
     * Save image to the SD card
     *
     * @param photoBitmap
     *
     */
    public void savePhotoToSDCard(Bitmap photoBitmap) {

    }


    /**
     * 创建jpeg的文件
     *
     * @return
     */
    private  File createJpeg() {
        long time = System.currentTimeMillis();
        File dir = new File(Globle.PIC_PATH);
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
        String finalName =format.format(time)+ ".jpg"; //命名规则
        Log.i("JpegSaver", finalName);
        return new File(dir, finalName);
    }
}
