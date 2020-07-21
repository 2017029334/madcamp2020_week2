package com.example.youtube.contact;

import com.google.gson.annotations.SerializedName;

////////////json 파일을 파싱한 후 받아오는 실제 데이터들이 저장된 꼴////////////
// 연락처에 있는 개개인의 이름과 전화번호를 담은 객체
public class Customer {
    @SerializedName("_id")
    String id;
    String nameContact;
    //String email;
    String phoneNum;
    //Drawable image;

    Customer(String nameContact, String phoneNum, String id/*, Drawable image*/){
        this.id = id;
        this.nameContact = nameContact;
        //this.email = email;
        this.phoneNum = phoneNum;
        //this.image = image;
    }

    public String getName(){
        return nameContact;
    }

    public String getPhoneNum(){
        return phoneNum;
    }

//    public String getEmail(){
//        return email;
//    }

    public String getId(){
        return id;
    }

    //public Drawable getImage() {return image;}

//    public void setName(String Name){
//        nameContact = Name;
//    }
//    public void setPassword(String Password){
//        phoneNum = Password;
//    }
//    public void setEmail(String Phone){
//        email = Phone;
//    }
    //public void setImage(Drawable Image) {image = Image;}
}