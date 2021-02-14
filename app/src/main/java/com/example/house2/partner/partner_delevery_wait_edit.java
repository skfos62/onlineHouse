package com.example.house2.partner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.house2.AppHelper;
import com.example.house2.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.house2.login_real.loginIdInfo;
import static com.example.house2.login_real.loginIdInfoNick;

public class partner_delevery_wait_edit extends AppCompatActivity {

    EditText delevery_wait_edit_adress;
    // 스피너 관련 선언
    Spinner delevery_wait_edit_select_spinner;
    ArrayAdapter arrayAdapter;
    String SelectDelevery;

    EditText delevery_wait_edit_num;
    Button delevery_wait_save;
    Button delevery_wait_cancle;

    // 송장번호 입력하는곳 정보
    String adress;
    String deleveryNum;


    // 리사이클러뷰에서 정보 받아오는곳
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_delevery_wait_edit);

        /**
         * 송장번호를 입력하면 주소, 구매자이름, 송장번호, 결제정보id, 택배사를 저장하는 db(h_partnerDeleveryInfoInsert.php)
         *  db 이름 : delevery_info
         *  주소 : adress (text)
         *  구매자이름 : buyer (text)
         *  송장번호 : deleveryNum (int)
         *  결제정보id : saleNum (int)
         *  택배사 이름 : companyName (text)
         *  받는사람 이름: Recipient (text)
         *
         *
         * <송장 번호 저장하는 로직>
         *     1. 리사이클러뷰에서 해당하는 제품의 정보를 알아온다 (ex. 주소 등등.. )
         *     2. 정보를 edit텍스트에 뿌려준다.
         *     3. 스피너로 택배사를 선택한다.
         *     4. 송장 번호를 입력한다.
         *     5. 서버(h_partnerDeleveryEdit.php)로 택배의 상태를 택배 배송중으로 바꿔준다.
         *        5-1. db에 해당하는 배송번호를 저장한다.
         *     6. 배송관리 액티비티를 켜준다.
         *     </송장>
         */

        // 1. 리사이클러뷰에서 판매자가 클릭한 제품의 정보를 알아온다.
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        productName = intent.getStringExtra("productName");
        buyer = intent.getStringExtra("buyer");
        payment = intent.getStringExtra("payment");
        sellerName = intent.getStringExtra("sellerName");
        date = intent.getStringExtra("date");
        productState = intent.getStringExtra("productState");
        Recipient = intent.getStringExtra("Recipient");
        RecipAdress = intent.getStringExtra("RecipAdress");
        Reciphone = intent.getStringExtra("Reciphone");
        // 2. 정보를 edittext에 뿌려준다.
        //    정보를 보여주는 editTExt 선언
        delevery_wait_edit_adress = findViewById(R.id.delevery_wait_edit_adress);
        delevery_wait_edit_adress.setText(RecipAdress);
        adress = delevery_wait_edit_adress.getText().toString();

        // 2-1. 송장번호 입력한것
        delevery_wait_edit_num = findViewById(R.id.delevery_wait_edit_num);


        // 3. 스피너로 택배사를 선택한다.
        //      스피너 관련 한 부분 시작 ----------------------------------------------------------------
        //      레이아웃에서 스피너 부분에 넣어줄 텍스트
        // 3-1. 어레이리스트를 만들어서
        ArrayList spinnerDelevery = new ArrayList();
        spinnerDelevery.add("우체국택배");
        spinnerDelevery.add("대한통운");
        spinnerDelevery.add("로젠택배");
        spinnerDelevery.add("경동택배");
        spinnerDelevery.add("선택해주세요.");
        // 3-2. 스피너 선언을 해주고
        delevery_wait_edit_select_spinner = findViewById(R.id.delevery_wait_edit_select_spinner);
        // 3-3. 해당 스피너의 어댑터에 어레이리스트를 붙여준다.
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,spinnerDelevery);
        delevery_wait_edit_select_spinner.setAdapter(arrayAdapter);
        delevery_wait_edit_select_spinner.setSelection(arrayAdapter.getCount()-1);
        delevery_wait_edit_select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 서버에 저장할때 카테고리의 이름은 furniture,chair,table,etc 이기 때문에 여기서 바꿔주어야 한다.
                SelectDelevery = (String) spinnerDelevery.get(position);
                Log.i("스피너에 제대로 찍혔는지 확인하기", "onItemSelected: " + SelectDelevery);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //      스피너 관련 한 부분 끝 ------------------------------------------------------------------

        // 저장하기 버튼을 누르는곳
        delevery_wait_save = findViewById(R.id.delevery_wait_save);
        delevery_wait_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 5. 서버로 택배의 상태를 택배 배송중으로 바꿔준다.
                sendRequest("http://13.125.62.22/h_partnerDeleveryEdit.php");

                Intent intent2 = new Intent(getApplicationContext(),partner_delevery.class);
                startActivity(intent2);
                finish();
            }
        });

    }
    // 5. 택배의 상태를 바꿔주기 위해 서버 통신하는 부분 --------------------------------------------------------
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
                        // 5-1. 다시 쓰레드를 돌려서 디비에 저장한다.
                        // 송장번호 입력한것을 가져오는곳
                        deleveryNum = delevery_wait_edit_num.getText().toString();
                        Log.i("송장번호 로그", "onCreate: " + deleveryNum);
                        sendRequest2("http://13.125.62.22/h_partnerDeleveryInfoInsert.php");
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
                params.put("id", id);
                return params;
            }
        };
        // 이전 결과가 있더라도 새로 요청
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
    // 매출리스트를 받기위한 서버 통신하는 부분 끝 ------------------------------------------------------

    // 5-1. 택배의 상태를 바꿔주기 위해 서버 통신하는 부분 --------------------------------------------------------
    public void sendRequest2(String url) {

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
                params.put("adress", adress);
                params.put("buyer", buyer);
                params.put("deleveryNum", deleveryNum);
                params.put("saleNum", id);
                params.put("companyName", SelectDelevery);
                params.put("sellerName", loginIdInfo);
                params.put("Recipient", Recipient); // 받는사람도 함께 보내줘야함
                return params;
            }
        };
        // 이전 결과가 있더라도 새로 요청
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
    // 매출리스트를 받기위한 서버 통신하는 부분 끝 ------------------------------------------------------

}
