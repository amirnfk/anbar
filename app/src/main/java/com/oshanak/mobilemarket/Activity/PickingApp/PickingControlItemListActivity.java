package com.oshanak.mobilemarket.Activity.PickingApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.oshanak.mobilemarket.Activity.Activity.EditPickingItemActivity;
import com.oshanak.mobilemarket.Activity.Activity.Enum.PickingDeliverItemListActivityMode;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;

import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverItemData;
import com.oshanak.mobilemarket.Activity.DataStructure.SapPickingResult;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Enum.PickingItemStatus;
import com.oshanak.mobilemarket.Activity.Enum.PickingLineItemFilter;
import com.oshanak.mobilemarket.Activity.Enum.PickingOrderStatus;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingItemStatusRequestModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingItemStatusResponseModel;
import com.oshanak.mobilemarket.Activity.Service.Enum.PickingDeliverServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.PickingDeliverService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.GetPickingDeliverItemRequest;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.GetPickingDeliverItemResponse;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingLineStatusRequest;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.updatePickingHeaderStatusResponse;

import javadz.beanutils.BeanUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickingControlItemListActivity extends AppCompatActivity
        implements OnTaskCompleted, row_picking_item.OnPickingItemListCommandListener {

    private TextView tvDocNo;
    private TextView tvStoreId, tvLine;
    private TextView tvDate;
    private TextView tvStatusName;
    private RecyclerView listView;
    private TextView tvCount;
    ImageView imgRefresh, imgScan;
    TextView txtItemsAction;
    EditText searchView;
    private Spinner sItemFilter;
    private TextView tvCollectorName;

    private EditText etCode;
    private PickingDeliverHeaderData pickingDeliverHeaderData;
    private PickingDeliverItemData pickingDeliverItemData;
    private PickingDeliverItemListActivityMode pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.Unknown;
    private boolean headerUpdated = false;
    ArrayList<PickingDeliverItemData> list = new ArrayList<>();
    ProgressBar prg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picking_deliver_itemlist_redesign);

        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        getWindow().setStatusBarColor(getResources().getColor(R.color.Cyan1)); // Use your desired color
        searchView = findViewById(R.id.search_edit_text);
        tvDocNo = findViewById(R.id.tvDocNo);
        tvStoreId = findViewById(R.id.tvStoreId);
        tvCollectorName = findViewById(R.id.tvCollectorName);
        tvLine = findViewById(R.id.tvLine);
        tvDate = findViewById(R.id.tvDate);
        tvStatusName = findViewById(R.id.tvStatusName);
        listView = findViewById(R.id.recyclerview);
        tvCount = findViewById(R.id.tvCount);
        sItemFilter = findViewById(R.id.sItemFilter);
        etCode = findViewById(R.id.etCode);
        imgScan = findViewById(R.id.imgScan);
        imgRefresh = findViewById(R.id.imgRefreshitems);
        txtItemsAction = findViewById(R.id.txtItemsAction);
        Intent intent = getIntent();
        pickingDeliverHeaderData = (PickingDeliverHeaderData) intent.getSerializableExtra("pickingDeliverHeaderData");
        prg = findViewById(R.id.progressBar5);
        tvCollectorName.setText(" کالکتور :" + pickingDeliverHeaderData.getCollectorName());
        tvStoreId.setText(String.valueOf(pickingDeliverHeaderData.getStoreID()));
        tvDocNo.setText(pickingDeliverHeaderData.getVBELN());
//        tvStoreId.setText(String.valueOf(pickingDeliverHeaderData.getKUNNR()));
        tvDate.setText(pickingDeliverHeaderData.getBLDAT());
        tvStatusName.setText(pickingDeliverHeaderData.getStatusName() + " (" + "لاین: " + pickingDeliverHeaderData.getLine() + ") ");
        txtItemsAction.setText("آغاز کنترل");
//tvLine.setText("لاین: "+pickingDeliverHeaderData.getLine());
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                pickingDeliverItemData = (PickingDeliverItemData) parent.getItemAtPosition(position);
//                ((row_picking_item) parent.getAdapter()).setSelection(position);
//            }
//        });


        if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.PickedUp.getCode()) {
            txtItemsAction.setVisibility(View.VISIBLE);
            txtItemsAction.setText("آغاز کنترل");
        } else if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InControl.getCode()) {
            txtItemsAction.setVisibility(View.VISIBLE);
            txtItemsAction.setText("خاتمه کنترل");
        } else {
            txtItemsAction.setVisibility(View.GONE);
//            mnuAction.setVisible(false);
        }


        txtItemsAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtItemsAction.setEnabled(false);
                updateHeaderStatus();
                txtItemsAction.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtItemsAction.setEnabled(true);
                    }
                }, 1000);
            }
        });
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgRefresh.setEnabled(false);
                getListUsiingRestApi();
                imgRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgRefresh.setEnabled(true);
                    }
                }, 1000);

            }
        });
        imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(PickingControlItemListActivity.this);
                integrator.setPrompt("باركد كالا را دقيقاً داخل كادر قرار دهيد");
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.initiateScan();
            }
        });


        sItemFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reset();
                getListUsiingRestApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Utility.initPickingLineItemFilterSpinner(this, sItemFilter);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        if (headerUpdated) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("pickingDeliverHeaderData", pickingDeliverHeaderData);
            setResult(Activity.RESULT_OK, returnIntent);


        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void reset() {
        tvCount.setText("");
        listView.setAdapter(null);
        pickingDeliverItemData = null;
    }

    private boolean validateChangeHeaderStatus() {
        Log.d("chvalidateChangeHeaderStatus",pickingDeliverHeaderData.getStatusID()+"..."+ PickingOrderStatus.PickedUp.getCode());
        if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InControl.getCode()) {
            for (int i = 0; i < list.size(); i++) {
                PickingDeliverItemData pData = list.get(i);
                if (pData.getStatusID() == PickingItemStatus.Unknown.getCode()) {
                    Utility.simpleAlert(this, "قبل از خاتمه کنترل بايد كليه اقلام سفارش را تعيين تكليف نماييد.", DialogIcon.Warning);
                    return false;
                }
            }
        }
        return true;
    }

    private void updateHeaderStatus() {


        if (!validateChangeHeaderStatus()) return;
        PickingOrderStatus status = PickingOrderStatus.Unknown;

        if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.PickedUp.getCode()) {
            status = PickingOrderStatus.InControl;
        } else if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InControl.getCode()) {
            status = PickingOrderStatus.ControlConfirmed;
        }

//        if (status == PickingOrderStatus.PickedUp) {
//            Intent intent = new Intent(this, PickingEnterPalletActivity.class);
//            startActivityForResult(intent, 2);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            return;
//        }

        updateHeaderUsingRestApi(status);
    }

    private void updateHeaderUsingRestApi(PickingOrderStatus status) {
        prg.setVisibility(View.VISIBLE);
        MetaData metaData = new MetaData(GlobalData.getUserName(), Utilities.getApkVersionCode(PickingControlItemListActivity.this), GlobalData.getPassword(), Utility.applicationMode.toString(), Utility.getDeviceInfo(), GlobalData.getStoreID() + "");
        UpdatePickingLineStatusRequest requestBody = new UpdatePickingLineStatusRequest(pickingDeliverHeaderData.getLineID() + "", status.getCode() + "", metaData);

        PickingApiService apiService = com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);

        Call<updatePickingHeaderStatusResponse> call = apiService.UpdatePickingLineStatus(requestBody);

        call.enqueue(new Callback<updatePickingHeaderStatusResponse>() {
            @Override
            public void onResponse(Call<updatePickingHeaderStatusResponse> call, Response<updatePickingHeaderStatusResponse> response) {
                prg.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    // Handle the successful response
                    updatePickingHeaderStatusResponse apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {


                        SapPickingResult sapPickingResult = (SapPickingResult) response.body().getSapPickingResult();
                        if (!response.isSuccessful() && sapPickingResult.isItemNotExistInSAP()) {

                            Utility.simpleAlert(PickingControlItemListActivity.this, "اقلامي در سفارش موجود است كه در سفارش معادل آن در سپ وجود ندارد." + "\n" +
                                    "جهت اطلاعات بيشتر لازم است سفارش در سپ بررسي شود." + "\n" + response.body().getSapPickingResult().getMessage(), DialogIcon.Error);
                            return;
                        }
//                            else if (!response.isSuccessful() && response.body().getSapPickingResult().isExceptionOccured("error in sending picking list to SAP")) {
//                                Utility.simpleAlert(this, "خطا هنگام ارسال اطلاعات به سپ." + "\n" + taskResult.message
//                                        , DialogIcon.Error);
//                                return;
//                            }

//                            else if (!taskResult.isSuccessful) {
//
//                                Utility.simpleAlert(this, getString(R.string.general_error), DialogIcon.Error);
//                                return;
//                            }

                        String message = "";
                        if (sapPickingResult.isItemRejectedByUser()) {
                            message += "سفارش با شرايط زير به سپ منتقل گرديد:" + "\n\n";
                            message += "1. تعدادي از اقلام سفارش توسط كاربر (شما) از سفارش حذف گرديد.";
                        }
                        if (sapPickingResult.isItemDeletedDeficit()) {
                            message += "\n" + "2. تعدادي از اقلام سفارش به دليل ناكافي بودن موجودي از سفارش حذف گرديد.";
                        }

                        PickingOrderStatus status = PickingOrderStatus.Unknown;
                        if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.PickedUp.getCode()) {
                            status = PickingOrderStatus.InControl;
//                mnuAction.setTitle("خاتمه جمع آوري");
                            txtItemsAction.setVisibility(View.VISIBLE);
                            txtItemsAction.setText("خاتمه کنترل");
                        } else if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InControl.getCode()) {
                            status = PickingOrderStatus.ControlConfirmed;
//                mnuAction.setVisible(false);
                            txtItemsAction.setVisibility(View.VISIBLE);
                            txtItemsAction.setText("");
                        } else {
                            txtItemsAction.setVisibility(View.GONE);

                        }
                        pickingDeliverHeaderData.setStatusID(status.getCode());
                        pickingDeliverHeaderData.setStatusName(status.getDescription());
                        tvStatusName.setText(pickingDeliverHeaderData.getStatusName());
                        headerUpdated = true;

                        if (message.equals("")) {
                            Utility.showSuccessToast(PickingControlItemListActivity.this, "وضعيت سفارش تغيير يافت");

//                                Toast.makeText(PickingControlItemListActivity.this, "وضعيت سفارش تغيير يافت", Toast.LENGTH_SHORT).show();
                        } else {
                            Utility.simpleAlert(PickingControlItemListActivity.this, message, DialogIcon.Info);
                        }
                        pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterUpdateHeaderStatus;


                        // Process the successful response data
//                        Log.d("asdasdAPI succ", "Response : " + response.errorBody()+status.getCode());
//                        PickingOrderStatus status = PickingOrderStatus.Unknown;
//                        if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.Ready.getCode()) {
//                            status = PickingOrderStatus.InPacking;
////                mnuAction.setTitle("خاتمه جمع آوري");
//                            txtItemsAction.setVisibility(View.VISIBLE);
//                            txtItemsAction.setText("خاتمه جمع آوری");
//                        } else if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InPacking.getCode()) {
//                            status = PickingOrderStatus.PickedUp;
////                mnuAction.setVisible(false);
//                            txtItemsAction.setVisibility(View.GONE);
//                        }
//                        pickingDeliverHeaderData.setStatusID(status.getCode());
//                        pickingDeliverHeaderData.setStatusName(status.getDescription());
//                        tvStatusName.setText(pickingDeliverHeaderData.getStatusName());
//                        headerUpdated = true;
//
//
//                        pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterUpdateHeaderStatus;
//                    } else if (pickingDeliverItemListActivityMode == PickingDeliverItemListActivityMode.BeforeUpdateItemStatus) {
//                        if (! response.isSuccessful()) {
//                            Utility.simpleAlert(PickingItemListActivity.this, getString(R.string.general_error), DialogIcon.Error);
//                            return;
//                        }
//
//                        Toast.makeText(PickingItemListActivity.this, "وضعيت تغيير يافت", Toast.LENGTH_SHORT).show();
//                        pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterUpdateItemStatus;


                    }
                } else {
                    Utility.showFailureToast(PickingControlItemListActivity.this, "مشکلی در انجام درخواست شما رخ داد");
                }
            }

            @Override
            public void onFailure(Call<updatePickingHeaderStatusResponse> call, Throwable t) {
                prg.setVisibility(View.GONE);
                Utility.showFailureToast(PickingControlItemListActivity.this, "ارتباط با سرور برقرار نشد.");

            }
        });
//        pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.BeforeUpdateHeaderStatus;
//        PickingDeliverService task = new PickingDeliverService(PickingDeliverServiceMode.UpdatePickingHeaderStatus, this);
//        PropertyInfo pi;
//
//        pi = new PropertyInfo();
//        pi.setName("HeaderID");
//        pi.setValue(pickingDeliverHeaderData.getID());
//        task.piList.add(pi);
//
//        Log.d("sadasdasdasd",status.toString()+"...."+palletCount+"");
//
//        pi = new PropertyInfo();
//        pi.setName("StatusID");
//        pi.setValue(status.getCode());
//        task.piList.add(pi);
//
//        pi = new PropertyInfo();
//        pi.setName("PalletCount");
//        pi.setValue(palletCount);
//        task.piList.add(pi);
//
//        task.listener = this;
//        task.execute();
//        startWait();
    }


    private void getListUsiingRestApi() {
        prg.setVisibility(View.VISIBLE);


        PickingApiService apiService = com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);
        MetaData metaData = new MetaData(
                GlobalData.getUserName(), Utilities.getApkVersionCode(this), GlobalData.getPassword(), Utility.applicationMode.toString(), Utility.getDeviceInfo(), GlobalData.getStoreID()
        );
        GetPickingDeliverItemRequest request = new GetPickingDeliverItemRequest(pickingDeliverHeaderData.getID() + "", pickingDeliverHeaderData.getLineID(), ((PickingLineItemFilter) sItemFilter.getSelectedItem()).getCode() + "", metaData);

        Call<GetPickingDeliverItemResponse> call = apiService.getPickingDeliverItem(request);
        call.enqueue(new Callback<GetPickingDeliverItemResponse>() {
            @Override
            public void onResponse(Call<GetPickingDeliverItemResponse> call, Response<GetPickingDeliverItemResponse> response) {
                prg.setVisibility(View.GONE);


                if (response.isSuccessful() && response.body() != null && response.body().getPickingItemList() != null && response.body().getPickingItemList().size() > 0) {
                    GetPickingDeliverItemResponse data = response.body();
                    // Do something with the data (e.g., display it)

                    list = (ArrayList<PickingDeliverItemData>) response.body().getPickingItemList();

                    row_picking_item adapter = new row_picking_item(PickingControlItemListActivity.this, list);
                    listView.setLayoutManager(new LinearLayoutManager(PickingControlItemListActivity.this));

                    listView.setAdapter(adapter);
                    Utility.setListCount(adapter.getItemCount(), tvCount);
                    Utility.hideKeyboard(PickingControlItemListActivity.this);
                    pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterGetList;

                    searchView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            // Do nothing
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            // Perform the search whenever the text changes
                            adapter.filter(s.toString().trim()); // Call performSearch with the new text
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Do nothing
                        }
                    });

                } else if (response.body().getPickingItemList() != null && response.body().getPickingItemList().size() == 0) {
                    Utility.showFailureToast(PickingControlItemListActivity.this, "آیتمی برای نمایش وجود ندارد");
//                    Toast.makeText(PickingControlItemListActivity.this, "آیتمی برای نمایش وجود ندارد", Toast.LENGTH_SHORT).show();
                } else {
                    Utility.showFailureToast(PickingControlItemListActivity.this, "دریافت لیست با مشکل مواجه شد");
                }
            }

            @Override
            public void onFailure(Call<GetPickingDeliverItemResponse> call, Throwable t) {
                Utility.showFailureToast(PickingControlItemListActivity.this, "ارتباط با سرور برقرار نشد");
                prg.setVisibility(View.GONE);

            }
        });


    }


    @Override
    public void onTaskCompleted(Object result) {
//        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this)) {
            return;
        } else if (pickingDeliverItemListActivityMode == PickingDeliverItemListActivityMode.BeforeGetList) {
            reset();
            if (taskResult == null) return;

            if (!taskResult.isSuccessful && !taskResult.message.equals("No rows found!")) {
                Utility.simpleAlert(this, getString(R.string.error_in_fetching_data), DialogIcon.Error);
                return;
            } else if (!taskResult.isSuccessful && taskResult.message.equals("No rows found!")) {
                return;
            }
            ArrayList<PickingDeliverItemData> list = (ArrayList<PickingDeliverItemData>) taskResult.dataStructure;

            row_picking_item adapter = new row_picking_item(this, list);
            listView.setLayoutManager(new LinearLayoutManager(this));

            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getItemCount(), tvCount);
            Utility.hideKeyboard(this);
            pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterGetList;

        } else if (pickingDeliverItemListActivityMode == PickingDeliverItemListActivityMode.BeforeUpdateHeaderStatus) {
            SapPickingResult sapPickingResult = (SapPickingResult) taskResult.dataStructure;
            if (!taskResult.isSuccessful && sapPickingResult.isItemNotExistInSAP()) {

                Utility.simpleAlert(this, "اقلامي در سفارش موجود است كه در سفارش معادل آن در سپ وجود ندارد." + "\n" +
                        "جهت اطلاعات بيشتر لازم است سفارش در سپ بررسي شود." + "\n" + taskResult.message, DialogIcon.Error);
                return;
            } else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("error in sending picking list to SAP")) {
                Utility.simpleAlert(this, "خطا هنگام ارسال اطلاعات به سپ." + "\n" + taskResult.message
                        , DialogIcon.Error);
                return;
            } else if (!taskResult.isSuccessful) {

                Utility.simpleAlert(this, getString(R.string.general_error), DialogIcon.Error);
                return;
            }

            String message = "";
            if (sapPickingResult.isItemRejectedByUser()) {
                message += "سفارش با شرايط زير به سپ منتقل گرديد:" + "\n\n";
                message += "1. تعدادي از اقلام سفارش توسط كاربر (شما) از سفارش حذف گرديد.";
            }
            if (sapPickingResult.isItemDeletedDeficit()) {
                message += "\n" + "2. تعدادي از اقلام سفارش به دليل ناكافي بودن موجودي از سفارش حذف گرديد.";
            }

            PickingOrderStatus status = PickingOrderStatus.Unknown;
            if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.PickedUp.getCode()) {
                status = PickingOrderStatus.InControl;
//                mnuAction.setTitle("خاتمه جمع آوري");
                txtItemsAction.setVisibility(View.VISIBLE);
                txtItemsAction.setText("خاتمه کنترل");
            } else if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InControl.getCode()) {
                status = PickingOrderStatus.ControlConfirmed;
//                mnuAction.setVisible(false);
                txtItemsAction.setVisibility(View.GONE);
            }
            pickingDeliverHeaderData.setStatusID(status.getCode());
            pickingDeliverHeaderData.setStatusName(status.getDescription());
            tvStatusName.setText(pickingDeliverHeaderData.getStatusName());
            headerUpdated = true;

            if (message.equals("")) {
                Utility.showSuccessToast(PickingControlItemListActivity.this, "وضعيت سفارش تغيير يافت");
//                Toast.makeText(this, "وضعيت سفارش تغيير يافت", Toast.LENGTH_SHORT).show();
            } else {
                Utility.simpleAlert(this, message, DialogIcon.Info);
            }
            pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterUpdateHeaderStatus;
        } else if (pickingDeliverItemListActivityMode == PickingDeliverItemListActivityMode.BeforeUpdateItemStatus) {
            if (!taskResult.isSuccessful) {
                Utility.simpleAlert(this, getString(R.string.general_error), DialogIcon.Error);
                return;
            }
            int OrgDeliverAmount = Integer.parseInt(taskResult.tag.toString());
            setItemStatus(OrgDeliverAmount);
            Utility.showSuccessToast(PickingControlItemListActivity.this, "وضعيت سفارش تغيير يافت");
//            Toast.makeText(this, "وضعيت تغيير يافت", Toast.LENGTH_SHORT).show();
            pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterUpdateItemStatus;
        }
    }

    private void setItemStatus(int OrgDeliverAmount) {
//        ArrayAdapter<PickingDeliverItemData> adapter = (ArrayAdapter<PickingDeliverItemData>) listView.getAdapter();
        for (int i = 0; i < list.size(); i++) {
            PickingDeliverItemData pData = list.get(i);
            if (pData.getID() == pickingDeliverItemData.getID()) {
                pData.setStatusID(newStatus.getCode());
                pData.setStatusName(newStatus.getDescription());
                if (newStatus == PickingItemStatus.Unknown && OrgDeliverAmount > 0) {
                    pData.setDeliverAmount(OrgDeliverAmount);
                    pickingDeliverItemData.setDeliverAmount(OrgDeliverAmount);
                }
                if (newStatus.getCode() == PickingItemStatus.Reject.getCode()) {
                    pData.setDeliverAmount(0);
                    pickingDeliverItemData.setDeliverAmount(0);
                }
                listView.getAdapter().notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            pickingDeliverItemData = (PickingDeliverItemData) data.getSerializableExtra("pickingDeliverItemData");
            for (int i = 0; i < list.size(); i++) {
                PickingDeliverItemData pData = list.get(i);
                if (pData.getID() == pickingDeliverItemData.getID()) {
                    try {
                        BeanUtils.copyProperties(pData, pickingDeliverItemData);
                    } catch (IllegalAccessException ex) {
                        Utility.simpleAlert(this, getString(R.string.error_in_fetching_data));
                    } catch (InvocationTargetException ex) {
                        Utility.simpleAlert(this, getString(R.string.error_in_fetching_data));
                    }
                    listView.getAdapter().notifyDataSetChanged();
                    break;
                }
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            int palletCount = data.getIntExtra("palletCount", 0);
            updateHeaderUsingRestApi(PickingOrderStatus.PickedUp);
        } else if (requestCode == GlobalData.getBarcodeActivityRequestCode()) {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult == null || scanningResult.getContents() == null) {
                Utility.showFailureToast(PickingControlItemListActivity.this, "بارکد کالا به درستی اسکن نشد");
//                Toast.makeText(this, "بارکد کالا به درستی اسکن نشد.", Toast.LENGTH_SHORT).show();

            } else {
                String scanContent = scanningResult.getContents();
                moveToPickingItem(scanContent, 1);
            }
        }
    }

    private void updateItemStatus() {
        if (!editItemValidation()) return;

        prg.setVisibility(View.VISIBLE);
//        if (!editItemValidation()) return;

        pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.BeforeUpdateItemStatus;
        MetaData metaData = new MetaData(GlobalData.getUserName(), Utilities.getApkVersionCode(PickingControlItemListActivity.this), GlobalData.getPassword(), Utility.applicationMode.toString(), Utility.getDeviceInfo(), GlobalData.getStoreID() + "");
        UpdatePickingItemStatusRequestModel requestBody = new UpdatePickingItemStatusRequestModel(pickingDeliverItemData.getID(), newStatus.getCode(), metaData);

        PickingApiService apiService = com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);

        Call<UpdatePickingItemStatusResponseModel> call = apiService.UpdatePickingItemStatus(requestBody);

        call.enqueue(new Callback<UpdatePickingItemStatusResponseModel>() {
            @Override
            public void onResponse(Call<UpdatePickingItemStatusResponseModel> call, Response<UpdatePickingItemStatusResponseModel> response) {
                prg.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    // Handle the successful response
                    UpdatePickingItemStatusResponseModel apiResponse = response.body();
                    if (apiResponse.isSuccessful()) {
//                        getListUsiingRestApi();


                        int OrgDeliverAmount = Integer.parseInt(apiResponse.getOrgDeliverAmount() + "");
                        setItemStatus(OrgDeliverAmount);
//                        Utility.showSuccessToast(PickingControlItemListActivity.this,"وضعيت سفارش تغيير يافت");
//            Toast.makeText(this, "وضعيت تغيير يافت", Toast.LENGTH_SHORT).show();
                        pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterUpdateItemStatus;

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<UpdatePickingItemStatusResponseModel> call, Throwable t) {
                Utility.showFailureToast(PickingControlItemListActivity.this, "مشکلی در ارتباط با سرور رخ داده است.");
                prg.setVisibility(View.GONE);

            }
        });
//        if (!editItemValidation()) return;
//
//        pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.BeforeUpdateItemStatus;
//        PickingDeliverService task = new PickingDeliverService(PickingDeliverServiceMode.UpdatePickingItemStatus, this);
//        PropertyInfo pi;
//
//        pi = new PropertyInfo();
//        pi.setName("ItemID");
//        pi.setValue(pickingDeliverItemData.getID());
//        task.piList.add(pi);
//
//        pi = new PropertyInfo();
//        pi.setName("StatusID");
//        pi.setValue(newStatus.getCode());
//        task.piList.add(pi);
//
//        task.listener = this;
//        task.execute();
//        startWait();
    }

    private boolean editItemValidation() {
        if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.PickedUp.getCode() ||
        pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.ControlConfirmed.getCode() ||
         pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InPacking.getCode() ||
         pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.Unknown.getCode() ||
         pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.Ready.getCode()
        ) {
            Utility.simpleAlert(this, "امكان اصلاح در خصوص سفارشات با وضعیت فعلی وجود ندارد", DialogIcon.Info);
            return false;
        }
        return true;
    }

    PickingItemStatus newStatus = PickingItemStatus.Unknown;

    @Override
    public void OnPickingItemListCommand(PickingDeliverItemData pickingDeliverItemData, PickingItemStatus newStatus, int position, row_picking_item.PickingItemListCommandType commandType, int minimumInventory, String orderType) {

        this.pickingDeliverItemData = pickingDeliverItemData;
        this.newStatus = newStatus;

//        ((row_picking_item) listView.getAdapter()).setSelectedPosition(position);

        if (commandType == row_picking_item.PickingItemListCommandType.Edit) {
            if (!editItemValidation()) return;

            Intent intent = new Intent(this, EditPickingItemActivity.class);
            intent.putExtra("pickingDeliverItemData", pickingDeliverItemData);
            intent.putExtra("minimumInventory", minimumInventory);
            intent.putExtra("orderType", orderType);


            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (commandType == row_picking_item.PickingItemListCommandType.Confirm) {
            updateItemStatus();
        } else if (commandType == row_picking_item.PickingItemListCommandType.Reject) {
            updateItemStatus();
        } else if (commandType == row_picking_item.PickingItemListCommandType.ShowInventory) {
            if (!Utility.isPowerUser()) return;
            String message = "موجودي(واحد جزء): " + pickingDeliverItemData.getLBKUM() + "\n" +
                    "موجودي(واحد کل): " + pickingDeliverItemData.getLBKUM() / pickingDeliverItemData.getUMVKZ() + "\n" +
                    "واحد جزء در كل: " + pickingDeliverItemData.getUMVKZ();
            Utility.simpleAlert(this, message);
        }
    }

    public void onSearch(View view) {
        moveToPickingItem(etCode.getText().toString().trim(), 2);
    }

    private void moveToPickingItem(String code, int mode) {
        Utility.hideKeyboard(this);
        if (!code.equals("")) {
            for (int i = 0; i < list.size(); i++) {
                PickingDeliverItemData pData = list.get(i);
                if (pData.getBARCODE().equals(code) || pData.getMATNR().equals(code)) {
//                    listView.setSelection(i);
//                    pickingDeliverItemData = pData;
//                    ((row_picking_item) adapter).setSelection(i);
//                    if (mode == 1) {
//                        listView.smoothScrollToPositionFromTop(i, 500, 500);
//                    } else if (mode == 2) {
//                        listView.smoothScrollToPositionFromTop(i, 500, 500);
//
//
//                    }

                    return;

                }
            }
            Utility.simpleAlert(this, "باركد/کد كالا پيدا نشد. اين موضوع مي تواند به دلايل زير باشد:" + "\n" +
                    "1. كالاي مورد نظر در ليست جمع آوري موجود نيست." + "\n" +
                    "2. باركد/کد صحيح كالا را اسكن/وارد نكرده ايد." + "\n" +
                    "3. باركد/کد كالا در سيستم تعريف نشده است.", DialogIcon.Warning);
        }
    }
}