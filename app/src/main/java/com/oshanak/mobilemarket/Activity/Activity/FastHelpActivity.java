package com.oshanak.mobilemarket.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.oshanak.mobilemarket.Activity.Common.BaseActivity;
import com.oshanak.mobilemarket.Activity.Common.Utility;
import com.oshanak.mobilemarket.R;

public class FastHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_help);
        ///////////////////
//        if (Utility.restartAppIfNeed(this)) return;
        this.setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        ((TextView)findViewById(R.id.tvTitle)).setText(intent.getStringExtra("title"));
        String comment = intent.getStringExtra("comment");
        TextView tvComment = findViewById(R.id.tvComment);
        Utility.makeHyperlinkText(this, comment, tvComment);

        ImageView ivIcon = findViewById(R.id.ivIcon);
        ivIcon.setImageResource(intent.getIntExtra("imageID",R.drawable.question2));
    }
    public void onCancelClick(View view)
    {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}