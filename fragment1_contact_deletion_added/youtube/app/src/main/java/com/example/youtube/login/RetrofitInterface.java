package com.example.youtube.login;

import com.example.youtube.login.LoginResult;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/api/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/api/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

}
