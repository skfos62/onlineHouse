package com.example.house2;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

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
 * 프래그먼트(구경하기)
 */
public class community extends Fragment {
    Button community_image_uptest;
    FloatingActionButton community_write_btn;
    RecyclerView item_show_recycle_list;
//    ArrayList<String> list;
    String zz;

    String str2;

    Gson gson = new Gson();
    ArrayList<community_json_tag> list = new ArrayList<>();
    community_data_down community_data_down;
    commnunity_RE_adapter commnunity_re_adapter;
    community_json_tag community_json3;
    public community() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        // 플로팅 버튼
        // 버튼 누르면 커뮤니티 글쓰기 액티비티로 넘어가게
        community_write_btn = view.findViewById(R.id.community_write_btn);
        community_write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인텐트 넘어가게
                Intent intent = new Intent(getActivity(), communityWrite.class);
                String ck = "new";
                intent.putExtra("ck",ck);
                startActivity(intent);
            }
        });

        community_data_down = new community_data_down();
        try {
            community_data_down.execute("http://13.125.62.22/h_getJson.php").get();

            // -------------------------------------------------------------------------------------
            // 리사이클러뷰 관련 (레이아웃 매니져 여기있음)
            // -------------------------------------------------------------------------------------

            Log.e("리사이클러", "1");
            list = new ArrayList<>();
            // 리사이클러뷰 객체 지정
            item_show_recycle_list = view.findViewById(R.id.item_show_recycle_list);
            Log.e("리사이클러", "2");
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            item_show_recycle_list.setLayoutManager(mLayoutManager);

            // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("온포즈상", "띠용");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void refresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    class community_data_down extends AsyncTask <String, String,String>{

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

                    list.add(community_json3);
                    index++;

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 리사이클러뷰에 어댑터 객체 지정
            commnunity_re_adapter = new commnunity_RE_adapter(list);
            item_show_recycle_list.setAdapter(commnunity_re_adapter);
            commnunity_re_adapter.notifyDataSetChanged();
            Log.e("리사이클러", "3");
        }


    }

}
