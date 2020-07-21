package com.example.youtube.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.youtube.ChatActivity;
import com.example.youtube.R;

public class CustomersClicked extends Activity implements View.OnClickListener {
    private TextView mNum;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_clicked);

        Intent intent = getIntent();

        TextView name = (TextView) findViewById(R.id.tv_name);
        //TextView gender = (TextView) findViewById(R.id.tv_gender);
        TextView phone = (TextView) findViewById(R.id.tv_phone);
        //ImageView image = (ImageView) findViewById(R.id.image);

        Button mDialogCall = (Button) findViewById(R.id.btnDial);
        Button mChat = (Button) findViewById(R.id.btnChat);
        Button mCall = (Button) findViewById(R.id.btnCall);

        String person_name = intent.getStringExtra("name");
        userName = person_name;
        name.setText(person_name);
        //gender.setText(intent.getStringExtra("gender"));
        phone.setText(intent.getStringExtra("phone"));

        mNum = phone;
        mDialogCall.setOnClickListener(this);
        mChat.setOnClickListener(this);
        mCall.setOnClickListener(this);
    }

    public void onClick(View v){

        String tel = "tel:" + mNum.getText().toString();
        switch(v.getId()){
            case R.id.btnCall:
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);
                startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                break;
            case R.id.btnChat:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("name", userName);
                startActivity(intent);
                break;
            case R.id.btnDial:
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                break;
        }
    }
}