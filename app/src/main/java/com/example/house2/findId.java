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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class findId extends AppCompatActivity {

    Button find_id_btn;
    FrameLayout find_id_frame;
    String TAG_findid;
    EditText find_id_emailInput;
    TextView find_id_result;

    Button find_login_btn;
    Button find_code_btn;
    Button find_pw_move_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        find_id_btn = findViewById(R.id.find_id_btn);
        find_id_frame = findViewById(R.id.find_id_frame);
        find_id_emailInput = findViewById(R.id.find_id_emailInput);
        find_id_result = findViewById(R.id.find_id_result);

        // 로그인하러 가기 버튼
        find_login_btn = findViewById(R.id.find_login_btn);
        find_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 액티비티 열기
                Intent intent = new Intent(getApplicationContext(),login_real.class);
                startActivity(intent);
                finish();
            }
        });
        // 아이디 찾으러가기 버튼
        find_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find_id_frame.setVisibility(View.VISIBLE);

                // 이메일을 php에 보내서 확인 하는거
                TAG_findid = "/h_findId.php";
                String email = find_id_emailInput.getText().toString();

                findData task2 = new findData();
                task2.execute("http://" +"52.79.83.19"+ TAG_findid, email);
            }
        });

        // 비밀번호 찾으러 가기 버튼
        find_pw_move_btn = findViewById(R.id.find_pw_move_btn);
        find_pw_move_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비밀번호 찾기 액티비티 열기
                Intent intent = new Intent(getApplicationContext(),find_pw.class);
                startActivity(intent);
                finish();
            }
        });

    }

    /**
     * 1. 사용자가 가입할때 사용한 이메일을 작성한다.
     * 2. 이메일을 php로 보내어서 mysql 에 해당하는 아이디가 있는지 확인한다.
     * 3. 해당하는 아이디가 있으면 텍스트뷰에 해당하는 아이디를 띄우고 없으면 안띄운다.
     * ps- 이메일 주소로 아이디를 찾는 php가 작성이 되어있어야 한다.
     */

    // -------------------------------------------------------------------------------------
    // 아이디 비밀번호 할때 나오는 쓰레드
    // -------------------------------------------------------------------------------------
    class findData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(findId.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            // 결과값을 여기서 띄워줌
            Log.i("쓰레드 결과값", result);
            if(result.equals("N")){
                find_id_result.setText("아이디가 없습니다.");
            } else {
                find_id_result.setText(result);
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String email = (String)params[1];
            String serverURL = (String)params[0];
            String postParameters = "email=" + email ;
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

}
