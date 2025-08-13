package com.oshanak.mobilemarket.Activity.Service;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.ResponseBody;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RemoteApi {
    @Streaming  // Important
    @POST()
    Call<ResponseBody> getImage(@Body float body, @Url String itemid);
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .authenticator(new NTLMAuthenticator("oesales", "123456789")).protocols(Util.immutableList(Protocol.HTTP_1_1))
            // .some other init here if necessary
            .build();
    class Factory {
        private static RemoteApi mInstance;

        public static RemoteApi create() {
            if (mInstance == null) {
                mInstance = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://oshanakportal.oshanak.com/Sale/Item/ItemImage/")
                        .client(okHttpClient)

                        .build()
                        .create(RemoteApi.class);
            }
            return mInstance;
        }
    }
}