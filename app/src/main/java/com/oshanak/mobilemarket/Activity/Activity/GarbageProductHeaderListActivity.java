package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Activity.Enum.GarbageProductHeaderListActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.GarbageProductHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_garbage_product_header;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;

public class GarbageProductHeaderListActivity extends BaseActivity
        implements OnTaskCompleted
                    ,row_garbage_product_header.OnGarbageProductHeaderListCommandListener
{
    private TextView tvCount;
    private ListView listView;
    private GarbageProductHeaderListActivityMode garbageProductHeaderListActivityMode = GarbageProductHeaderListActivityMode.Unknown;
    private GarbageProductHeaderData garbageProductHeaderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage_product_header_list);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                garbageProductHeaderData = (GarbageProductHeaderData) parent.getItemAtPosition(position);
                ((row_garbage_product_header)parent.getAdapter()).setSelection(position);
            }
        });
        getList();
    }
    public void onNewOperation(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, DefineGarbageProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(intent,1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    private void getList()
    {
        reset();

        garbageProductHeaderListActivityMode = GarbageProductHeaderListActivityMode.BeforeGetList;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetGarbageProductHeader, this);

        task.addParam("StoreId", GlobalData.getStoreID());

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
        else if(garbageProductHeaderListActivityMode == GarbageProductHeaderListActivityMode.BeforeGetList)
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
            ArrayList<GarbageProductHeaderData> list = (ArrayList<GarbageProductHeaderData>) taskResult.dataStructure;

            row_garbage_product_header adapter = new row_garbage_product_header(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            garbageProductHeaderListActivityMode = GarbageProductHeaderListActivityMode.AfterGetList;
        }
        else if(garbageProductHeaderListActivityMode == GarbageProductHeaderListActivityMode.BeforeDelete)
        {
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
                return;
            }

            ArrayAdapter<GarbageProductHeaderData> adapter = (ArrayAdapter<GarbageProductHeaderData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                GarbageProductHeaderData pData = adapter.getItem(i);
                if (pData.getID() == garbageProductHeaderData.getID())
                {
                    adapter.remove(pData);
                    garbageProductHeaderData = null;
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
            garbageProductHeaderListActivityMode = GarbageProductHeaderListActivityMode.AfterDelete;
        }

    }
    private void reset()
    {
        tvCount.setText("");
        listView.setAdapter(null);
        garbageProductHeaderData = null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if(data == null) return;
        boolean refreshList = data.getBooleanExtra("refreshList", false);
        if((requestCode == 1 || requestCode == 2) &&
                resultCode == Activity.RESULT_OK && refreshList)
        {
            getList();
        }
    }
    private void Delete()
    {
        if (garbageProductHeaderData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا اطلاعات مربوط به سند ضايعات به شماره " +
                garbageProductHeaderData.getID() +
                " حذف گردد؟");

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        GarbageProductHeaderListActivity.this.garbageProductHeaderListActivityMode
                                = GarbageProductHeaderListActivityMode.BeforeDelete;

                        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.DeleteGarbageProductHeader
                                , GarbageProductHeaderListActivity.this);

                        task.addParam("headerID", garbageProductHeaderData.getID());

                        task.listener = GarbageProductHeaderListActivity.this;
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
    public void OnInboundHeaderListCommand(GarbageProductHeaderData garbageProductHeaderData
            , int position, row_garbage_product_header.GarbageProductHeaderCommandType commandType)
    {
        this.garbageProductHeaderData = garbageProductHeaderData;
        ((row_garbage_product_header)listView.getAdapter()).setSelection(position);
        if(commandType == row_garbage_product_header.GarbageProductHeaderCommandType.Delete)
        {
            if(Utility.isDoubleClick()) return;
            Delete();
        }
        else if(commandType == row_garbage_product_header.GarbageProductHeaderCommandType.Edit)
        {
            if(Utility.isDoubleClick()) return;
            Intent intent = new Intent(this, DefineGarbageProductActivity.class);
            intent.putExtra("garbageProductHeaderData", garbageProductHeaderData);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}