package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverOrderData;
import com.oshanak.mobilemarket.R;

public class EditDeliverOrderPriceActivity extends BaseActivity {

    private DeliverOrderData deliverOrderData;
    private TextView tvTotalPrice;
    private EditText etPosPrice;
    private EditText etCashPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deliver_order_price);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        etPosPrice = findViewById(R.id.etPosPrice);
        etCashPrice = findViewById(R.id.etCashPrice);

        etPosPrice.addTextChangedListener(new ThousandSeparatorWatcher(etPosPrice));
        etCashPrice.addTextChangedListener(new ThousandSeparatorWatcher(etCashPrice));

        Intent intent = getIntent();
        deliverOrderData = (DeliverOrderData) intent.getSerializableExtra("deliverOrderData");

        tvTotalPrice.setText(ThousandSeparatorWatcher.addSeparator( deliverOrderData.getPayablePrice()));

        inCode = true;
        if(deliverOrderData.getPosPrice() == 0 && deliverOrderData.getCashPrice() == 0)
        {
            etPosPrice.setText( String.valueOf( (int)deliverOrderData.getPayablePrice()));
        }
        else
        {
            etPosPrice.setText( String.valueOf( (int)deliverOrderData.getPosPrice()));
        }
        etCashPrice.setText( String.valueOf( (int)deliverOrderData.getCashPrice()));
        inCode = false;


        //region price changed
        etPosPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                setZeroIfEmpty();
                if(inCode) return;
                int total = (int)deliverOrderData.getPayablePrice();
                int pos = Integer.parseInt( ThousandSeparatorWatcher.removeSeparator( etPosPrice.getText().toString()));
                inCode = true;
                etCashPrice.setText(total - pos < 0 ? "0" : String.valueOf( total - pos));
                inCode = false;
            }
        });
        etCashPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                setZeroIfEmpty();
                if(inCode) return;
                int total = (int)deliverOrderData.getPayablePrice();
                int cash = Integer.parseInt( ThousandSeparatorWatcher.removeSeparator( etCashPrice.getText().toString()));
                inCode = true;
                etPosPrice.setText(total - cash < 0 ? "0" : String.valueOf(total - cash));
                inCode = false;
            }
        });
        //endregion price changed

    }
    @Override
    public void onBackPressed()
    {
        if(priceConfirmed )
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("deliverOrderData", deliverOrderData);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public void onClickExit(View view)
    {
        onBackPressed();
    }
    private boolean priceConfirmed = false;
    public void onClickConfirm(View view)
    {
        setZeroIfEmpty();

        int pos = Integer.parseInt( ThousandSeparatorWatcher.removeSeparator( etPosPrice.getText().toString().trim()));
        int cash = Integer.parseInt( ThousandSeparatorWatcher.removeSeparator( etCashPrice.getText().toString().trim()));
        if(pos > deliverOrderData.getPayablePrice() || cash > deliverOrderData.getPayablePrice())
        {
            Toast.makeText(this, "هيچيك از مبالغ دريافتي نمي تواند بيشتر از جمع فاكتور باشد.", Toast.LENGTH_SHORT).show();
            return;
        }
        deliverOrderData.setPosPrice(pos);
        deliverOrderData.setCashPrice(cash);
        priceConfirmed = true;
        onBackPressed();
    }
    private void setZeroIfEmpty()
    {
        if(etPosPrice.getText().toString().trim().equals("")) etPosPrice.setText("0");
        if(etCashPrice.getText().toString().trim().equals("")) etCashPrice.setText("0");
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.setFontBold(tvTotalPrice);
            Utility.increaseTextSize(tvTotalPrice,10);
        }
    }
}