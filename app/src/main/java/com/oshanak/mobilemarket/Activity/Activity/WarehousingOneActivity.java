package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.WarehousingOneActivityMode;
import com.oshanak.mobilemarket.Activity.Activity.Enum.WarehousingTwoActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

public class WarehousingOneActivity extends BaseActivity implements
        OnTaskCompleted {

    private WarehousingOneActivityMode warehousingOneActivityMode;
    private TextView tvStartWarehousing;
    private TextView tvContinueWarehousing;
    private boolean isOpenWarehousingExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehousing_one);
        ////////////

        tvStartWarehousing = findViewById(R.id.tvStartWarehousing);
        tvContinueWarehousing = findViewById(R.id.tvContinueWarehousing);

        isOpenWarehousingExist();
    }
    public void onStartWarehousing(View view)
    {
        if(isOpenWarehousingExist)
        {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("شمارش انبارگرداني قبلي تكميل نشده است." + "\n" +
                    "در صورت شروع شمارش جديد اطلاعات قبلي حذف مي گردد.");

            dlgAlert.setTitle("شمارش جديد");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("حذف گردد",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(WarehousingOneActivity.this, WarehousingTwoActivity.class);
                            intent.putExtra("warehousingTwoActivityMode", WarehousingTwoActivityMode.BeforeStartWarehousing);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivityForResult(intent,1);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    });
            dlgAlert.setNegativeButton("انصراف",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getBaseContext(),"انصراف",Toast.LENGTH_SHORT).show();
                        }
                    });
            dlgAlert.setIcon(R.drawable.question128);
            dlgAlert.create().show();
        }
        else
        {
            Intent intent = new Intent(this, WarehousingTwoActivity.class);
            intent.putExtra("warehousingTwoActivityMode", WarehousingTwoActivityMode.BeforeStartWarehousing);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
    public void onContinueWarehousing(View view)
    {
        if(!isOpenWarehousingExist)
        {
            Toast.makeText(this, "انبارگرداني باز موجود نيست.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, WarehousingTwoActivity.class);
        intent.putExtra("warehousingTwoActivityMode", WarehousingTwoActivityMode.BeforeContinueWarehousing);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent,1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void onExitClick(View view)
    {
        onBackPressed();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize(tvStartWarehousing,20);
            Utility.setFontUnderline(tvStartWarehousing);
            Utility.setFontBold(tvStartWarehousing);

            Utility.increaseTextSize(tvContinueWarehousing,20);
            Utility.setFontUnderline(tvContinueWarehousing);
            Utility.setFontBold(tvContinueWarehousing);

            TextView tvExit = findViewById(R.id.tvExit);
            Utility.setFontUnderline(tvExit);
        }
    }
    private void isOpenWarehousingExist()
    {
        warehousingOneActivityMode = WarehousingOneActivityMode.BeforeCheckForOpenWarehousing;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.CheckForOpenWarehousing, this);

        task.addParam("StoreID", GlobalData.getStoreID());
        task.addParam("deviceID", Utility.getMACAddress(this));

        task.listener = this;
        task.execute();
        startWait();
    }
    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if(warehousingOneActivityMode == WarehousingOneActivityMode.BeforeCheckForOpenWarehousing )
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                Utility.simpleAlert(this,getString(R.string.general_error) + "\n" + taskResult.message,
                        "", DialogIcon.Error, onFinishClick);
                return;
            }
            else if (!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this,getString(R.string.general_error) + "\n" + taskResult.message,
                        "", DialogIcon.Error, onFinishClick);
                return;
            }
            isOpenWarehousingExist = (boolean) taskResult.dataStructure;
            if(!isOpenWarehousingExist)
            {
                tvContinueWarehousing.setTextColor(Color.GRAY);
            }
            else
            {
                tvContinueWarehousing.setTextColor(Color.BLACK);
            }

            Utility.hideKeyboard(this);
            warehousingOneActivityMode = WarehousingOneActivityMode.AfterCheckForOpenWarehousing;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if(data == null) return;
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            isOpenWarehousingExist();
        }
    }
    DialogInterface.OnClickListener onFinishClick= new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            onBackPressed();
        }
    };
}
