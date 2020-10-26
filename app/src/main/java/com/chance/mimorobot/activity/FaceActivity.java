package com.chance.mimorobot.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.manager.SerialControlManager;

import butterknife.BindView;
import butterknife.OnClick;

public class FaceActivity extends TitleBarActivity {
    private String TAG = FaceActivity.class.getSimpleName();
    @BindView(R.id.iv_weixiao)
    ImageView ivWeixiao;
    @BindView(R.id.iv_kaixin)
    ImageView ivKaixin;
    @BindView(R.id.iv_tiaopi)
    ImageView ivTiaopi;
    @BindView(R.id.iv_gandong)
    ImageView ivGandong;
    @BindView(R.id.iv_xihuan)
    ImageView ivXihuan;
    @BindView(R.id.iv_haixiu)
    ImageView ivHaixiu;
    @BindView(R.id.iv_yun)
    ImageView ivYun;
    @BindView(R.id.iv_ganga)
    ImageView ivGanga;
    @BindView(R.id.iv_weiqu)
    ImageView ivWeiqu;
    @BindView(R.id.iv_daku)
    ImageView ivDaku;
    @BindView(R.id.iv_shengqi)
    ImageView ivShengqi;
    @BindView(R.id.iv_xiuxi)
    ImageView ivXiuxi;


    private SerialControlManager serialControlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serialControlManager=SerialControlManager.newInstance();
    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_face;
    }

    @OnClick({R.id.iv_weixiao, R.id.iv_kaixin, R.id.iv_tiaopi, R.id.iv_gandong, R.id.iv_xihuan, R.id.iv_haixiu, R.id.iv_yun, R.id.iv_ganga, R.id.iv_weiqu, R.id.iv_daku, R.id.iv_shengqi, R.id.iv_xiuxi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_weixiao:
                serialControlManager.setFace(8);
                break;
            case R.id.iv_kaixin:
                serialControlManager.setFace(5);
                break;
            case R.id.iv_tiaopi:
                serialControlManager.setFace(7);
                break;
            case R.id.iv_gandong:
                serialControlManager.setFace(3);
                break;
            case R.id.iv_xihuan:
                serialControlManager.setFace(10);
                break;
            case R.id.iv_haixiu:
                serialControlManager.setFace(4);
                break;
            case R.id.iv_yun:
                serialControlManager.setFace(12);
                break;
            case R.id.iv_ganga:
                serialControlManager.setFace(2);
                break;
            case R.id.iv_weiqu:
                serialControlManager.setFace(9);
                break;
            case R.id.iv_daku:
                serialControlManager.setFace(1);
                break;
            case R.id.iv_shengqi:
                serialControlManager.setFace(6);
                break;
            case R.id.iv_xiuxi:
                serialControlManager.setFace(11);
                break;
        }
    }
}
