package com.chance.mimorobot.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chance.mimorobot.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class RoomDetailActivity extends TitleBarActivity {
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.img4)
    ImageView img4;
    @BindView(R.id.img5)
    ImageView img5;
    @BindView(R.id.img6)
    ImageView img6;
    @BindView(R.id.img7)
    ImageView img7;
    @BindView(R.id.image)
    PhotoView image;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.ll2)
    LinearLayout ll2;
    @BindView(R.id.ll3)
    LinearLayout ll3;
    @BindView(R.id.ll4)
    LinearLayout ll4;
    @BindView(R.id.ll5)
    LinearLayout ll5;
    @BindView(R.id.ll6)
    LinearLayout ll6;
    @BindView(R.id.ll7)
    LinearLayout ll7;


    @Override
    int getContentLayoutId() {
        return R.layout.activity_room_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        image.setImageResource(R.drawable.room_a);
        ll1.setBackgroundResource(R.drawable.room_sel_shape);
        ll2.setBackgroundResource(R.drawable.room_nosel_shape);
        ll3.setBackgroundResource(R.drawable.room_nosel_shape);
        ll4.setBackgroundResource(R.drawable.room_nosel_shape);
        ll5.setBackgroundResource(R.drawable.room_nosel_shape);
        ll6.setBackgroundResource(R.drawable.room_nosel_shape);
        ll7.setBackgroundResource(R.drawable.room_nosel_shape);
    }

    @OnClick({R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.ll7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                image.setImageResource(R.drawable.room_a);
                ll1.setBackgroundResource(R.drawable.room_sel_shape);
                ll2.setBackgroundResource(R.drawable.room_nosel_shape);
                ll3.setBackgroundResource(R.drawable.room_nosel_shape);
                ll4.setBackgroundResource(R.drawable.room_nosel_shape);
                ll5.setBackgroundResource(R.drawable.room_nosel_shape);
                ll6.setBackgroundResource(R.drawable.room_nosel_shape);
                ll7.setBackgroundResource(R.drawable.room_nosel_shape);
                break;
            case R.id.ll2:
                image.setImageResource(R.drawable.room_b);
                ll1.setBackgroundResource(R.drawable.room_nosel_shape);
                ll2.setBackgroundResource(R.drawable.room_sel_shape);
                ll3.setBackgroundResource(R.drawable.room_nosel_shape);
                ll4.setBackgroundResource(R.drawable.room_nosel_shape);
                ll5.setBackgroundResource(R.drawable.room_nosel_shape);
                ll6.setBackgroundResource(R.drawable.room_nosel_shape);
                ll7.setBackgroundResource(R.drawable.room_nosel_shape);
                break;
            case R.id.ll3:
                image.setImageResource(R.drawable.room_c);
                ll1.setBackgroundResource(R.drawable.room_nosel_shape);
                ll2.setBackgroundResource(R.drawable.room_nosel_shape);
                ll3.setBackgroundResource(R.drawable.room_sel_shape);
                ll4.setBackgroundResource(R.drawable.room_nosel_shape);
                ll5.setBackgroundResource(R.drawable.room_nosel_shape);
                ll6.setBackgroundResource(R.drawable.room_nosel_shape);
                ll7.setBackgroundResource(R.drawable.room_nosel_shape);
                break;
            case R.id.ll4:
                image.setImageResource(R.drawable.room_d);
                ll1.setBackgroundResource(R.drawable.room_nosel_shape);
                ll2.setBackgroundResource(R.drawable.room_nosel_shape);
                ll3.setBackgroundResource(R.drawable.room_nosel_shape);
                ll4.setBackgroundResource(R.drawable.room_sel_shape);
                ll5.setBackgroundResource(R.drawable.room_nosel_shape);
                ll6.setBackgroundResource(R.drawable.room_nosel_shape);
                ll7.setBackgroundResource(R.drawable.room_nosel_shape);
                break;
            case R.id.ll5:
                image.setImageResource(R.drawable.room_e);
                ll1.setBackgroundResource(R.drawable.room_nosel_shape);
                ll2.setBackgroundResource(R.drawable.room_nosel_shape);
                ll3.setBackgroundResource(R.drawable.room_nosel_shape);
                ll4.setBackgroundResource(R.drawable.room_nosel_shape);
                ll5.setBackgroundResource(R.drawable.room_sel_shape);
                ll6.setBackgroundResource(R.drawable.room_nosel_shape);
                ll7.setBackgroundResource(R.drawable.room_nosel_shape);
                break;
            case R.id.ll6:
                image.setImageResource(R.drawable.room_f);
                ll1.setBackgroundResource(R.drawable.room_nosel_shape);
                ll2.setBackgroundResource(R.drawable.room_nosel_shape);
                ll3.setBackgroundResource(R.drawable.room_nosel_shape);
                ll4.setBackgroundResource(R.drawable.room_nosel_shape);
                ll5.setBackgroundResource(R.drawable.room_nosel_shape);
                ll6.setBackgroundResource(R.drawable.room_sel_shape);
                ll7.setBackgroundResource(R.drawable.room_nosel_shape);
                break;
            case R.id.ll7:
                image.setImageResource(R.drawable.room_g);
                ll1.setBackgroundResource(R.drawable.room_nosel_shape);
                ll2.setBackgroundResource(R.drawable.room_nosel_shape);
                ll3.setBackgroundResource(R.drawable.room_nosel_shape);
                ll4.setBackgroundResource(R.drawable.room_nosel_shape);
                ll5.setBackgroundResource(R.drawable.room_nosel_shape);
                ll6.setBackgroundResource(R.drawable.room_nosel_shape);
                ll7.setBackgroundResource(R.drawable.room_sel_shape);
                break;
        }
    }
}
