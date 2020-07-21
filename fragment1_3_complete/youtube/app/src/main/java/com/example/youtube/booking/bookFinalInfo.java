package com.example.youtube.booking;

public class bookFinalInfo {
    private String hotelName;
    private String hotelID;
    private String hotelLoc;
    private String email;
    private String checkIn;
    private String checkOut;
    private String reservID;

    bookFinalInfo(String hotelName, String hotelID, String hotelLoc, String reservID, String email, String checkIn, String checkOut){
        this.hotelName = hotelName;
        this.hotelID = hotelID;
        this.hotelLoc = hotelLoc;
        this.reservID = reservID;
        this.email = email;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }


    public String getHotelName() {
        return hotelName;
    }
    public String getHotelID() {
        return hotelID;
    }
    public String getHotelLoc() {
        return hotelLoc;
    }
    public String getReservID(){
        return reservID;
    }
    public String getEmail(){
        return email;
    }
    public String getCheckIn() {
        return checkIn;
    }
    public String getCheckOut() {
        return checkOut;
    }
}
