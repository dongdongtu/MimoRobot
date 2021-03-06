package com.chance.mimorobot.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chance.mimorobot.R;

import java.io.File;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

public class ImageActivity extends TitleBarActivity {
    @BindView(R.id.image)
    PhotoView imgIv;

    @Override
    int getContentLayoutId() {
        return R.layout.activity_image;
    }


    public static Intent getIntent(Context context, String json) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("data", json);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentWithUIUpdate();
    }


    public void setContentWithUIUpdate() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("data"))) {
            String content = getIntent().getStringExtra("data");
            //下载图片保存到本地
            Glide.with(this)
                    .load(content).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File file, GlideAnimation<? super File> glideAnimation) {
                    imgIv.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
            });
        }
    }
}
