package com.example.youtube.tab3;

import com.google.gson.annotations.SerializedName;

public class reservList {
    @SerializedName("_id")
    String id;
    String email;
    String checkIn;
    String checkOut;

    reservList(String id, String email, String checkIn, String checkOut){
        this.id = id;
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public String getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getCheckIn(){
        return checkIn;
    }

    public String getCheckOut() {return checkOut;}
}
