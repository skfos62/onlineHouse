package com.example.house2.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.house2.R;

public class editPw extends AppCompatActivity {

    String editpwId;
    String editpw;
    String editnickname;
    String editmail;
    String editimage;
    String editname;

    //입력한 비밀번호
    EditText oldEdit_pw_idInput;

    Button edit_pw_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pw);

        // mypage 에서 넘어온 인텐트
        // 현재 로그인한 사람의 정보를 받아온다.
        Intent intent = getIntent();
        editpwId = intent.getExtras().getString("id");
        editname =  intent.getExtras().getString("name");
        editpw =  intent.getExtras().getString("pw");
        editnickname =  intent.getExtras().getString("nickname");
        editmail =  intent.getExtras().getString("mail");
        editimage =  intent.getExtras().getString("image");

//        intent.putExtra("id",staticUser.getId());
//        intent.putExtra("name",staticUser.getName());
//        intent.putExtra("nickname",staticUser.getNickname());
//        intent.putExtra("mail",staticUser.getEmail());
//        intent.putExtra("image",staticUser.getImage());
//        intent.putExtra("pw",staticUser.getPw());
        oldEdit_pw_idInput = findViewById(R.id.oldEdit_pw_idInput);
        edit_pw_btn = findViewById(R.id.edit_pw_btn);
        edit_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ckPw = oldEdit_pw_idInput.getText().toString();
                Log.i("비밀번호확이", "onClick: " +ckPw);
                Log.i("비밀번호확이", "onClick: " +editpw);
                if(ckPw.equals(editpw)){
                    Intent intent = new Intent(getApplicationContext(), edit_pw_new.class);
                    intent.putExtra("id",editpwId);
                    intent.putExtra("name",editname);
                    intent.putExtra("nickname",editnickname);
                    intent.putExtra("mail",editmail);
                    intent.putExtra("image",editimage);
                    intent.putExtra("pw",editpw);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(editPw.this, "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });







    }
}
