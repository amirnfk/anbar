package com.oshanak.mobilemarket.Activity.Activity;

import android.os.Bundle;
import android.view.View;

import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.R;

public class ColleaguePresentationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colleague_presentation);
        /////////////////////
        if (Utility.restartAppIfNeed(this)) return;
    }
    public void onFingerPrint(View view)
    {}
}