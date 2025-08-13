package com.oshanak.mobilemarket.Activity.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.oshanak.mobilemarket.R;

public class activity_splash extends AppCompatActivity {

    private boolean started = false;
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /////////////
        getSupportActionBar().hide();
        setStatusBarColor();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                Intent mainIntent = new Intent(activity_splash.this, activity_login.class);
                startActivity(mainIntent);
                finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
        /////////////

        Animation fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade);

        ImageView ivArm = findViewById(R.id.imgsplash);
//        ivArm.startAnimation(fadeAnimation);

//        TextView tv1 = findViewById(R.id.tv1);
//        tv1.startAnimation(fadeAnimation);

//        TextView tv2 = findViewById(R.id.tv2);
//        tv2.startAnimation(fadeAnimation);
    }

    private void setStatusBarColor() {
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.White));
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(!started)
        {
//            started = true;
//            TextView tv1 = findViewById(R.id.tv1);
//            Utility.increaseTextSize(tv1,50);
        }
    }
}
