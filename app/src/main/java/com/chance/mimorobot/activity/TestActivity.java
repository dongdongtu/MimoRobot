package com.chance.mimorobot.activity;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        headhor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (SerialControlManager.newInstance().getHeadghor() > i) {
                    SerialControlManager.newInstance().headTurnRight(SerialControlManager.newInstance().getHeadghor() - i);
                } else if (SerialControlManager.newInstance().getHeadghor() < i) {
                    SerialControlManager.newInstance().headTurnLeft(i - SerialControlManager.newInstance().getHeadghor());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        headver.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (SerialControlManager.newInstance().getHeadver() > i) {
                    SerialControlManager.newInstance().headTurnDown(SerialControlManager.newInstance().getHeadver() - i);
                } else if (SerialControlManager.newInstance().getHeadver() < i) {
                    SerialControlManager.newInstance().headTurnUp(i - SerialControlManager.newInstance().getHeadver());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        lefthand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (SerialControlManager.newInstance().getLefthand() > i) {
                    SerialControlManager.newInstance().armLeftTurnDown(SerialControlManager.newInstance().getLefthand() - i);
                } else if (SerialControlManager.newInstance().getLefthand() < i) {
                    SerialControlManager.newInstance().armLeftTurnUp(i - SerialControlManager.newInstance().getLefthand());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        righthand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (SerialControlManager.newInstance().getRighthand() > i) {
                    SerialControlManager.newInstance().armRightTurnDown(SerialControlManager.newInstance().getRighthand() - i);
                } else if (SerialControlManager.newInstance().getRighthand() < i) {
                    SerialControlManager.newInstance().armRightTurnUp(i - SerialControlManager.newInstance().getRighthand());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rightXiaHand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (SerialControlManager.newInstance().getRightxiahand() > i) {
                    SerialControlManager.newInstance().armRightXiaTurnDown(SerialControlManager.newInstance().getRightxiahand() - i);
                } else if (SerialControlManager.newInstance().getRightxiahand() < i) {
                    SerialControlManager.newInstance().armRightXiaTurnUp(i - SerialControlManager.newInstance().getRightxiahand());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        leftXiaHand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (SerialControlManager.newInstance().getLeftxiahand() > i) {
                    SerialControlManager.newInstance().armLeftXiaTurnDown(SerialControlManager.newInstance().getLeftxiahand() - i);
                } else if (SerialControlManager.newInstance().getLeftxiahand() < i) {
                    SerialControlManager.newInstance().armLeftXiaTurnUp(i - SerialControlManager.newInstance().getLeftxiahand());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
    public void startMove(){

    }
}
