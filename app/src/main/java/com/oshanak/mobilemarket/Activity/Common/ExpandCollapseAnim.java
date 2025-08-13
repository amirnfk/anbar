package com.oshanak.mobilemarket.Activity.Common;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oshanak.mobilemarket.R;

public class ExpandCollapseAnim
{
//    private LinearLayout lArea;
    private int originalHeight;
    private boolean isCollapsed = false;
    private final int duration = 300;
    private LinearLayout lArea;
    private ImageButton bExpand;
    private TextView tvMoreParam;

public ExpandCollapseAnim(final ImageButton bExpand, final TextView tvMoreParam, final LinearLayout lArea, boolean collapsed)
{
    this.lArea = lArea;
    this.bExpand = bExpand;
    this.tvMoreParam = tvMoreParam;

//        Utility.increaseTextSize(tvMoreParam, -10);
    originalHeight = lArea.getHeight();

    bExpand.setOnClickListener(onClickListener);
    tvMoreParam.setOnClickListener(onClickListener);

    if(collapsed)
    {
        collapse(lArea, duration, 0);
        bExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
        isCollapsed = true;
    }
}
    private View.OnClickListener onClickListener =
            new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(isCollapsed)
                    {
                        expand(lArea,duration,originalHeight);
                        bExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        isCollapsed = false;
                        tvMoreParam.setText("کمتر");
                    }
                    else
                    {
                        collapse(lArea, duration, 0);
                        bExpand.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                        isCollapsed = true;
                        tvMoreParam.setText("بیشتر");
                    }
                }
            };

    public void expand(final View v, int duration, int targetHeight)
    {
        int prevHeight  = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
    //SOURCE: https://stackoverflow.com/questions/4946295/android-expand-collapse-animation
    public void collapse(final View v, int duration, int targetHeight)
    {
        int prevHeight  = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
}
