package com.example.youtube.tab3;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class bookInfo {

    @SerializedName("_id")
    String id;
    String hotelName;
    String loc;
    ArrayList<reservList> reservList;

    bookInfo(String id, String hotelName, String loc, ArrayList<reservList> reservList){
        this.id = id;
        this.hotelName = hotelName;
        this.loc = loc;
        this.reservList = reservList;
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

    public ArrayList<com.example.youtube.tab3.reservList> getReservList(){
        return reservList;
    }
}
