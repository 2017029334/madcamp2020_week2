package com.example.youtube.booking;

import java.util.ArrayList;

public class getInfoPlease {

    private ArrayList<bookInfo> hotelList;

    public getInfoPlease(ArrayList<bookInfo> hotelList){
        this.hotelList = hotelList;
    }

    public ArrayList<bookInfo> getBookInfo(){
        return hotelList;
    }
}
