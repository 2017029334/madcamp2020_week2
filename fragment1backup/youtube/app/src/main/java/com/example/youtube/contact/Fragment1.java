package com.example.youtube.contact;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.youtube.MainActivity;
import com.example.youtube.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    private ArrayList<Customer> list;
    private ArrayList<Customer> arraylist;
    private ListView listView;
    private ListViewAdapter_mod adapter;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.19.243:8980";

    public Fragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_1, container, false);

        listView = (ListView) mView.findViewById(R.id.listview1);
        list = new ArrayList<Customer>();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        initiation();

        mView.findViewById(R.id.login).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handleLoginDialog();
            }
        });

        mView.findViewById(R.id.signup).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handleSignupDialog();

            }
        });

        return mView;
    }

    private void handleLoginDialog() {

        View view = getLayoutInflater().inflate(R.layout.login_dialog, null);
        Context loginContext = getContext();

        final AlertDialog.Builder builder = new AlertDialog.Builder(loginContext);

        builder.setView(view).show();

        Button loginBtn = view.findViewById(R.id.login);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        //////////////실제 로그인 버튼을 눌렀을 때//////////
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();

                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Call<JsonObject> call = retrofitInterface.executeLogin(map);

                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.code() == 200){
                            System.out.println("class name: " + response.body().getClass());
                            JsonParser Parser = new JsonParser();
                            JsonObject jsonObj = (JsonObject) Parser.parse(response.body().toString());
                            JsonObject object = (JsonObject) jsonObj.get("result");
                            //LoginResult result = response.body();

                            //System.out.println(result);
                            //JsonObject object = (JsonObject) memberArray;
                            String name = object.get("name").toString();
                            String email = object.get("email").toString();
                            //String email = result.getEmail();

                            System.out.println(name);
                            System.out.println(email);

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                            builder1.setTitle(name);
                            builder1.setMessage(email);

                            builder1.show();
                        }
                        else if(response.code()==404){
                            System.out.println("You are wrong");
                            Toast.makeText(mContext, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void handleSignupDialog() {
        View view = getLayoutInflater().inflate(R.layout.signup_dialog, null);
        Context signupContext = getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(signupContext);
        builder.setView(view).show();

        Button signupBtn = view.findViewById(R.id.signup);
        final EditText nameEdit = view.findViewById(R.id.nameEdit);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        signupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                HashMap<String, String> map = new HashMap<>();

                map.put("name", nameEdit.getText().toString());
                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            Toast.makeText(mContext, "Signed up successfully",
                                    Toast.LENGTH_LONG).show();
                            initiation();
                            System.out.println("I'm successful!! Hahahahaha");
                        }
                        else if(response.code()==400){
                            Toast.makeText(mContext, "Already registered",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(mContext, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void initiation(){

        Call<CustomersResponse> call = retrofitInterface.executeInit();

        call.enqueue(new Callback<CustomersResponse>() {
            @Override
            public void onResponse(Call<CustomersResponse> call, Response<CustomersResponse> response) {
                System.out.println("hey!");
                if(response.code() == 200){
                    list = response.body().getUsers();

                    arraylist = new ArrayList<Customer>();
                    arraylist.addAll(list);
                    adapter = new ListViewAdapter_mod(list, mContext);

                    listView.setAdapter(adapter);
                }
                else if(response.code()==404){
                    Toast.makeText(mContext, "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CustomersResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(mContext, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}