package com.oshanak.mobilemarket.Activity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.Enum.WarehousingItemFilterMode;
import com.oshanak.mobilemarket.R;

public class FilterWarehousingItemActivity extends BaseActivity {

    private RadioButton rAllItem;
    private RadioButton rCountedItem;
    private RadioButton rNoCountedItem;
    private RadioGroup rgItemFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_warehousing_item);
        //////////////////////////////
        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        rAllItem = findViewById(R.id.rAllItem);
        rCountedItem = findViewById(R.id.rCountedItem);
        rNoCountedItem = findViewById(R.id.rNoCountedItem);
        rgItemFilter = findViewById(R.id.rgItemFilter);

        rgItemFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                filterSelected = true;
                FilterWarehousingItemActivity.this.onBackPressed();
            }
        });
    }
    public void onClickExit(View view)
    {
        onBackPressed();
    }
    private boolean filterSelected = false;
    @Override
    public void onBackPressed()
    {
        if(filterSelected )
        {

            WarehousingItemFilterMode filterMode = WarehousingItemFilterMode.Unknown;
            if(rAllItem.isChecked())
            {
                filterMode = WarehousingItemFilterMode.AllItem;
            }
            else if(rCountedItem.isChecked())
            {
                filterMode = WarehousingItemFilterMode.CountedItem;
            }
            else if(rNoCountedItem.isChecked())
            {
                filterMode = WarehousingItemFilterMode.NoCountedItem;
            }
            Intent returnIntent = new Intent();
            returnIntent.putExtra("filterMode", filterMode);
            setResult(Activity.RESULT_OK, returnIntent);
        }

        super.onBackPressed();
        Utility.hideKeyboard(this);
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}