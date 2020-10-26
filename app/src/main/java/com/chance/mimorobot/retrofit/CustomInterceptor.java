package com.chance.mimorobot.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 拦截器
 * Created by Administrator on 2017/4/25 0025.
 */

public class CustomInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("Content-Type", "application/json")
                .build();
        return chain.proceed(request);
    }
}
