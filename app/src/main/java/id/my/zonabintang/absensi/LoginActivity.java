package id.my.zonabintang.absensi;

import androidx.appcompat.app.AppCompatActivity;
import id.my.zonabintang.absensi.api.ApiClient;
import id.my.zonabintang.absensi.api.ApiInterface;
import id.my.zonabintang.absensi.model.login.Login;
import id.my.zonabintang.absensi.model.login.LoginData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText input_user_login, input_pass_login ;
    Button btn_login;
    String username,password;
    TextView txt_new_account;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    LottieAnimationView load_salad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        input_user_login = findViewById(R.id.input_user_login);
        input_pass_login = findViewById(R.id.input_pass_login);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        txt_new_account = findViewById(R.id.txt_new_account);
        txt_new_account.setOnClickListener(this);
        load_salad = findViewById(R.id.load_salad);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                username = input_user_login.getText().toString();
                password = input_pass_login.getText().toString();
                btn_login.setVisibility(View.GONE);
                txt_new_account.setVisibility(View.GONE);
                load_salad.setVisibility(View.VISIBLE);
                login(username,password);
                break;
            case R.id.txt_new_account:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void login(String username, String password) {

        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<Login> loginCall = apiInterface.loginResponse(username, password);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                if (response.body() != null && response.isSuccessful() && response.body().isStatus()){

                    sessionManager = new SessionManager(LoginActivity.this);
                    LoginData loginData = response.body().getData();
                    sessionManager.createLoginSession(loginData);
                    btn_login.setVisibility(View.VISIBLE);
                    txt_new_account.setVisibility(View.VISIBLE);
                    load_salad.setVisibility(View.GONE);

                    Toast.makeText(LoginActivity.this, response.body().getData().getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    btn_login.setVisibility(View.VISIBLE);
                    txt_new_account.setVisibility(View.VISIBLE);
                    load_salad.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                btn_login.setVisibility(View.VISIBLE);
                txt_new_account.setVisibility(View.VISIBLE);
                load_salad.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}