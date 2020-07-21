package com.example.youtube.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.youtube.R;

public class FullImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent intent = getIntent();
        String imageName = intent.getStringExtra("String");

        // Selected image id
        int position = intent.getExtras().getInt("id");
        FullImageAdapter fullImageAdapter = new FullImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);

        Glide.with(this)
                .load("http://192.249.19.243:8980/api/uploads/"+ imageName)
                .into(imageView);
    }

}