/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.chance.mimorobot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.chance.mimorobot.R;
import com.chance.mimorobot.utils.wifi.entities.WifiEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;



import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adaptar that manages a collection of {@link WifiEntity}.
 */
public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.WifiViewHolder> {

    private int[] wifiIcomResources = {R.drawable.icon_wifi_1, R.drawable.icon_wifi_2, R.drawable.icon_wifi_3, R.drawable.icon_wifi_4};

    private final int COLOR_CONNECTED = Color.parseColor("#0088FF");
    private final int COLOR_DISCONNECTED = Color.parseColor("#000000");

    public interface OnItemClickListener {
        void onWifiItemClicked(WifiEntity wifiEntity);
    }

    private List<WifiEntity> wifiCollection;

    private OnItemClickListener onItemClickListener;

    public WifiAdapter(Context context) {
        this.wifiCollection = Collections.emptyList();
    }

    @Override
    public int getItemCount() {
        return (this.wifiCollection != null) ? this.wifiCollection.size() : 0;
    }

    @Override
    public WifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_wifi, parent, false);
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiViewHolder holder, final int position) {
        final WifiEntity wifiEntity = this.wifiCollection.get(position);

        holder.textViewName.setText(wifiEntity.getWifiName());
        holder.imageViewSignal.setImageResource(wifiIcomResources[wifiEntity.getWifiStrength()]);
        switch (wifiEntity.getWifiSecurityMode()) {
            case NOON:
                holder.imageViewLock.setImageResource(0);
                break;
            case WPA2:
                holder.imageViewLock.setImageResource(R.drawable.icon_wifi_lock);
                break;
            case WPA_AND_WPA2:
                holder.imageViewLock.setImageResource(R.drawable.icon_wifi_lock);
                break;
            default:
                holder.imageViewLock.setImageResource(0);
                break;
        }
        switch (wifiEntity.getWifiState()) {
            case STATE_CONNECTED:
                holder.textViewName.setTextColor(COLOR_CONNECTED);
                holder.textViewStatus.setText(R.string.is_connected);
                break;
            case STATE_SAVED:
                holder.textViewName.setTextColor(COLOR_DISCONNECTED);
                holder.textViewStatus.setText(R.string.is_saved);
                break;
            default:
                holder.textViewName.setTextColor(COLOR_DISCONNECTED);
                holder.textViewStatus.setText("");
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WifiAdapter.this.onItemClickListener != null) {
                    WifiAdapter.this.onItemClickListener.onWifiItemClicked(wifiEntity);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setWifiCollection(Collection<WifiEntity> wifiCollection) {
        this.validateWifisCollection(wifiCollection);
        this.wifiCollection = (List<WifiEntity>) wifiCollection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateWifisCollection(Collection<WifiEntity> wifisCollection) {
        if (wifisCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    static class WifiViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.wifi_name)
        TextView textViewName;
        @BindView(R.id.wifi_signal)
        ImageView imageViewSignal;
        @BindView(R.id.wifi_lock)
        ImageView imageViewLock;
        @BindView(R.id.wifi_status)
        TextView textViewStatus;

        WifiViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
