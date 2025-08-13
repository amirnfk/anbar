package com.oshanak.mobilemarket.Activity.PushNotification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class createTokenClient {
    private static final String BASE_URL = "https://onotify.oshanak.com/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static SignalerNotificationApiService getApiService() {
        return getRetrofitInstance().create(SignalerNotificationApiService.class);
    }
}