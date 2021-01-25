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

    @BindView(R.id.image)
    PhotoView image;
    @BindView(R.id.ll1)
    LinearLayout ll1;
    @BindView(R.id.ll2)
    LinearLayout ll2;



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

    }

    @OnClick({R.id.ll1, R.id.ll2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                image.setImageResource(R.drawable.room_a);
                ll1.setBackgroundResource(R.drawable.room_sel_shape);
                ll2.setBackgroundResource(R.drawable.room_nosel_shape);
                break;
            case R.id.ll2:
                image.setImageResource(R.drawable.room_b);
                ll1.setBackgroundResource(R.drawable.room_nosel_shape);
                ll2.setBackgroundResource(R.drawable.room_sel_shape);
                break;
        }
    }
}
