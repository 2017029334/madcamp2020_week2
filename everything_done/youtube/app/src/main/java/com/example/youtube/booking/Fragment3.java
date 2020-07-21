package com.example.youtube.booking;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youtube.MainActivity;
import com.example.youtube.R;
import com.example.youtube.booking.bookFinalInfo;
import com.example.youtube.booking.bookInfo;
import com.example.youtube.booking.bookViewAdapter;
import com.example.youtube.booking.getInfoPlease;
import com.example.youtube.booking.reservList;
import com.example.youtube.booking.showHotel;
import com.example.youtube.contact.Customer;
import com.example.youtube.contact.CustomersClicked;
import com.example.youtube.contact.ListViewAdapter_mod;
import com.example.youtube.booking.bookingRetrofitInterface;
import com.example.youtube.login.LoginActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragment3 extends Fragment implements OnMapReadyCallback {

    Context mContext;
    View mView;
    private String email_key;
    private String location;
    private String checkInDate;
    private String checkOutDate;
    private int guestNum = 4;

    private double Lati = 37.56;
    private double Longi = 126.97;
    private String mapHotel = "서울";
    private String mapLoc = "대전";

    private TextView textView_Date1;
    private TextView textView_Date2;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private int checkInYear;
    private int checkInMonth;
    private int checkInDay;
    private int checkOutYear;
    private int checkOutMonth;
    private int checkOutDay;
    private bookViewAdapter adapter;
    private Button btn_date_pick1;
    private Button btn_date_pick2;
    private Button searchHotel;
    private Retrofit retrofit;
    private ListView listView;
    private bookingRetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.19.243:8980";
    private ArrayList<bookFinalInfo> showBook;
    private MapView mMap = null;
    int mYear, mMonth, mDay, mHour, mMinute;



    public Fragment3() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_3, container, false);

        email_key = ((MainActivity) getActivity()).getEmail_key();

        listView = (ListView) mView.findViewById(R.id.bookList);
        showBook = new ArrayList<bookFinalInfo>();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(bookingRetrofitInterface.class);

        //텍스트뷰 2개 연결
        textView_Date1 = (TextView) mView.findViewById(R.id.textView_date1);
        textView_Date2 = (TextView) mView.findViewById(R.id.textView_date2);

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        mMap = (MapView) mView.findViewById(R.id.map);
        mMap.getMapAsync(this);


        btn_date_pick1 = (Button) mView.findViewById(R.id.btn_date_pick1);
        btn_date_pick1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mOnClick(1);
            }
        });

        btn_date_pick2 = (Button) mView.findViewById(R.id.btn_date_pick2);
        btn_date_pick2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mOnClick(2);
            }
        });

        UpdateNow();//화면에 텍스트뷰에 업데이트 해줌.

        // fragment 3 화면에 예약정보 띄워주기
        initBookList();

        // 예약한 숙소 내역 중 하나를 터치하면 그 위치로 지도가 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                bookFinalInfo a = showBook.get(position);
                Lati = Double.valueOf(a.getLati());
                Longi = Double.valueOf(a.getLongi());
                mapHotel = a.getHotelName();
                mapLoc = a.getHotelLoc();

                mMap = (MapView) mView.findViewById(R.id.map);
                mMap.getMapAsync(Fragment3.this);
            }

        });

        // 예약한 숙소 내역 중 하나를 길게 터치하면 해당하는 예약 취소
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                bookFinalInfo contact = showBook.get(i);

                String hotelName = contact.getHotelName();
                String loc = contact.getHotelLoc();
                String removeID = contact.getReservID();
                cancelReserv(hotelName, loc, removeID);

                initBookList();
                Toast.makeText(mContext, contact.getHotelName() + " " + contact.getEmail(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // textview
        final TextView tv = (TextView) mView.findViewById(R.id.textView1);

        // spinner 선언 후 spinner에서 선택된 값을 textview에 띄움
        Spinner s = (Spinner) mView.findViewById(R.id.spinner1);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                tv.setText("position : " + position +
                        parent.getItemAtPosition(position));
                location = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 위치, 체크인, 체크아웃에 맞는 숙소 정보를 보여주는 activity로 넘어가기 위한 intent
        searchHotel = (Button) mView.findViewById(R.id.searchHotel);
        searchHotel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, showHotel.class) ;
                intent.putExtra("loc", location);
                intent.putExtra("ciy", checkInYear);
                intent.putExtra("cim", checkInMonth);
                intent.putExtra("cid", checkInDay);
                intent.putExtra("coy", checkOutYear);
                intent.putExtra("com", checkOutMonth);
                intent.putExtra("cod", checkOutDay);
                intent.putExtra("guestNum", guestNum);
                intent.putExtra("email_key", email_key);
                startActivity(intent);
            }

        });

        return mView;
    }


    // 예약 내역들을 띄우는 함수
    public void initBookList() {
        HashMap<String, String> map = new HashMap<>();
        Call<getInfoPlease> call = retrofitInterface.executeBookList(map);
        showBook = new ArrayList<bookFinalInfo>();

        call.enqueue(new Callback<getInfoPlease>() {
            @Override
            // server로부터 응답이 돌아오면
            public void onResponse(Call<getInfoPlease> call, Response<getInfoPlease> response) {
                // 정상적인 응답 돌아오면 예약 정보들 listview에 뿌림
                if(response.code() == 200){
                    //Toast.makeText(mContext, "Book List initiation success",
                            //Toast.LENGTH_LONG).show();
                    ArrayList<bookInfo> a = response.body().getBookInfo();
                    for (int i = 0; i < a.size(); i++){
                        ArrayList<reservList> b = a.get(i).getReservList();
                        for (int j = 0; j < b.size(); j++){
                            if (b.get(j).getEmail().equals(email_key)){
                                String hotelName = a.get(i).getHotelName();
                                String hotelID = a.get(i).getId();
                                String hotelLoc = a.get(i).getLoc();
                                String lati = a.get(i).getLati();
                                String longi = a.get(i).getLongi();
                                String reservID = b.get(j).getId();
                                String email = b.get(j).getEmail();
                                String checkIn = b.get(j).getCheckIn();
                                String checkOut = b.get(j).getCheckOut();
                                showBook.add(new bookFinalInfo(hotelName, hotelID, hotelLoc, lati, longi, reservID, email, checkIn, checkOut));
                            }
                        }
                    }
                    adapter = new bookViewAdapter(showBook, mContext);
                    listView.setAdapter(adapter);
                    System.out.println("I'm successful!! Hahahahaha");
                }
                // 비정상적인 이벤트 발생시 (400), 토스트 메시지 띄우기
                else if(response.code()==400){
                    Toast.makeText(mContext, "Something wrong...no ID or Email",
                            Toast.LENGTH_LONG).show();
                }

                else if(response.code()==404){
                    Toast.makeText(mContext, "Removing error occurred",
                            Toast.LENGTH_LONG).show();
                }
            }

            // 에러 발생시
            @Override
            public void onFailure(Call<getInfoPlease> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    // 예약한 숙소 정보를 취소하는 함수
    public void cancelReserv(String hotelName, String loc, String removeID){
        HashMap<String, String> map = new HashMap<>();
        map.put("hotelName", hotelName);
        map.put("loc", loc);
        map.put("removeID", removeID);

        Call<Void> call = retrofitInterface.executeCancelBook(map);

        call.enqueue(new Callback<Void>() {
            @Override
            // server로부터 응답이 돌아오면
            public void onResponse(Call<Void> call, Response<Void> response) {
                // 정상적으로 예약이 취소되면, 토스트메시지 띄우기!
                if(response.code() == 200){
                    Toast.makeText(mContext, "Cancel Successful",
                            Toast.LENGTH_LONG).show();

                    System.out.println("I'm successful!! Hahahahaha");
                }

                // 비정상적인 이벤트 발생시 (400), 토스트 메시지 띄우기
                else if(response.code()==400){
                    Toast.makeText(mContext, "Something wrong...no ID or Email",
                            Toast.LENGTH_LONG).show();
                }
                else if(response.code()==404){
                    Toast.makeText(mContext, "Removing error occurred",
                            Toast.LENGTH_LONG).show();
                }
            }
            // 에러 발생시
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    //체크인, 체크아웃 정보를 받을 달력 띄우기
    public void mOnClick(int v){
        switch(v){
            //체크인 대화상자 버튼이 눌리면 대화상자를 보여줌
            case 1:
                //여기서 리스너도 등록함
                new DatePickerDialog(mContext, mDateSetListener1, mYear,
                        mMonth, mDay).show();
                break;

            //체크아웃 대화상자 버튼이 눌리면 대화상자를 보여줌
            case 2:
                //여기서 리스너도 등록함
                new DatePickerDialog(mContext, mDateSetListener2, mYear,
                        mMonth, mDay).show();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener mDateSetListener1 =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤
                    checkInYear = year;
                    checkInMonth = monthOfYear+1;
                    checkInDay = dayOfMonth;
                    //텍스트뷰의 값을 업데이트함
                    UpdateNow();
                }
            };

    DatePickerDialog.OnDateSetListener mDateSetListener2 =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    //사용자가 입력한 값을 가져온뒤
                    checkOutYear = year;
                    checkOutMonth = monthOfYear+1;
                    checkOutDay = dayOfMonth;
                    //텍스트뷰의 값을 업데이트함
                    UpdateNow();

                }
            };


    //그 달력 밑에 띄우기... 별 의미 없음
    void UpdateNow(){
        textView_Date1.setText(String.format("%d/%d/%d", checkInYear,
                checkInMonth, checkInDay));
        textView_Date2.setText(String.format("%d/%d/%d", checkOutYear,
                checkOutMonth, checkOutDay));

    }

    ///////////////////////구글맵 연습
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng SEOUL = new LatLng(Lati, Longi);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title(mapHotel);
        markerOptions.snippet(mapLoc);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    @Override
    public void onStart() {
        super.onStart();
        mMap.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMap.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMap.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMap.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//액티비티가 처음 생성될 때 실행되는 함수

        if(mMap != null)
        {
            mMap.onCreate(savedInstanceState);
        }


    }

}