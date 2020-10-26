package com.chance.mimorobot.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.chance.mimorobot.R;

/**
 * created by Lynn
 * on 2019/6/3
 */
public class TitleBarView extends RelativeLayout {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvRightTitle;

    public TitleBarView(Context context) {
        super(context);
        init(null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_title_bar, this, true);

        mIvBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title_bar_title);
        mTvRightTitle = (TextView) findViewById(R.id.tv_title_bar_right_title);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBarView);

            if (typedArray.getBoolean(R.styleable.TitleBarView_title_bar_has_left_icon, true)) {
                mIvBack.setVisibility(VISIBLE);
            } else {
                mIvBack.setVisibility(GONE);
            }

            String title = typedArray.getString(R.styleable.TitleBarView_title_bar_title);
            if (!StringUtils.isEmpty(title)) {
                mTvTitle.setText(title);
            }
            String rightTitle = typedArray.getString(R.styleable.TitleBarView_title_bar_right_title);
            if (!StringUtils.isEmpty(rightTitle)) {
                mTvRightTitle.setText(rightTitle);
            }

            int titleSize = typedArray.getDimensionPixelSize(R.styleable.TitleBarView_title_bar_title_size, getResources().getDimensionPixelOffset(R.dimen.header_text_size));
            mTvTitle.setTextSize(0, titleSize);
            int rightTitleSize = typedArray.getDimensionPixelSize(R.styleable.TitleBarView_title_bar_right_title_size, getResources().getDimensionPixelOffset(R.dimen.header_text_size));
            mTvRightTitle.setTextSize(0, rightTitleSize);

            Drawable leftIcon = typedArray.getDrawable(R.styleable.TitleBarView_title_bar_left_icon);
            if (leftIcon != null) {
                mIvBack.setImageDrawable(leftIcon);
            }

            typedArray.recycle();
        }
    }

    public void setBackListener(OnClickListener onClickListener) {
        mIvBack.setOnClickListener(onClickListener);
    }

    public void setRightListener(OnClickListener onClickListener) {
        mTvRightTitle.setOnClickListener(onClickListener);
    }

    public void setTitleListener(OnClickListener onClickListener) {
        mTvTitle.setOnClickListener(onClickListener);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setRightTitle(String title) {
        mTvRightTitle.setText(title);
    }

    public void setBackVisible(boolean visible) {
        mIvBack.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setTitleSize(int titleSize) {
        mTvTitle.setTextSize(titleSize);
    }

    public void setRightTitleSize(int titleSize) {
        mTvRightTitle.setTextSize(titleSize);
    }
}
