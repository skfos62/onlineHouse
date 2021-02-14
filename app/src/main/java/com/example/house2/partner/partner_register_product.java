package com.example.house2.partner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.example.house2.classfile.shopping_list_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.house2.login_real.loginIdInfo;

/**
 * 상품등록 페이지 입니다.(등록은 안돼고 리스트만 확인할수있게)
 */
public class partner_register_product extends AppCompatActivity {
    Button partner_register_btn;

    //서버에서 받아오는 변수
    String Furname;
    String Furprice;
    String Furcopy;
    String Furcategory;
    String FurArck;
    String FurImage;
    String sfa;
    String sellerName;
    int IntFurprice;

    shopping_list_class partnerRegisterMainClass;

    // 리사이클러뷰 관련
    ArrayList<shopping_list_class> partnerRegisterMainList = new ArrayList<>();
    partner_register_product_adapter partner_register_product_adapter;
    RecyclerView partner_register_list;

    com.example.house2.partner.partner_register_product_adapter.onDeleteClickListener onDeleteClickListener;

    TextView partner_register_Home_btn,partner_register_product_btn,partner_register_delevery_btn;
    // 상품관리    //배송관리

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_register_product);
        // 하단네비게이션
        partner_register_Home_btn = findViewById(R.id.partner_register_Home_btn);
        partner_register_Home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_Main.class);
                startActivity(intent);//액티비티 띄우기
            }
        });
        partner_register_product_btn = findViewById(R.id.partner_register_product_btn);
        partner_register_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_register_product.class);
                startActivity(intent);//액티비티 띄우기
            }
        });
        partner_register_delevery_btn = findViewById(R.id.partner_register_delevery_btn);
        partner_register_delevery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_delevery.class);
                startActivity(intent);//액티비티 띄우기
            }
        });

        /**
         * <내가 등록한 가구 리스트 보기>
         *     1. 안드로이드에서 php(h_partnerRegisterView.php)로 현재 로그인한 id 정보를 보낸다.
         *     2. php에서는 받아온 id 정보를 가지고 sql문을 이용해서 데이터를 안드로이드 쪽으로 보낸다.
         *     3. 응답된 json형식의 데이터를 파싱해서 arraylist로 만든다.
         *     4. 만든 arraylist에 리사이클러뷰를 붙여서 액티비티에 뿌려준다.
         *     </등록한>
         */
        // 리사이클러뷰 관련
        partner_register_list = findViewById(R.id.partner_register_list);
        partner_register_list.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        partner_register_list.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // 레이아웃 메니저

        partner_register_btn = findViewById(R.id.partner_register_btn);
        partner_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), partner_register_product_detail.class);
                startActivity(intent);//액티비티 띄우기
                finish();

            }
        });

        //1. 안드로이드에서 서버로 통신하는곳
        sendRequest("http://13.125.62.22/h_partnerRegisterView.php");
    }

    // 1-1. 가구리스트를 받기위한 서버 통신하는 부분 --------------------------------------------------------
    public void sendRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 1-2. 서버와 통신이 완료(성공)되고 응답을 받아오는곳
                        // 확인하는곳
                        Log.i("php리스폰값 확인", "onResponse: " + response);
                        Log.i("php리스폰값 확인", "된거여 만거여1 " );
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
                                Furname = results.getJSONObject(index).getString("Furname");
                                Furprice = results.getJSONObject(index).getString("Furprice");
                                Furcopy = results.getJSONObject(index).getString("Furcopy");
                                Furcategory = results.getJSONObject(index).getString("Furcategory");
                                FurArck = results.getJSONObject(index).getString("FurArck");
                                FurImage = results.getJSONObject(index).getString("FurImage");
                                sfa = results.getJSONObject(index).getString("sfa");
                                sellerName = results.getJSONObject(index).getString("sellerName");
                                IntFurprice =Integer.parseInt(Furprice);
                                partnerRegisterMainClass = new shopping_list_class(Furname,IntFurprice,Furcopy,Furcategory,FurArck,FurImage,sfa,sellerName);
                                // 만든 오브젝트를 리사이클러뷰를 뿌릴 어레이리스트에 넣어주기
                                partnerRegisterMainList.add(partnerRegisterMainClass);
                                index++;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // 4. 만든 어레이리스트에 리사이클러뷰어댑터를 붙이기
                        partner_register_product_adapter = new partner_register_product_adapter(partnerRegisterMainList); // 어댑터에 리스트 붙이고
                        Log.i("리스트 추가됐는지 안됐는지 확인하기", "onCreate: " + partnerRegisterMainList.size());
                        partner_register_list.setAdapter(partner_register_product_adapter); // 리사이클러뷰에 어댑터 장착
                        partner_register_product_adapter.notifyDataSetChanged();
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



    @Override
    protected void onPause() {
        super.onPause();
        // 리사이클러뷰 새로고침 해줘야댐
        Log.i("새로고침확인", "onPause: 새로고침이 되는건가?");
//        partner_register_product_adapter.notifyDataSetChanged();
    }

    // 리사이클러뷰 콜백함수 시작 -------------------------------------------------------
    com.example.house2.partner.partner_register_product_adapter.onDeleteClickListener onDeleteClickListener1= new com.example.house2.partner.partner_register_product_adapter.onDeleteClickListener() {

        @Override
        public void onDeleteClick(String callbackCk) {
            if(callbackCk.equals("new")){
                sendRequest("http://13.125.62.22/h_partnerRegisterView.php");
                partner_register_product_adapter.notifyDataSetChanged();
            } else {

            }
        }
    };
    // 리사이클러뷰 콜백함수 끝 -------------------------------------------------------

}


