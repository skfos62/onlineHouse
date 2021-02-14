package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * 파트너 멤버가입
 * 파트너의 사의 회원들의 정보를 저장하는곳
 * 에디트 텍스트에 입력한 파트너사의 정보를 디비에 저장해야한다.
 *
 *     <유저 디비에 정보를 저장하는 순서>
 *      1. 먼저 안드로이드에서 php로 데이터를 보낸다.
 *          volley를 사용해서 데이터를 보냄
 *      2. php에서 sql문 (insert문)을 이용해서 디비에 데이터를 저장한다.
 *         h_signInPartner.php 로 데이터 보내기
 *     </유저>
 *
 * 그리고 가입을 완료하면 메인화면으로 가게 코딩을 해두어야한다.
 *
 */
public class join_partner extends AppCompatActivity {

    EditText join_partner_id_input;
    EditText join_partner_pw_input;
    EditText join_partner_name_input;
    EditText join_partner_num_input;
    Button join_partner_btn;

    String TAG_login;
    String PartnerId;
    String PartnerPw;
    String PartnerName;
    String PartnerNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_partner);

        // 서버에 정보를 보내는곳

        join_partner_id_input = findViewById(R.id.join_partner_id_input);
        join_partner_pw_input = findViewById(R.id.join_partner_pw_input);
        join_partner_name_input = findViewById(R.id.join_partner_name_input);
        join_partner_num_input = findViewById(R.id.join_partner_num_input);

        join_partner_btn = findViewById(R.id.join_partner_btn);
        join_partner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 서버로 정보를 보내는 메소드를 실행하는 부분
                PartnerId = join_partner_id_input.getText().toString();
                PartnerPw = join_partner_pw_input.getText().toString();
                PartnerName = join_partner_name_input.getText().toString();
                PartnerNum = join_partner_num_input.getText().toString();

                Log.i("인풋텍스트 정보가 잘 들어왔나?", "onClick: " + PartnerId);

                // 이게 없으면 Appherper가 null 이라고 뜬다
                // 이것의 뜻은 뭘까?
                if (AppHelper.requestQueue == null)
                    AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

                // php로 사용자(파트너)의 정보를 php로 보내는 메소드
                sendRequest("http://13.125.62.22/h_signInPartner.php");
            }

        });
    }
    // -------------------------------------------------------------------------------------
    // http 통신하는 부분 (로그인하는부분 / volley)
    // -------------------------------------------------------------------------------------

    public void sendRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "가입 성공", Toast.LENGTH_LONG).show();
                        // 로그인 되나 안되나
                        // 확인하는곳
                        Log.i("php리스폰값 확인", "onResponse: " + response);
                        // 여기서 성공적으로 db로 데이터 전송이 끝나고 나면 로그인하는 페이지가 나오게해야함
                        // 가입 완료하고 로그인 액티비티 열기
                        Intent intent = new Intent(getApplicationContext(),login.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                        Log.i("에러확인", "onErrorResponse: " + error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //여기다가 서버에 보낼 데이터값을 넣어준다.
                // 키 밸류 형태로 해쉬맵으로 넣어준다.
                // 이것은 php에서 변수 - 데이터값 의 형태로 사용될 데이터
                params.put("PartnerId", PartnerId);
                params.put("PartnerPw", PartnerPw);
                params.put("PartnerName", PartnerName);
                params.put("PartnerNum", PartnerNum);
                params.put("userType", "partner");
                return params;
            }
        };

        // 이전 결과가 있더라도 새로 요청
        request.setShouldCache(false);

        AppHelper.requestQueue.add(request);

        Toast.makeText(getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();

    }
}
