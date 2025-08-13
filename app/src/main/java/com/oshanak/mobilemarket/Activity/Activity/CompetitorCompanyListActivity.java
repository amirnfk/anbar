package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.CompetitorCompanyListActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorCompanyData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_competitor_company;
import com.oshanak.mobilemarket.Activity.Service.CompetitorService;
import com.oshanak.mobilemarket.Activity.Service.Enum.CompetitorServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;

public class CompetitorCompanyListActivity extends BaseActivity implements OnTaskCompleted, row_competitor_company.OnCompetitorCompanyCommandListener
{
    CompetitorCompanyListActivityMode competitorCompanyListActivityMode = CompetitorCompanyListActivityMode.Unknown;
    private ListView listView;
    private TextView tvCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_company_list);
        //////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);

        getList();
    }
    private void getList()
    {
        competitorCompanyListActivityMode = CompetitorCompanyListActivityMode.BeforeLoadList;
        CompetitorService service = new CompetitorService(CompetitorServiceMode.GetCompetitorCompany,this);
        service.listener = this;
        service.execute();
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
        else if(competitorCompanyListActivityMode == CompetitorCompanyListActivityMode.BeforeLoadList)
        {

            if (!taskResult.isSuccessful && !taskResult.message.equals("No rows found!")) {
                Utility.simpleAlert(this, getString(R.string.error_in_fetching_data), "", DialogIcon.Error, onFinishClick);
                return;
            }
            else if(!taskResult.isSuccessful && taskResult.message.equals( "No rows found!"))
            {
                return;
            }

            ArrayList<CompetitorCompanyData> list = (ArrayList<CompetitorCompanyData>) taskResult.dataStructure;

            row_competitor_company adapter = new row_competitor_company(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            competitorCompanyListActivityMode = CompetitorCompanyListActivityMode.AfterLoadList;
            if(adapter.getCount() >= 200)
            {
                Toast.makeText(this,
                        getString( R.string.only_200_rows)
                        ,Toast.LENGTH_LONG).show();
            }
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

    @Override
    public void OnCompetitorCompanyCommand(CompetitorCompanyData competitorCompanyData
            , int position, row_competitor_company.CompetitorCompanyCommandType commandType)
    {
        if(commandType == row_competitor_company.CompetitorCompanyCommandType.Select)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("competitorCompanyData", competitorCompanyData);
            this.setResult(Activity.RESULT_OK, returnIntent);
            onBackPressed();
        }

    }
}
