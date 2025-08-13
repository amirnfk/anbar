package com.oshanak.mobilemarket.Activity.Common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.oshanak.mobilemarket.R;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        int textColor = getTextColors().getDefaultColor();
//        setTextColor(getResources().getColor(R.color.HaftOrange)); // your stroke's color
//        getPaint().setStrokeWidth(10);
//        getPaint().setStyle(Paint.Style.STROKE);
//        super.onDraw(canvas);
//        setTextColor(textColor);
//        getPaint().setStrokeWidth(0);
//        getPaint().setStyle(Paint.Style.FILL);
//        super.onDraw(canvas);
        super.onDraw(canvas);
        drawStrikeThroughPaint(canvas);
    }

    public void drawStrikeThroughPaint(Canvas canvas) {
        Paint paint=new Paint();
        paint.setColor(getResources().getColor(R.color.Black));
        paint.setStrokeWidth(5);
        canvas.drawLine(0, getMeasuredHeight() / 1, getMeasuredWidth(), 1/getMeasuredHeight() ,paint);
    }
}
