package com.chance.mimorobot.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chance.mimorobot.R;
import com.chance.mimorobot.adapter.WifiAdapter;
import com.chance.mimorobot.utils.WifiViewUtil;
import com.chance.mimorobot.utils.wifi.WifiProcess;
import com.chance.mimorobot.utils.wifi.WifiProcessInterface;
import com.chance.mimorobot.utils.wifi.entities.WifiEntity;
import com.chance.mimorobot.utils.wifi.enums.WifiSecutityEnum;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WifiActivity extends TitleBarActivity {

    private String TAG=WifiActivity.class.getSimpleName();
    private int connectingNetId = -1;    //正在连接wifi的netId

    @BindView(R.id.tv_wifi_list_title)
    TextView tvWifiListTitle;
    @BindView(R.id.view_wifi_list_divider)
    ImageView viewWifiListDivider;
    @BindView(R.id.rv_wifi)
    RecyclerView rvWifi;
    private MaterialDialog progressDialog;
    WifiViewUtil mWifiViewUtil;
    WifiAdapter mWifiAdapter;
    WifiProcessInterface wifiProcess;
    private boolean canGetWifiList = true;
    private List<WifiEntity> wifiEntityList;

    private WifiReceiver receiverWifi;
    private long clickConnectWifi;
    private Disposable rxSubscriptionConnectWifi;
    private final static int WifiConnectMaxTime = 10;
    private final static long SCAN_WIFI_PERIOD = 500;


    private boolean isInit = false;
    @Override
    int getContentLayoutId() {
        return R.layout.activity_wifi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        mWifiAdapter=new WifiAdapter(this);
        setupRecyclerView();
        loadWifi();
        mWifiViewUtil=new WifiViewUtil(WifiActivity.this);
        mWifiViewUtil.setOnDialogButtonClickListener(new WifiViewUtil.OnDialogButtonClickListener() {
            @Override
            public void onConnectClick(String password, WifiEntity wifiEntity) {
                connectWifi(password, wifiEntity);
            }

            @Override
            public void onSavedConnectClick(int netId) {
                connectWifi(netId);
            }

            @Override
            public void onRemoveClick(int netId) {
                removeWifi(netId);
            }
        });
    }
    /**
     * Loads all wifis.
     */
    private void loadWifi() {
        this.getWifi();
    }

    private void getWifi() {
        wifiProcess = new WifiProcess();//初始化
        wifiProcess.init(this);
        wifiProcess.recyclerScan(SCAN_WIFI_PERIOD);
        registerReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    /**
     * 打开wifi
     */
    public void openWifi() {
        wifiProcess.openWifi();
        setCanGetWifiList(true);
    }

    /**
     * 关闭wifi
     */
    public void closeWifi() {
        wifiProcess.closeWifi();
        setCanGetWifiList(false);
    }


    /**
     * 连接未保存wifi
     */
    public void connectWifi(String password, WifiEntity wifiEntity) {
        clickConnectWifi = System.currentTimeMillis();
        setCanGetWifiList(false);
        connectingNetId = wifiProcess.connectWifi(wifiEntity.getWifiName(), password, wifiEntity.getWifiSecurityMode());
        showProgressDialog();
    }

    public void disconnectWifi(int netId) {
        wifiProcess.disconnectWifi(netId);
    }


    /**
     * 连接已保存的wifi
     *
     * @param netId
     */
    public void connectWifi(int netId) {
        clickConnectWifi = System.currentTimeMillis();
        connectingNetId = netId;
        setCanGetWifiList(false);
        wifiProcess.connectWifi(netId);
        showProgressDialog();
    }


    /**
     * 如果在规定时间无法连接wifi，则返回连接wifi错误
     */
    public void startRxConnectWifi() {
        if (rxSubscriptionConnectWifi == null) {
            rxSubscriptionConnectWifi = Observable.timer(WifiConnectMaxTime, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                                connectWifiFail();
                        }
                    });
        }
    }

    public void cancelRxConnectWifi() {
        if (rxSubscriptionConnectWifi != null && !rxSubscriptionConnectWifi.isDisposed()) {
            rxSubscriptionConnectWifi.dispose();
        }
        rxSubscriptionConnectWifi = null;
    }

    /**
     * 删除wifi
     */
    public void removeWifi(int id) {
        wifiProcess.removeWifi(id);       //连接失败删除该netId
    }

    /**
     * 删除wifi
     */
    public void removeConnectingWifi() {
        wifiProcess.removeWifi(connectingNetId);       //连接失败删除该netId
    }
    /**
     * 扫描完成
     */
    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {//wifi result
                if (wifiProcess.isWifiEnabled() && canGetWifiList) {
                    wifiEntityList = wifiProcess.getWifiEntityList();
                    if (wifiEntityList == null) {
                        return;
                    }
                    if (wifiEntityList.size() == 0) {
                        return;
                    }
                    Log.d(TAG, "onReceive: 得到wifi列表 size:" + wifiEntityList.size());
                    renderWifiList(noSameName(wifiEntityList));
                    if (isInit) {
                        isInit = false;

                    }
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {//wifi连接网络状态变化
                NetworkInfo.DetailedState state = ((NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).getDetailedState();
                if (wifiEntityList != null && wifiEntityList.size() > 0) {
                    dealWifiState(state);
                }
            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {//wifi状态变化
                int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                    if (wifiEntityList != null) {
                        wifiEntityList.clear();
                    }
//                    mView.CloseWifiViewVisibility();
                } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
//                    mView.OpenWifiViewVisibility();
                }
            }
        }
    }

    /**
     * 处理wifi的变化
     *
     * @param aState
     */
    private void dealWifiState(NetworkInfo.DetailedState aState) {
        if (aState == NetworkInfo.DetailedState.SCANNING) {
            Log.d(TAG, "dealWifiState: 正在扫描");
        } else if (aState == NetworkInfo.DetailedState.CONNECTING) {
            Log.d(TAG, "dealWifiState: 正在连接");
            wifiEntityList = wifiProcess.getWifiEntityList();
            renderWifiList(noSameName(wifiEntityList));
        } else if (aState == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
            Log.d(TAG, "dealWifiState: 正在获取ip");
            wifiEntityList = wifiProcess.getWifiEntityList();
            renderWifiList(noSameName(wifiEntityList));
        } else if (aState == NetworkInfo.DetailedState.CONNECTED) {
            long deltaTime = System.currentTimeMillis() - clickConnectWifi;
            String currentWifiSSID = wifiProcess.getWifiInfoTransform().getCurentWifiSSID();
            boolean isWifiConnect = wifiProcess.isWifiConnected();
            Log.d(TAG, "dealWifiState: 已连接 deltaTime" + deltaTime + "  getCurentWifiSSID" + currentWifiSSID + "  isWifiConnect" + isWifiConnect);
            if (deltaTime < 500 && !isWifiConnect)
                return;
            connectWifiSuccess();
            wifiEntityList = wifiProcess.getWifiEntityList();
            renderWifiList(noSameName(wifiEntityList));
        } else if (aState == NetworkInfo.DetailedState.DISCONNECTING) {

        } else if (aState == NetworkInfo.DetailedState.DISCONNECTED) {

        } else if (aState == NetworkInfo.DetailedState.FAILED) {
            renderWifiList(noSameName(wifiEntityList));
        }
    }

    public void setCanGetWifiList(boolean canGetWifiList) {
        this.canGetWifiList = canGetWifiList;
    }
    /**
     * 去除同名WIFI
     *
     * @param
     * @return 返回不包含同命的列表
     */
    public List<WifiEntity> noSameName(List<WifiEntity> oldSr) {
        List<WifiEntity> newSr = new ArrayList<WifiEntity>();
        if (oldSr != null && !oldSr.isEmpty())
            for (WifiEntity result : oldSr) {
                if (!TextUtils.isEmpty(result.getWifiName()) && !containName(newSr, result.getWifiName()))
                    newSr.add(result);
            }
        return newSr;
    }
    /**
     * 判断一个扫描结果中，是否包含了某个名称的WIFI
     *
     * @param sr   扫描结果
     * @param name 要查询的名称
     * @return 返回true表示包含了该名称的WIFI，返回false表示不包含
     */
    public boolean containName(List<WifiEntity> sr, String name) {
        if (sr != null && !sr.isEmpty())
            for (WifiEntity result : sr) {
                if (!TextUtils.isEmpty(result.getWifiName()) && result.getWifiName().equals(name))
                    return true;
            }
        return false;
    }

    /**
     * 解除广播注册
     */
    private void unregisterReceiver() {
        if (receiverWifi != null) {
           unregisterReceiver(receiverWifi);
        }
    }

    /**
     * 注册广播
     */
    private void registerReceiver() {
        receiverWifi = new WifiReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);  //wifi扫描成功后发送
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);   //网络连接状态改变后发送
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);      //wifi打开与否状态
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        registerReceiver(receiverWifi, intentFilter);

    }


    private void setupRecyclerView() {
        rvWifi.setLayoutManager(new LinearLayoutManager(this));
        rvWifi.setHasFixedSize(true);
        rvWifi.addItemDecoration(new DividerItemDecoration(WifiActivity.this, DividerItemDecoration.VERTICAL));

        mWifiAdapter.setOnItemClickListener(onItemClickListener);
        rvWifi.setAdapter(mWifiAdapter);
    }

    private WifiAdapter.OnItemClickListener onItemClickListener =
            new WifiAdapter.OnItemClickListener() {
                @Override
                public void onWifiItemClicked(WifiEntity wifiEntity) {

                        onWifiClick(wifiEntity);

                }
            };

    public void onWifiClick(WifiEntity wifiEntity) {
        selectWifi(wifiEntity);
    }
    public void renderWifiList(Collection<WifiEntity> wifiEntityCollection) {
        if (wifiEntityCollection != null) {
            mWifiAdapter.setWifiCollection(wifiEntityCollection);
        }
    }
    public void selectWifi(WifiEntity wifiEntity) {
        switch (wifiEntity.getWifiState()) {
            case STATE_NONE:
                if (wifiEntity.getWifiSecurityMode().equals(WifiSecutityEnum.NOON)) {
                    //不需要密码的情况，直接连接
                    connectWifi("", wifiEntity);
                } else {
                    mWifiViewUtil.createDialogConnectWifi(wifiEntity); //输入密码的对话框
                }
                break;
            case STATE_CONNECTED:
                mWifiViewUtil.createDialogWifiConnectedInfo(wifiEntity);
                break;
            case STATE_SAVED:
                mWifiViewUtil.createDialogWifiSave(wifiEntity);
                break;
            default:
        }
    }

    /**
     * 显示正在连接的dialog
     */
    public void showProgressDialog() {
        startRxConnectWifi();
        progressDialog = new MaterialDialog.Builder(WifiActivity.this)
                .title("连接中")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();
        progressDialog.setCancelable(false);
    }

    /**
     * 连接wifi失败
     */

    public void connectWifiFail() {
        if (progressDialog != null && progressDialog.isShowing()) {
            Toast.makeText(WifiActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            cancelRxConnectWifi();
            progressDialog.dismiss();
            progressDialog = null;
            removeConnectingWifi();     //连接失败删除该netId
            setCanGetWifiList(true);
        }
    }

    /**
     * 连接wifi成功
     */

    public void connectWifiSuccess() {
        if (progressDialog != null && progressDialog.isShowing()) {
            Toast.makeText(WifiActivity.this, "连接网络成功", Toast.LENGTH_SHORT).show();
            cancelRxConnectWifi();
            progressDialog.dismiss();
            progressDialog = null;
            setCanGetWifiList(true);
        }
    }



}
