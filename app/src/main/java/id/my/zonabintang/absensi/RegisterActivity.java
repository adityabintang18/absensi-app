package id.my.zonabintang.absensi;

import androidx.appcompat.app.AppCompatActivity;
import id.my.zonabintang.absensi.api.ApiClient;
import id.my.zonabintang.absensi.api.ApiInterface;
import id.my.zonabintang.absensi.model.login.Login;
import id.my.zonabintang.absensi.model.register.Register;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText input_user_regis, input_pass_regis, input_name_regis ;
    Button btn_regis;
    String username,password, name;
    ApiInterface apiInterface;
    LottieAnimationView load_salad;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        input_user_regis= findViewById(R.id.input_user_regis);
        input_pass_regis = findViewById(R.id.input_pass_regis);
        input_name_regis = findViewById(R.id.input_name_regis);
        btn_regis = findViewById(R.id.btn_regis);
        btn_regis.setOnClickListener(this);
        load_salad = findViewById(R.id.load_salad);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_regis:

            isAllFieldsChecked = CheckAllFields();
            username = input_user_regis.getText().toString();
            password = input_pass_regis.getText().toString();
            name = input_name_regis.getText().toString();
                if (isAllFieldsChecked) {
                    btn_regis.setVisibility(View.GONE);
                    load_salad.setVisibility(View.VISIBLE);
                    register(username,password,name);
                    break;
                }


        }

    }

    private boolean CheckAllFields() {
        if (input_name_regis.length() == 0) {
            input_name_regis.setError("This field is required");
            return false;
        }

        if (input_user_regis.length() == 0) {
            input_user_regis.setError("This field is required");
            return false;
        }


        if (input_pass_regis.length() == 0) {
            input_pass_regis.setError("Password is required");
            return false;
        } else if (input_pass_regis.length() < 8) {
            input_pass_regis.setError("Password must be minimum 8 characters");
            return false;
        }

        // after all validation return true.
        return true;
    }

    private void register(String username, String password, String name) {

        apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<Register> registerCall = apiInterface.registerResponse(username, password, name);
        registerCall.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.body() != null && response.isSuccessful() && response.body().isStatus()){
                    btn_regis.setVisibility(View.VISIBLE);
                    load_salad.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    btn_regis.setVisibility(View.VISIBLE);
                    load_salad.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                btn_regis.setVisibility(View.VISIBLE);
                load_salad.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this,t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}