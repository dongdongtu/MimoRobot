package com.chance.mimorobot.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chance.mimorobot.widget.StatusBar;
import com.chance.mimorobot.widget.StatusBarItem;



/**
 * 状态栏服务
 * Created by Shao Shizhang on 2018/5/30.
 */
public abstract class StatusBarService extends Service {

    private WindowManager winMgr;
    private StatusBar statusBar;
    private int mStatusBarHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        winMgr = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        statusBar = getStatusBar();
        initStatusBar(statusBar);
        statusBar.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mStatusBarHeight = statusBar.getMeasuredHeight();
        winMgr.addView(statusBar, getStatusBarLayoutParams(mStatusBarHeight));
    }

    protected void setStatusBarVisibility(int visibility) {
        statusBar.setVisibility(visibility);
    }

    private StatusBar getStatusBar() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewGroup v = (ViewGroup) inflater.inflate(getStatusBarLayoutId(), null);

        StatusBar statusBar;
        if (v instanceof StatusBar) {
            statusBar = (StatusBar) v;
        } else {
            statusBar = new StatusBar(this);
            int childCount = v.getChildCount();
            for (int i = 0; i < childCount; i++) {
                StatusBarItem item = (StatusBarItem) v.getChildAt(i);
                statusBar.addItem(item, i);
            }
        }
        return statusBar;
    }

    protected WindowManager.LayoutParams getStatusBarLayoutParams(int statusBarHieght) {
        WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
        wlp.format = PixelFormat.TRANSPARENT;
        wlp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

        wlp.x = 0;
        wlp.y = 0;

        wlp.height = statusBarHieght;
        wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wlp.type = WindowManager.LayoutParams.TYPE_PHONE;
        wlp.gravity = getStatusBarGravity();
        return wlp;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 获取状态栏布局,Root
     *
     * @return
     */
    protected abstract int getStatusBarLayoutId();

    /**
     * 初始化状态图标
     *
     * @param statusBar
     */
    protected void initStatusBar(StatusBar statusBar) {
    }

    protected int getStatusBarGravity() {
        return Gravity.LEFT | Gravity.TOP;
    }

    @Override
    public void onDestroy() {
        winMgr.removeView(statusBar);
        super.onDestroy();
    }
}
