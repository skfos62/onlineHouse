package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class find_pw extends AppCompatActivity {
    FrameLayout find_pw_frame;
    EditText find_pw_idInput;
    EditText find_pw_emailInput;
    TextView find_id_result;
    Button find_pw_btn;
    Button find_login_btn;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        find_pw_idInput = findViewById(R.id.find_pw_idInput);
        find_pw_emailInput = findViewById(R.id.find_pw_emailInput);

        find_pw_frame = findViewById(R.id.find_pw_frame);
        find_id_result = findViewById(R.id.find_id_result);

        find_login_btn = findViewById(R.id.find_login_btn);

        // 임시 비밀번호 생성되는지 확인
        getRandomPassword(6);
        Log.i("임시비밀번호",getRandomPassword(6));

        // 메일로 비밀번호 찾기를 누르면 프레임 레이아웃이 보여지게
        find_pw_btn = findViewById(R.id.find_pw_btn);
        find_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_pw_frame.setVisibility(View.VISIBLE);
                // php 서버에 보내게
                TAG = "/PHPMailer-master/phpmail_test_pwe.php";
                String id = find_pw_idInput.getText().toString();
                String email = find_pw_emailInput.getText().toString();
                String pwe = getRandomPassword(6);

                Log.i("되는겨?1", email);
                findpwData task = new findpwData();
                task.execute("http://" +"52.79.83.19"+ TAG, email, id, pwe);
            }
        });

        // 로그인 버튼 누르면 로그인 하는 화면으로가게
        find_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),login_real.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 비밀번호 찾는 로직
     * 1. 이메일과 아이디, 여섯개의 임시 비밀번호를를 보낸다.
     * 2. 첫번째로 php 에서 해당하는 아이디로 아이디가 있는지 확인한다.
     * 3. 아이디가 있다면 해당하는 아이디의 비밀번호를 임시 비밀번호로 수정한다(업데이트사용)
     * 4. 그리고 함께 보낸 메일주소로 임시 비밀번호를 담아서 보낸다.
     * 5. 임시비밀번호로 로그인이 되는지 확인한다.
     */

    // -------------------------------------------------------------------------------------
    // 비밀번호 찾을때 나오는 쓰레드
    // -------------------------------------------------------------------------------------
    class findpwData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(find_pw.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            // 결과값을 여기서 띄워줌
            Log.i("쓰레드 결과값", result);
            if(result.contains("실패")){
                find_id_result.setText("회원님의 아이디가 없습니다.");
            } else {
                find_id_result.setText("임시 비밀번호가 발송되었습니다. 로그인해주세요.");
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            String id = (String)params[2];
            String pwe = (String)params[3];
            String serverURL = (String)params[0];

            String postParameters = "email=" + email + "&id=" + id + "&pwe=" + pwe;
            Log.i("값이들어가나1", postParameters);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.i("값이들어가나1", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                httpURLConnection.disconnect();


                return sb.toString();


            } catch (Exception e) {


                return new String("Error: " + e.getMessage());
            }

        }


    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    // -------------------------------------------------------------------------------------
    // 임시 비밀번호 생성하는 로직
    // -------------------------------------------------------------------------------------
    public static String getRandomPassword(int length) {
        String[] passwords = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d"};
        StringBuilder builder = new StringBuilder("");

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            builder.append(passwords[random.nextInt(passwords.length)]);
        }
        String pwe = builder.toString();

        return builder.toString();


    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


}
