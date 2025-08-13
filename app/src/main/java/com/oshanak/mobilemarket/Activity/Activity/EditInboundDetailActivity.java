package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.EditInboundDetailActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Models.UpdateDetailResponse;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_general_spinner;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Get_Item_API;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Get_Item_API_PILOT;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Inbound_Data_API_Operation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Inbound_Data_API_Pilot;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.UpdateDetailRequest;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditInboundDetailActivity extends BaseActivity implements OnTaskCompleted
{

    private TextView tvItemID;
    private TextView tvItemName;
    private TextView tvUMREZ;
    private EditText etAmount;
    private Spinner sUnit;
    private InboundDetailData inboundDetailData;
    private EditInboundDetailActivityMode editInboundDetailActivityMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inbound_detail);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        tvItemID = findViewById(R.id.tvItemID);
        tvItemName = findViewById(R.id.tvItemName);
        tvUMREZ = findViewById(R.id.tvUMREZ);
        etAmount = findViewById(R.id.etAmount);
        sUnit = findViewById(R.id.sUnit);

        etAmount.addTextChangedListener(new ThousandSeparatorWatcher(etAmount));

        Intent intent = getIntent();
        inboundDetailData = (InboundDetailData) intent.getSerializableExtra("inboundDetailData");

        tvItemID.setText(String.valueOf( inboundDetailData.getItemId()));
        tvItemName.setText(inboundDetailData.getItemName());
        tvUMREZ.setText("تعداد در جعبه: " + inboundDetailData.getUMREZ());
        etAmount.setText( ThousandSeparatorWatcher.addSeparator(inboundDetailData.getUserCount()));

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
        List<String> list = new ArrayList<>();
        list.add(inboundDetailData.getTranslate_Meinh());
        list.add(inboundDetailData.getMeins());
        row_general_spinner adapter = new row_general_spinner(this, list);
        sUnit.setAdapter(adapter);
        for(int i = 0; i < sUnit.getCount(); i++)
        {
            String unit = sUnit.getItemAtPosition(i).toString();
            if(unit.equals(inboundDetailData.getUserMeins()))
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

        double deliverQuantity = Double.valueOf( ThousandSeparatorWatcher.removeSeparator( etAmount.getText().toString()));

        updateInboundDtailUsingRestApi(inboundDetailData.getID()+"",deliverQuantity,sUnit.getSelectedItem().toString());
//        editInboundDetailActivityMode = EditInboundDetailActivityMode.BeforeUpdate;
//        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateInboundDetail,this);
//
//        service.addParam("inboundDetailID", inboundDetailData.getID());
//        service.addParam("userCount", deliverQuantity);
//        service.addParam("userMeins", sUnit.getSelectedItem().toString());
//
//        service.listener = this;
//        service.execute();
//        startWait();

    }

    private void updateInboundDtailUsingRestApi(String id, double deliverQuantity, String sUnit) {

        UpdateDetailRequest request = new UpdateDetailRequest(id, (int) deliverQuantity, sUnit,new MetaData(GlobalData.getUserName(), Utilities.getApkVersionCode(EditInboundDetailActivity.this),"", ApplicationMode.StoreHandheld.toString(),Utility.getDeviceInfo(),GlobalData.getStoreID()));








        Common c = new Common(this);
        String s = c.URL();

        ApiInterface apiService;

        if (s.contains("pilot")) {
            apiService = Inbound_Data_API_Pilot.getAPI().create(ApiInterface.class);
        } else {
            apiService = Inbound_Data_API_Operation.getAPI().create(ApiInterface.class);
        }


        // Make the POST request
        Call<UpdateDetailResponse> call = apiService.UpdateInboundDetail(request);
        call.enqueue(new Callback<UpdateDetailResponse>() {
            @Override
            public void onResponse(Call<UpdateDetailResponse> call, Response<UpdateDetailResponse> response) {


                if (response.body().getMessage().equals("Successful.")) {
                    Utility.hideKeyboard(EditInboundDetailActivity.this);
                    Toast.makeText(EditInboundDetailActivity.this, "تغییرات با موفقیت اعمال شد"  , Toast.LENGTH_SHORT).show();
                    editInboundDetailActivityMode = EditInboundDetailActivityMode.AfterUpdate;
                    onBackPressed();
                }else {
                    Toast.makeText(EditInboundDetailActivity.this, "Request Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateDetailResponse> call, Throwable t) {

                Toast.makeText(EditInboundDetailActivity.this, "Request Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
        else if(editInboundDetailActivityMode == EditInboundDetailActivityMode.BeforeUpdate)
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
            editInboundDetailActivityMode = EditInboundDetailActivityMode.AfterUpdate;
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
        if(editInboundDetailActivityMode == EditInboundDetailActivityMode.AfterUpdate )
        {
            double deliverQuantity = Double.valueOf( ThousandSeparatorWatcher.removeSeparator( etAmount.getText().toString()));
            inboundDetailData.setUserCount(deliverQuantity);
            inboundDetailData.setUserMeins(sUnit.getSelectedItem().toString());

            Intent returnIntent = new Intent();
            returnIntent.putExtra("inboundDetailData", inboundDetailData);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}