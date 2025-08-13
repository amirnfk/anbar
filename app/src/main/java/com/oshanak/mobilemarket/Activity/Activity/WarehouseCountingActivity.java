package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.oshanak.mobilemarket.Activity.Activity.Enum.WarehouseCountingActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ProductCodeFragment;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseCountingDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseCountingProductControlData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseCountingWrapper;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_general_spinner;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_warehouse_counting;
import com.oshanak.mobilemarket.Activity.Service.Enum.PickingDeliverServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.PickingDeliverService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javadz.beanutils.BeanUtils;

public class WarehouseCountingActivity extends BaseActivity implements
        OnTaskCompleted
        ,ProductCodeFragment.OnConfirmListener
        ,ProductCodeFragment.OnBarcodeScannedByCameraListener
        ,ProductCodeFragment.OnBarcodeChanged
        ,row_warehouse_counting.OnWarehouseCountingCommandListener
{
    private TextView tvProductCode;
    private TextView tvProductName;
//    private TextView tvLast14DaysSale;
//    private Spinner sUnit;
    private EditText etAmount;
    private ListView listView;
    private TextView tvCount;
    private TextView tvTotalCount;
    private ProductCodeFragment productCodeFragment;
    private WarehouseCountingActivityMode warehouseCountingActivityMode;
//    private StoreReturnItemData storeReturnItemData;
    private WarehouseCountingProductControlData warehouseCountingProductControlData;
    private WarehouseCountingWrapper warehouseCountingWrapper = null;
    private WarehouseCountingDetailData warehouseCountingDetailData = null;
    private Spinner sLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_counting);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvProductCode = findViewById(R.id.tvProductCode);
        tvProductName = findViewById(R.id.tvProductName);
        etAmount = findViewById(R.id.etAmount);
        listView = findViewById(R.id.recyclerview);
        tvCount = findViewById(R.id.tvCount);
        productCodeFragment = (ProductCodeFragment)getSupportFragmentManager().findFragmentById(R.id.frProductCode);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        sLine = findViewById(R.id.sLine);
        initLineSpinner(this, sLine);
        etAmount.addTextChangedListener(new ThousandSeparatorWatcher(etAmount));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                warehouseCountingDetailData = (WarehouseCountingDetailData) parent.getItemAtPosition(position);
                ((row_warehouse_counting)parent.getAdapter()).setSelection(position);
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

        getList();
    }
    private void setLine()
    {
        if(warehouseCountingWrapper == null) return;
        int lineNo = warehouseCountingWrapper.getWarehouseCountingHeaderData().getLineNumber();
        for(int i = 0; i < sLine.getCount(); i++)
        {
            if( String.valueOf( lineNo).equalsIgnoreCase(sLine.getItemAtPosition(i).toString()))
            {
                sLine.setSelection(i);
                Utility.enableViews(this, false, sLine);
                break;
            }
        }
    }
    private void initLineSpinner(Context context, Spinner sLine)
    {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        list.add("16");
        list.add("17");
        list.add("18");
        list.add("19");
        list.add("20");

        row_general_spinner adapter = new row_general_spinner(context, list);
        sLine.setAdapter(adapter);
        sLine.setSelection(0);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize(tvTotalCount,20);
            Utility.setFontBold(tvTotalCount);
            productCodeFragment.setFont();
            productCodeFragment.setAutoScanVisible(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.warehouse_counting_menu, menu);
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
        if(listView.getAdapter() ==  null ||
                listView.getAdapter().getCount() == 0)
        {
            Utility.simpleAlert(this, "هيچ كالايي جهت ارسال ثبت نشده است.", DialogIcon.Warning);
            return;
        }
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آيا اطلاعات به مركز ارسال گردد؟");
        dlgAlert.setTitle("ارسال اطلاعات");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("بله",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        warehouseCountingActivityMode = WarehouseCountingActivityMode.BeforeSendDataToSAP;
                        PickingDeliverService service = new PickingDeliverService(PickingDeliverServiceMode.SendCountingToSAP
                                ,WarehouseCountingActivity.this);

                        service.addParam("UserName", GlobalData.getUserName());

                        service.listener = WarehouseCountingActivity.this;
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
    private void setFooter()
    {
        ArrayAdapter<WarehouseCountingDetailData> adapter = (ArrayAdapter<WarehouseCountingDetailData>)listView.getAdapter();
        int totalCount = 0;

        for (int i = 0; i < adapter.getCount(); i++)
        {
            WarehouseCountingDetailData data = adapter.getItem(i);
            totalCount += data.getAmount();
        }
        tvTotalCount.setText(ThousandSeparatorWatcher.addSeparator(totalCount));
    }
    private void getList()
    {
        warehouseCountingActivityMode = WarehouseCountingActivityMode.BeforeGetDetailList;
        PickingDeliverService task = new PickingDeliverService(PickingDeliverServiceMode.GetCounting, this);

        task.addParam("UserName", GlobalData.getUserName());

        task.listener = this;
        task.execute();
        startWait();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Utility.hideKeyboard(this);
    }


    private void getProductInfo(String barcode)
    {
        reset(false);

        warehouseCountingActivityMode = WarehouseCountingActivityMode.BeforeGetProductInfo;
        PickingDeliverService task = new PickingDeliverService(PickingDeliverServiceMode.ProductControl, this);
        task.addParam("code", productCodeFragment.getProductCode());
        task.listener = this;
        task.execute();

        startWait();
    }
    private void reset(boolean resetProductFragment)
    {
        tvProductCode.setText("");
        tvProductName.setText("");
        etAmount.setText("");
//        sReturnReason.setSelection(0);
        if(resetProductFragment) {
            productCodeFragment.setProductCode("");
        }
        warehouseCountingProductControlData = null;
        warehouseCountingDetailData = null;
    }

    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if(warehouseCountingActivityMode == WarehouseCountingActivityMode.BeforeGetProductInfo )
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
//            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("product does not exist"))
//            {
//                Utility.simpleAlert(this,"كالايي با اين مشخصات يافت نشد!", DialogIcon.Error);
//                return;
//            }
            else if (!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this,taskResult.message, DialogIcon.Error);
                return;
            }

            warehouseCountingProductControlData = (WarehouseCountingProductControlData)taskResult.dataStructure;
            tvProductCode.setText( String.valueOf( Integer.parseInt( warehouseCountingProductControlData.getProductCode())));
            tvProductName.setText( warehouseCountingProductControlData.getProductName());

            Utility.hideKeyboard(this);
            Utility.playBeep();
            etAmount.requestFocus();
            Utility.showKeyboard(this);
            warehouseCountingActivityMode = WarehouseCountingActivityMode.AfterGetProductInfo;
        }
        else if(warehouseCountingActivityMode == WarehouseCountingActivityMode.BeforeInsertDetail)
        {
            if (!taskResult.isSuccessful)
            {
                if(taskResult.isExceptionOccured("IX_WarehouseCountingDetail_HeaderID_ItemID"))
                {
                    duplicateDetected();
                }
                else
                {
                    Utility.simpleAlert(this, getString( R.string.insert_do_not) + "\n" +
                            taskResult.message, DialogIcon.Error);
                }
                return;
            }
            Toast.makeText(this, "ثبت اطلاعات انجام شد.",Toast.LENGTH_SHORT).show();
            if(warehouseCountingWrapper == null) {
                Utility.enableViews(this, false, sLine);
            }
            insertToList(  taskResult.tag);
            Utility.hideKeyboard(this);
            Utility.playShortBeep();
            reset(true);
            setFooter();
            if(productCodeFragment.isAutoScanEnable()) productCodeFragment.startScan();
            warehouseCountingActivityMode = WarehouseCountingActivityMode.AfterInsertDetail;
        }
        else if(warehouseCountingActivityMode == WarehouseCountingActivityMode.BeforeGetDetailList)
        {
            reset(true);
            if(taskResult == null) return;

            if(!taskResult.isSuccessful && !taskResult.message.equals( "No rows found!"))
            {
                Utility.simpleAlert(this, getString( R.string.error_in_fetching_data) + "\n" +
                        taskResult.message, DialogIcon.Error);
                return;
            }
            else if(!taskResult.isSuccessful && taskResult.message.equals( "No rows found!"))
            {
                return;
            }
            warehouseCountingWrapper = (WarehouseCountingWrapper) taskResult.dataStructure;

            row_warehouse_counting adapter = new row_warehouse_counting(this, warehouseCountingWrapper.getWarehouseCountingDetailDataList());
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            setFooter();
            setLine();
            warehouseCountingActivityMode = WarehouseCountingActivityMode.AfterGetDetailList;
        }
        else if(warehouseCountingActivityMode == WarehouseCountingActivityMode.BeforeDelete)
        {
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
                return;
            }

            ArrayAdapter<WarehouseCountingDetailData> adapter = (ArrayAdapter<WarehouseCountingDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                WarehouseCountingDetailData pData = adapter.getItem(i);
                if (pData.getID() == warehouseCountingDetailData.getID())
                {
                    adapter.remove(pData);
                    warehouseCountingDetailData = null;
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
            setFooter();
            warehouseCountingActivityMode = WarehouseCountingActivityMode.AfterDelete;
        }
        else if(warehouseCountingActivityMode == WarehouseCountingActivityMode.BeforeUpdate)
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            if (!taskResult.isSuccessful) {
                if (!Utility.generalErrorOccurred(taskResult, this)) {
                    Utility.simpleAlert(this, getString(R.string.update_do_not) + "\n" +
                            taskResult.message, DialogIcon.Error);
                }
                return;
            }

            ArrayAdapter<WarehouseCountingDetailData> adapter = (ArrayAdapter<WarehouseCountingDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                WarehouseCountingDetailData pData = adapter.getItem(i);
                if (pData.getID() == tempWarehouseCountingDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, tempWarehouseCountingDetailData);
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
            setFooter();
            warehouseCountingActivityMode = WarehouseCountingActivityMode.AfterUpdate;
        }
        else if(warehouseCountingActivityMode == WarehouseCountingActivityMode.BeforeSendDataToSAP)
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
            Utility.simpleAlert(this, "ارسال اطلاعات به مركز انجام گرديد." + "\n" +
                    taskResult.message,"", DialogIcon.Info, onFinishClick);
            warehouseCountingActivityMode = WarehouseCountingActivityMode.AfterSendDataToSAP;
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
        ArrayAdapter<WarehouseCountingDetailData> adapter = (ArrayAdapter<WarehouseCountingDetailData>) listView.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++)
        {
            WarehouseCountingDetailData iData = adapter.getItem(i);
            if (iData.getItemID() == Integer.valueOf( warehouseCountingProductControlData.getProductCode()))
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("اين كالا قبلاً به مقدار " +
                        + iData.getAmount() + " "
                        + "ثبت شده است. مقدار قبلي با مقدار جديد تجميع گردد؟");

                dlgAlert.setTitle("كالاي تكراري");
                dlgAlert.setCancelable(true);
                dlgAlert.setPositiveButton("جمع كن",
                        (dialog, which) -> updateDuplicate(true, iData));
                dlgAlert.setNegativeButton("جايگزين كن",
                        (dialog, which) -> updateDuplicate(false, iData));
                dlgAlert.setNeutralButton("انصراف",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

                dlgAlert.setIcon(R.drawable.question128);
                dlgAlert.create().show();

                break;
            }
        }
    }

    WarehouseCountingDetailData tempWarehouseCountingDetailData;
    private void updateDuplicate(boolean addToOldValue, WarehouseCountingDetailData iData)
    {
        double inputValue = Double.valueOf( ThousandSeparatorWatcher.removeSeparator( etAmount.getText().toString()));
        double newCount = 0;
        if(addToOldValue)
        {
            double oldCount = iData.getAmount();
            newCount = inputValue + oldCount;
        }
        else
        {
            newCount = inputValue;
        }
        warehouseCountingActivityMode = WarehouseCountingActivityMode.BeforeUpdate;
        PickingDeliverService service = new PickingDeliverService(PickingDeliverServiceMode.UpdateCounting,this);

        service.addParam("ID", iData.getID() );
        service.addParam("Amount", newCount );

        iData.setAmount(newCount);
        tempWarehouseCountingDetailData = iData;

        service.listener = this;
        service.execute();
        startWait();
    }
    private void insertToList(String tag)
    {
        int HeaderID = Integer.parseInt( tag.split(",")[0]);
        int DetailID = Integer.parseInt( tag.split(",")[1]);
        if(warehouseCountingWrapper == null)
        {
            warehouseCountingWrapper = new WarehouseCountingWrapper();
        }
        if(warehouseCountingWrapper.getWarehouseCountingHeaderData().getID() == 0)
        {
            warehouseCountingWrapper.getWarehouseCountingHeaderData().setID(HeaderID);
            warehouseCountingWrapper.getWarehouseCountingHeaderData().setInsertDate(Utility.getCurrentPersianDate());
            warehouseCountingWrapper.getWarehouseCountingHeaderData().setStatusID(0);
            warehouseCountingWrapper.getWarehouseCountingHeaderData().setLineNumber(Integer.valueOf( sLine.getSelectedItem().toString()));
            warehouseCountingWrapper.getWarehouseCountingHeaderData().setUserName(GlobalData.getUserName());
        }
        WarehouseCountingDetailData detailData = new WarehouseCountingDetailData();
        detailData.setID(DetailID);
        detailData.setHeaderID(HeaderID);
        detailData.setItemID(Integer.parseInt( warehouseCountingProductControlData.getProductCode()));
        detailData.setItemName(warehouseCountingProductControlData.getProductName());
        detailData.setAmount( Integer.parseInt( ThousandSeparatorWatcher.removeSeparator( etAmount.getText().toString())));

        row_warehouse_counting adapter = (row_warehouse_counting)listView.getAdapter();
        warehouseCountingWrapper.getWarehouseCountingDetailDataList().add(detailData);
        if(adapter == null)
        {
            adapter = new row_warehouse_counting(this,
                    warehouseCountingWrapper.getWarehouseCountingDetailDataList());
            listView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
        Utility.setListCount(adapter.getCount(), tvCount);
    }
    public void onInsertClick(View view)
    {
        if (!validateData()) return;

        warehouseCountingActivityMode = WarehouseCountingActivityMode.BeforeInsertDetail;
        PickingDeliverService service = new PickingDeliverService(PickingDeliverServiceMode.InsertCounting,this);

        service.addParam("ItemID", warehouseCountingProductControlData.getProductCode());
        service.addParam("ItemName", warehouseCountingProductControlData.getProductName());
        service.addParam("Amount", ThousandSeparatorWatcher.removeSeparator( etAmount.getText().toString()));
        service.addParam("Line", sLine.getSelectedItem().toString());
        service.addParam("HeaderID", warehouseCountingWrapper == null ? 0 : warehouseCountingWrapper.getWarehouseCountingHeaderData().getID());

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
        if(warehouseCountingProductControlData == null)
        {
            Utility.simpleAlert(this,"ابتدا كالا را انتخاب نماييد.", DialogIcon.Warning);
            return false;
        }
        if(sLine.getSelectedItemId() == 0)
        {
            Toast.makeText(this, "لاین را مشخص نمایید", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Utility.editTextIsEmpty(etAmount, "تعداد كالا را وارد نماييد")) return false;
        return true;
    }

    private void Delete()
    {
        if (warehouseCountingDetailData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا اطلاعات مربوط به كالاي زير حذف گردد؟" + "\n"
                + warehouseCountingDetailData.getItemID() + "\n"
                + warehouseCountingDetailData.getItemName());

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        WarehouseCountingActivity.this.warehouseCountingActivityMode
                                = WarehouseCountingActivityMode.BeforeDelete;

                        PickingDeliverService task = new PickingDeliverService(PickingDeliverServiceMode.DeleteCounting
                                , WarehouseCountingActivity.this);

                        task.addParam("ID", warehouseCountingDetailData.getID());

                        task.listener = WarehouseCountingActivity.this;
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
            warehouseCountingDetailData = (WarehouseCountingDetailData) data.getSerializableExtra("warehouseCountingDetailData");

            ArrayAdapter<WarehouseCountingDetailData> adapter = (ArrayAdapter<WarehouseCountingDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                WarehouseCountingDetailData pData = adapter.getItem(i);
                if (pData.getID() == warehouseCountingDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, warehouseCountingDetailData);
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
        else if(requestCode == GlobalData.getBarcodeActivityRequestCode())
        {
            productCodeFragment.setResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void OnBarcodeScannedByCamera(String barcode) {
        getProductInfo(barcode);
    }

    @Override
    public void OnBarcodeChanged(String barcode) {
        reset(false);
    }
    @Override
    public void OnConfirm(String barcode)
    {
        if(!validateProductControl()) return;
        getProductInfo(barcode);
    }

    @Override
    public void OnWarehouseCountingCommand(WarehouseCountingDetailData warehouseCountingDetailData, int position, row_warehouse_counting.CountingCommandType commandType)
    {
        this.warehouseCountingDetailData = warehouseCountingDetailData;
        ((row_warehouse_counting)listView.getAdapter()).setSelection(position);
        if(commandType == row_warehouse_counting.CountingCommandType.Delete)
        {
            Delete();
        }
        else if(commandType == row_warehouse_counting.CountingCommandType.Edit)
        {
            Intent intent = new Intent(this, EditWarehouseCountingActivity.class);
            intent.putExtra("warehouseCountingDetailData", warehouseCountingDetailData);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}