package com.oshanak.mobilemarket.Activity.PickingApp;

import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.GetPickingDeliverItemRequest;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.GetPickingDeliverItemResponse;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingControlHeaderDataResponseBodyModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingControlHeaderRequestBodyModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingDeliverHeaderDataResponseBodyModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.PickingDeliverHeaderRequestBodyModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingHeaderStatusRequest;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingItemStatusRequestModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingItemStatusResponseModel;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingLineStatusRequest;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingRequest;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.UpdatePickingResponse;
import com.oshanak.mobilemarket.Activity.PickingApp.PicikkingWebService.Models.updatePickingHeaderStatusResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PickingApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/Collector/Login")
    Call<PickingLoginResponse> loginUser(@Body PickingLoginRequest loginRequest);
    @Headers("Content-Type: application/json")
    @POST("api/collector/GetPickingDeliverHeader")
    Call<PickingDeliverHeaderDataResponseBodyModel> getPickingDeliverHeader(@Body PickingDeliverHeaderRequestBodyModel requestBody);

    @Headers("Content-Type: application/json")
    @POST("api/collector/GetPickingControlHeader")
    Call<PickingControlHeaderDataResponseBodyModel> getPickingControlHeader(@Body PickingControlHeaderRequestBodyModel requestBody);


    @Headers("Content-Type: application/json")
    @POST("api/collector/GetPickingControlLine")
    Call<PickingControlHeaderDataResponseBodyModel> GetPickingControlLine(@Body PickingControlHeaderRequestBodyModel requestBody);

    @POST("api/collector/GetPickingDeliverItem")
    Call<GetPickingDeliverItemResponse> getPickingDeliverItem(@Body GetPickingDeliverItemRequest request);

    @POST("api/collector/UpdatePickingHeaderStatus")
    Call<updatePickingHeaderStatusResponse> updatePickingHeaderStatus(@Body UpdatePickingHeaderStatusRequest requestBody);


    @POST("api/collector/UpdatePickingLineStatus")
    Call<updatePickingHeaderStatusResponse> UpdatePickingLineStatus(@Body UpdatePickingLineStatusRequest requestBody);

    @POST("api/collector/UpdatePickingItemStatus")
    Call<UpdatePickingItemStatusResponseModel> UpdatePickingItemStatus(@Body UpdatePickingItemStatusRequestModel requestBody);

    @Headers("Content-Type: application/json")
    @POST("api/collector/UpdatePickingDeliverItemAmount")
    Call<UpdatePickingResponse> UpdatePickingDeliverItemAmount(@Body UpdatePickingRequest request);
}

