package com.chance.mimorobot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.model.AiuiResultEntity;
import com.chance.mimorobot.model.CityWeatherModel;
import com.chance.mimorobot.model.FlightResult;
import com.chance.mimorobot.utils.ObjectUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;


public class FlightActivity extends TitleBarActivity {
    FlightAdapter flightAdapter;
    List<FlightResult> list;
    TextView contentTv, answerTv;

    TextView startLocTv, endLocTv, dateTv;
    ListView listView;

    public static Intent getIntent(Context context, String json) {
        Intent intent = new Intent(context, FlightActivity.class);
        intent.putExtra("data", json);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setTitle("航班");
        content = getIntent().getStringExtra("data");
        setContentWithUIUpdate(content);

    }

    /**
     * 当前页需要的内容
     */
    protected String content = "";

    public void setContentWithUIUpdate(String content) {
        if (TextUtils.isEmpty(content))
            return;

        getList();
        if (!ObjectUtils.isEmpty(list)) {
            answerTv.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            flightAdapter = new FlightAdapter();
            listView.setAdapter(flightAdapter);
            listView.setOnItemClickListener(flightAdapter);
                if (list.size() > 0) {
                    String dateTime = list.get(0).getTakeOffTime();
                    dateTv.setText(dateTime);
                    startLocTv.setText(list.get(0).getDepartCity());
                    endLocTv.setText(list.get(0).getArriveCity());
                }
        } else {
            answerTv.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        Flowable.timer(30, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        finish();
                    }
                });
    }



    private void getList() {
        Type type=new TypeToken<List<FlightResult>>(){}.getType();
        this.list=new Gson().fromJson(content,type);
    }




    protected void init() {
        contentTv = (TextView) findViewById(R.id.titleContent);
        listView = (ListView) findViewById(R.id.listview);
        answerTv = (TextView) findViewById(R.id.answerTv);

        dateTv = (TextView)findViewById(R.id.flight_date);
        startLocTv = (TextView) findViewById(R.id.flight_startloc);
        endLocTv = (TextView) findViewById(R.id.flight_endloc);
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    @Override
    int getContentLayoutId() {
        return R.layout.activity_flight;
    }

    /**
     * 航班信息列表
     */
    class FlightAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(FlightActivity.this).inflate(R.layout.adapter_flight, null);
                viewHolder = new ViewHolder();
                viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.flight_icon);
                viewHolder.airLineTv = (TextView) convertView.findViewById(R.id.flight_airline);
                viewHolder.nameTv = (TextView) convertView.findViewById(R.id.flight_name);
                viewHolder.takeOffTimeTv = (TextView) convertView.findViewById(R.id.flight_takeOffTime);
                viewHolder.dPortTv = (TextView) convertView.findViewById(R.id.flight_dPort);
                viewHolder.arriveTimeTv = (TextView) convertView.findViewById(R.id.flight_arriveTime);
                viewHolder.aPortTv = (TextView) convertView.findViewById(R.id.flight_aPort);
                viewHolder.priceTv = (TextView) convertView.findViewById(R.id.flight_price);
                viewHolder.rateTv = (TextView) convertView.findViewById(R.id.flight_rate);
                viewHolder.weastTv = (TextView) convertView.findViewById(R.id.flight_weast);
                viewHolder.cabinTv = (TextView) convertView.findViewById(R.id.flight_cabin);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            FlightResult flightResult = list.get(position);
            String airline = flightResult.getAirline().replace("股份有限公司", "").replace("中国", "").replace("有限责任公司", "").replace("有限公司", "");
            viewHolder.airLineTv.setText(airline);
            viewHolder.nameTv.setText(flightResult.getFlight());
            String takeOffTIme = dateFormat.format(new Date( Long.parseLong(flightResult.getTakeOffTimeStamp()) * 1000));

            viewHolder.takeOffTimeTv.setText(takeOffTIme);
            viewHolder.dPortTv.setText(flightResult.getdPort().trim());
            String arriveTIme = dateFormat.format(new Date( Long.parseLong(flightResult.getArriveTimeStamp()) * 1000));
            viewHolder.arriveTimeTv.setText(arriveTIme);
            viewHolder.aPortTv.setText(flightResult.getaPort().trim());

//            if (flightResult.getPrice().equals("-1")) {
                viewHolder.priceTv.setText("票价未知");
//            } else {
//                Spannable WordtoSpan = new SpannableString("¥ " + flightResult.getPrice());
//                WordtoSpan.setSpan(new AbsoluteSizeSpan(18), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                WordtoSpan.setSpan(new AbsoluteSizeSpan(32), 2, WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                viewHolder.priceTv.setText(WordtoSpan);
//
//                if (flightResult.getRate() .equals("1") ) {
//                    viewHolder.rateTv.setText("全票");
//                } else {
//                    DecimalFormat decimalFormat = new DecimalFormat("#0.0");
//                    viewHolder.rateTv.setText(decimalFormat.format(Float.parseFloat(flightResult.getOntimeRate()) * 10) + " 折");
//                }
//            }
            String weastTv = getWeastTime((int) (Long.parseLong(flightResult.getArriveTimeStamp()) - Long.parseLong(flightResult.getTakeOffTimeStamp())));
            viewHolder.weastTv.setText(weastTv);
            viewHolder.cabinTv.setText(flightResult.getCabinInfo());
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }

        class ViewHolder {
            ImageView iconIv;
            TextView airLineTv, nameTv, takeOffTimeTv, dPortTv, arriveTimeTv, aPortTv, priceTv, rateTv;
            TextView weastTv, cabinTv;
        }
    }

    /**
     * 计算总时长
     *
     * @param times 单位 秒
     * @return
     */
    private String getWeastTime(int times) {
        StringBuilder sb = new StringBuilder();
        int totalMins = times / 60;
        int hour = totalMins / 60;
        int lessMins = totalMins % 60;

        if (hour > 0) {
            sb.append(hour + "小时");
        }

        if (lessMins > 0) {
            sb.append(lessMins + "分钟");
        }
        return sb.toString();
    }
}
