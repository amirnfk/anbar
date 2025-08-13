package com.oshanak.mobilemarket.Activity.PickingApp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oshanak.mobilemarket.Activity.Activity.Enum.PickingDeliverHeaderListActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.DateFragment;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Enum.PickingOrderStatus;

import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingControlHeaderDataResponseBodyModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingControlHeaderRequestBodyModel;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import javadz.beanutils.BeanUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickingControlLineActivity extends AppCompatActivity
        implements DateFragment.OnDateChangedListener, OnTaskCompleted, row_picking_control_header.OnPickingControlCommandListener , row_picking_control_Line.OnItemClickListener
{
    private DateFragment frDateFrom;
    private DateFragment frDateTo;
    private TextView tvCount;
    private ListView listView;
    //    Spinner shopsListSpinner;
    String ExtraVBELN;
    String ExtraStoreId;
    String ExtraStoreName;
    String ExtraStatusName;
    TextView txtHeaderStatus;
    TextView txtHeaderStore;
    TextView txtHeaderNumber;
    ProgressBar progressBar;
    ImageView imgrefresh,imgbacker;
    private PickingDeliverHeaderData pickingDeliverHeaderData;
    private PickingDeliverHeaderListActivityMode pickingDeliverHeaderListActivityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_control_line);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        getWindow().setStatusBarColor(getResources().getColor(R.color.Cyan1)); // Use your desired color

        frDateFrom = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateFrom);
        frDateTo = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateTo);
        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);
//        shopsListSpinner=findViewById(R.id.shopsListSpinner);
        txtHeaderStatus=findViewById(R.id.txt_HeaderStatus);
        txtHeaderNumber=findViewById(R.id.txt_HeaderNumber);
        txtHeaderStore=findViewById(R.id.txtHeaderShop);
        frDateFrom.setTitle("از تاریخ");
        frDateTo.setTitle("تا تاریخ");
        ExtraVBELN =getIntent().getStringExtra("VBELN");
        ExtraStoreId =getIntent().getStringExtra("StoreID");
        ExtraStatusName =getIntent().getStringExtra("StatusName");
        ExtraStoreName =getIntent().getStringExtra("StoreName");
        PersianCalendar pc = new PersianCalendar();
        pc.addPersianDate(PersianCalendar.DATE, -7);
        frDateFrom.setDate(pc);
        frDateTo.setDateToCurrent();
        txtHeaderStore.setText("فروشگاه: "+" ("+ExtraStoreId.toString()+" )"+ExtraStoreName);
        txtHeaderStatus.setText("وضعیت: "+ExtraStatusName.toString()+"");
        txtHeaderNumber.setText("شماره حواله: "+ExtraVBELN.toString()+"");
        progressBar=findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.GONE);
        imgbacker=findViewById(R.id.imgbackeerlinelist);
        imgrefresh=findViewById(R.id.imgrefreshlinelist);
        imgbacker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
        ///TEMP
//        GlobalData.setUserName("sotaheri");
//        GlobalData.setUserName("dlal");
//        GlobalData.setUserName("mafarahani");
        // TODO: 1/11/2023 hatman comment shavad

        if(Utility.isPowerUser())
        {
            frDateFrom.allowChangeDate = true;
            frDateTo.allowChangeDate = true;
        }
        else
        {
            frDateFrom.allowChangeDate = false;
            frDateTo.allowChangeDate = false;
        }

//        Intent intent = getIntent();
//        selectedCompetitor = (CompetitorData) intent.getSerializableExtra("selectedCompetitor");
//        tvStore.setText(selectedCompetitor.getNameWithCompany());

        //region Collapse Params
//        final LinearLayout lExtraParam = findViewById(R.id.lExtraParam);
//        ViewTreeObserver vto = lExtraParam.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
//        {
//            @Override
//            public void onGlobalLayout() {
//                ViewTreeObserver obs = lExtraParam.getViewTreeObserver();
//
//                new ExpandCollapseAnim((ImageButton) findViewById(R.id.ibMinimize)
//                        ,(LinearLayout) findViewById(R.id.lExtraParam)
//                        ,true);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    obs.removeOnGlobalLayoutListener(this);
//                } else {
//                    obs.removeGlobalOnLayoutListener(this);
//                }
//            }
//        });
        //endregion Collapse Params

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                pickingDeliverHeaderData = (PickingDeliverHeaderData)  parent.getItemAtPosition(position);
//                ((row_picking_deliver_header)parent.getAdapter()).setSelection(position);
//            }
//        });

        getList();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.picking_order_list_menu, menu);
        super.onCreateOptionsMenu(menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
//            case R.id.mnuNewPrice:
//                Intent intent = new Intent(this, DefineCompetitorProductActivity.class);
//                intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeInsert);
//                intent.putExtra("selectedCompetitor", selectedCompetitor);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                return true;
            case R.id.mnuSearch:
                getList();
                return true;
            case R.id.mnuExit:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onStart()
    {
        super.onStart();
//        if(!isStarted)
//        {
//            isStarted = true;
//            Utility.setFontBold(tvStore);
//            Utility.increaseTextSize(tvStore,20);
//        }
    }
    @Override
    public void onBackPressed()
    {
        Intent resultIntent = new Intent();
        // You can also put some data in the result if needed
        resultIntent.putExtra("refresh_list", true);
//        resultIntent.putExtra("scrollPosition", getIntent().getIntExtra("scrollPosition", 0));
//
        setResult(RESULT_OK, resultIntent);
//        Intent intent=new Intent(PickingControlLineActivity.this,PickingControlHeaderActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // اگر A در back stack بود، همه فعالیت‌ها بعدش حذف می‌شن و A بالا میاد
//        intent.putExtra("refresh_list", true);
//        startActivity(resultIntent);
        finish();

        super.onBackPressed();
        Utility.hideKeyboard(this);
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void reset()
    {
        tvCount.setText("");
        listView.setAdapter(null);
        pickingDeliverHeaderData = null;
    }
    private void getList()
    {
        progressBar.setVisibility(View.VISIBLE);
        reset();
        if(!Utility.compareTwoDates(this, frDateFrom, frDateTo))
        {
            return;
        }

        pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.BeforeGetList;
//        PickingDeliverService task = new PickingDeliverService(PickingDeliverServiceMode.GetPickingDeliverHeader, this);
//        PropertyInfo pi;
//
//        Log.d("checkthedataasas",task.toString());
//
//        pi = new PropertyInfo();
//        pi.setName("UserName");
//        pi.setValue(GlobalData.getUserName());
//        task.piList.add(pi);
//
//        pi = new PropertyInfo();
//        pi.setName("fromDate");
//        pi.setValue(frDateFrom.getShortDate().substring(0,10));
//        task.piList.add(pi);
//
//        pi = new PropertyInfo();
//        pi.setName("toDate");
//        pi.setValue(frDateTo.getShortDate().substring(0,10));
//        task.piList.add(pi);

        PickingControlHeaderRequestBodyModel requestBody = new PickingControlHeaderRequestBodyModel();
//        requestBody.setStoreID(ExtraStoreId);

        requestBody.setFromDate(frDateFrom.getShortDate().substring(0,10));
        requestBody.setToDate(frDateTo.getShortDate().substring(0,10));
        requestBody.setVBELN(ExtraVBELN);
        MetaData metaData = new  MetaData();
        metaData.setUserName(GlobalData.getUserName());
        metaData.setAppVersionCode(Utilities.getApkVersionCode(PickingControlLineActivity.this));
        metaData.setPassword(GlobalData.getPassword());
        metaData.setAppMode(Utility.applicationMode.toString());
        metaData.setDeviceInfo(Utilities.getDeviceInfo());
        metaData.setStoreID(ExtraStoreId);

        requestBody.setMetaData(metaData);



        // Create Retrofit instance
        PickingApiService apiService = com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient.getRetrofitInstance( ).create(PickingApiService.class);

        // Call the API
        Call<PickingControlHeaderDataResponseBodyModel> call = apiService.GetPickingControlLine(requestBody);
        call.enqueue(new Callback<PickingControlHeaderDataResponseBodyModel>() {
            @Override
            public void onResponse(Call<PickingControlHeaderDataResponseBodyModel> call, Response<PickingControlHeaderDataResponseBodyModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    PickingControlHeaderDataResponseBodyModel responseBody = response.body();
                    if (responseBody != null && responseBody.isSuccessful && responseBody.getPickingHeaderList() != null) {
                        // Handle the successful response


                        ArrayList<PickingDeliverHeaderData> list = (ArrayList<PickingDeliverHeaderData>) responseBody.getPickingHeaderList();
                        ArrayList<PickingDeliverHeaderData> filteredList = new ArrayList<PickingDeliverHeaderData>();
                        filteredList.addAll(list);
                        ArrayList<String> shopsList =new ArrayList<>() ;

                        shopsList.add("همه");
                        for (int i = 0; i < list.size(); i++) {
                            if(!shopsList.contains(list.get(i).getStoreID())){
                                shopsList.add(list.get(i).getStoreID());


                            }else{

                            }
                        }

                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(PickingControlLineActivity.this,
                                R.layout.spinner_item, // Custom layout for items
                                R.id.spinner_text, // TextView ID in spinner_item layout
                                shopsList);
//                        shopsListSpinner.setAdapter(spinnerAdapter);

//                        shopsListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                filteredList.clear();
//
//                                for (int i = 0; i < list.size(); i++) {
//                                    if(shopsList.get(position).toString().equals(list.get(i).getStoreID()) || shopsList.get(position).toString().equals("همه")){
//                                        filteredList.add(list.get(i));
//                                    }
//                                }
//                                row_picking_control_headerLevel2 adapter = new row_picking_control_headerLevel2(PickingControlHeaderActivityLevel2.this, filteredList);
//                                listView.setAdapter(adapter);
//                                Utility.setListCount(adapter.getCount(), tvCount);
//                                Utility.hideKeyboard(PickingControlHeaderActivityLevel2.this);
//                                pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
//
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });


                        row_picking_control_Line adapter = new row_picking_control_Line(PickingControlLineActivity.this, filteredList, PickingControlLineActivity.this);
                        listView.setAdapter(adapter);
                        Utility.setListCount(adapter.getCount(), tvCount);
                        Utility.hideKeyboard(PickingControlLineActivity.this);
                        pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;



                    }
                } else {
                    Utility.showFailureToast(PickingControlLineActivity.this, "دریافت لیست با مشکل مواجه شد");
                }
            }

            @Override
            public void onFailure(Call<PickingControlHeaderDataResponseBodyModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Utility.showFailureToast(PickingControlLineActivity.this, "ارتباط با سرور برقرار نشد");
            }
        });

//        task.listener = this;
//        task.execute();
//        startWait();
    }

    @Override
    public void OnDateChanged(String PersianLongDate, String PersianShortDate)
    {
//        getList();
    }

    @Override
    public void onTaskCompleted(Object result)
    {
//        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this))
        {
            return;
        }
        else if(pickingDeliverHeaderListActivityMode == PickingDeliverHeaderListActivityMode.BeforeGetList)
        {
            reset();
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
            ArrayList<PickingDeliverHeaderData> list = (ArrayList<PickingDeliverHeaderData>) taskResult.dataStructure;
            ArrayList<PickingDeliverHeaderData> filteredList = new ArrayList<PickingDeliverHeaderData>();
            filteredList.addAll(list);
            ArrayList<String> shopsList =new ArrayList<>() ;

            shopsList.add("همه");
            for (int i = 0; i < list.size(); i++) {
                if(!shopsList.contains(list.get(i).getKUNNR())){
                    shopsList.add(list.get(i).getKUNNR());


                }else{

                }
            }
//            Log.d("checkasdasd",shopsListSpinner.toString());
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                    R.layout.spinner_item, // Custom layout for items
                    R.id.spinner_text, // TextView ID in spinner_item layout
                    shopsList);
//            shopsListSpinner.setAdapter(spinnerAdapter);

//            shopsListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                     filteredList.clear();
//
//                    for (int i = 0; i < list.size(); i++) {
//                        if(shopsList.get(position).toString().equals(list.get(i).getKUNNR()) || shopsList.get(position).toString().equals("همه")){
//                           filteredList.add(list.get(i));
//                        }
//                    }
//                    row_picking_deliver_header adapter = new row_picking_deliver_header(PickingControlHeaderActivityLevel2.this, filteredList);
//                    listView.setAdapter(adapter);
//                    Utility.setListCount(adapter.getCount(), tvCount);
//                    Utility.hideKeyboard(PickingControlHeaderActivityLevel2.this);
//                    pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });


//            row_picking_deliver_header adapter = new row_picking_deliver_header(PickingControlLineActivity.this, filteredList);
//            listView.setAdapter(adapter);
//            Utility.setListCount(adapter.getCount(), tvCount);
//            Utility.hideKeyboard(this);
//            pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
//            if(adapter.getCount() >= 200)
//            {
//                Toast.makeText(this,
//                        getString( R.string.only_200_rows)
//                        ,Toast.LENGTH_LONG).show();
//            }
        }
//        else if(competitorProductListActivityMode == CompetitorProductListActivityMode.BeforeDelete)
//        {
////            if(!taskResult.isSuccessful && taskResult.isExceptionOccured("FK_Product_ProductGroup"))
////            {
////                Utility.simpleAlert(this, "گروه کالای انتخابی دارای کالاهای زیرمجموعه بوده و قابل حذف نمی باشد. موارد زير پيشنهاد مي گردد:"
////                        +"\n" + "1. مي توانيد از منوي اصلاح گروه كالا نسبت به غيرفعال كردن آن اقدام نماييد."
////                        +"\n" + "2. نسبت به انتقال كالاهاي زيرمجموعه آن به گروه ديگر اقدام نموده، پس از آن گروه را حذف نماييد.", DialogIcon.Error);
////                return;
////            }
////            else
//            if(!taskResult.isSuccessful)
//            {
//                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
//                return;
//            }
//
//            ArrayAdapter<CompetitorProductData> adapter = (ArrayAdapter<CompetitorProductData>) listView.getAdapter();
//            for (int i = 0; i < adapter.getCount(); i++)
//            {
//                CompetitorProductData pData = adapter.getItem(i);
//                if (pData.getCode() == competitorProductData.getCode())
//                {
//                    adapter.remove(pData);
//                    competitorProductData = null;
//                    adapter.notifyDataSetChanged();
//                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
//                    break;
//                }
//            }
//            Utility.setListCount(adapter.getCount(), (TextView) findViewById(R.id.tvCount));
//            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
//            competitorProductListActivityMode = CompetitorProductListActivityMode.AfterDelete;
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if(requestCode == 1  && resultCode == Activity.RESULT_OK)
        {
            pickingDeliverHeaderData = (PickingDeliverHeaderData) data.getSerializableExtra("pickingDeliverHeaderData");
            //todo farda
            ArrayAdapter<PickingDeliverHeaderData> adapter = (ArrayAdapter<PickingDeliverHeaderData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                PickingDeliverHeaderData pData = adapter.getItem(i);
                if (pData.getID() == pickingDeliverHeaderData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, pickingDeliverHeaderData);
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
        } else if (requestCode == 3  && resultCode == Activity.RESULT_OK) {
            reset();
            getList();

        }
    }


    //    @Override
//    public void OnPickingOrderListCommand(PickingDeliverHeaderData pickingDeliverHeaderData, int position, row_picking_deliver_header.PickingOrderListCommandType commandType)
//    {
//
//    }
    @Override
    public void onItemClick(PickingDeliverHeaderData data) {
        // Handle the click event and start the new activity
        if(data.getStatusID()== PickingOrderStatus.PickedUp.getCode() ||
                data.getStatusID()== PickingOrderStatus.InControl.getCode() ||
                data.getStatusID()== PickingOrderStatus.ControlConfirmed.getCode()
                || data.getStatusID()== PickingOrderStatus.SentToSap.getCode()){
            Intent intent = new Intent(PickingControlLineActivity.this, PickingControlItemListActivity.class);

            intent.putExtra("pickingDeliverHeaderData", data);
            startActivityForResult(intent,3);
        }else{
            Toast.makeText(this, "  امکان مشاهده سفارش صرفا در وضعیت جمع آوری شده وجود دارد", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void OnPickingControlCommand(PickingDeliverHeaderData pickingControlHeaderData, int position, row_picking_control_header.PickingControlCommandType commandType) {
        this.pickingDeliverHeaderData = pickingControlHeaderData;
//        ((row_picking_control_header)listView.getAdapter()).setSelection(position);

//        if(commandType == row_picking_control_header.PickingControlCommandType.Items && !pickingDeliverHeaderData.getOrderType().equalsIgnoreCase("S"))
//        {

        Intent intent = new Intent(this, PickingDeliverItemListActivity.class);
        intent.putExtra("pickingDeliverHeaderData", pickingDeliverHeaderData);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        }
//        else{
//            Intent intent = new Intent(this, PickingItemListActivity_Cold_Chain.class);
//            intent.putExtra("pickingDeliverHeaderData", pickingDeliverHeaderData);
//            startActivityForResult(intent, 1);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        }
//        else if(commandType == row_deliver_order.DeliverOrderListCommandType.Return)
//        {
//            if(this.deliverOrderData.getOrderStatusId() != DeliverOrderStatus.ReadyToSend.getCode())
//            {
//                Toast.makeText(this, "فقط سفارشهايي با وضعيت آماده ارسال قابل برگشت مي باشند.", Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            Intent intent = new Intent(this, DeliverOrderActivity.class);
//            intent.putExtra("deliverMode", DeliverMode.Return);
//            intent.putExtra("deliverOrderData", deliverOrderData);
//            startActivityForResult(intent, 2);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        }
//        else if(commandType == row_deliver_order.DeliverOrderListCommandType.Items)
//        {
//            Intent intent = new Intent(this, DeliverItemListActivity.class);
//            intent.putExtra("deliverOrderData", deliverOrderData);
//            startActivityForResult(intent, 3);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        }

    }


}








//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.oshanak.mobilemarket.Activity.Activity.Enum.PickingControlHeaderActivityMode;
//import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
//import com.oshanak.mobilemarket.Activity.Common.DateFragment;
//import com.oshanak.mobilemarket.Activity.Common.ExpandCollapseAnim;
//import com.oshanak.mobilemarket.Activity.Common.Utility;
//import com.oshanak.mobilemarket.Activity.DataStructure.PickingControlHeaderData;
//import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
//import com.oshanak.mobilemarket.Activity.RowAdapter.row_picking_control_header;
//import com.oshanak.mobilemarket.Activity.Service.Enum.PickingDeliverServiceMode;
//import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
//import com.oshanak.mobilemarket.Activity.Service.PickingDeliverService;
//import com.oshanak.mobilemarket.Activity.Service.TaskResult;
//import com.oshanak.mobilemarket.R;
//
//import java.util.ArrayList;
//
//import ir.hamsaa.persiandatepicker.util.PersianCalendar;
//
//public class PickingControlHeaderActivity extends BaseActivity
//        implements DateFragment.OnDateChangedListener, OnTaskCompleted
//        , row_picking_control_header.OnPickingControlCommandListener
//{
//    private DateFragment frDateFrom;
//    private DateFragment frDateTo;
//    private TextView tvCount;
//    private ListView listView;
//    private EditText etStore;
//    private PickingControlHeaderData pickingControlHeaderData;
//    private PickingControlHeaderActivityMode pickingControlHeaderActivityMode;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_picking_control_header);
//        ////////////////////
//        if (Utility.restartAppIfNeed(this)) return;
//
//        frDateFrom = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateFrom);
//        frDateTo = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateTo);
//        tvCount = findViewById(R.id.tvCount);
//        listView = findViewById(R.id.recyclerview);
//        etStore = findViewById(R.id.etStore);
//
//        frDateFrom.setTitle("از تاریخ");
//        frDateTo.setTitle("تا تاریخ");
//
//        PersianCalendar pc = new PersianCalendar();
//        pc.addPersianDate(PersianCalendar.DATE, -7);
//        frDateFrom.setDate(pc);
//        frDateTo.setDateToCurrent();
//
//        //region Collapse Params
//        final LinearLayout lParamValues = findViewById(R.id.lParamValues);
//        ViewTreeObserver vto = lParamValues.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
//        {
//            @Override
//            public void onGlobalLayout() {
//                ViewTreeObserver obs = lParamValues.getViewTreeObserver();
//
//                new ExpandCollapseAnim( (ImageButton) findViewById(R.id.ibMinimize)
//                        ,(TextView) findViewById(R.id.tvMoreParam)
//                        ,(LinearLayout) findViewById(R.id.lParamValues)
//                        ,true);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    obs.removeOnGlobalLayoutListener(this);
//                } else {
//                    obs.removeGlobalOnLayoutListener(this);
//                }
//            }
//        });
//        //endregion Collapse Params
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                pickingControlHeaderData = (PickingControlHeaderData) parent.getItemAtPosition(position);
//                ((row_picking_control_header)parent.getAdapter()).setSelection(position);
//            }
//        });
////        getList();
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.picking_control_header_menu, menu);
//        super.onCreateOptionsMenu(menu);
//
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case R.id.mnuSearch:
//                getList();
//                return true;
//            case R.id.mnuExit:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//    @Override
//    protected void onStart()
//    {
//        super.onStart();
////        if(!isStarted)
////        {
////            isStarted = true;
////            Utility.setFontBold(tvStore);
////            Utility.increaseTextSize(tvStore,20);
////        }
//    }
//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        Utility.hideKeyboard(this);
////        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//    }
//    private void reset()
//    {
//        tvCount.setText("");
//        listView.setAdapter(null);
////        pickingDeliverHeaderData = null;
//    }
//    private boolean validate()
//    {
//        if(!Utility.compareTwoDates(this, frDateFrom, frDateTo))
//        {
//            return false;
//        }
//        if(Utility.editTextIsEmpty(etStore, "کد فروشگاه را مشخص نمایید")) return false;
//        return true;
//    }
//    private void getList()
//    {
//        reset();
//        if(! validate()) return;
//
//        pickingControlHeaderActivityMode = PickingControlHeaderActivityMode.BeforeGetList;
//        PickingDeliverService task = new PickingDeliverService(PickingDeliverServiceMode.GetPickingControlHeader, this);
//
//        task.addParam("storeID", etStore.getText().toString().trim());
//        task.addParam("fromDate", frDateFrom.getShortDate().substring(0,10));
//        task.addParam("toDate", frDateTo.getShortDate().substring(0,10));
//
//        task.listener = this;
//        task.execute();
//        startWait();
//    }
//
//    @Override
//    public void OnDateChanged(String PersianLongDate, String PersianShortDate)
//    {
////        getList();
//    }
//
//    @Override
//    public void onTaskCompleted(Object result)
//    {
//        stopWait();
//        TaskResult taskResult = (TaskResult) result;
//
//        if (Utility.generalErrorOccurred(taskResult, this))
//        {
//            return;
//        }
//        else if(pickingControlHeaderActivityMode == PickingControlHeaderActivityMode.BeforeGetList)
//        {
//            reset();
//            if(taskResult == null) return;
//
//            if(!taskResult.isSuccessful && !taskResult.message.equals( "No rows found!"))
//            {
//                Utility.simpleAlert(this, getString( R.string.error_in_fetching_data), DialogIcon.Error);
//                return;
//            }
//            else if(!taskResult.isSuccessful && taskResult.message.equals( "No rows found!"))
//            {
//                return;
//            }
//            ArrayList<PickingControlHeaderData> list = (ArrayList<PickingControlHeaderData>) taskResult.dataStructure;
//
//            row_picking_control_header adapter = new row_picking_control_header(this, list);
//            listView.setAdapter(adapter);
//            Utility.setListCount(adapter.getCount(), tvCount);
//            Utility.hideKeyboard(this);
//            pickingControlHeaderActivityMode = PickingControlHeaderActivityMode.AfterGetList;
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult( requestCode, resultCode, data);
//        if(requestCode == 1  && resultCode == Activity.RESULT_OK)
//        {
//            ArrayAdapter<PickingControlHeaderData> adapter = (ArrayAdapter<PickingControlHeaderData>) listView.getAdapter();
//            for (int i = 0; i < adapter.getCount(); i++)
//            {
//                PickingControlHeaderData pData = adapter.getItem(i);
//                if (pData.getID() == pickingControlHeaderData.getID())
//                {
//                    adapter.remove(pData);
//                    pickingControlHeaderData = null;
//                    adapter.notifyDataSetChanged();
//                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
//                    break;
//                }
//            }
//            Utility.setListCount(adapter.getCount(), tvCount);
//        }
//    }
//
//    @Override
//    public void OnPickingControlCommand(PickingControlHeaderData pickingControlHeaderData, int position
//            , row_picking_control_header.PickingControlCommandType commandType)
//    {
//        this.pickingControlHeaderData = pickingControlHeaderData;
//        ((row_picking_control_header)listView.getAdapter()).setSelection(position);
//
//        if(commandType == row_picking_control_header.PickingControlCommandType.Items)
//        {
//
//            Intent intent = new Intent(this, PickingControlItemActivity.class);
//            intent.putExtra("pickingControlHeaderData", pickingControlHeaderData);
//            startActivityForResult(intent, 1);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        }
//    }
//}