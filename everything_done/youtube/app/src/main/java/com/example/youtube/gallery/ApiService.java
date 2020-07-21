package com.example.youtube.gallery;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {
    @Multipart
    @POST("/api/upload")
    Call<ResponseBody> postImage(
            @Part MultipartBody.Part image,
            @Part("upload") RequestBody name
    );

    //@GET("image/futurestudio-university-logo.png")
    //Call<ResponseBody> downloadFile();

    //@GET
    //Call<ResponseBody> downloadFile(@Url String url);

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    @GET("/api")
    Call<ImageReceived> getNames();

}