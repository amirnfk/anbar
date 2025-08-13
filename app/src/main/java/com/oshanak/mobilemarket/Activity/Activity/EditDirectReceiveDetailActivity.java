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

import com.oshanak.mobilemarket.Activity.Activity.Enum.EditDirectReceiveDetailActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DirectReceiveDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductUnitData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_unit;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

public class EditDirectReceiveDetailActivity extends BaseActivity implements OnTaskCompleted
{

    private TextView tvItemID;
    private TextView tvItemName;
    private TextView tvBoxUnit;
    private EditText etAmount;
    private Spinner sUnit;
    private DirectReceiveDetailData directReceiveDetailData;
    private EditDirectReceiveDetailActivityMode editDirectReceiveDetailActivityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_direct_receive_detail);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        tvItemID = findViewById(R.id.tvItemID);
        tvItemName = findViewById(R.id.tvItemName);
        tvBoxUnit = findViewById(R.id.tvBoxUnit);
        etAmount = findViewById(R.id.etAmount);
        sUnit = findViewById(R.id.sUnit);

        etAmount.addTextChangedListener(new ThousandSeparatorWatcher(etAmount));

        Intent intent = getIntent();
        directReceiveDetailData = (DirectReceiveDetailData) intent.getSerializableExtra("directReceiveDetailData");

        tvItemID.setText(String.valueOf( directReceiveDetailData.getItemID()));
        tvItemName.setText(directReceiveDetailData.getItemName());
        tvBoxUnit.setText("تعداد در جعبه: " + directReceiveDetailData.getBoxQuantity());
        etAmount.setText( ThousandSeparatorWatcher.addSeparator(directReceiveDetailData.getAmount()));

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
        unitData.setUnitName(directReceiveDetailData.getPartUnit());
        unitData.setAmount("1");
        list.add(unitData);

        unitData = new ProductUnitData();
        unitData.setUnitName(directReceiveDetailData.getWholeUnit());
        unitData.setAmount(String.valueOf( directReceiveDetailData.getBoxQuantity()));
        list.add(unitData);

        row_unit adapter = new row_unit(this, list);
        sUnit.setAdapter(adapter);
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

        editDirectReceiveDetailActivityMode = EditDirectReceiveDetailActivityMode.BeforeUpdate;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateDirectReceiveItem,this);
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("detailID");
        pi.setValue(directReceiveDetailData.getID());
        service.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("amount");
        pi.setValue(getPcAmount());
        service.piList.add(pi);

        service.listener = this;
        service.execute();
        startWait();

    }
    private int getPcAmount()
    {
        int count = Integer.valueOf( etAmount.getText().toString());
        int boxUnit = Integer.valueOf (((ProductUnitData)sUnit.getSelectedItem()).getAmount());
        int amount = count * boxUnit;
        return amount;
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
        else if(editDirectReceiveDetailActivityMode == EditDirectReceiveDetailActivityMode.BeforeUpdate)
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
            editDirectReceiveDetailActivityMode = EditDirectReceiveDetailActivityMode.AfterUpdate;
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
        if(editDirectReceiveDetailActivityMode == EditDirectReceiveDetailActivityMode.AfterUpdate )
        {
            directReceiveDetailData.setAmount(getPcAmount());

            Intent returnIntent = new Intent();
            returnIntent.putExtra("directReceiveDetailData", directReceiveDetailData);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}