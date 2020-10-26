package com.chance.mimorobot.helper.camera2;

import android.graphics.Point;
import android.os.Build;
import android.util.Size;
import android.view.Surface;

import com.blankj.utilcode.util.ActivityUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;

/**
 * Created by Administrator on 2018/5/19.
 */

public class CameraSizeOption {
    /**
     * 视频录制的VideoSize
     *
     * @param choices
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        return choices[choices.length - 1];
    }

    /**
     * 选择PreviewSize
     * @param maxPreviewSizeWidth
     * @param maxPreviewSizeHeight
     * @param sensorOrientation
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Size chooseMaxSize(int maxPreviewSizeWidth, int maxPreviewSizeHeight, int sensorOrientation){
        int displayRotation = ActivityUtils.getTopActivity().getWindowManager().getDefaultDisplay().getRotation();
        boolean swappedDimensions = false;
        switch (displayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    swappedDimensions = true;
                }
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    swappedDimensions = true;
                }
                break;
            default:
                break;
        }
        Point displaySize = new Point();
        ActivityUtils.getTopActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);
        int outputWidth = displaySize.x;
        int outputHeight = displaySize.y;
        if (swappedDimensions) {
            outputWidth = displaySize.y;
            outputHeight = displaySize.x;
        }

        if (outputWidth > maxPreviewSizeWidth) {
            outputWidth = maxPreviewSizeWidth;
        }

        if (outputHeight > maxPreviewSizeHeight) {
            outputHeight = maxPreviewSizeHeight;
        }

        // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
        // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
        // garbage capture data.
//                previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
//                        maxPreviewHeight, largest);

        //注：使用过程中发现显示不佳，这里为了最好的预览效果，直接用可预览的最大尺寸！
       return new Size(outputWidth, outputHeight);
    }
    /**
     * 为了避免太大的预览大小会超过相机总线的带宽限
     *
     * @param choices
     * @param textureViewWidth
     * @param textureViewHeight
     * @param maxWidth
     * @param maxHeight
     * @param aspectRatio
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight && option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth && option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }
}
