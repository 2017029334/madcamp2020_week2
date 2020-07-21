package com.example.youtube.booking;

import java.util.ArrayList;

public class hotelList {

    private ArrayList<hotelInfo> hotelList;

    public hotelList(ArrayList<hotelInfo> hotelList){
        this.hotelList = hotelList;
    }

    public ArrayList<hotelInfo> getHotelList(){
        return hotelList;
    }

}
