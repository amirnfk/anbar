//package com.oshanak.mobilemarket.Activity.Activity.Enum;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
//import com.oshanak.mobilemarket.Activity.Activity.EditPickingItemActivity;
//import com.oshanak.mobilemarket.Activity.PickingApp.PickingEnterPalletActivity;
//import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
//import com.oshanak.mobilemarket.Activity.Common.ExpandCollapseAnim;
//import com.oshanak.mobilemarket.Activity.Common.Utility;
//import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
//import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverHeaderData;
//import com.oshanak.mobilemarket.Activity.DataStructure.PickingDeliverItemData;
//import com.oshanak.mobilemarket.Activity.DataStructure.SapPickingResult;
//import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
//import com.oshanak.mobilemarket.Activity.Enum.PickingItemStatus;
//import com.oshanak.mobilemarket.Activity.Enum.PickingLineItemFilter;
//import com.oshanak.mobilemarket.Activity.Enum.PickingOrderStatus;
//import com.oshanak.mobilemarket.Activity.PickingApp.row_picking_item;
//import com.oshanak.mobilemarket.Activity.Service.Enum.PickingDeliverServiceMode;
//import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
//import com.oshanak.mobilemarket.Activity.Service.PickingDeliverService;
//import com.oshanak.mobilemarket.Activity.Service.TaskResult;
//import com.oshanak.mobilemarket.R;
//
//import org.ksoap2.serialization.PropertyInfo;
//
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//
//import javadz.beanutils.BeanUtils;
//
//public class PickingItemListActivity_Cold_Chain extends BaseActivity
//        implements OnTaskCompleted, row_picking_item.OnPickingItemListCommandListener {
//    public static ArrayList<PickingDeliverItemData> _list=new ArrayList<>();
//    private TextView tvDocNo;
//    private TextView tvStoreId;
//    private TextView tvDate;
//    private TextView tvStatusName;
//    private ListView listView;
//    private TextView tvCount;
//    private Spinner sItemFilter;
//    private EditText etCode;
//    private PickingDeliverHeaderData pickingDeliverHeaderData;
//    private PickingDeliverItemData pickingDeliverItemData;
//    private PickingDeliverItemListActivityMode pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.Unknown;
//    private boolean headerUpdated = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_picking_item_list_cold_chain);
//        ////////////////////
//        if (Utility.restartAppIfNeed(this)) return;
//
//
//        tvDocNo = findViewById(R.id.tvDocNo);
//        tvStoreId = findViewById(R.id.tvStoreId);
//        tvDate = findViewById(R.id.tvDate);
//        tvStatusName = findViewById(R.id.tvStatusName);
//        listView = findViewById(R.id.recyclerview);
//        tvCount = findViewById(R.id.tvCount);
//        sItemFilter = findViewById(R.id.sItemFilter);
//        etCode = findViewById(R.id.etCode);
//
//        Intent intent = getIntent();
//        pickingDeliverHeaderData = (PickingDeliverHeaderData) intent.getSerializableExtra("pickingDeliverHeaderData");
//
//        tvDocNo.setText(pickingDeliverHeaderData.getVBELN());
//
////        tvStoreId.setText(String.valueOf( pickingDeliverHeaderData.getKUNNR()));
//        tvStoreId.setText("*زنجیره سرد*");
//
//        tvDate.setText(pickingDeliverHeaderData.getBLDAT());
//        tvStatusName.setText(pickingDeliverHeaderData.getStatusName());
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                pickingDeliverItemData = (PickingDeliverItemData) parent.getItemAtPosition(position);
//                ((row_picking_item) parent.getAdapter()).setSelection(position);
//            }
//        });
//        //region Collapse Params
//        final LinearLayout lParamValues = findViewById(R.id.lParamValues);
//        ViewTreeObserver vto = lParamValues.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ViewTreeObserver obs = lParamValues.getViewTreeObserver();
//
//                new ExpandCollapseAnim((ImageButton) findViewById(R.id.ibMinimize)
//                        , (TextView) findViewById(R.id.tvMoreParam)
//                        , (LinearLayout) findViewById(R.id.lParamValues)
//                        , true);
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    obs.removeOnGlobalLayoutListener(this);
//                } else {
//                    obs.removeGlobalOnLayoutListener(this);
//                }
//            }
//        });
//        //endregion Collapse Params
//
//        sItemFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                reset();
//                getList();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        Utility.initPickingLineItemFilterSpinner(this, sItemFilter);
//    }
//
//    MenuItem mnuAction;
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.picking_item_list_menu_cold_chain, menu);
//
//        super.onCreateOptionsMenu(menu);
//        //////////
//        mnuAction = menu.findItem(R.id.mnuAction);
//
//        if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InPacking.getCode()) {
//            mnuAction.setTitle("خاتمه جمع آوري");
//        } else if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.PickedUp.getCode()
//              ) {
//            mnuAction.setVisible(false);
//        }
//        //////////
//
//
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.mnuAction:
//                updateHeaderStatus(item);
//                return true;
//            case R.id.mnuRefresh:
//                getList();
//                return true;
//            case R.id.mnuExit:
//                onBackPressed();
//                return true;
//            case R.id.mnuBarcode:
//                IntentIntegrator integrator = new IntentIntegrator(this);
//                integrator.setPrompt("باركد كالا را دقيقاً داخل كادر قرار دهيد");
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.initiateScan();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
////        if(!isStarted)
////        {
////            isStarted = true;
////            Utility.setFontBold(tvStore);
////            Utility.increaseTextSize(tvStore,20);
////        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (headerUpdated) {
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra("pickingDeliverHeaderData", pickingDeliverHeaderData);
//            setResult(Activity.RESULT_OK, returnIntent);
//
//
//        }
//        super.onBackPressed();
//        Utility.hideKeyboard(this);
////        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//    }
//
//    private void reset() {
//        tvCount.setText("");
//        listView.setAdapter(null);
//        pickingDeliverItemData = null;
//    }
//
//    private boolean validateChangeHeaderStatus() {
//        if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InPacking.getCode()) {
//            ArrayAdapter<PickingDeliverItemData> adapter = (ArrayAdapter<PickingDeliverItemData>) listView.getAdapter();
//            for (int i = 0; i < adapter.getCount(); i++) {
//                PickingDeliverItemData pData = adapter.getItem(i);
//                if (pData.getStatusID() == PickingItemStatus.Unknown.getCode()) {
//                    Utility.simpleAlert(this, "قبل از خاتمه جمع آوري بايد كليه اقلام سفارش را تعيين تكليف نماييد.", DialogIcon.Warning);
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    private void updateHeaderStatus(MenuItem item) {
//        if (!validateChangeHeaderStatus()) return;
//        PickingOrderStatus status = PickingOrderStatus.Unknown;
//
//        if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.Ready.getCode()) {
//            status = PickingOrderStatus.InPacking;
//        } else if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InPacking.getCode()) {
//            status = PickingOrderStatus.PickedUp;
//        }
//
//        if (status == PickingOrderStatus.PickedUp) {
//            Intent intent = new Intent(this, PickingEnterPalletActivity.class);
//            startActivityForResult(intent, 2);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            return;
//        }
//
//        updateHeader(status, 0);
//    }
//
//    private void updateHeader(PickingOrderStatus status, int palletCount) {
//        pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.BeforeUpdateHeaderStatus;
//        PickingDeliverService task = new PickingDeliverService(PickingDeliverServiceMode.UpdatePickingHeaderStatus, this);
//        PropertyInfo pi;
//
//        pi = new PropertyInfo();
//        pi.setName("HeaderID");
//        pi.setValue(pickingDeliverHeaderData.getID());
//        task.piList.add(pi);
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
//    }
//
//    private void getList() {
//        reset();
//
//        pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.BeforeGetList;
//        PickingDeliverService task = new PickingDeliverService(PickingDeliverServiceMode.GetPickingDeliverItem, this);
//        PropertyInfo pi;
//
//        pi = new PropertyInfo();
//        pi.setName("HeaderID");
//        pi.setValue(pickingDeliverHeaderData.getID());
//        task.piList.add(pi);
//
//        pi = new PropertyInfo();
//        pi.setName("itemFilter");
//        pi.setValue(((PickingLineItemFilter) sItemFilter.getSelectedItem()).getCode());
//        task.piList.add(pi);
//
//        task.listener = this;
//        task.execute();
//        startWait();
//    }
//
//
//    @Override
//    public void onTaskCompleted(Object result) {
//        stopWait();
//        TaskResult taskResult = (TaskResult) result;
//
//        if (Utility.generalErrorOccurred(taskResult, this)) {
//            return;
//        } else if (pickingDeliverItemListActivityMode == PickingDeliverItemListActivityMode.BeforeGetList) {
//            reset();
//            if (taskResult == null) return;
//
//            if (!taskResult.isSuccessful && !taskResult.message.equals("No rows found!")) {
//                Utility.simpleAlert(this, getString(R.string.error_in_fetching_data), DialogIcon.Error);
//                return;
//            } else if (!taskResult.isSuccessful && taskResult.message.equals("No rows found!")) {
//                return;
//            }
//            ArrayList<PickingDeliverItemData> list = (ArrayList<PickingDeliverItemData>) taskResult.dataStructure;
//            _list=list;
//            row_picking_item adapter = new row_picking_item(this, list );
//            listView.setAdapter(adapter);
//            Utility.setListCount(adapter.getCount(), tvCount);
//            Utility.hideKeyboard(this);
//            pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterGetList;
////            if(adapter.getCount() >= 200)
////            {
////                Toast.makeText(this,
////                        getString( R.string.only_200_rows)
////                        ,Toast.LENGTH_LONG).show();
////            }
//        } else if (pickingDeliverItemListActivityMode == PickingDeliverItemListActivityMode.BeforeUpdateHeaderStatus) {
//            SapPickingResult sapPickingResult = (SapPickingResult) taskResult.dataStructure;
//            if (!taskResult.isSuccessful && sapPickingResult.isItemNotExistInSAP()) {
//
//                Utility.simpleAlert(this, "اقلامي در سفارش موجود است كه در سفارش معادل آن در سپ وجود ندارد." + "\n" +
//                        "جهت اطلاعات بيشتر لازم است سفارش در سپ بررسي شود." + "\n" + taskResult.message, DialogIcon.Error);
//                return;
//            }
//            else if (!taskResult.isSuccessful && sapPickingResult.isItemInventoryAmountNotEnough()) {
//
//                disableAllItemsExceptOneWithError(((SapPickingResult) taskResult.dataStructure).getItemID(),((SapPickingResult) taskResult.dataStructure).getItemMinInventory());
//
//
//
//                Utility.simpleAlert(this,
//                          taskResult.message, DialogIcon.Error);
//                return;
//            }else if (!taskResult.isSuccessful && taskResult.isExceptionOccured("error in sending picking list to SAP")) {
//                Utility.simpleAlert(this, "خطا هنگام ارسال اطلاعات به سپ." + "\n" + taskResult.message
//                        , DialogIcon.Error);
//                return;
//            } else if (!taskResult.isSuccessful) {
//
//                Utility.simpleAlert(this, getString(R.string.general_error), DialogIcon.Error);
//                return;
//            }
//
//            String message = "";
//            if (sapPickingResult.isItemRejectedByUser()) {
//                message += "سفارش با شرايط زير به سپ منتقل گرديد:" + "\n\n";
//                message += "1. تعدادي از اقلام سفارش توسط كاربر (شما) از سفارش حذف گرديد.";
//            }
//            if (sapPickingResult.isItemDeletedDeficit()) {
//                message += "\n" + "2. تعدادي از اقلام سفارش به دليل ناكافي بودن موجودي از سفارش حذف گرديد.";
//            }
//
//            PickingOrderStatus status = PickingOrderStatus.Unknown;
//            if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.Ready.getCode()) {
//                status = PickingOrderStatus.InPacking;
//                mnuAction.setTitle("خاتمه جمع آوري");
//            } else if (pickingDeliverHeaderData.getStatusID() == PickingOrderStatus.InPacking.getCode()) {
//                status = PickingOrderStatus.PickedUp;
//                mnuAction.setVisible(false);
//            }
//            pickingDeliverHeaderData.setStatusID(status.getCode());
//            pickingDeliverHeaderData.setStatusName(status.getDescription());
//            tvStatusName.setText(pickingDeliverHeaderData.getStatusName());
//            headerUpdated = true;
//
//            if (message.equals("")) {
//                Toast.makeText(this, "وضعيت سفارش تغيير يافت", Toast.LENGTH_SHORT).show();
//            } else {
//                Utility.simpleAlert(this, message, DialogIcon.Info);
//            }
//            pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterUpdateHeaderStatus;
//        } else if (pickingDeliverItemListActivityMode == PickingDeliverItemListActivityMode.BeforeUpdateItemStatus) {
//            if (!taskResult.isSuccessful) {
//                Utility.simpleAlert(this, getString(R.string.general_error), DialogIcon.Error);
//                return;
//            }
//            int OrgDeliverAmount = Integer.parseInt(taskResult.tag.toString());
//            setItemStatus(OrgDeliverAmount);
//            Toast.makeText(this, "وضعيت تغيير يافت", Toast.LENGTH_SHORT).show();
//            pickingDeliverItemListActivityMode = PickingDeliverItemListActivityMode.AfterUpdateItemStatus;
//        }
//    }
//
//    private void setItemStatus(int OrgDeliverAmount) {
//        ArrayAdapter<PickingDeliverItemData> adapter = (ArrayAdapter<PickingDeliverItemData>) listView.getAdapter();
//        for (int i = 0; i < adapter.getCount(); i++) {
//            PickingDeliverItemData pData = adapter.getItem(i);
//            if (pData.getID() == pickingDeliverItemData.getID()) {
//                pData.setStatusID(newStatus.getCode());
//                pData.setStatusName(newStatus.getDescription());
//                if (newStatus == PickingItemStatus.Unknown && OrgDeliverAmount > 0) {
//                    pData.setDeliverAmount(OrgDeliverAmount);
//                    pickingDeliverItemData.setDeliverAmount(OrgDeliverAmount);
//                }
//                if (newStatus.getCode() == PickingItemStatus.Reject.getCode()) {
//                    pData.setDeliverAmount(0);
//                    pickingDeliverItemData.setDeliverAmount(0);
//                }
//                adapter.notifyDataSetChanged();
//                break;
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
//            pickingDeliverItemData = (PickingDeliverItemData) data.getSerializableExtra("pickingDeliverItemData");
//
//            ArrayAdapter<PickingDeliverItemData> adapter = (ArrayAdapter<PickingDeliverItemData>) listView.getAdapter();
//            for (int i = 0; i < adapter.getCount(); i++) {
//                PickingDeliverItemData pData = adapter.getItem(i);
//                if (pData.getID() == pickingDeliverItemData.getID()) {
//                    try {
//                        BeanUtils.copyProperties(pData, pickingDeliverItemData);
//                    } catch (IllegalAccessException ex) {
//                        Utility.simpleAlert(this, getString(R.string.error_in_fetching_data));
//                    } catch (InvocationTargetException ex) {
//                        Utility.simpleAlert(this, getString(R.string.error_in_fetching_data));
//                    }
//                    adapter.notifyDataSetChanged();
//                    break;
//                }
//            }
//        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
//            int palletCount = data.getIntExtra("palletCount", 0);
//            updateHeader(PickingOrderStatus.PickedUp, palletCount);
//        } else if (requestCode == GlobalData.getBarcodeActivityRequestCode()) {
//            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//            if (scanningResult == null || scanningResult.getContents() == null) {
//                Toast.makeText(this, "بارکد کالا به درستی اسکن نشد.", Toast.LENGTH_SHORT).show();
//
//            } else {
//                String scanContent = scanningResult.getContents();
//                moveToPickingItem(scanContent, 1);
//            }
//        }
//    }
//
//    private void updateItemStatus() {
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
//    }
//
//    private boolean editItemValidation() {
//        if (pickingDeliverHeaderData.getStatusID() != PickingOrderStatus.InPacking.getCode()) {
//            Utility.simpleAlert(this, "امكان اصلاح فقط درخصوص سفارشات با وضعيت در حال جمع آوري ميسر مي باشد.", DialogIcon.Info);
//            return false;
//        }
//        return true;
//    }
//
//    PickingItemStatus newStatus = PickingItemStatus.Unknown;
//
//    @Override
//    public void OnPickingItemListCommand(PickingDeliverItemData pickingDeliverItemData, PickingItemStatus newStatus, int position, row_picking_item.PickingItemListCommandType commandType, int minimumInventory,String orderType) {
//
//        this.pickingDeliverItemData = pickingDeliverItemData;
//        this.newStatus = newStatus;
//
//        ((row_picking_item) listView.getAdapter()).setSelection(position);
//
//        if (commandType == row_picking_item.PickingItemListCommandType.Edit) {
//            if (!editItemValidation()) return;
//
//            Intent intent = new Intent(this, EditPickingItemActivity.class);
//            intent.putExtra("pickingDeliverItemData", pickingDeliverItemData);
//            intent.putExtra("minimumInventory", minimumInventory);
//            intent.putExtra("orderType", orderType);
//
//            startActivityForResult(intent, 1);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        } else if (commandType == row_picking_item.PickingItemListCommandType.Confirm) {
//            updateItemStatus();
//        } else if (commandType == row_picking_item.PickingItemListCommandType.Reject) {
//            updateItemStatus();
//        } else if (commandType == row_picking_item.PickingItemListCommandType.ShowInventory) {
//            if (!Utility.isPowerUser()) return;
//            String message = "موجودي(واحد جزء): " + pickingDeliverItemData.getLBKUM() + "\n" +
//                    "موجودي(واحد کل): " + pickingDeliverItemData.getLBKUM() / pickingDeliverItemData.getUMVKZ() + "\n" +
//                    "واحد جزء در كل: " + pickingDeliverItemData.getUMVKZ();
//            Utility.simpleAlert(this, message);
//        }
//    }
//
//    public void onSearch(View view) {
//        moveToPickingItem(etCode.getText().toString().trim(), 2);
//    }
//
//    private void moveToPickingItem(String code, int mode) {
//        Utility.hideKeyboard(this);
//        if (!code.equals("")) {
//            ArrayAdapter<PickingDeliverItemData> adapter = (ArrayAdapter<PickingDeliverItemData>) listView.getAdapter();
//            for (int i = 0; i < adapter.getCount(); i++) {
//                PickingDeliverItemData pData = adapter.getItem(i);
//                if (pData.getBARCODE().equals(code) || pData.getMATNR().equals(code)) {
//                    listView.setSelection(i);
//                    pickingDeliverItemData = pData;
//                    ((row_picking_item) adapter).setSelection(i);
//                    if (mode == 1) {
//                        listView.smoothScrollToPositionFromTop(i, 0, 500);
//                    } else if (mode == 2) {
//                        listView.smoothScrollToPosition(i);
//                    }
//                      return;
////                        listView.smoothScrollToPosition(i);
////                        listView.setSelection(i);
////                        listView.smoothScrollToPositionFromTop(i, 0, 1000);
//                }
//            }
//            Utility.simpleAlert(this, "باركد/کد كالا پيدا نشد. اين موضوع مي تواند به دلايل زير باشد:" + "\n" +
//                    "1. كالاي مورد نظر در ليست جمع آوري موجود نيست." + "\n" +
//                    "2. باركد/کد صحيح كالا را اسكن/وارد نكرده ايد." + "\n" +
//                    "3. باركد/کد كالا در سيستم تعريف نشده است.", DialogIcon.Warning);
//        }
//    }
//
//
//    private  void disableAllItemsExceptOneWithError(String code, int itemMinInventory) {
//        row_picking_item adapter = new row_picking_item(this, _list, pickingDeliverHeaderData.getOrderType(),code,itemMinInventory);
//        listView.setAdapter(adapter);
//        code=Integer.parseInt(code)+"";
//
//        Utility.hideKeyboard(this);
//        if (!code.equals("")) {
//
//            for (int i = 0; i < adapter.getCount(); i++) {
//                PickingDeliverItemData pData = adapter.getItem(i);
//                if (pData.getBARCODE().equals(code) || pData.getMATNR().equals(code)) {
//                    listView.setSelection(i);
//                    pickingDeliverItemData = pData;
//                    ((row_picking_item) adapter).setSelection(i);
//
//
//                       listView.smoothScrollToPositionFromTop(i, 0, 500);
//
//
//                       listView.smoothScrollToPosition(i);
//
//                    return;
////                        listView.smoothScrollToPosition(i);
////                        listView.setSelection(i);
////                        listView.smoothScrollToPositionFromTop(i, 0, 1000);
//                }
//            }
//            Utility.simpleAlert(this, "باركد/کد كالا پيدا نشد. اين موضوع مي تواند به دلايل زير باشد:" + "\n" +
//                    "1. كالاي مورد نظر در ليست جمع آوري موجود نيست." + "\n" +
//                    "2. باركد/کد صحيح كالا را اسكن/وارد نكرده ايد." + "\n" +
//                    "3. باركد/کد كالا در سيستم تعريف نشده است.", DialogIcon.Warning);
//        }
//        Utility.hideKeyboard(this);
//
//
//
//            Toast.makeText(this, "item"+Integer.parseInt(code)+" error", Toast.LENGTH_SHORT).show();
//        }
//
//
//}