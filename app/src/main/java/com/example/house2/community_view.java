package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static com.example.house2.community_image_tagging.arr;

public class community_view extends AppCompatActivity {
    // 레트로핏관련
    Retrofit retrofit;
//    ApiService2 apiService;
//    Model2 model;
    String back;


    ImageView community_view_close_btn;
    ImageView view_image;
    TextView community_view_title;
    TextView community_view_con;
    TextView view_edit_btn;

    String title;
    String con;
    String Image;
    private JSONArray mArray;

    RelativeLayout relative_layout2;
    // 동적 이미지뷰 생성
    //좌표값
    float x;
    float y;
    String name;
    String id;
    String tag_name;
    String BASE_URL;
//    community_data_down community_data_down;

    // 스트링을 제이슨으로
    JSONObject jObject;
    JSONArray results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_view);


//        BASE_URL = "http://13.125.62.22/h_getJson_tag.php";


        // 닫기 누르면 액티비티가 닫히게
        community_view_close_btn = findViewById(R.id.community_view_close_btn);
        community_view_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//
//        community_data_down = new community_data_down();
//        try {
//            community_data_down.execute("http://13.125.62.22/h_getJson_tag.php").get();
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        con = intent.getStringExtra("con");
        Image = intent.getStringExtra("Image");
        id = intent.getStringExtra("id");
        tag_name = intent.getStringExtra("tag");
        System.out.println("태그이름" + tag_name);
        String tag_convert_json = "{status: " + tag_name+"}";
        // 태그이름 받아오기 완료!
        // 이제 스트링으로 받아온거를 어레이 리스트로 만들어가지고 사용하자!

        jObject = null;
        try {
            jObject = new JSONObject(tag_convert_json);
            results = jObject.getJSONArray("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // results라는 key는 JSON배열로 되어있다.
        Log.e("제대로 제이슨이 되었는가?", String.valueOf(results));
//                JSONArray results2 = new JSONArray(str);
//                Log.e("제이슨확인", String.valueOf(results2));

//        try {
//            String x = results.getJSONObject(0).getString("x");
//            Log.e("제대로 제이슨이 되었는가? 값구하", x);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        // 안드로이드
        view_image = findViewById(R.id.view_image);
        community_view_title = findViewById(R.id.community_view_title);
        community_view_con = findViewById(R.id.community_view_con);

        //서버와 통신해서 태그를 받아와야한다.

        // 이미지뷰 들어오자마자 동적레이아웃 사용하기
        relative_layout2 = findViewById(R.id.relative_layout2);

        if (tag_name != null) {
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
//                JSONObject json = null;
//                try {
//                    json = new JSONObject(arr.get(i));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                JsonParser parser = new JsonParser();
//                JsonElement element = parser.parse(arr.get(i));
//                x = Float.parseFloat(element.getAsJsonObject().get("x").getAsString());
//                y = Float.parseFloat(element.getAsJsonObject().get("y").getAsString());
//                name = element.getAsJsonObject().get("name").getAsString();
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
                        img2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                img2.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }

        } else {

        }


        Glide.with(this).load(Image).into(view_image);
        community_view_title.setText(title);
        community_view_con.setText(con);

        // 수정하기 누르면 넘어가게
        view_edit_btn = findViewById(R.id.view_edit_btn);
        view_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), communityWrite.class);
                String ck = "edit";
                intent.putExtra("ck",ck);
                intent.putExtra("title",title);
                intent.putExtra("con",con);
                intent.putExtra("id",id);
                intent.putExtra("tag_name",tag_name);
                intent.putExtra("Image",Image);
                startActivity(intent);
            }
        });

        // -------------------------------------------------------------------------------------
        // 레트로핏 관련 설정들
        // -------------------------------------------------------------------------------------
        //retrofit builder 설정과 retrofit 객체생성
        //retrofit 에서 제공하는 Builder 객체를 이용하여 설정
//        retrofit = new Retrofit.Builder()
//                //baseUrl : 네트워크 통신을 요청할 URL 설정
//                .baseUrl(BASE_URL)
//                //addConverterFactory : 통신이 완료된 후 어떤 Converter를 이용하여 데이터를 파싱할 것인지에 대한 설정
//                .addConverterFactory(GsonConverterFactory.create()) //retrofit 응답 Gson 처리
//                .build(); //retrofit builder 객체에 설정한 정보로 retrofit 객체 만들어 반환

        //interface 객체 생성 (클라이언트 객체)
//        apiService = retrofit.create(ApiService2.class);
////
//        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//        // 결과 받아오기
//        apiService.test(id).enqueue(new Callback<Model2>() {
//
//            //onResponse : 요청이 성공된 경우의 처리
//            @Override
//            public void onResponse(Call<Model2> call, Response<Model2> response) {
//                model = response.body(); //받아온 데이터 model에 담기
//                back = model.getId(); //"id"로 받아온 id 데이터 mstring3값에 설정
//                Toast.makeText(getApplicationContext(), back.toString(), Toast.LENGTH_SHORT).show();
//                mtextview2.setText(back); //mtextview2 텍스트 mstring4로 설정
//            }
//
//            //onFailure : 요청이 실패패된 경우의 처리
//            @Override
//            public void onFailure(Call<Model2> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//       // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    }
    }


    // -------------------------------------------------------------------------------------
    // 이미지 업로드 버튼 눌렀을때 (레트로핏관련)
    // -------------------------------------------------------------------------------------

//
//    interface ApiService2 {
//
//        //서버와 통신하는 방법에 따라 @GET, @POST, @PUT, @DELETED, @HEAD를 사용할 수 있습니다.
//        //예제는 GET방식을 사용했습니다.
//
//        //GET방식 틀
//        //@GET(“api주소”)
//        //Call<모델클래스> 함수이름(@Query("변수이름") 안드로이드에서 보낼 변수);
//
//        //POST방식 틀
//        //@FormUrlEncoded
//        //@POST(“api주소”)
//        //Call<모델클래스> 함수이름(@Field("변수이름") 안드로이드에서 보낼 변수);
//
//        @FormUrlEncoded
//        @POST("h_imageconUP.php")
//            //Call 결과타입은 Model을 사용하여 test 라는 이름으로 만들었습니다.
//        Call<communityWrite.Model> test(@Field("id") String id
//        );
//
//    }
//
//    class Model2 {
//        public String getId() {
//            return id;
//        }
//    }





    // -------------------------------------------------------------------------------------
    // 태그 위치 가져오기(레트로핏으로 서버랑 통신하자)
    // -------------------------------------------------------------------------------------




//
//    class community_data_down extends AsyncTask<String, String,String> {
//        Gson gson = new Gson();
//        @Override
//        protected String doInBackground(String... params) {
//            StringBuilder jsonHtml = new StringBuilder();
//            try{
//                // 연결 url 설정
//                URL url = new URL(params[0]);
//                // 커넥션 객체 생성
//                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//                // 연결되었으면.
//                if(conn != null){
//                    conn.setConnectTimeout(10000);
//                    conn.setUseCaches(false);
//                    // 연결되었음 코드가 리턴되면.
//                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
//                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                        for(;;){
//                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
//                            String line = br.readLine();
//                            Log.e("염병", line);
//                            if(line == null) break;
//                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
//                            jsonHtml.append(line + "\n");
//                            Log.e("염병", String.valueOf(jsonHtml));
//                        }
//                        br.close();
//                    }
//                    conn.disconnect();
//                }
//            } catch(Exception ex){
//                ex.printStackTrace();
//            }
//            System.out.println(jsonHtml.toString());
//            Log.e("염병", "0");
//            String str2 = jsonHtml.toString();
//            Log.e("염병", "0");
//            Log.e("염병", str2);
//            return jsonHtml.toString();
//
//        }
//        protected void onPostExecute(String str){
//            String jObject = new String(String.valueOf(str));
//            Log.e("염병2", String.valueOf(jObject));
//            try {
//                mArray = new JSONArray(jObject);
//                Log.e("염병2", String.valueOf(mArray));
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
////            try {
////                Log.e("염병2","되는고? ");
////                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
////                String jObject = new String(String.valueOf(str));
////                Log.e("염병2", String.valueOf(jObject));
//////                // results라는 key는 JSON배열로 되어있다.
//////                JSONArray results = jObject.getJSONArray("result");
//////                Log.e("염병2", String.valueOf(results));
//////                Log.e("염병2", "11");
//////
//////                // 지슨을 이용해서 리사이클러뷰에 보여줄 리스트 만들기
//////                int index = 0;
//////                community_json_tag community_json2 = gson.fromJson(results.get(index).toString(), community_json_tag.class);
//////                String jObject2 =String.valueOf(community_json2);
//////                System.out.println("태그값 받아온거" +jObject2);
////
////            } catch (JSONException e) {
////                e.printStackTrace();
////            }
//        }
//
//
//    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -




