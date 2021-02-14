package com.example.house2.partner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.house2.AppHelper;
import com.example.house2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.house2.login_real.loginIdInfo;

public class partner_delevery extends AppCompatActivity {

    // 배송대기 리사이클러뷰 관련
    RecyclerView partner_delevery_wait_recycler;
    RecyclerView partner_delevery_ing_recycler;
    ArrayList<partner_delevery_saleinfo_class> deleveryList = new ArrayList<>();
    ArrayList<partner_delevery_saleinfo_class> deleveryFineList = new ArrayList<>();
    partner_delevery_wait_adapter partner_delevery_wait_adapter;
    partner_delevery_ing_adapter partner_delevery_ing_adapter;

    // 서버에 받아오는
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

    //하단 네비게이션
    TextView partner_delevery_Home_btn, partner_delevery_product_btn,partner_delevery_btn; // 홈버튼
    // 상품등록 하단 홈버튼
            // 배송관리 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_delevery);

        // 하단 네비게이션바 선언
        partner_delevery_Home_btn= findViewById(R.id.partner_delevery_Home_btn);
        partner_delevery_Home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_Main.class);
                startActivity(intent);//액티비티 띄우기

            }
        });
        partner_delevery_product_btn= findViewById(R.id.partner_delevery_product_btn);
        partner_delevery_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_register_product.class);
                startActivity(intent);//액티비티 띄우기
            }
        });
        partner_delevery_btn= findViewById(R.id.partner_delevery_btn);
        partner_delevery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_delevery.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        /**
         * 관리자가 택배를 관리하는 곳인데
         * 여기서 관리자가 송장번호를 등록하게 되면 배송중으로 텍스트 바뀌게됨
         *  1. 배송대기중과 배송중의 리사이클러뷰를 보여준다.
         *  2. 배송대기중 상태에서 배송중으로 바뀌게 되면 하단의 리사이클러뷰로 옮겨진다.
         *
         *  - 서버(h_partnerDeleveryView.php)로 보낼 정보 : 판매자 이름
         *  서버로 판매자 이름을 보내서 해당하는 판매자가 판 물건의 리스트를 받아와야야함
         *
         *  <판매리스트를 받아오는 방법>
         *      1. 판매자id를 php로 보낸다.
         *      2. 보낸 php에서 응답을 받아온다.
         *      3. 해당하는 데이터를 리사이클러뷰에 뿌려준다.
         *      4. 이때 리사이클러뷰의 onbindHolder에서 배송 상태를 체크해서
         *         각각 맞는 상태에서 뿌려준다.
         *      </판매리스트를>
         *
         *  <배송중으로 바뀐리스트 받아오는것도 동일하다>
         *      </배송중으로>
         */

        // 1. 판매자 id 를 php로 보낸다
        sendRequest("http://13.125.62.22/h_partnerDeleveryView.php");
        // 배송대기중
        partner_delevery_wait_recycler = findViewById(R.id.partner_delevery_wait_recycler);
//        partner_delevery_wait_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        partner_delevery_wait_recycler.setLayoutManager(mLayoutManager);

        // 배송중
        partner_delevery_ing_recycler = findViewById(R.id.partner_delevery_ing_recycler);
//        partner_delevery_ing_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setReverseLayout(true);
        mLayoutManager2.setStackFromEnd(true);
        partner_delevery_ing_recycler.setLayoutManager(mLayoutManager2);

    }
    // 1. 매출리스트를 받기위한 서버 통신하는 부분 --------------------------------------------------------
    public void sendRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 1-1. 서버와 통신이 완료(성공)되고 응답을 받아오는곳
                        // 확인하는곳
                        Log.i("php리스폰값 확인", "onResponse: " + response);
                        Log.i("php리스폰값 확인", "된거여 만거여1 " );
//                        responeJson = response;
                        // 1-2. 통신을 하여서 받아온 제이슨을 리사이클러뷰에 뿌려주기 위해서
                        //    제이슨 형식의 데이터를 오브젝트로 바꾸고 -> 어레이리스트에 넣어주어야한다.
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
                                if(productState.equals("N")){
                                    partner_delevery_saleinfo_class deleveryWaitClass = new partner_delevery_saleinfo_class(Integer.parseInt(id),productName,IntProductNum,buyer,IntPayment,sellerName,date,productState,Recipient,RecipAdress,Reciphone);
                                    // 만든 오브젝트를 리사이클러뷰를 뿌릴 어레이리스트에 넣어주기
                                    deleveryList.add(deleveryWaitClass);
                                    index++;
                                } else {
                                    Log.i("배송준비중이 아닌것", "onResponse: ");
                                    partner_delevery_saleinfo_class deleveryFineClass = new partner_delevery_saleinfo_class(Integer.parseInt(id),productName,IntProductNum,buyer,IntPayment,sellerName,date,productState,Recipient,RecipAdress,Reciphone);
                                    deleveryFineList.add(deleveryFineClass);
                                    index++;
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 4. 만든 어레이리스트에 리사이클러뷰어댑터를 붙이기
                        // 배송대기 리스트
                        partner_delevery_wait_adapter = new partner_delevery_wait_adapter(deleveryList); // 어댑터에 리스트 붙이고
                        Log.i("리스트 추가됐는지 안됐는지 확인하기", "onCreate: " + deleveryList.size());
                        partner_delevery_wait_recycler.setAdapter(partner_delevery_wait_adapter); // 리사이클러뷰에 어댑터 장착
                        partner_delevery_wait_adapter.notifyDataSetChanged();
                        // 배송완료 리스트
                        partner_delevery_ing_adapter = new partner_delevery_ing_adapter(deleveryFineList);
                        partner_delevery_ing_recycler.setAdapter(partner_delevery_ing_adapter);
                        partner_delevery_ing_adapter.notifyDataSetChanged();

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
                Log.i("php리스폰값 확인", "된거여 만거여3 " +loginIdInfo);
                params.put("sellerName", loginIdInfo);
                return params;
            }
        };
        // 이전 결과가 있더라도 새로 요청
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
    // 매출리스트를 받기위한 서버 통신하는 부분 끝 ------------------------------------------------------

}
