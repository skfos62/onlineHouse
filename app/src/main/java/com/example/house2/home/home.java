package com.example.house2.home;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.house2.JSONParser;
import com.example.house2.R;
import com.example.house2.classfile.shopping_list_class;
import com.example.house2.community_json_tag;
import com.example.house2.community_view;
import com.example.house2.login;
import com.example.house2.mypage.mypage_profileEdit;
import com.example.house2.secret;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */

/**
 * 프래그먼트 home 자바
 */
public class home extends Fragment {

    // 최신글 관련
    TextView tv_new_con;
    TextView tv_new_title;
    ImageView iv_new_image;
    LinearLayout main_list;

    // 가구 리사이클러뷰 관련
    ArrayList<shopping_list_class> mainList = new ArrayList<>();
    shopHttp shopHttp;
    RecyclerView main_list_recycle;
    homeReAdapter homeReAdapter;


    private Button kakao_logout;
    static shopping_list_class shopListReData;
    community_json_tag community_json3;
    http http;

    //시크릿
    TextView secret_btn;

    public home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        secret_btn = view.findViewById(R.id.secret_btn);
        secret_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), secret.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        // 최신글 시작 -------------------------------------------------------------------------------
        /**
         * 최신글 받아오는 로직
         * 1. 쓰레드를 통해서 데이터에 접속해서 php 에서 관련내용을 받아온다
         *
         */
        // 데이터 받아오기 (리사이클러뷰)-------------------------------------------------------------
        main_list_recycle = view.findViewById(R.id.main_list_recycle);
        shopHttp = new shopHttp();
        try {
            shopHttp.execute("http://13.125.62.22/h_getMainShop.php").get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 데이터 받아오기 (리사이클러뷰)-------------------------------------------------------------
        main_list = view.findViewById(R.id.main_list);
        tv_new_title= view.findViewById(R.id.tv_new_title);
        tv_new_con= view.findViewById(R.id.tv_new_con);
        iv_new_image= view.findViewById(R.id.iv_new_image);
        http = new http();
        try {
            http.execute("http://13.125.62.22/h_getMain.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 받아온 최신글 데이터를 이미지뷰에다가 셋시키기
        // 2초간 멈추게 하고싶다면
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_new_title.setText(community_json3.getTitle());
                tv_new_con.setText(community_json3.getCon());
                Glide.with(getActivity()).load(community_json3.getImageURL()).into(iv_new_image);
                main_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), community_view.class);
                        intent.putExtra("title",community_json3.getTitle());
                        intent.putExtra("id",community_json3.getTitle());
                        intent.putExtra("con",community_json3.getCon());
                        intent.putExtra("Image",community_json3.getImageURL());
                        intent.putExtra("Writer",community_json3.getWriter());
                        intent.putExtra("WriterDate()",community_json3.getWriterDate());
                        intent.putExtra("tag",community_json3.getTag());
                        v.getContext().startActivity(intent);
                        Toast.makeText(v.getContext(), community_json3.getCon(), Toast.LENGTH_SHORT).show();
                    }
                });
                // 클릭하면 글쓰기를 볼수있게
            }
        }, 2000);  // 2000은 2초를 의미합니다.




        // 최신글 끝 ---------------------------------------------------------------------------------

//        // 로그아웃 시작 ------------------------------------------------------------------------------
//        kakao_logout = (Button)view.findViewById(R.id.kakaologout);
//
//        // 로그아웃 버튼을 누르면 로그아웃이 되게
//        kakao_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show(); //로그아웃되었다는 것을 알려주는 토스트 메세지
//                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() { //로그아웃 실행
//                    @Override
//                    public void onCompleteLogout() {
//                        //로그아웃 성공 시 로그인 화면(LoginActivity)로 이동
//                        Intent intent = new Intent(getActivity(), login.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }
//                });
//
//            }
//        });
//        // 로그아웃 끝 -------------------------------------------------------------------------------

        // 메인페이지 리사이클러뷰 시작 ------------------------------------------------------------------


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        main_list_recycle.setLayoutManager(mLayoutManager);




        // 메인페이지 리사이클러뷰 끝 --------------------------------------------------------------------

        return view;
    }

    // 서버로 데이터 전송하는것 (최신글)-------------------------------------------------------------------

    class http extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("메인로딩중..");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            Log.e("리사이클러", "4");
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(params[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(jsonHtml.toString());
            String str2 = jsonHtml.toString();
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.
                JSONArray results = jObject.getJSONArray("status");
//                try {
//                    JSONArray arr = new JSONArray(results);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                // 지슨을 이용해서 리사이클러뷰에 보여줄 리스트 만들기
                int index = 0;
                String id = results.getJSONObject(index).getString("id");
                Log.e("미친제이1", id);
                String imageURL = results.getJSONObject(index).getString("H_showImg.imageURL");
                String con = results.getJSONObject(index).getString("con");
                String title = results.getJSONObject(index).getString("title");
                String writer = results.getJSONObject(index).getString("writer");
                String writerDate = results.getJSONObject(index).getString("writerDate");
                String tag = results.getJSONObject(index).getString("tag");
                int id_par =Integer.parseInt(id);
                Log.e("미친제이2", String.valueOf(id_par));
                community_json3 = new community_json_tag(id_par,imageURL,con,title,writer,writerDate,tag);
                Log.e("미친제이3", community_json3.getTitle());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    // 서버로 데이터 전송하는것 (최신글)-------------------------------------------------------------------

    class shopHttp extends AsyncTask<String, String, String> {
//        ProgressDialog progressDialog;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("메인로딩중..");
//            progressDialog.show();
//        }
        @Override
        protected String doInBackground(String... params) {
            Log.e("리사이클러", "4");
            StringBuilder jsonHtml = new StringBuilder();
            try {
                // 연결 url 설정
                URL url = new URL(params[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if (line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(jsonHtml.toString());
            String str2 = jsonHtml.toString();
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.
                JSONArray results = jObject.getJSONArray("status");

                try {
                    JSONArray arr = new JSONArray(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 지슨을 이용해서 리사이클러뷰에 보여줄 리스트 만들기
                int index = 0;
                while (index < results.length()) {
                    String id = results.getJSONObject(index).getString("id");
                    String Furname = results.getJSONObject(index).getString("Furname");
                    int Furprice = Integer.parseInt(results.getJSONObject(index).getString("Furprice"));
                    String Furcopy = results.getJSONObject(index).getString("Furcopy");
                    String Furcategory = results.getJSONObject(index).getString("Furcategory");
                    String FurArck = results.getJSONObject(index).getString("FurArck");
                    String FurImage = results.getJSONObject(index).getString("FurImage");
                    String sfa = results.getJSONObject(index).getString("sfa");
                    String sellerName = results.getJSONObject(index).getString("sellerName");

                    shopListReData= new shopping_list_class(Furname,Furprice,Furcopy,Furcategory,FurArck,FurImage,sfa,sellerName);
                    mainList.add(shopListReData);
                    Log.e("제이슨확인2", String.valueOf(mainList.get(0).getFurprice()));
                    index++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            homeReAdapter = new homeReAdapter(mainList);
            main_list_recycle.setAdapter(homeReAdapter);
        }

    }
    // 서버로 데이터 전송하는것 (최신글)-------------------------------------------------------------------



}
