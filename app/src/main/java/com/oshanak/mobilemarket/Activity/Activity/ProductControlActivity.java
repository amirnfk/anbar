package com.oshanak.mobilemarket.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.ProductControlActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ProductCodeFragment;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductControlContainerData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_unit;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.Activity.zxing.IntentIntegrator;
import com.oshanak.mobilemarket.R;

public class ProductControlActivity extends BaseActivity implements
        OnTaskCompleted
        ,ProductCodeFragment.OnConfirmListener
        ,ProductCodeFragment.OnBarcodeScannedByCameraListener
        ,ProductCodeFragment.OnBarcodeChanged
{

    private ProductControlActivityMode productControlActivityMode = ProductControlActivityMode.Unknown;
//    private EditText etBarcode;
    private TextView tvProductCode;
    private TextView tvProductName;
    private TextView tvPrice;
    private TextView tvSalePrice;
    private TextView tvLatestSaleDate;
    private TextView tvLatestReceiptDate;
    private ProductCodeFragment productCodeFragment;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_control);
        ///////////////////////

        tvProductCode = findViewById(R.id.tvProductCode);
        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvSalePrice = findViewById(R.id.tvSalePrice);
        tvLatestSaleDate = findViewById(R.id.tvLatestSaleDate);
        tvLatestReceiptDate = findViewById(R.id.tvLatestReceiptDate);
//        sUnit = findViewById(R.id.sUnit);
        listView = findViewById(R.id.recyclerview);
        productCodeFragment = (ProductCodeFragment)getSupportFragmentManager().findFragmentById(R.id.frProductCode);
    }
    private void clear()
    {
        tvProductCode.setText("");
        tvProductName.setText("");
        tvPrice.setText("");
        tvSalePrice.setText("");
        tvLatestSaleDate.setText("");
        tvLatestReceiptDate.setText("");
        listView.setAdapter(null);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            int increasePercent = 10;
            Utility.increaseTextSize(tvProductCode, increasePercent * 2);
            Utility.increaseTextSize(tvProductName, increasePercent);
            Utility.increaseTextSize(tvPrice, increasePercent * 4);
            Utility.setFontBold(tvPrice);
            Utility.increaseTextSize(tvSalePrice, increasePercent * 4);
            Utility.setFontBold(tvSalePrice);
            Utility.increaseTextSize(tvLatestSaleDate, increasePercent);
            Utility.increaseTextSize(tvLatestReceiptDate, increasePercent * 2);
            Utility.setFontBold(tvLatestReceiptDate);

            productCodeFragment.setFont();
        }
    }

    public void onCancelClick(View view)
    {
        onBackPressed();
    }
    public void onMoreInfo(View view)
    {

    }

    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if(productControlActivityMode == ProductControlActivityMode.BeforeGetProduct  )
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("product does not exist"))
            {
                Utility.simpleAlert(this,"كالايي با اين مشخصات يافت نشد!", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this,getString(R.string.general_error), DialogIcon.Error);
                return;
            }
            ProductControlContainerData data = (ProductControlContainerData) taskResult.dataStructure;

            tvProductCode.setText(data.getProductControlData().getProductCode());
            tvProductName.setText(data.getProductControlData().getProductName());
            tvPrice.setText(data.getProductControlData().getPrice());
            tvSalePrice.setText(data.getProductControlData().getSalePrice());
            tvLatestSaleDate.setText(data.getProductControlData().getLatestSaleDate());
            tvLatestReceiptDate.setText(data.getProductControlData().getLatestReceiptDate());

            row_unit adapter = new row_unit(this, data.getProductUnitDataList());
            listView.setAdapter(adapter);

            Utility.hideKeyboard(this);
            Utility.playBeep();
            productControlActivityMode = ProductControlActivityMode.AfterGetProduct;
        }
    }
    public void onClickBarcode(View view)
    {
        clear();
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult( requestCode, resultCode, intent);
        if(requestCode == GlobalData.getBarcodeActivityRequestCode())
        {
            productCodeFragment.setResult(requestCode, resultCode, intent);
        }
    }

    private boolean validateData()
    {
        if(productCodeFragment.getProductCode().equals(""))
        {
            Toast.makeText(this,"كد/باركد كالا را وارد نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void OnConfirm(String barcode)
    {
        if(!validateData()) return;
        clear();
        productControlActivityMode = ProductControlActivityMode.BeforeGetProduct;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetMaterialInfo, this);

        task.addParam("barcode", productCodeFragment.getProductCode());

        task.listener = this;
        task.execute();

        startWait();

    }


    @Override
    public void OnBarcodeScannedByCamera(String barcode) {
        OnConfirm(barcode);
    }

    @Override
    public void OnBarcodeChanged(String barcode) {
        clear();
    }

}
