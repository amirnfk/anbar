package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.EditPickingItemActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utilities;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.MetaData;
import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverItemData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Enum.PickingItemStatus;
import com.oshanak.mobilemarket.Activity.Enum.ProductUnit;

import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingRequest;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingResponse;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.PickingRetrofitClient;
import com.oshanak.mobilemarket.Activity.PickingApp.PickingApiService;
import com.oshanak.mobilemarket.Activity.PickingApp.PickingControlHeaderActivity;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_product_unit_spinner;
import com.oshanak.mobilemarket.Activity.Service.Enum.PickingDeliverServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.PickingDeliverService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPickingItemActivity extends BaseActivity implements OnTaskCompleted {

    private EditText etQuantity;
    private TextView tvName;
    private Spinner sUnit;
    private PickingDeliverItemData pickingDeliverItemData;
    private int minimumInventory;
    private String orderType;
    private EditPickingItemActivityMode editPickingItemActivityMode;
    TextView tvDeliverBox;
    TextView tvDeliverPc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picking_item);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        etQuantity = findViewById(R.id.etQuantity);
        tvName = findViewById(R.id.tvName);
        sUnit = findViewById(R.id.sUnit);


        etQuantity.addTextChangedListener(new ThousandSeparatorWatcher(etQuantity));

        Intent intent = getIntent();
        pickingDeliverItemData = (PickingDeliverItemData) intent.getSerializableExtra("pickingDeliverItemData");

        minimumInventory = intent.getIntExtra("minimumInventory", 0);
        orderType = intent.getStringExtra("orderType");

        if (pickingDeliverItemData.isIs_PC_Unit()) {
//            sUnit. setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                }
//            });

            sUnit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    sUnit.setEnabled(false);
                    Toast.makeText(getBaseContext(), "این کالا صرفا به صورت واحد جزء (pc) مجاز به جمع آوری می باشد", Toast.LENGTH_LONG).show();

                    return false;
                }
            });
        } else {
            sUnit.setEnabled(true);
        }

//        if ( orderType.equalsIgnoreCase("S")) {
//            sUnit.setEnabled(false);
//        }
        etQuantity.setText(ThousandSeparatorWatcher.addSeparator(pickingDeliverItemData.getDeliverAmount()));
        tvName.setText(pickingDeliverItemData.getARKTX());
        initProductUnitSpinner(sUnit);
        if (pickingDeliverItemData.getDeliverUnit().equals(ProductUnit.ST.getName()) ||
                pickingDeliverItemData.getDeliverUnit().equals(ProductUnit.G.getName())) {
            sUnit.setSelection(1);
        } else {
            sUnit.setSelection(2);
        }

        TextView tvBoxUnit = findViewById(R.id.tvBoxUnit);
        tvDeliverBox = findViewById(R.id.tvDeliverBox);
        tvDeliverPc = findViewById(R.id.tvDeliverPc);
        tvBoxUnit.setText("تعداد در كارتن: " + pickingDeliverItemData.getUMVKZ());
        setDeliverUnit();
//        initProductUnitSpinner(sUnit);
        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setDeliverUnit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDeliverUnit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDeliverUnit() {
        float deliverBox = 0;
        int deliverPc = 0;
        int deliverAmount = 0;
        try {
            deliverAmount = Integer.parseInt(ThousandSeparatorWatcher.removeSeparator(etQuantity.getText().toString()));
        } catch (NumberFormatException e) {
            return;
            //Do nothing...
        }

        if ((sUnit.getSelectedItem()) == ProductUnit.KAR || (sUnit.getSelectedItem()) == ProductUnit.KG) {
            deliverBox = deliverAmount;
            deliverPc = deliverAmount * pickingDeliverItemData.getUMVKZ();
        } else if ((sUnit.getSelectedItem()) == ProductUnit.ST || (sUnit.getSelectedItem()) == ProductUnit.G) {
            deliverBox = (float) deliverAmount / pickingDeliverItemData.getUMVKZ();
            deliverPc = deliverAmount;
        }
        tvDeliverBox.setText("تحويل به واحد كل: " + String.format("%.2f", deliverBox));
        tvDeliverPc.setText("تحويل به واحد جزء: " + deliverPc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isStarted) {
            isStarted = true;
            Utility.setFontBold(etQuantity);
            Utility.increaseTextSize(etQuantity, 70);
        }
    }

    public void onClickExit(View view) {
        onBackPressed();
    }

    public void onClickConfirm(View view) {
        Update ();
    }

    private void Update() {


        if (!validateData()) return;
        double d = Double.parseDouble(ThousandSeparatorWatcher.removeSeparator(etQuantity.getText().toString().trim()));
        int deliverAmount = (int) d;

//        PickingDeliverService service;
//
//        editPickingItemActivityMode = EditPickingItemActivityMode.BeforeUpdate.BeforeUpdate;
//        service = new PickingDeliverService(PickingDeliverServiceMode.UpdatePickingDeliverItemAmount, this);
//        PropertyInfo pi;
//
//        pi = new PropertyInfo();
//        pi.setName("ItemID");
//        pi.setValue(pickingDeliverItemData.getID());
//        service.piList.add(pi);
//
//        pi = new PropertyInfo();
//        pi.setName("deliverAmount");
//        pi.setValue(deliverAmount);
//        service.piList.add(pi);
//
//        pi = new PropertyInfo();
//        pi.setName("deliverUnit");
//        pi.setValue(((ProductUnit) sUnit.getSelectedItem()).getName());
//        service.piList.add(pi);
//
//        service.listener = this;
//
//        Log.d("asdasdasdasd",deliverAmount+"..."+pickingDeliverItemData.getID());
//        service.execute();
//        startWait();

        MetaData metaData = new MetaData();
        metaData.setUserName(GlobalData.getUserName());
        metaData.setAppVersionCode(Utilities.getApkVersionCode(EditPickingItemActivity.this));
        metaData.setPassword(GlobalData.getPassword());
        metaData.setAppMode(Utility.applicationMode.toString());
        metaData.setDeviceInfo(Utilities.getDeviceInfo());

        UpdatePickingRequest request = new UpdatePickingRequest(
                pickingDeliverItemData.getID()+"", deliverAmount+"", ((ProductUnit) sUnit.getSelectedItem()).getName(), metaData
        );
        PickingApiService apiService = PickingRetrofitClient.getRetrofitInstance().create(PickingApiService.class);
        Call<UpdatePickingResponse> call = apiService.UpdatePickingDeliverItemAmount(request);

        call.enqueue(new Callback<UpdatePickingResponse>() {
            @Override
            public void onResponse(Call<UpdatePickingResponse> call, Response<UpdatePickingResponse> response) {

                Log.d("asdasdasxxx",response.body().toString());
                if (response.isSuccessful() && response.body() != null) {
                    TaskResult taskResult = new TaskResult(response.body().isSuccessful(),response.body().getMessage(),null,"");
                    Log.d("asdasdasxxx1",response.body().toString());
                     if (editPickingItemActivityMode == EditPickingItemActivityMode.BeforeUpdate) {
                         Log.d("asdasdasxxx2",response.body().toString());
                        if (!taskResult.isSuccessful) {
                            Log.d("asdasdasxxx3",response.body().toString());
                            Log.d("asdasdas",taskResult.message.toString());
                            if (Utility.generalErrorOccurred(taskResult, EditPickingItemActivity.this)) {
                                Log.d("asdasdasxxx4",response.body().toString());
                                return;
                            } else if (taskResult.isExceptionOccured("order status not valid to change.")) {
                                Log.d("asdasdasxxx5",response.body().toString());
                                Utility.simpleAlert(EditPickingItemActivity.this, "با توجه به وضعيت سفارش امكان اصلاح اقلام آن وجود ندارد.", ""
                                        , DialogIcon.Warning);
                                return;
                            } else {
                                Log.d("asdasdasxxx6",response.body().toString());
                                Utility.simpleAlert(EditPickingItemActivity.this, getString(R.string.update_do_not), DialogIcon.Error);
                                return;
                            }
                        }
                         Log.d("asdasdasxxx7",response.body().toString());


                    }
                    Toast.makeText(EditPickingItemActivity.this, getString(R.string.update_done), Toast.LENGTH_SHORT).show();
                    editPickingItemActivityMode = EditPickingItemActivityMode.AfterUpdate;
                    onBackPressed();
                    Utility.hideKeyboard(EditPickingItemActivity.this);
                } else {
                    Log.d("asdasdasxxx8",response.body().toString());
                    Utility.simpleAlert(EditPickingItemActivity.this, getString(R.string.update_do_not), DialogIcon.Error);

                }
            }

            @Override
            public void onFailure(Call<UpdatePickingResponse> call, Throwable t) {
                Utility.showFailureToast(EditPickingItemActivity.this,"خطای ارتباط با سرور");

            }
        });







    }

    @Override
    public void onTaskCompleted(Object result) {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this)) {
            return;
        } else if (editPickingItemActivityMode == EditPickingItemActivityMode.BeforeUpdate) {
            if (!taskResult.isSuccessful) {
                Log.d("asdasdas",taskResult.message.toString());
                if (Utility.generalErrorOccurred(taskResult, this)) {
                    return;
                } else if (taskResult.isExceptionOccured("order status not valid to change.")) {
                    Utility.simpleAlert(this, "با توجه به وضعيت سفارش امكان اصلاح اقلام آن وجود ندارد.", ""
                            , DialogIcon.Warning);
                    return;
                } else {
                    Utility.simpleAlert(this, getString(R.string.update_do_not), DialogIcon.Error);
                    return;
                }
            }
            Log.d("asdasdas1",taskResult.toString());

            Toast.makeText(this, getString(R.string.update_done), Toast.LENGTH_SHORT).show();
            editPickingItemActivityMode = EditPickingItemActivityMode.AfterUpdate;
            onBackPressed();
        }
        Utility.hideKeyboard(this);
    }

    private int getInventoryPcUnit() {
        return pickingDeliverItemData.getLBKUM(); //* pickingDeliverItemData.getUMVKZ()//;
    }

    private double getOrderPcUnit() {
        return pickingDeliverItemData.getLFIMG() * pickingDeliverItemData.getUMVKZ();
    }

    private int getDeliverPcUnit() {
        double d = Double.parseDouble(ThousandSeparatorWatcher.removeSeparator(etQuantity.getText().toString().trim()));

        int count = (int) d;
        int unit = 0;
        if (((ProductUnit) sUnit.getSelectedItem()).getName().equals(ProductUnit.ST.getName()) ||
                ((ProductUnit) sUnit.getSelectedItem()).getName().equals(ProductUnit.G.getName())) {
            unit = 1;
        } else {
            unit = pickingDeliverItemData.getUMVKZ();
        }
        return count * unit;
    }

    private boolean validateData() {


        int inventoryPcUnit = getInventoryPcUnit();
        double orderPcUnit = getOrderPcUnit();
        int deliverPcUnit = getDeliverPcUnit();

        if ((Integer.parseInt(etQuantity.getText().toString().trim())) < minimumInventory) {
            showMinimumQuantityWarning("تعداد تحويل نمي تواند کمتر از مینیموم " + minimumInventory + " باشد .");

            etQuantity.setText(minimumInventory + "");
            return false;
        }
        if (sUnit.getSelectedItem() == ProductUnit.Splash || sUnit.getSelectedItem() == ProductUnit.Unknown) {
            Toast.makeText(this, "واحد كالا را انتخاب نماييد.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (deliverPcUnit > orderPcUnit) {
            Toast.makeText(this, "تعداد تحويل نمي تواند بيشتر از تعداد سفارش باشد.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (minimumInventory > 0) {
            if ((deliverPcUnit - minimumInventory) >= inventoryPcUnit) {
                Toast.makeText(this, "تعداد تحويل نمي تواند بيشتر از موجودي باشد.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if ((deliverPcUnit) >= inventoryPcUnit) {
                Toast.makeText(this, "تعداد تحويل نمي تواند بيشتر از موجودي باشد.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (etQuantity.getText().toString().trim().equals("")) {
            Toast.makeText(this, "مقداري معتبر وارد نماييد.", Toast.LENGTH_SHORT).show();
            return false;
        }

        double d = (etQuantity.getText().toString().trim().equals("") ? 1 :
                Double.parseDouble(ThousandSeparatorWatcher.removeSeparator(etQuantity.getText().toString().trim())));
        if (d < 1) {
            Toast.makeText(this, "تعداد تحويل نمي تواند صفر باشد.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onClickChangeCount(View view) {


        double d = 0;
        try {

            //For Exception Handling only..
//            int temp = Integer.parseInt(ThousandSeparatorWatcher.removeSeparator(etQuantity.getText().toString()));
            d = (etQuantity.getText().toString().trim().equals("") ? 1 :
                    Double.parseDouble(ThousandSeparatorWatcher.removeSeparator(etQuantity.getText().toString().trim())));


            if ((Integer.parseInt(etQuantity.getText().toString().trim()) < minimumInventory)) {
                showMinimumQuantityWarning("تعداد تحويل نمي تواند کمتر از مینیموم " + minimumInventory + " باشد .");
                etQuantity.setText(minimumInventory + "");
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "مقدار وارد شده معتبر نيست!", Toast.LENGTH_SHORT).show();
            return;
        }

        int count = (int) d;
        if (view.getId() == findViewById(R.id.ibMinus).getId()) {
            if (count <= 1) return;
            count--;
        } else {
            if (count >= 999999) return;
            int inventoryPcUnit = getInventoryPcUnit();
            double orderPcUnit = getOrderPcUnit();
            int deliverPcUnit = getDeliverPcUnit();


            if (deliverPcUnit >= orderPcUnit) {
                Toast.makeText(this, "تعداد تحويل نمي تواند بيشتر از تعداد سفارش باشد.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (minimumInventory > 0) {
                if ((deliverPcUnit - minimumInventory) >= inventoryPcUnit) {
                    Toast.makeText(this, "تعداد تحويل نمي تواند بيشتر از موجودي باشد.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                if ((deliverPcUnit) >= inventoryPcUnit) {
                    Toast.makeText(this, "تعداد تحويل نمي تواند بيشتر از موجودي باشد.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            count++;
        }
        etQuantity.setText(String.valueOf(count));
        etQuantity.clearFocus();
        Utility.hideKeyboard(this);
    }

    private void showMinimumQuantityWarning(String message) {

        View view = getLayoutInflater().inflate(R.layout.ctoast_view_error, null);
        TextView toastTextView = (TextView) view.findViewById(R.id.message);
        toastTextView.setText(message);

        Toast mToast = new Toast(getApplicationContext());
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();

    }

    @Override
    public void onBackPressed() {
        if (editPickingItemActivityMode == EditPickingItemActivityMode.AfterUpdate) {
            double d = Double.parseDouble(ThousandSeparatorWatcher.removeSeparator(etQuantity.getText().toString().trim()));
            int deliverAmount = (int) d;

            Intent returnIntent = new Intent();
            pickingDeliverItemData.setDeliverAmount(deliverAmount);
            pickingDeliverItemData.setStatusID(PickingItemStatus.Unknown.getCode());
            pickingDeliverItemData.setStatusName(PickingItemStatus.Unknown.getName());
            pickingDeliverItemData.setDeliverUnit(((ProductUnit) sUnit.getSelectedItem()).getName());
            returnIntent.putExtra("pickingDeliverItemData", pickingDeliverItemData);
            returnIntent.putExtra("minimumInventory", minimumInventory);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initProductUnitSpinner(Spinner spinner) {
        ArrayList<ProductUnit> list = new ArrayList<>();
        list.add(ProductUnit.Splash);
        if (pickingDeliverItemData.equals(null)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "خطا در دریافت اطلاعات",
                    Toast.LENGTH_SHORT);

            toast.show();
        }
        String WholeUnit = pickingDeliverItemData.getWholeUnit();
        String PartUnit = pickingDeliverItemData.getPartUnit();

        if ((WholeUnit.equals("ST") || PartUnit.equals("ST")) && !list.contains(ProductUnit.ST))
            list.add(ProductUnit.ST);
        if ((WholeUnit.equals("KAR") || PartUnit.equals("KAR")) && !list.contains(ProductUnit.KAR))
            list.add(ProductUnit.KAR);
        if ((WholeUnit.equals("KG") || PartUnit.equals("KG")) && !list.contains(ProductUnit.KG))
            list.add(ProductUnit.KG);
        if ((WholeUnit.equals("G") || PartUnit.equals("G")) && !list.contains(ProductUnit.G))
            list.add(ProductUnit.G);

        row_product_unit_spinner adapter = new row_product_unit_spinner(this, list);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }
}