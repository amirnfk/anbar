package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.DeliverOrderActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverOrderData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.DeliverMode;
import com.oshanak.mobilemarket.Activity.Enum.DeliverOrderStatus;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Enum.PhoneDeliveryOrderType;
import com.oshanak.mobilemarket.Activity.Enum.ReturnReason;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_return_cause_spinner;
import com.oshanak.mobilemarket.Activity.Service.DeliverOrderService;
import com.oshanak.mobilemarket.Activity.Service.Enum.DeliverOrderServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;

public class DeliverOrderActivity extends BaseActivity implements OnTaskCompleted
{
    private DeliverOrderData deliverOrderData;
    private TextView tvOrderId;
    private TextView tvOrderStatusName;
    private TextView tvStore;
    private TextView tvCustomerName;
    private TextView tvAddress;
    private TextView tvPhone;
    private Spinner sReturnCause;
    private TextView tvTotalPrice;
    private EditText etComment;
    private TextView tvNo;
    private TextView tvUnit;
    private TextView tvReceiveDateTime;
    private TextView tvBonPrice;
    private TextView tvPayablePrice;
    private DeliverMode deliverMode = DeliverMode.Unknown;
    private DeliverOrderActivityMode deliverOrderActivityMode;
    private TextView tvPayment;
    private TextView tvOrderType;
//    private EditText etCodeConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_order);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderStatusName = findViewById(R.id.tvOrderStatusName);
        tvStore = findViewById(R.id.tvStore);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        sReturnCause = findViewById(R.id.sReturnCause);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        etComment = findViewById(R.id.etComment);
        tvNo = findViewById(R.id.tvNo);
        tvUnit = findViewById(R.id.tvUnit);
        tvReceiveDateTime = findViewById(R.id.tvReceiveDateTime);
        tvPayment = findViewById(R.id.tvPayment);
        tvBonPrice = findViewById(R.id.tvBonPrice);
        tvPayablePrice = findViewById(R.id.tvPayablePrice);
        tvOrderType = findViewById(R.id.tvOrderType);

        Intent intent = getIntent();
        deliverOrderData = (DeliverOrderData) intent.getSerializableExtra("deliverOrderData");
        deliverMode = (DeliverMode) intent.getSerializableExtra("deliverMode");

        initReturnCauseSpinner();

        if(deliverMode == DeliverMode.Deliver)
        {
            setTitle("تحويل");
            ((Button)findViewById(R.id.bConfirm)).setText("تحويل");
            findViewById(R.id.lReturnCause).setVisibility(View.GONE);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Cyan1)));

            findViewById(R.id.lPayment).setVisibility(View.VISIBLE);
            if(deliverOrderData.getOrderTypeCode() != PhoneDeliveryOrderType.Phone.getCode())
            {
                findViewById(R.id.bEdit).setVisibility(View.GONE);
            }

//            if(!Utility.isMobileNoValid(deliverOrderData.getMobile()))
//            {
//                findViewById(R.id.dConfirmCode).setVisibility(View.GONE);
//                findViewById(R.id.lConfirmCode).setVisibility(View.GONE);
//            }
        }
        else if(deliverMode == DeliverMode.Return)
        {
            setTitle("برگشت");
            ((Button)findViewById(R.id.bConfirm)).setText("برگشت");
            findViewById(R.id.lReturnCause).setVisibility(View.VISIBLE);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Orange1)));
            findViewById(R.id.lPayment).setVisibility(View.GONE);
//            findViewById(R.id.dConfirmCode).setVisibility(View.GONE);
//            findViewById(R.id.lConfirmCode).setVisibility(View.GONE);
        }

        tvOrderId.setText(deliverOrderData.getAxFactorNo());
        tvOrderStatusName.setText(deliverOrderData.getOrderStatusName());
        tvStore.setText(String.valueOf( deliverOrderData.getRetailStoreId()));
        tvOrderType.setText(deliverOrderData.getOrderTypeName());
        tvCustomerName.setText(deliverOrderData.getCustomerName());
        tvAddress.setText(deliverOrderData.getCustomerAddress());
        tvPhone.setText(deliverOrderData.getMobile());
        tvTotalPrice.setText(ThousandSeparatorWatcher.addSeparator( deliverOrderData.getTotalPrice()));
        tvPayablePrice.setText(ThousandSeparatorWatcher.addSeparator( deliverOrderData.getPayablePrice()));
        tvBonPrice.setText(ThousandSeparatorWatcher.addSeparator( deliverOrderData.getBonPrice()));
        tvNo.setText(deliverOrderData.getNo());
        tvUnit.setText(deliverOrderData.getUnit());
        tvReceiveDateTime.setText( deliverOrderData.getReceiveDateTime().length() > 0 ?
                deliverOrderData.getReceiveDateTime().substring(0,16) : "");

        if(this.deliverOrderData.getOrderStatusId() != DeliverOrderStatus.ReadyToSend.getCode())
        {
            Button bConfirm = findViewById(R.id.bConfirm);
            Utility.enableViews(this,false, bConfirm);
//            Utility.enableViews(this,false, bConfirm, etCodeConfirm);
        }
//        if(this.deliverOrderData.getOrderStatusId() == DeliverOrderStatus.Delivered.getCode() ||
//                this.deliverOrderData.getOrderStatusId() == DeliverOrderStatus.ReturnItem.getCode())
//        {
//            etCodeConfirm.setText( String.valueOf( this.deliverOrderData.getOrderNo()));
//        }
        if(deliverOrderData.getBonPrice() == 0)
        {
            LinearLayout lPayable = findViewById(R.id.lPayable);
            lPayable.setVisibility(View.GONE);
        }
        setPaymentAlarm();
    }
    private void setPaymentAlarm()
    {
        String sCash = ThousandSeparatorWatcher.addSeparator((int)deliverOrderData.getCashPrice());
        String sPos = (deliverOrderData.getPosPrice() == 0 && deliverOrderData.getCashPrice() == 0 ?
                ThousandSeparatorWatcher.addSeparator((int)deliverOrderData.getPayablePrice()) :
                ThousandSeparatorWatcher.addSeparator((int)deliverOrderData.getPosPrice()));

        if(deliverOrderData.getOrderTypeCode() != PhoneDeliveryOrderType.Phone.getCode())
        {
            tvPayment.setText("کل مبلغ سفارش قبلاً پرداخت شده است.");
        }
        else
        {
            if ((deliverOrderData.getCashPrice() == 0 && deliverOrderData.getPosPrice() == 0) ||
                    (deliverOrderData.getCashPrice() == 0 && deliverOrderData.getPosPrice() != 0)) {
                tvPayment.setText("كل مبلغ " + sPos + " ريال از طريق كارتخوان پرداخت مي گردد.");
            } else if (deliverOrderData.getCashPrice() != 0 && deliverOrderData.getPosPrice() == 0) {
                tvPayment.setText("كل مبلغ " + sCash + " ريال به صورت نقد پرداخت مي گردد.");
            } else {
                tvPayment.setText("مبلغ " + sPos + " ريال از طريق كارتخوان و " + sCash + " ريال به صورت نقد پرداخت مي گردد.");
            }
        }
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize(tvOrderId, 40);
            Utility.setFontBold(tvOrderId);
        }
    }
    @Override
    public void onBackPressed()
    {
        //        if( defineProductGroupActivityMode == DefineProductGroupActivityMode.BeforeInsert && insertDone)
//        {
//            Intent returnIntent = new Intent();
//            setResult(Activity.RESULT_OK, returnIntent);
//        }
//        else
        if(deliverOrderActivityMode == DeliverOrderActivityMode.AfterInsert || priceConfirmed)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", deliverOrderData);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private boolean validateData()
    {
//        if(!Utility.isMobileNoValid(deliverOrderData.getMobile())) return true;
//        if (Utility.editTextIsEmpty(etCodeConfirm, ""))
//        {
//            Utility.simpleAlert(this, "كد تحويل را از مشتري پرسيده و وارد نماييد." + "\n" +
//                    "اگر مشتري كد تحويل را نمي داند، موضوع را به مركز اطلاع دهيد.", DialogIcon.Warning);
//            return false;
//        }
//        String orderNo = etCodeConfirm.getText().toString().trim();
//        if(!orderNo.equals(String.valueOf( deliverOrderData.getCodeConfirm())))
//        {
//            Utility.simpleAlert(this, "كد تحويل صحيح نمي باشد." + "\n" +
//                    "اگر مشتري كد تحويل را نمي داند، موضوع را به مركز اطلاع دهيد.", DialogIcon.Warning);
//            return false;
//        }
        return true;
    }
    public void onConfirm(View view)
    {
        if(deliverMode == DeliverMode.Deliver)
        {
            if(!validateData()) return;

            Intent intent = new Intent(this, DeliverOrderLocationActivity.class);
            intent.putExtra("deliverOrderData", deliverOrderData);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else if(deliverMode == DeliverMode.Return)
        {
            ReturnReason returnReason = (ReturnReason)sReturnCause.getSelectedItem();
            if(returnReason == ReturnReason.Unknown)
            {
                Utility.simpleAlert(this,"علت برگشت را مشخص نماييد", DialogIcon.Warning);
                return;
            }

            deliverOrderActivityMode = DeliverOrderActivityMode.BeforeInsert;
            DeliverOrderService task = new DeliverOrderService(DeliverOrderServiceMode.InsertOrderStatus, this);
            PropertyInfo pi;

            pi = new PropertyInfo();
            pi.setName("UserName");
            pi.setValue(GlobalData.getUserName());
            task.piList.add(pi);

            pi = new PropertyInfo();
            pi.setName("OrderId");
            pi.setValue(deliverOrderData.getOrderId());
            task.piList.add(pi);

            pi = new PropertyInfo();
            pi.setName("StatusId");
            pi.setValue(DeliverOrderStatus.Return.getCode());
            task.piList.add(pi);

            pi = new PropertyInfo();
            pi.setName("ReturnReasonId");
            pi.setValue(returnReason.getCode());
            task.piList.add(pi);

            pi = new PropertyInfo();
            pi.setName("Comment");
            pi.setValue(etComment.getText().toString().trim());
            task.piList.add(pi);

            pi = new PropertyInfo();
            pi.setName("PosPrice");
            pi.setValue(0);
            task.piList.add(pi);

            pi = new PropertyInfo();
            pi.setName("CashPrice");
            pi.setValue(0);
            task.piList.add(pi);

            deliverOrderData.setOrderStatusId(DeliverOrderStatus.Return.getCode());
            deliverOrderData.setOrderStatusName(DeliverOrderStatus.Return.getDescription());
            deliverOrderData.setComment(etComment.getText().toString().trim());

            task.listener = this;
            task.execute();
            startWait();
        }
    }
    @Override
    public void onTaskCompleted(Object result)
    {
//        reset();
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this))
        {
            return;
        }
        else if(deliverOrderActivityMode == DeliverOrderActivityMode.BeforeInsert)
        {
            if (!taskResult.isSuccessful)
            {
//                if(taskResult.isExceptionOccured("IX_Competitor_CompanyCode_Name"))
//                {
//                    Utility.simpleAlert(this, "عنوان فروشگاه تکراری است"
//                            , DialogIcon.Warning);
//                    etName.requestFocus();
//                }
//                else
//                {
                    Utility.simpleAlert(this, getString( R.string.insert_do_not) + "\n" +
                            taskResult.message, DialogIcon.Error);
//                }
                return;
            }
            Toast.makeText(this, "تحويل سفارش انجام شد.",Toast.LENGTH_SHORT).show();
            deliverOrderActivityMode = DeliverOrderActivityMode.AfterInsert;
            onBackPressed();

        }
        Utility.hideKeyboard(this);
    }
    private void initReturnCauseSpinner()
    {
        if(deliverMode != DeliverMode.Return) return;

        ArrayList<ReturnReason> list = new ArrayList<>();
        list.add(ReturnReason.Unknown);
        list.add(ReturnReason.Absent);
        list.add(ReturnReason.WrongAddress);
        list.add(ReturnReason.Cancel);
        list.add(ReturnReason.WrongOrder);
        list.add(ReturnReason.Corrupt);
        list.add(ReturnReason.Other);

        row_return_cause_spinner adapter = new row_return_cause_spinner(this, list);
        sReturnCause.setAdapter(adapter);
        sReturnCause.setSelection(0);
    }
    public void onEditPayment(View view)
    {
        if(this.deliverOrderData.getOrderStatusId() != DeliverOrderStatus.ReadyToSend.getCode())
        {
            Toast.makeText(this,"با توجه به وضعيت سفارش قابل اصلاح نمي باشد." , Toast.LENGTH_SHORT).show();;
            return;
        }
        Intent intent = new Intent(this, EditDeliverOrderPriceActivity.class);
        intent.putExtra("deliverOrderData", deliverOrderData);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private boolean priceConfirmed = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if((requestCode == 1) && resultCode == Activity.RESULT_OK)
        {
            deliverOrderData = (DeliverOrderData) data.getSerializableExtra("deliverOrderData");
            setPaymentAlarm();
            priceConfirmed = true;

        }
        else if((requestCode == 2) && resultCode == Activity.RESULT_OK)
        {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);

            boolean updateCustomerLocation = data.getBooleanExtra("updateCustomerLocation", false);
            boolean dataChanged = data.getBooleanExtra("dataChanged", false);
            boolean onlyDataChanged = data.getBooleanExtra("onlyDataChanged", false);
            if(dataChanged)
            {
                deliverOrderData = (DeliverOrderData) data.getSerializableExtra("deliverOrderData");
            }
            if(onlyDataChanged) return;
            if(latitude <= 0 || longitude <= 0)
            {
                Utility.simpleAlert(this,
                        "موقعیت مکانی شما صحیح نمی باشد. از روشن بودن جی پی اس گوشی مطمئن شوید، سپس دوباره تلاش نمایید.",DialogIcon.Warning);
                return;
            }

            deliverOrderActivityMode = DeliverOrderActivityMode.BeforeInsert;
            DeliverOrderService task = new DeliverOrderService(DeliverOrderServiceMode.InsertOrderStatus, this);

            task.addParam("UserName", GlobalData.getUserName());
            task.addParam("OrderId", deliverOrderData.getOrderId());
            task.addParam("StatusId", DeliverOrderStatus.Delivered.getCode());
            task.addParam("ReturnReasonId", ReturnReason.Unknown.getCode());

            task.addParam("Comment", etComment.getText().toString().trim());
            task.addParam("PosPrice", (int)(deliverOrderData.getPosPrice() == 0 && deliverOrderData.getCashPrice() == 0 ?
                    deliverOrderData.getTotalPrice() : deliverOrderData.getPosPrice()));
            task.addParam("CashPrice", (int)deliverOrderData.getCashPrice());
            task.addParam("ConfirmCode", "0");
            task.addParam("deliverLatitude", latitude);
            task.addParam("deliverLongitude", longitude);
            task.addParam("updateCustomerLocation", updateCustomerLocation);
            task.addParam("OrderType", deliverOrderData.getOrderTypeCode());

            deliverOrderData.setOrderStatusId(DeliverOrderStatus.Delivered.getCode());
            deliverOrderData.setOrderStatusName(DeliverOrderStatus.Delivered.getDescription());
            deliverOrderData.setComment(etComment.getText().toString().trim());

            task.listener = this;
            task.execute();
            startWait();
        }
    }
    public void onCodeHelp(View view)
    {
        String comment = "هنگام تحويل سفارش به مشتري كد تاييدي كه براي او پيامك شده را از مشتري پرسيده و در اين قسمت وارد نماييد." + "\n" +
                "در صورت عدم ثبت كد صحيح در اين قسمت، مجاز به تحويل سفارش به مشتري نمي باشيد.";

        Utility.fastHelp(this, "كد تاييد", comment);
    }
}
