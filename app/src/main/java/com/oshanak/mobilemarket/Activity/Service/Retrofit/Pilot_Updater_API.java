package com.oshanak.mobilemarket.Activity.Service.Retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.Util;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Pilot_Updater_API {

    public static String BASE_URL= "https://storehandheldpilot.ows.gbgnetwork.net/api/store/";
    public static Retrofit myRetrofit = null;

    public static Retrofit getAPI() {

        if (myRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            myRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .protocols(Util.immutableList(Protocol.HTTP_1_1))
                            .addInterceptor(new BasicAuthInterceptor("MobileService", "Aa123456"))
                            .build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }

        return myRetrofit;

    }

}
