package com.oshanak.mobilemarket.Activity.Service;


import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.POST;

public interface OpenInboundApiService {

    @POST("api/Store/GetOpenInboundHeader")
    Call<OpenInboundResponseModel> getOpenInboundHeader(@Body OpenInboundRequestModel openInboundRequestModel);

    @POST("api/Store/ClearInboundHeader")
    Call<TaskResult> ClearInboundHeader(@Body OpenInboundClearRequestModel openInboundClearRequestModel);
}