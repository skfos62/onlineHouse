package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class join_select extends AppCompatActivity {

    Button general_Member;
    Button partner_Member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_select);

        general_Member = findViewById(R.id.general_Member);
        partner_Member = findViewById(R.id.partner_Member);

        general_Member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 일반회원가입
                Intent intent = new Intent(getApplicationContext(),join.class);
                startActivity(intent);
                finish();
            }
        });

        partner_Member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 파트너 회원사
                Intent intent = new Intent(getApplicationContext(),join_partner.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
