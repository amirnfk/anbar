package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.StoreInboundDetailActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ProductCodeFragment;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundSaleItemData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.DataStructure.UnitValue;
import com.oshanak.mobilemarket.Activity.Enum.ApplicationMode;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Models.InboundDetailResponse;
import com.oshanak.mobilemarket.Activity.Models.UpdateDetailResponse;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_general_spinner;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_inbound_detail;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.InboundDetailRequest;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Inbound_Data_API_Operation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Inbound_Data_API_Pilot;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.UpdateStatusDetailRequest;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javadz.beanutils.BeanUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreInboundDetailActivity extends BaseActivity implements ProductCodeFragment.OnConfirmListener, OnTaskCompleted
    ,row_inbound_detail.OnInboundDetailListCommandListener
    , ProductCodeFragment.OnBarcodeScannedByCameraListener
    ,ProductCodeFragment.OnBarcodeChanged
{
    private InboundHeaderData inboundHeaderData;
    private InboundSaleItemData inboundSaleItemData;
    private InboundDetailData inboundDetailData;
    private TextView tvInboundId;
    private TextView tvProductCode;
    private TextView tvBoxUnit;
    private Spinner sUnit;
    private EditText etAmount;
    private ListView listView;
    private TextView tvCount;
    private StoreInboundDetailActivityMode storeInboundDetailActivityMode;
    private ProductCodeFragment productCodeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_inbound_detail);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        Intent intent = getIntent();
        inboundHeaderData = (InboundHeaderData)intent.getSerializableExtra("inboundHeaderData");
      String  inboundHeaderID = (String) intent.getStringExtra("inboundHeaderID");


        tvInboundId = findViewById(R.id.tvInboundId);
        tvProductCode = findViewById(R.id.tvProductCode);
        tvBoxUnit = findViewById(R.id.tvBoxUnit);
        sUnit = findViewById(R.id.sUnit);
        etAmount = findViewById(R.id.etAmount);
        listView = findViewById(R.id.recyclerview);
        tvCount = findViewById(R.id.tvCount);
        productCodeFragment = (ProductCodeFragment)getSupportFragmentManager().findFragmentById(R.id.frProductCode);

        tvInboundId.setText(inboundHeaderData.getInboundId());
        Utility.clearGeneralSpinner(this, sUnit);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                inboundDetailData = (InboundDetailData) parent.getItemAtPosition(position);
                ((row_inbound_detail)parent.getAdapter()).setSelection(position);
            }
        });
        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT))
                {
                    onInsertClick(v);
                }
                return false;
            }
        };
        etAmount.setOnEditorActionListener(editorActionListener);

            getListUsingRestApi(inboundHeaderID);
//        getList();
    }



    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            productCodeFragment.setFont();
            productCodeFragment.setAutoScanVisible(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.store_inbound_detail_menu, menu);
        super.onCreateOptionsMenu(menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnuSend:
                send();
                return true;
            case R.id.mnuExit:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void send()
    {


        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            inboundDetailData = (InboundDetailData) listView.getItemAtPosition(i);

            if(inboundDetailData.getStatusID()!=2){
                Toast.makeText(this, "ابتدا کلیه کالاهارا تعیین تکلیف کنید", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(listView.getAdapter() ==  null ||
                listView.getAdapter().getCount() == 0)
        {
            Utility.simpleAlert(this, "هيچ كالايي جهت ارسال ثبت نشده است.", DialogIcon.Warning);
            return;
        }
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آيا اطلاعات سفارش به مركز ارسال گردد؟");
        dlgAlert.setTitle("ارسال اطلاعات");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("بله",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        storeInboundDetailActivityMode = StoreInboundDetailActivityMode.BeforeSendInboundToSAP;
                        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.SendInboundToSAP
                                ,StoreInboundDetailActivity.this);

                        service.addParam("InboundHeaderID", inboundHeaderData.getID());

                        service.listener = StoreInboundDetailActivity.this;
                        service.execute();
                        startWait();
                    }
                });
        dlgAlert.setNegativeButton("خير",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });

        dlgAlert.setIcon(R.drawable.question128);
        dlgAlert.create().show();
    }
    private void getList()
    {
        storeInboundDetailActivityMode = StoreInboundDetailActivityMode.BeforeGetInboundDetail;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetInboundDetail, this);

        task.addParam("InboundHeaderID", String.valueOf( inboundHeaderData.getID()));

        task.listener = this;
        task.execute();
        startWait();
    }
    private void getListUsingRestApi(String inBoundHeaderId) {

         InboundDetailRequest request = new InboundDetailRequest(String.valueOf( inBoundHeaderId), new MetaData(GlobalData.getUserName(), Utilities.getApkVersionCode(StoreInboundDetailActivity.this),"", ApplicationMode.StoreHandheld.toString(),Utility.getDeviceInfo(),GlobalData.getStoreID()));

        // Create Retrofit client and service

        Common c = new Common(this);
        String s = c.URL();

        ApiInterface apiService;

        if (s.contains("pilot")) {
            apiService = Inbound_Data_API_Pilot.getAPI().create(ApiInterface.class);
        } else {
            apiService = Inbound_Data_API_Operation.getAPI().create(ApiInterface.class);
        }

        // Make the POST request
        Call<InboundDetailResponse> call = apiService.getInboundDetail(request);
        call.enqueue(new Callback<InboundDetailResponse>() {
            @Override
            public void onResponse(Call<InboundDetailResponse> call, Response<InboundDetailResponse> response) {

                if (response.isSuccessful() && Objects.equals(response.body().getMessage(), "Successful!")) {

                   UpdateInboundListDetailData(  response.body().getInboundDetailList());

                } else if(response.isSuccessful() && Objects.equals(response.body().getMessage(), "No rows found!")){
//                    Toast.makeText(StoreInboundDetailActivity.this, "", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(StoreInboundDetailActivity.this, "Request Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InboundDetailResponse> call, Throwable t) {

                Toast.makeText(StoreInboundDetailActivity.this, "Request Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UpdateInboundListDetailData(List<InboundDetailData> _inboundDetailData) {
        ArrayList<InboundDetailData> list = (ArrayList<InboundDetailData>) _inboundDetailData;

        row_inbound_detail adapter = new row_inbound_detail(this, list);
        listView.setAdapter(adapter);
        Utility.setListCount(adapter.getCount(), tvCount);
        Utility.hideKeyboard(this);
        storeInboundDetailActivityMode = StoreInboundDetailActivityMode.AfterGetInboundDetail;
        if(adapter.getCount() >= 200)
        {
            Toast.makeText(this,
                    getString( R.string.only_200_rows)
                    ,Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onBackPressed()
    {
        if(storeInboundDetailActivityMode == StoreInboundDetailActivityMode.AfterSendInboundToSAP )
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("resetList", true);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void OnConfirm(String barcode)
    {
        if(!validateProductControl()) return;
        if(duplicateItemDetected(barcode)){
            Toast.makeText(this, "کالای تکراری ، این کالا قبلا ثبت شده است", Toast.LENGTH_SHORT).show();
            Utility.hideKeyboard(StoreInboundDetailActivity.this);
            reset(true);
            return;
        }else {
            getInboundSaleItem(barcode);
        }

//        if(duplicateItemDetected(barcode)){
//            Toast.makeText(this, "کالای تکراری", Toast.LENGTH_SHORT).show();
//            Utility.hideKeyboard(this);
//        }
//
//        else {
//                getInboundSaleItem(barcode);
//
//        }
    }

    private boolean duplicateItemDetected(String barcode) {

        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            inboundDetailData = (InboundDetailData) listView.getItemAtPosition(i);

            if (String.valueOf(inboundDetailData.getItemId()).equals(barcode)) {

                ((row_inbound_detail) listView.getAdapter()).setSelection(i);
                listView.smoothScrollToPosition(i);
                reset(true);
                return true;

            }
        }
        return false;
    }
//        ArrayAdapter<InboundDetailData> adapter = (ArrayAdapter<InboundDetailData>) listView.getAdapter();
//        for (int i = 0; i < adapter.getCount(); i++)
//        {
//            InboundDetailData iData = adapter.getItem(i);
//            if (iData.getItemId() == inboundSaleItemData.getItemId())
//            {
//                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//                dlgAlert.setMessage("اين كالا قبلاً به مقدار " +
//                        + iData.getUserCount() + " "
//                        + iData.getUserMeins() + " "
//                        + "ثبت شده است. مقدار قبلي با مقدار جديد تجميع گردد؟");
//
//                dlgAlert.setTitle("كالاي تكراري");
//                dlgAlert.setCancelable(true);
//                dlgAlert.setPositiveButton("جمع كن",
//                        new DialogInterface.OnClickListener()
//                        {
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                updateDuplicate(true, iData);
//                            }
//                        });
//                dlgAlert.setNegativeButton("جايگزين كن",
//                        new DialogInterface.OnClickListener()
//                        {
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                updateDuplicate(false, iData);
//                            }
//                        });
//                dlgAlert.setNeutralButton("انصراف",
//                        new DialogInterface.OnClickListener()
//                        {
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                dialog.cancel();
//                            }
//                        });
//
//                dlgAlert.setIcon(R.drawable.question128);
//                dlgAlert.create().show();
//
//                break;
//            }
//        }


    private void getInboundSaleItem(String barcode)
    {
        reset(false);

        storeInboundDetailActivityMode = StoreInboundDetailActivityMode.BeforeGetInboundSaleItem;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetInboundSaleItem, this);

        task.addParam("StoreId", GlobalData.getStoreID());
        task.addParam("ItemId", barcode);

        task.listener = this;
        task.execute();
        startWait();
    }
    private void reset(boolean resetProductFragment)
    {
        tvProductCode.setText("");
        tvBoxUnit.setText("");
        etAmount.setText("");
        Utility.clearGeneralSpinner(this, sUnit);
        if(resetProductFragment) {
            productCodeFragment.setProductCode("");
        }
        inboundSaleItemData = null;
    }

    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;


        if(storeInboundDetailActivityMode == StoreInboundDetailActivityMode.BeforeGetInboundSaleItem )
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("No rows found!"))
            {
                Utility.simpleAlert(this,"كالايي با اين مشخصات يافت نشد!", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this,getString(R.string.general_error), DialogIcon.Error);
                return;
            }

            inboundSaleItemData = (InboundSaleItemData) taskResult.dataStructure;

            tvProductCode.setText(String.valueOf( inboundSaleItemData.getItemId()) + " - " +
                    inboundSaleItemData.getItemName());


            if(duplicateItemDetected(inboundSaleItemData.getItemId()+"")){
                Toast.makeText(this, "کالای تکراری ، این کالا قبلا ثبت شده است", Toast.LENGTH_SHORT).show();
                Utility.hideKeyboard(StoreInboundDetailActivity.this);
                reset(true);
                return;
                 }else {


                tvBoxUnit.setText(ThousandSeparatorWatcher.addSeparator(inboundSaleItemData.getUMREZ()));

                List<String> list = new ArrayList<>();


                if (inboundHeaderData.getOrderType().equalsIgnoreCase("d")) {
                    list.add(inboundSaleItemData.getTranslate_Meinh());
                }
                list.add(inboundSaleItemData.getMeins());
                row_general_spinner adapter = new row_general_spinner(this, list);
                sUnit.setAdapter(adapter);
                Utility.hideKeyboard(this);
                Utility.playBeep();
                etAmount.requestFocus();
                Utility.showKeyboard(this);
                storeInboundDetailActivityMode = StoreInboundDetailActivityMode.AfterGetInboundSaleItem;
            }
        }
        else if(storeInboundDetailActivityMode == StoreInboundDetailActivityMode.BeforeInsertInboundDetail)
        {
            if (!taskResult.isSuccessful)
            {
                if(taskResult.isExceptionOccured("IX_InboundDetail_UK"))
                {
                    duplicateDetected();
                }
                else
                {
                    Utility.simpleAlert(this, getString( R.string.insert_do_not), DialogIcon.Error);
                }
                return;
            }
            Toast.makeText(this, "ثبت اطلاعات انجام شد.",Toast.LENGTH_SHORT).show();
            insertToList( Integer.valueOf( taskResult.tag));
            Utility.hideKeyboard(this);
            Utility.playShortBeep();
            reset(true);
            if(productCodeFragment.isAutoScanEnable()) productCodeFragment.startScan();
            storeInboundDetailActivityMode = StoreInboundDetailActivityMode.AfterInsertInboundDetail;
//            insertDone = true;
        }
        else if(storeInboundDetailActivityMode == StoreInboundDetailActivityMode.BeforeGetInboundDetail)
        {
//            reset();
            if(taskResult == null) return;

            if(!taskResult.isSuccessful && !taskResult.message.equals( "No rows found!"))
            {
                Utility.simpleAlert(this, getString( R.string.error_in_fetching_data), DialogIcon.Error);
                return;
            }
            else if(!taskResult.isSuccessful && taskResult.message.equals( "No rows found!"))
            {
                return;
            }
            ArrayList<InboundDetailData> list = (ArrayList<InboundDetailData>) taskResult.dataStructure;

            row_inbound_detail adapter = new row_inbound_detail(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            storeInboundDetailActivityMode = StoreInboundDetailActivityMode.AfterGetInboundDetail;
            if(adapter.getCount() >= 200)
            {
                Toast.makeText(this,
                        getString( R.string.only_200_rows)
                        ,Toast.LENGTH_LONG).show();
            }
        }
        else if(storeInboundDetailActivityMode == StoreInboundDetailActivityMode.BeforeDelete)
        {
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
                return;
            }

            ArrayAdapter<InboundDetailData> adapter = (ArrayAdapter<InboundDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                InboundDetailData pData = adapter.getItem(i);
                if (pData.getID() == inboundDetailData.getID())
                {
                    adapter.remove(pData);
                    inboundDetailData = null;
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), (TextView) findViewById(R.id.tvCount));
            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
            storeInboundDetailActivityMode = StoreInboundDetailActivityMode.AfterDelete;
        }
        else if(storeInboundDetailActivityMode == StoreInboundDetailActivityMode.BeforeUpdate)
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            if (!taskResult.isSuccessful) {
                if (!Utility.generalErrorOccurred(taskResult, this)) {
                    Utility.simpleAlert(this, getString(R.string.update_do_not), DialogIcon.Error);
                }
                return;
            }

            ArrayAdapter<InboundDetailData> adapter = (ArrayAdapter<InboundDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                InboundDetailData pData = adapter.getItem(i);
                if (pData.getID() == tempInboundDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, tempInboundDetailData);
                    }
                    catch(IllegalAccessException ex)
                    {
                        Utility.simpleAlert(this,getString(R.string.error_in_fetching_data));
                    }
                    catch (InvocationTargetException ex)
                    {
                        Utility.simpleAlert(this,getString(R.string.error_in_fetching_data));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
            reset(true);
            Toast.makeText(this, getString(R.string.update_done), Toast.LENGTH_SHORT).show();
            Utility.hideKeyboard(this);
            Utility.playShortBeep();
            storeInboundDetailActivityMode = StoreInboundDetailActivityMode.AfterUpdate;

        }
        else if(storeInboundDetailActivityMode == StoreInboundDetailActivityMode.BeforeSendInboundToSAP)
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            if (!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, "خطا در ارسال اطلاعات به مركز." + "\n" +
                        taskResult.message, DialogIcon.Error);
                return;
            }

            Utility.hideKeyboard(this);
            storeInboundDetailActivityMode = StoreInboundDetailActivityMode.AfterSendInboundToSAP;
            Utility.simpleAlert(this, "ارسال اطلاعات به مركز انجام گرديد." ,"", DialogIcon.Info, onFinishClick);
        }
    }
    DialogInterface.OnClickListener onFinishClick= new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            onBackPressed();
        }
    };
    private void duplicateDetected()
    {

        ArrayAdapter<InboundDetailData> adapter = (ArrayAdapter<InboundDetailData>) listView.getAdapter();

        for (int i = 0; i < listView.getAdapter().getCount(); i++) {
            inboundDetailData = (InboundDetailData) listView.getItemAtPosition(i);
            InboundDetailData iData = adapter.getItem(i);
            if (iData.getItemId() == inboundSaleItemData.getItemId())
            {                ((row_inbound_detail) listView.getAdapter()).setSelection(i);
                listView.smoothScrollToPosition(i);
                Toast.makeText(this, "کالای تکرای، این کالا قبلا ثبت شده است", Toast.LENGTH_SHORT).show();

            }
        }


//        ArrayAdapter<InboundDetailData> adapter = (ArrayAdapter<InboundDetailData>) listView.getAdapter();
//        for (int i = 0; i < adapter.getCount(); i++)
//        {
//            InboundDetailData iData = adapter.getItem(i);
//            if (iData.getItemId() == inboundSaleItemData.getItemId())
//            {
//                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
//                dlgAlert.setMessage("اين كالا قبلاً به مقدار " +
//                        + iData.getUserCount() + " "
//                        + iData.getUserMeins() + " "
//                        + "ثبت شده است. مقدار قبلي با مقدار جديد تجميع گردد؟");
//
//                dlgAlert.setTitle("كالاي تكراري");
//                dlgAlert.setCancelable(true);
//                dlgAlert.setPositiveButton("جمع كن",
//                        new DialogInterface.OnClickListener()
//                        {
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                updateDuplicate(true, iData);
//                            }
//                        });
//                dlgAlert.setNegativeButton("جايگزين كن",
//                        new DialogInterface.OnClickListener()
//                        {
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                updateDuplicate(false, iData);
//                            }
//                        });
//                dlgAlert.setNeutralButton("انصراف",
//                        new DialogInterface.OnClickListener()
//                        {
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//                                dialog.cancel();
//                            }
//                        });
//
//                dlgAlert.setIcon(R.drawable.question128);
//                dlgAlert.create().show();
//
//                break;
//            }
//        }
    }

    InboundDetailData tempInboundDetailData;
    private void updateDuplicate(boolean addToOldValue, InboundDetailData iData)
    {
        double inputValue = Double.valueOf( etAmount.getText().toString());
        double newCount = 0;
        String newMein = "";
        if(addToOldValue)
        {
            double oldUserCount = iData.getUserCount();
            String oldUserMeins = iData.getUserMeins();
            int UMREZ = iData.getUMREZ();

            String unitItem = sUnit.getSelectedItem().toString();
            UnitValue unitValue = Utility.getNewUnitAndValue(oldUserMeins, oldUserCount, unitItem, inputValue, UMREZ);

//            if ((oldUserMeins.equals( "CAR") && unitItem.equals( "CAR")) ||
//                    (oldUserMeins.equals("KG") && unitItem.equals("KG"))) {
//                newCount = inputValue + oldUserCount;
//                newMein = unitItem;
//            } else if ((oldUserMeins.equals("CAR") && unitItem.equals("PC")) ||
//                    (oldUserMeins.equals("KG") && unitItem.equals("G"))) {
//                newCount = inputValue + (oldUserCount * UMREZ);
//                newMein = unitItem;
//            } else if ((oldUserMeins.equals("PC") && unitItem == "CAR") ||
//                    (oldUserMeins.equals("G") && unitItem.equals("KG"))) {
//                newCount = (inputValue * UMREZ) + oldUserCount;
//                newMein = oldUserMeins;
//            } else if ((oldUserMeins.equals("PC") && unitItem.equals("PC")) ||
//                    (oldUserMeins.equals("G") && unitItem.equals("G"))) {
//                newCount = inputValue + oldUserCount;
//                newMein = unitItem;
//            }
            newCount = unitValue.getValue();
            newMein = unitValue.getUnit();
        }
        else
        {
            newCount = inputValue;
            newMein = sUnit.getSelectedItem().toString();
        }
        storeInboundDetailActivityMode = StoreInboundDetailActivityMode.BeforeUpdate;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateInboundDetail,this);
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("inboundDetailID");
        pi.setValue(iData.getID());
        service.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("userCount");
        pi.setValue(newCount);
        service.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("userMeins");
        pi.setValue(newMein);
        service.piList.add(pi);

        iData.setUserCount(newCount);
        iData.setUserMeins(newMein);
        tempInboundDetailData = iData;

        service.listener = this;
        service.execute();
        startWait();
    }
    private void insertToList(int ID)
    {

        InboundDetailData data = new InboundDetailData();
        data.setID(ID);
        data.setInboundHeaderID(inboundHeaderData.getID());
        data.setInboundItemID(inboundSaleItemData.getID());
        data.setItemId(inboundSaleItemData.getItemId());
        data.setUserCount(Double.valueOf( etAmount.getText().toString()));
        data.setUserMeins(sUnit.getSelectedItem().toString());
        data.setItemName(inboundSaleItemData.getItemName());
        data.setUMREZ(inboundSaleItemData.getUMREZ());
        data.setMeins(inboundSaleItemData.getMeins());
        data.setTranslate_Meinh(inboundSaleItemData.getTranslate_Meinh());

        row_inbound_detail adapter = (row_inbound_detail)listView.getAdapter();
        if(adapter == null)
        {
            adapter = new row_inbound_detail(this, new ArrayList<>());
            listView.setAdapter(adapter);
        }
        adapter.add(data);
        adapter.notifyDataSetChanged();
        Utility.setListCount(adapter.getCount(), tvCount);

    }
    public void onInsertClick(View view)
    {

        if (!validateData()) return;
        if(duplicateItemDetected(inboundHeaderData.getID()+"")){
            Toast.makeText(this, "کالای تکراری ، این کالا قبلا ثبت شده است", Toast.LENGTH_SHORT).show();
            Utility.hideKeyboard(StoreInboundDetailActivity.this);
            reset(true);
            return;
    }

        storeInboundDetailActivityMode = StoreInboundDetailActivityMode.BeforeInsertInboundDetail;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.InsertInboundDetail,this);

        service.addParam("InboundHeaderID", inboundHeaderData.getID());
        service.addParam("InboundItemID", inboundSaleItemData.getID());
        service.addParam("UserCount", etAmount.getText().toString());
        service.addParam("UserMeins", sUnit.getSelectedItem().toString());

        service.listener = this;
        service.execute();
        startWait();
    }
    private boolean validateProductControl()
    {
        if(productCodeFragment.getProductCode().equals(""))
        {
            Toast.makeText(this,"كد/باركد كالا را وارد نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean validateData()
    {
        if(inboundSaleItemData == null)
        {
            Utility.simpleAlert(this,"ابتدا كالا را انتخاب نماييد.", DialogIcon.Warning);
            return false;
        }
        if (Utility.editTextIsEmpty(etAmount, "تعداد كالا را وارد نماييد")) {
            return false;
        }
        return true;
    }

    @Override
    public void OnInboundDetailListCommand(InboundDetailData inboundDetailData
            , int position, row_inbound_detail.InboundDetailListCommandType commandType )
    {
        this.inboundDetailData = inboundDetailData;
        ((row_inbound_detail)listView.getAdapter()).setSelection(position);
        if(commandType == row_inbound_detail.InboundDetailListCommandType.Delete)
        {
            Delete();
        }
        else if(commandType == row_inbound_detail.InboundDetailListCommandType.Edit)
        {
            Intent intent = new Intent(this, EditInboundDetailActivity.class);
            intent.putExtra("inboundDetailData", inboundDetailData);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (commandType==row_inbound_detail.InboundDetailListCommandType.Confirm) {
            Confirm();
        }
    }

    private void Confirm() {
        if (inboundDetailData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }
        if(inboundDetailData.getStatusID()==2){
                    UpdateInboundListDetailDataUsingRestApi(inboundDetailData.getID(), 1);
                } else if (inboundDetailData.getStatusID()==1 || inboundDetailData.getStatusID()==0) {
                    UpdateInboundListDetailDataUsingRestApi(inboundDetailData.getID(), 2 );
                }


    }

    private void UpdateInboundListDetailDataUsingRestApi(int inBoundId, int  newStatusId  ) {

        UpdateStatusDetailRequest request = new UpdateStatusDetailRequest(inBoundId,   newStatusId, new MetaData(GlobalData.getUserName(), Utilities.getApkVersionCode(StoreInboundDetailActivity.this),"", ApplicationMode.StoreHandheld.toString(),Utility.getDeviceInfo(),GlobalData.getStoreID()));

        // Create Retrofit client and service

        Common c = new Common(this);
        String s = c.URL();

        ApiInterface apiService;

        if (s.contains("pilot")) {
            apiService = Inbound_Data_API_Pilot.getAPI().create(ApiInterface.class);
        } else {
            apiService = Inbound_Data_API_Operation.getAPI().create(ApiInterface.class);
        }


        // Make the POST request
        Call<UpdateDetailResponse> call = apiService.UpdateInboundDetailStatus(request);
        call.enqueue(new Callback<UpdateDetailResponse>() {
            @Override
            public void onResponse(Call<UpdateDetailResponse> call, Response<UpdateDetailResponse> response) {


                if (response.body().getMessage().equals("Successful.")) {
                    if(newStatusId==2){


                            ArrayAdapter<InboundDetailData> adapter = (ArrayAdapter<InboundDetailData>) listView.getAdapter();
                            adapter.notifyDataSetChanged();


                    }else{

                        ArrayAdapter<InboundDetailData> adapter = (ArrayAdapter<InboundDetailData>) listView.getAdapter();
                        adapter.notifyDataSetChanged();

                    }


                }else {
                    Toast.makeText(StoreInboundDetailActivity.this, "Request Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateDetailResponse> call, Throwable t) {

                Toast.makeText(StoreInboundDetailActivity.this, "Request Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Delete()
    {
        if (inboundDetailData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا اطلاعات مربوط به كالاي زير حذف گردد؟" + "\n"
                + inboundDetailData.getItemId() + "\n"
                + inboundDetailData.getItemName());

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        StoreInboundDetailActivity.this.storeInboundDetailActivityMode
                                = StoreInboundDetailActivityMode.BeforeDelete;

                        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.DeleteInboundDetail
                                , StoreInboundDetailActivity.this);

                        task.addParam("inboundDetailID", inboundDetailData.getID());

                        task.listener = StoreInboundDetailActivity.this;
                        task.execute();
                        startWait();
                    }
                });
        dlgAlert.setNegativeButton("انصراف",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Toast.makeText(getBaseContext(),"انصراف",Toast.LENGTH_SHORT).show();
                    }
                });
        dlgAlert.setIcon(R.drawable.question128);
        dlgAlert.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            inboundDetailData = (InboundDetailData) data.getSerializableExtra("inboundDetailData");

            ArrayAdapter<InboundDetailData> adapter = (ArrayAdapter<InboundDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                InboundDetailData pData = adapter.getItem(i);
                if (pData.getID() == inboundDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, inboundDetailData);
                    }
                    catch(IllegalAccessException ex)
                    {
                        Utility.simpleAlert(this,getString(R.string.error_in_fetching_data));
                    }
                    catch (InvocationTargetException ex)
                    {
                        Utility.simpleAlert(this,getString(R.string.error_in_fetching_data));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        else if(requestCode == GlobalData.getBarcodeActivityRequestCode())
        {
            productCodeFragment.setResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void OnBarcodeScannedByCamera(String barcode) {
        if(duplicateItemDetected(barcode)){
            Toast.makeText(this, "کالای تکراری ، این کالا قبلا ثبت شده است", Toast.LENGTH_SHORT).show();
            Utility.hideKeyboard(StoreInboundDetailActivity.this);
            reset(true);
            return;
        }else{
            getInboundSaleItem(barcode);
        }

    }

    @Override
    public void OnBarcodeChanged(String barcode) {
        reset(false);
    }
}
