package com.chance.mimorobot.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chance.mimorobot.R;
import com.chance.mimorobot.widget.ColorPickerView;
import com.chance.mimorobot.widget.CoverFlowAdapter;
import com.chance.mimorobot.widget.CoverFlowViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BusinessActivity extends TitleBarActivity {
    @BindView(R.id.vp_homepage)
    CoverFlowViewPager viewPager;
    @BindView(R.id.iv_homepage_left_button)
    ImageView ivHomepageLeftButton;
    @BindView(R.id.iv_homepage_right_button)
    ImageView ivHomepageRightButton;

    private int mCurrentItemPosition = 1;
    private int[] homepageIcons = new int[]{R.drawable.business_1, R.drawable.business_2,
            R.drawable.business_3,R.drawable.business_4,R.drawable.business_5};

    private int mDownX, mDownY;

    @Override
    int getContentLayoutId() {
        return R.layout.activity_business;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


        // 初始化数据
        List<View> list = new ArrayList<>();
        for (int i = 0; i < homepageIcons.length; i++) {
            ImageView img = new ImageView(this);
            img.setImageResource(homepageIcons[i]);
            list.add(img);
        }
        //设置显示的数据
        viewPager.setViewList(list);

        // 设置滑动的监听，该监听为当前页面滑动到中央时的索引
        viewPager.setOnPageSelectListener(new CoverFlowAdapter.OnPageSelectListener() {
            @Override
            public void select(int position) {
                Log.i("currentPosition-select", "" + mCurrentItemPosition);
                mCurrentItemPosition = position;
            }
        });
        viewPager.setCurrentItem(1);

        //左中右三个item的范围
        final Rect leftRect = new Rect(120, 240, 340, 450);
        final Rect middleRect = new Rect(480, 160, 790, 480);
        final Rect rightRect = new Rect(940, 220, 1180, 470);
        viewPager.getViewPager().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i("position down", "x=" + event.getRawX() + ",y=" + event.getRawY());
                        mDownX = (int) event.getRawX();
                        mDownY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();
                        Log.i("position up", "x=" + event.getRawX() + ",y=" + event.getRawY());

                        if (Math.abs(x - mDownX) < 20 && Math.abs(y - mDownY) < 20) {
                            //默认为点击事件
                            if (middleRect.contains(x, y)) {
                                //click middle item
                                Log.i("click", "position=" + (mCurrentItemPosition));
                                clickItemView(mCurrentItemPosition);
                            }

                            if (mCurrentItemPosition > 0) {
                                if (leftRect.contains(x, y)) {
                                    //click left item
                                    Log.i("click", "position=" + (mCurrentItemPosition - 1));
                                    clickItemView(mCurrentItemPosition - 1);
                                }
                            }
                            if (mCurrentItemPosition < homepageIcons.length - 1) {
                                if (rightRect.contains(x, y)) {
                                    //click right item
                                    Log.i("click", "position=" + (mCurrentItemPosition + 1));
                                    clickItemView(mCurrentItemPosition + 1);
                                }
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }


    private void clickItemView(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(BusinessActivity.this,WebActivity.class));
                break;
            case 1:
                //验证版本以防跳出

                break;
            case 2:
//                navigator.navigateToAlbum(this);
                break;
            case 3:
//                navigator.navigateToClock(this);
                break;
        }
    }

    @OnClick({R.id.iv_homepage_left_button, R.id.iv_homepage_right_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_homepage_left_button:
                Log.i("currentPosition-left", "" + mCurrentItemPosition);
                if (mCurrentItemPosition > 0) {
                    viewPager.setCurrentItem(mCurrentItemPosition - 1);
                }
                break;
            case R.id.iv_homepage_right_button:
                Log.i("currentPosition-right", "" + mCurrentItemPosition);
                if (mCurrentItemPosition < homepageIcons.length - 1) {
                    viewPager.setCurrentItem(mCurrentItemPosition + 1);
                }
                break;
        }
    }
}
