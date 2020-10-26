package com.chance.mimorobot.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chance.mimorobot.R;



/**
 * 状态栏单项
 * Created by Shao Shizhang on 2018/5/30.
 */
public class StatusBarItem extends LinearLayout {

    private ImageView mIcon;
    private TextView mText;

    private int textVisibility = View.VISIBLE;

    private OnBarItemClickListener mListener;

    public StatusBarItem(Context context) {
        this(context, null);
    }

    public StatusBarItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initItem(context, attrs, defStyle);
    }

    private void initItem(Context ctx, AttributeSet attrs, int defStyle){
        LayoutInflater ll = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = ll.inflate(R.layout.item_status_bar, null);

        addView(v);

        mIcon = (ImageView)findViewById(R.id.iv_bar_icon );
        mText = (TextView)findViewById(R.id.tv_bar_text);

        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.BarItem, defStyle, 0);

        Drawable icon = a.getDrawable(R.styleable.BarItem_android_icon);
        setIcon(icon);

        CharSequence txt = a.getText(R.styleable.BarItem_android_text);
        if(txt != null){
            setText(txt.toString());
        }

        int textColor = a.getColor(R.styleable.BarItem_android_textColor, getResources().getColor(android.R.color.white));
        setTextColor(textColor);

        textVisibility = a.getInt(R.styleable.BarItem_textVisibility, textVisibility);
        setTextVisibility(textVisibility);

        a.recycle();
    }

    public void setIcon(Drawable icon){
        if(icon == null){
            return;
        }
        this.mIcon.setImageDrawable(icon);
    }

    public void setIcon(int resId){
        setIcon(getResources().getDrawable(resId));
    }

    public void setText(int resId){
        setText(getResources().getString(resId));
    }

    public void setText(String text){
        this.mText.setText(text);
    }

    public void setTextColor(int textColor){
        mText.setTextColor(textColor);
    }

    /**
     * 设置文字的可见状态
     * @param visibility {@link View#GONE},{@link View#VISIBLE}
     * @attr R.styleable.BarItem_textVisibility
     */
    public void setTextVisibility(int visibility){
        mText.setVisibility(visibility);
    }

    /**
     * 设置图标单击事件监听器
     * @param l
     */
    public void setBarItemClickListener(OnBarItemClickListener l){
        this.mListener = l;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(StatusBarItem.this);
                }
            }
        });
    }

    /**
     * BarItem click event
     */
    public interface OnBarItemClickListener {
        void onClick(StatusBarItem item);
    }
}
