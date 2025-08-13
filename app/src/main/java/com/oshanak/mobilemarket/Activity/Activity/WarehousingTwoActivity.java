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

import com.oshanak.mobilemarket.Activity.Activity.Enum.WarehousingTwoActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.NumericUpDownFragment;
import com.oshanak.mobilemarket.Activity.Common.ProductCodeFragment;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingHeaderDetailWrapperData;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingItemUMdata;
import com.oshanak.mobilemarket.Activity.DataStructure.WarehousingProductContainer;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Enum.WarehousingItemFilterMode;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_unit_warehousing;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_warehousing;
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

public class WarehousingTwoActivity extends BaseActivity implements NumericUpDownFragment.OnValueChanged,
        OnTaskCompleted
        , ProductCodeFragment.OnConfirmListener
        ,ProductCodeFragment.OnBarcodeScannedByCameraListener
        ,ProductCodeFragment.OnBarcodeChanged
        , row_warehousing.OnWarehousingCommandListener
{
    private TextView tvProductCode;
    private Spinner sUnit;
    private EditText etAmount;
    private ListView listView;
    private TextView tvCount;
    private TextView tvTotalCount;
    private ProductCodeFragment productCodeFragment;
    private WarehousingTwoActivityMode warehousingTwoActivityMode;
    private WarehousingDetailData warehousingDetailData;
    private WarehousingProductContainer warehousingProductContainer;
    private NumericUpDownFragment numericUpDownFragment;
    private TextView tvCreateDate;
    private TextView tvCountingStatusName;
    private TextView tvFilterMode;
    private WarehousingHeaderDetailWrapperData warehousingHeaderDetailWrapperData;
    private WarehousingItemFilterMode filterMode = WarehousingItemFilterMode.Unknown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehousing_two);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvProductCode = findViewById(R.id.tvProductCode);
        sUnit = findViewById(R.id.sUnit);
        etAmount = findViewById(R.id.etAmount);
        listView = findViewById(R.id.recyclerview);
        tvCount = findViewById(R.id.tvCount);
        productCodeFragment = (ProductCodeFragment)getSupportFragmentManager().findFragmentById(R.id.frProductCode);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        numericUpDownFragment = (NumericUpDownFragment) getSupportFragmentManager().findFragmentById(R.id.numericUpDownFragment);
        tvCreateDate = findViewById(R.id.tvCreateDate);
        tvCountingStatusName = findViewById(R.id.tvCountingStatusName);
        tvFilterMode = findViewById(R.id.tvFilterMode);

        Intent intent = getIntent();
        warehousingTwoActivityMode = (WarehousingTwoActivityMode) intent.getSerializableExtra("warehousingTwoActivityMode");

        Utility.clearGeneralSpinner(this, sUnit);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                warehousingDetailData = (WarehousingDetailData) parent.getItemAtPosition(position);
                ((row_warehousing)parent.getAdapter()).setSelection(position);
            }
        });
        if(warehousingTwoActivityMode == WarehousingTwoActivityMode.BeforeStartWarehousing)
        {
            startWarehousing();
        }
        else if(warehousingTwoActivityMode == WarehousingTwoActivityMode.BeforeContinueWarehousing)
        {
            continueWarehousing(WarehousingItemFilterMode.CountedItem);
        }

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
        EditText etFrgAmount = findViewById(R.id.etFrgAmount);
        etAmount.setOnEditorActionListener(editorActionListener);
        etFrgAmount.setOnEditorActionListener(editorActionListener);
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
        inflater.inflate(R.menu.warehousing_menu, menu);
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
    public void onRefresh(View view)
    {
        continueWarehousing(filterMode);
    }
    public void onFilterList(View view)
    {
        Intent intent = new Intent(this, FilterWarehousingItemActivity.class);
        startActivityForResult(intent, 2);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void send()
    {
        if(listView.getAdapter() ==  null ||
                listView.getAdapter().getCount() == 0)
        {
            Utility.simpleAlert(this, "هيچ شمارشي جهت ارسال ثبت نشده است.", DialogIcon.Warning);
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
                        warehousingTwoActivityMode = WarehousingTwoActivityMode.BeforeSendDataToSAP;
                        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.SendWarehousingToSAP
                                ,WarehousingTwoActivity.this);

                        service.addParam("HeaderCode", warehousingHeaderDetailWrapperData.getWarehousingHeaderList().get(0).getCode());
                        service.addParam("StoreID", GlobalData.getStoreID());

                        service.listener = WarehousingTwoActivity.this;
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
        ArrayAdapter<WarehousingDetailData> adapter = (ArrayAdapter<WarehousingDetailData>)listView.getAdapter();
        int totalCount = 0;

        for (int i = 0; i < adapter.getCount(); i++)
        {
            WarehousingDetailData data = adapter.getItem(i);
            totalCount += data.getCountValue();
        }
        tvTotalCount.setText(ThousandSeparatorWatcher.addSeparator(totalCount));
    }
    private void startWarehousing()
    {
        warehousingTwoActivityMode = WarehousingTwoActivityMode.BeforeStartWarehousing;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.StartWarehousing, this);

        task.addParam("StoreID", GlobalData.getStoreID());
        task.addParam("deviceID", Utility.getMACAddress(this));
        task.addParam("deviceIP", Utility.getIPAddress(true));
        task.addParam("filterMode", WarehousingItemFilterMode.CountedItem.getCode());

        task.listener = this;
        task.execute();
        startWait();
    }
    WarehousingItemFilterMode tempFilterMode = WarehousingItemFilterMode.CountedItem;
    private void continueWarehousing(WarehousingItemFilterMode filterMode)
    {
        tempFilterMode = filterMode;
        warehousingTwoActivityMode = WarehousingTwoActivityMode.BeforeContinueWarehousing;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetOpenWarehousing, this);

        task.addParam("StoreID", GlobalData.getStoreID());
        task.addParam("deviceID", Utility.getMACAddress(this));
        task.addParam("filterMode", filterMode.getCode());

        task.listener = this;
        task.execute();
        startWait();
    }
    @Override
    public void onBackPressed()
    {
//        if(isOpenWarehousingExist)
//        {
            Intent returnIntent = new Intent();
//            returnIntent.putExtra("isOpenWarehousingExist", isOpenWarehousingExist);
            setResult(Activity.RESULT_OK, returnIntent);
//        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

        warehousingTwoActivityMode = WarehousingTwoActivityMode.BeforeGetProductInfo;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetWarehousingProductInfo, this);

        task.addParam("WarehousingHeaderCode", warehousingHeaderDetailWrapperData.getWarehousingHeaderList().get(0).getCode());
        task.addParam("barcode", barcode);

        task.listener = this;
        task.execute();

        startWait();
    }
    private void reset(boolean resetProductFragment)
    {
        tvProductCode.setText("");
        etAmount.setText("");
        Utility.clearGeneralSpinner(this, sUnit);
        numericUpDownFragment.setValue("0");
        if(resetProductFragment) {
            productCodeFragment.setProductCode("");
        }
        warehousingProductContainer = null;
    }

    @Override
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

          if(warehousingTwoActivityMode == WarehousingTwoActivityMode.BeforeStartWarehousing ||
                  warehousingTwoActivityMode == WarehousingTwoActivityMode.BeforeContinueWarehousing)
        {
            reset(true);
            if(taskResult == null) return;
            if(!taskResult.isSuccessful && taskResult.isExceptionOccured("Warehousing plan not defined"))
            {
                Utility.simpleAlert(this, "برنامه انبارگرداني تعريف نشده است.","", DialogIcon.Warning, onFinishClick);
                return;
            }
            else if(!taskResult.isSuccessful && !taskResult.message.equals( "No rows found!"))
            {
                Utility.simpleAlert(this, getString( R.string.error_in_fetching_data) + "\n" +
                        taskResult.message,"", DialogIcon.Error, onFinishClick);
                return;
            }
            else if(!taskResult.isSuccessful && taskResult.message.equals( "No rows found!"))
            {
                return;
            }
            warehousingHeaderDetailWrapperData = (WarehousingHeaderDetailWrapperData) taskResult.dataStructure;
            tvCountingStatusName.setText(warehousingHeaderDetailWrapperData.getWarehousingHeaderList().get(0).getCountingStatusName());
            tvCreateDate.setText(warehousingHeaderDetailWrapperData.getWarehousingHeaderList().get(0).getCreateDate().substring(0,16));
//
            row_warehousing adapter = new row_warehousing(this, warehousingHeaderDetailWrapperData.getWarehousingDetailList());
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            setFooter();
            Utility.hideKeyboard(this);

            filterMode = tempFilterMode;
            tvFilterMode.setText(filterMode.getName());
            warehousingTwoActivityMode = WarehousingTwoActivityMode.AfterContinueWarehousing;
        }
        else if(warehousingTwoActivityMode == WarehousingTwoActivityMode.BeforeGetProductInfo )
        {
            if (Utility.generalErrorOccurred(taskResult, this))
            {
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("product does not exist"))
            {
                Utility.simpleAlert(this,  "كالايي با اين مشخصات يافت نشد! اين موضوع مي تواند به دلايل زير باشد:" + "\n" +
                        "1. كد/باركد وارد شده اشتباه است." + "\n" +
                        "2. كالاي مورد نظر در اين برنامه انبارگرداني تعريف نشده است.", DialogIcon.Warning);
                return;
            }
            else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("unit list not is empty"))
            {
                Utility.simpleAlert(this,  "واحد كالا تعريف نشده است. با پشتيباني تماس بگيريد."
                        , DialogIcon.Warning);
                return;
            }
            else if (!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this,getString(R.string.general_error), DialogIcon.Error);
                return;
            }
            warehousingProductContainer = (WarehousingProductContainer) taskResult.dataStructure;

            tvProductCode.setText(String.valueOf( warehousingProductContainer.getWarehousingProductInfoData().getItemID()) + " - " +
                    warehousingProductContainer.getWarehousingProductInfoData().getItemName());

            List<WarehousingItemUMdata> unitDataList = warehousingProductContainer.getWarehousingItemUMdataList();

            row_unit_warehousing adapter = new row_unit_warehousing(this, unitDataList);
            sUnit.setAdapter(adapter);
            for(int i = 0; i < sUnit.getCount(); i++)
            {
                String unit = ((WarehousingItemUMdata)sUnit.getItemAtPosition(i)).getUMID();
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
            warehousingTwoActivityMode = WarehousingTwoActivityMode.AfterGetProductInfo;
        }
        else if(warehousingTwoActivityMode == WarehousingTwoActivityMode.BeforeInsertDetail)
        {
            if (!taskResult.isSuccessful)
            {
                if(taskResult.isExceptionOccured("this product already counted"))
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
            warehousingTwoActivityMode = WarehousingTwoActivityMode.AfterInsertDetail;
        }
        else if(warehousingTwoActivityMode == WarehousingTwoActivityMode.BeforeDelete)
        {
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, "اشكال در لغو شمارش.", DialogIcon.Error);
                return;
            }

            ArrayAdapter<WarehousingDetailData> adapter = (ArrayAdapter<WarehousingDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                WarehousingDetailData pData = adapter.getItem(i);
                pData.setCountingDone(false);
                pData.setCountValue(0);
                pData.setLocation(0);
                if (pData.getID() == warehousingDetailData.getID())
                {
//                    try
//                    {
//                        BeanUtils.copyProperties(pData, tempWarehousingDetailData);
//                    }
//                    catch(IllegalAccessException ex)
//                    {
//                        Utility.simpleAlert(this,getString(R.string.error_in_fetching_data));
//                    }
//                    catch (InvocationTargetException ex)
//                    {
//                        Utility.simpleAlert(this,getString(R.string.error_in_fetching_data));
//                    }
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.simpleAlert(this, "لغو شمارش انجام گرديد.", DialogIcon.Info);
            setFooter();
            warehousingTwoActivityMode = WarehousingTwoActivityMode.AfterDelete;
        }
        else if(warehousingTwoActivityMode == WarehousingTwoActivityMode.BeforeUpdate)
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

            ArrayAdapter<WarehousingDetailData> adapter = (ArrayAdapter<WarehousingDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                WarehousingDetailData pData = adapter.getItem(i);
                if (pData.getID() == tempWarehousingDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, tempWarehousingDetailData);
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
            warehousingTwoActivityMode = WarehousingTwoActivityMode.AfterUpdate;
        }
        else if(warehousingTwoActivityMode == WarehousingTwoActivityMode.BeforeSendDataToSAP)
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
//            isOpenWarehousingExist = false;
            Utility.simpleAlert(this, "ارسال اطلاعات به مركز انجام گرديد.","", DialogIcon.Info, onFinishClick);
            warehousingTwoActivityMode = WarehousingTwoActivityMode.AfterSendDataToSAP;
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
        ArrayAdapter<WarehousingDetailData> adapter = (ArrayAdapter<WarehousingDetailData>) listView.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++)
        {
            WarehousingDetailData iData = adapter.getItem(i);
            if (iData.getItemID() == Integer.valueOf( warehousingProductContainer.getWarehousingProductInfoData().getItemID()))
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("اين كالا قبلاً به مقدار " +
                        + iData.getCountValue() + " "
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

    WarehousingDetailData tempWarehousingDetailData;
    private void updateDuplicate(boolean addToOldValue, WarehousingDetailData iData)
    {
        int newCount = getPcAmount();
        if(addToOldValue)
        {
            int oldCount = iData.getCountValue();
            newCount += oldCount;
        }
        iData.setCountValue(newCount);
        iData.setLocation(Integer.valueOf( numericUpDownFragment.getValue()));
        tempWarehousingDetailData = iData;

        insertDetail(newCount, 0, WarehousingTwoActivityMode.BeforeUpdate);
    }
    private void insertToList(String tag)
    {
        WarehousingDetailData data = new WarehousingDetailData();
        data.setID(/*Integer.parseInt( tag)*/warehousingProductContainer.getWarehousingProductInfoData().getID());
        data.setItemID(Integer.valueOf( warehousingProductContainer.getWarehousingProductInfoData().getItemID()));
        data.setHeaderCode(warehousingHeaderDetailWrapperData.getWarehousingHeaderList().get(0).getCode());
        data.setItemName(warehousingProductContainer.getWarehousingProductInfoData().getItemName());
        data.setCountValue(getPcAmount());
        data.setLocation( Integer.parseInt( numericUpDownFragment.getValue()));
//        data.setHandheldIP(Utility.getIPAddress(true));
//        data.setHandheldID(Utility.getMACAddress(null));
        data.setCreateDate(Utility.getCurrentPersianDate());
        data.setCountingDone(true);
        data.setPartUnit(warehousingProductContainer.getPartUnit() != null ? warehousingProductContainer.getPartUnit().getUMID() : "");
        data.setWholeUnit(warehousingProductContainer.getWholeUnit()!= null ? warehousingProductContainer.getWholeUnit().getUMID() : "");
        data.setMultipleConvert(warehousingProductContainer.getWholeUnit()!= null ? warehousingProductContainer.getWholeUnit().getMultipleConvert() : 0);

        row_warehousing adapter = (row_warehousing)listView.getAdapter();
        if(adapter == null)
        {
            adapter = new row_warehousing(this, new ArrayList<>());
            listView.setAdapter(adapter);
        }
        adapter.add(data);
        adapter.notifyDataSetChanged();
        Utility.setListCount(adapter.getCount(), tvCount);
    }
    private void insertDetail(int amount, int CheckIfAlreadyCounted, WarehousingTwoActivityMode mode)
    {
        warehousingTwoActivityMode = mode;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateWarehousingItem,this);
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("ID");
        pi.setValue(warehousingProductContainer.getWarehousingProductInfoData().getID());
        service.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("CountValue");
        pi.setValue(amount);
        service.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("Location");
        pi.setValue(numericUpDownFragment.getValue());
        service.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("CheckIfAlreadyCounted");
        pi.setValue(CheckIfAlreadyCounted);
        service.piList.add(pi);

        service.listener = this;
        service.execute();
        startWait();
    }
    public void onInsertClick(View view)
    {
        if (!validateData()) return;
        insertDetail(getPcAmount(), 1, WarehousingTwoActivityMode.BeforeInsertDetail);
    }
    private int getPcAmount()
    {
        int count = Integer.valueOf( etAmount.getText().toString());
        int boxUnit = Integer.valueOf (((WarehousingItemUMdata)sUnit.getSelectedItem()).getMultipleConvert());
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
        if(warehousingProductContainer == null)
        {
            Utility.simpleAlert(this,"ابتدا كالا را انتخاب نماييد.", DialogIcon.Warning);
            return false;
        }
        if(numericUpDownFragment.isEmpty())
        {
            Toast.makeText(this, "موقعيت كالا را انتخاب نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Utility.editTextIsEmpty(etAmount, "تعداد كالا را وارد نماييد")) return false;

        WarehousingItemUMdata warehousingItemUMdata = (WarehousingItemUMdata)sUnit.getSelectedItem();
        if(warehousingItemUMdata == null || warehousingItemUMdata.getUMID().equals(""))
        {
            Toast.makeText(this, "واحد كالا را انتخاب نماييد", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void Delete()
    {
        if (warehousingDetailData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا اطلاعات مربوط به شمارش كالاي زير حذف گردد؟" + "\n"
                + warehousingDetailData.getItemID() + "\n"
                + warehousingDetailData.getItemName());

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        WarehousingTwoActivity.this.warehousingTwoActivityMode
                                = WarehousingTwoActivityMode.BeforeDelete;

                        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.DeleteWarehousingItem
                                , WarehousingTwoActivity.this);

                        task.addParam("ID", warehousingDetailData.getID());

                        task.listener = WarehousingTwoActivity.this;
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
            warehousingDetailData = (WarehousingDetailData)data.getSerializableExtra("warehousingDetailData");

            ArrayAdapter<WarehousingDetailData> adapter = (ArrayAdapter<WarehousingDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                WarehousingDetailData pData = adapter.getItem(i);
                if (pData.getID() == warehousingDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, warehousingDetailData);
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
        else if(requestCode == 2 && resultCode == Activity.RESULT_OK)
        {
            WarehousingItemFilterMode filterMode = (WarehousingItemFilterMode)data.getSerializableExtra("filterMode");
            continueWarehousing(filterMode);
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
    public void OnValueChanged(String value) {

    }

    @Override
    public void OnWarehousingCommand(WarehousingDetailData warehousingDetailData, int position, row_warehousing.WarehousingCommandType commandType)
    {
        this.warehousingDetailData = warehousingDetailData;
        ((row_warehousing)listView.getAdapter()).setSelection(position);
        if(commandType == row_warehousing.WarehousingCommandType.Delete)
        {
            Delete();
        }
        else if(commandType == row_warehousing.WarehousingCommandType.Edit)
        {
            Intent intent = new Intent(this, EditWarehousingItemActivity.class);
            intent.putExtra("warehousingDetailData", warehousingDetailData);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }
}
