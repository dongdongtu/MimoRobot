package com.chance.mimorobot.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.chance.mimorobot.BaseActivity;
import com.chance.mimorobot.MyApplication;
import com.chance.mimorobot.R;
import com.chance.mimorobot.manager.SlamManager;
import com.chance.mimorobot.utils.NetworkHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class WelcomeActivity extends BaseActivity {

    private boolean isFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        requestPermissions();
        ((MyApplication)getApplication()).initDir();
    }

    @Override
    public void onResume() {
        super.onResume();
            Flowable.timer(45, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {

                    if (NetworkHelper.ping()) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(WelcomeActivity.this, WifiActivity.class));
                    }
                }
            });
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(WelcomeActivity.this);
        //请求权限全部结果
        rxPermission.request(
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            Toasty.warning(WelcomeActivity.this, "App未能获取全部需要的相关权限，部分功能可能不能正常使用.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
