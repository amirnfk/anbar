package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.EditWarehousingItemActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.NumericUpDownFragment;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingItemUMdata;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_unit_warehousing;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.List;

public class EditWarehousingItemActivity extends BaseActivity implements OnTaskCompleted, NumericUpDownFragment.OnValueChanged
{
    private TextView tvItemID;
    private TextView tvItemName;
    private Spinner sUnit;
    private WarehousingDetailData warehousingDetailData;
    private EditWarehousingItemActivityMode editWarehousingItemActivityMode;
    private NumericUpDownFragment numCountValue;
    private NumericUpDownFragment numLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_warehousing_item);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        tvItemID = findViewById(R.id.tvItemID);
        tvItemName = findViewById(R.id.tvItemName);
//        etAmount = findViewById(R.id.etAmount);
        sUnit = findViewById(R.id.sUnit);
        numCountValue = (NumericUpDownFragment) getSupportFragmentManager().findFragmentById(R.id.numCountValue);
        numLocation = (NumericUpDownFragment) getSupportFragmentManager().findFragmentById(R.id.numLocation);

        Intent intent = getIntent();
        warehousingDetailData = (WarehousingDetailData) intent.getSerializableExtra("warehousingDetailData");

        tvItemID.setText(String.valueOf( warehousingDetailData.getItemID()));
        tvItemName.setText(warehousingDetailData.getItemName());
        numCountValue.setValue(String.valueOf( warehousingDetailData.getCountValue()));
        numLocation.setValue(String.valueOf( warehousingDetailData.getLocation()));

        initUnitSpinner();
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
//        List<WarehousingItemUMdata> unitDataList = warehousingProductContainer.getWarehousingItemUMdataList();
//
//        row_unit_warehousing adapter = new row_unit_warehousing(this, unitDataList);
//        sUnit.setAdapter(adapter);

        List<WarehousingItemUMdata> list = new ArrayList<>();

        WarehousingItemUMdata unitData;
        if(!warehousingDetailData.getPartUnit().equals(""))
        {
            unitData = new WarehousingItemUMdata();
            unitData.setUMID(warehousingDetailData.getPartUnit());
            unitData.setMultipleConvert(1);
            unitData.setItemID(warehousingDetailData.getItemID());
            list.add(unitData);
        }

        if(!warehousingDetailData.getWholeUnit().equals(""))
        {
            unitData = new WarehousingItemUMdata();
            unitData.setUMID(warehousingDetailData.getWholeUnit());
            unitData.setMultipleConvert(warehousingDetailData.getMultipleConvert());
            unitData.setItemID(warehousingDetailData.getItemID());
            list.add(unitData);
        }

        row_unit_warehousing adapter = new row_unit_warehousing(this, list);
        sUnit.setAdapter(adapter);
        for(int i = 0; i < sUnit.getCount(); i++)
        {
            String unit = ((WarehousingItemUMdata)sUnit.getItemAtPosition(i)).getUMID();
            if(unit.equals("PC") || unit.equals("GR"))
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
    private int getPcAmount()
    {
        int count = Integer.valueOf( numCountValue.getValue());
        int boxUnit = Integer.valueOf (((WarehousingItemUMdata)sUnit.getSelectedItem()).getMultipleConvert());
        int amount = count * boxUnit;
        return amount;
    }
    private void Update()
    {
        if (!validateData()) return;

        editWarehousingItemActivityMode = EditWarehousingItemActivityMode.BeforeUpdate;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateWarehousingItem,this);

        service.addParam("ID", warehousingDetailData.getID());
        service.addParam("CountValue", getPcAmount());
        service.addParam("Location", numLocation.getValue());
        service.addParam("HandheldIP", Utility.getIPAddress(true));
        service.addParam("HandheldID", Utility.getMACAddress(this));
        service.addParam("CheckIfAlreadyCounted", 0);

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
        else if(editWarehousingItemActivityMode == EditWarehousingItemActivityMode.BeforeUpdate)
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
            editWarehousingItemActivityMode = EditWarehousingItemActivityMode.AfterUpdate;
            onBackPressed();
        }
        Utility.hideKeyboard(this);
    }
    private boolean validateData()
    {
        if (numCountValue.isEmpty())
        {
            Toast.makeText(this, "تعداد كالا را وارد نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (numLocation.isEmpty())
        {
            Toast.makeText(this, "موقعيت كالا را وارد نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if(editWarehousingItemActivityMode == EditWarehousingItemActivityMode.AfterUpdate )
        {
            warehousingDetailData.setCountValue(Integer.valueOf( getPcAmount()));
            warehousingDetailData.setLocation(Integer.valueOf( numLocation.getValue()));
//            warehousingDetailData.setHandheldIP(Utility.getIPAddress(true));
//            warehousingDetailData.setHandheldID(Utility.getMACAddress(null));
            warehousingDetailData.setCreateDate(Utility.getCurrentPersianDate());
            warehousingDetailData.setCountingDone(true);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("warehousingDetailData", warehousingDetailData);
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