package com.chance.mimorobot.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 状态栏
 * Created by Shao Shizhang on 2018/5/30.
 */
public class StatusBar extends RelativeLayout {

    public StatusBar(Context context) {
        super(context);
        init(null, 0);
    }

    public StatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public StatusBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

    }

    public void addItem(StatusBarItem item){
        addItem(item, -1);
    }

    public void addItem(StatusBarItem item, int index){
        addView(item, index);
    }

}
