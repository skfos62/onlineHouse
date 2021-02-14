package com.example.house2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.example.house2.community_image_tagging.arr;

public class communityWrite extends AppCompatActivity {

    ImageView imageUP_btn;
    ImageView community_write_close_btn;
    Button community_write_close_btn2;
    Button community_write_save_btn;
    String FilePath;
    EditText community_write_text_con;
    EditText community_write_text;
    Button community_write_image_tagging;
    TextView community_write_title_btn;

    Retrofit retrofit;
    Model model;
    ApiService apiService;
    String BASE_URL = "http://13.125.62.22/";

    String community_text_con; // 글 작성 내용
    String community_text_title; // 글 작성 제목
    String community_text_writer; // 글 작성자

    RelativeLayout relative_layout2;

    //model 에서 사용 하는 string
    public String title;
    public String con;
    public String writer;
    public String tag;
    public String ck;
    Uri selectedImage;

    private final int REQUEST_CODE_123 = 300;
    Intent intent3;

    // 동적 이미지뷰 생성
    //좌표값
    float x;
    float y;
    String name;

    String ck_survial;
    Intent intent4;

    //수정하기에서 넘어온것들
    String title_edit;
    String con_edit;
    String image_edit;
    String tag_name_edit;
    String id_edit;

    TextView img2;
    ImageView img;

    // 스트링을 제이슨으로
    JSONObject jObject;
    JSONArray results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_write);
        // 글내용 받아오기
        // 작성자와 글내용 둘다 보내야됨!
        community_write_text_con = findViewById(R.id.community_write_text_con);
        community_write_text = findViewById(R.id.community_write_text);
        imageUP_btn =(ImageView)findViewById(R.id.imageUP_btn);

        intent4 = new Intent();
        intent4 = getIntent();
        ck_survial=intent4.getStringExtra("ck");
        title_edit=intent4.getStringExtra("title");
        con_edit=intent4.getStringExtra("con");
        tag_name_edit=intent4.getStringExtra("tag_name");
        id_edit= intent4.getStringExtra("id");
        image_edit= intent4.getStringExtra("Image");

        String tag_convert_json = "{status: " + tag_name_edit+"}";

        System.out.println("글수정"+ck_survial);
        System.out.println("타이틀"+title_edit);
        community_write_title_btn = findViewById(R.id.community_write_title_btn);
        if(ck_survial.equals("edit")){
            community_write_title_btn.setText("글 수정하기");
            community_write_text.setText(title_edit);
            community_write_text_con.setText(con_edit);
            Glide.with(this).load(image_edit).into(imageUP_btn);
            //태그 이미지 위치시키기
            jObject = null;
            try {
                jObject = new JSONObject(tag_convert_json);
                results = jObject.getJSONArray("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            relative_layout2 = findViewById(R.id.relative_layout2);

            if (tag_name_edit != null) {
                //어레이에 있는 값을 for문을 통해 하나하나 가져오기
                for (int i = 0; i < results.length(); i++) {
                    try {
                        x = Float.parseFloat(results.getJSONObject(i).getString("x"));
                        System.out.println("x위치값" + x);
                        y = Float.parseFloat(results.getJSONObject(i).getString("y"));
                        System.out.println("x위치값" + y);
                        name = results.getJSONObject(i).getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("x위치값" + x);
                    System.out.println("y위치값" + y);

                    ImageView img = new ImageView(getApplicationContext());
                    img.setImageResource(R.drawable.tag124);

                    //이미지가 저장될 곳의 x,y좌표를 표현
                    img.setX(x - 40);
                    img.setY(y - 90);
                    //최상단 릴레이티브 레이아웃에 이미지를 Add
                    relative_layout2.addView(img);

                    final TextView img2 = new TextView(getApplicationContext());
                    img2.setText(name);
                    img2.setVisibility(View.INVISIBLE);
                    img2.setBackgroundColor(Color.WHITE);

                    //이미지가 저장될 곳의 x,y좌표를 표현
                    img2.setX(x);
                    img2.setY(y);
                    //최상단 릴레이티브 레이아웃에 이미지를 Add
                    relative_layout2.addView(img2);

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                            System.out.println(name);
                            img2.setVisibility(View.VISIBLE);
                        }
                    });
                }

            } else {

            }




        }

        // 이미지 태깅하기 버튼 눌렀을때

        community_write_image_tagging = findViewById(R.id.community_write_image_tagging);
        community_write_image_tagging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results = null;
                jObject = null;
                Intent intent4 = new Intent(v.getContext(), community_image_tagging.class);
//                Intent intent = new Intent(v.getContext(),community_image_tagging.class);
                intent4.putExtra("image",selectedImage);
                System.out.println("갔다왔니? 300");
                startActivityForResult(intent4, 300);
            }
        });

        // 이미지 업로드 버튼 눌렀을때

        imageUP_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results = null;
                jObject = null;
                System.out.println("갔다왔니? 200");
                pickFromGallery();

            }
        });

        // -------------------------------------------------------------------------------------
        // 닫기버튼 눌렀을때
        // -------------------------------------------------------------------------------------
        // 닫기버튼
        community_write_close_btn = findViewById(R.id.community_write_close_btn);
        community_write_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //취소하기
        community_write_close_btn2 = findViewById(R.id.community_write_close_btn2);
        community_write_close_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr = null;
                finish();
            }
        });

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        // -------------------------------------------------------------------------------------
        // 레트로핏 관련 설정들
        // -------------------------------------------------------------------------------------
        //retrofit builder 설정과 retrofit 객체생성
        //retrofit 에서 제공하는 Builder 객체를 이용하여 설정
        retrofit = new Retrofit.Builder()
                //baseUrl : 네트워크 통신을 요청할 URL 설정
                .baseUrl(BASE_URL)
                //addConverterFactory : 통신이 완료된 후 어떤 Converter를 이용하여 데이터를 파싱할 것인지에 대한 설정
                .addConverterFactory(GsonConverterFactory.create()) //retrofit 응답 Gson 처리
                .build(); //retrofit builder 객체에 설정한 정보로 retrofit 객체 만들어 반환

        //interface 객체 생성 (클라이언트 객체)
        apiService = retrofit.create(ApiService.class);

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


        // -------------------------------------------------------------------------------------
        // 작성하기를 눌렀을때 (이미지를 서버에 전송해야한다)
        // -------------------------------------------------------------------------------------



        // 작성하기 누를때
        community_write_save_btn = findViewById(R.id.community_write_save_btn);
        community_write_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 글내용 제목 작성자
                community_text_con = community_write_text_con.getText().toString();
                community_text_title = community_write_text.getText().toString();
                community_text_writer = MainActivity.strNickname;
                System.out.println(community_text_writer);

//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                intent.putExtra("글쓰기완료","글쓰기완료"); /*송신*/
////                startActivity(intent);
                System.out.println(arr.toString());

                //레트로핏 호출
                apiService.test(community_text_title,community_text_con,community_text_writer,arr.toString(),ck_survial).enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        System.out.println("서버로 넘어갔심");
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        System.out.println("안넘어갔는디?");

                    }
                });
                // 이미지 부분
                String ImageUploadURL = "http://13.125.62.22/h_imageUP.php";
                new ImageUploadTask().execute(ImageUploadURL, FilePath);

            }
        });

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



    }


    // -------------------------------------------------------------------------------------
    // 이미지 업로드 버튼 눌렀을때
    // -------------------------------------------------------------------------------------
    private void pickFromGallery(){
        Intent pickphoto = new Intent(Intent.ACTION_PICK);
        pickphoto.setType("image/*");
        String[] mimeTypes = {"image/jpeg","image/png"};
        pickphoto.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(pickphoto,200);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 200:
                    System.out.println("갔다왔음200");
                    //data.getData returns the content URI for the selected Image
                    selectedImage = data.getData();
                    imageUP_btn.setImageURI(selectedImage);
                    community_write_image_tagging.setVisibility(View.VISIBLE);
                    // 이미지 절대 경로 얻기
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    FilePath = imgDecodableString;
                    // imgDecoableString = 선택한 이미지의 절대 경로임d
                    break;
                case 300:
                    System.out.println("갔다왔음! 300");
                    //이미지태그가 몇개 되었는지 보여주게 하기
                    // 프레임 레이아웃을 사용해서 몇개의 태그를 사용하였는지 알려주기
                    community_write_image_tagging.setText("총 " + arr.size() + "개의 태그를 거셨습니다.");
                    community_write_image_tagging.setBackgroundResource(R.color.colorMain);
                    community_write_image_tagging.setClickable(false);
                    // 이미지 태그 걸기
                    // 조건문을 걸어서 해보기
                    relative_layout2 = findViewById(R.id.relative_layout2);

                    if (arr != null) {
                        //어레이에 있는 값을 for문을 통해 하나하나 가져오기
                        for (int i = 0; i < arr.size(); i++) {
                            JSONObject json = null;
                            try {
                                json = new JSONObject(arr.get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(arr.get(i));
                            x = Float.parseFloat(element.getAsJsonObject().get("x").getAsString());
                            y = Float.parseFloat(element.getAsJsonObject().get("y").getAsString());
                            name = element.getAsJsonObject().get("name").getAsString();
                            System.out.println("x위치값" + x);
                            System.out.println("y위치값" + y);

                            ImageView img = new ImageView(getApplicationContext());
                            img.setImageResource(R.drawable.tag124);

                            //이미지가 저장될 곳의 x,y좌표를 표현
                            img.setX(x - 40);
                            img.setY(y - 90);
                            //최상단 릴레이티브 레이아웃에 이미지를 Add
                            relative_layout2.addView(img);
                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "이미지를 클릭했삼" + name, Toast.LENGTH_LONG).show();
                                    System.out.println(name);
                                }
                            });
                        }

                    } else {

                    }

                    // 번들로 받아오는거
//                    Bundle MBuddle = data.getExtras();
//                    assert MBuddle != null;
//                    String MMessage = MBuddle.getString("tag_obj");
//                    System.out.println("받아온 리스트 갯스2" + MMessage);
//
//                    try {
//                        JSONArray array = new JSONArray(MMessage);
//                        System.out.println("받아온 리스트 갯스2" + array);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if(intent3 != null) {
//                      intent3 = getIntent();
////                    ArrayList<String> names = (ArrayList<String>) intent.getSerializableExtra("names");
////                    ArrayList<Object> list = (new ArrayList<Object>) intent3.getSerializableExtra("tag_obj");
//                        String jsonArray2 = intent3.getStringExtra("tag_obj");
//
//                        try {
//                            String jsonArray = intent3.getStringExtra("tag_obj");
//                            System.out.println("받아온 리스트 갯스2" + jsonArray);
//                            JSONArray array = new JSONArray(jsonArray);
//                            System.out.println(array);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
////                    ArrayList<Object> list = (ArrayList<Object>)intent3.getSerializableExtra("tag_obj");
////                    list = intent3.getSerializableExtra("tag_obj");
//
//                    }else {
//                        System.out.println( "아니스바 되는겨 ? ");
//                    }
                    break;
            }

    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    // -------------------------------------------------------------------------------------
    // 이미지 업로드 버튼 눌렀을때 (레트로핏관련)
    // -------------------------------------------------------------------------------------



    interface ApiService {

        //서버와 통신하는 방법에 따라 @GET, @POST, @PUT, @DELETED, @HEAD를 사용할 수 있습니다.
        //예제는 GET방식을 사용했습니다.

        //GET방식 틀
        //@GET(“api주소”)
        //Call<모델클래스> 함수이름(@Query("변수이름") 안드로이드에서 보낼 변수);

        //POST방식 틀
        //@FormUrlEncoded
        //@POST(“api주소”)
        //Call<모델클래스> 함수이름(@Field("변수이름") 안드로이드에서 보낼 변수);

        @FormUrlEncoded
        @POST("h_imageconUP.php")
            //Call 결과타입은 Model을 사용하여 test 라는 이름으로 만들었습니다.
        Call<Model>test(@Field("title") String title,
                        @Field("con") String con,
                        @Field("writer") String writer,
                        @Field("tag") String tag,
                        @Field("ck") String ck
                        );

    }
    class Model {
        public String gettitle() {
            return title;
        }
        public String getcon() {
            return con;
        }
        public String getwriter() {
            return writer;
        }
        public String gettag() {
            return tag;
        }
        public String getck() {
            return ck;
        }
    }


    private void uploadToServer(String filePath) {


        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        NetworkClient.UploadAPIs uploadAPIs = retrofit.create(NetworkClient.UploadAPIs.class);
        //Create a file object using file path
        File file = new File(filePath);
        Log.e("파일 경로 맞니?", String.valueOf(filePath));

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        System.out.println(fileReqBody);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);
        //Create request body with text description and text media type
        System.out.println(part);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        System.out.println(description);
        //
        Call call = uploadAPIs.uploadImage(part, description);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("성공쓰?");
                System.out.println("성공쓰?");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println("실패쓰?");
            }
        });


    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    // -------------------------------------------------------------------------------------
    // 이미지 업로드 쓰레드 (okhttp / 이미지)
    // -------------------------------------------------------------------------------------

    private  class ImageUploadTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(communityWrite.this);
                progressDialog.setMessage("이미지 업로드중....");
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(String... params) {

                try {
                    JSONObject jsonObject = JSONParser.uploadImage(params[0],params[1]);
                    if (jsonObject != null)
                        return jsonObject.getString("result").equals("success");

                } catch (JSONException e) {
                    Log.i("TAG", "Error : " + e.getLocalizedMessage());
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


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -



}
