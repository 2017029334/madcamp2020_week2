package com.example.youtube.tab3;

import com.example.youtube.contact.Customer;

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
