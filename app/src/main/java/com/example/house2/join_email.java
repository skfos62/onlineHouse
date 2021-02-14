package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class join_email extends AppCompatActivity {
    Button emailAuth_btn;
    EditText emailAuth_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_email);
        // 인텐트 받기
        Intent intent = getIntent();
        final String num = intent.getExtras().getString("num2");
        Log.i("인텐트로 난수넘어왔니", num);
        emailAuth_number = findViewById(R.id.emailAuth_number);
        Log.i("뭘입력했니1", "왜값이 안보이");


        // 난수랑 에디트 텍스트에 적은 내용이랑 비교해서 일치하면
        // 토스트로 인증이 완료되었습니다. 라고 뜨게 하기

        emailAuth_btn = findViewById(R.id.emailAuth_btn);
            emailAuth_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String numStr = emailAuth_number.getText().toString();
                    Log.i("뭘입력했니", numStr);
                    if(num.equals(numStr)) {
                        Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_LONG).show();
                        // 인증성공 했다고 메인 액티비티에 결과값 보내기
                        Intent intent = new Intent(join_email.this, join.class);
                        intent.putExtra("email","성공");
                        setResult(RESULT_OK, intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "실패ㅠ", Toast.LENGTH_LONG).show();
                    }
                }
            });







    }
}
