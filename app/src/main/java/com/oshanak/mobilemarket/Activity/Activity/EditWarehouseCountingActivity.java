package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.EditWarehouseCountingActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.NumericUpDownFragment;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseCountingDetailData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Service.Enum.PickingDeliverServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.PickingDeliverService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

public class EditWarehouseCountingActivity extends BaseActivity
        implements OnTaskCompleted, NumericUpDownFragment.OnValueChanged
{
    private TextView tvItemID;
    private TextView tvItemName;
    private WarehouseCountingDetailData warehouseCountingDetailData;
    private EditWarehouseCountingActivityMode editWarehouseCountingActivityMode;
    private NumericUpDownFragment numericUpDownFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_warehouse_counting);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        tvItemID = findViewById(R.id.tvItemID);
        tvItemName = findViewById(R.id.tvItemName);
        numericUpDownFragment = (NumericUpDownFragment) getSupportFragmentManager().findFragmentById(R.id.numericUpDownFragment);

        Intent intent = getIntent();
        warehouseCountingDetailData = (WarehouseCountingDetailData) intent.getSerializableExtra("warehouseCountingDetailData");

        tvItemID.setText(String.valueOf( warehouseCountingDetailData.getItemID()));
        tvItemName.setText(warehouseCountingDetailData.getItemName());
        numericUpDownFragment.setValue(String.valueOf( (int)warehouseCountingDetailData.getAmount()));
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize( findViewById(R.id.etFrgAmount),50);
        }
    }

    public void onClickExit(View view)
    {
        onBackPressed();
    }
    public void onClickConfirm(View view)
    {
        Update();
    }
    private void Update()
    {
        if (!validateData()) return;

        editWarehouseCountingActivityMode = EditWarehouseCountingActivityMode.BeforeUpdate;
        PickingDeliverService service = new PickingDeliverService(PickingDeliverServiceMode.UpdateCounting,this);

        service.addParam("ID", warehouseCountingDetailData.getID() );
        service.addParam("Amount", numericUpDownFragment.getValue() );

        service.listener = this;
        service.execute();
        startWait();
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
        else if(editWarehouseCountingActivityMode == EditWarehouseCountingActivityMode.BeforeUpdate)
        {
            if(!taskResult.isSuccessful)
            {
                if (!Utility.generalErrorOccurred(taskResult, this))
                {
                    Utility.simpleAlert(this, getString(R.string.update_do_not) + "\n" +
                            taskResult.message, DialogIcon.Error);
                }
                return;
            }
            Toast.makeText(this, getString( R.string.update_done),Toast.LENGTH_SHORT).show();
            editWarehouseCountingActivityMode = EditWarehouseCountingActivityMode.AfterUpdate;
            onBackPressed();
        }
        Utility.hideKeyboard(this);
    }
    private boolean validateData()
    {
        if (numericUpDownFragment.isEmpty())
        {
            Toast.makeText(this, "تعداد  كالا را وارد نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed()
    {
        if(editWarehouseCountingActivityMode == EditWarehouseCountingActivityMode.AfterUpdate )
        {
            warehouseCountingDetailData.setAmount(Integer.valueOf( numericUpDownFragment.getValue()));

            Intent returnIntent = new Intent();
            returnIntent.putExtra("warehouseCountingDetailData", warehouseCountingDetailData);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void OnValueChanged(String value)
    {
    }
}