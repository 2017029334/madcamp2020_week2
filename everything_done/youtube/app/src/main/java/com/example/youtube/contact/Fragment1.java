package com.example.youtube.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.youtube.MainActivity;
import com.example.youtube.R;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Fragment1 extends Fragment {

    Context mContext;
    View mView;
    private String email_key;
    private String userName;
    private ArrayList<Customer> list;
    private ArrayList<Customer> arraylist;
    private ListView listView;
    private ListViewAdapter_mod adapter;
    private EditText editSearch;
    private Retrofit retrofit;
    private contactRetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.19.243:8980";

    public Fragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 연락처 fragment에 대한 context와 view 받아오기
        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_1, container, false);

        editSearch = (EditText) mView.findViewById(R.id.editTextFilter);

        // MainActivity에서 넘겨준 key로 사용할 email 값 선언
        email_key = ((MainActivity) getActivity()).getEmail_key();
        userName = ((MainActivity) getActivity()).getUserName();

        // 연락처들을 담을 listView와 list를 선언
        listView = (ListView) mView.findViewById(R.id.listview1);
        list = new ArrayList<Customer>();

        // retrofit을 이용하여 서버 연결 설정
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(contactRetrofitInterface.class);

        // fragment1이 실행되자마자 연락처 목록 갱신
        initiation();

//        mView.findViewById(R.id.login).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                handleLoginDialog();
//            }
//        });

        // add라는 버튼이 눌리면 연락처를 추가하는 handleAddDialog() 호출
        mView.findViewById(R.id.add).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handleAddDialog();
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(mContext, CustomersClicked.class);
                intent.putExtra("name", list.get(position).getName());
                //intent.putExtra("gender", list.get(position).getGender());
                intent.putExtra("phone", list.get(position).getPhoneNum());
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                Customer contact = list.get(i);
                String removeID = contact.getId();

                HashMap<String, String> map = new HashMap<>();
                map.put("email", email_key);
                map.put("removeID", removeID);

                Call<Void> call = retrofitInterface.executeRemoveDialog(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    // server로부터 응답이 돌아오면
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // 정상적으로 서버에 등록되었다면(200), 토스트 메시지 띄우고 연락처 갱신
                        if(response.code() == 200){
                            Toast.makeText(mContext, "Removed Successful",
                                    Toast.LENGTH_LONG).show();
                            initiation();
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

//                arraylist.remove(i);
//                adapter.notifyDataSetChanged();
//
                Toast.makeText(mContext, contact.getId(), Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        return mView;
    }



    // 연락처 db에 추가하는 함수
    private void handleAddDialog() {
        // 연락처 추가하는 팝업의 view와 context 가져오기
        View view = getLayoutInflater().inflate(R.layout.add_dialog, null);
        Context signupContext = getContext();

        // AlertDialog로 팝업창 뜨게 함
        AlertDialog.Builder builder = new AlertDialog.Builder(signupContext);
        builder.setView(view).show();

        // 각 button과 editText를 찾아서 선언
        Button signupBtn = view.findViewById(R.id.signup);
        final EditText nameEdit = view.findViewById(R.id.nameEdit);
        //final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.phoneNumEdit);

        // 연락처 추가하는 버튼이 눌리면
        signupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Hashmap에 key로 쓰일 email, 정보 저장을 위한 사람이름과 전화번호를 저장
                HashMap<String, String> map = new HashMap<>();
                map.put("email", email_key);
                map.put("nameContact", nameEdit.getText().toString());
                //map.put("email", emailEdit.getText().toString());
                map.put("phoneNum", passwordEdit.getText().toString());


                // map을 담은 call을 선언한 뒤 server로 전송
                Call<Void> call = retrofitInterface.executeAddDialog(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    // server로부터 응답이 돌아오면
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // 정상적으로 서버에 등록되었다면(200), 토스트 메시지 띄우고 연락처 갱신
                        if(response.code() == 200){
                            Toast.makeText(mContext, "Signed up successfully",
                                    Toast.LENGTH_LONG).show();
                            initiation();
                            System.out.println("I'm successful!! Hahahahaha");
                        }
                        // 비정상적인 이벤트 발생시 (400), 토스트 메시지 띄우기
                        else if(response.code()==400){
                            Toast.makeText(mContext, "Already registered",
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
        });
    }

    // 연락처를 갱신하는 함수
    private void initiation(){

        // Hashmap 선언 후 key로 쓰일 이메일 값만 담아서 보냄
        HashMap<String, String> map = new HashMap<>();

        map.put("email", email_key);
        Call<CustomersResponse> call = retrofitInterface.executeInit(map);

        call.enqueue(new Callback<CustomersResponse>() {
            @Override
            // server로부터 응답이 돌아오면
            public void onResponse(Call<CustomersResponse> call, Response<CustomersResponse> response) {
                System.out.println("hey!");
                // 정상적으로 연락처 정보가 도착 시(200), 연락처 갱신
                if(response.code() == 200){
                    //CustomersResponse list = response.body();
                    if (response.body() != null) {
                        list = response.body().getContacts();

                        arraylist = new ArrayList<Customer>();
                        arraylist.addAll(list);
                        adapter = new ListViewAdapter_mod(list, mContext);

                        listView.setAdapter(adapter);
                    }
                }
                // 비정상적인 이벤트 발생시 (404), 토스트 메시지 띄우기
                else if(response.code()==404){
                    Toast.makeText(mContext, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }
            }

            // 에러 발생시
            @Override
            public void onFailure(Call<CustomersResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void search(String charText){
        list.clear();

        if(charText.length()==0){
            list.addAll(arraylist);
        }

        else
        {
            for(int i = 0;i<arraylist.size();i++){
                if (arraylist.get(i).getName().toLowerCase().contains(charText)||arraylist.get(i).getPhoneNum().toLowerCase().contains(charText)){
                    list.add(arraylist.get(i));
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

//    private void handleLoginDialog() {
//
//        View view = getLayoutInflater().inflate(R.layout.login_dialog, null);
//        Context loginContext = getContext();
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(loginContext);
//
//        builder.setView(view).show();
//
//        Button loginBtn = view.findViewById(R.id.login);
//        final EditText emailEdit = view.findViewById(R.id.emailEdit);
//        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);
//
//        //////////////실제 로그인 버튼을 눌렀을 때//////////
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                HashMap<String, String> map = new HashMap<>();
//
//                map.put("email", emailEdit.getText().toString());
//                map.put("password", passwordEdit.getText().toString());
//
//                Call<JsonObject> call = retrofitInterface.executeLogin(map);
//
//                call.enqueue(new Callback<JsonObject>() {
//                    @Override
//                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                        if(response.code() == 200){
//                            System.out.println("class name: " + response.body().getClass());
//                            JsonParser Parser = new JsonParser();
//                            JsonObject jsonObj = (JsonObject) Parser.parse(response.body().toString());
//                            JsonObject object = (JsonObject) jsonObj.get("result");
//                            //LoginResult result = response.body();
//
//                            //System.out.println(result);
//                            //JsonObject object = (JsonObject) memberArray;
//                            String name = object.get("name").toString();
//                            String email = object.get("email").toString();
//                            //String email = result.getEmail();
//
//                            System.out.println(name);
//                            System.out.println(email);
//
//                            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
//                            builder1.setTitle(name);
//                            builder1.setMessage(email);
//
//                            builder1.show();
//                        }
//                        else if(response.code()==404){
//                            System.out.println("You are wrong");
//                            Toast.makeText(mContext, "Wrong Credentials",
//                                    Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<JsonObject> call, Throwable t) {
//                        Toast.makeText(mContext, t.getMessage(),
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//        });
//    }
//
}