package com.example.house2.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.house2.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class edit_pw_new extends AppCompatActivity {

    String newEditpwId;
    String newEditpw;
    String newEditnickname;
    String newEditmail;
    String newEditimage;
    String newEditname;

    Button newEdit_pw_btn;

    EditText newEdit_pw_emailInput;
    EditText newEdit_pw_emailInput1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pw_new);

        Intent intent = getIntent();
        newEditpwId = intent.getExtras().getString("id");
        newEditname =  intent.getExtras().getString("name");
        newEditpw =  intent.getExtras().getString("pw");
        newEditnickname =  intent.getExtras().getString("nickname");
        newEditmail =  intent.getExtras().getString("mail");
        newEditimage =  intent.getExtras().getString("image");

        // 서버로 에디트 텍스트 보내기
        newEdit_pw_emailInput = findViewById(R.id.newEdit_pw_emailInput);
        newEdit_pw_emailInput1 = findViewById(R.id.newEdit_pw_emailInput1);
        newEdit_pw_btn = findViewById(R.id.newEdit_pw_btn);
        newEdit_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpw = newEdit_pw_emailInput.getText().toString();
                String newpw2 = newEdit_pw_emailInput1.getText().toString();
                if(newpw.equals(newpw2)) {
                    String findUserloadURL = "http://13.125.62.22/h_pwEidt.php";
                    new pwEditload().execute(findUserloadURL, newEditname, newpw);
                    finish();
                } else {
                    Toast.makeText(edit_pw_new.this, "입력하신 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 비밀번호 수정
    private class pwEditload extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(edit_pw_new.this);
            progressDialog.setMessage("확인중입니다");
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONObject jsonObject = mypagePwEditload.pwEditload(params[0],params[1],params[2]);
                if (jsonObject != null)
                    return jsonObject.getString("result").equals("success");

            } catch (JSONException e) {
                Log.i("TAG", "스벌2 : " + e.getLocalizedMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null)
                progressDialog.dismiss();
            if (aBoolean) {
//                Toast.makeText(getApplicationContext(), "파일 업로드 성공", Toast.LENGTH_LONG).show();
            }else {
//                Toast.makeText(getApplicationContext(), "파일 업로드 실패", Toast.LENGTH_LONG).show();
            }

        }
    }
    // 서버로 데이터 전송하는 코드
    public static class mypagePwEditload {

        public static JSONObject pwEditload(String imageUploadUrl, String newEditname ,String newEditpw ){

            try {

                final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=utf-8");
                // OKHTTP3
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("name", newEditname)
                        .addFormDataPart("pw", newEditpw)
                        .build();
                Request request = new Request.Builder()
                        .url(imageUploadUrl)
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String res = response.body().string();
                return new JSONObject(res);

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "스벌2: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "스벌3: " + e.getLocalizedMessage());
            }

            return null;
        }

    }

}


