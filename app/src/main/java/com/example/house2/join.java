package com.example.house2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class join extends AppCompatActivity {

    FrameLayout join_code_frame;
    MaterialButton join_num_btn, join_code_btn;
    EditText join_num_input;
    EditText join_code_input;
    EditText join_id_input;
    EditText join_pw_input;
    EditText join_name_input;
    MaterialButton join_btn;

    FirebaseAuth mAuth;
    String codeSent;

    private static String IP_ADDRESS = "52.79.83.19";
    private static String TAG;

    // 이메일결과 수신 (startforresult)
    final int INTENT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        // 회원가입 db 연동 로직
//        1. 데이터 베이스를 new 생성한다
//        2. 아톰으로 회원정보를 넣는 php 를 생성하여서 서버에 업로드를 시킨다
//        3. 안드로이드에서 해당 쿼리가 담겨져 있는 php 주소를 불러온다.

        join_code_frame = findViewById(R.id.join_code_frame);
        join_num_btn = findViewById(R.id.join_num_btn);
        join_code_btn = findViewById(R.id.join_code_btn);
        // 휴대폰번호 입력하는 창
        join_num_input = findViewById(R.id.join_num_input);
        // 아이디 입력창
        join_id_input = findViewById(R.id.join_id_input);
        // 비밀번호 입력창
        join_pw_input = findViewById(R.id.join_pw_input);
        // 닉네임 입력창
        join_name_input = findViewById(R.id.join_name_input);

        //  휴대폰 번호로 온 코드 입력받는창
        join_code_input = findViewById(R.id.join_code_input);

        // 가입하기 눌렀을때 나오는 버튼
        join_btn = findViewById(R.id.join_btn);

        // 제일처음에는 가입완료가 비활성화가 되어있다가
        // 조건을 다 만족하면 활성화가 되게 수정하자.



        // 메일인증 누르면 나오는거----------------------------------------------------------------------
        // 서버 php에 연결하는 메소드
        join_num_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TAG = "/PHPMailer-master/phpmail_test.php";
                String email = join_num_input.getText().toString();

                // 랜덤난수 생성
                // 랜덤난수 생성하기
                generateNum geNum = new generateNum();
                System.out.println(geNum.excuteGenerate());
                String num2= geNum.excuteGenerate();
                String num3 = num2;

                Log.i("되는겨?1", email);
                InsertData task = new InsertData();
                task.execute("http://" +"52.79.83.19"+ TAG, email, num2);

                // 새로운 액티비티를 연다. 인텐트에 랜덤난수를 담는다
                Intent intent = new Intent(getApplicationContext(),join_email.class);
                intent.putExtra("num2",num3);
                startActivityForResult(intent, INTENT_REQUEST_CODE);

            }
        });

        // 가입하기 누르면 나오는거----------------------------------------------------------------------
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(join_num_input.getText().toString().isEmpty() || join_id_input.getText().toString().isEmpty() || join_pw_input.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"모든 항목 작성 및 인증을 해주세요.",Toast.LENGTH_LONG).show();
                } else {
                    // 어디 php로 보낼지 결정하는 스트링
                    TAG = "/h_signIn.php";
                    String name = join_id_input.getText().toString();
                    String pw = join_pw_input.getText().toString();
                    String email = join_num_input.getText().toString();
                    String nickname = join_name_input.getText().toString();

                    // 어디 php로 데이터를 전달하고 어떤 데이터를 전달할지 정하는곳
                    InsertData2 task = new InsertData2();
                    task.execute("http://" +"52.79.83.19"+ TAG, name, pw,email,nickname );

                    // 가입 완료하고 로그인 액티비티 열기
                    Intent intent = new Intent(getApplicationContext(),login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == INTENT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            String name = data.getStringExtra("email");
            Log.i("이메일 인증 됐는지 확인 ", name);
            join_num_btn.setText("성공");
            join_num_btn.setAlpha(.5f);
            join_num_btn.setClickable(false);
        }

    }

    // -------------------------------------------------------------------------------------
    // 메일 인증 눌렀을때 나오는 메소드
    // -------------------------------------------------------------------------------------
        class InsertData extends AsyncTask <String, Void, String>{
            ProgressDialog progressDialog;

            @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    progressDialog = ProgressDialog.show(join.this,
                            "Please Wait", null, true, true);
                }

                @Override
                protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                Log.i(TAG, "post 보냈나 - " + result);
            }

            @Override
            protected String doInBackground(String... params) {

                String email = (String)params[1];
                String num2 = (String)params[2];
                String serverURL = (String)params[0];
                String postParameters = "email=" + email + "&num2=" + num2;
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
                    Log.i("값이들어가나", email);
                    while((line = bufferedReader.readLine()) != null){
                        sb.append(line);
                    }
                    bufferedReader.close();
                    httpURLConnection.disconnect();


                    return sb.toString();


                } catch (Exception e) {

                    Log.i(TAG, "InsertData: Error ", e);

                    return new String("Error: " + e.getMessage());
                }

            }


        }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    // -------------------------------------------------------------------------------------
    // 가입하기 버튼을 눌렀을때 서버에 데이터가 저장이 되게 해야한다.
    // -------------------------------------------------------------------------------------
    class InsertData2 extends AsyncTask <String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(join.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.i(TAG, "post 보냈나 - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
//            php에 보내는 변수들
//            String name = join_id_input.getText().toString();
//            String pw = join_pw_input.getText().toString();
//            String email = join_num_input.getText().toString();

            String name = (String)params[1];
            String pw = (String)params[2];
            String email = (String)params[3];
            String nickname =(String) params[4];
            String serverURL = (String)params[0];

            String postParameters = "name=" + name + "&pw=" + pw + "&email=" + email+ "&nickname=" + nickname;
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
                Log.i("값이들어가나", email);
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                httpURLConnection.disconnect();


                return sb.toString();


            } catch (Exception e) {

                Log.i(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }


    }




    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



    }

//
//    private void Async_Prepare() {
//        Async_test async_test =new Async_test();
//        async_test.execute("hello","rabbit"); // php 의 변수로 넘겨주는 값
//    }
//
//    class Async_test extends AsyncTask<String,Void,String> {
//        ProgressDialog progressDialog;
//
//        int cnt=0;
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.i("서버전송","서버로 메세지륿 보냈습니다.");
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = ProgressDialog.show(join.this,
//                    "Please Wait", null, true, true);
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//            progressDialog.dismiss();
//            Log.i("onProgress update","서버메세지 가는중"+cnt++);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            HttpURLConnection httpURLConnection = null;
//
//            try{
//                String tmsg = params[0];
//                String tmsg2 = params[1];
//                String data = URLEncoder.encode("tmsg","UTF-8")+"="+URLEncoder.encode(tmsg,"UTF-8");// UTF-8로  설정 실제로 string 상으로 봤을땐, tmsg="String" 요런식으로 설정 된다.
//                data+= "&"+ URLEncoder.encode("tmsg2","UTF-8")+"="+URLEncoder.encode(tmsg2,"UTF-8");
//
////                String data2 = "tmsg="+testMsg+"&tmsg2="+testMsg2;
//
//                String link = "http://52.79.83.19/h_signIn.php";// 요청하는 url 설정 ex)192.168.0.1/httpOnlineTest.php
//
//                URL url = new URL(link);
//
//                httpURLConnection = (HttpURLConnection) url.openConnection();//httpURLConnection은 url.openconnection을 통해서 만 생성 가능 직접생성은 불가능하다.
//                httpURLConnection.setRequestMethod("POST");//post방식으로 설정
//                httpURLConnection.setDoInput(true);// server와의 통신에서 입력 가능한 상태로 만든다.
//                httpURLConnection.setDoOutput(true);//server와의 통신에서 출력 가능한 상태로 ㅏㄴ든다.
//
////                httpURLConnection.setConnectTimeout(30);// 타임 아웃 설정 default는 무제한 unlimit이다.
//                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());//서버로 뿅 쏴줄라구용
//                wr.write(data);//아까 String값을 쓱삭쓱삭 넣어서 보내주고!
//
//                wr.flush();//flush!
//                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));//자 이제 받아옵시다.
//                StringBuilder sb = new StringBuilder();// String 값을 이제 스슥스슥 넣을 껍니다.
//                String line;
//                while ((line= reader.readLine())!=null){
//                    sb.append(line);//
//                }
//
//                httpURLConnection.disconnect();//이거 꼭해주세요. 보통은 별일 없는데, 특정상황에서 문제가 생기는 경우가 있다고 합니다.
//                return sb.toString();//자 이렇게 리턴이되면 이제 post로 가겠습니다.
//
//            }catch (Exception e){
//
//                httpURLConnection.disconnect();
//
//                return new String ("Exception Occure"+e.getMessage());
//
//            }//try catch end
//
//        }//doInbackground end
//
//        }
//    }


