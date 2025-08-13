package com.oshanak.mobilemarket.Activity.PushNotification;

import com.oshanak.mobilemarket.Activity.Service.Retrofit.LoginRequest;
import com.oshanak.mobilemarket.Activity.Service.Retrofit.LoginResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface SignalerNotificationApiService {
    @GET
    Call<ResponseBody> downloadAudio(@Url String fileUrl);

    @POST("Api/Token/CreateToken")
    Call<CreateTokenResponse> createToken(@Body CreateTokenRequest request);

    @POST("Api/Authentication/Login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}