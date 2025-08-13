package com.oshanak.mobilemarket.Activity.PickingApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.oshanak.mobilemarket.Activity.Activity.Enum.PickingDeliverHeaderListActivityMode;
import com.oshanak.mobilemarket.Activity.Common.DateFragment;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingDeliverHeaderDataResponseBodyModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingDeliverHeaderRequestBodyModel;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import javadz.beanutils.BeanUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickingDeliverHeaderListActivity extends AppCompatActivity
        implements DateFragment.OnDateChangedListener, OnTaskCompleted, row_picking_deliver_header.OnPickingOrderListCommandListener {
    private DateFragment frDateFrom;
    private DateFragment frDateTo;
    private TextView tvCount;
    private RecyclerView listView;
    ImageView imgrefresh, imgBacker;
    ProgressBar prg;
    private PersianCalendar _persianCalendar = new PersianCalendar();
    AppBarLayout appBarLayout;
    private PickingDeliverHeaderData pickingDeliverHeaderData;
    private PickingDeliverHeaderListActivityMode pickingDeliverHeaderListActivityMode;
    ArrayList<PickingDeliverHeaderData> list;
    TextView txtFromDate, txtToDate;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_deliver_header_redesign);

        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        getWindow().setStatusBarColor(getResources().getColor(R.color.Cyan1)); // Use your desired color

        frDateFrom = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateFrom);
        frDateTo = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateTo);
        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);
        prg = findViewById(R.id.prg_deliver_header_list);
        imgBacker = findViewById(R.id.imgbackerdeliverheader);
        imgrefresh = findViewById(R.id.imgrefreshdeliverheader);
        txtFromDate = findViewById(R.id.txt_from__date);
        txtToDate = findViewById(R.id.txt_to_date);


        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList();
            }
        });
        imgBacker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        frDateFrom.setTitle("از تاریخ");
//        frDateTo.setTitle("تا تاریخ");

        PersianCalendar pc = new PersianCalendar();
        pc.addPersianDate(PersianCalendar.DATE, -7);
        frDateFrom.setDate(pc);
        frDateTo.setDateToCurrent();
        setDate(txtFromDate, pc);
        setDateToCurrent(txtToDate);
        ///TEMP
//        GlobalData.setUserName("sotaheri");
//        GlobalData.setUserName("dlal");
//        GlobalData.setUserName("mafarahani");
        // TODO: 1/11/2023 hatman comment shavad

        if (Utility.isPowerUser()) {
            frDateFrom.allowChangeDate = true;
            frDateTo.allowChangeDate = true;
        } else {
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

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                pickingDeliverHeaderData = (PickingDeliverHeaderData) parent.getItemAtPosition(position);
//                ((row_picking_deliver_header) parent.getAdapter()).setSelection(position);
//            }
//        });

        getList();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.picking_order_list_menu, menu);
//        super.onCreateOptionsMenu(menu);
//
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
////            case R.id.mnuNewPrice:
////                Intent intent = new Intent(this, DefineCompetitorProductActivity.class);
////                intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeInsert);
////                intent.putExtra("selectedCompetitor", selectedCompetitor);
////                startActivity(intent);
////                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////                return true;
//            case R.id.mnuSearch:
//
//                getList();
//                return true;
//            case R.id.mnuExit:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    @Override
    protected void onStart() {
        super.onStart();
//        if(!isStarted)
//        {
//            isStarted = true;
//            Utility.setFontBold(tvStore);
//            Utility.increaseTextSize(tvStore,20);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utility.hideKeyboard(this);
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void setDate(TextView tvDate, PersianCalendar pc) {

        tvDate.setText(pc.getPersianLongDate());
//        tvDate.setTag(Utility.getPersianShortDate(pc));
    }

    public void setDateToCurrent(TextView tvdate) {
        tvdate.setText(new PersianCalendar().getPersianLongDate());
    }

    private void reset() {
        tvCount.setText("0");
        listView.setAdapter(null);
        pickingDeliverHeaderData = null;
    }

    private void getList() {
        prg.setVisibility(View.VISIBLE);
        reset();
        if (!Utility.compareTwoDates(this, frDateFrom, frDateTo)) {
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

        PickingDeliverHeaderRequestBodyModel requestBody = new PickingDeliverHeaderRequestBodyModel();
        requestBody.setUserName(GlobalData.getUserName());
        requestBody.setFromDate(frDateFrom.getShortDate().substring(0, 10));
        requestBody.setToDate(frDateTo.getShortDate().substring(0, 10));

        MetaData metaData = new MetaData();
        metaData.setUserName(GlobalData.getUserName());
        metaData.setAppVersionCode(GlobalData.getAppVersionCode());
        metaData.setPassword(GlobalData.getPassword());
        metaData.setAppMode(Utility.applicationMode.toString());
        metaData.setDeviceInfo(Utilities.getDeviceInfo());
        metaData.setStoreID(GlobalData.getStoreID());

        requestBody.setMetaData(metaData);

        // Create Retrofit instance
        PickingApiService apiService = com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);

        // Call the API
        Call<PickingDeliverHeaderDataResponseBodyModel> call = apiService.getPickingDeliverHeader(requestBody);
        call.enqueue(new Callback<PickingDeliverHeaderDataResponseBodyModel>() {
            @Override
            public void onResponse(Call<PickingDeliverHeaderDataResponseBodyModel> call, Response<PickingDeliverHeaderDataResponseBodyModel> response) {
                prg.setVisibility(View.GONE);
                Log.d("dfsgs",response.body().toString()+"");
                if (response.body().isSuccessful()) {
                    PickingDeliverHeaderDataResponseBodyModel responseBody = response.body();
                    if (responseBody != null && responseBody.isSuccessful && responseBody.getPickingHeaderList() != null) {

                        list = (ArrayList<PickingDeliverHeaderData>) responseBody.getPickingHeaderList();

                        row_picking_deliver_header adapter = new row_picking_deliver_header(PickingDeliverHeaderListActivity.this, list);
                        listView.setLayoutManager(new LinearLayoutManager(PickingDeliverHeaderListActivity.this));

                        listView.setAdapter(adapter);
                        Utility.setListCount(adapter.getItemCount(), tvCount);
                        Utility.hideKeyboard(PickingDeliverHeaderListActivity.this);
                        pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;


                        // Handle the successful response
//                        Log.d("APIcheckthedataasasres", responseBody.toString());
                    }
                } else {
                    Log.d("asdawdasda","...");
                    Log.d("asdawdasda",response.body().getMessage()+"...");
                    Utility.showFailureToast(PickingDeliverHeaderListActivity.this, response.body().getMessage()+"");

//                    Log.e("APIcheckthedataasaserr", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PickingDeliverHeaderDataResponseBodyModel> call, Throwable t) {
                Utility.showFailureToast(PickingDeliverHeaderListActivity.this, "ارتباط با سرور برقرار نشد");
                prg.setVisibility(View.GONE);
            }
        });

//        task.listener = this;
//        task.execute();
//        startWait();
    }


    @Override
    public void OnDateChanged(String PersianLongDate, String PersianShortDate) {
//        getList();
    }

    @Override
    public void onTaskCompleted(Object result) {
//        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this)) {
            return;
        } else if (pickingDeliverHeaderListActivityMode == PickingDeliverHeaderListActivityMode.BeforeGetList) {
            reset();
            if (taskResult == null) return;

            if (!taskResult.isSuccessful && !taskResult.message.equals("No rows found!")) {
                Utility.simpleAlert(this, getString(R.string.error_in_fetching_data), DialogIcon.Error);
                return;
            } else if (!taskResult.isSuccessful && taskResult.message.equals("No rows found!")) {
                return;
            }
            ArrayList<PickingDeliverHeaderData> list = (ArrayList<PickingDeliverHeaderData>) taskResult.dataStructure;

            row_picking_deliver_header adapter = new row_picking_deliver_header(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getItemCount(), tvCount);
            Utility.hideKeyboard(this);
            pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            pickingDeliverHeaderData = (PickingDeliverHeaderData) data.getSerializableExtra("pickingDeliverHeaderData");

            for (int i = 0; i < listView.getAdapter().getItemCount(); i++) {
                PickingDeliverHeaderData pData = list.get(i);
                if (pData.getID() == pickingDeliverHeaderData.getID()) {
                    try {
                        BeanUtils.copyProperties(pData, pickingDeliverHeaderData);
                    } catch (IllegalAccessException ex) {
                        Utility.simpleAlert(this, getString(R.string.error_in_fetching_data));
                    } catch (InvocationTargetException ex) {
                        Utility.simpleAlert(this, getString(R.string.error_in_fetching_data));
                    }
                    listView.getAdapter().notifyDataSetChanged();
                    break;
                }
            }
        }
    }


    @Override
    public void OnPickingOrderListCommand(PickingDeliverHeaderData pickingDeliverHeaderData, int position, row_picking_deliver_header.PickingOrderListCommandType commandType) {
        prg.setVisibility(View.VISIBLE);
        this.pickingDeliverHeaderData = pickingDeliverHeaderData;
        ((row_picking_deliver_header) listView.getAdapter()).setSelection(position);


        Intent intent = new Intent(this, PickingDeliverItemListActivity.class);
        intent.putExtra("pickingDeliverHeaderData", pickingDeliverHeaderData);

        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        prg.setVisibility(View.GONE);


    }
}
