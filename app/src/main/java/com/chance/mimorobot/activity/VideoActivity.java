package com.chance.mimorobot.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.widget.neteasedisc.model.MusicData;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends TitleBarActivity {
    private String TAG = VideoActivity.class.getSimpleName();


    @BindView(R.id.video_player)
    StandardGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;

    private static OnFinishVideoListener onFinishVideoListener;
    public static Intent getIntent(Context context, String json) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("data", json);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        init();
    }


    //
    public interface OnFinishVideoListener {
        void isFinish(boolean b);
    }


    public static void setOnFinishListener(OnFinishVideoListener listener) {
        onFinishVideoListener = listener;
    }


    @Override
    int getContentLayoutId() {
        return R.layout.activity_video;
    }

    private void init() {
        videoPlayer =  (StandardGSYVideoPlayer)findViewById(R.id.video_player);
        String source1 = "http://www.qijie.mimm.co/files/video/1.mp4";
        if (!TextUtils.isEmpty(getIntent().getStringExtra("data"))){
            source1=getIntent().getStringExtra("data");
        }
        videoPlayer.setUp(source1, true, "");

//        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.mipmap.xxx1);
//        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.INVISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.INVISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.startWindowFullscreen(VideoActivity.this, false, true);
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        videoPlayer.getBackButton().setVisibility(View.INVISIBLE);
        videoPlayer.startPlayLogic();
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }

}