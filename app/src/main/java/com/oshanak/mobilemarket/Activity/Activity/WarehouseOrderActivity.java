package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.oshanak.mobilemarket.Activity.Activity.Enum.WarehouseOrderActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ProductCodeFragment;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductControlContainerData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductUnitData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehouseOrderData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_unit;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_warehouse_order;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javadz.beanutils.BeanUtils;

public class WarehouseOrderActivity extends BaseActivity implements
        OnTaskCompleted
        , ProductCodeFragment.OnConfirmListener
        ,ProductCodeFragment.OnBarcodeScannedByCameraListener
        ,ProductCodeFragment.OnBarcodeChanged
        , row_warehouse_order.OnWarehouseOrderCommandListener
{
    private TextView tvProductCode;
    private TextView tvLast14DaysSale;
    private Spinner sUnit;
    private EditText etAmount;
    private ListView listView;
    private TextView tvCount;
    private TextView tvTotalCount;
    private ProductCodeFragment productCodeFragment;
    private WarehouseOrderActivityMode warehouseOrderActivityMode;
    private WarehouseOrderData warehouseOrderData;
    private ProductControlContainerData productControlContainerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_order);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvProductCode = findViewById(R.id.tvProductCode);
        tvLast14DaysSale = findViewById(R.id.tvLast14DaysSale);
        sUnit = findViewById(R.id.sUnit);
        etAmount = findViewById(R.id.etAmount);
        listView = findViewById(R.id.recyclerview);
        tvCount = findViewById(R.id.tvCount);
        productCodeFragment = (ProductCodeFragment)getSupportFragmentManager().findFragmentById(R.id.frProductCode);
        tvTotalCount = findViewById(R.id.tvTotalCount);

        Utility.clearGeneralSpinner(this, sUnit);
        Utility.enableViews(this,false, sUnit);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                warehouseOrderData = (WarehouseOrderData) parent.getItemAtPosition(position);
                ((row_warehouse_order)parent.getAdapter()).setSelection(position);
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
        inflater.inflate(R.menu.warehouse_order_menu, menu);
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
                        warehouseOrderActivityMode = WarehouseOrderActivityMode.BeforeSendDataToSAP;
                        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.SendWarehouseOrderToSAP
                                ,WarehouseOrderActivity.this);

                        service.addParam("StoreID", GlobalData.getStoreID());

                        service.listener = WarehouseOrderActivity.this;
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
        ArrayAdapter<WarehouseOrderData> adapter = (ArrayAdapter<WarehouseOrderData>)listView.getAdapter();
        int totalCount = 0;

        for (int i = 0; i < adapter.getCount(); i++)
        {
            WarehouseOrderData data = adapter.getItem(i);
            totalCount += data.getAmount();
        }
        tvTotalCount.setText(ThousandSeparatorWatcher.addSeparator(totalCount));
    }
    private void getList()
    {
        warehouseOrderActivityMode = WarehouseOrderActivityMode.BeforeGetDetailList;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetWarehouseOrder, this);

        task.addParam("StoreID", GlobalData.getStoreID());

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

    @Override
    public void OnConfirm(String barcode)
    {
        if(!validateProductControl()) return;
        getProductInfo(barcode);
    }
    private void getProductInfo(String barcode)
    {
        reset(false);

        warehouseOrderActivityMode = WarehouseOrderActivityMode.BeforeGetProductInfo;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetMaterialInfo, this);
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("barcode");
        pi.setValue(barcode);
        task.piList.add(pi);

        task.listener = this;
        task.execute();

        startWait();
    }
    private void reset(boolean resetProductFragment)
    {
        tvProductCode.setText("");
        etAmount.setText("");
        tvLast14DaysSale.setText("");
        Utility.clearGeneralSpinner(this, sUnit);
        if(resetProductFragment) {
            productCodeFragment.setProductCode("");
        }
        productControlContainerData = null;
    }

    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if(warehouseOrderActivityMode == WarehouseOrderActivityMode.BeforeGetProductInfo )
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("product does not exist"))
            {
                Utility.simpleAlert(this,"كالايي با اين مشخصات يافت نشد!", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this,getString(R.string.general_error), DialogIcon.Error);
                return;
            }
            productControlContainerData = (ProductControlContainerData) taskResult.dataStructure;

            tvProductCode.setText(String.valueOf( productControlContainerData.getProductControlData().getProductCode()) + " - " +
                    productControlContainerData.getProductControlData().getProductName());
            tvLast14DaysSale.setText(ThousandSeparatorWatcher.addSeparator( productControlContainerData.getProductControlData().getSoldCount()));

            List<ProductUnitData> unitDataList = productControlContainerData.getProductUnitDataList();

            row_unit adapter = new row_unit(this, unitDataList);
            sUnit.setAdapter(adapter);
            for(int i = 0; i < sUnit.getCount(); i++)
            {
                String unit = ((ProductUnitData)sUnit.getItemAtPosition(i)).getUnitName();
                if(unit.equals("CAR"))
                {
                    sUnit.setSelection(i);
                    break;
                }
            }

            Utility.hideKeyboard(this);
            Utility.playBeep();
            etAmount.requestFocus();
            Utility.showKeyboard(this);
            warehouseOrderActivityMode = WarehouseOrderActivityMode.AfterGetProductInfo;
        }
        else if(warehouseOrderActivityMode == WarehouseOrderActivityMode.BeforeInsertDetail)
        {
            if (!taskResult.isSuccessful)
            {
                if(taskResult.isExceptionOccured("IX_WarehouseOrder_StoreID_ItemID_StatusID"))
                {
                    duplicateDetected();
                }
                else
                {
                    Utility.simpleAlert(this, getString( R.string.insert_do_not), DialogIcon.Error);
                }
                return;
            }
            Toast.makeText(this, "ثبت اطلاعات انجام شد.",Toast.LENGTH_SHORT).show();
            insertToList(  taskResult.tag);
            Utility.hideKeyboard(this);
            Utility.playShortBeep();
            reset(true);
            setFooter();
            if(productCodeFragment.isAutoScanEnable()) productCodeFragment.startScan();
            warehouseOrderActivityMode = WarehouseOrderActivityMode.AfterInsertDetail;
        }
        else if(warehouseOrderActivityMode == WarehouseOrderActivityMode.BeforeGetDetailList)
        {
            reset(true);
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
            ArrayList<WarehouseOrderData> list = (ArrayList<WarehouseOrderData>) taskResult.dataStructure;

            row_warehouse_order adapter = new row_warehouse_order(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            setFooter();
            warehouseOrderActivityMode = WarehouseOrderActivityMode.AfterGetDetailList;
//            if(adapter.getCount() >= 200)
//            {
//                Toast.makeText(this,
//                        getString( R.string.only_200_rows)
//                        ,Toast.LENGTH_LONG).show();
//            }
        }
        else if(warehouseOrderActivityMode == WarehouseOrderActivityMode.BeforeDelete)
        {
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
                return;
            }

            ArrayAdapter<WarehouseOrderData> adapter = (ArrayAdapter<WarehouseOrderData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                WarehouseOrderData pData = adapter.getItem(i);
                if (pData.getID() == warehouseOrderData.getID())
                {
                    adapter.remove(pData);
                    warehouseOrderData = null;
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
            setFooter();
            warehouseOrderActivityMode = WarehouseOrderActivityMode.AfterDelete;
        }
        else if(warehouseOrderActivityMode == WarehouseOrderActivityMode.BeforeUpdate)
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            if (!taskResult.isSuccessful) {
                if (!Utility.generalErrorOccurred(taskResult, this)) {
                    Utility.simpleAlert(this, getString(R.string.update_do_not), DialogIcon.Error);
                }
                return;
            }

            ArrayAdapter<WarehouseOrderData> adapter = (ArrayAdapter<WarehouseOrderData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                WarehouseOrderData pData = adapter.getItem(i);
                if (pData.getID() == tempWarehouseOrderData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, tempWarehouseOrderData);
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
            warehouseOrderActivityMode = WarehouseOrderActivityMode.AfterUpdate;

        }
        else if(warehouseOrderActivityMode == WarehouseOrderActivityMode.BeforeSendDataToSAP)
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
                    "سفارشي با شماره موقت " + taskResult.dataStructure.toString() + " در مركز ايجاد شد.","", DialogIcon.Info, onFinishClick);
            warehouseOrderActivityMode = WarehouseOrderActivityMode.AfterSendDataToSAP;
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
        ArrayAdapter<WarehouseOrderData> adapter = (ArrayAdapter<WarehouseOrderData>) listView.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++)
        {
            WarehouseOrderData iData = adapter.getItem(i);
            if (iData.getItemID() == Integer.valueOf( productControlContainerData.getProductControlData().getProductCode()))
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("اين كالا قبلاً به مقدار " +
                        + iData.getAmount() + " "
                        + iData.getSelectedUnit() + " "
                        + "ثبت شده است. مقدار قبلي با مقدار جديد تجميع گردد؟");

                dlgAlert.setTitle("كالاي تكراري");
                dlgAlert.setCancelable(true);
                dlgAlert.setPositiveButton("جمع كن",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                updateDuplicate(true, iData);
                            }
                        });
                dlgAlert.setNegativeButton("جايگزين كن",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                updateDuplicate(false, iData);
                            }
                        });
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

    WarehouseOrderData tempWarehouseOrderData;
    private void updateDuplicate(boolean addToOldValue, WarehouseOrderData iData)
    {
        int newCount = Integer.valueOf( etAmount.getText().toString());//getPcAmount();
        if(addToOldValue)
        {
            newCount += iData.getAmount();
        }
        warehouseOrderActivityMode = WarehouseOrderActivityMode.BeforeUpdate;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateWarehouseOrder,this);
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("ID");
        pi.setValue(iData.getID());
        service.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("amount");
        pi.setValue(newCount);
        service.piList.add(pi);

        iData.setAmount(newCount);
        tempWarehouseOrderData = iData;

        service.listener = this;
        service.execute();
        startWait();
    }
    private void insertToList(String tag)
    {
        WarehouseOrderData data = new WarehouseOrderData();
        data.setID(Integer.parseInt( tag));
        data.setItemID(Integer.valueOf( productControlContainerData.getProductControlData().getProductCode()));
        data.setItemName(productControlContainerData.getProductControlData().getProductName());
        data.setAmount(Integer.parseInt( etAmount.getText().toString()));
        data.setSelectedUnit(((ProductUnitData)sUnit.getSelectedItem()).getUnitName());
        data.setPartUnit(productControlContainerData.getPartUnit().getUnitName());
        data.setWholeUnit(productControlContainerData.getWholeUnit().getUnitName());
        data.setBoxQuantity(Integer.valueOf( productControlContainerData.getWholeUnit().getAmount()));

        row_warehouse_order adapter = (row_warehouse_order)listView.getAdapter();
        if(adapter == null)
        {
            adapter = new row_warehouse_order(this, new ArrayList<>());
            listView.setAdapter(adapter);
        }
        adapter.add(data);
        adapter.notifyDataSetChanged();
        Utility.setListCount(adapter.getCount(), tvCount);
    }
    public void onInsertClick(View view)
    {
        if (!validateData()) return;

        warehouseOrderActivityMode = WarehouseOrderActivityMode.BeforeInsertDetail;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.InsertWarehouseOrder,this);

        service.addParam("StoreID", GlobalData.getStoreID());
        service.addParam("ItemID", productControlContainerData.getProductControlData().getProductCode());
        service.addParam("ItemName", productControlContainerData.getProductControlData().getProductName());
        service.addParam("Amount", etAmount.getText().toString());
        service.addParam("SelectedUnit", ((ProductUnitData)sUnit.getSelectedItem()).getUnitName());
        service.addParam("PartUnit", productControlContainerData.getPartUnit().getUnitName());
        service.addParam("WholeUnit", productControlContainerData.getWholeUnit().getUnitName());
        service.addParam("BoxQuantity", productControlContainerData.getWholeUnit().getAmount());

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
        if(productControlContainerData == null)
        {
            Utility.simpleAlert(this,"ابتدا كالا را انتخاب نماييد.", DialogIcon.Warning);
            return false;
        }
        if (Utility.editTextIsEmpty(etAmount, "تعداد كالا را وارد نماييد")) return false;
        return true;
    }

    private void Delete()
    {
        if (warehouseOrderData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا اطلاعات مربوط به كالاي زير حذف گردد؟" + "\n"
                + warehouseOrderData.getItemID() + "\n"
                + warehouseOrderData.getItemName());

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        WarehouseOrderActivity.this.warehouseOrderActivityMode
                                = WarehouseOrderActivityMode.BeforeDelete;

                        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.DeleteWarehouseOrder
                                , WarehouseOrderActivity.this);

                        task.addParam("ID", warehouseOrderData.getID());

                        task.listener = WarehouseOrderActivity.this;
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
            warehouseOrderData = (WarehouseOrderData) data.getSerializableExtra("warehouseOrderData");

            ArrayAdapter<WarehouseOrderData> adapter = (ArrayAdapter<WarehouseOrderData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                WarehouseOrderData pData = adapter.getItem(i);
                if (pData.getID() == warehouseOrderData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, warehouseOrderData);
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
    public void OnWarehouseOrderCommand(WarehouseOrderData warehouseOrderData
            , int position, row_warehouse_order.WarehouseOrderCommandType commandType)
    {
        this.warehouseOrderData = warehouseOrderData;
        ((row_warehouse_order)listView.getAdapter()).setSelection(position);
        if(commandType == row_warehouse_order.WarehouseOrderCommandType.Delete)
        {
            Delete();
        }
        else if(commandType == row_warehouse_order.WarehouseOrderCommandType.Edit)
        {
            Intent intent = new Intent(this, EditWarehouseOrderActivity.class);
            intent.putExtra("warehouseOrderData", warehouseOrderData);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}