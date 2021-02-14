package com.example.house2.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.house2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.house2.login_real.loginIdInfoNick;

public class partner_chatt extends AppCompatActivity {

    TextView partner_chatt_sellerName;
    String getType;

    //서버에서 받아오는 변수
    String roomId;
    String userID;
    String sellerID;
    String chatContent;
    Context context;

    // 리사이클러뷰
    ArrayList<DataItem> partnerChatList = new ArrayList<>();
    partner_chatt_list_adapter partner_chatt_list_adapter;
    RecyclerView partner_chatt_recycler;
    DataItem makeDataList;

    LinearLayout partner_bottom;

    //스와이프로 새로고침하는곳 선언
    SwipeRefreshLayout swipe_layout;



    /**
     * 여기서 보여주는거는 채팅방 리스트
     * 판매자 일경우와 구매자일 경우 두가지를 나누어서 보내야한다.
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_chatt);
        Intent intent = getIntent();
        getType = intent.getStringExtra("from");
        if(getType.equals("user")){
            partner_bottom = findViewById(R.id.partner_bottom);
            partner_bottom.setVisibility(View.INVISIBLE);
        }
        // 채팅리스트를 불러오기 위해서 서버와 접속하는곳
        sendRequest("http://13.125.62.22/h_chatRoomListCkPartner.php");

        partner_chatt_sellerName = findViewById(R.id.partner_chatt_sellerName);
        partner_chatt_sellerName.setText(loginIdInfoNick);
        partner_chatt_sellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기에서는 디비에 접속해서 채팅리스트가 있는지 확인해야한다.
                Intent intent = new Intent(partner_chatt.this, partner_chatt_view.class);
                intent.putExtra("from","seller");
                intent.putExtra("sellerID",loginIdInfoNick);
//                intent.putExtra("roomId",roomId);
//                intent.putExtra("chatContent",chatContent);
//                intent.putExtra("userID",userID);

                startActivity(intent);
            }
        });

        // 리사이클러뷰 관련 선언하는곳
        partner_chatt_recycler = findViewById(R.id.partner_chatt_recycler);
        partner_chatt_recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        partner_chatt_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        // 스와이프로 새로고침 하는곳
        swipe_layout = findViewById(R.id.swipe_layout);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                partnerChatList = new ArrayList<>();
                sendRequest("http://13.125.62.22/h_chatRoomListCkPartner.php");
                partner_chatt_list_adapter.notifyDataSetChanged();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_layout.setRefreshing(false);
                    }
                }, 300);

            }

        });

    }

    /**
     * <채팅 리스트 만드는 로직>
     *     1. 일단 채팅하기를 누르면 방 생성이 되는데 이순간 db에 방번호랑 요청한 사람의 아이디와 내용이 서버로 날라간다.
     *     2. 방넘버 / 요청한사람의 아이디 / 요청받는사람의 아이디 / 전송 내용 / 전송 요청 시간
     *     3. 방넘버를 체크하고 같은 방넘버를 가진 사람들끼리만 broadcast를 해준다.
     *     
     *     </채팅>
     */

    // 1. 채팅리스트를 받기위한 서버 통신하는 부분 --------------------------------------------------------
    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // 1-1. 서버와 통신이 완료(성공)되고 응답을 받아오는곳
                        // 확인하는곳
                        Log.i("채팅리스트 리스폰값 확인", "onResponse: " + response);
//                        responeJson = response;
                        // 1-2. 통신을 하여서 받아온 제이슨을 리사이클러뷰에 뿌려주기 위해서
                        //    제이슨 형식의 데이터를 오브젝트로 바꾸고 -> 어레이리스트에 넣어주어야한다.

                        try {
                            // 제이슨 오브젝트에서 status라는 이름을 가진 어레이를 뽑아내서
                            JSONObject jObject = new JSONObject(response);
                            JSONArray results = jObject.getJSONArray("status");
                            // 하나하나 partner_Main_list_class의 오브젝트로 만들어서 어레이리스트로 만들기
                            int index = 0;
                            while (index < results.length()) {
                            // jsonobject인 result에서 하나하나 값을 가져와서 partner_Main_list 클래스 오브젝트로 만드는곳
                            roomId = results.getJSONObject(index).getString("roomId");
                            userID = results.getJSONObject(index).getString("userID");
                            sellerID = results.getJSONObject(index).getString("sellerID");
                            chatContent = results.getJSONObject(index).getString("chatContent");
                            //    private String content 1;
                                //    private String name 2;
                                //    private int viewType 3;
                                //    private String RoomNum 4;

                                // 유저아이디가 내가 로그인한 아이디 일때 (현재 내가 구매자일때)
                            if(userID.equals(loginIdInfoNick)){
                                makeDataList = new DataItem(chatContent,sellerID,0,roomId);
                                partnerChatList.add(makeDataList);
                                index++;
                            } else if(sellerID.equals(loginIdInfoNick)){
                                // 판매자아이디가 내가 로그인한 아이디 일때 (현재 내가 판매자일때)
                                makeDataList = new DataItem(chatContent,userID,1,roomId);
                                partnerChatList.add(makeDataList);
                                index++;
                            }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("채팅리스트 리스폰값 확인", "onResponse: 제대로 받아와지는거 확인 끝!" + response);
                        // 여기서 리사이클러뷰로 만들 작업을 진행해야한다.
                        // 4. 만든 어레이리스트에 리사이클러뷰어댑터를 붙이기
                        partner_chatt_list_adapter = new partner_chatt_list_adapter(partnerChatList); // 어댑터에 리스트 붙이고
                        partner_chatt_recycler.setAdapter(partner_chatt_list_adapter); // 리사이클러뷰에 어댑터 장착
                        partner_chatt_list_adapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    // 서버와의 통신이 실패 되면 나오는곳
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("에러확인", "onErrorResponse: " + error);
                        Log.i("php리스폰값 확인", "된거여 만거여2 ");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //여기다가 서버에 보낼 데이터값을 넣어준다.
//                $seller=$_POST['seller'];
//                $userId=$_POST['userId'];
                Log.i("php리스폰값 확인", "된거여 만거여3 " +loginIdInfoNick);
                Log.i("php리스폰값 확인", "된거여 만거여3 " +getType);
                params.put("typeCk", getType);
                // 현재 로그인 유저 아이디 넣어주기
                params.put("typeID", loginIdInfoNick);
                return params;
            }
        };
        // 이전 결과가 있더라도 새로 요청
        // 볼리 초기화 해주는 부분
        queue.add(request);
        request.setShouldCache(false);
//        AppHelper.requestQueue.add(request);
    }
    // 매출리스트를 받기위한 서버 통신하는 부분 끝 ------------------------------------------------------


    @Override
    protected void onPause() {
        super.onPause();
        //리사이클러뷰 새로고침하기
        // 채팅리스트를 불러오기 위해서 서버와 접속하는곳
        partnerChatList = new ArrayList<>();
        sendRequest("http://13.125.62.22/h_chatRoomListCkPartner.php");
        partner_chatt_list_adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //리사이클러뷰 새로고침하기
        // 채팅리스트를 불러오기 위해서 서버와 접속하는곳
//        partnerChatList = new ArrayList<>();
//        sendRequest("http://13.125.62.22/h_chatRoomListCkPartner.php");
        if(partner_chatt_list_adapter != null){
            partner_chatt_list_adapter.notifyDataSetChanged();
        }

    }
}
