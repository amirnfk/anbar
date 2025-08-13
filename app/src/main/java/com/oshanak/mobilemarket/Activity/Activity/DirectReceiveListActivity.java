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

import com.oshanak.mobilemarket.Activity.Activity.Enum.DirectReceiveListActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.DirectReceiveHeaderData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_direct_receive_header;
import com.oshanak.mobilemarket.Activity.Service.Enum.StoreHandheldServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.StoreHandheldService;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;

public class DirectReceiveListActivity extends BaseActivity
        implements OnTaskCompleted, row_direct_receive_header.OnDirectReceiveHeaderListCommandListener
{
    private TextView tvCount;
    private ListView listView;
    private DirectReceiveListActivityMode directReceiveListActivityMode = DirectReceiveListActivityMode.Unknown;
    private DirectReceiveHeaderData directReceiveHeaderData;
    private boolean isNewOperation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_receive_list);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                directReceiveHeaderData = (DirectReceiveHeaderData) parent.getItemAtPosition(position);
                ((row_direct_receive_header)parent.getAdapter()).setSelection(position);
            }
        });
    }
    public void onContinueLastOperation(View view)
    {
        getDirectReceiveHeader();
    }
    public void onNewOperation(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, SelectVendorByProductActivity.class);
        startActivityForResult(intent,1);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void getDirectReceiveHeader()
    {
        reset();

        directReceiveListActivityMode = DirectReceiveListActivityMode.BeforeGetList;
        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.GetDirectReceiveHeader, this);

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
        else if(directReceiveListActivityMode == DirectReceiveListActivityMode.BeforeGetList)
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
            ArrayList<DirectReceiveHeaderData> list = (ArrayList<DirectReceiveHeaderData>) taskResult.dataStructure;

            row_direct_receive_header adapter = new row_direct_receive_header(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            directReceiveListActivityMode = DirectReceiveListActivityMode.AfterGetList;
        }
        else if(directReceiveListActivityMode == DirectReceiveListActivityMode.BeforeDelete)
        {
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
                return;
            }

            ArrayAdapter<DirectReceiveHeaderData> adapter = (ArrayAdapter<DirectReceiveHeaderData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                DirectReceiveHeaderData pData = adapter.getItem(i);
                if (pData.getID() == directReceiveHeaderData.getID())
                {
                    adapter.remove(pData);
                    directReceiveHeaderData = null;
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
            directReceiveListActivityMode = DirectReceiveListActivityMode.AfterDelete;
        }
    }
    private void reset()
    {
        tvCount.setText("");
        listView.setAdapter(null);
        directReceiveHeaderData = null;
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
            getDirectReceiveHeader();
        }
    }

    private void Delete()
    {
        if (directReceiveHeaderData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا اطلاعات مربوط به سند به شماره " +
                directReceiveHeaderData.getID() +
                " حذف گردد؟");

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DirectReceiveListActivity.this.directReceiveListActivityMode
                                = DirectReceiveListActivityMode.BeforeDelete;

                        StoreHandheldService task = new StoreHandheldService(StoreHandheldServiceMode.DeleteDirectReceiveHeader
                                , DirectReceiveListActivity.this);

                        task.addParam("headerID", directReceiveHeaderData.getID());

                        task.listener = DirectReceiveListActivity.this;
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
    public void OnDirectReceiveListCommand(DirectReceiveHeaderData directReceiveHeaderData, int position
            , row_direct_receive_header.DirectReceiveHeaderCommandType commandType)
    {
        this.directReceiveHeaderData = directReceiveHeaderData;
        ((row_direct_receive_header)listView.getAdapter()).setSelection(position);
        if(commandType == row_direct_receive_header.DirectReceiveHeaderCommandType.Delete)
        {
            Delete();
        }
        else if(commandType == row_direct_receive_header.DirectReceiveHeaderCommandType.Edit)
        {
            if(Utility.isDoubleClick()) return;
            Intent intent = new Intent(this, DirectReceiveItemActivity.class);
            intent.putExtra("directReceiveHeaderData", directReceiveHeaderData);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}