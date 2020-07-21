package com.example.youtube.booking;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragment3 extends Fragment {

    Context mContext;
    View mView;
    private String email_key;
    private String location;
    private String checkInDate;
    private String checkOutDate;
    private int guestNum = 4;
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
    int mYear, mMonth, mDay, mHour, mMinute;

    //출처: https://gakari.tistory.com/entry/안드로이드-날짜-대화상자-시간-대화상자-만들기 [가카리의 공부방]



    public Fragment3() {
    }

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

        btn_date_pick1 = (Button) mView.findViewById(R.id.btn_date_pick1);
        btn_date_pick1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mOnClick(1);
                //InitializeView1();
               //InitializeListener1();
            }
        });

        btn_date_pick2 = (Button) mView.findViewById(R.id.btn_date_pick2);
        btn_date_pick2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mOnClick(2);
                //InitializeView2();
                //InitializeListener2();
            }
        });
        UpdateNow();//화면에 텍스트뷰에 업데이트 해줌.

        // fragment 3 화면에 예약정보 띄워주기
        initBookList();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override

            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                bookFinalInfo contact = showBook.get(i);

                String hotelName = contact.getHotelName();
                String loc = contact.getHotelLoc();
                String removeID = contact.getReservID();
                cancelReserv(hotelName, loc, removeID);


//                arraylist.remove(i);
//                adapter.notifyDataSetChanged();
//
                initBookList();
                Toast.makeText(mContext, contact.getHotelName() + " " + contact.getEmail(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        final TextView tv = (TextView) mView.findViewById(R.id.textView1);
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

    private void initBookList() {
        HashMap<String, String> map = new HashMap<>();
        Call<getInfoPlease> call = retrofitInterface.executeBookList(map);

        call.enqueue(new Callback<getInfoPlease>() {
            @Override
            // server로부터 응답이 돌아오면
            public void onResponse(Call<getInfoPlease> call, Response<getInfoPlease> response) {
                // 정상적으로 서버에 등록되었다면(200), 토스트 메시지 띄우고 연락처 갱신
                if(response.code() == 200){
                    Toast.makeText(mContext, "Book List initiation success",
                            Toast.LENGTH_LONG).show();
                    ArrayList<bookInfo> a = response.body().getBookInfo();
                    for (int i = 0; i < a.size(); i++){
                        ArrayList<reservList> b = a.get(i).getReservList();
                        for (int j = 0; j < b.size(); j++){
                            if (b.get(j).getEmail().equals(email_key)){
                                String hotelName = a.get(i).getHotelName();
                                String hotelID = a.get(i).getId();
                                String hotelLoc = a.get(i).getLoc();
                                String reservID = b.get(j).getId();
                                String email = b.get(j).getEmail();
                                String checkIn = b.get(j).getCheckIn();
                                String checkOut = b.get(j).getCheckOut();
                                showBook.add(new bookFinalInfo(hotelName, hotelID, hotelLoc, reservID, email, checkIn, checkOut));
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
                // 정상적으로 서버에 등록되었다면(200), 토스트 메시지 띄우고 연락처 갱신
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

    public void mOnClick(int v){
        switch(v){
            //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌
            case 1:
                //여기서 리스너도 등록함
                new DatePickerDialog(mContext, mDateSetListener1, mYear,
                        mMonth, mDay).show();
                break;

            //시간 대화상자 버튼이 눌리면 대화상자를 보여줌
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

    void UpdateNow(){
        textView_Date1.setText(String.format("%d/%d/%d", checkInYear,
                checkInMonth, checkInDay));
        textView_Date2.setText(String.format("%d/%d/%d", checkOutYear,
                checkOutMonth, checkOutDay));
    }
}