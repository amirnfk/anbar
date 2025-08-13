package com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService;



import com.oshanak.mobilemarket.Activity.Service.Retrofit.UnsafeOkHttpClient;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class PickingRetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://pickingpilot.ows.gbgnetwork.net";

    // Timeout values in seconds
    private static final int CONNECT_TIMEOUT = 30; // Connection timeout
    private static final int READ_TIMEOUT = 30;    // Read timeout
    private static final int WRITE_TIMEOUT = 30;   // Write timeout

    public static Retrofit getRetrofitInstance() {
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        // Build with custom timeouts
        OkHttpClient.Builder builder = client.newBuilder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

        // Create a new client with the updated timeouts
        OkHttpClient customClient = builder.build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(customClient) // Use the custom client with new timeouts
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
//import com.oshanak.mobilemarket.Activity.Service.Retrofit.UnsafeOkHttpClient;
//
//import okhttp3.OkHttpClient;
//import retrofit2.Retrofit;
//        import retrofit2.converter.gson.GsonConverterFactory;
//
//public class PickingRetrofitClient {
//    private static Retrofit retrofit;
//    private static final String BASE_URL = "https://pickingpilot.ows.gbgnetwork.net";
//    public static Retrofit getRetrofitInstance() {
//        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
//
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(client)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//}