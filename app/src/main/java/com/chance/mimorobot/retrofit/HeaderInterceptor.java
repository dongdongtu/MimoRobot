package com.chance.mimorobot.retrofit;



import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.utils.DigestUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * AIUI webApi请求拦截器
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }
//    private static final String APPID = "5ee4440b";
//    private static final String API_KEY = "5022b83357e83c74606d5635fbbcfc68";
//    private static final String DATA_TYPE = "text";
//    private static final String SCENE = "main";
//    private static final String AUTH_ID = "32fa5e3d3ff5fe52df67bab0728bdbd4";
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        /**
//         * 判断是否请求aiui 添加响应的头部信息
//         */
//        if (chain.request().url().toString().contains("openapi.xfyun.cn")) {
//            String curTime = String.valueOf(System.currentTimeMillis() / 1000L);
//            String paramBase64 = getParams();
//            String checkSum = DigestUtils.md5Encode(API_KEY + curTime + paramBase64);
//            Request request = chain.request().newBuilder()
//                    .addHeader("X-Param", paramBase64)
//                    .addHeader("X-CurTime", curTime)
//                    .addHeader("X-CheckSum", checkSum)
//                    .addHeader("X-Appid", APPID)
//                    .build();
//            return chain.proceed(request);
//        }
//        Request request = chain.request().newBuilder().build();
//        return chain.proceed(request);
//    }
//
//    private String getParams() {
//        String lat = "";
//        String lng = "";
//        if (Globle.location != null) {
//            lat = String.valueOf(Globle.location.getLatitude());
//            lng = String.valueOf(Globle.location.getLongitude());
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("auth_id", AUTH_ID);
//            jsonObject.put("data_type", DATA_TYPE);
//            jsonObject.put("scene", SCENE);
//            jsonObject.put("lat", lat);
//            jsonObject.put("lng", lng);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return new String(Base64.encodeBase64(jsonObject.toString().getBytes()));
//    }

}
