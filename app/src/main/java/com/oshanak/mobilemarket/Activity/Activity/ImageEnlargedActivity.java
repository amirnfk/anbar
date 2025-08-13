package com.oshanak.mobilemarket.Activity.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ZoomControls;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.oshanak.mobilemarket.Activity.PushNotification.PicassoClient;
import com.oshanak.mobilemarket.R;
import com.squareup.picasso.Picasso;

public class ImageEnlargedActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE_URL = "extra_image_url";

    PhotoView enlargedImageView ;
    ProgressBar progressBar;
    private ZoomControls zoomControls;
    private float scale = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_enlarged);
        progressBar=findViewById(R.id.imageinlargeprogress);
        enlargedImageView = findViewById(R.id.enlargedImageView);
//        zoomControls = findViewById(R.id.zoomControls);
        String from="empty";
        // Retrieve the image URL from the intent
        String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);
        from = getIntent().getStringExtra("from");
        assert from != null;
        if(from.equals("notifications"))   {
    String authToken = "bm90aWZ5U2VydmljZVVzZXI6bnNoc2RnZmFoc3VydHFpdXBvamtramtqYjg3Njg3ZzAyMzRrOHY1eDM=";


        Picasso picasso = PicassoClient.getPicassoInstance(this, authToken);
        picasso.load("https://onotify.oshanak.com:8443/Home/Api/Notify/Image?fileName="+imageUrl).into( enlargedImageView);

}else {
    Glide.with(this)
            .load(imageUrl)

            .into(enlargedImageView);
}



//        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scale += 0.1f;
//                applyScaleToImageView();
//            }
//        });
//
//        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scale -= 0.1f;
//                applyScaleToImageView();
//            }
//        });
    }

//    private void applyScaleToImageView() {
//        myImageView.setScaleX(scale);
//        myImageView.setScaleY(scale);
//    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

