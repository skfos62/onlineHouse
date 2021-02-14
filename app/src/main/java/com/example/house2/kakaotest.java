package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class kakaotest extends AppCompatActivity {
    String strNickname, strProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakaotest);


        Log.e("카카오", "11");
            TextView tvNickname = findViewById(R.id.tvNickname);
//            TextView tvProfile = findViewById(R.id.tvProfile);
        Log.e("카카오", "12");
            Intent intent = getIntent();
            strNickname = intent.getStringExtra("name");
            strProfile = intent.getStringExtra("profile");
        Log.e("카카오", "13");
            tvNickname.setText(strNickname);
        Log.e("카카오", "14");
            Intent intent2 = new Intent(kakaotest.this, MainActivity.class);
            startActivity(intent2);
        Log.e("카카오", "15");
//            tvProfile.setText(strProfile);

    }
}
