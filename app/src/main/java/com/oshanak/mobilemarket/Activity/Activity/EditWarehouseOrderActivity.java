package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.EditWarehouseOrderActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductUnitData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseOrderData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_unit;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.List;

public class EditWarehouseOrderActivity extends BaseActivity implements OnTaskCompleted
{
    private TextView tvItemID;
    private TextView tvItemName;
    private EditText etAmount;
    private Spinner sUnit;
    private WarehouseOrderData warehouseOrderData;
    private EditWarehouseOrderActivityMode editWarehouseOrderActivityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_warehouse_order);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        tvItemID = findViewById(R.id.tvItemID);
        tvItemName = findViewById(R.id.tvItemName);
        etAmount = findViewById(R.id.etAmount);
        sUnit = findViewById(R.id.sUnit);

        Utility.enableViews(this,false, sUnit);
        etAmount.addTextChangedListener(new ThousandSeparatorWatcher(etAmount));

        Intent intent = getIntent();
        warehouseOrderData = (WarehouseOrderData) intent.getSerializableExtra("warehouseOrderData");

        tvItemID.setText(String.valueOf( warehouseOrderData.getItemID()));
        tvItemName.setText(warehouseOrderData.getItemName());
        etAmount.setText( ThousandSeparatorWatcher.addSeparator(warehouseOrderData.getAmount()));

        initUnitSpinner();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize(etAmount,50);
        }
    }
    private void initUnitSpinner()
    {
        List<ProductUnitData> list = new ArrayList<>();

        ProductUnitData unitData = new ProductUnitData();
        unitData.setUnitName(warehouseOrderData.getPartUnit());
        unitData.setAmount("1");
        list.add(unitData);

        unitData = new ProductUnitData();
        unitData.setUnitName(warehouseOrderData.getWholeUnit());
        unitData.setAmount(String.valueOf( warehouseOrderData.getBoxQuantity()));
        list.add(unitData);

        row_unit adapter = new row_unit(this, list);
        sUnit.setAdapter(adapter);
        for(int i = 0; i < sUnit.getCount(); i++)
        {
            String unit = ((ProductUnitData)sUnit.getItemAtPosition(i)).getUnitName();
            if(unit.equals("CAR"))
            {
                sUnit.setSelection(i);
                break;
            }
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

        editWarehouseOrderActivityMode = EditWarehouseOrderActivityMode.BeforeUpdate;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateWarehouseOrder,this);

        service.addParam("ID", warehouseOrderData.getID());
        service.addParam("amount", etAmount.getText().toString());

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
        else if(editWarehouseOrderActivityMode == EditWarehouseOrderActivityMode.BeforeUpdate)
        {
            if(!taskResult.isSuccessful)
            {
                if (!Utility.generalErrorOccurred(taskResult, this))
                {
                    Utility.simpleAlert(this, getString(R.string.update_do_not), DialogIcon.Error);
                }
                return;
            }
            Toast.makeText(this, getString( R.string.update_done),Toast.LENGTH_SHORT).show();
            editWarehouseOrderActivityMode = EditWarehouseOrderActivityMode.AfterUpdate;
            onBackPressed();
        }
        Utility.hideKeyboard(this);
    }
    private boolean validateData()
    {
        if (Utility.editTextIsEmpty(etAmount, "تعداد كالا را وارد نماييد")) return false;
        return true;
    }
    public void onClickChangeCount(View view)
    {
        double d = (etAmount.getText().toString().trim().equals("") ? 1 :
                Double.parseDouble( ThousandSeparatorWatcher.removeSeparator(etAmount.getText().toString().trim())));

        int count = (int)d;
        if(view.getId() == findViewById(R.id.ibMinus).getId())
        {
            if(count <= 0) return;
            count--;
        }
        else
        {
            if(count >= 999999) return;
            count++;
        }
        etAmount.setText(String.valueOf(count));
        etAmount.clearFocus();
        Utility.hideKeyboard(this);
    }
    @Override
    public void onBackPressed()
    {
        if(editWarehouseOrderActivityMode == EditWarehouseOrderActivityMode.AfterUpdate )
        {
            warehouseOrderData.setAmount(Integer.valueOf( etAmount.getText().toString()));

            Intent returnIntent = new Intent();
            returnIntent.putExtra("warehouseOrderData", warehouseOrderData);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}