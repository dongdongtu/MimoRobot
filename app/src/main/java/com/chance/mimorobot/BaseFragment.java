package com.chance.mimorobot;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.chance.mimorobot.activity.MainActivity;
import com.chance.mimorobot.manager.SharedPreferencesManager;
import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @Description 所有Fragment的基类
 * @Author wj
 * @Time 2017/6/30 15:00
 */

public abstract class BaseFragment extends Fragment {

    public static final String ARGUMENT = "argument";

    protected SharedPreferencesManager preferencesManager;
    protected SharedPreferences sharedPreferences;
    Toast toast;

    protected Gson gson = new Gson();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = SharedPreferencesManager.newInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    /**
     * 获取主Activity
     *
     * @return
     */
    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    /**
     * 设置布局文件的ID
     */
    protected abstract int setLayoutId();

    /**
     * 初始化控件状态
     */
    protected abstract void init(View rootView);

    protected void toast(String text) {
        if (toast == null) {
            toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        MyApplication.Info.getRefWatcher(getContext()).watch(this);
    }
}
