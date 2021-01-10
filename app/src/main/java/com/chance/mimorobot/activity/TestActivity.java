package com.chance.mimorobot.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.manager.SerialControlManager;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends TitleBarActivity {


    @BindView(R.id.lefthand)
    SeekBar lefthand;
    @BindView(R.id.righthand)
    SeekBar righthand;
    @BindView(R.id.left_xia_hand)
    SeekBar leftXiaHand;
    @BindView(R.id.right_xia_hand)
    SeekBar rightXiaHand;
    @BindView(R.id.start_move)
    TextView startMove;
    @BindView(R.id.stop_move)
    TextView stopMove;
    @BindView(R.id.headver)
    SeekBar headver;
    @BindView(R.id.headhor)
    SeekBar headhor;
    @BindView(R.id.tv_headver)
    TextView tvHeadver;
    @BindView(R.id.tv_headhor)
    TextView tvHeadhor;
    @BindView(R.id.tv_lefthand)
    TextView tvLefthand;
    @BindView(R.id.tv_righthand)
    TextView tvRighthand;
    @BindView(R.id.tv_left_xia_hand)
    TextView tvLeftXiaHand;
    @BindView(R.id.tv_right_xia_hand)
    TextView tvRightXiaHand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        headhor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvHeadhor.setText("头部左右:"+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("tag", seekBar.getProgress() + "   =ssss");
                if (SerialControlManager.newInstance().getHeadghor() > (seekBar.getProgress() - 20)) {
                    SerialControlManager.newInstance().headTurnRight(SerialControlManager.newInstance().getHeadghor() - (seekBar.getProgress() - 20));
                } else if (SerialControlManager.newInstance().getHeadghor() < (seekBar.getProgress() - 20)) {
                    SerialControlManager.newInstance().headTurnLeft((seekBar.getProgress() - 20) - SerialControlManager.newInstance().getHeadghor());
                }
            }
        });
        headver.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvHeadver.setText("头部上下:"+i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (SerialControlManager.newInstance().getHeadver() > (seekBar.getProgress() - 8)) {
                    SerialControlManager.newInstance().headTurnDown(SerialControlManager.newInstance().getHeadver() - (seekBar.getProgress() - 8));
                } else if (SerialControlManager.newInstance().getHeadver() < (seekBar.getProgress() -8)) {
                    SerialControlManager.newInstance().headTurnUp((seekBar.getProgress() -8) - SerialControlManager.newInstance().getHeadver());
                }
            }
        });
        lefthand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvLefthand.setText("左臂："+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (SerialControlManager.newInstance().getLefthand() > (seekBar.getProgress() - 50)) {
                    SerialControlManager.newInstance().armLeftTurnDown(SerialControlManager.newInstance().getLefthand() - (seekBar.getProgress() - 50));
                } else if (SerialControlManager.newInstance().getLefthand() < (seekBar.getProgress() - 50)) {
                    SerialControlManager.newInstance().armLeftTurnUp((seekBar.getProgress() - 50) - SerialControlManager.newInstance().getLefthand());
                }
            }
        });
        righthand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvRighthand.setText("右臂："+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (SerialControlManager.newInstance().getRighthand() >  (seekBar.getProgress() - 50)) {
                    SerialControlManager.newInstance().armRightTurnDown(SerialControlManager.newInstance().getRighthand() -  (seekBar.getProgress() - 50));
                } else if (SerialControlManager.newInstance().getRighthand() <  (seekBar.getProgress() - 50)) {
                    SerialControlManager.newInstance().armRightTurnUp( (seekBar.getProgress() - 50) - SerialControlManager.newInstance().getRighthand());
                }
            }
        });

        rightXiaHand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvRightXiaHand.setText("右下臂+"+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (SerialControlManager.newInstance().getRightxiahand() > seekBar.getProgress() ) {
                    SerialControlManager.newInstance().armRightXiaTurnDown(SerialControlManager.newInstance().getRightxiahand() - seekBar.getProgress() );
                } else if (SerialControlManager.newInstance().getRightxiahand() < seekBar.getProgress() ) {
                    SerialControlManager.newInstance().armRightXiaTurnUp(seekBar.getProgress()  - SerialControlManager.newInstance().getRightxiahand());
                }
            }
        });
        leftXiaHand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvLeftXiaHand.setText("左下臂+"+i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (SerialControlManager.newInstance().getLeftxiahand() > seekBar.getProgress()) {
                    SerialControlManager.newInstance().armLeftXiaTurnDown(SerialControlManager.newInstance().getLeftxiahand() -seekBar.getProgress());
                } else if (SerialControlManager.newInstance().getLeftxiahand() < seekBar.getProgress()) {
                    SerialControlManager.newInstance().armLeftXiaTurnUp(seekBar.getProgress()- SerialControlManager.newInstance().getLeftxiahand());
                }
            }
        });
    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_test;
    }

    @OnClick({R.id.start_move, R.id.stop_move})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_move:
                break;
            case R.id.stop_move:
                break;
        }
    }

    public void startMove() {

    }
}
