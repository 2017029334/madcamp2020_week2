package com.example.youtube.tab3;

import com.example.youtube.tab3.hotelInfo;

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
