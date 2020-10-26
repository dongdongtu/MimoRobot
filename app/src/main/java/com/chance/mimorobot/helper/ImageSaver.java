package com.chance.mimorobot.helper;

import android.content.Context;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;


import com.chance.mimorobot.constant.Constant;
import com.chance.mimorobot.constant.Globle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Random;


public class ImageSaver implements Runnable {

    private ImageReader mImageReader;
    private Context mContext;

    private ImageSaverCallback mCallback;


    public ImageSaver(ImageSaverCallback callback, Context context, ImageReader mImageReader) {
        this.mCallback = callback;
        this.mContext = context;
        this.mImageReader = mImageReader;
    }

    @Override
    public void run() {
        Log.i("level", "ImageSaver--->run");
        Image image = mImageReader.acquireLatestImage();
        checkParentDir();
        File file;
        checkJpegDir();
        file = createJpeg();
        try {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            try {
                save(bytes, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new UpdateMediaHelper().broadcastFile(mContext, file, true, false);
            image.close();
            mCallback.onComplete(file.getPath());

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * 判断父文件是否存在
     */
    private void checkParentDir() {
        File dir = new File(Constant.PHOTO_PATH_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * 判断文件夹是否存在
     */
    private void checkJpegDir() {
        File dir = new File(Constant.PHOTO_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }


    /**
     * 创建jpeg的文件
     *
     * @return
     */
    private File createJpeg() {
        long time = System.currentTimeMillis();
        int random = new Random().nextInt(1000);
        File dir = new File(Constant.PHOTO_PATH);

        int robotId =Integer.parseInt(Globle.robotId) ;

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");

        String finalName = robotId + "_" + format.format(time) + "_" + random + ".jpg"; //命名规则
        Log.i("JpegSaver", finalName);
        return new File(dir, finalName);
    }


    /**
     * 保存
     *
     * @param bytes
     * @param file
     * @throws java.io.IOException
     */
    private void save(byte[] bytes, File file) throws IOException {
        Log.i("JpegSaver", "save");
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            os.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public interface ImageSaverCallback {
        void onComplete(String path);
    }
}
