package com.example.youtube.booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.youtube.R;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class showHotel extends AppCompatActivity {

    private int checkInYear;
    private int checkInMonth;
    private int checkInDay;
    private int checkOutYear;
    private int checkOutMonth;
    private int checkOutDay;
    private int guestNum;
    private ListView listView;
    private hotelViewAdapter adapter;
    private String location;
    private String email_key;
    private Retrofit retrofit;
    private bookingRetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.19.243:8980";
    private ArrayList<hotelInfo> list;
    private ArrayList<hotelInfo> arraylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hotel);

        listView = (ListView) findViewById(R.id.hotelListView);

        location = getIntent().getStringExtra("loc");
        checkInYear = getIntent().getIntExtra("ciy", 0);
        checkInMonth = getIntent().getIntExtra("cim", 0);
        checkInDay = getIntent().getIntExtra("cid", 0);
        checkOutYear = getIntent().getIntExtra("coy", 0);
        checkOutMonth = getIntent().getIntExtra("com", 0);
        checkOutDay = getIntent().getIntExtra("cod", 0);
        guestNum = getIntent().getIntExtra("guestNum", 0);
        email_key = getIntent().getStringExtra("email_key");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(bookingRetrofitInterface.class);

        initiation();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String name = list.get(position).getHotelName();
                String loc = list.get(position).getLoc();
                bookHotel(name, loc);
//                Intent intent = new Intent(showHotel.this, CustomersClicked.class);
//                intent.putExtra("name", list.get(position).getName());
//                //intent.putExtra("gender", list.get(position).getGender());
//                intent.putExtra("phone", list.get(position).getPhoneNum());
//                startActivity(intent);
            }
        });

    }

    private void initiation(){

        // Hashmap 선언 후 key로 쓰일 이메일 값만 담아서 보냄
        HashMap<String, String> map = new HashMap<>();

        map.put("loc", location);
        Call<hotelList> call = retrofitInterface.executeGetHotel(map);

        call.enqueue(new Callback<hotelList>() {
            @Override
            // server로부터 응답이 돌아오면
            public void onResponse(Call<hotelList> call, Response<hotelList> response) {
                System.out.println("hey!");
                // 정상적으로 연락처 정보가 도착 시(200), 연락처 갱신
                if(response.code() == 200){
                    //CustomersResponse list = response.body();
                    if (response.body() != null) {
                        list = response.body().getHotelList();

                        arraylist = new ArrayList<hotelInfo>();
                        arraylist.addAll(list);
                        adapter = new hotelViewAdapter(list, showHotel.this);

                        listView.setAdapter(adapter);
                    }
                }
                // 비정상적인 이벤트 발생시 (404), 토스트 메시지 띄우기
                else if(response.code()==404){
                    Toast.makeText(showHotel.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }
            }

            // 에러 발생시
            @Override
            public void onFailure(Call<hotelList> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(showHotel.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bookHotel(String name, String loc){
        HashMap<String, String> map = new HashMap<>();

        map.put("hotelName", name);
        map.put("loc", loc);
        map.put("email", email_key);
        map.put("checkIn", checkInYear + "." + checkInMonth + "." + checkInDay);
        map.put("checkOut", checkOutYear + "." + checkOutMonth + "." + checkOutDay);

        System.out.println("checkout date is this!!!!!" + checkOutYear + "." + checkOutMonth + "." + checkOutDay);
        Call<Void> call = retrofitInterface.executeBook(map);

        call.enqueue(new Callback<Void>() {
            @Override
            // server로부터 응답이 돌아오면
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("hey!");
                // 정상적으로 연락처 정보가 도착 시(200), 연락처 갱신
                if(response.code() == 200){
                    //CustomersResponse list = response.body();
//                    if (response.body() != null) {
//                        list = response.body().getHotelList();
//
//                        arraylist = new ArrayList<hotelInfo>();
//                        arraylist.addAll(list);
//                        adapter = new hotelViewAdapter(list, showHotel.this);
//
//                        listView.setAdapter(adapter);
//                    }
                    Toast.makeText(showHotel.this, "Booking success!",
                            Toast.LENGTH_LONG).show();
                }
                // 비정상적인 이벤트 발생시 (404), 토스트 메시지 띄우기
                else if(response.code()==404){
                    Toast.makeText(showHotel.this, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }
            }

            // 에러 발생시
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(showHotel.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        System.out.println(checkInYear + "." + checkInMonth + "." + checkInDay);
    }
}