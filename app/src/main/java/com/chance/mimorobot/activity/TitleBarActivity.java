package com.chance.mimorobot.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.chance.mimorobot.BaseActivity;
import com.chance.mimorobot.R;
import com.chance.mimorobot.widget.TitleBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class TitleBarActivity extends BaseActivity {
    private Unbinder unbinder;

    @BindView(R.id.layout_title_bar_container)
    RelativeLayout layoutContainer;
    @BindView(R.id.view_fragment_title_bar)
    TitleBarView viewTitleBar;
    FrameLayout layoutContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_title_bar);

        layoutContent = findViewById(R.id.layout_title_bar_content);
        if (getContentLayoutId() != 0) {
            View contentView = LayoutInflater.from(this).inflate(getContentLayoutId(), null, false);
            layoutContent.addView(contentView);
        }
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({ R.id.iv_title_bar_back, R.id.tv_title_bar_title, R.id.tv_title_bar_right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_bar_back:
                onBackClicked();
                break;
            case R.id.tv_title_bar_title:
                onTitleClicked();
                break;
            case R.id.tv_title_bar_right_title:
                onRightTitleClicked();
                break;
        }
    }

    public void onBackClicked() {
        finish();
    }

    public void onTitleClicked() {
    }

    public void onRightTitleClicked() {
    }

    public void onRetryClicked() {
    }

    public void setBackgroundRes(int resId) {
        layoutContainer.setBackgroundResource(resId);
    }

    public void setTitle(String title) {
        viewTitleBar.setTitle(title);
    }

    public void setRightTitle(String title) {
        viewTitleBar.setRightTitle(title);
    }

    abstract int getContentLayoutId();
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }
}
