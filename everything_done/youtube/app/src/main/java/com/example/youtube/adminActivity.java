package com.example.youtube;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.youtube.booking.bookingRetrofitInterface;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class adminActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final LatLng SEOUL = new LatLng(37.56, 126.97);
    private Marker mSeoul;
    private GoogleMap mMap;
    private double lati = 100;
    private double longi = 100;
    private String loc;
    private String hotelName;
    private Button regiHotel;
    private Retrofit retrofit;
    private bookingRetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.19.243:8980";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(bookingRetrofitInterface.class);
//
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapHotelReg);
        mapFragment.getMapAsync(this);
        //mSeoul = mMap.addMarker(new MarkerOptions().position(SEOUL).draggable(true));

        final EditText hotelName = findViewById(R.id.hotelNameEdit);

        Spinner s = (Spinner) findViewById(R.id.spinner2);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                loc = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        ///////////호텔을 등록하는 버튼 가져오기///////////////
        // 버튼 누르면 호텔 이름, 위치, 지도를 통한 위도, 경도 정보를 서버 db에 등록
        regiHotel = findViewById(R.id.regiHotel);
        regiHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            HashMap<String, String> map = new HashMap<>();

            map.put("hotelName", hotelName.getText().toString());
            map.put("loc", loc);
            map.put("lati", Double.toString(lati));
            map.put("longi", Double.toString(longi));

            Call<Void> call = retrofitInterface.executeRegiHotel(map);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    //success of login
                    if (response.code() == 200) {
                        Toast.makeText(adminActivity.this, "Register Success!",
                                Toast.LENGTH_LONG).show();
                    }
                    //failure of login
                    else if (response.code() == 400) {
                        Toast.makeText(adminActivity.this, "Register Fail...",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(adminActivity.this, t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

            }
        });

    }



    ////////////// 이 이후는 딱히 볼거 없음////////////////
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mSeoul = mMap.addMarker(new MarkerOptions().position(SEOUL).draggable(true));
        mSeoul.setTag(0);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }
            @Override
            public void onMarkerDragEnd(Marker marker) {
                lati = mSeoul.getPosition().latitude;
                longi = mSeoul.getPosition().longitude;
                System.out.println(lati + ", " + longi);
            }
            @Override
            public void onMarkerDrag(Marker marker) {

            }
        });
        //mSeoul = mMap.addMarker(new MarkerOptions().position(SEOUL).draggable(true));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}
