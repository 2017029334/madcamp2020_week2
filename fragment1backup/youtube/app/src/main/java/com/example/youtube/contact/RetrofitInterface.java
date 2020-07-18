package com.example.youtube.contact;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @GET("/api/init")
    Call<CustomersResponse> executeInit();

    @POST("/api/login")
    Call<JsonObject> executeLogin(@Body HashMap<String, String> map);

    @POST("/api/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);
}
