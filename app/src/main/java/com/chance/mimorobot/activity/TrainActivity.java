package com.chance.mimorobot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.chance.mimorobot.R;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.model.TrainResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class TrainActivity extends TitleBarActivity {
    private String TAG=TrainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private TextView mStartCity, mEndCity, mTitle, noTrain;//开始城市 结束城市 时间标题 暂无车次
    private String startCity, endCity, title;

    List<TrainResult> list;
    private TrainAdapter trainAdapter;

    Gson gson = new Gson();

    /**
     * 当前页需要的内容
     */
    protected String content = "";


    public static Intent getIntent(Context context, String json) {
        Intent intent = new Intent(context, TrainActivity.class);
        intent.putExtra("data", json);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = getIntent().getStringExtra("data");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTitle = (TextView) findViewById(R.id.mTitle);
        mEndCity = (TextView) findViewById(R.id.mEndCity);
        mStartCity = (TextView) findViewById(R.id.mStartCity);
        noTrain = (TextView) findViewById(R.id.noTrain);
        this.list = getList();
        if (!ObjectUtils.isEmpty(list)) {
            noTrain.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            trainAdapter = new TrainAdapter();
            recyclerView.setAdapter(trainAdapter);

            TrainResult trainResult = list.get(0);
            mTitle.setText(trainResult.getStarttime_for_voice().replaceAll("\\d\\d:\\d\\d", "").replaceAll("\\d\\d\\d\\d年", ""));
            mStartCity.setText(trainResult.getOriginStation());
            mEndCity.setText(trainResult.getTerminalStation());
        } else {
            noTrain.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        Flowable.timer(30, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        finish();
                    }
                });
    }

    private List<TrainResult> getList() {
        Type type=new TypeToken<List<TrainResult>>(){}.getType();
        this.list=new Gson().fromJson(content,type);
        return list;
    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_train;
    }


    /**
     * 列车适配器
     */
    class TrainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_train_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TrainResult result = list.get(position);
            if (holder instanceof ViewHolder) {
                ((ViewHolder) holder).startTime.setText(result.getStartTime().replaceAll("\\d\\d\\d\\d-\\d\\d-\\d\\d", ""));
                ((ViewHolder) holder).endTime.setText(result.getArrivalTime().replaceAll("\\d\\d\\d\\d-\\d\\d-\\d\\d", ""));
                ((ViewHolder) holder).price.setText(result.getPrice().get(0).getValue());
                ((ViewHolder) holder).trainNo.setText(result.getTrainNo());
                ((ViewHolder) holder).runTime.setText(result.getRunTime());
//                long offsetDay = result.getOffsetDay();
//                if (offsetDay > 0) {
//                    ((ViewHolder) holder).offsetDay.setVisibility(View.VISIBLE);
//                    ((ViewHolder) holder).offsetDay.setText("+" + offsetDay + "天");
//                } else {
//                    ((ViewHolder) holder).offsetDay.setVisibility(View.INVISIBLE);
//                }
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView startTime, endTime, price, trainNo, offsetDay, runTime;

            public ViewHolder(View itemView) {
                super(itemView);
                startTime = (TextView) itemView.findViewById(R.id.startTime);
                endTime = (TextView) itemView.findViewById(R.id.endTime);
                price = (TextView) itemView.findViewById(R.id.price);
                trainNo = (TextView) itemView.findViewById(R.id.trainNo);
                offsetDay = (TextView) itemView.findViewById(R.id.offsetDay);
                runTime = (TextView) itemView.findViewById(R.id.runTime);
            }
        }
    }
}
