package com.example.youtube.contact;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

// contact에 대한 api들을 설정하는 java interface
public interface contactRetrofitInterface {

    @POST("/api/contact/init")
    Call<CustomersResponse> executeInit(@Body HashMap<String, String> map);

//    @POST("/api/login")
//    Call<JsonObject> executeLogin(@Body HashMap<String, String> map);

    @POST("/api/contact/add")
    Call<Void> executeAddDialog (@Body HashMap<String, String> map);

    @POST("/api/contact/remove")
    Call<Void> executeRemoveDialog (@Body HashMap<String, String> map);
}
