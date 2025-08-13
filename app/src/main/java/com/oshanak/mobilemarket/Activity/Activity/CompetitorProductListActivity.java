package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.oshanak.mobilemarket.Activity.Activity.Enum.CompetitorProductListActivityMode;
import com.oshanak.mobilemarket.Activity.Activity.Enum.DefineCompetitorPriceActivityMode;
import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.DateFragment;
import com.oshanak.mobilemarket.Activity.Common.ExpandCollapseAnim;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorData;
import com.oshanak.mobilemarket.Activity.DataStructure.CompetitorProductData;
import com.oshanak.mobilemarket.Activity.DataStructure.GlobalData;
import com.oshanak.mobilemarket.Activity.Enum.DialogIcon;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_competitor_product;
import com.oshanak.mobilemarket.Activity.Service.CompetitorService;
import com.oshanak.mobilemarket.Activity.Service.Enum.CompetitorServiceMode;
import com.oshanak.mobilemarket.Activity.Service.OnTaskCompleted;
import com.oshanak.mobilemarket.Activity.Service.TaskResult;
import com.oshanak.mobilemarket.R;

import org.ksoap2.serialization.PropertyInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import javadz.beanutils.BeanUtils;

public class CompetitorProductListActivity extends BaseActivity
        implements row_competitor_product.OnCompetitorPriceListCommandListener, DateFragment.OnDateChangedListener,
        OnTaskCompleted
{

    private CompetitorProductListActivityMode competitorProductListActivityMode = CompetitorProductListActivityMode.Unknown;
    private CompetitorData selectedCompetitor;
    private TextView tvStore;
    private DateFragment frDateFrom;
    private DateFragment frDateTo;
    private EditText etProductName;
    private TextView tvCount;
    private ListView listView;
    private CompetitorProductData competitorProductData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_product_list);
        ////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        tvStore = findViewById(R.id.tvStore);
        frDateFrom = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateFrom);
        frDateTo = (DateFragment) getSupportFragmentManager().findFragmentById(R.id.frDateTo);
        etProductName = findViewById(R.id.etProductName);
        tvCount = findViewById(R.id.tvCount);
        listView = findViewById(R.id.recyclerview);

        frDateFrom.setTitle("از تاریخ");
        frDateTo.setTitle("تا تاریخ");

        PersianCalendar pc = new PersianCalendar();
        pc.addPersianDate(PersianCalendar.DATE, -30);
        frDateFrom.setDate(pc);
        frDateTo.setDateToCurrent();

        Intent intent = getIntent();
        selectedCompetitor = (CompetitorData) intent.getSerializableExtra("selectedCompetitor");
        tvStore.setText(selectedCompetitor.getNameWithCompany());

        //region Collapse Params
        final LinearLayout lParamValues = findViewById(R.id.lParamValues);
        ViewTreeObserver vto = lParamValues.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = lParamValues.getViewTreeObserver();

                new ExpandCollapseAnim( (ImageButton) findViewById(R.id.ibMinimize)
                        ,(TextView) findViewById(R.id.tvMoreParam)
                        ,(LinearLayout) findViewById(R.id.lParamValues)
                        ,true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }
        });
        //endregion Collapse Params

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                competitorProductData = (CompetitorProductData) parent.getItemAtPosition(position);
                ((row_competitor_product)parent.getAdapter()).setSelection(position);
            }
        });

        getList();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.competitor_price_menu, menu);
        super.onCreateOptionsMenu(menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mnuNewPrice:
                Intent intent = new Intent(this, DefineCompetitorProductActivity.class);
                intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeInsert);
                intent.putExtra("selectedCompetitor", selectedCompetitor);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case R.id.mnuSearch:
                getList();
                return true;
            case R.id.mnuExit:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        super.onBackPressed();
        Utility.hideKeyboard(this);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private void reset()
    {
        tvCount.setText("");
        listView.setAdapter(null);
        competitorProductData = null;
    }
    private void getList()
    {
        reset();
        if(!Utility.compareTwoDates(this, frDateFrom, frDateTo))
        {
            return;
        }

        competitorProductListActivityMode = CompetitorProductListActivityMode.BeforeGetList;
        CompetitorService task = new CompetitorService(CompetitorServiceMode.GetCompetitorProduct, this);
        PropertyInfo pi;

        pi = new PropertyInfo();
        pi.setName("CompetitorCode");
        pi.setValue(selectedCompetitor.getCode());
        task.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("ProductName");
        pi.setValue(etProductName.getText().toString().trim());
        task.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("fromDate");
        pi.setValue(frDateFrom.getShortDate());
        task.piList.add(pi);

        pi = new PropertyInfo();
        pi.setName("toDate");
        pi.setValue(frDateTo.getShortDate());
        task.piList.add(pi);

        task.listener = this;
        task.execute();
        startWait();
    }
    public void onRemoveProduct(View view)
    {
        etProductName.setText("");
        getList();
    }
    @Override
    public void OnCompetitorPriceListCommand(CompetitorProductData selectedCompetitorProduct
            , int position, row_competitor_product.CompetitorPriceListCommandType commandType)
    {
        this.competitorProductData = selectedCompetitorProduct;
        ((row_competitor_product)listView.getAdapter()).setSelection(position);

        if(commandType == row_competitor_product.CompetitorPriceListCommandType.Edit)
        {
            Intent intent = new Intent(this, DefineCompetitorProductActivity.class);
            intent.putExtra("DefineCompetitorPriceActivityMode", DefineCompetitorPriceActivityMode.BeforeUpdate);
            intent.putExtra("selectedCompetitor", selectedCompetitor);
            intent.putExtra("selectedCompetitorProduct", selectedCompetitorProduct);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else if(commandType == row_competitor_product.CompetitorPriceListCommandType.Delete)
        {
            Delete();
        }
    }

    @Override
    public void OnDateChanged(String PersianLongDate, String PersianShortDate)
    {
        getList();
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
        else if(competitorProductListActivityMode == CompetitorProductListActivityMode.BeforeGetList)
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
            ArrayList<CompetitorProductData> list = (ArrayList<CompetitorProductData>) taskResult.dataStructure;

            row_competitor_product adapter = new row_competitor_product(this, list);
            listView.setAdapter(adapter);
            Utility.setListCount(adapter.getCount(), tvCount);
            Utility.hideKeyboard(this);
            competitorProductListActivityMode = CompetitorProductListActivityMode.AfterGetList;
            if(adapter.getCount() >= 200)
            {
                Toast.makeText(this,
                        getString( R.string.only_200_rows)
                        ,Toast.LENGTH_LONG).show();
            }
        }
        else if(competitorProductListActivityMode == CompetitorProductListActivityMode.BeforeDelete)
        {
//            if(!taskResult.isSuccessful && taskResult.isExceptionOccured("FK_Product_ProductGroup"))
//            {
//                Utility.simpleAlert(this, "گروه کالای انتخابی دارای کالاهای زیرمجموعه بوده و قابل حذف نمی باشد. موارد زير پيشنهاد مي گردد:"
//                        +"\n" + "1. مي توانيد از منوي اصلاح گروه كالا نسبت به غيرفعال كردن آن اقدام نماييد."
//                        +"\n" + "2. نسبت به انتقال كالاهاي زيرمجموعه آن به گروه ديگر اقدام نموده، پس از آن گروه را حذف نماييد.", DialogIcon.Error);
//                return;
//            }
//            else
            if(!taskResult.isSuccessful)
            {
                Utility.simpleAlert(this, getString( R.string.delete_do_not), DialogIcon.Error);
                return;
            }

            ArrayAdapter<CompetitorProductData> adapter = (ArrayAdapter<CompetitorProductData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                CompetitorProductData pData = adapter.getItem(i);
                if (pData.getCode() == competitorProductData.getCode())
                {
                    adapter.remove(pData);
                    competitorProductData = null;
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);// برای پاک کردن رنگ آبی رکورد انتخاب شده
                    break;
                }
            }
            Utility.setListCount(adapter.getCount(), (TextView) findViewById(R.id.tvCount));
            Utility.simpleAlert(this, getString( R.string.delete_done), DialogIcon.Info);
            competitorProductListActivityMode = CompetitorProductListActivityMode.AfterDelete;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult( requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            competitorProductData = (CompetitorProductData) data.getSerializableExtra("result");

            ArrayAdapter<CompetitorProductData> adapter = (ArrayAdapter<CompetitorProductData>) listView.getAdapter();
            for (int i = 0; i < adapter.getCount(); i++)
            {
                CompetitorProductData pData = adapter.getItem(i);
                if (pData.getCode() == competitorProductData.getCode())
                {
                    try
                    {
                        BeanUtils.copyProperties(pData, competitorProductData);
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
    private void Delete()
    {
        if (competitorProductData == null)
        {
            Utility.simpleAlert(this,getString(R.string.select_a_row), DialogIcon.Warning);
            return;
        }
        if(!competitorProductData.getRegisterUserName().equals(GlobalData.getUserName()))
        {
            Toast.makeText(this,getString( R.string.can_not_delete_other_user_data), Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("آیا ركورد مربوط به" + " " + competitorProductData.getName() + " " + "از فهرست حذف گردد؟");

        dlgAlert.setTitle("حذف");
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("قبول",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        CompetitorProductListActivity.this.competitorProductListActivityMode
                                = CompetitorProductListActivityMode.BeforeDelete;

                        CompetitorService task = new CompetitorService(CompetitorServiceMode.DeleteCompetitorProduct
                                , CompetitorProductListActivity.this);
                        task.competitorProductData.setCode( competitorProductData.getCode());
                        task.listener = CompetitorProductListActivity.this;
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
