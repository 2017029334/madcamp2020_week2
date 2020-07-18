package com.example.youtube.contact;

import java.util.ArrayList;

//////////////서버에서 받아오는 json 파일 자체를 파싱하는 클래스////////////
public class CustomersResponse {

    private ArrayList<Customer> users;

    public CustomersResponse(ArrayList<Customer> users){
        this.users = users;
    }

    public ArrayList<Customer> getUsers(){
        return users;
    }
}
