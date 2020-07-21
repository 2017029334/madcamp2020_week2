package com.example.youtube.booking;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// contact에 대한 api들을 설정하는 java interface
public interface bookingRetrofitInterface {

    @POST("/api/booking/register")
    Call<hotelList> executeInit(@Body HashMap<String, String> map);

    @POST("/api/booking/book")
    Call<Void> executeBook(@Body HashMap<String, String> map);

    @POST("/api/booking/cancel")
    Call<Void> executeCancelBook(@Body HashMap<String, String> map);

    @POST("/api/booking/getHotelList")
    Call<hotelList> executeGetHotel(@Body HashMap<String, String> map);

    @POST("/api/booking/getBookList")
    Call<getInfoPlease> executeBookList(@Body HashMap<String, String> map);
}
