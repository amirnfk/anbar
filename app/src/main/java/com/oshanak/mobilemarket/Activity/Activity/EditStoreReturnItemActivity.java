package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.EditStoreReturnItemActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.NumericUpDownFragment;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductUnitData;
import com.oshanak.mobilemarket.Activity.DataStructure.StoreReturnItemData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Enum.StoreReturnItemReason;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_unit;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.List;

public class EditStoreReturnItemActivity extends BaseActivity implements OnTaskCompleted, NumericUpDownFragment.OnValueChanged
{
    private TextView tvItemID;
    private TextView tvItemName;
//    private EditText etAmount;
    private Spinner sUnit;
    private StoreReturnItemData storeReturnItemData;
    private EditStoreReturnItemActivityMode editStoreReturnItemActivityMode;
    private Spinner sReturnReason;
    private NumericUpDownFragment numericUpDownFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_store_return_item);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        tvItemID = findViewById(R.id.tvItemID);
        tvItemName = findViewById(R.id.tvItemName);
//        etAmount = findViewById(R.id.etAmount);
        sUnit = findViewById(R.id.sUnit);
        sReturnReason = findViewById(R.id.sReturnReason);
        numericUpDownFragment = (NumericUpDownFragment) getSupportFragmentManager().findFragmentById(R.id.numericUpDownFragment);

//        etAmount.addTextChangedListener(new ThousandSeparatorWatcher(etAmount));

        Intent intent = getIntent();
        storeReturnItemData = (StoreReturnItemData) intent.getSerializableExtra("storeReturnItemData");

        tvItemID.setText(String.valueOf( storeReturnItemData.getItemID()));
        tvItemName.setText(storeReturnItemData.getItemName());
//        etAmount.setText( ThousandSeparatorWatcher.addSeparator(storeReturnItemData.getAmount()));
        numericUpDownFragment.setValue(String.valueOf( storeReturnItemData.getAmount()));

        initUnitSpinner();
        Utility.initReturnReasonSpinner(this, sReturnReason);

        for(int i = 0; i < sReturnReason.getCount(); i++)
        {
            StoreReturnItemReason reason = ((StoreReturnItemReason)sReturnReason.getItemAtPosition(i));
            if(reason.getID() == storeReturnItemData.getReturnReasonID())
            {
                sReturnReason.setSelection(i);
                break;
            }
        }
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
    private void initUnitSpinner()
    {
        List<ProductUnitData> list = new ArrayList<>();

        ProductUnitData unitData = new ProductUnitData();
        unitData.setUnitName(storeReturnItemData.getPartUnit());
        unitData.setAmount("1");
        list.add(unitData);

        unitData = new ProductUnitData();
        unitData.setUnitName(storeReturnItemData.getWholeUnit());
        unitData.setAmount(String.valueOf( storeReturnItemData.getBoxQuantity()));
        list.add(unitData);

        row_unit adapter = new row_unit(this, list);
        sUnit.setAdapter(adapter);
        for(int i = 0; i < sUnit.getCount(); i++)
        {
            String unit = ((ProductUnitData)sUnit.getItemAtPosition(i)).getUnitName();
            if(unit.equals(storeReturnItemData.getSelectedUnit()))
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

        editStoreReturnItemActivityMode = EditStoreReturnItemActivityMode.BeforeUpdate;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateStoreReturnItem,this);

        service.addParam("ID", storeReturnItemData.getID());
        service.addParam("unit", ((ProductUnitData)sUnit.getSelectedItem()).getUnitName());
        service.addParam("amount", numericUpDownFragment.getValue());
        service.addParam("ReturnReason", ((StoreReturnItemReason)sReturnReason.getSelectedItem()).getID());

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
        else if(editStoreReturnItemActivityMode == EditStoreReturnItemActivityMode.BeforeUpdate)
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
            editStoreReturnItemActivityMode = EditStoreReturnItemActivityMode.AfterUpdate;
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
        StoreReturnItemReason storeReturnItemReason = (StoreReturnItemReason)sReturnReason.getSelectedItem();
        if(storeReturnItemReason == null || storeReturnItemReason.getID() == 0)
        {
            Toast.makeText(this, "علت برگشت را انتخاب نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed()
    {
        if(editStoreReturnItemActivityMode == EditStoreReturnItemActivityMode.AfterUpdate )
        {
            storeReturnItemData.setAmount(Integer.valueOf( numericUpDownFragment.getValue()));
            storeReturnItemData.setSelectedUnit(((ProductUnitData)sUnit.getSelectedItem()).getUnitName());
            storeReturnItemData.setReturnReasonID(((StoreReturnItemReason)sReturnReason.getSelectedItem()).getID());
            storeReturnItemData.setReturnReasonCode(((StoreReturnItemReason)sReturnReason.getSelectedItem()).getCode());
            storeReturnItemData.setReturnReasonName(((StoreReturnItemReason)sReturnReason.getSelectedItem()).getName());

            Intent returnIntent = new Intent();
            returnIntent.putExtra("storeReturnItemData", storeReturnItemData);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void OnValueChanged(String value) {

    }
}