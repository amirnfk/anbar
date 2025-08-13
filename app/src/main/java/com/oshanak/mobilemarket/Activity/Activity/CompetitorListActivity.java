package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.CompetitorListActivityMode;
import com.oshanak.mobilemarket.Activity.Activity.Enum.DefineCompetitorActivityMode;
import com.oshanak.mobilemarket.Activity.Activity.Enum.DefineCompetitorPriceActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorCompanyData;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_competitor;
import com.oshanak.mobilemarket.Activity.Service.CompetitorService;
import com.oshanak.mobilemarket.Activity.Service.Enum.CompetitorServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javadz.beanutils.BeanUtils;

public class CompetitorListActivity extends BaseActivity implements row_competitor.OnCompetitorListCommandListener
    , OnTaskCompleted
{
    private CompetitorListActivityMode competitorListActivityMode = CompetitorListActivityMode.Unknown;
    private TextView tvCount;
    private ListView listView;
    private CompetitorData competitorData;
    private EditText etName;
    private EditText etCompetitorCompany;
    private CompetitorCompanyData competitorCompanyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_list);
        //////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);
        etCompetitorCompany = findViewById(R.id.etCompetitorCompany);
        etName = findViewById(R.id.etName);

        etCompetitorCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                getList();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                competitorData = (CompetitorData) parent.getItemAtPosition(position);
                ((row_competitor)parent.getAdapter()).setSelection(position);
            }
        });

        getList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.competitor_list_menu, menu);
        super.onCreateOptionsMenu(menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnuNew:
                Intent intent = new Intent(this, DefineCompetitorActivity.class);
                intent.putExtra("DefineCompetitorActivityMode", DefineCompetitorActivityMode.BeforeInsert);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case R.id.mnuSearch:
                getList();
                return true;
            case R.id.mnuExit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onCompetitorCompany(View view)
    {
        Intent intent = new Intent(this, CompetitorCompanyListActivity.class);
        startActivityForResult(intent, 1);
    }
    public void onRemoveCompetitorCompany(View view)
    {
        competitorCompanyData = null;
        etCompetitorCompany.setText("");
    }
    public void onClickRemoveBranch(View view)
    {
        etName.setText("");
    }
    private void getList()
    {
        competitorListActivityMode = CompetitorListActivityMode.BeforeGetList;
        CompetitorService task = new CompetitorService(CompetitorServiceMode.GetCompetitor, this);
        if(competitorCompanyData != null) {
            task.competitorData.setCompanyCode(competitorCompanyData.getCode());
        }

        task.competitorData.setName(etName.getText().toString().trim());
        task.listener = this;
        task.execute();
        startWait();
    }

    @Override
    public void OnCompetitorListCommand(CompetitorData selectedCompetitor, int position, row_competitor.CompetitorListCommandType commandType)
    {
        this.competitorData = selectedCompetitor;
        ((row_competitor)listView.getAdapter()).setSelection(position);

        if(commandType == row_competitor.CompetitorListCommandType.AddPrice)
        {
            if(Utility.isDoubleClick()) return;
            Intent intent = new Intent(this, DefineCompetitorProductActivity.class);
            intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeInsert);
            intent.putExtra("selectedCompetitor", selectedCompetitor);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(commandType == row_competitor.CompetitorListCommandType.Edit)
        {
            if(Utility.isDoubleClick()) return;
            Intent intent = new Intent(this, DefineCompetitorActivity.class);
            intent.putExtra("selectedCompetitor", selectedCompetitor);
            intent.putExtra("DefineCompetitorActivityMode", DefineCompetitorActivityMode.BeforeUpdate);
            startActivityForResult(intent, 2);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if(commandType == row_competitor.CompetitorListCommandType.PriceList)
        {
            if(Utility.isDoubleClick()) return;
            Intent intent = new Intent(this, CompetitorProductListActivity.class);
            intent.putExtra("selectedCompetitor", selectedCompetitor);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
            else if(commandType == row_competitor.CompetitorListCommandType.Delete)
        {
            Delete();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            competitorCompanyData = (CompetitorCompanyData) data.getSerializableExtra("competitorCompanyData");
            etCompetitorCompany.setText(competitorCompanyData.getName());
        }
        else if(requestCode == 2 && resultCode == Activity.RESULT_OK)
        {
            competitorData = (CompetitorData) data.getSerializableExtra("result");

            ArrayAdapter<CompetitorData> adapter = (ArrayAdapter<CompetitorData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                CompetitorData pData = adapter.getItem(i);
                if (pData.getCode() == competitorData.getCode())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, competitorData);
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
    public void onTaskCompleted(Object result)
    {
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this))
        {
            return;
        }
        else if(competitorListActivityMode == CompetitorListActivityMode.BeforeGetList)
        {
            tvCount.setText("");
            competitorData = null;
            listView.setAdapter(null);
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
            ArrayList<CompetitorData> list = (ArrayList<CompetitorData>) taskResult.dataStructure;

            row_competitor adapter = new row_competitor(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            competitorListActivityMode = CompetitorListActivityMode.AfterGetList;
            if(adapter.getCount() >= 200)
            {
                Toast.makeText(this,
                        getString( R.string.only_200_rows)
                        ,Toast.LENGTH_LONG).show();
            }
        }
        else if(competitorListActivityMode == CompetitorListActivityMode.BeforeDelete)
        {
            if(!taskResult.isSuccessful && taskResult.isExceptionOccured("FK_CompetitorProduct_Competitor"))
            {
                Utility.simpleAlert(this,
                        "براي اين فروشگاه مبلغ و محصول ثبت شده و قابل حذف نمي باشد."
                       , DialogIcon.Warning);
                return;
            }
            else
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
                return;
            }

            ArrayAdapter<CompetitorData> adapter = (ArrayAdapter<CompetitorData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                CompetitorData pData = adapter.getItem(i);
                if (pData.getCode() == competitorData.getCode())
                {
                    adapter.remove(pData);
                    competitorData = null;
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), (TextView) findViewById(R.id.tvCount));
            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
            competitorListActivityMode = CompetitorListActivityMode.AfterDelete;
        }

    }
    private void Delete()
    {
        if(Utility.isDoubleClick()) return;
        if (competitorData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }
        if(!competitorData.getRegisterUserName().equals(GlobalData.getUserName()))
        {
            Toast.makeText(this,getString( R.string.can_not_delete_other_user_data), Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا اطلاعات مربوط به" + " " + competitorData.getNameWithCompany() + " " + "از فهرست حذف گردد؟");

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        CompetitorListActivity.this.competitorListActivityMode
                                = CompetitorListActivityMode.BeforeDelete;

                        CompetitorService task = new CompetitorService(CompetitorServiceMode.DeleteCompetitor
                                , CompetitorListActivity.this);
                        task.competitorData.setCode( competitorData.getCode());
                        task.listener = CompetitorListActivity.this;
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
}
