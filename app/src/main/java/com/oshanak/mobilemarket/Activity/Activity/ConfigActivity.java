package com.oshanak.mobilemarket.Activity.Activity;

//import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.Enum.ServiceUrlType;
import com.oshanak.mobilemarket.Activity.LocalDB.DBHandler;
import com.oshanak.mobilemarket.Activity.LocalDB.Param;
import com.oshanak.mobilemarket.R;

public class ConfigActivity extends BaseActivity {

    private RadioButton rOperational;
    private RadioButton rPilot;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        ////////////////////
//        if (Utility.restartAppIfNeed(this)) return;

        rOperational = findViewById(R.id.rOperational);
        rPilot = findViewById(R.id.rPilot);

        DBHandler dbHandler = new DBHandler(this);
        String serviceUrl = dbHandler.getParamValue(Param.ServiceUrlType);
        if(serviceUrl.equals("") || serviceUrl.equals("0") ||
                serviceUrl.equals(ServiceUrlType.PhoneDelivery_Operational.getCode()) ||
                serviceUrl.equals(ServiceUrlType.Picking_Operational.getCode()) ||
                serviceUrl.equals(ServiceUrlType.StoreHandheld_Operational.getCode()) ||
                serviceUrl.equals(ServiceUrlType.Competitor_Operational.getCode()))
        {
            rOperational.setChecked(true);
        }
        else if(serviceUrl.equals(ServiceUrlType.PhoneDelivery_Pilot.getCode()) ||
                serviceUrl.equals(ServiceUrlType.Picking_Pilot.getCode()) ||
                serviceUrl.equals(ServiceUrlType.StoreHandheld_Pilot.getCode()) ||
                serviceUrl.equals(ServiceUrlType.Competitor_Pilot.getCode()))
        {
            rPilot.setChecked(true);
        }
    }
    public void onConfirm(View view)
    {
        if(!rOperational.isChecked() && !rPilot.isChecked())
        {
            Toast.makeText(this,"نوع سرويس عملياتي يا آزمايشي را مشخص نماييد.",Toast.LENGTH_SHORT).show();
            return;
        }

        DBHandler dbHandler = new DBHandler(this);
//        dbHandler.setParamValue(Param.ServiceUrlType, rOperational.isChecked() ?
//                                                        ServiceUrlType.Operational.getCode() :
//                                                        ServiceUrlType.Pilot.getCode());
        ServiceUrlType urlType = ServiceUrlType.Unknown;
        if(rOperational.isChecked())
        {
            if(Utility.applicationMode == ApplicationMode.PhoneDelivery)
            {
                urlType = ServiceUrlType.PhoneDelivery_Operational;
            }
            else if(Utility.applicationMode == ApplicationMode.PickingWarehouse)
            {
                urlType = ServiceUrlType.Picking_Operational;
            }
            else if(Utility.applicationMode == ApplicationMode.StoreHandheld)
            {
                urlType = ServiceUrlType.StoreHandheld_Operational;
            }
            else if(Utility.applicationMode == ApplicationMode.Competitor)
            {
                urlType = ServiceUrlType.Competitor_Operational;
            }
        }
        else if(rPilot.isChecked())
        {
            if(Utility.applicationMode == ApplicationMode.PhoneDelivery)
            {
                urlType = ServiceUrlType.PhoneDelivery_Pilot;
            }
            else if(Utility.applicationMode == ApplicationMode.PickingWarehouse)
            {
                urlType = ServiceUrlType.Picking_Pilot;
            }
            else if(Utility.applicationMode == ApplicationMode.StoreHandheld)
            {
                urlType = ServiceUrlType.StoreHandheld_Pilot;
            }
            else if(Utility.applicationMode == ApplicationMode.Competitor)
            {
                urlType = ServiceUrlType.Competitor_Pilot;
            }
        }
        dbHandler.setParamValue(Param.ServiceUrlType, urlType.getCode());

        Utility.hideKeyboard(this);
        onBackPressed();
    }
}
