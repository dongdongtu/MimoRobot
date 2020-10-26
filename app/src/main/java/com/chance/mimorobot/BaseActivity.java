package com.chance.mimorobot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.chance.mimorobot.activity.ExplainActivty;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("ACTION_STOP");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public void onResume() {
        ((MyApplication) getApplication()).setCurrentActivity(this);
        super.onResume();
    }

    public void showToast(String text){
        Toasty.warning(this,text, Toast.LENGTH_SHORT).show();
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("ACTION_STOP".equals(action)) {
                ((MyApplication)getApplication()).setActoin(false);
            }
        }


    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
