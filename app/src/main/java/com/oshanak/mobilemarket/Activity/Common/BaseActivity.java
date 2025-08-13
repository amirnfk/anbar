package com.oshanak.mobilemarket.Activity.Common;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {

    protected boolean inCode = false;
    protected boolean isStarted = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean started = false;
    @Override
    protected void onStart() {
        super.onStart();

        if(!started)
        {
            started = true;
            ViewGroup vg = findViewById(android.R.id.content);
            Utility.setFont(this, vg);
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        for(int pos = 0; pos < menu.size(); pos++)
//        {
//            MenuItem item = menu.getItem(pos);
//            SpannableString s = new SpannableString(item.getTitle());
//
//            s.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, s.length(), 0);
//
//            item.setTitle(s);
//        }
//        return true;
//    }

    public void startWait()
    {
        startWait("لطفاً صبر نمایید...");
    }

    public void startWait(String message/*, int counter*/)
    {
        progressDialog = ProgressDialog.show(this, "", message, true);
    }
    public void stopWait()
    {
            progressDialog.cancel();
    }
}
