package com.example.youtube.contact;

import com.google.gson.annotations.SerializedName;

////////////json 파일을 파싱한 후 받아오는 실제 데이터들이 저장된 꼴////////////
public class Customer {
    @SerializedName("_id")
    String id;
    String name;
    String email;
    String password;
    //Drawable image;

    Customer(String name, String email, String password, String id/*, Drawable image*/){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        //this.image = image;
    }

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getId(){
        return id;
    }

    //public Drawable getImage() {return image;}

    public void setName(String Name){
        name = Name;
    }
    public void setPassword(String Password){
        password = Password;
    }
    public void setEmail(String Phone){
        email = Phone;
    }
    //public void setImage(Drawable Image) {image = Image;}
}