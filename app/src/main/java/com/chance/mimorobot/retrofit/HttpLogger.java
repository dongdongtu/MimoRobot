package com.chance.mimorobot.retrofit;

import android.util.Log;

import com.chance.mimorobot.BuildConfig;

import okhttp3.logging.HttpLoggingInterceptor.Logger;

/**
 * @Author
 * @Date
 * @Desc
 * @Url
 */

public class HttpLogger implements Logger {
    @Override
    public void log(String s) {
//        if (BuildConfig.LOG_DEBUG)
            Log.i("Httplog", s);
    }
}
