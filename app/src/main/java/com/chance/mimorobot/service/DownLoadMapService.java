package com.chance.mimorobot.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.utils.FileUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.chance.mimorobot.constant.Constant.ACTION_DOWNLOAD_CODE;
import static com.chance.mimorobot.constant.Constant.ACTION_DOWNLOAD_EXTRA;

import static com.chance.mimorobot.constant.Globle.DOWNLOAD_PATH;
import static com.chance.mimorobot.constant.Globle.MAP_PATH;
import static com.chance.mimorobot.retrofit.ApiManager.ROBOT_SERVER_URL;
import static com.chance.mimorobot.retrofit.ApiManager.ROBOT_SERVER_URL1;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DownLoadMapService extends IntentService {
    private final String TAG = DownLoadMapService.class.getSimpleName();
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "sevice.action.FOO";
    private static final String ACTION_BAZ = "sevice.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "sevice.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "sevice.extra.PARAM2";


    private String url;


    public DownLoadMapService() {
        super("DownLoadMapService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1) {
        Intent intent = new Intent(context, DownLoadMapService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                url = ROBOT_SERVER_URL1 + intent.getStringExtra(EXTRA_PARAM1);
            }
            Log.e(TAG, "url=" + url);
            final String name = url.substring(url.lastIndexOf("/") + 1);
//            File  map=new File(DOWNLOAD_PATH+ File.separator+name);
            if (FileUtils.fileIsExists(DOWNLOAD_PATH + File.separator + name)) {
                String path = DOWNLOAD_PATH + File.separator + name;
                File f = new File(path);
                f.delete();
                Log.e(TAG, "Exists");
//                Intent intent12 = new Intent(ACTION_DOWNLOAD_CODE);
//                intent12.putExtra(ACTION_DOWNLOAD_EXTRA, path);
//                sendBroadcast(intent12);
            }
            ApiManager.getInstance().getRobotServer().downloadFileWithDynamicUrlSync(url)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResponseBody>() {
                @Override
                public void accept(ResponseBody responseBody) throws Exception {
                    Log.e(TAG, "responseBody");
                    String path = MAP_PATH + File.separator + name;
                    FileUtils.writeResponseBodyToDisk(responseBody, path);
                    Intent intent = new Intent(ACTION_DOWNLOAD_CODE);
                    intent.putExtra(ACTION_DOWNLOAD_EXTRA, path);
                    Flowable.timer(2, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            sendBroadcast(intent);
                        }
                    });

                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                }
            });
        }

    }


}
