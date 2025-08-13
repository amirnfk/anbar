package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oshanak.mobilemarket.Activity.Activity.Enum.StoreDeliverOrderActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.InboundHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.InboundHeaderAdapter;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_inbound_header;
import com.oshanak.mobilemarket.Activity.Service.Common;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.OpenInboundApiService;
import com.oshanak.mobilemarket.Activity.Service.OpenInboundRequestModel;
import com.oshanak.mobilemarket.Activity.Service.OpenInboundResponseModel;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.ApiInterface;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Operation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.Doc_Upload_API_Pilot;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.OpenInboundRetrofitClientOperation;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.OpenInboundRetrofitClientPilot;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDeliverInboundHeaderActivity extends BaseActivity implements OnTaskCompleted, row_inbound_header.ResetOperationClickListener
{
    private TextView tvCount;
    private ListView listView;
    private ProgressBar prgOpenInbound;
    ArrayList<InboundHeaderData> list=new ArrayList<>();
    ArrayList<InboundHeaderData> filtered_orders_list;
    row_inbound_header adapter;
    private DrawerLayout drawer;
    FloatingActionButton fabSort;
    TextView txt_from_date;

    LinearLayout lyt_from_date;
    TextView txt_to_date;
    LinearLayout lyt_to_date;

    ImageButton btn_close_filter;
    ImageView img_from_date_clear;
    ImageView img_to_date_clear;
    static long time_stamp_to;
    static long time_stamp_from;
    ImageButton imgsort;
    ImageButton imgGotoButtom;
    ImageButton imgGotoTop;
    ImageButton imgRefresh;
    Spinner spn_order_status;
    Spinner spn_order_send_to_sap_status;
    private StoreDeliverOrderActivityMode storeDeliverOrderActivityMode = StoreDeliverOrderActivityMode.Unknown;
    private InboundHeaderData inboundHeaderData;
    private boolean isNewOperation = false;
    private RecyclerView recyclerView;
    private InboundHeaderAdapter inboundHeaderAdapter;


    @Override
    protected void onRestart() {
        inboundHeaderAdapter.clearData();
        tvCount.setText("");
        inboundHeaderAdapter.notifyDataSetChanged();
        getOpenInboundFromSapUsingRestApi(0);
        // After updating, stop the refreshing indicator
        prgOpenInbound.setVisibility(View.VISIBLE);


        super.onRestart();
    }
    @Override
    protected void onStart() {

        super.onStart();
    }

    private ArrayList<InboundHeaderData> dataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inboundheaderlistlayout);

        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        drawer = findViewById(R.id.drawer_layout);
        fabSort=findViewById(R.id.fab_sort);
        imgsort=findViewById(R.id.imgsort);
        imgGotoButtom=findViewById(R.id.imgGotoButtom);
        imgGotoTop=findViewById(R.id.imgGotoTop);
        imgRefresh=findViewById(R.id.imgRefresh);
        txt_from_date = findViewById(R.id.txt_from_date);
        lyt_from_date = findViewById(R.id.lyt_from_date);
        txt_to_date = findViewById(R.id.txt_to_date);
        recyclerView = findViewById(R.id.recyclerview);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        imgsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);

            }
        });

        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(StoreDeliverInboundHeaderActivity.this);
                String message = "میخواهید اطلاعات حواله ها بازخوانی شود؟";

                dlgAlert.setMessage(message);
                 dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton("بله",
                        (DialogInterface dialog, int which)->
                        {
                            try{
                                inboundHeaderAdapter.clearData();
                                tvCount.setText("");
                                inboundHeaderAdapter.notifyDataSetChanged();
                                spn_order_send_to_sap_status.setSelection(0);
                            }catch(Exception e){

                            }

                            getOpenInboundFromSapUsingRestApi(0);
                            // After updating, stop the refreshing indicator
                            prgOpenInbound.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                        });
                dlgAlert.setNegativeButton("خیر",
                        (DialogInterface dialog, int which)-> {
                        });
                dlgAlert.setIcon(R.drawable.question128);
                dlgAlert.create().show();

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your refresh action here
                // For example, reload data from the server or perform any other task
                // You might use AsyncTask, Retrofit, or any other method to perform the refresh task
                // After completing the task, call setRefreshing(false) to indicate that the refresh is complete
                // For demonstration purposes, I'm using a Handler to simulate a delay
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                        // Update your data or UI here
                        // For example, updateRecyclerView(), updateListView(), etc.

//                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(StoreDeliverInboundHeaderActivity.this);
//                        String message = "میخواهید اطلاعات حواله ها بازخوانی شود؟";
//
//                        dlgAlert.setMessage(message);
//                         dlgAlert.setCancelable(false);
//                        dlgAlert.setPositiveButton("بله",
//                                (DialogInterface dialog, int which)->
//                                {
//                                    inboundHeaderAdapter.clearData();
//                                    inboundHeaderAdapter.notifyDataSetChanged();
//                                    getOpenInboundFromSapUsingRestApi();
//                                    // After updating, stop the refreshing indicator
//                                    prgOpenInbound.setVisibility(View.VISIBLE);
                                    swipeRefreshLayout.setRefreshing(false);
//                                });
//                        dlgAlert.setNegativeButton("خیر",
//                                (DialogInterface dialog, int which)-> {
//                                    swipeRefreshLayout.setRefreshing(false);
//                                });
//                        dlgAlert.setIcon(R.drawable.question128);
//                        dlgAlert.create().show();
//                    }
//                }, 100); // 2000 milliseconds delay (2 seconds)
            }
        });
        lyt_to_date = findViewById(R.id.lyt_to_date);
        tvCount = findViewById(R.id.tvCount);
        prgOpenInbound=findViewById(R.id.prgOpenInound);
//        listView = findViewById(R.id.listv);
        spn_order_status = findViewById(R.id.spn_order_status);
        spn_order_send_to_sap_status = findViewById(R.id.spn_sendtosap_status);
        btn_close_filter=findViewById(R.id.btn_close_filter);
        img_from_date_clear=findViewById(R.id.img_from_date_clear);
        img_to_date_clear=findViewById(R.id.img_to_date_clear);
        prgOpenInbound.setVisibility(View.VISIBLE);
        getOpenInboundFromSapUsingRestApi(0);
        imgGotoButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(inboundHeaderAdapter.getItemCount()-1);
            }
        });
        imgGotoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });
        img_from_date_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_from_date.setText("");
                time_stamp_from=0;
            }
        });
        img_to_date_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_to_date.setText("");
                time_stamp_to=0;
            }
        });
        btn_close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                reset();
                drawer.closeDrawer(GravityCompat.END);
                txt_from_date.setText("");
                time_stamp_from=0;
                txt_to_date.setText("");
                time_stamp_to=0;
                spn_order_status.setSelection(0);
            }
        });
        fabSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);
            }
        });
        lyt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(StoreDeliverInboundHeaderActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(StoreDeliverInboundHeaderActivity.this)
                        .setPositiveButtonString("انتخاب کن")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("نمایش امروز")
                        .setTodayButtonVisible(true)
                        .setMinYear(1400)
                        .setTypeFace(typeface)

                        .setInitDate(Integer.parseInt(Utilities.getCurrentShamsiYear()), Integer.parseInt(Utilities.getCurrentShamsiMonth()), Integer.parseInt(Utilities.getCurrentShamsiDay()))
                        .setActionTextColor(getResources().getColor(R.color.haft_orange))
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(new PersianPickerListener() {
                            @Override
                            public void onDateSelected(@NotNull PersianPickerDate persianPickerDate) {

                                String ppdMonth=persianPickerDate.getPersianMonth()+"";
                                String ppdDay=persianPickerDate.getPersianDay()+"";

                                if(ppdMonth.length()<2){
                                    ppdMonth="0"+ppdMonth;
                                }
                                if(ppdDay.length()<2){
                                    ppdDay="0"+ppdDay;
                                }

                                txt_from_date.setText(persianPickerDate.getPersianYear() + "/" + ppdMonth + "/" + ppdDay);
                                time_stamp_from = persianPickerDate.getTimestamp();

                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                picker.show();
            }
        });
        txt_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(StoreDeliverInboundHeaderActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(StoreDeliverInboundHeaderActivity.this)
                        .setPositiveButtonString("انتخاب کن")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("نمایش امروز")
                        .setTodayButtonVisible(true)
                        .setMinYear(1400)
                        .setTypeFace(typeface)

                        .setInitDate(Integer.parseInt(Utilities.getCurrentShamsiYear()), Integer.parseInt(Utilities.getCurrentShamsiMonth()), Integer.parseInt(Utilities.getCurrentShamsiDay()))
                        .setActionTextColor(getResources().getColor(R.color.haft_orange))
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(new PersianPickerListener() {
                            @Override
                            public void onDateSelected(@NotNull PersianPickerDate persianPickerDate) {

                           String ppdMonth=persianPickerDate.getPersianMonth()+"";
                           String ppdDay=persianPickerDate.getPersianDay()+"";

                           if(ppdMonth.length()<2){
                               ppdMonth="0"+ppdMonth;
                           }
                           if(ppdDay.length()<2){
                               ppdDay="0"+ppdDay;
                           }

                                txt_from_date.setText(persianPickerDate.getPersianYear() + "/" + ppdMonth + "/" + ppdDay);
                                time_stamp_from = persianPickerDate.getTimestamp();
                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                picker.show();
            }
        });
        txt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(StoreDeliverInboundHeaderActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(StoreDeliverInboundHeaderActivity.this)
                        .setPositiveButtonString("انتخاب کن")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("نمایش امروز")
                        .setTodayButtonVisible(true)
                        .setMinYear(1400)
                        .setTypeFace(typeface)

                        .setInitDate(Integer.parseInt(Utilities.getCurrentShamsiYear()), Integer.parseInt(Utilities.getCurrentShamsiMonth()), Integer.parseInt(Utilities.getCurrentShamsiDay()))
                        .setActionTextColor(getResources().getColor(R.color.haft_orange))
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(new PersianPickerListener() {
                            @Override
                            public void onDateSelected(@NotNull PersianPickerDate persianPickerDate) {

                                String ppdMonth=persianPickerDate.getPersianMonth()+"";
                                String ppdDay=persianPickerDate.getPersianDay()+"";

                                if(ppdMonth.length()<2){
                                    ppdMonth="0"+ppdMonth;
                                }
                                if(ppdDay.length()<2){
                                    ppdDay="0"+ppdDay;
                                }

                                txt_to_date.setText(persianPickerDate.getPersianYear() + "/" + ppdMonth + "/" + ppdDay);
                                time_stamp_to = persianPickerDate.getTimestamp();



                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                picker.show();
            }
        });
        lyt_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Typeface typeface = ResourcesCompat.getFont(StoreDeliverInboundHeaderActivity.this, R.font.iransansbold);

                PersianDatePickerDialog picker = new PersianDatePickerDialog(StoreDeliverInboundHeaderActivity.this)
                        .setPositiveButtonString("انتخاب کن")
                        .setNegativeButton("بیخیال")
                        .setTodayButton("نمایش امروز")
                        .setTodayButtonVisible(true)
                        .setMinYear(1400)
                        .setTypeFace(typeface)

                        .setInitDate(Integer.parseInt(Utilities.getCurrentShamsiYear()), Integer.parseInt(Utilities.getCurrentShamsiMonth()), Integer.parseInt(Utilities.getCurrentShamsiDay()))
                        .setActionTextColor(getResources().getColor(R.color.haft_orange))
                        .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                        .setShowInBottomSheet(true)
                        .setListener(new PersianPickerListener() {
                            @Override
                            public void onDateSelected(@NotNull PersianPickerDate persianPickerDate) {

                                String ppdMonth=persianPickerDate.getPersianMonth()+"";
                                String ppdDay=persianPickerDate.getPersianDay()+"";

                                if(ppdMonth.length()<2){
                                    ppdMonth="0"+ppdMonth;
                                }
                                if(ppdDay.length()<2){
                                    ppdDay="0"+ppdDay;
                                }

                                txt_to_date.setText(persianPickerDate.getPersianYear() + "/" + ppdMonth + "/" + ppdDay);
                                time_stamp_to = persianPickerDate.getTimestamp();

                            }

                            @Override
                            public void onDismissed() {

                            }
                        });

                picker.show();
            }
        });
        findViewById(R.id.btn_apply).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.END);
                prgOpenInbound.setVisibility(View.VISIBLE);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOpenInboundFromSapUsingRestApi(spn_order_send_to_sap_status.getSelectedItemPosition());
                    }
                }, 300);


//                Utility.setListCount(adapter.getCount(), tvCount);
//                Utility.hideKeyboard(StoreDeliverInboundHeader.this);
//                storeDeliverOrderActivityMode = StoreDeliverOrderActivityMode.AfterGetList;




            }
        });
//                    getOpenInboundFromSap();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                inboundHeaderData = (InboundHeaderData) parent.getItemAtPosition(position);
////                ((row_inbound_header)parent.getAdapter()).setSelection(position);
////                Toast.makeText(StoreDeliverInboundHeader.this, inboundHeaderData.toString()+"", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void applyFilters() {
        txt_to_date.setText(  Utility.getCurrentPersianShortDate());
        txt_from_date.setText( Utility.getLstWeekShamsiDate()) ;
         prgOpenInbound.setVisibility(View.VISIBLE);

        filtered_orders_list = new ArrayList<InboundHeaderData>();
        filtered_orders_list.clear();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String spinnerText = spn_order_status.getSelectedItem().toString().trim();
                String rowTextType = list.get(i).getLOGGR().toString().trim();
                long rowTimeStamp = list.get(i).getDATEN_M();
                long rowTimeStampFrom = list.get(i).getDATEN_M_From();
                long rowTimeStampTo = list.get(i).getDATEN_M_To();

                if (time_stamp_from==0){
                    time_stamp_from=Utilities.getTimestamp7DaysAgo();
                }
                boolean isStatusMatched = spinnerText.equals("همه") || spinnerText.equals(rowTextType);
                boolean isDateRangeMatched = (time_stamp_from == 0 || time_stamp_from <= rowTimeStampFrom) &&
                        (time_stamp_to == 0 || time_stamp_to >= rowTimeStampTo);
                boolean isShamsiDateMatchs = list.get(i).DATEN_S.equals(txt_from_date.getText().toString().trim()) || list.get(i).DATEN_S.equals(txt_to_date.getText().toString().trim());

                if (isStatusMatched && (isDateRangeMatched || isShamsiDateMatchs)) {
                    filtered_orders_list.add(list.get(i));
                }
            }
            onTaskCompletedforRestApi(filtered_orders_list);

        }
    }

    private void getOpenInboundFromSapUsingRestApi(int sendToSap) {
        Common c = new Common(StoreDeliverInboundHeaderActivity.this);
        String s = c.URL();


        OpenInboundApiService apiService;

        if (s.contains("pilot")) {
            apiService = OpenInboundRetrofitClientPilot.getClient().create(OpenInboundApiService.class);
        } else {
            apiService = OpenInboundRetrofitClientOperation.getClient().create(OpenInboundApiService.class);
        }

        // Prepare your request body
        OpenInboundRequestModel requestModel = new OpenInboundRequestModel();
        requestModel.setStoreID(GlobalData.getStoreID());
        MetaData metaData =   new MetaData(GlobalData.getUserName(), Utilities.getApkVersionCode(StoreDeliverInboundHeaderActivity.this),"","StoreHandheld",Utility.getDeviceInfo(),GlobalData.getStoreID());
        metaData.setUserName(GlobalData.getUserName());
        metaData.setAppVersionCode(GlobalData.getAppVersionCode());
        metaData.setDeviceInfo(Utility.getDeviceInfo()+"");
        metaData.setAppMode("StoreHandheld");
        requestModel.setMetaData(metaData);
        requestModel.setSendToSap(sendToSap);
        // Make the API call
        Call<OpenInboundResponseModel> call = apiService.getOpenInboundHeader(requestModel);
        call.enqueue(new Callback<OpenInboundResponseModel>() {
            @Override
            public void onResponse(Call<OpenInboundResponseModel> call, Response<OpenInboundResponseModel> response) {
                prgOpenInbound.setVisibility(View.GONE);
                Log.e("API_CALL", sendToSap+"resp0onse code: " + response.body().toString());
                Log.e("API_CALLcall", call.request().toString());
                if (response.isSuccessful() && response.body().getInboundHeaderData()!=null) {

                    // Handle successful response
                    OpenInboundResponseModel responseBody = response.body();
                    Log.e("API_CALL", sendToSap+"resp0onse code: " + responseBody.getInboundHeaderData().toString());
                    list= (ArrayList<InboundHeaderData>) responseBody.getInboundHeaderData();
                    tvCount.setText(list.size()+"");

                    applyFilters();
                    // Process the response here
                } else if(response.isSuccessful() && response.body().getMessage().equals("No rows found!")) {
                        try {
                            Toast.makeText(StoreDeliverInboundHeaderActivity.this, " حواله ای با فیلترهای اعمال شده برای نمایش وجود ندارد", Toast.LENGTH_LONG).show();
                            list.clear();
                            inboundHeaderAdapter.notifyDataSetChanged();
                            tvCount.setText("0");

                            applyFilters();
                        }catch (Exception e){

                        }

                } else{
                    Toast.makeText(StoreDeliverInboundHeaderActivity.this, "دریافت اطلاعات با مشکل مواجه شد", Toast.LENGTH_SHORT).show();
                    Log.e("API_CALL", sendToSap+"Failed with code: " + response.code());

                }
                    // Handle error response


            }

            @Override
            public void onFailure(Call<OpenInboundResponseModel> call, Throwable t) {
                prgOpenInbound.setVisibility(View.GONE);
                Toast.makeText(StoreDeliverInboundHeaderActivity.this, "دریافت اطلاعات با مشکل مواجه شد", Toast.LENGTH_SHORT).show();
                // Handle failure
                Log.e("API_CALL", "Failed: " + t.getMessage());
            }
        });


    }

    public void onContinueLastOperation(View view)
    {
        getOpenInboundHeader();
    }
    public void onNewOperation(View view)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        String message = "در صورت ادامه اطلاعات قبلی حذف می گردد آیا مایل به ادامه می باشید؟";

        dlgAlert.setMessage(message);
        dlgAlert.setTitle("هشدار");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("بله",
                (DialogInterface dialog, int which)->
                {
                    getOpenInboundFromSap();
                });
        dlgAlert.setNegativeButton("خیر",
                (DialogInterface dialog, int which)-> {
                });
        dlgAlert.setIcon(R.drawable.question128);
        dlgAlert.create().show();
    }
    private void getOpenInboundFromSap()
    {
        reset();
        isNewOperation = true;

        storeDeliverOrderActivityMode = StoreDeliverOrderActivityMode.BeforeGetList;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetOpenInboundFromSAP, this);
        task.addParam("StoreId", GlobalData.getStoreID());
        task.listener = this;
        task.execute();
        startWait();
    }
    private void getOpenInboundHeader()
    {
        reset();

        storeDeliverOrderActivityMode = StoreDeliverOrderActivityMode.BeforeGetList;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetOpenInboundHeader, this);

        task.addParam("StoreId", GlobalData.getStoreID());

        task.listener = this;
        task.execute();
        startWait();
    }
    public void onTaskCompletedforRestApi(List _result)
    {

        // Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set adapter
        inboundHeaderAdapter = new InboundHeaderAdapter((Context) this, (ArrayList<InboundHeaderData>) _result);
        recyclerView.setAdapter(inboundHeaderAdapter);

        // Populate dataList with your data
        // Example:
        // dataList.add(new InboundHeaderData(...));
        // Add more items as needed

        // Notify adapter about the data change
        inboundHeaderAdapter.notifyDataSetChanged();
        tvCount.setText(inboundHeaderAdapter.getItemCount()+"");
        prgOpenInbound.setVisibility(View.GONE);
//            for (int i=0;i<list.size();i++){
//
//            }

//            adapter= new row_inbound_header((Context) this, (ArrayList<InboundHeaderData>) _result,StoreDeliverInboundHeader.this);
//            adapter.setResetOperationClickListener(this);
//
//            listView.setAdapter(adapter);
//            Utility.setListCount(adapter.getCount(), tvCount);
//            Utility.hideKeyboard(this);
//            storeDeliverOrderActivityMode = StoreDeliverOrderActivityMode.AfterGetList;
//            if(_result.size() >= 200)
//            {
//                Toast.makeText(this,
//                        getString( R.string.only_200_rows)
//                        ,Toast.LENGTH_LONG).show();
//            }

    }
    @Override
    public void onTaskCompleted(Object result)
    {
//        stopWait();
//        TaskResult taskResult = (TaskResult) result;
//
//        if (Utility.generalErrorOccurred(taskResult, this))
//        {
//            return;
//        }
//        else if(storeDeliverOrderActivityMode == StoreDeliverOrderActivityMode.BeforeGetList)
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
//          list = (ArrayList<InboundHeaderData>) taskResult.dataStructure;
//            for (int i=0;i<list.size();i++){
//
//            }
//
//          adapter= new row_inbound_header(this, list ,StoreDeliverInboundHeader.this);
//            adapter.setResetOperationClickListener(this);
//
//            listView.setAdapter(adapter);
//            Utility.setListCount(adapter.getCount(), tvCount);
//            Utility.hideKeyboard(this);
//            storeDeliverOrderActivityMode = StoreDeliverOrderActivityMode.AfterGetList;
//            if(adapter.getCount() >= 200)
//            {
//                Toast.makeText(this,
//                        getString( R.string.only_200_rows)
//                        ,Toast.LENGTH_LONG).show();
//            }
//        }
//        else if(storeDeliverOrderActivityMode == StoreDeliverOrderActivityMode.BeforeContinue)
//        {
//            if(taskResult == null) return;
//
//            if(!taskResult.isSuccessful)
//            {
//                Utility.simpleAlert(this, getString( R.string.error_in_fetching_data), DialogIcon.Error);
//                return;
//            }
//            openInboundDetailActivity();
//        }

    }
    private void reset()
    {
        tvCount.setText("");
        listView.setAdapter(null);
        inboundHeaderData = null;
    }
    public void onCancel(View view)
    {
        onBackPressed();
    }
    public void onContinue(View view)
    {

        if (inboundHeaderData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }

        if(isNewOperation) {
            storeDeliverOrderActivityMode = StoreDeliverOrderActivityMode.BeforeContinue;
            StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetInboundSaleItemFromSAP, this);

            task.addParam("StoreId", GlobalData.getStoreID());
            task.addParam("InboundID", inboundHeaderData.getInboundId());
            task.addParam("OrderType", inboundHeaderData.getOrderType());

            task.listener = this;
            task.execute();
            startWait();
        }
        else
        {
            openInboundDetailActivity();
        }
    }
    private void openInboundDetailActivity()
    {
        Intent intent = new Intent(this, StoreInboundDetailActivity.class);
        intent.putExtra("inboundHeaderData", inboundHeaderData);

        intent.putExtra("inboundHeaderID", inboundHeaderData.getID());
        startActivityForResult(intent,1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            getOpenInboundHeader();
        }
    }


    @Override
    public void onResetOperationClick() {
        onNewOperation(null); // Call onNewOperation method

    }
}