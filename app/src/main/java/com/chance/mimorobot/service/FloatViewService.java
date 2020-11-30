package com.chance.mimorobot.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chance.mimorobot.R;
import com.chance.mimorobot.manager.ActionManager;

public class FloatViewService extends Service {
    private static final String TAG = "FloatViewService";
    //定义浮动窗口布局
    private LinearLayout mFloatLayout;
    private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;

    private ImageView mFloatView;


    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG, "onCreate");
        createFloatView();
    }

    @SuppressWarnings("static-access")
    @SuppressLint("InflateParams") private void createFloatView()
    {
        wmParams = new WindowManager.LayoutParams();
        //通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = -1500;
        wmParams.y = -1500;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.alert_window_menu, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        mFloatView = (ImageView) mFloatLayout.findViewById(R.id.alert_window_imagebtn);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //设置监听浮动窗口的触摸移动
//        mFloatView.setOnTouchListener(new View.OnTouchListener()
//        {
//
//            boolean isClick;
//
//            @SuppressLint("ClickableViewAccessibility") @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
////                        mFloatView.setBackgroundResource(R.drawable.circle_red);
//                        isClick = false;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        isClick = true;
//                        // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
//                        wmParams.x = (int) event.getRawX()
//                                - mFloatView.getMeasuredWidth() / 2;
//                        // 减25为状态栏的高度
//                        wmParams.y = (int) event.getRawY()
//                                - mFloatView.getMeasuredHeight() / 2 - 75;
//                        // 刷新
//                        mWindowManager.updateViewLayout(mFloatLayout, wmParams);
//                        return true;
//                    case MotionEvent.ACTION_UP:
////                        mFloatView.setBackgroundResource(R.drawable.circle_cyan);
//                        return isClick;// 此处返回false则属于移动事件，返回true则释放事件，可以出发点击否。
//
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });

        mFloatView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent("ACTION_STOP");
                sendBroadcast(intent);
                ActionManager.getInstance().cancelAction();
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(mFloatLayout != null)
        {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
