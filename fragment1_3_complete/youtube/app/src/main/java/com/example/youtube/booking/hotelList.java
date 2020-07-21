package com.example.youtube.booking;

import java.util.ArrayList;

// hotel 내역을 받는 객체
public class hotelList {

    private ArrayList<hotelInfo> hotelList;

    public hotelList(ArrayList<hotelInfo> hotelList){
        this.hotelList = hotelList;
    }

    public ArrayList<hotelInfo> getHotelList(){
        return hotelList;
    }

}
