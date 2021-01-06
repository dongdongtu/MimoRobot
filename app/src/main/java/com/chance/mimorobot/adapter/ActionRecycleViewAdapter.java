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

public class ActionRecycleViewAdapter extends RecyclerView.Adapter<ActionRecycleViewAdapter.ActionViewHolder> {

    private List<ActionItemModel> listData;
    private Context context;

    public ActionRecycleViewAdapter(List<ActionItemModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    public OnActionItemClick getOnActionItemClick() {
        return onActionItemClick;
    }

    public void setOnActionItemClick(OnActionItemClick onActionItemClick) {
        this.onActionItemClick = onActionItemClick;
    }

    private OnActionItemClick onActionItemClick;

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_action_list, parent, false);
        return new ActionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        holder.textView.setText(listData.get(position).getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onActionItemClick.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    static class ActionViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_name)
        TextView textView ;
        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public interface OnActionItemClick {
        void onClick(int position);
    }
}
