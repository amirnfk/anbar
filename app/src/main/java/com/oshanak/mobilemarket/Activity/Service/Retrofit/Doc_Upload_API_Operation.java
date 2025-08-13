package com.oshanak.mobilemarket.Activity.Service.Retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Doc_Upload_API_Operation {

    public static String BASE_URL="https://storehandheld.ows.gbgnetwork.net/api/store/";
    public static Retrofit myRetrofit = null;

    public static Retrofit getAPI() {

        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        if (myRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

           myRetrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return myRetrofit;

    }

}