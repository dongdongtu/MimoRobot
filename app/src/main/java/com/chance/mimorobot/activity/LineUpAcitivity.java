package com.chance.mimorobot.activity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.manager.PrintManager;
import com.chance.mimorobot.model.BaseResponseModel;
import com.chance.mimorobot.model.LineUpRequestModel;
import com.chance.mimorobot.retrofit.ApiManager;
import com.hnj.dp_nusblist.USBFactory;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LineUpAcitivity extends TitleBarActivity {

    private String TAG=LineUpAcitivity.class.getSimpleName();

    private USBFactory usbfactory;
    private PrintManager printManager;
    @BindView(R.id.tv_fund)
    TextView tvFund;
    @BindView(R.id.iv_fund)
    ImageView ivFund;
    @BindView(R.id.tv_insurrance)
    TextView tvInsurrance;
    @BindView(R.id.iv_insurrance)
    ImageView ivInsurrance;
    @BindView(R.id.tv_risgester)
    TextView tvRisgester;
    @BindView(R.id.iv_risgester)
    ImageView ivRisgester;
    private Date calendarDate;
    private int num1,num2,num3=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printManager=PrintManager.newInstance(getApplicationContext());
        usbfactory = PrintManager.newInstance(getApplicationContext()).usbfactory;
        // 初始话一个Time对象，也可以写成:Time time = new Time("GTM+8"),即加上时区
        Calendar calendar = Calendar.getInstance();
        calendarDate = calendar.getTime();
    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_lineup;
    }

    @OnClick({R.id.iv_fund, R.id.iv_insurrance, R.id.iv_risgester})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_fund:
                num1++;
                usbfactory.PrintText("公积金缴费", 2, 1, 30);
                usbfactory.PrintText("排号："+num1, 2, 2, 40);
                usbfactory.PrintText("日期："+DateFormat.format("yyyy-MM-dd kk:mm:ss", calendarDate).toString(), 1, 1, 30);
                usbfactory.GapCut();
                tvFund.setText("前面排队人数："+num1);
                uploadNum("公积金缴费",num1);
                break;
            case R.id.iv_insurrance:
                num2++;
                usbfactory.PrintText("保险缴费", 2, 1, 30);
                usbfactory.PrintText("排号："+num2, 2, 2, 40);
                usbfactory.PrintText("日期："+DateFormat.format("yyyy-MM-dd kk:mm:ss", calendarDate).toString(), 1, 1, 30);
                usbfactory.GapCut();
                tvInsurrance.setText("前面排队人数："+num2);
                uploadNum("保险缴费",num2);
                break;
            case R.id.iv_risgester:
                num3++;
                usbfactory.PrintText("工商注册", 2, 1, 30);
                usbfactory.PrintText("排号："+num3, 2, 2, 40);
                usbfactory.PrintText("日期："+DateFormat.format("yyyy-MM-dd kk:mm:ss", calendarDate).toString(), 1, 1, 30);
                usbfactory.GapCut();

                tvRisgester.setText("前面排队人数："+num3);
                uploadNum("工商注册",num3);
                break;
        }
    }


    public void uploadNum(String type,int num){
        LineUpRequestModel lineUpRequestModel=new LineUpRequestModel();
        lineUpRequestModel.setNumber(num);
        lineUpRequestModel.setRobotno(Globle.robotId);
        lineUpRequestModel.setTypename(type);
        ApiManager.getInstance().getRobotServer().uploadLineUp(lineUpRequestModel).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResponseModel>() {
            @Override
            public void accept(BaseResponseModel baseResponseModel) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }
}
