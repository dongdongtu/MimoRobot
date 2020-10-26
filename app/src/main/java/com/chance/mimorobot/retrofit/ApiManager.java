package com.chance.mimorobot.retrofit;

import com.chance.mimorobot.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private RobotServer robotServer;
    public static final String ROBOT_SERVER_URL = "http://47.110.149.187:10031/";//正式服务器
    public static final String ROBOT_SERVER_URL1 = "http://47.110.149.187:10031";//正式服务器

    public static final String ROBOT_SERVER_URL_TEST = "http://192.168.31.244:12026/";//测试服务器

    private static ApiManager manager;
    private OkHttpClient mClient;
    public static ApiManager getInstance() {
        if (manager == null) {
            manager = new ApiManager();
        }
        return manager;
    }

    private ApiManager() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mClient = new OkHttpClient.Builder()
//                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(loggingInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//设置写入超时时间
                .build();
    }


    public RobotServer getRobotServer() {
        if (robotServer == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(mClient)
                    .baseUrl(ROBOT_SERVER_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            robotServer = retrofit.create(RobotServer.class);
        }
        return robotServer;
    }
}
