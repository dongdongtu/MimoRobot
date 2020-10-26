package com.chance.mimorobot.update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.model.AndroidUpdate;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.utils.SystemUtils;


import androidx.appcompat.app.AlertDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 已弃用
 * 版本更新管理类
 * Created by Mr_zhou on 2017/8/14.
 */

public class UpadateManager {
    public static void checkVersion(final Context ctx) {
        ApiManager.getInstance().getRobotServer().getNewAndroidVersion(Globle.robotId,1)
                .subscribeOn(Schedulers.io())               //在IO线程进行网络请求
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AndroidUpdate>() {
                    @Override
                    public void accept(AndroidUpdate updateReponse) throws Exception {
                        if (updateReponse.getCode() == 200) {
                            // 登录成功
                            if (updateReponse.getData().getVersionCode() > SystemUtils.getAppVersionCode(ctx)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                                builder.setTitle("发现新版本");
                                builder.setMessage(updateReponse.getData().getDes());
                                builder.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(ctx, UpdateService.class);
                                        intent.putExtra("url", updateReponse.getData().getApkUrl());
                                        ctx.startService(intent);

                                    }
                                });
                                builder.setNegativeButton("稍候再说", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.show();
                            }

                        } else {
//                            Toast.makeText(ctx, updateReponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
