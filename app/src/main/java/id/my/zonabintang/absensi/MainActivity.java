package id.my.zonabintang.absensi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_greeting, txt_name_main;
    SessionManager sessionManager;
    String  name ;
    Button btn_logout;
    Calendar calender;
    Integer hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(MainActivity.this);
        if (sessionManager.isLoggedIn() == false){
            moveToLogin();
        }

        txt_greeting = findViewById(R.id.txt_greeting);
        txt_name_main = findViewById(R.id.txt_name_main);
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);

        name = sessionManager.getUserDetail().get(SessionManager.NAME);

        calender = Calendar.getInstance();
        hour = calender.get(Calendar.HOUR_OF_DAY);
        if(hour>0 && hour<10){
            txt_greeting.setText("Selamat Pagi");
        } else if (hour>10 && hour<14){
            txt_greeting.setText("Selamat Siang");
        } else if (hour>14 && hour<18) {
            txt_greeting.setText("Selamat Sore");
        }else {
            txt_greeting.setText("Selamat Malam");
        }
        txt_name_main.setText(name);



    }

    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_logout:
                sessionManager.logoutSession();
                moveToLogin();
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.actionLogout:
//                sessionManager.logoutSession();
//                moveToLogin();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}