package com.chance.mimorobot.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.manager.SerialControlManager;
import com.chance.mimorobot.widget.ColorPickerView;

import butterknife.BindView;

public class PickupColorActivity extends TitleBarActivity {
    @BindView(R.id.cpv)
    ColorPickerView cpv;
    @BindView(R.id.tv_text)
    TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cpv.setOnColorChangedListenner(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int r, int g, int b) {
                if(r==0 && g==0 && b==0){
                    return;
                }
                tvText.setText("你选择的灯光颜色\nR:"+r+",G:"+g+",B:"+b);
                if (r>g&&r>b){
                    SerialControlManager.newInstance().lightREDControl();
                }
                if (g>r&&g>b){
                    SerialControlManager.newInstance().lightGREENControl();
                }
                if (b>r&&b>g){
                    SerialControlManager.newInstance().lightBLUEControl();
                }
            }

            @Override
            public void onMoveColor(int r, int g, int b) {
                if(r==0 && g==0 && b==0){
                    return;
                }
//                tvText.setText("你选择的灯光颜色\nR:"+r+",G:"+g+",B:"+b);
//                tvText.setText("你选择的灯光颜色\n  R:"+r+" G,"+g+", B"+b);
            }
        });
    }


    @Override
    int getContentLayoutId() {
        return R.layout.activity_pickupcolor;
    }
}
