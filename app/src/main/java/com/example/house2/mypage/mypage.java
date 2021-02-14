package com.example.house2.mypage;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.Model;
import com.example.house2.AddCart.addChat;
import com.example.house2.R;
import com.example.house2.chat.MyService;
import com.example.house2.chat.partner_chatt;
import com.example.house2.commnunity_RE_adapter;
import com.example.house2.community;
import com.example.house2.community_json_tag;
import com.example.house2.login;
import com.example.house2.login_real;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.bootpay.api.ApiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * 프래그먼트 mypage 자바
 *
 * 마이페이지에 들어가야 하는 내용
 * 1. 장바구니 보러가기
 * 2. 프로필 편집 기능
 * 3. 결제 목록 보기 기능 추가하기
 */
public class mypage extends Fragment {
    String strNNickname, strProfile;
    CircleImageView mypage_photo;
    LinearLayout btn_addCart;
    LinearLayout tvProfileEditButton;
    LinearLayout tvPwEditButton;
    LinearLayout tvaddPaymentButton;
    LinearLayout tvaddChatButton;

    private Button kakao_logout;

    public static TextView tvNickname;
    public static String message;

    // 서버에 저장된 자료 불러오기
    public static staticUser staticUser;

    // 리사이클러뷰 관련
    RecyclerView Rv_MyContent_View;
    ArrayList<community_json_tag> list = new ArrayList<>();
    mypage.community_data_down community_data_down2;
    mypageAdapter mypageAdapter;

    //레트로핏
    String BASE_URL = "http://13.125.62.22/";
    Retrofit retrofit;
    Model model;
    ApiService apiService;
    String name;

    //Model class에서 사용할것
    public String staticUserId;
    public String staticUsername;


    public mypage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);


        kakao_logout = (Button)view.findViewById(R.id.kakaologout);
        // 새로고침
        Log.i("생명주기", "onCreateView: ");
        // 자동로그인 쉐어드 프리퍼런스 시작 --------------------------------------------------------------
        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences("auto", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        // 자동로그인 쉐어드 프리퍼런스 끝 --------------------------------------------------------------

        /**
         * 프로필 보기 로직
         * 1. id 는 찾아왔다.
         * 2. 찾아온 id 를 가지고 서버에 접속해서 가져올것
         * 3. okghttp를 통해서 데이터 가져오든가 레트로핏 쓰던가.. 암튼 맘대로
         */
        //카카오 유저 정보 받아오기
        tvNickname = view.findViewById(R.id.tvNickname);
        mypage_photo = view.findViewById(R.id.mypage_photo);

        Bundle bundle = getArguments();
        Log.e("번들도착", String.valueOf(bundle));
        if (bundle != null) {
            message = bundle.getString("name");
            // name 은 id
            String message2 = bundle.getString("profilePicture");
//            tvNickname.setText(message);
//            Glide.with(this).load(message2).into(mypage_photo);
//            Log.e("그라이드확인",String.valueOf(mypage_photo));
        }

        // 순서를 뒤로 미뤘음
        // okhttp로 유저 정보 받아오기
        String findUserloadURL = "http://13.125.62.22/h_profile.php";
        new findUserload().execute(findUserloadURL,message);



        // 레트로핏 --------------------------------------------------------------------
//        name = message;
//        Gson gson = new GsonBuilder()
//                .setLenient() //building as lenient mode`enter code here`
//                .create();
//        //retrofit builder 설정과 retrofit 객체생성
//        //retrofit 에서 제공하는 Builder 객체를 이용하여 설정
//        retrofit = new Retrofit.Builder()
//                //baseUrl : 네트워크 통신을 요청할 URL 설정
//                .baseUrl(BASE_URL)
//                //addConverterFactory : 통신이 완료된 후 어떤 Converter를 이용하여 데이터를 파싱할 것인지에 대한 설정
//                .addConverterFactory(GsonConverterFactory.create(gson)) //retrofit 응답 Gson 처리
//                .build(); //retrofit builder 객체에 설정한 정보로 retrofit 객체 만들어 반환
//
//        //interface 객체 생성 (클라이언트 객체)
//        apiService = retrofit.create(ApiService.class);
//
//        apiService.test(name).enqueue(new Callback<Model>() {
//            @Override
//            public void onResponse(Call<Model> call, Response<Model> response) {
//                //onResponse : 요청이 성공된 경우의 처리
//                model = response.body();
//                Log.i("레트로핏", name.toString());
//
//            }
//
//            @Override
//            public void onFailure(Call<Model> call, Throwable t) {
//                Log.i("레트로핏", "통신실패");
//                Log.i("레트로핏", t.toString());
//
//            }
//        });


        // 장바구니 보러가기
        btn_addCart = view.findViewById(R.id.tvaddCartButton);
        btn_addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addChat.class);
                startActivity(intent);
            }
        });
        // 결제목록 보러가기
        tvaddPaymentButton = view.findViewById(R.id.tvaddPaymentButton);
        tvaddPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 결제목록 보러가기 페이지에 정보 보내기
                Intent intent = new Intent(getActivity(), mypage_payment.class);
                intent.putExtra("id",staticUser.getId());
                intent.putExtra("name",staticUser.getName());
                intent.putExtra("nickname",staticUser.getNickname());
                intent.putExtra("mail",staticUser.getEmail());
                intent.putExtra("image",staticUser.getImage());
                startActivity(intent);
            }
        });



        // 로그아웃 버튼을 누르면 로그아웃이 되게
        kakao_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();

                Toast.makeText(getActivity(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show(); //로그아웃되었다는 것을 알려주는 토스트 메세지
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() { //로그아웃 실행
                    @Override
                    public void onCompleteLogout() {
                        //로그아웃 성공 시 로그인 화면(LoginActivity)로 이동
                        Intent intent = new Intent(getActivity(), login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

            }
        });

        // 내가 쓴 글 보기
        /**
         *
         * 내가 쓴글 보기 로직
         * 1. 리사이클러뷰를 만든다.
         * 2. 리사이클러뷰에 받아올 데이터를 aws의 H_show에 접속해서 데이터를 받아온다.
         *    (필요한 데이터 작성자, 타이틀, 사진, 몇번글인지)
         *
         *         title = intent.getStringExtra("title");
         *         con = intent.getStringExtra("con");
         *         Image = intent.getStringExtra("Image");
         *         id = intent.getStringExtra("id");
         *         tag_name = intent.getStringExtra("tag");
         *         System.out.println("태그이름" + tag_name);
         *         String tag_convert_json = "{status: " + tag_name+"}";
         *
         *         community_json_tag 클래스를 이용하기
         *
         */

        // -------------------------------------------------------------------------------------
        // 리사이클러뷰 관련 (레이아웃 매니져 여기있음)
        // -------------------------------------------------------------------------------------
        Rv_MyContent_View = view.findViewById(R.id.RvMyContentView);
        community_data_down2 = new mypage.community_data_down();
        try {
            community_data_down2.execute("http://13.125.62.22/h_getJson.php").get();

            // -------------------------------------------------------------------------------------
            // 리사이클러뷰 관련 (레이아웃 매니져 여기있음)
            // -------------------------------------------------------------------------------------

            Log.e("리사이클러", "1");
            list = new ArrayList<>();
            // 리사이클러뷰 객체 지정
            Rv_MyContent_View = view.findViewById(R.id.RvMyContentView);
            Log.e("리사이클러", "2");
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
            Rv_MyContent_View.setLayoutManager(mLayoutManager);


//            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//            mLayoutManager.setReverseLayout(true);
//            mLayoutManager.setStackFromEnd(true);
//
//            recyclerView.setLayoutManager(mLayoutManager);



            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        // 비밀번호 변경하러가기
        tvPwEditButton = view.findViewById(R.id.tvPwEditButton);
        tvPwEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //edit_pw_new 로 보내는 인텐트
                Intent intent = new Intent(getActivity(), editPw.class);
                intent.putExtra("id",staticUser.getId());
                intent.putExtra("name",staticUser.getName());
                intent.putExtra("nickname",staticUser.getNickname());
                intent.putExtra("mail",staticUser.getEmail());
                intent.putExtra("image",staticUser.getImage());
                intent.putExtra("pw",staticUser.getPw());
                startActivity(intent);
            }
        });


        //쓰레드 때문에 순서를 뒤로 미뤘다. 앞으로 오면 쓰레드가 겹쳐져서 null이 뜨기 때문에
        tvNickname.setText(staticUser.getNickname());
        Glide.with(this).load(staticUser.getImage()).into(mypage_photo);

        // 프로필수정하러가기
        tvProfileEditButton = view.findViewById(R.id.tvProfileEditButton);
        tvProfileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mypage_profileEdit.class);
                intent.putExtra("id",staticUser.getId());
                intent.putExtra("name",staticUser.getName());
                intent.putExtra("nickname",staticUser.getNickname());
                intent.putExtra("mail",staticUser.getEmail());
                intent.putExtra("image",staticUser.getImage());
                startActivity(intent);
            }
        });

        // 상담내역 보러가기
        tvaddChatButton = view.findViewById(R.id.tvaddChatButton);
        tvaddChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_real.loginIdInfoNick = staticUser.getNickname();
                Intent intent = new Intent(getActivity(), partner_chatt.class);
                intent.putExtra("from","user");
                startActivity(intent);
            }
        });


        // 서비스 사용할 소켓 정의하기--------------------------------------------------------------------
        login_real.realIDck = staticUser.getNickname();


        return view;

    } // 온크리에이트 끝




    @Override
    public void onResume() {
        super.onResume();
        Log.i("생명주기", "onResume: ");
        // 순서를 뒤로 미뤘음
        // okhttp로 유저 정보 받아오기
        String findUserloadURL = "http://13.125.62.22/h_profile.php";
        new findUserload().execute(findUserloadURL,message);

        // -------------------------------------------------------------------------------------
        // 리사이클러뷰 관련 (레이아웃 매니져 여기있음)
        // -------------------------------------------------------------------------------------
        community_data_down2 = new mypage.community_data_down();
        try {
            community_data_down2.execute("http://13.125.62.22/h_getJson.php").get();

            // -------------------------------------------------------------------------------------
            // 리사이클러뷰 관련 (레이아웃 매니져 여기있음)
            // -------------------------------------------------------------------------------------

            Log.e("리사이클러", "1");
            list = new ArrayList<>();
            // 리사이클러뷰 객체 지정
            Log.e("리사이클러", "2");
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
            Rv_MyContent_View.setLayoutManager(mLayoutManager);

//            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//            mLayoutManager.setReverseLayout(true);
//            mLayoutManager.setStackFromEnd(true);
//
//            recyclerView.setLayoutManager(mLayoutManager);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        //쓰레드 때문에 순서를 뒤로 미뤘다. 앞으로 오면 쓰레드가 겹쳐져서 null이 뜨기 때문에
        tvNickname.setText(staticUser.getNickname());
        Glide.with(this).load(staticUser.getImage()).into(mypage_photo);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("생명주기", "onPause: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("생명주기", "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("생명주기", "onDestroy: ");
    }

    //
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            //Write down your refresh code here, it will call every time user come to this fragment.
//            //If you are using listview with custom adapter, just call notifyDataSetChanged().
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(this).attach(this).commit();
//        }
//    }


    class community_data_down extends AsyncTask<String, String,String> {

        @Override
        protected String doInBackground(String... params) {
            Log.e("리사이클러", "4");
            StringBuilder jsonHtml = new StringBuilder();
            try{
                // 연결 url 설정
                URL url = new URL(params[0]);
                Log.e("리사이클러", "5");
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                Log.e("리사이클러", "6");
                Log.e("리사이클러", String.valueOf(conn));
                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    Log.e("리사이클러", "7");
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        Log.e("리사이클러", "8-1");
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            Log.e("리사이클러", "8");
                            if(line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        Log.e("리사이클러", "9");
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            System.out.println(jsonHtml.toString());
            String str2 = jsonHtml.toString();
            return jsonHtml.toString();

        }
        protected void onPostExecute(String str){
            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                Log.e("리사이클러", "10");
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.
                Log.e("제이슨확인", String.valueOf(jObject));
//                JSONArray results2 = new JSONArray(str);
//                Log.e("제이슨확인", String.valueOf(results2));
                JSONArray results = jObject.getJSONArray("status");
                Log.e("제이슨확인", String.valueOf(results));
                Log.e("제이슨확인", String.valueOf(results.get(0)));
                try {
                    JSONArray arr = new JSONArray(results);
                    Log.e("제이슨확인", String.valueOf(arr));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                zz = "";
//                zz += "Status : " + jObject.get("status");
//                zz += "\n";
//                zz += "Number of results : " + jObject.get("num_result");
//                zz += "\n";
//                zz += "Results : \n";
                Log.e("리사이클러", "11");

                // 지슨을 이용해서 리사이클러뷰에 보여줄 리스트 만들기
                int index = 0;
                while (index < results.length()) {

//                    community_json_tag community_json2 = gson.fromJson(results.get(index).toString(), community_json_tag.class);
                    String id = results.getJSONObject(index).getString("id");
                    Log.e("미친제이", id);
                    String imageURL = results.getJSONObject(index).getString("imageURL");
                    String con = results.getJSONObject(index).getString("con");
                    String title = results.getJSONObject(index).getString("title");
                    String writer = results.getJSONObject(index).getString("writer");
                    String writerDate = results.getJSONObject(index).getString("writerDate");
                    String tag = results.getJSONObject(index).getString("tag");
                    int id_par =Integer.parseInt(id);
                    Log.e("미친제이", String.valueOf(id_par));
                    Log.e("미친제이", imageURL);
                    community_json_tag community_json3 = new community_json_tag(id_par,imageURL,con,title,writer,writerDate,tag);
//                    community_json3.setId(id_par);
//                    community_json3.setCon(imageURL);
//                    community_json3.setImageURL(con);
//                    community_json3.setTag(tag);
//                    community_json3.setTitle(title);
//                    community_json3.setWriter(writer);
//                    community_json3.setWriterDate(writerDate);

                    list.add(community_json3);
                    index++;

                }
//                for ( int i = 0; i < results.length(); ++i ) {
//                    JSONObject temp = results.getJSONObject(i);
//                    zz += "\tid : " + temp.get("id");
//                    zz += "\timage : " + temp.get("image");
//                    zz += "\tcon : " + temp.get("con");
//                    zz += "\ttitle : " + temp.get("title");
//                    zz += "\twriter : " + temp.get("writer");
//                    zz += "\twriterDate : " + temp.get("writerDate");
//                    Log.e("리사이클러", "12");
//                    list.add(zz);
//
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 리사이클러뷰에 어댑터 객체 지정
            mypageAdapter = new mypageAdapter(list);
            Rv_MyContent_View.setAdapter(mypageAdapter);
            mypageAdapter.notifyDataSetChanged();
            Log.e("리사이클러", "3");
        }

    }
// 레트로핏 ----------------------------------------------------------
//    interface ApiService {
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
//        @POST("h_profile.php")
//            //Call 결과타입은 Model을 사용하여 test 라는 이름으로 만들었습니다.
//        Call<Model> test(@Field("name") String name);
//
//    }
//
//    class Model {
//        public String name(){
//            return name;
//        }
//    }
    // 유저 정보 찾기


    private class findUserload extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("확인중입니다");
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONObject jsonObject = mypagefindUserload.findUserload(params[0],params[1]);
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
                //Toast.makeText(getContext(), "파일 업로드 성공", Toast.LENGTH_LONG).show();
            }else {
               // Toast.makeText(getContext(), "파일 업로드 실패", Toast.LENGTH_LONG).show();
            }

        }
    }
    // 서버로 데이터 전송하는 코드
    public static class mypagefindUserload {

        public static JSONObject findUserload(String imageUploadUrl, String message ){

            try {

                final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=utf-8");
                // OKHTTP3
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id", message)
                        .build();

                Request request = new Request.Builder()
                        .url(imageUploadUrl)
                        .post(requestBody)
                        .build();
                Log.d("TAG", "스벌 메일" + message);
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String res = response.body().string();
                Log.e("TAG", "스벌1: " + res);
                Gson gson = new Gson();
                staticUser = gson.fromJson(res,staticUser.class);
                Log.e("TAG", "스벌1.5: " + staticUser.getName());
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
