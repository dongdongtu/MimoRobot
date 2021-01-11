package com.chance.mimorobot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.retrofit.model.ActionItemModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomRecycleViewAdapter extends RecyclerView.Adapter<RoomRecycleViewAdapter.RoomViewHolder> {

    private List<ActionItemModel> listData;
    private Context context;

    public RoomRecycleViewAdapter(List<ActionItemModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    public void setOnActionRoomItemClick(OnActionRoomItemClick onActionItemClick) {
        this.onActionItemClick = onActionItemClick;
    }

    private OnActionRoomItemClick onActionItemClick;

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_room_list, parent, false);
        return new RoomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        holder.textView.setText(listData.get(position).getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionItemClick.onRoomClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView textView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnActionRoomItemClick {
        void onRoomClick(int position);
    }
}