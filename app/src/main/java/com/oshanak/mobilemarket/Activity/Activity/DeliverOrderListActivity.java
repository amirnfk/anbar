package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.DeliverOrderListActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.DateFragment;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DeliverOrderData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.DeliverMode;
import com.oshanak.mobilemarket.Activity.Enum.DeliverOrderStatus;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_deliver_order;
import com.oshanak.mobilemarket.Activity.Service.DeliverOrderService;
import com.oshanak.mobilemarket.Activity.Service.Enum.DeliverOrderServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import javadz.beanutils.BeanUtils;

public class DeliverOrderListActivity extends BaseActivity
        implements DateFragment.OnDateChangedListener, OnTaskCompleted, row_deliver_order.OnDeliverOrderListCommandListener
{
    private DateFragment frDateFrom;
    private DateFragment frDateTo;
    private TextView tvCount;
    private ListView listView;
    private DeliverOrderData deliverOrderData;
    private DeliverOrderListActivityMode deliverOrderListActivityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_order_list);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        frDateFrom = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateFrom);
        frDateTo = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateTo);
        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);

        frDateFrom.setTitle("از تاریخ");
        frDateTo.setTitle("تا تاریخ");

        PersianCalendar pc = new PersianCalendar();
        pc.addPersianDate(PersianCalendar.DATE, -7);
        frDateFrom.setDate(pc);
        frDateTo.setDateToCurrent();

        Intent intent = getIntent();
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

//        GlobalData.setUserName("basgarpoor");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                deliverOrderData = (DeliverOrderData) parent.getItemAtPosition(position);
                ((row_deliver_order)parent.getAdapter()).setSelection(position);
            }
        });

        getList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.deliver_order_list_menu, menu);
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
        super.onBackPressed();
        Utility.hideKeyboard(this);
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private void reset()
    {
        tvCount.setText("");
        listView.setAdapter(null);
//        competitorProductData = null;
    }
    private void getList()
    {
        reset();
        if(!Utility.compareTwoDates(this, frDateFrom, frDateTo))
        {
            return;
        }

        deliverOrderListActivityMode = DeliverOrderListActivityMode.BeforeGetList;
        DeliverOrderService task = new DeliverOrderService(DeliverOrderServiceMode.GetDeliverOrder, this);
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("UserName");
        pi.setValue(GlobalData.getUserName());
        task.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("fromDate");
        pi.setValue(frDateFrom.getShortDate());
        task.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("toDate");
        pi.setValue(frDateTo.getShortDate());
        task.piList.add(pi);

        task.listener = this;
        task.execute();
        startWait();
    }

    @Override
    public void OnDateChanged(String PersianLongDate, String PersianShortDate)
    {
        getList();
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
        else if(deliverOrderListActivityMode == DeliverOrderListActivityMode.BeforeGetList)
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
            ArrayList<DeliverOrderData> list = (ArrayList<DeliverOrderData>) taskResult.dataStructure;

            row_deliver_order adapter = new row_deliver_order(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            deliverOrderListActivityMode = DeliverOrderListActivityMode.AfterGetList;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if((requestCode == 1 || requestCode == 2 || requestCode == 3) && resultCode == Activity.RESULT_OK)
        {
            deliverOrderData = (DeliverOrderData) data.getSerializableExtra("result");

            ArrayAdapter<DeliverOrderData> adapter = (ArrayAdapter<DeliverOrderData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                DeliverOrderData pData = adapter.getItem(i);
                if (pData.getOrderId().equals( deliverOrderData.getOrderId()))
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, deliverOrderData);
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
    }

    @Override
    public void OnDeliverOrderListCommand(DeliverOrderData deliverOrderData
            , int position, row_deliver_order.DeliverOrderListCommandType commandType)
    {
        this.deliverOrderData = deliverOrderData;
        ((row_deliver_order)listView.getAdapter()).setSelection(position);

        if(commandType == row_deliver_order.DeliverOrderListCommandType.Deliver)
        {
//            if(this.deliverOrderData.getOrderStatusId() != DeliverOrderStatus.ReadyToSend.getCode())
//            {
//                Toast.makeText(this, "فقط سفارشهايي با وضعيت آماده ارسال قابل تحويل مي باشند.", Toast.LENGTH_LONG).show();
//                return;
//            }

            Intent intent = new Intent(this, DeliverOrderActivity.class);
//            intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeInsert);
            intent.putExtra("deliverMode", DeliverMode.Deliver);
            intent.putExtra("deliverOrderData", deliverOrderData);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else if(commandType == row_deliver_order.DeliverOrderListCommandType.Return)
        {
            if(this.deliverOrderData.getOrderStatusId() != DeliverOrderStatus.ReadyToSend.getCode())
            {
                Toast.makeText(this, "فقط سفارشهايي با وضعيت آماده ارسال قابل برگشت مي باشند.", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(this, DeliverOrderActivity.class);
//            intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeInsert);
            intent.putExtra("deliverMode", DeliverMode.Return);
            intent.putExtra("deliverOrderData", deliverOrderData);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else if(commandType == row_deliver_order.DeliverOrderListCommandType.Items)
        {
            Intent intent = new Intent(this, DeliverItemListActivity.class);
//            intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeInsert);
            intent.putExtra("deliverOrderData", deliverOrderData);
            startActivityForResult(intent, 3);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
