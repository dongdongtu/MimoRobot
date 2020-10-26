package com.chance.mimorobot.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.BaseActivity;
import com.chance.mimorobot.R;
import com.chance.mimorobot.model.CityWeatherModel;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class WeatherActivity extends TitleBarActivity {
    private CityWeatherModel mWeatherModel;

    @BindView(R.id.iv_weather_weather)
    ImageView ivWeather;
    @BindView(R.id.tv_weather_real_temperature)
    TextView tvRealTemperature;
    @BindView(R.id.tv_weather_city)
    TextView tvCity;
    @BindView(R.id.tv_weather_temperature_range)
    TextView tvTemperatureRange;
    @BindView(R.id.tv_weather_wind)
    TextView tvWind;
    @BindView(R.id.tv_weather_weather_condition)
    TextView tvWeatherCondition;
    @BindView(R.id.tv_weather_air_condition)
    TextView tvAirCondition;

    public static Intent getIntent(Context context, String json) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra("data", json);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("天气");
        String weatherData = getIntent().getStringExtra("data");
        if (!StringUtils.isEmpty(weatherData)) {
            mWeatherModel = new Gson().fromJson(weatherData, CityWeatherModel.class);
            showWeather();
        } else {
            finish();
        }
    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_weather;
    }

    @SuppressLint("CheckResult")
    private void showWeather() {
        String temperature;
        if (StringUtils.isEmpty(mWeatherModel.getTempReal())) {
            temperature = mWeatherModel.getTempLow().substring(0, mWeatherModel.getTempLow().length() - 1);
        } else {
            temperature = mWeatherModel.getTempReal().substring(0, mWeatherModel.getTempReal().length() - 1);
        }
        tvRealTemperature.setText(temperature);
        tvCity.setText(mWeatherModel.getCity().substring(0, mWeatherModel.getCity().length() - 1));
        tvTemperatureRange.setText(mWeatherModel.getTempRange());
        tvWind.setText(mWeatherModel.getWind());
        tvWeatherCondition.setText(mWeatherModel.getWeather());
        tvAirCondition.setText(String.valueOf("空气质量 " + mWeatherModel.getAirQuality()));

        String weather = mWeatherModel.getWeather();
        if (weather.contains("多云转晴")) {
            setBackgroundRes(R.drawable.background_weather_sunny);
            ivWeather.setImageResource(R.drawable.icon_weather_clouds_sun);
        } else if (weather.contains("晴转多云")) {
            setBackgroundRes(R.drawable.background_weather_cloudy);
            ivWeather.setImageResource(R.drawable.icon_weather_clouds_sun);
        } else if (weather.contains("阴转晴")) {
            setBackgroundRes(R.drawable.background_weather_sunny);
            ivWeather.setImageResource(R.drawable.icon_weather_cloudy_sun);
        } else if (weather.contains("雨转晴")) {
            setBackgroundRes(R.drawable.background_weather_sunny);
            ivWeather.setImageResource(R.drawable.icon_weather_rainy_sun);
        } else if (weather.contains("冰雹")) {
            setBackgroundRes(R.drawable.background_weather_hail);
            ivWeather.setImageResource(R.drawable.icon_weather_hail);
        } else if (weather.contains("晴")) {
            setBackgroundRes(R.drawable.background_weather_sunny);
            ivWeather.setImageResource(R.drawable.icon_weather_sunny);
        } else if (weather.contains("多云")) {
            setBackgroundRes(R.drawable.background_weather_cloudy);
            ivWeather.setImageResource(R.drawable.icon_weather_clouds);
        } else if (weather.contains("阴")) {
            setBackgroundRes(R.drawable.background_weather_cloudy_dark);
            ivWeather.setImageResource(R.drawable.icon_weather_cloudy);
        } else if (weather.contains("雷电")) {
            setBackgroundRes(R.drawable.background_weather_thunder);
            ivWeather.setImageResource(R.drawable.icon_weather_thunder);
        } else if (weather.contains("雷阵雨")) {
            setBackgroundRes(R.drawable.background_weather_thunder);
            ivWeather.setImageResource(R.drawable.icon_weather_thunder_rainy);
        } else if (weather.contains("雨夹雪")) {
            ivWeather.setImageResource(R.drawable.icon_weather_rainy_snowy);
            setBackgroundRes(R.drawable.background_weather_rainy);
        } else if (weather.contains("雨")) {
            setBackgroundRes(R.drawable.background_weather_rainy);
            ivWeather.setImageResource(R.drawable.icon_weather_rainy);
        } else if (weather.contains("雪")) {
            setBackgroundRes(R.drawable.background_weather_snowy);
            ivWeather.setImageResource(R.drawable.icon_weather_snowy);
        } else if (weather.contains("雾")) {
            setBackgroundRes(R.drawable.background_weather_fog);
            ivWeather.setImageResource(R.drawable.icon_weather_fog);
        } else if (weather.contains("霾")) {
            ivWeather.setImageResource(R.drawable.icon_weather_haze);
            setBackgroundRes(R.drawable.background_weather_haze);
        } else if (weather.contains("沙尘")) {
            ivWeather.setImageResource(R.drawable.icon_weahter_sand);
            setBackgroundRes(R.drawable.background_weather_sand);
        } else {
            //default 无法处理的天气状况
            setBackgroundRes(R.drawable.background_weather_sunny);
            ivWeather.setImageResource(R.drawable.icon_weather_clouds_sun);
        }

        Flowable.timer(15, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        finish();
                    }
                });
    }

}
