package com.oshanak.mobilemarket.Activity.PushNotification;

import com.oshanak.mobilemarket.Activity.Models.NotificationPreviewRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NotificationRestApiService {
    @GET("Home/Api/Notify/GetAllNotifications")
    Call<List<Notification>> getAllNotifications(
            @Query("storeId") int storeId,
            @Header("Authorization") String authHeader
    );

    @POST("Home/Api/Notify/Preview")
     Call<previewResponse> SetPreview(@Body NotificationPreviewRequest request,
                             @Header("Authorization") String authHeader
    );
}
