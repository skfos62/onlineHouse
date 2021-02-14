package com.example.house2.mypage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.house2.AddCart.addChat;
import com.example.house2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class mypage_profileEdit extends AppCompatActivity {

    CircleImageView profileImage;
    EditText mypageEdit_id_input;
    EditText mypageEdit_mail_input;

    // 스트링을 제이슨으로 이미지 업로드에서 쓴다.
    JSONObject jObject;
    JSONArray results;

    // 선택한 이미지 주소
    Uri selectedImage;
    // 알아낸 파일주소
    String FilePath;

//    String mail2;
    static String mypageID;

    // 수정하기 버튼
    Button mypage_btn;

    //프래그먼트
    mypage mypage;




    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_profile_edit);

        // mypage 클래스에서 받아온 닉네임 인텐트
        Intent intent = getIntent();
        mypageID = intent.getExtras().getString("id"); // 이건 글번호같은거임
        String mypageName = intent.getExtras().getString("name"); // 진짜 아이디
        mypageEdit_id_input = (EditText) findViewById(R.id.mypageEdit_id_input);
        mypageEdit_mail_input = (EditText)findViewById(R.id.mypageEdit_mail_input);
        mypageEdit_id_input.setText(intent.getExtras().getString("nickname"));
        mypageEdit_mail_input.setText(intent.getExtras().getString("mail"));

        /**
         * 이미지 업로드하는 로직
         * 1. 앨범에서 이미지 선택해서 띄우는 함수 작성
         * 2. 해당하는 이미지의 절대경로를 가져오는 함수 작성
         * 3. okhttp로 multipart 생성 후 서버업로드 -> 업로드까지 완료
         * 4. 마이에스큐엘에 경로 저장하기 -> update 함수 / 값이 넘어가긴 하니까 update로 mysql 수정하기 -> 저장완료
         * 5. 마이페이지에서 okhttp 이용해서 값불러오기
         */

        profileImage = findViewById(R.id.mypage_photo);
        Glide.with(this).load(intent.getExtras().getString("image")).into(profileImage);
//        profileImage.set
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results = null;
                jObject = null;
                pickFromGallery();

            }
        });

        mypage_btn = findViewById(R.id.mypage_btn);
        mypage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail2 = mypageEdit_mail_input.getText().toString();
                String mypageNick = mypageEdit_id_input.getText().toString();
                String ImageUploadURL = "http://13.125.62.22/h_profileEidt.php";
                new ImageUpload().execute(ImageUploadURL,FilePath,mypageName,mypageNick,mail2);
                Log.d("TAG", "스벌 메일2" + mail2);

            }
        });

    }

    // 이미지 가져오는 함수
    private void pickFromGallery(){
        Intent pickphoto = new Intent(Intent.ACTION_PICK);
        pickphoto.setType("image/*");
        String[] mimeTypes = {"image/jpeg","image/png"};
        pickphoto.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(pickphoto,200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == 200){
                System.out.println("갔다왔음200");
                //data.getData returns the content URI for the selected Image
                selectedImage = data.getData();
                profileImage.setImageURI(selectedImage);
                // 이미지 절대 경로 얻기
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                FilePath = imgDecodableString;
                // imgDecoableString = 선택한 이미지의 절대 경로임
            } else {
                Toast.makeText(this, "다시 선택해 주세요.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    // 서버로 데이터 전송하는 전체 코드 -------------------------------------------------------------------
    private class ImageUpload extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mypage_profileEdit.this);
            progressDialog.setMessage("이미지 업로드중....");
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONObject jsonObject = mypageImageUploade.ImageUploade(params[0],params[1],params[2],params[3],params[4]);
                if (jsonObject != null)
                    return jsonObject.getString("result").equals("success");

            } catch (JSONException e) {
                Log.i("TAG", "스벌 : " + e.getLocalizedMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null)
                progressDialog.dismiss();
            if (aBoolean) {

                Toast.makeText(getApplicationContext(), "파일 업로드 성공", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), "파일 업로드 실패", Toast.LENGTH_LONG).show();
            }
            FilePath = "";

            finish();

        }
    }
    // 서버로 데이터 전송하는 코드 -----------------------------------------------------------------------
    public static class mypageImageUploade {

       public static JSONObject ImageUploade(String imageUploadUrl, String sourceImageFile ,String mypageID, String mypageNic, String mail){

           try {
               File sourceFile = new File(sourceImageFile);
               Log.d("TAG", "스벌" + sourceFile + " : " + sourceFile.exists());
               final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
               final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=utf-8");
               String filename = sourceImageFile.substring(sourceImageFile.lastIndexOf("/")+1);

               // OKHTTP3
               RequestBody requestBody = new MultipartBody.Builder()
                       .setType(MultipartBody.FORM)
                       .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                       .addFormDataPart("mail", mail)
                       .addFormDataPart("id", mypageID)
                       .addFormDataPart("nickname", mypageNic)
                       .build();

               Request request = new Request.Builder()
                       .url(imageUploadUrl)
                       .post(requestBody)
                       .build();
               Log.d("TAG", "스벌 메일 " + mail);
               Log.d("TAG", "스벌 아이디 " + mypageID);
               Log.d("TAG", "스벌 닉네임  " + mypageNic);
               OkHttpClient client = new OkHttpClient();
               Response response = client.newCall(request).execute();
               String res = response.body().string();
               Log.e("TAG", "스벌 결과: " + res);
               return new JSONObject(res);

           } catch (UnknownHostException | UnsupportedEncodingException e) {
               Log.e("TAG", "스벌: " + e.getLocalizedMessage());
           } catch (Exception e) {
               Log.e("TAG", "스벌: " + e.getLocalizedMessage());
           }

            return null;
        }

    }
}
