package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.DefineCompetitorActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorCompanyData;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Service.CompetitorService;
import com.oshanak.mobilemarket.Activity.Service.Enum.CompetitorServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

public class DefineCompetitorActivity extends BaseActivity implements OnTaskCompleted
{

    private EditText etName;
    private CompetitorData selectedCompetitor;
    private EditText etCompetitorCompany;
    private CompetitorCompanyData competitorCompanyData;
    private EditText etLocation;
    private DefineCompetitorActivityMode defineCompetitorActivityMode = DefineCompetitorActivityMode.Unknown;
    private double latitude = 0;
    private double longitude = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_competitor);
        /////////////
        if (Utility.restartAppIfNeed(this)) return;
        etCompetitorCompany = findViewById(R.id.etCompetitorCompany);
        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);

        Intent intent = getIntent();
        defineCompetitorActivityMode = (DefineCompetitorActivityMode) intent.getSerializableExtra("DefineCompetitorActivityMode");
        if(defineCompetitorActivityMode == DefineCompetitorActivityMode.BeforeUpdate)
        {
            Button bConfirm = findViewById(R.id.bConfirm);
            bConfirm.setText("اصلاح");

            selectedCompetitor = (CompetitorData) intent.getSerializableExtra("selectedCompetitor");
            competitorCompanyData = new CompetitorCompanyData();
            competitorCompanyData.setCode(selectedCompetitor.getCompanyCode());
            competitorCompanyData.setName(selectedCompetitor.getCompanyName());

            etCompetitorCompany.setText(competitorCompanyData.getName());
            etName.setText(selectedCompetitor.getName());
            latitude = selectedCompetitor.getLatitude();
            longitude = selectedCompetitor.getLongitude();

            if(latitude > 0 || longitude > 0)
            {
                etLocation.setText("داراي موقعيت مكاني");
            }
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.increaseTextSize(etCompetitorCompany, 20);
            Utility.increaseTextSize(etName, 20);
            Utility.increaseTextSize(etLocation, 20);
        }
    }
    public void onCompetitorCompany(View view)
    {
        Intent intent = new Intent(this, CompetitorCompanyListActivity.class);
        startActivityForResult(intent, 1);
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
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            etLocation.setText("داراي موقعيت مكاني");
        }
    }
    public void onLocationClick(View view)
    {
        if(Utility.isDoubleClick()) return;
        Intent intent = new Intent(this, CompetitorLocationActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("competitorName", etName.getText().toString());
        intent.putExtra("companyName", etCompetitorCompany.getText().toString());
        startActivityForResult(intent, 2);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    @Override
    public void onBackPressed()
    {
        //        if( defineProductGroupActivityMode == DefineProductGroupActivityMode.BeforeInsert && insertDone)
//        {
//            Intent returnIntent = new Intent();
//            setResult(Activity.RESULT_OK, returnIntent);
//        }
//        else
        if(defineCompetitorActivityMode == DefineCompetitorActivityMode.AfterUpdate)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", selectedCompetitor);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onTaskCompleted(Object result)
    {
//        reset();
        stopWait();
        TaskResult taskResult = (TaskResult) result;

        if (Utility.generalErrorOccurred(taskResult, this))
        {
            return;
        }
        else if(defineCompetitorActivityMode == DefineCompetitorActivityMode.BeforeInsert)
        {
            if (!taskResult.isSuccessful)
            {
                if(taskResult.isExceptionOccured("IX_Competitor_CompanyCode_Name"))
                {
                    Utility.simpleAlert(this, "عنوان فروشگاه تکراری است"
                           , DialogIcon.Warning);
                    etName.requestFocus();
                }
                else
                {
                    Utility.simpleAlert(this, getString( R.string.insert_do_not), DialogIcon.Error);
                }
                return;
            }
            Toast.makeText(this, "فروشگاه جديد ثبت گردید.",Toast.LENGTH_LONG).show();
            defineCompetitorActivityMode = DefineCompetitorActivityMode.AfterInsert;
            onBackPressed();

        }
        else if(defineCompetitorActivityMode == DefineCompetitorActivityMode.BeforeUpdate)
        {
            if(!taskResult.isSuccessful)
            {
                if(taskResult.isExceptionOccured("IX_Competitor_CompanyCode_Name"))
                {
                    Utility.simpleAlert(this, "عنوان فروشگاه تکراری است"
                            , DialogIcon.Warning);
                    etName.requestFocus();
                    return;
                }
                else
                {
                    Utility.simpleAlert(this, getString( R.string.update_do_not), DialogIcon.Error);
                    return;
                }
            }

            Toast.makeText(this, getString( R.string.update_done),Toast.LENGTH_LONG).show();
            defineCompetitorActivityMode = DefineCompetitorActivityMode.AfterUpdate;
            onBackPressed();
        }
        Utility.hideKeyboard(this);

    }
//    DialogInterface.OnClickListener onFinishClick= new DialogInterface.OnClickListener()
//    {
//        @Override
//        public void onClick(DialogInterface dialog, int which)
//        {
//            onBackPressed();
//        }
//    };
    public void onClickConfirm(View view)
    {
        if (!validateData()) return;
        CompetitorService service = null;

        if(defineCompetitorActivityMode == DefineCompetitorActivityMode.BeforeInsert)
        {
            service = new CompetitorService(CompetitorServiceMode.InsertCompetitor,this);
            service.competitorData.setRegisterUserName(GlobalData.getUserName());
        }
        else if(defineCompetitorActivityMode == DefineCompetitorActivityMode.BeforeUpdate)
        {
            service = new CompetitorService(CompetitorServiceMode.UpdateCompetitor,this);
            service.competitorData = selectedCompetitor;
            service.competitorData.setCode(selectedCompetitor.getCode());
            selectedCompetitor = service.competitorData;
        }
        service.competitorData.setCompanyCode( competitorCompanyData.getCode());
        service.competitorData.setName( etName.getText().toString().trim());
        service.competitorData.setLatitude(latitude);
        service.competitorData.setLongitude(longitude);

        service.listener = this;
        service.execute();
        startWait();
    }
    private boolean validateData()
    {
        if(competitorCompanyData == null)
        {
            Utility.editTextIsEmpty(etCompetitorCompany, "فروشگاه زنجيره اي را انتخاب نماييد");
            return false;
        }
        if (Utility.editTextIsEmpty(etName, "عنواني را براي فروشگاه وارد کنید")) return false;
        if(defineCompetitorActivityMode == DefineCompetitorActivityMode.BeforeUpdate &&
                !selectedCompetitor.getRegisterUserName().equals( GlobalData.getUserName()))
        {
            Toast.makeText(this,getString( R.string.can_not_update_other_user_data), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
