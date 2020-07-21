package com.example.youtube.gallery;

import java.util.ArrayList;

//////////////서버에서 받아오는 json 파일 자체를 파싱하는 클래스////////////
// contactList에 담겨있는 연락처 사람들의 리스트가 getContacts를 통해 반환될 수 있다.
public class ImageReceived {
    private ArrayList<Image> data;
    public ImageReceived(ArrayList<Image> data){
        this.data = data;
    }
    public ArrayList<Image> getImages(){
        return data;
    }
}