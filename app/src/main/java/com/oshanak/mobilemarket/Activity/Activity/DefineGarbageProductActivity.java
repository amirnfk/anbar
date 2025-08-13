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

import com.oshanak.mobilemarket.Activity.Activity.Enum.DefineGarbageProductActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ProductCodeFragment;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GarbageProductDetailData;
import com.oshanak.mobilemarket.Activity.DataStructure.GarbageProductHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductControlContainerData;
import com.oshanak.mobilemarket.Activity.DataStructure.ProductUnitData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_garbage_product_detail;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_unit;
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

public class DefineGarbageProductActivity extends BaseActivity implements
        OnTaskCompleted
        ,ProductCodeFragment.OnConfirmListener
        ,ProductCodeFragment.OnBarcodeScannedByCameraListener
        ,ProductCodeFragment.OnBarcodeChanged
        ,row_garbage_product_detail.OnGarbageProductDetailListCommandListener
{
    private GarbageProductHeaderData garbageProductHeaderData;
    private ProductControlContainerData productControlContainerData;
    private GarbageProductDetailData garbageProductDetailData;
    private TextView tvHeaderId;
    private TextView tvProductCode;
    private TextView tvBoxUnit;
    private Spinner sUnit;
    private EditText etAmount;
    private ListView listView;
    private TextView tvCount;
    private DefineGarbageProductActivityMode defineGarbageProductActivityMode;
    private ProductCodeFragment productCodeFragment;
    private boolean newDocumentInserted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_garbage_product);

        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        Intent intent = getIntent();
        garbageProductHeaderData = (GarbageProductHeaderData) intent.getSerializableExtra("garbageProductHeaderData");

        tvHeaderId = findViewById(R.id.tvHeaderId);
        tvProductCode = findViewById(R.id.tvProductCode);
        tvBoxUnit = findViewById(R.id.tvBoxUnit);
        sUnit = findViewById(R.id.sUnit);
        etAmount = findViewById(R.id.etAmount);
        listView = findViewById(R.id.recyclerview);
        tvCount = findViewById(R.id.tvCount);
        productCodeFragment = (ProductCodeFragment)getSupportFragmentManager().findFragmentById(R.id.frProductCode);

        if(garbageProductHeaderData != null)
        {
            tvHeaderId.setText(String.valueOf( garbageProductHeaderData.getID()));
        }
        Utility.clearGeneralSpinner(this, sUnit);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                garbageProductDetailData = (GarbageProductDetailData) parent.getItemAtPosition(position);
                ((row_garbage_product_detail)parent.getAdapter()).setSelection(position);
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
            productCodeFragment.setFont();
            productCodeFragment.setAutoScanVisible(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.define_garbage_product_menu, menu);
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
                        defineGarbageProductActivityMode = DefineGarbageProductActivityMode.BeforeSendGarbageDataToSAP;
                        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.SendGarbageProductToSAP
                                ,DefineGarbageProductActivity.this);

                        service.addParam("headerID", garbageProductHeaderData.getID());

                        service.listener = DefineGarbageProductActivity.this;
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
    private void getList()
    {
        if(garbageProductHeaderData == null) return;

        defineGarbageProductActivityMode = DefineGarbageProductActivityMode.BeforeGetDetailList;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetGarbageProductDetail, this);

        task.addParam("HeaderID", garbageProductHeaderData.getID());

        task.listener = this;
        task.execute();
        startWait();
    }
    @Override
    public void onBackPressed()
    {
        if(defineGarbageProductActivityMode == DefineGarbageProductActivityMode.AfterSendGarbageDataToSAP
                || newDocumentInserted)
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
        getProductInfo(barcode);
    }
    private void getProductInfo(String barcode)
    {
        reset(false);

        defineGarbageProductActivityMode = DefineGarbageProductActivityMode.BeforeGetProductInfo;
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
        tvBoxUnit.setText("");
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

        if(defineGarbageProductActivityMode == DefineGarbageProductActivityMode.BeforeGetProductInfo )
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
//            inboundSaleItemData = (InboundSaleItemData) taskResult.dataStructure;
            productControlContainerData = (ProductControlContainerData) taskResult.dataStructure;

            tvProductCode.setText(String.valueOf( productControlContainerData.getProductControlData().getProductCode()) + " - " +
                    productControlContainerData.getProductControlData().getProductName());

            List<ProductUnitData> unitDataList = productControlContainerData.getProductUnitDataList();
            int boxUnit = 1;
            boxUnit = (productControlContainerData.getWholeUnit() == null ? 1 :
                        Integer.parseInt( productControlContainerData.getWholeUnit().getAmount()));
//            for(int i = 0; i < unitDataList.size(); i++)
//            {
//                boxUnit = (Integer.parseInt( unitDataList.get(i).getAmount()) > boxUnit ?
//                        Integer.parseInt( unitDataList.get(i).getAmount()) : boxUnit);
//            }
            tvBoxUnit.setText(ThousandSeparatorWatcher.addSeparator( boxUnit));

            row_unit adapter = new row_unit(this, unitDataList);
            sUnit.setAdapter(adapter);
            Utility.hideKeyboard(this);
            Utility.playBeep();
            etAmount.requestFocus();
            Utility.showKeyboard(this);
            defineGarbageProductActivityMode = DefineGarbageProductActivityMode.AfterGetProductInfo;
        }
        else if(defineGarbageProductActivityMode == DefineGarbageProductActivityMode.BeforeInsertDetail)
        {
            if (!taskResult.isSuccessful)
            {
                if(taskResult.isExceptionOccured("IX_GarbageProductDetail_HeaderID_ItemID"))
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
            if(productCodeFragment.isAutoScanEnable()) productCodeFragment.startScan();
            defineGarbageProductActivityMode = DefineGarbageProductActivityMode.AfterInsertDetail;
        }
        else if(defineGarbageProductActivityMode == DefineGarbageProductActivityMode.BeforeGetDetailList)
        {
            reset(true);
            if(taskResult == null) return;

            if(!taskResult.isSuccessful && !taskResult.message.equals( "No rows found!"))
            {
//                Utility.simpleAlert(this, getString( R.string.error_in_fetching_data), DialogIcon.Error);
                return;
            }
            else if(!taskResult.isSuccessful && taskResult.message.equals( "No rows found!"))
            {
                return;
            }
            ArrayList<GarbageProductDetailData> list = (ArrayList<GarbageProductDetailData>) taskResult.dataStructure;

            row_garbage_product_detail adapter = new row_garbage_product_detail(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            defineGarbageProductActivityMode = DefineGarbageProductActivityMode.AfterGetDetailList;
//            if(adapter.getCount() >= 200)
//            {
//                Toast.makeText(this,
//                        getString( R.string.only_200_rows)
//                        ,Toast.LENGTH_LONG).show();
//            }
        }
        else if(defineGarbageProductActivityMode == DefineGarbageProductActivityMode.BeforeDelete)
        {
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
                return;
            }

            ArrayAdapter<GarbageProductDetailData> adapter = (ArrayAdapter<GarbageProductDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                GarbageProductDetailData pData = adapter.getItem(i);
                if (pData.getID() == garbageProductDetailData.getID())
                {
                    adapter.remove(pData);
                    garbageProductDetailData = null;
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
            defineGarbageProductActivityMode = DefineGarbageProductActivityMode.AfterDelete;
        }
        else if(defineGarbageProductActivityMode == DefineGarbageProductActivityMode.BeforeUpdate)
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

            ArrayAdapter<GarbageProductDetailData> adapter = (ArrayAdapter<GarbageProductDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                GarbageProductDetailData pData = adapter.getItem(i);
                if (pData.getID() == tempGarbageProductDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, tempGarbageProductDetailData);
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
            defineGarbageProductActivityMode = DefineGarbageProductActivityMode.AfterUpdate;

        }
        else if(defineGarbageProductActivityMode == DefineGarbageProductActivityMode.BeforeSendGarbageDataToSAP)
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
            defineGarbageProductActivityMode = DefineGarbageProductActivityMode.AfterSendGarbageDataToSAP;
            Utility.simpleAlert(this, "ارسال اطلاعات به مركز انجام گرديد." ,"", DialogIcon.Info, onFinishClick);
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
        ArrayAdapter<GarbageProductDetailData> adapter = (ArrayAdapter<GarbageProductDetailData>) listView.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++)
        {
            GarbageProductDetailData iData = adapter.getItem(i);
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

    GarbageProductDetailData tempGarbageProductDetailData;
    private void updateDuplicate(boolean addToOldValue, GarbageProductDetailData iData)
    {
        double newCount = getPcAmount();
        if(addToOldValue)
        {
            newCount += iData.getAmount();
        }
        defineGarbageProductActivityMode = DefineGarbageProductActivityMode.BeforeUpdate;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.UpdateGarbageProductDetail,this);

        service.addParam("detailID", iData.getID());
        service.addParam("amount", newCount);

        iData.setAmount(newCount);
        tempGarbageProductDetailData = iData;

        service.listener = this;
        service.execute();
        startWait();
    }
    private void insertToList(String tag)
    {
        int headerID = Integer.parseInt( tag.split(",")[0]);
        int detailID = Integer.parseInt( tag.split(",")[1]);

        if(garbageProductHeaderData == null)
        {
            garbageProductHeaderData = new GarbageProductHeaderData();
            garbageProductHeaderData.setID(headerID);
            tvHeaderId.setText(ThousandSeparatorWatcher.addSeparator( headerID));
            newDocumentInserted = true;
        }

        GarbageProductDetailData data = new GarbageProductDetailData();
        data.setID(detailID);
        data.setHeaderID(headerID);
        data.setItemID(Integer.valueOf( productControlContainerData.getProductControlData().getProductCode()));
        data.setAmount(getPcAmount());
        data.setItemName(productControlContainerData.getProductControlData().getProductName());
        data.setPartUnit(productControlContainerData.getPartUnit().getUnitName());
        data.setWholeUnit(productControlContainerData.getWholeUnit().getUnitName());
        data.setBoxQuantity(Integer.valueOf( productControlContainerData.getWholeUnit().getAmount()));

        row_garbage_product_detail adapter = (row_garbage_product_detail)listView.getAdapter();
        if(adapter == null)
        {
            adapter = new row_garbage_product_detail(this, new ArrayList<>());
            listView.setAdapter(adapter);
        }
        adapter.add(data);
        adapter.notifyDataSetChanged();
        Utility.setListCount(adapter.getCount(), tvCount);

    }
    public void onInsertClick(View view)
    {
        if (!validateData()) return;

        defineGarbageProductActivityMode = DefineGarbageProductActivityMode.BeforeInsertDetail;
        StoreHandheldService service = new StoreHandheldService(StoreHandheldServiceMode.InsertGarbageProductDetail,this);

        service.addParam("HeaderID", garbageProductHeaderData == null? 0 : garbageProductHeaderData.getID());
        service.addParam("StoreID", GlobalData.getStoreID());
        service.addParam("ItemID", productControlContainerData.getProductControlData().getProductCode());
        service.addParam("ItemName", productControlContainerData.getProductControlData().getProductName());
        double amount = getPcAmount();
        service.addParam("Amount", amount);
        service.addParam("PartUnit", productControlContainerData.getPartUnit().getUnitName());
        service.addParam("WholeUnit", productControlContainerData.getWholeUnit().getUnitName());
        service.addParam("BoxQuantity", productControlContainerData.getWholeUnit().getAmount());

        service.listener = this;
        service.execute();
        startWait();
    }
    private double getPcAmount()
    {
        double count = Double.valueOf( etAmount.getText().toString());
        int boxUnit = Integer.valueOf (((ProductUnitData)sUnit.getSelectedItem()).getAmount());
        double amount = count * boxUnit;
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
        if (garbageProductDetailData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا اطلاعات مربوط به كالاي زير حذف گردد؟" + "\n"
                + garbageProductDetailData.getItemID() + "\n"
                + garbageProductDetailData.getItemName());

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DefineGarbageProductActivity.this.defineGarbageProductActivityMode
                                = DefineGarbageProductActivityMode.BeforeDelete;

                        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.DeleteGarbageProductDetail
                                , DefineGarbageProductActivity.this);

                        task.addParam("garbageProductDetailID", garbageProductDetailData.getID());

                        task.listener = DefineGarbageProductActivity.this;
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
            garbageProductDetailData = (GarbageProductDetailData) data.getSerializableExtra("garbageProductDetailData");

            ArrayAdapter<GarbageProductDetailData> adapter = (ArrayAdapter<GarbageProductDetailData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                GarbageProductDetailData pData = adapter.getItem(i);
                if (pData.getID() == garbageProductDetailData.getID())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, garbageProductDetailData);
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
    public void OnInboundDetailListCommand(GarbageProductDetailData garbageProductDetailData
            , int position, row_garbage_product_detail.GarbageProductDetailCommandType commandType)
    {
        this.garbageProductDetailData = garbageProductDetailData;
        ((row_garbage_product_detail)listView.getAdapter()).setSelection(position);
        if(commandType == row_garbage_product_detail.GarbageProductDetailCommandType.Delete)
        {
            Delete();
        }
        else if(commandType == row_garbage_product_detail.GarbageProductDetailCommandType.Edit)
        {
            Intent intent = new Intent(this, EditGarbageProductDetailActivity.class);
            intent.putExtra("garbageProductDetailData", garbageProductDetailData);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}