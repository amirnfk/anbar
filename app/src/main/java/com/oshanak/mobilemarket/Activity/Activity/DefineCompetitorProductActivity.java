package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.DefineCompetitorPriceActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.ThousandSeparatorWatcher;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorData;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorProductData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.Service.CompetitorService;
import com.oshanak.mobilemarket.Activity.Service.Enum.CompetitorServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

public class DefineCompetitorProductActivity extends BaseActivity implements OnTaskCompleted
{
    private TextView tvStore;
    private EditText etBarcode;
    private EditText etName;
    private EditText etPrice;
    private CheckBox cbIsPromotion;
    private CompetitorData selectedCompetitor;
    private CompetitorProductData competitorProductData;
    private DefineCompetitorPriceActivityMode defineCompetitorPriceActivityMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_competitor_product);
        ////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvStore = findViewById(R.id.tvStore);
        etBarcode = findViewById(R.id.etBarcode);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        cbIsPromotion = findViewById(R.id.cbIsPromotion);

        etPrice.addTextChangedListener(new ThousandSeparatorWatcher(etPrice));

        Intent intent = getIntent();
        defineCompetitorPriceActivityMode = (DefineCompetitorPriceActivityMode) intent.getSerializableExtra("DefineCompetitorPriceActivityMode");
        selectedCompetitor = (CompetitorData) intent.getSerializableExtra("selectedCompetitor");

        tvStore.setText(selectedCompetitor.getNameWithCompany());
//        Utility.enableViews(this, false, etBarcode);

        LinearLayout lUserName = findViewById(R.id.lUserName);
        LinearLayout lInsertDate = findViewById(R.id.lInsertDate);
        View divider2 = findViewById(R.id.divider2);

        if(defineCompetitorPriceActivityMode == DefineCompetitorPriceActivityMode.BeforeInsert)
        {
            lUserName.setVisibility(View.GONE);
            lInsertDate.setVisibility(View.GONE);
            divider2.setVisibility(View.GONE);
        }
        else if(defineCompetitorPriceActivityMode == DefineCompetitorPriceActivityMode.BeforeUpdate)
        {
            Button bConfirm = findViewById(R.id.bConfirm);
            bConfirm.setText("اصلاح");
            competitorProductData = (CompetitorProductData) intent.getSerializableExtra("selectedCompetitorProduct");
            etName.setText(competitorProductData.getName());
            etPrice.setText(String.valueOf( competitorProductData.getPrice()));
            cbIsPromotion.setChecked(competitorProductData.getIsPromotion() == 1);

            TextView tvUserName = findViewById(R.id.tvUserName);
            TextView tvInsertDate = findViewById(R.id.tvInsertDate);
            tvUserName.setText(competitorProductData.getRegisterUserName());
            tvInsertDate.setText(competitorProductData.getDateTime());
        }
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        if(!isStarted)
        {
            isStarted = true;
            Utility.setFontBold(tvStore);
            Utility.increaseTextSize(tvStore,20);
        }
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
            if(defineCompetitorPriceActivityMode == DefineCompetitorPriceActivityMode.AfterUpdate)
        {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", competitorProductData);
            setResult(Activity.RESULT_OK, returnIntent);
        }
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public void onClickConfirm(View view)
    {
        if (!validateData()) return;
        CompetitorService service = null;

        if(defineCompetitorPriceActivityMode == DefineCompetitorPriceActivityMode.BeforeInsert)
        {
            service = new CompetitorService(CompetitorServiceMode.InsertCompetitorProduct, this);
            service.competitorProductData.setCompetitorCode(selectedCompetitor.getCode());
            service.competitorProductData.setName(etName.getText().toString().trim());
            service.competitorProductData.setPrice(Integer.valueOf(ThousandSeparatorWatcher.removeSeparator(etPrice.getText().toString().trim())));
            service.competitorProductData.setIsPromotion(cbIsPromotion.isChecked() ? 1 : 0);
            service.competitorProductData.setRegisterUserName(GlobalData.getUserName());
        }
        else if(defineCompetitorPriceActivityMode == DefineCompetitorPriceActivityMode.BeforeUpdate)
        {
            service = new CompetitorService(CompetitorServiceMode.UpdateCompetitorProduct, this);
            service.competitorProductData = competitorProductData;
            service.competitorProductData.setCode(competitorProductData.getCode());
            service.competitorProductData.setName(etName.getText().toString().trim());
            service.competitorProductData.setPrice(Integer.valueOf(ThousandSeparatorWatcher.removeSeparator(etPrice.getText().toString().trim())));
            service.competitorProductData.setIsPromotion(cbIsPromotion.isChecked() ? 1 : 0);
            competitorProductData = service.competitorProductData;
        }
        service.listener = this;
        service.execute();
        startWait();
    }
    private boolean validateData()
    {
        if (Utility.editTextIsEmpty(etName, "عنوان كالا را وارد نماييد")) return false;
        if (Utility.editTextIsEmpty(etPrice, "قيمت را وارد نماييد")) return false;
        if(defineCompetitorPriceActivityMode == DefineCompetitorPriceActivityMode.BeforeUpdate &&
            !competitorProductData.getRegisterUserName().equals( GlobalData.getUserName()))
        {
            Toast.makeText(this,getString( R.string.can_not_update_other_user_data), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
        else if(defineCompetitorPriceActivityMode == DefineCompetitorPriceActivityMode.BeforeInsert)
        {
            if (!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.insert_do_not), DialogIcon.Error);
                return;
            }
            Toast.makeText(this, "مبلغ مورد نظر ثبت گردید.",Toast.LENGTH_LONG).show();
            defineCompetitorPriceActivityMode = DefineCompetitorPriceActivityMode.AfterInsert;
//            onBackPressed();
            resetToInsert();

        }
        else if(defineCompetitorPriceActivityMode == DefineCompetitorPriceActivityMode.BeforeUpdate)
        {
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.update_do_not), DialogIcon.Error);
                return;
            }

            Toast.makeText(this, getString( R.string.update_done),Toast.LENGTH_LONG).show();
            defineCompetitorPriceActivityMode = DefineCompetitorPriceActivityMode.AfterUpdate;
            onBackPressed();
        }
        Utility.hideKeyboard(this);
    }
    private void resetToInsert()
    {
        defineCompetitorPriceActivityMode = DefineCompetitorPriceActivityMode.BeforeInsert;
        etName.setText("");
        etPrice.setText("");
        cbIsPromotion.setChecked(false);
        etBarcode.setText("");
        Utility.hideKeyboard(this);
    }
    public void onClickBarcode(View view)
    {
//        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
//        scanIntegrator.initiateScan();
        startScan();
    }
    public void startScan()
    {
        com.google.zxing.integration.android.IntentIntegrator integrator = new com.google.zxing.integration.android.IntentIntegrator(this);
        integrator.setPrompt("باركد كالا را دقيقاً داخل كادر قرار دهيد");
        integrator.setDesiredBarcodeFormats(com.google.zxing.integration.android.IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setBeepEnabled(false);
//            integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult( requestCode, resultCode, intent);
        if(requestCode == GlobalData.getBarcodeActivityRequestCode())
        {
            if(requestCode != GlobalData.getBarcodeActivityRequestCode()) return;
            com.google.zxing.integration.android.IntentResult scanningResult =
                    com.google.zxing.integration.android.IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult == null || scanningResult.getContents() == null)
            {
                etBarcode.setText("");
                Toast.makeText(this, "بارکد کالا به درستی اسکن نشد.", Toast.LENGTH_SHORT).show();

            } else
            {
                String scanContent = scanningResult.getContents();
                etBarcode.setText(scanContent);
            }
        }
    }
}
