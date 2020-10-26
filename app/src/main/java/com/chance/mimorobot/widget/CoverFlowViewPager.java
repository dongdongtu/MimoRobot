package com.chance.mimorobot.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.chance.mimorobot.R;


import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

public class CoverFlowViewPager extends RelativeLayout {
    private CoverFlowAdapter mAdapter;
    private ViewPager mViewPager;
    private List<View> mViewList = new ArrayList<>();

    private CoverFlowAdapter.OnPageSelectListener listener;

    public CoverFlowViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_cover_flow_viewpager, this);
        mViewPager = (ViewPager) findViewById(R.id.vp_cover_flow);
        init();
    }

    private void init() {
        mAdapter = new CoverFlowAdapter(mViewList, getContext());

        mAdapter.setOnPageSelectListener(new CoverFlowAdapter.OnPageSelectListener() {
            @Override
            public void select(int position) {
                if (listener != null) listener.select(position);
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mAdapter);
        mViewPager.setOffscreenPageLimit(5);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 传递给ViewPager 进行滑动处理
                return mViewPager.dispatchTouchEvent(event);
            }
        });

    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
    }

    public void setViewList(List<View> lists) {
        if (lists == null) {
            return;
        }
        mViewList.clear();
        for (View view : lists) {

            FrameLayout layout = new FrameLayout(getContext());
            layout.setPadding(CoverFlowAdapter.sWidthPadding, CoverFlowAdapter.sHeightPadding, CoverFlowAdapter.sWidthPadding, 0);
            layout.addView(view);
            mViewList.add(layout);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setOnPageSelectListener(CoverFlowAdapter.OnPageSelectListener listener) {
        this.listener = listener;
    }

}
