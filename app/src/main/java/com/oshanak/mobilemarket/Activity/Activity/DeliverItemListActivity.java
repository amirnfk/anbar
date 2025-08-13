package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.DeliverItemListActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverItemData;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverOrderData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_deliver_item;
import com.oshanak.mobilemarket.Activity.Service.DeliverOrderService;
import com.oshanak.mobilemarket.Activity.Service.Enum.DeliverOrderServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javadz.beanutils.BeanUtils;

public class DeliverItemListActivity extends BaseActivity implements
        row_deliver_item.OnDeliverItemListCommandListener, OnTaskCompleted
{
    private TextView tvCount;
    private ListView listView;
    TextView tvOrderId;
    TextView tvCustomerName;
    private TextView tvTotalCount;
    private TextView tvTotalPrice;
    private DeliverOrderData deliverOrderData;
    private DeliverItemData deliverItemData;
    private DeliverItemListActivityMode deliverItemListActivityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_item_list);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvTotalCount = findViewById(R.id.tvTotalCount11);
        tvTotalPrice = findViewById(R.id.tvTotalPrice11);

        Intent intent = getIntent();
        deliverOrderData = (DeliverOrderData) intent.getSerializableExtra("deliverOrderData");

        tvOrderId.setText(deliverOrderData.getAxFactorNo());
        tvCustomerName.setText(deliverOrderData.getCustomerName());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                competitorProductData = (CompetitorProductData) parent.getItemAtPosition(position);
                ((row_deliver_item)parent.getAdapter()).setSelection(position);
            }
        });

        getList();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize(tvTotalPrice, 10);
            Utility.increaseTextSize(tvTotalCount, 10);
            Utility.setFontBold(tvTotalPrice);
            Utility.setFontBold(tvTotalCount);
        }
    }
    private void getList()
    {
//        reset();
//        if(!Utility.compareTwoDates(this, frDateFrom, frDateTo))
//        {
//            return;
//        }

        deliverItemListActivityMode = deliverItemListActivityMode.BeforeGetList;
        DeliverOrderService task = new DeliverOrderService(DeliverOrderServiceMode.GetDeliverItem, this);
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("OrderId");
        pi.setValue(deliverOrderData.getOrderId());
        task.piList.add(pi);

        task.listener = this;
        task.execute();
        startWait();
    }
    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this))
        {
            return;
        }
        else if(deliverItemListActivityMode == DeliverItemListActivityMode.BeforeGetList)
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
            ArrayList<DeliverItemData> list = (ArrayList<DeliverItemData>) taskResult.dataStructure;

            row_deliver_item adapter = new row_deliver_item(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            setFooter();
            deliverItemListActivityMode = DeliverItemListActivityMode.AfterGetList;
            if(adapter.getCount() >= 200)
            {
                Toast.makeText(this,
                        getString( R.string.only_200_rows)
                        ,Toast.LENGTH_LONG).show();
            }
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
    public void onBackPressed()
    {
        //        if( defineProductGroupActivityMode == DefineProductGroupActivityMode.BeforeInsert && insertDone)
//        {
//            Intent returnIntent = new Intent();
//            setResult(Activity.RESULT_OK, returnIntent);
//        }
//        else
//        if(deliverOrderActivityMode == DeliverOrderActivityMode.AfterInsert)
//        {
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra("result", deliverOrderData);
//            setResult(Activity.RESULT_OK, returnIntent);
//        }
        deliverOrderData.setTotalPrice(totalPrice);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", deliverOrderData);
        setResult(Activity.RESULT_OK, returnIntent);
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private long totalCount = 0;
    private double totalPrice = 0;
    private void setFooter()
    {
        ArrayAdapter<DeliverItemData> adapter = (ArrayAdapter<DeliverItemData>)listView.getAdapter();

        totalCount = 0;
        totalPrice = 0;

        for (int i = 0; i < adapter.getCount(); i++)
        {
            DeliverItemData data = adapter.getItem(i);
            totalCount += data.getDeliverQuantity();
            totalPrice += data.getDeliverQuantity() * data.getUnitPrice();

        }
        tvTotalCount.setText("تعداد: " + ThousandSeparatorWatcher.addSeparator(totalCount));
        tvTotalPrice.setText("مبلغ: " + ThousandSeparatorWatcher.addSeparator(totalPrice));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            double deliverQuantity = data.getDoubleExtra("deliverQuantity", 0);
            deliverItemData.setDeliverQuantity(deliverQuantity);
            deliverItemData.setComment(data.getStringExtra("comment"));

            ArrayAdapter<DeliverItemData> adapter = (ArrayAdapter<DeliverItemData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                DeliverItemData pData = adapter.getItem(i);
                if (pData.getItemId() == deliverItemData.getItemId())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, deliverItemData);
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
            setFooter();
        }
    }
    @Override
    public void OnDeliverItemListCommand(DeliverItemData deliverItemData, int position
            , row_deliver_item.DeliverItemListCommandType commandType)
    {
        this.deliverItemData = deliverItemData;
        ((row_deliver_item)listView.getAdapter()).setSelection(position);

        if(commandType == row_deliver_item.DeliverItemListCommandType.Edit)
        {
            Intent intent = new Intent(this, EditDeliverItemActivity.class);
////            intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeInsert);
//            intent.putExtra("deliverMode", DeliverMode.Deliver);
            intent.putExtra("deliverItemData", deliverItemData);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
