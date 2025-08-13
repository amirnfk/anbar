package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.oshanak.mobilemarket.Activity.Activity.Enum.SendDirectReceiveToSAPActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DirectReceiveHeaderData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

public class SendDirectReceiveToSAPActivity extends BaseActivity implements OnTaskCompleted
{

    private EditText etInvoiceNo1;
    private EditText etInvoiceNo2;
    private DirectReceiveHeaderData directReceiveHeaderData;
    private SendDirectReceiveToSAPActivityMode sendDirectReceiveToSAPActivityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_direct_receive_to_sapactivity);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        etInvoiceNo1 = findViewById(R.id.etInvoiceNo1);
        etInvoiceNo2 = findViewById(R.id.etInvoiceNo2);

        Intent intent = getIntent();
        directReceiveHeaderData = (DirectReceiveHeaderData) intent.getSerializableExtra("directReceiveHeaderData");
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize(etInvoiceNo1,20);
            Utility.increaseTextSize(etInvoiceNo2,20);
        }
    }
    public void onClickExit(View view)
    {
        onBackPressed();
    }
    public void onClickConfirm(View view)
    {
        send();
    }
    private void send()
    {
        if(!validateData()) return;

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آيا اطلاعات به مركز ارسال گردد؟");
        dlgAlert.setTitle("ارسال اطلاعات");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("بله",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        sendDirectReceiveToSAPActivityMode = SendDirectReceiveToSAPActivityMode.BeforeSendData;
                        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.SendDirectReceiveToSAP
                                ,SendDirectReceiveToSAPActivity.this);

                        service.addParam("headerID", directReceiveHeaderData.getID());
                        service.addParam("invoiceNo", etInvoiceNo1.getText().toString().trim()
                                + "-" + etInvoiceNo2.getText().toString().trim());

                        service.listener = SendDirectReceiveToSAPActivity.this;
                        service.execute();
                        startWait();
                    }
                });
        dlgAlert.setNegativeButton("خير",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });

        dlgAlert.setIcon(R.drawable.question128);
        dlgAlert.create().show();

    }
    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this))
        {
            return;
        }
        else if(sendDirectReceiveToSAPActivityMode == SendDirectReceiveToSAPActivityMode.BeforeSendData)
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            if (!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, "خطا در ارسال اطلاعات به مركز." + "\n" +
                        taskResult.message, DialogIcon.Error);
                return;
            }

            Utility.hideKeyboard(this);
            sendDirectReceiveToSAPActivityMode = SendDirectReceiveToSAPActivityMode.AfterSendData;
            Utility.simpleAlert(this, "ارسال اطلاعات به مركز انجام گرديد." ,"", DialogIcon.Info, onFinishClick);
        }

        Utility.hideKeyboard(this);
    }
    DialogInterface.OnClickListener onFinishClick= new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            onBackPressed();
        }
    };
    private boolean validateData()
    {
        if (Utility.editTextIsEmpty(etInvoiceNo1, "شماره فاكتور را وارد نماييد")) return false;
        if (Utility.editTextIsEmpty(etInvoiceNo2, "شماره فاكتور را وارد نماييد")) return false;
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if(sendDirectReceiveToSAPActivityMode == SendDirectReceiveToSAPActivityMode.AfterSendData )
        {
            Intent returnIntent = new Intent();
//            returnIntent.putExtra("directReceiveDetailData", directReceiveDetailData);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}