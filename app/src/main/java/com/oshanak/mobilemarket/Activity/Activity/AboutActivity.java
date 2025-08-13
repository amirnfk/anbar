package com.oshanak.mobilemarket.Activity.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.AppVersion;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.R;

public class AboutActivity extends BaseActivity {
//test for commiting
    private TextView tvVersion;
    //test test 14000416


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //////////////////////////////

        this.setFinishOnTouchOutside(false);

        tvVersion = findViewById(R.id.tvVersion);
        TextView tvApp = findViewById(R.id.tvApp);
        TextView tvIP = findViewById(R.id.tvIP);

        String CurrentVersionName = AppVersion.getCurrentVersionName(this);
        tvVersion.setText("نسخه: " + CurrentVersionName);
        tvIP.setText("آی پی: " + Utility.getIPAddress(true));

        switch (Utility.applicationMode)
        {
            case PhoneDelivery:
                tvApp.setText("فروش و توزيع تلفني اوشانك");
                break;
            case PickingWarehouse:
                tvApp.setText("جمع آوري كالا در انبار");
                break;
            case StoreHandheld:
                tvApp.setText("سيستم فروشگاهي");
                break;
            case Competitor:
                tvApp.setText("سيستم شناسایی رُقبا");
                break;
        }
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize(tvVersion, 15);
            Utility.increaseTextSize(findViewById(R.id.tvIP), 15);
        }
    }
    public void onCancelClick(View view)
    {
        onBackPressed();
    }
}
