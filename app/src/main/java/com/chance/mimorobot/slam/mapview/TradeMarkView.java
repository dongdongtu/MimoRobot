package com.chance.mimorobot.slam.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.chance.mimorobot.R;


public class TradeMarkView extends View {
    private Bitmap mBitmap;
    private Paint mPaint;

    public TradeMarkView(Context context) {
        super(context);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 10, 10, mPaint);
    }
}
