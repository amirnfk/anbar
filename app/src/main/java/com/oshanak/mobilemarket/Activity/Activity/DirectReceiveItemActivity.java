package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.oshanak.mobilemarket.Activity.Activity.Enum.DirectReceiveItemActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ProductCodeFragment;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DirectReceiveDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.DirectReceiveHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductControlContainerData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductUnitData;
import com.oshanak.mobilemarket.Activity.DataStructure.VendorData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Enum.Font;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_direct_receive_item;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_unit;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import javadz.beanutils.BeanUtils;

public class DirectReceiveItemActivity extends BaseActivity implements
    ProductCodeFragment.OnConfirmListener
    ,ProductCodeFragment.OnBarcodeScannedByCameraListener
    ,ProductCodeFragment.OnBarcodeChanged
    , OnTaskCompleted
    ,row_direct_receive_item.OnDirectReceiveDetailListCommandListener
{
    private VendorData vendorData;
    private TextView tvVendor;
    private TextView tvProductCode;
    private TextView tvCustomerPrice;
    private TextView tvSalePrice;
    private Spinner sUnit;
    private EditText etAmount;
    private ListView listView;
    private TextView tvCount;
    private TextView tvHeaderId;
    private TextView tvTotalCount;
    private DirectReceiveItemActivityMode directReceiveItemActivityMode;
    private ProductCodeFragment productCodeFragment;
    private ProductControlContainerData productControlContainerData;
    private DirectReceiveHeaderData directReceiveHeaderData;
    private DirectReceiveDetailData directReceiveDetailData;
    private boolean newDocumentInserted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_receive_item);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        Intent intent = getIntent();
        directReceiveHeaderData = (DirectReceiveHeaderData) intent.getSerializableExtra("directReceiveHeaderData");
        vendorData = (VendorData) intent.getSerializableExtra("vendorData");
        if(directReceiveHeaderData != null && vendorData == null)
        {
            vendorData = new VendorData();
            vendorData.setID(String.valueOf( directReceiveHeaderData.getVendorID()));
            vendorData.setName(directReceiveHeaderData.getVendorName());
        }

        tvVendor = findViewById(R.id.tvVendor);
        tvProductCode = findViewById(R.id.tvProductCode);
        tvCustomerPrice = findViewById(R.id.tvCustomerPrice);
        tvSalePrice = findViewById(R.id.tvSalePrice);
        sUnit = findViewById(R.id.sUnit);
        etAmount = findViewById(R.id.etAmount);
        listView = findViewById(R.id.recyclerview);
        tvCount = findViewById(R.id.tvCount);
        productCodeFragment = (ProductCodeFragment)getSupportFragmentManager().findFragmentById(R.id.frProductCode);
        tvHeaderId = findViewById(R.id.tvHeaderId);
        tvTotalCount = findViewById(R.id.tvTotalCount);

        tvVendor.setText(vendorData.getID() + " - " + vendorData.getName());
        if(directReceiveHeaderData != null)
        {
//            tvHeaderId.setText(String.valueOf(directReceiveHeaderData.getID()) + " - " +  directReceiveHeaderData.getInsertDate().substring(0,10));
            setDate(directReceiveHeaderData.getID(), directReceiveHeaderData.getInsertDate());
        }
        Utility.clearGeneralSpinner(this, sUnit);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                directReceiveDetailData = (DirectReceiveDetailData) parent.getItemAtPosition(position);
                ((row_direct_receive_item)parent.getAdapter()).setSelection(position);
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
    private void setDate(int docID, String date)
    {
        tvHeaderId.setText(String.valueOf(docID) + " - " +  date.substring(0,10));
    }
    private void setFooter()
    {
        ArrayAdapter<DirectReceiveDetailData> adapter = (ArrayAdapter<DirectReceiveDetailData>)listView.getAdapter();
        int totalCount = 0;

        for (int i = 0; i < adapter.getCount(); i++)
        {
            DirectReceiveDetailData data = adapter.getItem(i);
            totalCount += data.getAmount();
        }
        tvTotalCount.setText(ThousandSeparatorWatcher.addSeparator(totalCount));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.direct_receive_item_menu, menu);
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

        Intent intent = new Intent(this, SendDirectReceiveToSAPActivity.class);
        intent.putExtra("directReceiveHeaderData", directReceiveHeaderData);
        startActivityForResult(intent, 2);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void getList()
    {
        if(directReceiveHeaderData == null) return;

        directReceiveItemActivityMode = DirectReceiveItemActivityMode.BeforeGetDetailList;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetDirectReceiveDetail, this);

        task.addParam("HeaderID", directReceiveHeaderData.getID());

        task.listener = this;
        task.execute();
        startWait();
    }
    @Override
    public void onBackPressed()
    {
        if(directReceiveItemActivityMode == DirectReceiveItemActivityMode.AfterSendDataToSAP
                || newDocumentInserted || !newDateTime.equals(""))
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("refreshList", true);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void OnConfirm(String barcode)
    {
        if(!validateProductControl()) return;
        getItemInfo(barcode);
    }
    private void getItemInfo(String barcode)
    {
        reset(false);

        directReceiveItemActivityMode = DirectReceiveItemActivityMode.BeforeGetDirectItemInfo;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetDirectProductInfo, this);

        task.addParam("storeID", GlobalData.getStoreID());
        task.addParam("barcode", barcode);
        task.addParam("vendorCode", vendorData.getID());

        task.listener = this;
        task.execute();
        startWait();
    }
    private void reset(boolean resetProductFragment)
    {
        tvProductCode.setText("");
        tvSalePrice.setText("");
        tvCustomerPrice.setText("");
        etAmount.setText("");
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

        if(directReceiveItemActivityMode == DirectReceiveItemActivityMode.BeforeGetDirectItemInfo )
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            else if (!taskResult.isSuccessful && (taskResult.isExceptionOccured("No rows found!") ||
                    taskResult.isExceptionOccured("product does not exist")))
            {
                Utility.simpleAlert(this,"كالايي با اين مشخصات يافت نشد!", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("product is blocked in this store"))
            {
                Utility.simpleAlert(this,"کالا در این فروشگاه غیرفعال می باشد!", DialogIcon.Error);
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("product vendor is differ to selected vendor"))
            {
                Utility.simpleAlert(this,"فروشنده متفاوت است. امکان افزودن اين کالا وجود ندارد!", DialogIcon.Error);
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
            tvSalePrice.setText(ThousandSeparatorWatcher.addSeparator( productControlContainerData.getProductControlData().getSalePrice()));
            tvCustomerPrice.setText(ThousandSeparatorWatcher.addSeparator( productControlContainerData.getProductControlData().getPrice()));

            List<ProductUnitData> unitDataList = productControlContainerData.getProductUnitDataList();

            row_unit adapter = new row_unit(this, unitDataList);
            sUnit.setAdapter(adapter);
            for(int i = 0; i < sUnit.getCount(); i++)
            {
                String unit = ((ProductUnitData)sUnit.getItemAtPosition(i)).getUnitName();
                if(unit.equals("PC"))
                {
                    sUnit.setSelection(i);
                    break;
                }
            }
            Utility.hideKeyboard(this);
            Utility.playBeep();
            etAmount.requestFocus();
            Utility.showKeyboard(this);
            directReceiveItemActivityMode = DirectReceiveItemActivityMode.AfterGetDirectItemInfo;
        }
        else if(directReceiveItemActivityMode == DirectReceiveItemActivityMode.BeforeInsertDetail)
        {
            if (!taskResult.isSuccessful)
            {
                if(taskResult.isExceptionOccured("IX_DirectReceiveDetail_HeaderID_ItemID"))
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
            directReceiveItemActivityMode = DirectReceiveItemActivityMode.AfterInsertDetail;
        }
        else if(directReceiveItemActivityMode == DirectReceiveItemActivityMode.BeforeGetDetailList)
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
            ArrayList<DirectReceiveDetailData> list = (ArrayList<DirectReceiveDetailData>) taskResult.dataStructure;

            row_direct_receive_item adapter = new row_direct_receive_item(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            setFooter();
            directReceiveItemActivityMode = DirectReceiveItemActivityMode.AfterGetDetailList;
//            if(adapter.getCount() >= 200)
//            {
//                Toast.makeText(this,
//                        getString( R.string.only_200_rows)
//                        ,Toast.LENGTH_LONG).show();
//            }
        }
        else if(directReceiveItemActivityMode == DirectReceiveItemActivityMode.BeforeDelete)
        {
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
                return;
            }

            ArrayAdapter<DirectReceiveDetailData> adapter = (ArrayAdapter<DirectReceiveDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                DirectReceiveDetailData pData = adapter.getItem(i);
                if (pData.getID() == directReceiveDetailData.getID())
                {
                    adapter.remove(pData);
                    directReceiveDetailData = null;
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
            setFooter();
            directReceiveItemActivityMode = DirectReceiveItemActivityMode.AfterDelete;
        }
        else if(directReceiveItemActivityMode == DirectReceiveItemActivityMode.BeforeUpdate)
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

            ArrayAdapter<DirectReceiveDetailData> adapter = (ArrayAdapter<DirectReceiveDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                DirectReceiveDetailData pData = adapter.getItem(i);
                if (pData.getID() == tempDirectReceiveDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, tempDirectReceiveDetailData);
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
            directReceiveItemActivityMode = DirectReceiveItemActivityMode.AfterUpdate;

        }
        else if(directReceiveItemActivityMode == DirectReceiveItemActivityMode.BeforeUpdateDocumentDate)
        {
            if (!taskResult.isSuccessful && taskResult.isExceptionOccured("Factor date is greater than current date") )
            {
                Utility.simpleAlert(this,"تاريخ انتخابي نمي تواند بزرگتر از تاريخ امروز باشد.",""
                        , DialogIcon.Warning);
                return;
            }
            else if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, "اشكال در اصلاح تاريخ سند", DialogIcon.Error);
            }
            Toast.makeText(this, "تاريخ سند اصلاح گردید.", Toast.LENGTH_SHORT).show();
//            tvHeaderId.setText(ThousandSeparatorWatcher.addSeparator( headerID) + " - " + insertDate.substring(0,16));
            directReceiveHeaderData.setInsertDate(newDateTime);
            setDate(directReceiveHeaderData.getID(), newDateTime);
//            newDateTime = "";
            directReceiveItemActivityMode = DirectReceiveItemActivityMode.AfterUpdateDocumentDate;
        }
//        else if(directReceiveItemActivityMode == DirectReceiveItemActivityMode.BeforeSendDataToSAP)
//        {
//            if (Utility.generalErrorOccurred(taskResult, this))
//            {
//                return;
//            }
//            if (!taskResult.isSuccessful)
//            {
//                Utility.simpleAlert(this, "خطا در ارسال اطلاعات به مركز." + "\n" +
//                        taskResult.message, DialogIcon.Error);
//                return;
//            }
//
//            Utility.hideKeyboard(this);
//            directReceiveItemActivityMode = DirectReceiveItemActivityMode.AfterSendDataToSAP;
//            Utility.simpleAlert(this, "ارسال اطلاعات به مركز انجام گرديد." ,"", DialogIcon.Info, onFinishClick);
//        }
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
        ArrayAdapter<DirectReceiveDetailData> adapter = (ArrayAdapter<DirectReceiveDetailData>) listView.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++)
        {
            DirectReceiveDetailData iData = adapter.getItem(i);
            if (iData.getItemID() == Integer.valueOf( productControlContainerData.getProductControlData().getProductCode()))
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("اين كالا قبلاً به مقدار " +
                        + iData.getAmount() + " "
                        + iData.getPartUnit() + " "
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

    DirectReceiveDetailData tempDirectReceiveDetailData;
    private void updateDuplicate(boolean addToOldValue, DirectReceiveDetailData iData)
    {
        int newCount = getPcAmount();
        if(addToOldValue)
        {
            newCount += iData.getAmount();
        }
        directReceiveItemActivityMode = DirectReceiveItemActivityMode.BeforeUpdate;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateDirectReceiveItem,this);

        service.addParam("detailID", iData.getID());
        service.addParam("amount", newCount);

        iData.setAmount(newCount);
        tempDirectReceiveDetailData = iData;

        service.listener = this;
        service.execute();
        startWait();
    }
    private void insertToList(String tag)
    {
        int headerID = Integer.parseInt( tag.split(",")[0]);
        int detailID = Integer.parseInt( tag.split(",")[1]);

        if(directReceiveHeaderData == null)
        {
            String insertDate = tag.split(",")[2];
            directReceiveHeaderData = new DirectReceiveHeaderData();
            directReceiveHeaderData.setID(headerID);
            directReceiveHeaderData.setVendorID(Integer.valueOf( vendorData.getID()));
            directReceiveHeaderData.setVendorName(vendorData.getName());
            directReceiveHeaderData.setInsertDate(insertDate);
//            tvHeaderId.setText(ThousandSeparatorWatcher.addSeparator( headerID) + " - " + insertDate.substring(0,10).substring(0,10));
            setDate(headerID, insertDate.substring(0,10));
            newDocumentInserted = true;
        }

        DirectReceiveDetailData data = new DirectReceiveDetailData();
        data.setID(detailID);
        data.setHeaderID(headerID);
        data.setItemID(Integer.valueOf( productControlContainerData.getProductControlData().getProductCode()));
        data.setAmount(getPcAmount());
        data.setItemName(productControlContainerData.getProductControlData().getProductName());
        data.setPartUnit(productControlContainerData.getPartUnit().getUnitName());
        data.setWholeUnit(productControlContainerData.getWholeUnit().getUnitName());
        data.setBoxQuantity(Integer.valueOf( productControlContainerData.getWholeUnit().getAmount()));

        row_direct_receive_item adapter = (row_direct_receive_item)listView.getAdapter();
        if(adapter == null)
        {
            adapter = new row_direct_receive_item(this, new ArrayList<>());
            listView.setAdapter(adapter);
        }
        adapter.add(data);
        adapter.notifyDataSetChanged();
        Utility.setListCount(adapter.getCount(), tvCount);

    }
    public void onInsertClick(View view)
    {
        if (!validateData()) return;
//
        directReceiveItemActivityMode = DirectReceiveItemActivityMode.BeforeInsertDetail;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.InsertDirectReceiveItem,this);

        service.addParam("HeaderID", directReceiveHeaderData == null? 0 : directReceiveHeaderData.getID());
        service.addParam("StoreID", GlobalData.getStoreID());
        service.addParam("VendorID", vendorData.getID());
        service.addParam("VendorName", vendorData.getName());
        service.addParam("ItemID", productControlContainerData.getProductControlData().getProductCode());
        service.addParam("ItemName", productControlContainerData.getProductControlData().getProductName());

        int amount = getPcAmount();
        service.addParam("Amount", amount);
        service.addParam("PartUnit", productControlContainerData.getPartUnit().getUnitName());
        service.addParam("WholeUnit", productControlContainerData.getWholeUnit().getUnitName());
        service.addParam("BoxQuantity", productControlContainerData.getWholeUnit().getAmount());

        service.listener = this;
        service.execute();
        startWait();
    }
    private int getPcAmount()
    {
        int count = Integer.valueOf( etAmount.getText().toString());
        int boxUnit = Integer.valueOf (((ProductUnitData)sUnit.getSelectedItem()).getAmount());
        int amount = count * boxUnit;
        return amount;
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
        if (directReceiveDetailData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا اطلاعات مربوط به كالاي زير حذف گردد؟" + "\n"
                + directReceiveDetailData.getItemID() + "\n"
                + directReceiveDetailData.getItemName());

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DirectReceiveItemActivity.this.directReceiveItemActivityMode
                                = DirectReceiveItemActivityMode.BeforeDelete;

                        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.DeleteDirectReceiveItem
                                , DirectReceiveItemActivity.this);

                        task.addParam("detailID", directReceiveDetailData.getID());

                        task.listener = DirectReceiveItemActivity.this;
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
            directReceiveDetailData = (DirectReceiveDetailData) data.getSerializableExtra("directReceiveDetailData");

            ArrayAdapter<DirectReceiveDetailData> adapter = (ArrayAdapter<DirectReceiveDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                DirectReceiveDetailData pData = adapter.getItem(i);
                if (pData.getID() == directReceiveDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, directReceiveDetailData);
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
        if(requestCode == 2 && resultCode == Activity.RESULT_OK)
        {
            Utility.hideKeyboard(this);
            directReceiveItemActivityMode = DirectReceiveItemActivityMode.AfterSendDataToSAP;
            onBackPressed();
        }
        else if(requestCode == GlobalData.getBarcodeActivityRequestCode())
        {
            productCodeFragment.setResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void OnBarcodeScannedByCamera(String barcode) {
        getItemInfo(barcode);
    }

    @Override
    public void OnBarcodeChanged(String barcode) {
        reset(false);
    }

    @Override
    public void OnDirectReceiveListCommand(DirectReceiveDetailData directReceiveDetailData, int position, row_direct_receive_item.DirectReceiveDetailCommandType commandType)
    {
        this.directReceiveDetailData = directReceiveDetailData;
        ((row_direct_receive_item)listView.getAdapter()).setSelection(position);
        if(commandType == row_direct_receive_item.DirectReceiveDetailCommandType.Delete)
        {
            Delete();
        }
        else if(commandType == row_direct_receive_item.DirectReceiveDetailCommandType.Edit)
        {
            Intent intent = new Intent(this, EditDirectReceiveDetailActivity.class);
            intent.putExtra("directReceiveDetailData", directReceiveDetailData);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
    String newDateTime = "";
    public void showDatePicker(View view)
    {
        if(directReceiveHeaderData == null)
        {
            Toast.makeText(this, "امكان اصلاح تاريخ الان ميسر نيست", Toast.LENGTH_SHORT).show();
            return;
        }
        String date1 = directReceiveHeaderData.getInsertDate().substring(0,10);
        PersianCalendar initDate = new PersianCalendar();
        initDate.setPersianDate(Integer.parseInt(date1.substring(0,4))
                ,Integer.parseInt(date1.substring(5,7))
                ,Integer.parseInt(date1.substring(8,10)));

        PersianDatePickerDialog picker = new PersianDatePickerDialog(this)
                .setPositiveButtonString("قبول")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1400)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setInitDate(initDate)
                .setActionTextColor(Color.GRAY)
                .setTypeFace(Utility.getTypeFace(getBaseContext(), Font.SansIran))
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar) {

                        String date = Utility.getPersianShortDate( persianCalendar);
                        newDateTime = date;

                        updateDocumentDate();
//                        anyChangesDone = true;
                    }

                    @Override
                    public void onDismissed() {

                    }
                });
        picker.show();
    }
    public void updateDocumentDate()
    {
        directReceiveItemActivityMode = DirectReceiveItemActivityMode.BeforeUpdateDocumentDate;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateDirectReceiveDocDate,this);

        service.addParam("HeaderID", directReceiveHeaderData.getID());
        service.addParam("newDate", newDateTime);

        service.listener = this;
        service.execute();
        startWait();
    }
}