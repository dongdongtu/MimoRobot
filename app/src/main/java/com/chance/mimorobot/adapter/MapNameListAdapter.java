package com.chance.mimorobot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.model.RobotMap;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapNameListAdapter extends RecyclerView.Adapter<MapNameListAdapter.MapViewHolder> {
    public interface OnPointItemClickListener {
        void onWifiItemClicked(RobotMap robotMap);
    }
    public interface OnPointItemLongClickListener {
        void onWifiItemClicked(RobotMap robotMap);
    }

    private List<RobotMap> wifiCollection;

    private OnPointItemClickListener onItemClickListener;

    private OnPointItemLongClickListener onItemLongClickListener;

    public MapNameListAdapter(Context context) {
        this.wifiCollection = Collections.emptyList();
    }

    @Override
    public int getItemCount() {
        return (this.wifiCollection != null) ? this.wifiCollection.size() : 0;
    }

    @Override
    public MapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explain_point, parent, false);
        return new MapViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MapViewHolder holder, final int position) {
        final RobotMap robotMapPoint = this.wifiCollection.get(position);

        holder.name.setText(robotMapPoint.getMapName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onWifiItemClicked(robotMapPoint);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onWifiItemClicked(robotMapPoint);
                }
                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setMapCollection(Collection<RobotMap> pointCollection) {
        this.validateWifisCollection(pointCollection);
        this.wifiCollection = (List<RobotMap>) pointCollection;
        this.notifyDataSetChanged();
    }

    public void setOnPointItemLongClickListener(OnPointItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnPointItemClickListener(OnPointItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    private void validateWifisCollection(Collection<RobotMap> wifisCollection) {
        if (wifisCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }




    static class MapViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView name;

        MapViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
