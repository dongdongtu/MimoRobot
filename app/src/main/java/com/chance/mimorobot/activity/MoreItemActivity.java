package com.chance.mimorobot.activity;

import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.model.MapPoint;
import com.chance.mimorobot.statemachine.robot.utils.aiui.custom.Video;

import butterknife.BindView;
import butterknife.OnClick;

public class MoreItemActivity extends TitleBarActivity {
    @BindView(R.id.expression)
    ImageView expression;
    @BindView(R.id.light)
    ImageView light;
    @BindView(R.id.media)
    ImageView media;
    @BindView(R.id.telphone)
    ImageView telphone;
    @BindView(R.id.music)
    ImageView music;
    @BindView(R.id.slam)
    ImageView slam;
    @BindView(R.id.settting)
    ImageView settting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_moreitem;
    }

    @OnClick({R.id.expression, R.id.light, R.id.media, R.id.telphone, R.id.music, R.id.slam, R.id.settting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.expression:
                startActivity(new Intent(MoreItemActivity.this, FaceActivity.class));
                break;
            case R.id.light:
                startActivity(new Intent(MoreItemActivity.this,PickupColorActivity.class));
                break;
            case R.id.media:
                startActivity(new Intent(MoreItemActivity.this, VideoActivity.class));
                break;
            case R.id.telphone:
                startActivity(new Intent(MoreItemActivity.this,IOTActivity.class));
                break;
            case R.id.music:
                startActivity(new Intent(MoreItemActivity.this, MusicActivity.class));
                break;
            case R.id.slam:
                startActivity(new Intent(MoreItemActivity.this, MapActivity.class));
                break;
            case R.id.settting:
                startActivity(new Intent(MoreItemActivity.this, SettingActivity.class));
                break;
        }
    }
}
