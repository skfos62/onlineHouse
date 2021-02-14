package com.example.house2.partner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.house2.AppHelper;
import com.example.house2.R;
import com.example.house2.chat.MyService;
import com.example.house2.chat.partner_chatt;
import com.example.house2.login_real;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.house2.login_real.loginIdInfo;
import static com.example.house2.login_real.loginIdInfoNick;

/**
 * 서버에서 관리자페이지로 받아와야하는 정보
 * 1. 사업자명
 * 2. 해당 사업자명의 매출 정보
 */
public class partner_Main extends AppCompatActivity {


    TextView partner_main_Home_btn;
    TextView partner_main_product_btn;
    TextView partner_main_delevery_btn;
    TextView partner_main_chatt_btn;

    partner_Main_list_class partner_main_list_class;
    ArrayList<partner_Main_list_class> partnerMainList = new ArrayList<>();
    RecyclerView partner_main_Home_recycler;
    partner_Main_list_adapter partner_Main_list_adapter;

    String TAG_partner;

    //서버에서 받아오는 변수
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

    // 서버에서 받아오는 스트링 변수
    String responeJson;

    // 메인의 사업자명과 오늘 하루 매출
    TextView partner_main_Home_sellerName;
    TextView partner_main_Home_todayTotalPayment;
    int totalPayment=0;

    //스와이프로 새로고침하는곳 선언
    SwipeRefreshLayout swipe_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_main);

        // 서비스 사용할 소켓 정의하기--------------------------------------------------------------------
        login_real.realIDck = loginIdInfoNick;

        Log.i("php리스폰값 확인", "된거여 만거5 ");
        // 1. 서버와 통신하여서 매출데이터를 알아와야한다.
        TAG_partner = "/h_saleInfoDb.php";
        sendRequest("http://13.125.62.22/h_saleInfoDb.php");
        Log.i("php리스폰값 확인", "된거여 만거4 ");

        if (AppHelper.requestQueue == null)
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());

        // 하단 네비게이션바
        // 관리자 페이지 홈버튼
        partner_main_Home_btn = findViewById(R.id.partner_main_Home_btn);
        partner_main_Home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_Main.class);
                startActivity(intent);

            }
        });
        // 상품등록페이지
        partner_main_product_btn = findViewById(R.id.partner_main_product_btn);
        partner_main_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_register_product.class);
                startActivity(intent);//액티비티 띄우기

            }
        });
        partner_main_delevery_btn = findViewById(R.id.partner_main_delevery_btn);
        partner_main_delevery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_delevery.class);
                startActivity(intent);//액티비티 띄우기

            }
        });
        partner_main_chatt_btn = findViewById(R.id.partner_main_chatt_btn);
        partner_main_chatt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_chatt.class);
                intent.putExtra("from","seller");
                startActivity(intent);//액티비티 띄우기

            }
        });

        /**
         * <서버에서 정보를 받아오는 로직>
         *     1. http를 통해서 서버에 접속한다.
         *     2. 고객의 결제완료 리스트 데이터를 받아온다.
         *     3. 리사이클러뷰로 해당하는 정보를 메인 액티비티에 뿌려준다.
         *        리사이클러뷰
         *        1) partner_Main 클래스에서 리사이클러뷰를 findViewById한다.
         *           서버에서 데이터를 받아 와서 어레이 리스트에 넣어준다.
         *        2) 어댑터를 만들어서 아이템 클래스와 데이터를 연결해준다.
         *     </서버에서>
         */

        // 사업자 명 표시해주는곳
        partner_main_Home_sellerName = findViewById(R.id.partner_main_Home_sellerName);
        partner_main_Home_sellerName.setText(loginIdInfoNick);

        // -------------------------------------------------------------------------------------
        // 메인에서 매출리스트 보여주는곳
        // -------------------------------------------------------------------------------------

        // 리사이클러뷰
        partner_main_Home_recycler = findViewById(R.id.partner_main_Home_recycler);
        partner_main_Home_recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        partner_main_Home_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // 레이아웃 메니저
        // 매출리스트
        // 이거를 buying class로 보내서 해당하는 사람들이 결제한 리스트가 뭔지 알아야한다.
//        partner_Main_list_adapter = new partner_Main_list_adapter(partnerMainList); // 어댑터에 리스트 붙이고
//        Log.i("리스트 추가됐는지 안됐는지 확인하기", "onCreate: " + partnerMainList.size());
//        partner_main_Home_recycler.setAdapter(partner_Main_list_adapter); // 리사이클러뷰에 어댑터 장착
//        partner_Main_list_adapter.notifyDataSetChanged();
        // 스와이프로 새로고침 하는곳
        swipe_layout = findViewById(R.id.swipe_layout);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                totalPayment = 0;
                sendRequest("http://13.125.62.22/h_saleInfoDb.php");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_layout.setRefreshing(false);
                    }
                }, 500);

            }

        });


    }

    // 2. 매출리스트를 받기위한 서버 통신하는 부분 --------------------------------------------------------
    public void sendRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        partnerMainList = new ArrayList<>();
                        // 2. 서버와 통신이 완료(성공)되고 응답을 받아오는곳
                        // 확인하는곳
                        Log.i("php리스폰값 확인", "onResponse: " + response);
                        Log.i("php리스폰값 확인", "된거여 만거여1 " );
//                        responeJson = response;
                        // 3. 통신을 하여서 받아온 제이슨을 리사이클러뷰에 뿌려주기 위해서
                        //    제이슨 형식의 데이터를 오브젝트로 바꾸고 -> 어레이리스트에 넣어주어야한다.
                        try {
                            // 제이슨 오브젝트에서 status라는 이름을 가진 어레이를 뽑아내서
                            JSONObject jObject = new JSONObject(response);
                            JSONArray results = jObject.getJSONArray("status");
                            // 하나하나 partner_Main_list_class의 오브젝트로 만들어서 어레이리스트로 만들기
                            int index = 0;
                            // jsonobject인 result에서 하나하나 값을 가져와서 partner_Main_list 클래스 오브젝트로 만드는곳
                            while (index < results.length()) {
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
                                partner_Main_list_class partnerMainClass = new partner_Main_list_class(productName,IntProductNum,buyer,IntPayment,sellerName,date,productState,Recipient,RecipAdress,Reciphone);
                                // 만든 오브젝트를 리사이클러뷰를 뿌릴 어레이리스트에 넣어주기
                                partnerMainList.add(partnerMainClass);
                                index++;
                                totalPayment = totalPayment + IntPayment;
                                Log.i("누적금액 확인하기", "onResponse: " + totalPayment);
                                partner_main_Home_todayTotalPayment =findViewById(R.id.partner_main_Home_todayTotalPayment);
                                partner_main_Home_todayTotalPayment.setText(String.valueOf(totalPayment));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 4. 만든 어레이리스트에 리사이클러뷰어댑터를 붙이기
                        partner_Main_list_adapter = new partner_Main_list_adapter(partnerMainList); // 어댑터에 리스트 붙이고
                        Log.i("리스트 추가됐는지 안됐는지 확인하기", "onCreate: " + partnerMainList.size());
                        partner_main_Home_recycler.setAdapter(partner_Main_list_adapter); // 리사이클러뷰에 어댑터 장착
                        partner_Main_list_adapter.notifyDataSetChanged();
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
