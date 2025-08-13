package com.oshanak.mobilemarket.Activity.PickingApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.NumericUpDownFragment;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.R;

public class PickingEnterPalletActivity extends BaseActivity implements NumericUpDownFragment.OnValueChanged
{
    private NumericUpDownFragment numPalletCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_enter_pallet);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        numPalletCount = (NumericUpDownFragment) getSupportFragmentManager().findFragmentById(R.id.numPalletCount);
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
        confirm();
    }
    private boolean validateData()
    {
        if (numPalletCount.isEmpty())
        {
            Utility.showFailureToast(PickingEnterPalletActivity.this,"تعداد پالت جمع آوري شده را وارد نماييد");
//            Toast.makeText(this, "تعداد پالت جمع آوري شده را وارد نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        int value = Integer.parseInt( numPalletCount.getValue());
        if(value < 0)
        {
            Utility.showFailureToast(PickingEnterPalletActivity.this,"تعداد پالت جمع آوري شده را وارد نماييد");
//            Toast.makeText(this, "تعداد پالت جمع آوري شده را وارد نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean isConfirmed = false;
    private void confirm()
    {
        if (!validateData()) return;
        isConfirmed = true;
        onBackPressed();
        Utility.hideKeyboard(this);
    }
    @Override
    public void onBackPressed()
    {
        if(isConfirmed )
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("palletCount", Integer.parseInt( numPalletCount.getValue()));
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