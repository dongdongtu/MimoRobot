package com.chance.mimorobot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.constant.Globle;
import com.chance.mimorobot.manager.CZIdcardApiManager;
import com.chance.mimorobot.manager.PrintManager;
import com.chance.mimorobot.model.BaseResponseModel;
import com.chance.mimorobot.model.IDCardData;
import com.chance.mimorobot.model.IDCardModel;
import com.chance.mimorobot.retrofit.ApiManager;
import com.chance.mimorobot.utils.ImageTools;
import com.hdos.usbdevice.publicSecurityIDCardLib;
import com.hnj.dp_nusblist.USBFactory;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class IdentifyActivity extends TitleBarActivity {

    private String TAG = IdentifyActivity.class.getSimpleName();
    private publicSecurityIDCardLib iDCardDevice;

    private int state = -1;

    private USBFactory usbfactory;

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_nature)
    TextView tvNature;
    @BindView(R.id.tv_idnum)
    TextView tvIdnum;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_idcard)
    TextView tvIdcard;
    @BindView(R.id.iv_print)
    ImageView ivPrint;

    private Bitmap bitmap;
    byte[] BmpFile = new byte[38556];

    public static boolean isStop = false;
    private int ret = 0;

    String pkName;

    private PrintManager printManager;
    public static boolean isStart = false;

    @Override
    int getContentLayoutId() {
        return R.layout.activity_identify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        pkName = this.getPackageName();
        iDCardDevice = new publicSecurityIDCardLib(IdentifyActivity.this);
        printManager=PrintManager.newInstance(getApplicationContext());
        usbfactory = PrintManager.newInstance(getApplicationContext()).usbfactory;
        if(getactivit!=null)
        {
            getactivit.onbackactivity(this);
        }
    }

    @OnClick(R.id.iv_print)
    public void onViewClicked() {
        Log.e(TAG,"iv_print");
        usbfactory.PrintPicture(bitmap,260,2);
        usbfactory.PrintText(tvName.getText().toString(), 1, 1, 30);
        usbfactory.PrintText(tvGender.getText().toString(), 1, 1, 30);
        usbfactory.PrintText(tvNature.getText().toString(), 1, 1, 30);
        usbfactory.PrintText(tvIdnum.getText().toString(), 1, 1, 30);
        usbfactory.PrintText(tvAddress.getText().toString(), 1, 1, 30);
        usbfactory.PrintText(tvDate.getText().toString(), 1, 1, 30);
        usbfactory.PrintText(" 与原件一致", 2, 2, 40);
        usbfactory.GapCut();
    }


    @Override
    public void onResume() {
        super.onResume();
        getSAMStatus();
        Flowable.timer(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (state == 1) {
                    isStart = true;
                    getIDDate();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStart = false;
        isStop=true;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isStop = true;
        if(iDCardDevice != null)  iDCardDevice.closeDevice();
    }
    /**
     * 获取身份证阅读器状态
     */
    private void getSAMStatus() {
        try {
            ret = iDCardDevice.getSAMStatus();
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (ret == 0x90) {
            state = 1;
        } else {
            Flowable.timer(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iDCardDevice = new publicSecurityIDCardLib(IdentifyActivity.this);
                            getSAMStatus();
                        }
                    });
                }
            });
        }
    }


    public void getIDDate() {

        String[] decodeInfo = new String[13];
        Arrays.fill(BmpFile, (byte) 0x00);
        if (iDCardDevice == null) {
            iDCardDevice = new publicSecurityIDCardLib(IdentifyActivity.this);
        }
//				ret =iDCardDevice.readBaseMsgToStr(pkName,BmpFile,FpMsg,name,sex,nation, birth, address, IDNo, Department,
//						EffectDate, ExpireDate);
        try {
            ret = iDCardDevice.PICC_Reader_ForeignerIDCard(decodeInfo, BmpFile, pkName);

            if (ret < 0) {
                Log.e("TAG", "读卡错误，原因：" + decodeInfo[12] + isStop);
                if (!isStop) {
                    getSAMStatus();
                    Flowable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            getIDDate();
                        }
                    });
                } else {
                    isStop = false;
                    return;
                }
//                return;
            } else if (ret == 0) {
                isStart = false;
                isStop = false;
                int[] colors = iDCardDevice.convertByteToColor(BmpFile);
//                showString("读取成功");
//                        showString("中华人民共和国居民身份证" );
//                        showString("姓名：" + decodeInfo[0] + "\n" + "性别：" + decodeInfo[1] + "\n" + "民族：" + decodeInfo[2] + "\n"
//                                + "出生日期：" + decodeInfo[3] + "\n" + "地址：" + decodeInfo[4] + "\n" + "身份号码：" + decodeInfo[5] + "\n"
//                                + "签发机关：" + decodeInfo[6] + "\n" + "有效期限：" + decodeInfo[7] + "-" + decodeInfo[8] + "\n"
//                                + decodeInfo[9] + "\n");

                Bitmap bm = Bitmap.createBitmap(colors, 102, 126, Bitmap.Config.ARGB_8888);

                bitmap = Bitmap.createScaledBitmap(bm,
                        (int) (102 * 2), (int) (126 * 2), false);

                ivPrint.setVisibility(View.VISIBLE);
                tvName.setText("姓名：" + decodeInfo[0]);
                tvGender.setText("性别：" + decodeInfo[1]);
                tvNature.setText("民族：" + decodeInfo[2]);
                tvIdnum.setText("身份证号码：" + decodeInfo[5]);
                tvAddress.setText("签发机关：" + decodeInfo[6]);
                tvDate.setText("有效期限：" + decodeInfo[7] + "-" + decodeInfo[8]);
                ivPhoto.setImageBitmap(bitmap);
                IDCardModel idCardModel=new IDCardModel();
                idCardModel.setRobotNo(Globle.robotId);
                idCardModel.setName(decodeInfo[0].trim());
                idCardModel.setSex(decodeInfo[1].trim());
                idCardModel.setNation(decodeInfo[2].trim());
                idCardModel.setBirthday(decodeInfo[3].trim());
                idCardModel.setVisaAuthority(decodeInfo[6].trim());
                idCardModel.setAddress( decodeInfo[4].trim());
                idCardModel.setTermOfValidityB( decodeInfo[7].trim());
                idCardModel.setTermOfValidityE( decodeInfo[8].trim());
                idCardModel.setIDCard(decodeInfo[5].trim());
                idCardModel.setPicFromIDCard(ImageTools.convertIconToString(bitmap));
                ApiManager.getInstance().getRobotServer().postIDcard(idCardModel).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResponseModel>() {
                    @Override
                    public void accept(BaseResponseModel baseResponseModel) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


            } else if (ret == 1) {
                isStop = false;
                isStart = false;
                int[] colors = iDCardDevice.convertByteToColor(BmpFile);
                Bitmap bm = Bitmap.createBitmap(colors, 102, 126, Bitmap.Config.ARGB_8888);
                Bitmap bm1 = Bitmap.createScaledBitmap(bm,
                        (int) (102 * 2), (int) (126 * 2), false); //这里你可以自定义它的大小
            } else {
                isStop = false;
                getSAMStatus();
                Flowable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        getIDDate();
                    }
                });
            }


        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    private static IdentifyActivity.onGetActivit getactivit = null;

    public static void setOnGetActivit(IdentifyActivity.onGetActivit mactivit){
        getactivit = mactivit;
    }

    public interface onGetActivit {
        void onbackactivity(Activity at);
    }

}
