package com.example.youtube.gallery;

import com.google.gson.annotations.SerializedName;

//Json parsing 이후 데이터 저장 형태
//갤러리 내부의 각 이미지를 담은 객체
public class Image {
    @SerializedName("_id")
    String id;
    private String name;
    private String filepath;

    //constructor
    Image(String id, String name, String filepath){
        this.id = id;
        this.name = name;
        this.filepath = filepath;
    }

    //getters
    public String getName() {
        return name;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getId() {
        return id;
    }

    //setter
    public void setName(String name) {
        this.name = name;
    }

}
