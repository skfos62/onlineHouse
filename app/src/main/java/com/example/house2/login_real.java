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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.house2.partner.partner_Main;

import java.util.HashMap;
import java.util.Map;

public class login_real extends AppCompatActivity {
    EditText login_id_input;
    EditText login_pw_input;
    Button login_btn;
    TextView login_findid;
    TextView login_findpw;
    private static String TAG_login;
    public static String loginIdInfo;
    public static String loginIdInfoNick;
    String id;
    String pw;
    String loginId, loginPwd;

    //로그인한 사용자 확인하기
    public static String realIDck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_real);


        login_id_input = findViewById(R.id.login_id_input);
        login_pw_input = findViewById(R.id.login_pw_input);

        // 아이디 비밀번호 찾기
        login_findid = findViewById(R.id.login_findid);
        login_findpw = findViewById(R.id.login_findpw);


        // 자동로그인 관련 시작 ------------------------------------------------------------------------
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줍니다.
        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);
        Log.i("자동로그인", "onCreate: " + loginId + " " + loginPwd);


        if (loginId != null && loginPwd != null) {
            Toast.makeText(login_real.this, loginId + "님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(login_real.this, MainActivity.class);
            intent.putExtra("name", loginId);
            intent.putExtra("pw", loginPwd);
            startActivity(intent);
            finish();

        } else if (loginId == null && loginPwd == null) {
            //로그인 하는 버튼 ------------------------------------------------------------------------


            login_btn = findViewById(R.id.login_btn);

            login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TAG_login = "/h_logIn.php";
                    id = login_id_input.getText().toString();
                    pw = login_pw_input.getText().toString();
                    loginIdInfo = id;
                    sendRequest("http://13.125.62.22/h_logIn.php");

                    // httpconnection 인 부분
//                    InsertData task2 = new InsertData();
////                    task2.execute("http://" +"13.125.62.22"+ TAG_login, id, pw);
//                    task2.execute("http://13.125.62.22/h_logIn.php", id, pw);
                }
            });
            if (AppHelper.requestQueue == null)
                AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());


            //로그인 하는 버튼 ------------------------------------------------------------------------
        }

        // 자동로그인 관련  ----------끝----------------------------------------------------------------

        // 아이디 찾기 누르면 나오는 버튼
        login_findid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_real.this, findId.class);
                startActivity(intent);
            }
        });

        // 비밀번호 찾기 누르면 나오는 버튼
        login_findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_real.this, find_pw.class);
                startActivity(intent);
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
                        // 서버와 통신이 완료(성공)되고 응답을 받아오는곳
                        // 확인하는곳
                        Log.i("php리스폰값 확인", "onResponse: " + response);
                        String[] responseList = response.split("-");

                        if(responseList[0].equals("Y")){
                            // 쉐어드에 자동로그인할 값 저장하기
                            SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                            //auto의 loginId와 loginPwd에 값을 저장해 줍니다.
                            SharedPreferences.Editor autoLogin = auto.edit();
                            autoLogin.putString("inputId", id);
                            autoLogin.putString("inputPwd", pw);
                            //꼭 commit()을 해줘야 값이 저장됩니다 ㅎㅎ
                            autoLogin.commit();
                            // 인텐트로 정보 보내기
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("name",id);
                            intent.putExtra("pw",pw);
                            startActivity(intent);
                            finish();
                       } else if (responseList[0].equals("P")){
                            Log.i("파트너사 로그인", "onResponse: 파트너사 로그인");
                            Intent intent = new Intent(getApplicationContext(), partner_Main.class);
                            loginIdInfoNick = responseList[1];
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"아이디 및 비밀번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                        }
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
                params.put("id", id);
                params.put("pw", pw);
                return params;
            }
        };

        // 이전 결과가 있더라도 새로 요청
        request.setShouldCache(false);

        AppHelper.requestQueue.add(request);
//

    }
}


// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



