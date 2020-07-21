package com.example.youtube.booking;

import com.google.gson.annotations.SerializedName;

///////////// 호텔 자체에 대한 정보를 받아오는 객체
public class hotelInfo {

    @SerializedName("_id")
    String id;
    String hotelName;
    String loc;

    hotelInfo(String id, String hotelName, String loc){
        this.id = id;
        this.hotelName = hotelName;
        this.loc = loc;
    }

    public String getLoc(){
        return loc;
    }

    public String getHotelName(){
        return hotelName;
    }

    public String getId(){
        return id;
    }

}
