package com.chance.mimorobot.helper;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * Created by Shao Shizhang on 2018/2/12.
 */

public class UpdateMediaHelper {

    public void broadcastFile(final Context activity, final File file, final boolean isNewPicture, final boolean isNewVideo) {
        if (file.isDirectory()) {
        } else {
            MediaScannerConnection.scanFile(activity, new String[]{file.getAbsolutePath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            announceUri(activity,uri, isNewPicture, isNewVideo);
//                            String action = activity.getIntent().getAction();
//                            if (MediaStore.ACTION_VIDEO_CAPTURE.equals(action)) {
//                                Intent output = new Intent();
//                                output.setData(uri);
//                                activity.setResult(Activity.RESULT_OK, output);
//                                activity.finish();
//                            }
                        }
                    }
            );
        }
    }

    private void announceUri(Context context, Uri uri, boolean is_new_picture, boolean is_new_video) {
        if (is_new_picture) {
            context.sendBroadcast(new Intent("android.hardware.action.NEW_PICTURE", uri));
            context.sendBroadcast(new Intent("com.android.camera.NEW_PICTURE", uri));
        } else if (is_new_video) {
            context.sendBroadcast(new Intent("android.hardware.action.NEW_VIDEO", uri));
        }
    }

}