package com.example.house2.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.house2.AppHelper;
import com.example.house2.R;
import com.example.house2.partner.partner_delevery_saleinfo_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mypage_payment extends AppCompatActivity {

    /**
     * 마이페이지에서 결제목록 보는 페이지
     * @param savedInstanceState
     */

    // 현재 로그인한 사람의 정보
    String userNickname;
    // 서버에 받아오는 정보
    String id;
    String productName;
    String productNum;
    String buyer;
    String payment;
    String sellerName;
    String date;
    String productState;
    String Recipient;
    String RecipAdress;
    String Reciphone;
    int IntProductNum;
    int IntPayment;
    private RequestQueue queue;

    //리사이클러뷰 관련 선언
    partner_delevery_saleinfo_class mypagePaymentClass;
    ArrayList<partner_delevery_saleinfo_class> mypagePaymentList = new ArrayList<>();
    RecyclerView mypage_payment_list;
    mypage_payment_adapter mypage_payment_adapter;

    // 볼리관련
    RequestQueue request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_payment);

        /**
         * 마이페이지에서 결제목록 보는 로직
         * 1. 현재로그인한 사람의 정보를 서버로 보낸다.
         * 2. sale_info_DB에서 로그인한 사람의 결제 내역이 있는지 확인한다.
         * 3. php(h_mypagePaymentView.php)에서 받아온 정보를 응답받아온다.
         * 4. 리사이클러뷰에 뿌려준다.
         *
         */

        // 결제목록 보러가기 페이지에 정보 보내기
        // 1. 현재 로그인한 사람의 정보를 서버로 보내기
        // 현재 로그인한 사람의 정보 알아내기
        Intent intent = getIntent();
        userNickname = intent.getExtras().getString("nickname");
        Log.i("정보가 제대로 받아와졌는지 확인하기 ", "onCreate:(확인완료) " + userNickname);
        // 1-1. php 로 정보 보내기
        sendRequest("http://13.125.62.22/h_mypagePaymentView.php");

        // 리사이클러뷰 선언
        mypage_payment_list = findViewById(R.id.mypage_payment_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mypage_payment_list.setLayoutManager(mLayoutManager);
        mypage_payment_adapter = new mypage_payment_adapter(mypagePaymentList);
        mypage_payment_list.setAdapter(mypage_payment_adapter);

    }

    // 2. 매출리스트를 받기위한 서버 통신하는 부분 --------------------------------------------------------
    public void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request2 = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 3. 서버와 통신이 완료(성공)되고 응답을 받아오는곳
                        // 확인하는곳
                        Log.i("php리스폰값 확인", "onResponse: " + response);

//                        responeJson = response;
//                         3-1. 통신을 하여서 받아온 제이슨을 리사이클러뷰에 뿌려주기 위해서
//                            제이슨 형식의 데이터를 오브젝트로 바꾸고 -> 어레이리스트에 넣어주어야한다.
                        try {
                            // 제이슨 오브젝트에서 status라는 이름을 가진 어레이를 뽑아내서
                            JSONObject jObject = new JSONObject(response);
                            JSONArray results = jObject.getJSONArray("status");
                            // 하나하나 partner_Main_list_class의 오브젝트로 만들어서 어레이리스트로 만들기
                            int index = 0;
                            // jsonobject인 result에서 하나하나 값을 가져와서 partner_Main_list 클래스 오브젝트로 만드는곳
                            while (index < results.length()) {
                                id = results.getJSONObject(index).getString("id");
                                productName = results.getJSONObject(index).getString("productName");
                                productNum = results.getJSONObject(index).getString("productNum");
                                buyer = results.getJSONObject(index).getString("buyer");
                                payment = results.getJSONObject(index).getString("payment");
                                sellerName = results.getJSONObject(index).getString("sellerName");
                                date = results.getJSONObject(index).getString("date");
                                productState = results.getJSONObject(index).getString("productState");
                                Recipient = results.getJSONObject(index).getString("Recipient");
                                RecipAdress = results.getJSONObject(index).getString("RecipAdress");
                                Reciphone = results.getJSONObject(index).getString("Reciphone");
                                IntProductNum =Integer.parseInt(productNum);
                                IntPayment =Integer.parseInt(payment);
                                mypagePaymentClass = new partner_delevery_saleinfo_class(Integer.parseInt(id),productName,IntProductNum,buyer,IntPayment,sellerName,date,productState,Recipient,RecipAdress,Reciphone);
                                // 만든 오브젝트를 리사이클러뷰를 뿌릴 어레이리스트에 넣어주기
                                mypagePaymentList.add(mypagePaymentClass);
                                index++;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                         4. 만든 어레이리스트에 리사이클러뷰어댑터를 붙이기
//                         배송대기 리스트
                        mypage_payment_adapter = new mypage_payment_adapter(mypagePaymentList); // 어댑터에 리스트 붙이고
                        Log.i("리스트 추가됐는지 안됐는지 확인하기", "onCreate: " + mypagePaymentList.size());
                        mypage_payment_list.setAdapter(mypage_payment_adapter); // 리사이클러뷰에 어댑터 장착
                        mypage_payment_adapter.notifyDataSetChanged();

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
//                Log.i("php리스폰값 확인", "된거여 만거여3 왜 안돼고 지럴이여");
                Log.i("php리스폰값 확인", userNickname);
                params.put("buyer", userNickname);
                return params;
            }
        };
        // 이전 결과가 있더라도 새로 요청
//        RequestQueue queue = Volley.newRequestQueue(this);
        // 볼리를 초기화 해주는 부분
        queue.add(request2);

//        Log.i("php리스폰값 확인1", "된거여 만거여3 왜 안돼고 지럴이여");
//        request2.setShouldCache(false);
//        Log.i("php리스폰값 확인1", "된거여 만거여3 왜 안돼고 지럴이여" +request2);
//        AppHelper.requestQueue.add(request2);
//        Log.i("php리스폰값 확인1", "된거여 만거여3 왜 안돼고 지럴이여" +AppHelper.requestQueue.add(request2));
    }
    // 매출리스트를 받기위한 서버 통신하는 부분 끝 ------------------------------------------------------

}
