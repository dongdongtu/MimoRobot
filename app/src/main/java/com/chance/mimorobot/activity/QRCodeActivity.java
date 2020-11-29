package com.chance.mimorobot.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.utils.EncodingUtils;

import butterknife.BindView;


public class QRCodeActivity extends TitleBarActivity {
    @BindView(R.id.iv_code)
    ImageView ivCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_qrcode;
    }


    @Override
    public void onResume() {
        super.onResume();
        ivCode.setImageBitmap(getQRCodeBitmap(Globle.robotId));
    }


    private Bitmap getQRCodeBitmap(String content) {
        //修复内容太短，二维码矩阵过稀不好识别的问题
        return EncodingUtils.createQRCode(content, 500, 500,
                BitmapFactory.decodeResource(getResources(), R.drawable.applogo));
    }
}
