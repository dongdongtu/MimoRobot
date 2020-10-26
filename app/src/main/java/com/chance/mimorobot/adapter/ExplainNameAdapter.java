package com.chance.mimorobot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.model.ExplainModel;
import com.chance.mimorobot.model.MapPoint;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExplainNameAdapter extends RecyclerView.Adapter<ExplainPointAdapter.PointViewHolder>{


    public interface OnItemClickListener {
        void onWifiItemClicked(ExplainModel.DataBean wifiEntity);
    }

    private List<ExplainModel.DataBean> nameCollection;

    private OnItemClickListener onItemClickListener;

    public ExplainNameAdapter(Context context) {
        this.nameCollection = Collections.emptyList();
    }


    @NonNull
    @Override
    public ExplainPointAdapter.PointViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_explain_point, parent, false);
        return new ExplainPointAdapter.PointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExplainPointAdapter.PointViewHolder holder, int position) {
        final ExplainModel.DataBean explainModel = this.nameCollection.get(position);

        holder.name.setText(explainModel.getPathname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onWifiItemClicked(explainModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (this.nameCollection != null) ? this.nameCollection.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setCollection(Collection<ExplainModel.DataBean> nameCollection) {
        this.validateWifisCollection(nameCollection);
        this.nameCollection = (List<ExplainModel.DataBean>) nameCollection;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateWifisCollection(Collection<ExplainModel.DataBean> wifisCollection) {
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
