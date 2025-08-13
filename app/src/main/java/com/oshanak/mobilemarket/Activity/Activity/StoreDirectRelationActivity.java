package com.oshanak.mobilemarket.Activity.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.Activity.RowAdapter.row_general_spinner;
import com.oshanak.mobilemarket.R;

import java.util.ArrayList;
import java.util.List;

public class StoreDirectRelationActivity extends BaseActivity
{
    private Spinner sSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_direct_relation);
        /////////////////////
        if (Utility.restartAppIfNeed(this)) return;

        sSubject = findViewById(R.id.sSubject);
        initList();
    }
    public void onConfirm(View view)
    {}
    private void initList()
    {
        List<String> list = new ArrayList<>();
        list.add("موضوع مورد نظر را انتخاب كنيد...");
        list.add("كالاي جديد");
        list.add("تامين");
        list.add("مغايرت قيمت");
        list.add("كارت تخفيف");

        row_general_spinner adapter = new row_general_spinner(this, list);
        sSubject.setAdapter(adapter);
    }
}