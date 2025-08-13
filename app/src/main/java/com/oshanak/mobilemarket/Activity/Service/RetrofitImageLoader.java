package com.oshanak.mobilemarket.Activity.Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

public class RetrofitImageLoader extends AsyncTask<InputStream, Integer, Bitmap> {
    private ImageView imageView;

    public RetrofitImageLoader(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(InputStream... inputStreams) {



        return BitmapFactory.decodeStream(inputStreams[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }


}