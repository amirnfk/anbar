package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.SelectVendorByProductActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ProductCodeFragment;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductControlContainerData;
import com.oshanak.mobilemarket.Activity.DataStructure.VendorData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_vendor;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

public class SelectVendorByProductActivity extends BaseActivity implements
        ProductCodeFragment.OnBarcodeScannedByCameraListener
        ,ProductCodeFragment.OnBarcodeChanged
        ,ProductCodeFragment.OnConfirmListener
        , OnTaskCompleted
        , row_vendor.OnVendorListCommandListener
{
    private SelectVendorByProductActivityMode selectVendorByProductActivityMode = SelectVendorByProductActivityMode.Unknown;
    private ProductCodeFragment productCodeFragment;
    private TextView tvProductCode;
    private ListView listView;
    private TextView tvCount;
    private VendorData vendorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vendor_by_product);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        productCodeFragment = (ProductCodeFragment)getSupportFragmentManager().findFragmentById(R.id.frProductCode);
        tvProductCode = findViewById(R.id.tvProductCode);
        listView = findViewById(R.id.recyclerview);
        tvCount = findViewById(R.id.tvCount);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            productCodeFragment.setFont();
        }
    }

    @Override
    public void OnBarcodeScannedByCamera(String barcode)
    {
        OnConfirm(barcode);
    }

    @Override
    public void OnBarcodeChanged(String barcode) {

    }

    @Override
    public void OnConfirm(String barcode)
    {
        if(!validateData()) return;
        clear();
        selectVendorByProductActivityMode = SelectVendorByProductActivityMode.BeforeGetProductInfo;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetMaterialInfo, this);
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("barcode");
        pi.setValue(productCodeFragment.getProductCode());
        task.piList.add(pi);

        task.listener = this;
        task.execute();

        startWait();
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
    private void clear()
    {
        tvProductCode.setText("");
        tvCount.setText("");
        listView.setAdapter(null);
    }
    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if(selectVendorByProductActivityMode == SelectVendorByProductActivityMode.BeforeGetProductInfo)
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

            tvProductCode.setText(data.getProductControlData().getProductCode() + " - " +
                    data.getProductControlData().getProductName());

            row_vendor adapter = new row_vendor(this, data.getVendorDataList());
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);

            Utility.hideKeyboard(this);
            Utility.playBeep();
            selectVendorByProductActivityMode = SelectVendorByProductActivityMode.AfterGetProductInfo;
        }
    }
    @Override
    public void onBackPressed()
    {
//        if(editDirectReceiveDetailActivityMode == EditDirectReceiveDetailActivityMode.AfterUpdate )
//        {
//            directReceiveDetailData.setAmount(getPcAmount());
//
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra("directReceiveDetailData", directReceiveDetailData);
//            setResult(Activity.RESULT_OK, returnIntent);
//        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if(data == null) return;
        boolean refreshList = data.getBooleanExtra("refreshList", false);
        if((requestCode == 1) &&
                resultCode == Activity.RESULT_OK && refreshList)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("refreshList", true);
            setResult(Activity.RESULT_OK, returnIntent);
            onBackPressed();
        }
        else if(requestCode == GlobalData.getBarcodeActivityRequestCode())
        {
            productCodeFragment.setResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void OnVendorListCommand(VendorData vendorData, int position, row_vendor.VendorListCommandType commandType)
    {
        this.vendorData = vendorData;
        ((row_vendor)listView.getAdapter()).setSelection(position);
        if(commandType == row_vendor.VendorListCommandType.Select)
        {
            Intent intent = new Intent(this, DirectReceiveItemActivity.class);
            intent.putExtra("vendorData", vendorData);
            startActivityForResult(intent,1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }
}