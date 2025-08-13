package com.oshanak.mobilemarket.Activity.PickingApp;


import static android.view.View.GONE;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.oshanak.mobilemarket.Activity.Activity.Enum.PickingDeliverHeaderListActivityMode;
import com.oshanak.mobilemarket.Activity.Common.DateFragment;
import com.oshanak.mobilemarket.Activity.Common.SearchableSpinner;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingControlHeaderDataResponseBodyModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingControlHeaderRequestBodyModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingHeaderStatusRequest;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.updatePickingHeaderStatusResponse;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickingControlHeaderActivity extends AppCompatActivity
        implements DateFragment.OnDateChangedListener, OnTaskCompleted, row_picking_control_header.OnPickingControlCommandListener, row_picking_control_header.OnSendToSapClickListener {
    private DateFragment frDateFrom;
    private DateFragment frDateTo;
    private TextView tvCount;
    private RecyclerView listView;
    TextView txtFromDate, txtToDate;

    SearchableSpinner shopsListSpinner;
    SearchableSpinner itemTypeSpinner;
    ProgressBar progressBar3;
    ImageView imgrefresh;

    private row_picking_control_header adapter;
    ArrayList<PickingDeliverHeaderData> list;
    ArrayList<PickingDeliverHeaderData>  filteredList;
    FrameLayout   overlay ;


    private PickingDeliverHeaderData pickingDeliverHeaderData;
    private PickingDeliverHeaderListActivityMode pickingDeliverHeaderListActivityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_control_header_redesign);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        getWindow().setStatusBarColor(getResources().getColor(R.color.Cyan1)); // Use your desired color

        frDateFrom = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateFrom);
        frDateTo = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateTo);
        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);
        shopsListSpinner = findViewById(R.id.shopsListSpinner);
        itemTypeSpinner = findViewById(R.id.itemTypeSpinner);
        frDateFrom.setTitle("از تاریخ");
        frDateTo.setTitle("تا تاریخ");
        imgrefresh = findViewById(R.id.imgRefreshList);
        PersianCalendar pc = new PersianCalendar();
        pc.addPersianDate(PersianCalendar.DATE, -7);
        frDateFrom.setDate(pc);
        frDateTo.setDateToCurrent();
        progressBar3 = findViewById(R.id.fullscreenProgressBar);

overlay= findViewById(R.id.fullscreenProgressOverlay);
        progressBar3.setVisibility(GONE);
        overlay.setVisibility(GONE);
        txtFromDate = findViewById(R.id.txt_from__date);
        txtToDate = findViewById(R.id.txt_to_date);
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
        imgrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                getList();
            }
        });
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
//
////                pickingDeliverHeaderData = (PickingDeliverHeaderData)  parent.getItemAtPosition(position);
////                ((row_picking_deliver_header)parent.getAdapter()).setSelection(position);
//            }
//        });
        reset();
        getList();
        setDate(txtFromDate, pc);
        setDateToCurrent(txtToDate);
    }

    public void setDate(TextView tvDate, PersianCalendar pc) {

        tvDate.setText(pc.getPersianLongDate());
//        tvDate.setTag(Utility.getPersianShortDate(pc));
    }

    public void setDateToCurrent(TextView tvdate) {
        tvdate.setText(new PersianCalendar().getPersianLongDate());
    }

    private void showInputDialog(PickingDeliverHeaderData data, ProgressBar prg) {
        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(PickingControlHeaderActivity.this);

        View dialogView = inflater.inflate(R.layout.palletecount_dialog_input, null);
        final EditText editTextNumber = dialogView.findViewById(R.id.editTextNumber);

        AlertDialog.Builder builder = new AlertDialog.Builder(PickingControlHeaderActivity.this);
        builder
                .setView(dialogView)
                .setPositiveButton("ثبت", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Utility.hideKeyboardbyView(PickingControlHeaderActivity.this,dialogView);
                        String input = editTextNumber.getText().toString();
                        if (!input.isEmpty()) {
                            prg.setVisibility(View.VISIBLE);
                            int paleteCountNumber = Integer.parseInt(input);
                            sendToSapUsingRestApi(data, paleteCountNumber,prg);
                        } else {
                            Utility.showFailureToast(PickingControlHeaderActivity.this, "لطفا عدد صحیحی را وارد نمایید");
//                            Toast.makeText(PickingControlHeaderActivity.this, "لطفا عدد صحیحی را وارد نمایید", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("کنسل", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utility.hideKeyboardbyView(PickingControlHeaderActivity.this,dialogView);
                        dialog.cancel();
                        prg.setVisibility(GONE);
                    }
                });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void sendToSapUsingRestApi(PickingDeliverHeaderData pickingDeliverHeaderData, int paleteCountNumber, ProgressBar prg) {
        progressBar3.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE);
        Utility.hideKeyboard(PickingControlHeaderActivity.this);


        MetaData metaData = new MetaData(GlobalData.getUserName(), Utilities.getApkVersionCode(PickingControlHeaderActivity.this), GlobalData.getPassword(), Utility.applicationMode.toString(), Utility.getDeviceInfo(), GlobalData.getStoreID() + "");
        UpdatePickingHeaderStatusRequest requestBody = new UpdatePickingHeaderStatusRequest(pickingDeliverHeaderData.getID() + "", 2 + "", paleteCountNumber + "", metaData);

        PickingApiService apiService = com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);

        Call<updatePickingHeaderStatusResponse> call = apiService.updatePickingHeaderStatus(requestBody);

        call.enqueue(new Callback<updatePickingHeaderStatusResponse>() {
            @Override
            public void onResponse(Call<updatePickingHeaderStatusResponse> call, Response<updatePickingHeaderStatusResponse> response) {
                Log.d("checkupdatexxx1",call.request().toString());
                Log.d("checkupdatexxx2",response.body().toString());
                progressBar3.setVisibility(GONE);
                overlay.setVisibility(GONE);
                prg.setVisibility(GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccessful()) {

                    // Handle the successful response
                    updatePickingHeaderStatusResponse apiResponse = response.body();

                    if (apiResponse.isSuccessful()) {

                        reset();
                        getList();
                        Utility.showSuccessToast(PickingControlHeaderActivity.this, "اطلاعات ارسال شد");


                    }
                } else {

                    String errorMessage=response.body().getMessage();
                    errorMessage=errorMessage.replace("error in sending picking list to SAP.","خطا در ارسال لیست جمع آوری به SAP");
//                    Utility.showFailureToast(PickingControlHeaderActivity.this, "ارسال اطلاعات موفقیت آمیز نبود");
                    Utility.showErrorDialog(PickingControlHeaderActivity.this, errorMessage);
                    if(response.body().getSapPickingResult().isItemInventoryAmountNotEnough()){

                        reset();
                        getList();
                    }

//                    Toast.makeText(PickingControlHeaderActivity.this, "ارسال اطلاعات موفقیت آمیز نبود", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(PickingControlHeaderActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<updatePickingHeaderStatusResponse> call, Throwable t) {
                prg.setVisibility(GONE);

//                Toast.makeText(PickingControlHeaderActivity.this, "ارتباط با سرور برقرار نشد", Toast.LENGTH_SHORT).show();
                Utility.showFailureToast(PickingControlHeaderActivity.this, "ارتباط با سرور برقرار نشد");

                progressBar3.setVisibility(GONE);
                overlay.setVisibility(GONE);
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.picking_order_list_menu, menu);
//        super.onCreateOptionsMenu(menu);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
////            case R.id.mnuNewPrice:
////                Intent intent = new Intent(this, DefineCompetitorProductActivity.class);
////                intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeInsert);
////                intent.putExtra("selectedCompetitor", selectedCompetitor);
////                startActivity(intent);
////                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
////                return true;
//            case R.id.mnuSearch:
////                getList();
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

    private void reset() {
        tvCount.setText("");
        listView.setAdapter(null);
        pickingDeliverHeaderData = null;
    }

//    private void getList() {
//        progressBar3.setVisibility(View.VISIBLE);
//        reset();
//        if (!Utility.compareTwoDates(this, frDateFrom, frDateTo)) {
//            return;
//        }
//
//        pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.BeforeGetList;
//
//
//        PickingControlHeaderRequestBodyModel requestBody = new PickingControlHeaderRequestBodyModel();
//        requestBody.setStoreID("0");
//        requestBody.setFromDate(frDateFrom.getShortDate().substring(0, 10));
//        requestBody.setToDate(frDateTo.getShortDate().substring(0, 10));
//
//        MetaData metaData = new MetaData();
//        metaData.setUserName(GlobalData.getUserName());
//        metaData.setAppVersionCode(Utilities.getApkVersionCode(PickingControlHeaderActivity.this));
//        metaData.setPassword(GlobalData.getPassword());
//        metaData.setAppMode(Utility.applicationMode.toString());
//        metaData.setDeviceInfo(Utilities.getDeviceInfo());
//        metaData.setStoreID("0");
//
//        requestBody.setMetaData(metaData);
//        Log.d("consdsdsAPIfggf0", "responseBody");
//
//
//        // Create Retrofit instance
//        PickingApiService apiService = com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);
//
//        // Call the API
//        Call<PickingControlHeaderDataResponseBodyModel> call = apiService.getPickingControlHeader(requestBody);
//        call.enqueue(new Callback<PickingControlHeaderDataResponseBodyModel>() {
//            @Override
//            public void onResponse(Call<PickingControlHeaderDataResponseBodyModel> call, Response<PickingControlHeaderDataResponseBodyModel> response) {
//                progressBar3.setVisibility(View.GONE);
//                if (response.isSuccessful()) {
//                    PickingControlHeaderDataResponseBodyModel responseBody = response.body();
//                    if (responseBody != null && responseBody.isSuccessful && responseBody.getPickingHeaderList() != null) {
//                        // Handle the successful response
//                        Log.d("consdsdsAPIfggf1", responseBody.toString());
//                        Log.d("consdsdsAPIfggf2", call.request().toString());
//
//                        ArrayList<PickingDeliverHeaderData> list = (ArrayList<PickingDeliverHeaderData>) responseBody.getPickingHeaderList();
//                        ArrayList<PickingDeliverHeaderData> filteredList = new ArrayList<PickingDeliverHeaderData>();
//                        filteredList.addAll(list);
//                        ArrayList<String> shopsList = new ArrayList<>();
//                        Map<Integer, String> itemTypesMap = new HashMap<>();
//                        itemTypesMap.put(101, "همه");
//                        itemTypesMap.put(103, "خوراکی");
//                        itemTypesMap.put(104, "شوینده");
//                        itemTypesMap.put(999, "سایر");
//
//                        List<String> itemTypeDisplayList = new ArrayList<>(itemTypesMap.values());
//
//
//
//                        shopsList.add("همه");
//                        for (int i = 0; i < list.size(); i++) {
//                            if (!shopsList.contains(list.get(i).getStoreID())) {
//                                shopsList.add(list.get(i).getStoreID());
//                                Log.d("consdsdsAPIfggf3", list.get(i).getStoreID());
//
//                            } else {
//
//                            }
//                        }
//                        Log.d("consdsdsAPIfggf4", shopsListSpinner.toString());
//                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(PickingControlHeaderActivity.this,
//                                R.layout.spinner_item, // Custom layout for items
//                                R.id.spinner_text, // TextView ID in spinner_item layout
//                                shopsList);
//                        shopsListSpinner.setAdapter(spinnerAdapter);
//                        ArrayAdapter<String> itemsspinnerAdapter = new ArrayAdapter<String>(PickingControlHeaderActivity.this,
//                                R.layout.spinner_item, // Custom layout for items
//                                R.id.spinner_text,itemTypeDisplayList     // TextView ID in spinner_item layout
//                               );
//
//                        itemTypeSpinner.setAdapter(itemsspinnerAdapter);
//
//                        shopsListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                filteredList.clear();
//
//                                for (int i = 0; i < list.size(); i++) {
//                                    if ((shopsList.get(position).toString().equals(list.get(i).getStoreID()) || shopsList.get(position).toString().equals("همه")) && itemstypeList) {
//                                        filteredList.add(list.get(i));
//                                    }
//                                }
//                                row_picking_control_header adapter = new row_picking_control_header(PickingControlHeaderActivity.this,PickingControlHeaderActivity.this, PickingControlHeaderActivity.this, filteredList);
//                                listView.setLayoutManager(new LinearLayoutManager(PickingControlHeaderActivity.this));
//
//                                listView.setAdapter(adapter);
//                                Utility.setListCount(adapter.getItemCount(), tvCount);
//                                Utility.hideKeyboard(PickingControlHeaderActivity.this);
//                                pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
//
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });
//
//
//                        itemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                // Get the selected item's display name
//                                String selectedItem = itemTypeDisplayList.get(position);
//
//                                // Get the corresponding key from the map
//                                for (Map.Entry<Integer, String> entry : itemTypesMap.entrySet()) {
//                                    if (entry.getValue().equals(selectedItem)) {
//                                        int selectedKey = entry.getKey();
//                                        // Now you can use selectedKey in your logic
//                                         break;
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//                                // Handle case when nothing is selected, if required
//                            }
//                        });
//
//                        row_picking_control_header adapter = new row_picking_control_header(PickingControlHeaderActivity.this,PickingControlHeaderActivity.this, PickingControlHeaderActivity.this, filteredList);
//                        listView.setLayoutManager(new LinearLayoutManager(PickingControlHeaderActivity.this));
//
//                        listView.setAdapter(adapter);
//                        Utility.setListCount(adapter.getItemCount(), tvCount);
//                        Utility.hideKeyboard(PickingControlHeaderActivity.this);
//                        pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
//
//
//                    }
//                } else {
//                    Utility.showFailureToast(PickingControlHeaderActivity.this, "دریافت لیست با مشکل مواجه شد");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PickingControlHeaderDataResponseBodyModel> call, Throwable t) {
//                Utility.showFailureToast(PickingControlHeaderActivity.this, "ارتباط با سرور برقرار نشد");
//                progressBar3.setVisibility(View.GONE);
//            }
//        });
//
//    }
private void getList() {
    progressBar3.setVisibility(View.VISIBLE);
    overlay.setVisibility(View.VISIBLE);

    if (!Utility.compareTwoDates(this, frDateFrom, frDateTo)) {
        return;
    }

    pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.BeforeGetList;

    PickingControlHeaderRequestBodyModel requestBody = new PickingControlHeaderRequestBodyModel();
    requestBody.setStoreID("0");
    requestBody.setFromDate(frDateFrom.getShortDate().substring(0, 10));
    requestBody.setToDate(frDateTo.getShortDate().substring(0, 10));

    MetaData metaData = new MetaData();
    metaData.setUserName(GlobalData.getUserName());
    metaData.setAppVersionCode(Utilities.getApkVersionCode(PickingControlHeaderActivity.this));
    metaData.setPassword(GlobalData.getPassword());
    metaData.setAppMode(Utility.applicationMode.toString());
    metaData.setDeviceInfo(Utilities.getDeviceInfo());
    metaData.setStoreID("0");

    requestBody.setMetaData(metaData);
    // Create Retrofit instance
    PickingApiService apiService = com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);

    // Call the API
    Call<PickingControlHeaderDataResponseBodyModel> call = apiService.getPickingControlHeader(requestBody);
    call.enqueue(new Callback<PickingControlHeaderDataResponseBodyModel>() {
        @Override
        public void onResponse(Call<PickingControlHeaderDataResponseBodyModel> call, Response<PickingControlHeaderDataResponseBodyModel> response) {
            progressBar3.setVisibility(GONE);
            overlay.setVisibility(GONE);
            if (response.isSuccessful()) {
                Log.d("checssd",response.body().toString()+"asdasd");
                Log.d("checssd1",call.request().toString()+"asdasd");
                PickingControlHeaderDataResponseBodyModel responseBody = response.body();
                if (responseBody != null && responseBody.isSuccessful && responseBody.getPickingHeaderList() != null) {
                    list = (ArrayList<PickingDeliverHeaderData>) responseBody.getPickingHeaderList();
                    filteredList = new ArrayList<>(list);

                    // Set up the spinners
                    List<String> shopsList = new ArrayList<>();
                    Map<Integer, String> itemTypesMap = new HashMap<>();
                    itemTypesMap.put(0, "همه");
                    itemTypesMap.put(1, "اتمام کنترل");
                    itemTypesMap.put(2, "ارسال شده به سپ");
                    itemTypesMap.put(3, "در حال کنترل");
                    itemTypesMap.put(4, "جمع آوری شده");
                    itemTypesMap.put(5, "در حال جمع آوری");
                    itemTypesMap.put(6, "آماده جمع آوری");
                    List<String> itemTypeDisplayList = new ArrayList<>(itemTypesMap.values());

                    // Populate shopsList with unique store IDs, including "همه"
                    shopsList.add("همه");
                    for (PickingDeliverHeaderData item : list) {
                        String storeID = item.getStoreID();
                        if (!shopsList.contains(storeID)) {
                            shopsList.add(storeID);
                        }
                    }

                    shopsList.remove("همه"); // حذف موقت "همه" برای جلوگیری از به‌هم‌ریختن ترتیب
                    Collections.sort(shopsList); // مرتب‌سازی بر اساس کد فروشگاه
                    shopsList.add(0, "همه");
                    // Create adapters for both spinners
                    ArrayAdapter<String> shopsAdapter = new ArrayAdapter<>(PickingControlHeaderActivity.this,
                            R.layout.spinner_item, R.id.spinner_text, shopsList);
                    shopsListSpinner.setItems(shopsList);

                    ArrayAdapter<String> itemTypeAdapter = new ArrayAdapter<>(PickingControlHeaderActivity.this,
                            R.layout.spinner_item, R.id.spinner_text, itemTypeDisplayList);
                    itemTypeSpinner.setItems(itemTypeDisplayList);

                    // Saving selected values for filtering
                    final String[] selectedStore = {"همه"}; // Default selection
                    final int[] selectedItemTypeKey = {0}; // Default selection (everything)
                    shopsListSpinner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(String item, int position) {


                            selectedStore[0] = shopsList.get(position); // Update selected store
                            filterListAndUpdateView(list, selectedStore[0], selectedItemTypeKey[0]);
                        }
                    });
                    // Spinner selection listeners
//                    shopsListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            if (!isStoreSpinnerInitialized) {
//                                isStoreSpinnerInitialized = true;
//                                return;
//                            }
//
//                            selectedStore[0] = shopsList.get(position); // Update selected store
//                            filterListAndUpdateView(list, selectedStore[0], selectedItemTypeKey[0]);
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {}
//                    });

//                    itemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                            String selectedItem = itemTypeDisplayList.get(position);
//
//                            // Get the corresponding key from the map
//                            for (Map.Entry<Integer, String> entry : itemTypesMap.entrySet()) {
//                                if (entry.getValue().equals(selectedItem)) {
//                                    selectedItemTypeKey[0] = entry.getKey(); // Update selected item type
//                                    break;
//                                }
//                            }
//                            filterListAndUpdateView(list, selectedStore[0], selectedItemTypeKey[0]); // Re-filter when item type changes
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {}
//                    });
                    itemTypeSpinner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(String item, int position) {
                            String selectedItem = itemTypeDisplayList.get(position);

                            // Get the corresponding key from the map
                            for (Map.Entry<Integer, String> entry : itemTypesMap.entrySet()) {
                                if (entry.getValue().equals(selectedItem)) {
                                    selectedItemTypeKey[0] = entry.getKey(); // Update selected item type
                                    break;
                                }
                            }
                            filterListAndUpdateView(list, selectedStore[0], selectedItemTypeKey[0]); // Re-filter when item type changes
                        }
                    });

                    // Initial filtering
                    filterListAndUpdateView(list, selectedStore[0], selectedItemTypeKey[0]);
                } else {
                    Utility.showFailureToast(PickingControlHeaderActivity.this, "دریافت لیست با مشکل مواجه شد");
                }
            } else {
                Utility.showFailureToast(PickingControlHeaderActivity.this, response.message()+"");
            }
        }

        @Override
        public void onFailure(Call<PickingControlHeaderDataResponseBodyModel> call, Throwable t) {
            Utility.showFailureToast(PickingControlHeaderActivity.this, "ارتباط با سرور برقرار نشد");
            progressBar3.setVisibility(GONE);
            overlay.setVisibility(GONE);
        }
    });
}
//    private void getListFromServer() {
//        progressBar3.setVisibility(View.VISIBLE);
//        overlay.setVisibility(View.VISIBLE);
//
//        if (!Utility.compareTwoDates(this, frDateFrom, frDateTo)) {
//            return;
//        }
//
//        pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.BeforeGetList;
//
//        PickingControlHeaderRequestBodyModel requestBody = new PickingControlHeaderRequestBodyModel();
//        requestBody.setStoreID("0");
//        requestBody.setFromDate(frDateFrom.getShortDate().substring(0, 10));
//        requestBody.setToDate(frDateTo.getShortDate().substring(0, 10));
//
//        MetaData metaData = new MetaData();
//        metaData.setUserName(GlobalData.getUserName());
//        metaData.setAppVersionCode(Utilities.getApkVersionCode(PickingControlHeaderActivity.this));
//        metaData.setPassword(GlobalData.getPassword());
//        metaData.setAppMode(Utility.applicationMode.toString());
//        metaData.setDeviceInfo(Utilities.getDeviceInfo());
//        metaData.setStoreID("0");
//
//        requestBody.setMetaData(metaData);
//        // Create Retrofit instance
//        PickingApiService apiService = com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);
//
//        // Call the API
//        Call<PickingControlHeaderDataResponseBodyModel> call = apiService.getPickingControlHeader(requestBody);
//        call.enqueue(new Callback<PickingControlHeaderDataResponseBodyModel>() {
//            @Override
//            public void onResponse(Call<PickingControlHeaderDataResponseBodyModel> call, Response<PickingControlHeaderDataResponseBodyModel> response) {
//                progressBar3.setVisibility(GONE);
//                overlay.setVisibility(GONE);
//                if (response.isSuccessful()) {
//                    Log.d("checssd",response.body().toString()+"asdasd");
//                    Log.d("checssd1",call.request().toString()+"asdasd");
//                    PickingControlHeaderDataResponseBodyModel responseBody = response.body();
//                    if (responseBody != null && responseBody.isSuccessful && responseBody.getPickingHeaderList() != null) {
//                        list.clear();
//                        list = (ArrayList<PickingDeliverHeaderData>) responseBody.getPickingHeaderList();
//                        filteredList = new ArrayList<>(list);
//                        listView.setAdapter(adapter);
//                       listView.getAdapter().notifyDataSetChanged();
//
//                        // Set up the spinners
//
//
//                        // Populate shopsList with unique store IDs, including "همه"
//
//
//
//                        // Create adapters for both spinners
//
//
//                        // Saving selected values for filtering
//
//                        // Spinner selection listeners
//
//
//
//
//
//                        // Initial filtering
//                     } else {
//                        Utility.showFailureToast(PickingControlHeaderActivity.this, "دریافت لیست با مشکل مواجه شد");
//                    }
//                } else {
//                    Utility.showFailureToast(PickingControlHeaderActivity.this, response.message()+"");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PickingControlHeaderDataResponseBodyModel> call, Throwable t) {
//                Utility.showFailureToast(PickingControlHeaderActivity.this, "ارتباط با سرور برقرار نشد");
//                progressBar3.setVisibility(GONE);
//                overlay.setVisibility(GONE);
//            }
//        });
//    }

    // Method to filter the list and update the ListView adapter
    private void filterListAndUpdateView(ArrayList<PickingDeliverHeaderData> originalList, String selectedStore, int selectedItemTypeKey) {
        ArrayList<PickingDeliverHeaderData> filteredList = new ArrayList<>();

        for (PickingDeliverHeaderData item : originalList) {
            // Use both selected criteria to filter
            boolean matchesStore = selectedStore.equals("همه") || selectedStore.equals(item.getStoreID());
            boolean matchesItemType = selectedItemTypeKey == 0 || item.getStatusID() == selectedItemTypeKey; // Assuming getItemTypeKey() gives the correct type

            // Add to filtered list if both conditions are met
            if (matchesStore && matchesItemType) {
                filteredList.add(item);
            }
        }

        // Update the adapter with the filtered list
          adapter = new row_picking_control_header(PickingControlHeaderActivity.this,PickingControlHeaderActivity.this, PickingControlHeaderActivity.this, filteredList);
        listView.setLayoutManager(new LinearLayoutManager(PickingControlHeaderActivity.this));
        listView.setAdapter(adapter);

        Utility.setListCount(adapter.getItemCount(), tvCount);
        Utility.hideKeyboard(PickingControlHeaderActivity.this);
        pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
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
            ArrayList<PickingDeliverHeaderData> filteredList = new ArrayList<PickingDeliverHeaderData>();
            filteredList.addAll(list);
            ArrayList<String> shopsList = new ArrayList<>();

            shopsList.add("همه");
            for (int i = 0; i < list.size(); i++) {
                if (!shopsList.contains(list.get(i).getKUNNR())) {
                    shopsList.add(list.get(i).getKUNNR());


                } else {

                }
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                    R.layout.spinner_item, // Custom layout for items
                    R.id.spinner_text, // TextView ID in spinner_item layout
                    shopsList);
            shopsListSpinner.setItems(shopsList);


            shopsListSpinner.setOnItemSelectedListener(new SearchableSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(String item, int position) {
                    filteredList.clear();

                    for (int i = 0; i < list.size(); i++) {
                        if (shopsList.get(position).toString().equals(list.get(i).getKUNNR()) || shopsList.get(position).toString().equals("همه")) {
                            filteredList.add(list.get(i));
                        }
                    }
                    row_picking_control_header adapter = new row_picking_control_header(PickingControlHeaderActivity.this,PickingControlHeaderActivity.this, PickingControlHeaderActivity.this, filteredList);
                    listView.setLayoutManager(new LinearLayoutManager(PickingControlHeaderActivity.this));

                    listView.setAdapter(adapter);
                    Utility.setListCount(adapter.getItemCount(), tvCount);
                    Utility.hideKeyboard(PickingControlHeaderActivity.this);
                    pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
                }
            });
//            shopsListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    filteredList.clear();
//
//                    for (int i = 0; i < list.size(); i++) {
//                        if (shopsList.get(position).toString().equals(list.get(i).getKUNNR()) || shopsList.get(position).toString().equals("همه")) {
//                            filteredList.add(list.get(i));
//                        }
//                    }
//                    row_picking_control_header adapter = new row_picking_control_header(PickingControlHeaderActivity.this,PickingControlHeaderActivity.this, PickingControlHeaderActivity.this, filteredList);
//                    listView.setLayoutManager(new LinearLayoutManager(PickingControlHeaderActivity.this));
//
//                    listView.setAdapter(adapter);
//                    Utility.setListCount(adapter.getItemCount(), tvCount);
//                    Utility.hideKeyboard(PickingControlHeaderActivity.this);
//                    pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });


            row_picking_control_header adapter = new row_picking_control_header(PickingControlHeaderActivity.this,PickingControlHeaderActivity.this, PickingControlHeaderActivity.this, filteredList);
            listView.setLayoutManager(new LinearLayoutManager(this));

            listView.setAdapter(adapter);

            Utility.setListCount(adapter.getItemCount(), tvCount);
            Utility.hideKeyboard(this);
            pickingDeliverHeaderListActivityMode = PickingDeliverHeaderListActivityMode.AfterGetList;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
//            pickingDeliverHeaderData = (PickingDeliverHeaderData) data.getSerializableExtra("pickingDeliverHeaderData");
////todo farda
//            ArrayAdapter<PickingDeliverHeaderData> adapter = (ArrayAdapter<PickingDeliverHeaderData>) listView.getAdapter();
//            for (int i = 0; i < adapter.getCount(); i++) {
//                PickingDeliverHeaderData pData = adapter.getItem(i);
//                if (pData.getID() == pickingDeliverHeaderData.getID()) {
//                    try {
//                        BeanUtils.copyProperties(pData, pickingDeliverHeaderData);
//                    } catch (IllegalAccessException ex) {
//                        Utility.simpleAlert(this, getString(R.string.error_in_fetching_data));
//                    } catch (InvocationTargetException ex) {
//                        Utility.simpleAlert(this, getString(R.string.error_in_fetching_data));
//                    }
//                    adapter.notifyDataSetChanged();
//                    break;
//                }
//            }
            if (data != null && data.getBooleanExtra("refresh_list", false)) {
                Log.d("gfdsgfdfj",data.getBooleanExtra("refresh_list", false)+"");
                reset();
//getListFromServer();
getList();
            }

        }
    }


//    @Override
//    public void OnPickingOrderListCommand(PickingDeliverHeaderData pickingDeliverHeaderData, int position, row_picking_deliver_header.PickingOrderListCommandType commandType)
//    {
//
//    }

    @Override
    public void OnPickingControlCommand(PickingDeliverHeaderData pickingControlHeaderData, int position, row_picking_control_header.PickingControlCommandType commandType) {
        this.pickingDeliverHeaderData = pickingControlHeaderData;


        Intent intent = new Intent(this, PickingControlLineActivity.class);
        intent.putExtra("VBELN", pickingDeliverHeaderData.getVBELN());
        intent.putExtra("StoreID", pickingDeliverHeaderData.getStoreID());
        intent.putExtra("StatusName", pickingDeliverHeaderData.getStatusName());
        intent.putExtra("StoreName", pickingDeliverHeaderData.getPLANT_NAME());



        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }

    @Override
    public void onSendToSapClick(PickingDeliverHeaderData data,ProgressBar prg) {

        showInputDialog(data,prg);
    }
}


