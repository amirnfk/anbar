package com.oshanak.mobilemarket.Activity.Service.Retrofit;


import com.oshanak.mobilemarket.Activity.Models.DeleteDocRequestModel;
import com.oshanak.mobilemarket.Activity.Models.DeleteDocResult;
import com.oshanak.mobilemarket.Activity.Models.DocModel;
import com.oshanak.mobilemarket.Activity.Models.DocResponse;
import com.oshanak.mobilemarket.Activity.Models.DocsListModel;
import com.oshanak.mobilemarket.Activity.Models.GetDocListModel;
import com.oshanak.mobilemarket.Activity.Models.InboundDetailResponse;
import com.oshanak.mobilemarket.Activity.Models.ItemsListModel;
import com.oshanak.mobilemarket.Activity.Models.ItemsListRequestModel;
import com.oshanak.mobilemarket.Activity.Models.UpdateDetailResponse;
import com.oshanak.mobilemarket.Activity.Models.UpdateDocModel;
import com.oshanak.mobilemarket.Activity.Models.UploadImageResponse;
import com.oshanak.mobilemarket.Activity.Models.metaData;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST("")
    Call<DocsTypeModel> getDocsTypes(@Url String Url,
                                     @Body() metaData metaData
    );

    @POST("")
    Call<DocResponse> uploadDoc(@Url String Url, @Body () DocModel docs_model);
    @POST("")
    Call<DocResponse> updateDoc(@Url String Url, @Body () UpdateDocModel updateDocModel);

    @POST("")
    Call<DocsListModel> getDocsList(@Url String Url, @Body () GetDocListModel getDocListModel);

    @POST("")
    Call<ItemsListModel> getItemsList(@Url String Url, @Body () ItemsListRequestModel itemsListRequestModel );

    @POST("")
    Call<DeleteDocResult> deleteDocById(@Url String Url, @Body () DeleteDocRequestModel deleteDocRequestModel);

    @POST("")
    Call<DeleteDocResult> deleteImageById(@Url String Url, @Body () DeleteDocRequestModel deleteDocRequestModel);

    @Multipart
    @POST("")
    Call<UploadImageResponse> uploadImage(@Url String Url,
                                          @Part MultipartBody.Part image,
                                          @Header("HeaderId") Integer HeaderId ,
                                          @Header("UserName") String UserName,
                                          @Header("ImageId") int ImageId);

    @Streaming
    @GET
    Call<ResponseBody> downlload(@Url String fileUrl);

    @Headers("Content-Type: application/json")
    @POST("store/GetInboundDetail")
    Call<InboundDetailResponse> getInboundDetail(@Body InboundDetailRequest request);
    @Headers("Content-Type: application/json")
    @POST("store/UpdateInboundDetailStatus")
    Call<UpdateDetailResponse> UpdateInboundDetailStatus(@Body UpdateStatusDetailRequest request);

    @Headers("Content-Type: application/json")
    @POST("store/UpdateInboundDetail")
    Call<UpdateDetailResponse> UpdateInboundDetail(@Body UpdateDetailRequest request);



}




