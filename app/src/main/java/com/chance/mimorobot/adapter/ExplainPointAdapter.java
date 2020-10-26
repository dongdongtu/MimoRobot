package com.chance.mimorobot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.chance.mimorobot.R;

import com.chance.mimorobot.model.MapPoint;


import java.util.Collection;
import java.util.Collections;
import java.util.List;



import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExplainPointAdapter extends RecyclerView.Adapter<ExplainPointAdapter.PointViewHolder> {

    public interface OnPointItemClickListener {
        void onWifiItemClicked(MapPoint wifiEntity);
    }
    public interface OnPointItemLongClickListener {
        void onWifiItemClicked(MapPoint wifiEntity);
    }


    private List<MapPoint> wifiCollection;

    private OnPointItemClickListener onItemClickListener;
    private OnPointItemLongClickListener onItemLongClickListener;

    public ExplainPointAdapter(Context context) {
        this.wifiCollection = Collections.emptyList();
    }

    @Override
    public int getItemCount() {
        return (this.wifiCollection != null) ? this.wifiCollection.size() : 0;
    }

    @Override
    public PointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explain_point, parent, false);
        return new PointViewHolder(view);
    }


    @Override
    public void onBindViewHolder(PointViewHolder holder, final int position) {
        final MapPoint mapPoint = this.wifiCollection.get(position);

        holder.name.setText(mapPoint.getPointName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onWifiItemClicked(mapPoint);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onWifiItemClicked(mapPoint);
                }
                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setWifiCollection(Collection<MapPoint> pointCollection) {
        this.validateWifisCollection(pointCollection);
        this.wifiCollection = (List<MapPoint>) pointCollection;
        this.notifyDataSetChanged();
    }

    public void setOnPointItemClickListener(OnPointItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void setOnPointItemLongClickListener(OnPointItemLongClickListener onItemClickListener) {
        this.onItemLongClickListener = onItemClickListener;
    }

    private void validateWifisCollection(Collection<MapPoint> wifisCollection) {
        if (wifisCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    static class PointViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView name;

        PointViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
