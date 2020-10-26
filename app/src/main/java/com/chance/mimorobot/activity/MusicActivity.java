package com.chance.mimorobot.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chance.mimorobot.R;
import com.chance.mimorobot.helper.RobotFunctionSettings;
import com.chance.mimorobot.widget.BackgourndAnimationRelativeLayout;
import com.chance.mimorobot.widget.neteasedisc.model.MusicData;
import com.chance.mimorobot.widget.neteasedisc.service.MusicService;
import com.chance.mimorobot.widget.neteasedisc.utils.DisplayUtil;
import com.chance.mimorobot.widget.neteasedisc.utils.FastBlurUtil;
import com.chance.mimorobot.widget.neteasedisc.widget.DiscView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

import static com.chance.mimorobot.widget.neteasedisc.service.MusicService.PARAM_MUSIC_LIST;
import static com.chance.mimorobot.widget.neteasedisc.widget.DiscView.DURATION_NEEDLE_ANIAMTOR;

public class MusicActivity extends TitleBarActivity implements DiscView.IPlayInfo, View.OnClickListener {
    @BindView(R.id.iv_volumedown)
    ImageView ivVolumedown;
    @BindView(R.id.iv_volumeup)
    ImageView ivVolumeup;
    private String TAG = MusicActivity.class.getSimpleName();

    @BindView(R.id.discview)
    DiscView mDisc;
    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    @BindView(R.id.ivDiscBlackgound)
    ImageView ivDiscBlackgound;
    @BindView(R.id.vpDiscContain)
    ViewPager vpDiscContain;
    @BindView(R.id.ivNeedle)
    ImageView ivNeedle;
    @BindView(R.id.tvCurrentTime)
    TextView mTvMusicDuration;
    @BindView(R.id.musicSeekBar)
    SeekBar mSeekBar;
    @BindView(R.id.tvTotalTime)
    TextView mTvTotalMusicDuration;
    @BindView(R.id.rlMusicTime)
    RelativeLayout rlMusicTime;
    @BindView(R.id.ivLast)
    ImageView mIvLast;
    @BindView(R.id.ivPlayOrPause)
    ImageView mIvPlayOrPause;
    @BindView(R.id.ivNext)
    ImageView mIvNext;
    @BindView(R.id.llPlayOption)
    LinearLayout llPlayOption;
    @BindView(R.id.rootLayout)
    BackgourndAnimationRelativeLayout mRootLayout;


    public static final int MUSIC_MESSAGE = 0;

    private Handler mMusicHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSeekBar.setProgress(mSeekBar.getProgress() + 1000);
            mTvMusicDuration.setText(duration2Time(mSeekBar.getProgress()));
            startUpdateSeekBarProgress();
        }
    };

    private Runnable runnable;

    private MusicReceiver mMusicReceiver = new MusicReceiver();
    private List<MusicData> mMusicDatas = new ArrayList<>();

    @Override
    int getContentLayoutId() {
        return R.layout.activity_music;
    }


    public static Intent getIntent(Context context, String json) {
        Intent intent = new Intent(context, MusicActivity.class);
        intent.putExtra("data", json);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        if (!TextUtils.isEmpty(getIntent().getStringExtra("data"))){
            mMusicDatas.add(new MusicData(getIntent().getStringExtra("data"), "https://bkimg.cdn.bcebos.com/pic/63d9f2d3572c11dfb2ab8904692762d0f703c29a?x-bce-process=image/resize,m_lfit,w_268,limit_1/format,f_jpg", "才华有限公司", "金玟岐"));

        }else{
            mMusicDatas.add(new MusicData("http://api.qijie.mimm.co/files/music/才华有限公司.mp3", "https://bkimg.cdn.bcebos.com/pic/63d9f2d3572c11dfb2ab8904692762d0f703c29a?x-bce-process=image/resize,m_lfit,w_268,limit_1/format,f_jpg", "才华有限公司", "金玟岐"));

        }

            Intent intent = new Intent(MusicActivity.this, MusicService.class);
        intent.putExtra(PARAM_MUSIC_LIST, (Serializable) mMusicDatas);
        startService(intent);
        initMusicReceiver();

        mTvMusicDuration.setText(duration2Time(0));
        mTvTotalMusicDuration.setText(duration2Time(0));
        mDisc.setMusicDataList(mMusicDatas);
        mDisc.playOrPause();
        mDisc.setPlayInfoListener(this);
        mIvLast.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
        mIvPlayOrPause.setOnClickListener(this);
        ivVolumedown.setOnClickListener(this);
        ivVolumeup.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvMusicDuration.setText(duration2Time(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopUpdateSeekBarProgree();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(seekBar.getProgress());
                startUpdateSeekBarProgress();
            }
        });
    }

    /*根据时长格式化称时间文本*/
    private String duration2Time(int duration) {
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        return (min < 10 ? "0" + min : min + "") + ":" + (sec < 10 ? "0" + sec : sec + "");
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_title_bar_back) {
            finish();
        } else if (view == mIvPlayOrPause) {
            mDisc.playOrPause();
        } else if (view == mIvNext) {
            mDisc.next();
        } else if (view == mIvLast) {
            mDisc.last();
        } else if (view ==ivVolumeup){
            RobotFunctionSettings.getInstance().volumeUp();
        }else if (view ==ivVolumedown){
            RobotFunctionSettings.getInstance().volumeDown();
        }
    }

    @Override
    public void onMusicInfoChanged(String musicName, String musicAuthor) {
        mToolbar.setTitle(musicName);
        mToolbar.setSubtitle(musicAuthor);
    }

    @Override
    public void onMusicPicChanged(String musicPicRes) {
        try2UpdateMusicPicBackground(musicPicRes);
    }

    @Override
    public void onMusicChanged(DiscView.MusicChangedStatus musicChangedStatus) {
        switch (musicChangedStatus) {
            case PLAY: {
                play();
                break;
            }
            case PAUSE: {
                pause();
                break;
            }
            case NEXT: {
                next();
                break;
            }
            case LAST: {
                last();
                break;
            }
            case STOP: {
                stop();
                break;
            }
        }
    }


    private void play() {
        optMusic(MusicService.ACTION_OPT_MUSIC_PLAY);
        startUpdateSeekBarProgress();
    }

    private void pause() {
        optMusic(MusicService.ACTION_OPT_MUSIC_PAUSE);
        stopUpdateSeekBarProgree();
    }

    private void stop() {
        stopUpdateSeekBarProgree();
        mIvPlayOrPause.setImageResource(R.drawable.ic_plays);
        mTvMusicDuration.setText(duration2Time(0));
        mTvTotalMusicDuration.setText(duration2Time(0));
        mSeekBar.setProgress(0);
    }

    private void next() {
        mRootLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                optMusic(MusicService.ACTION_OPT_MUSIC_NEXT);
            }
        }, DURATION_NEEDLE_ANIAMTOR);
        stopUpdateSeekBarProgree();
        mTvMusicDuration.setText(duration2Time(0));
        mTvTotalMusicDuration.setText(duration2Time(0));
    }

    private void last() {
        mRootLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                optMusic(MusicService.ACTION_OPT_MUSIC_LAST);
            }
        }, DURATION_NEEDLE_ANIAMTOR);
        stopUpdateSeekBarProgree();
        mTvMusicDuration.setText(duration2Time(0));
        mTvTotalMusicDuration.setText(duration2Time(0));
    }

    private void optMusic(final String action) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(action));
    }

    private void seekTo(int position) {
        Intent intent = new Intent(MusicService.ACTION_OPT_MUSIC_SEEK_TO);
        intent.putExtra(MusicService.PARAM_MUSIC_SEEK_TO, position);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void try2UpdateMusicPicBackground(final String musicPicRes) {
        if (mRootLayout.isNeed2UpdateBackground(musicPicRes)) {

            Glide.with(this) // could be an issue!
                    .load(musicPicRes)
                    .asBitmap()   //强制转换Bitmap
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    final Drawable foregroundDrawable = getForegroundDrawable(resource);
                                    if (foregroundDrawable == null) return;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mRootLayout.setForeground(foregroundDrawable);
                                            mRootLayout.beginAnimation();
                                        }
                                    });
                                }
                            }).start();
                        }
                    });
        }
    }

    private Drawable getForegroundDrawable(Bitmap musicPicRes) {
        /*得到屏幕的宽高比，以便按比例切割图片一部分*/
        final float widthHeightSize = (float) (DisplayUtil.getScreenWidth(this)
                * 1.0 / DisplayUtil.getScreenHeight(this) * 1.0);

        Bitmap bitmap = musicPicRes;
        int cropBitmapWidth = (int) (widthHeightSize * bitmap.getHeight());
        int cropBitmapWidthX = (int) ((bitmap.getWidth() - cropBitmapWidth) / 2.0);

        if (cropBitmapWidth <= 0 || cropBitmapWidthX <= 0) return null;
        /*切割部分图片*/
        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropBitmapWidthX, 0, cropBitmapWidth,
                bitmap.getHeight());
        /*缩小图片*/
        Bitmap scaleBitmap = Bitmap.createScaledBitmap(cropBitmap, musicPicRes.getWidth() / 50, musicPicRes
                .getHeight() / 50, false);
        /*模糊化*/
        final Bitmap blurBitmap = FastBlurUtil.doBlur(scaleBitmap, 8, true);

        final Drawable foregroundDrawable = new BitmapDrawable(blurBitmap);
        /*加入灰色遮罩层，避免图片过亮影响其他控件*/
        foregroundDrawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        return foregroundDrawable;
    }

    class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicService.ACTION_STATUS_MUSIC_PLAY)) {
                mIvPlayOrPause.setImageResource(R.drawable.ic_pauses);
                int currentPosition = intent.getIntExtra(MusicService.PARAM_MUSIC_CURRENT_POSITION, 0);
                mSeekBar.setProgress(currentPosition);
                if (!mDisc.isPlaying()) {
                    mDisc.playOrPause();
                }
            } else if (action.equals(MusicService.ACTION_STATUS_MUSIC_PAUSE)) {
                mIvPlayOrPause.setImageResource(R.drawable.ic_plays);
                if (mDisc.isPlaying()) {
                    mDisc.playOrPause();
                }
            } else if (action.equals(MusicService.ACTION_STATUS_MUSIC_DURATION)) {
                int duration = intent.getIntExtra(MusicService.PARAM_MUSIC_DURATION, 0);
                updateMusicDurationInfo(duration);
            } else if (action.equals(MusicService.ACTION_STATUS_MUSIC_COMPLETE)) {
                boolean isOver = intent.getBooleanExtra(MusicService.PARAM_MUSIC_IS_OVER, true);
                complete(isOver);
            }
        }
    }

    private void startUpdateSeekBarProgress() {
        /*避免重复发送Message*/
        stopUpdateSeekBarProgree();
        mMusicHandler.sendEmptyMessageDelayed(0, 1000);
    }

    private void stopUpdateSeekBarProgree() {
        mMusicHandler.removeMessages(MUSIC_MESSAGE);
    }

    private void updateMusicDurationInfo(int totalDuration) {
        mSeekBar.setProgress(0);
        mSeekBar.setMax(totalDuration);
        mTvTotalMusicDuration.setText(duration2Time(totalDuration));
        mTvMusicDuration.setText(duration2Time(0));
        startUpdateSeekBarProgress();
    }

    private void complete(boolean isOver) {
        if (isOver) {
            if (runnable != null) runnable.run();
            mDisc.playOrPause();
        } else {
            mDisc.next();
        }
    }


    private void initMusicReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_PLAY);
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_PAUSE);
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_DURATION);
        intentFilter.addAction(MusicService.ACTION_STATUS_MUSIC_COMPLETE);
        /*注册本地广播*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMusicReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra(PARAM_MUSIC_LIST, (Serializable) mMusicDatas);
        stopService(intent);
        mMusicHandler.removeCallbacksAndMessages(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMusicReceiver);
    }

}
