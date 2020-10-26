package com.chance.mimorobot.helper.camera2;

import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Size;

import com.blankj.utilcode.util.ActivityUtils;
import com.chance.mimorobot.widget.AutoFitTextureView;

import java.util.Arrays;
import java.util.Collections;

import androidx.annotation.RequiresApi;

/**
 * Created by Administrator on 2018/5/19.
 */

public class CameraOutputs {
    private static final int MAX_PREVIEW_WIDTH = 1920;//Max preview width that is guaranteed by Camera2 API
    private static final int MAX_PREVIEW_HEIGHT = 1080;//Max preview height that is guaranteed by Camera2 API

    private static ImageReader imageReader;
    private static int sensorOrientation;
    private static Size previewSize;
    private static Size videoSize;
    private static String cameraId;
    private static MediaRecorder mediaRecorder;
    private static boolean bFlashSupported;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void setUpCameraOutputs(int width, int height, CameraManager manager, AutoFitTextureView textureView) {
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                /*
                一般来说当你的Android智能设备有前后摄像头的话，那么后置摄像头的id为0 前置的为1
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                //前置摄像头
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }else if(facing != null && facing == CameraCharacteristics.LENS_FACING_BACK){
                    continue;
                }*/
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
                imageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG, /*maxImages*/2);//初始化ImageReader
                //处理图片方向相关
                sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                //注：使用过程中发现显示不佳，这里为了最好的预览效果，直接用可预览的最大尺寸！
                previewSize = CameraSizeOption.chooseMaxSize(MAX_PREVIEW_WIDTH, MAX_PREVIEW_HEIGHT, sensorOrientation);
                videoSize = CameraSizeOption.chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
                mediaRecorder = new MediaRecorder();
                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = ActivityUtils.getTopActivity().getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
                } else {
                    textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
                }
                // 设置是否支持闪光灯
                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                bFlashSupported = available == null ? false : available;
                CameraOutputs.cameraId = cameraId;
                return;
            }
        } catch (CameraAccessException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static ImageReader getImageReader() {
        return imageReader;
    }


    public static int getSensorOrientation() {
        return sensorOrientation;
    }


    public static Size getPreviewSize() {
        return previewSize;
    }


    public static Size getVideoSize() {
        return videoSize;
    }


    public static String getCameraId() {
        return cameraId;
    }


    public static MediaRecorder getMediaRecorder() {
        return mediaRecorder;
    }


    public static boolean isbFlashSupported() {
        return bFlashSupported;
    }


}
