package com.example.youtube.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.youtube.MainActivity;
import com.example.youtube.R;
import com.example.youtube.contact.Fragment1;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.youtube.R.id.fb_login;

public class LoginActivity extends AppCompatActivity {
    Fragment1 fragment1;

    //facebook 관련 변수
    private LoginButton btn_facebook_login;
    private CallbackManager callbackManager;
    LoginManager loginManager;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.19.243:8980"; //server address and port number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
//        AppEventsLogger.activateApp(this);

        setContentView(R.layout.login);

        /*
        자체 로그인
         */
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginDialog();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignupDialog();
            }
        });

        /*
        페이스북 로그인
        */
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance(); //로그인 매니저 등록

        btn_facebook_login = (LoginButton) findViewById(fb_login);
        btn_facebook_login.setReadPermissions(Arrays.asList("public_profile", "email"));

        //callback registration
        btn_facebook_login.registerCallback(callbackManager, new FacebookCallback<com.facebook.login.LoginResult>() {

            //로그인 성공
            @Override
            public void onSuccess(com.facebook.login.LoginResult loginResult) {

                //Access 토큰 값을 가져온다
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                String access_Token = accessToken.getToken();
                Log.d("json control(new token) : ", access_Token);

                //로그인 정보 가져오기
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    if(object.has("email")){
                                        Log.e("facebook email", object.getString("email"));
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                //parameter bundle을 추가해서 필요한 요소들 받아오기
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

                //MainActivity 시작
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            //로그인 취소
            @Override
            public void onCancel() {
            }

            //로그인 실패
            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }
        });

    } //onCreate

    //자체 로그인
    private void handleLoginDialog() {
        Button loginBtn = findViewById(R.id.login);
        final EditText emailEdit = findViewById(R.id.emailEdit);
        final EditText passwordEdit = findViewById(R.id.passwordEdit);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();

                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Call<LoginResult> call = retrofitInterface.executeLogin(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        //success of login
                        if (response.code() == 200) {
                            LoginResult result = response.body();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class) ;
                            intent.putExtra("email", emailEdit.getText().toString());
                            startActivity(intent) ;

                        }
                        //failure of login
                        else if (response.code() == 400) {
                            Toast.makeText(LoginActivity.this, "Wrong password",
                                    Toast.LENGTH_LONG).show();
                        }

                        else if (response.code() == 404) {
                            Toast.makeText(LoginActivity.this, "Log in failed",
                                    Toast.LENGTH_LONG).show();
                        }

                        else if (response.code() == 300) {
                            Toast.makeText(LoginActivity.this, "You are not registerd",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    //회원가입
    private void handleSignupDialog() {

        View view = getLayoutInflater().inflate(R.layout.signup, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        Button signupBtn = view.findViewById(R.id.signup);
        final EditText nameEdit = view.findViewById(R.id.nameEdit);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();

                map.put("name", nameEdit.getText().toString());
                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Call<Void> call = retrofitInterface.executeSignup(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        //success of sigined in
                        if (response.code() == 200) {
                            Toast.makeText(LoginActivity.this,
                                    "Signed up successfully", Toast.LENGTH_LONG).show();
                        }
                        //failure of sigined in (the info exists already)
                        else if (response.code() == 400) {
                            Toast.makeText(LoginActivity.this,
                                    "Already registered", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    //페이스북 로그인 이벤트 등록???
    private void loginFacebook(){
        loginManager.setDefaultAudience(DefaultAudience.FRIENDS);
        loginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        //callback 등록
    //http://blog.naver.com/PostView.nhn?blogId=scw0531&logNo=220840846576&parentCategoryNo=&categoryNo=8&viewDate=&isShowPopularPosts=true&from=search
    }

    //페이스북 로그인 결과
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

}
