package com.chance.mimorobot.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chance.mimorobot.activity.WelcomeActivity;


/**
 * 开机启动广播
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)) {
            // 每次开机后设置SLAM地图为未初始化状态
            Intent ootStartIntent = new Intent(context, WelcomeActivity.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }
    }
}
