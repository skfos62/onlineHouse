package com.example.house2.partner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

import static com.example.house2.AddCart.addChatAdapter.date;
import static com.example.house2.login_real.loginIdInfo;

public class partner_delevery_Info extends AppCompatActivity {

    //앞쪽에서 받아오는 id
    String deleveryInfoId;
    String deleveryInfoProductName;

    // 서버에서 데이터 받아오는 변수
    String adress;
    String buyer;
    String deleveryNum;
    String saleNum;
    String Recipient;
    String companyName;

    TextView delevery_product_Name;
    TextView delevery_product_Num;
    TextView delevery_product_companyName;
    TextView delevery_product_adress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_delevery_info);

        // 레이아웃 텍스트뷰 선언
        delevery_product_Name = findViewById(R.id.delevery_product_Name);
        delevery_product_Num = findViewById(R.id.delevery_product_Num);
        delevery_product_companyName = findViewById(R.id.delevery_product_companyName);
        delevery_product_adress = findViewById(R.id.delevery_product_adress);

        /**
         * 송장번호를 입력한 배송지를 보여주는 정보화면
         * 배송관리 액티비티에서 인텐트로 넘어온 정보를 가지고 setText를 시켜야한다.
         * id 값을 받아와서 delevery_info 디비에 해당하는 아이디값이 있는지 보고
         * 값을 받아와야한다.
         *
         * <선택한 배송지가 보여지게 하기>
         *  1. 정보가 잘 넘어오는지 안넘어 오는지 먼저 확인하기
         *  2. 정보가 넘어오면 http 통신을 통해서 데이터를 받아온다. (h_partnerDeleveryInfoInsert.php)
         *  3. json 오브젝트를 통해서 서버에서 데이터를 받아온다.
         *
         *     </선택한>
         *     String adress;
         *     String buyer;
         *     String deleveryNum;
         *     String saleNum;
         *     String companyName;
         *     String Recipient;
         */
//
        Intent intent = getIntent();
        deleveryInfoId =intent.getExtras().getString("id");
        deleveryInfoProductName =intent.getExtras().getString("productName");
        Log.i("송장번호 왔는지 확인 ", "onCreate: " + deleveryInfoId );
        Log.i("상품이름 왔는지 확인 ", "onCreate: " + deleveryInfoProductName );
        sendRequest("http://13.125.62.22/h_partnerDeleveryInfoView.php");


    }

    // 2. 매출리스트를 받기위한 서버 통신하는 부분 --------------------------------------------------------
    public void sendRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                            adress = results.getJSONObject(0).getString("Adress");
                            deleveryNum = results.getJSONObject(0).getString("deleveryNum");
                            buyer = results.getJSONObject(0).getString("Buyer");
                            saleNum = results.getJSONObject(0).getString("saleNum");
                            companyName = results.getJSONObject(0).getString("companyName");
                            Recipient = results.getJSONObject(0).getString("Recipient");
                            // 받아온데이터값을 클래스로 만들어야함
                            partner_delevery_info_class partnerInfoClass = new partner_delevery_info_class(adress,buyer,deleveryNum,saleNum,companyName,Recipient);
                            // 내용 setText하기
                            delevery_product_Name.setText(deleveryInfoProductName);
                            delevery_product_Num.setText(partnerInfoClass.getDeleveryNum());
                            delevery_product_companyName.setText(partnerInfoClass.getCompanyName());
                            delevery_product_adress.setText(partnerInfoClass.getAdress());


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                params.put("saleNum", deleveryInfoId);
                return params;
            }
        };
        // 이전 결과가 있더라도 새로 요청
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
    // 매출리스트를 받기위한 서버 통신하는 부분 끝 ------------------------------------------------------

}
