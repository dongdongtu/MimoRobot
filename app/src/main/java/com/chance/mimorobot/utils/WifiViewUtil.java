package com.chance.mimorobot.utils;

import android.app.Activity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.chance.mimorobot.R;
import com.chance.mimorobot.utils.wifi.entities.WifiEntity;
import com.chance.mimorobot.utils.wifi.enums.WifiSecutityEnum;
import com.chance.mimorobot.utils.wifi.enums.WifiStateEnum;

import androidx.annotation.NonNull;

/**
 * created by Lynn
 * on 2019/6/18
 */
public class WifiViewUtil {

    private OnDialogButtonClickListener listener;
    private Activity activity;

    public WifiViewUtil(Activity activity) {
        this.activity = activity;
    }

    /**
     * 输入wifi密码的dialog
     */
    private MaterialDialog mDialogConnectWifi;
    private WifiEntity mConnectWifiEntity;
    private EditText etInputPassword;

    public void createDialogConnectWifi(final WifiEntity wifiEntity) {
        mConnectWifiEntity = wifiEntity;
        mDialogConnectWifi = new MaterialDialog.Builder(activity)
                .title(wifiEntity.getWifiName())
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.dialog_input_wifi_password, false)
                .positiveText("连接")
                .negativeText("返回")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null)
                            listener.onConnectClick(etInputPassword.getText().toString(), wifiEntity);
                    }
                })
                .build();
        View convertView = mDialogConnectWifi.getCustomView();

        MDButton positiveAction = mDialogConnectWifi.getActionButton(DialogAction.POSITIVE);
        positiveAction.setEnabled(false);
        etInputPassword = (EditText) convertView.findViewById(R.id.et_password);
        RelativeLayout rlShowPassword = (RelativeLayout) convertView.findViewById(R.id.rl_show_password);
        ImageView ivSelectPassword = (ImageView) convertView.findViewById(R.id.iv_select_password);
        //点击显示密码选项
        rlShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivSelectPassword.getVisibility() == View.VISIBLE) {
                    ivSelectPassword.setVisibility(View.INVISIBLE);
                    etInputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    ivSelectPassword.setVisibility(View.VISIBLE);
                    etInputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });
        /**
         * 监听密码的位数，当密码大于等于8位时可以点击连接按钮
         */
        etInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etInputPassword.getText().toString().length() < 8) {
                    positiveAction.setEnabled(false);
                } else {
                    positiveAction.setEnabled(true);
                }
            }
        });
        mDialogConnectWifi.show();
    }


    /**
     * 已连接wifi的信息的对话框
     */
    private MaterialDialog mDialogConnectingWifi;
    private WifiEntity mConnectingWifiEntity;

    public void createDialogWifiConnectedInfo(final WifiEntity wifiEntity) {
        mConnectingWifiEntity = wifiEntity;
        String[] strWifiStrength = {"弱", "一般", "较强", "强"};
        mDialogConnectingWifi = new MaterialDialog.Builder(activity)
                .title(wifiEntity.getWifiName())
                .titleGravity(GravityEnum.CENTER)
                .customView(R.layout.dialog_wifi_info, false)
                .positiveText("返回")
                .neutralText(R.string.cancel_save)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) listener.onRemoveClick(wifiEntity.getNetId());
                    }
                })
                .build();
        View convertView = mDialogConnectingWifi.getCustomView();
        if (convertView == null) return;
        TextView tvWifiState = (TextView) convertView.findViewById(R.id.tv_wifi_state);
        tvWifiState.setText(WifiStateEnum.getState(wifiEntity.getWifiState()));
        TextView tvWifiStrength = (TextView) convertView.findViewById(R.id.tv_wifi_strength);
        tvWifiStrength.setText(strWifiStrength[wifiEntity.getWifiStrength()]);
        TextView tvWifiSpeed = (TextView) convertView.findViewById(R.id.tv_wifi_speed);
        tvWifiSpeed.setText(wifiEntity.getWifiSpeed());
        TextView tvWifiFrequency = (TextView) convertView.findViewById(R.id.tv_wifi_frequency);
        tvWifiFrequency.setText(wifiEntity.getWifiConnFrequency());
        TextView tvWifi_security = (TextView) convertView.findViewById(R.id.tv_wifi_security);
        tvWifi_security.setText(WifiSecutityEnum.getWifiSecutity(wifiEntity.getWifiSecurityMode()));
        mDialogConnectingWifi.show();
    }

    /**
     * 已保存wifi的对话框
     */
    private MaterialDialog mDialogSavedWifi;
    private WifiEntity mSavedWifiEntity;

    public void createDialogWifiSave(final WifiEntity wifiEntity) {
        mSavedWifiEntity = wifiEntity;
        mDialogSavedWifi = new MaterialDialog.Builder(activity)
                .title(wifiEntity.getWifiName())
                .titleGravity(GravityEnum.CENTER)
                .content("")
                .positiveText("连接")
                .negativeText("返回")
                .neutralText(R.string.cancel_save)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onRemoveClick(wifiEntity.getNetId());
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (listener != null) {
                            listener.onSavedConnectClick(wifiEntity.getNetId());
                        }
                    }
                })
                .show();
    }


    public interface OnDialogButtonClickListener {
        void onConnectClick(String password, WifiEntity wifiEntity);

        void onSavedConnectClick(int netId);

        void onRemoveClick(int netId);
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener listener) {
        this.listener = listener;
    }
}
