package com.example.youtube.tab3;

import com.google.gson.annotations.SerializedName;

public class hotelInfo {

    @SerializedName("_id")
    String id;
    String hotelName;
    //String email;
    String loc;
    //Drawable image;

    hotelInfo(String id, String hotelName, String loc/*, Drawable image*/){
        this.id = id;
        this.hotelName = hotelName;
        //this.email = email;
        this.loc = loc;
        //this.image = image;
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
