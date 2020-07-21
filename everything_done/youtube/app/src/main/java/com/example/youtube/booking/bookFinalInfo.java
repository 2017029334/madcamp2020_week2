package com.example.youtube.booking;

// 최종적으로 예약한 정보를 띄우기 위한 정보를 담은 객체
public class bookFinalInfo {
    private String hotelName;
    private String hotelID;
    private String hotelLoc;
    private String lati;
    private String longi;
    private String email;
    private String checkIn;
    private String checkOut;
    private String reservID;

    public bookFinalInfo(String hotelName, String hotelID, String hotelLoc, String lati, String longi, String reservID, String email, String checkIn, String checkOut){
        this.hotelName = hotelName;
        this.hotelID = hotelID;
        this.hotelLoc = hotelLoc;
        this.lati = lati;
        this.longi = longi;
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
    public String getLati() {
        return lati;
    }
    public String getLongi() {
        return longi;
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