package com.example.house2;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.house2.classfile.shopping_list_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * 여기서 리사이클러뷰를 보여줘야한다.
 */

public class shopping extends Fragment {
    TextView tvArcore;
    // 리사이클러뷰관련
    RecyclerView shopListRe;
    shopping_list_adapter shopListReAdapter;
    shopping_list_class shopListReData;
    public static ArrayList<shopping_list_class> shopListReDatalist = new ArrayList<>();
    ArrayList<shopping_list_class> shopListReDatalist2;
    shop_list shop_list;
    Context context;

    LinearLayout tv_shopping_fur,tv_shopping_chair,tv_shopping_table,tv_shopping_lamp,tv_shopping_viewAll;
    TextView tv_shopping_viewAll_text,tv_shopping_fur_text,tv_shopping_chair_text,tv_shopping_table_text,tv_shopping_lamp_text;

    //필터확인
    public static String recy_filter_ck=null;

    // 새로고침
    SwipeRefreshLayout swipe_layout;



    public shopping() {
        // Required empty public constructor
    }

    public shopping(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        // 가구배치 바로가기 누르면 ar코어에 연결된 액티비티가 뜨게 만들기
        tvArcore = (TextView) view.findViewById(R.id.tv_arcore2);
        tvArcore.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), shopping_arcore.class);
            startActivity(intent);
        });

        shopListRe = view.findViewById(R.id.shop_list_recycle);


        /**
         * 더미데이터 받아오기 순서
         * 1. okhhttp로 데이터를 받아온다
         * 2. 받아온 데이터를 어레이 리스트에 넣는다
         */

        shop_list = new shop_list();
        try {
            shop_list.execute("http://13.125.62.22/h_getShopList.php").get();

          // from Google Analytics

//            Log.e("리사이클러뷰 ", "2");
//            shopListReAdapter = new shopping_list_adapter(shopListReDatalist);
//            shopListRe.setLayoutManager(new LinearLayoutManager(context));
//            shopListRe.setAdapter(shopListReAdapter);
//            shopListReAdapter.notifyDataSetChanged();



            Log.e("리사이클러", "1");
            shopListReDatalist = new ArrayList<>();
            // 리사이클러뷰 객체 지정
            Log.e("리사이클러", "2");
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            shopListRe.setLayoutManager(mLayoutManager);




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        // 카테고리 버튼 생성하는곳
        tv_shopping_viewAll_text = view.findViewById(R.id.tv_shopping_viewAll_text);
        tv_shopping_fur_text = view.findViewById(R.id.tv_shopping_fur_text);
        tv_shopping_chair_text = view.findViewById(R.id.tv_shopping_chair_text);
        tv_shopping_table_text = view.findViewById(R.id.tv_shopping_table_text);
        tv_shopping_lamp_text = view.findViewById(R.id.tv_shopping_lamp_text);

        // 전체보기
        tv_shopping_viewAll = view.findViewById(R.id.tv_shopping_viewAll);
        tv_shopping_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shopListReAdapter = new shopping_list_adapter(shopListReDatalist);
                shopListRe.setAdapter(shopListReAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                shopListRe.setLayoutManager(mLayoutManager);
                shopListReAdapter.notifyDataSetChanged();
                tv_shopping_viewAll_text.setBackgroundResource(R.drawable.shape3);
                tv_shopping_fur_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_chair_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_table_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_lamp_text.setBackgroundResource(R.drawable.shape2);


            }
        });
        // 가구

        tv_shopping_fur = view.findViewById(R.id.tv_shopping_fur);
        tv_shopping_fur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recy_filter_ck = "furniture";
                shopListReDatalist2 = new ArrayList<>();
                for (int i =0 ; i <shopListReDatalist.size(); i++){
                    if(shopListReDatalist.get(i).getFurcategory().equals(recy_filter_ck)) {

                        shopListReDatalist2.add(shopListReDatalist.get(i));
                    }
                }
                Log.i("리사이클러뷰 확인", "onClick: " + shopListReDatalist2.size());
                shopListReAdapter = new shopping_list_adapter(shopListReDatalist2);
                shopListRe.setAdapter(shopListReAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                shopListRe.setLayoutManager(mLayoutManager);
                shopListReAdapter.notifyDataSetChanged();
                tv_shopping_viewAll_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_fur_text.setBackgroundResource(R.drawable.shape3);
                tv_shopping_chair_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_table_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_lamp_text.setBackgroundResource(R.drawable.shape2);
            }
        });



        // 의자

        tv_shopping_chair = view.findViewById(R.id.tv_shopping_chair);
        tv_shopping_chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recy_filter_ck = "chair";
                shopListReDatalist2 = new ArrayList<>();
                for (int i =0 ; i <shopListReDatalist.size(); i++){
                    if(shopListReDatalist.get(i).getFurcategory().equals(recy_filter_ck)) {

                        shopListReDatalist2.add(shopListReDatalist.get(i));
                    }
                }
                Log.i("리사이클러뷰 확인", "onClick: " + shopListReDatalist2.size());
                shopListReAdapter = new shopping_list_adapter(shopListReDatalist2);
                shopListRe.setAdapter(shopListReAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                shopListRe.setLayoutManager(mLayoutManager);
                shopListReAdapter.notifyDataSetChanged();
                tv_shopping_viewAll_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_fur_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_chair_text.setBackgroundResource(R.drawable.shape3);
                tv_shopping_table_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_lamp_text.setBackgroundResource(R.drawable.shape2);

            }
        });

        // 테이블
        tv_shopping_table = view.findViewById(R.id.tv_shopping_table);
        tv_shopping_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recy_filter_ck = "table";
                shopListReDatalist2 = new ArrayList<>();
                for (int i =0 ; i <shopListReDatalist.size(); i++){
                    if(shopListReDatalist.get(i).getFurcategory().equals(recy_filter_ck)) {

                        shopListReDatalist2.add(shopListReDatalist.get(i));
                    }
                }
                Log.i("리사이클러뷰 확인", "onClick: " + shopListReDatalist2.size());
                shopListReAdapter = new shopping_list_adapter(shopListReDatalist2);
                shopListRe.setAdapter(shopListReAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                shopListRe.setLayoutManager(mLayoutManager);
                shopListReAdapter.notifyDataSetChanged();
                tv_shopping_viewAll_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_fur_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_chair_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_table_text.setBackgroundResource(R.drawable.shape3);
                tv_shopping_lamp_text.setBackgroundResource(R.drawable.shape2);
            }
        });

        // 기타
        tv_shopping_lamp = view.findViewById(R.id.tv_shopping_lamp);
        tv_shopping_lamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recy_filter_ck = "etc";
                shopListReDatalist2 = new ArrayList<>();
                for (int i =0 ; i <shopListReDatalist.size(); i++){
                    if(shopListReDatalist.get(i).getFurcategory().equals(recy_filter_ck)) {

                        shopListReDatalist2.add(shopListReDatalist.get(i));
                    }
                }
                Log.i("리사이클러뷰 확인", "onClick: " + shopListReDatalist2.size());
                shopListReAdapter = new shopping_list_adapter(shopListReDatalist2);
                shopListRe.setAdapter(shopListReAdapter);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                shopListRe.setLayoutManager(mLayoutManager);
                shopListReAdapter.notifyDataSetChanged();
                tv_shopping_viewAll_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_fur_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_chair_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_table_text.setBackgroundResource(R.drawable.shape2);
                tv_shopping_lamp_text.setBackgroundResource(R.drawable.shape3);

            }
        });

        // 새로고침 하는곳
        swipe_layout = view.findViewById(R.id.swipe_layout);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    shop_list = new shop_list();
                    shop_list.execute("http://13.125.62.22/h_getShopList.php").get();
                    shopListReAdapter.notifyDataSetChanged();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_layout.setRefreshing(false);
                    }
                }, 500);

            }

        });

        return view;


    }

    // 데이터 받아오는곳

    class shop_list extends AsyncTask<String, String, String> {

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
//                Log.e("제이슨확인", String.valueOf(jObject));
//                JSONArray results2 = new JSONArray(str);
//                Log.e("제이슨확인", String.valueOf(results2));
                JSONArray results = jObject.getJSONArray("status");
//                Log.e("제이슨확인", String.valueOf(results));
//                Log.e("제이슨확인", String.valueOf(results.get(0)));
//                try {
//                    JSONArray arr = new JSONArray(results);
////                    Log.e("제이슨확인", String.valueOf(arr));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

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
                    shopListReDatalist.add(shopListReData);
                    Log.e("제이슨확인2", String.valueOf(shopListReDatalist.get(0).getFurprice()));
                    index++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            shopListReAdapter = new shopping_list_adapter(shopListReDatalist);
            shopListRe.setAdapter(shopListReAdapter);


        }


    }
}
