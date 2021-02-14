package com.example.house2.buying;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.house2.AddCart.addChatItemClass;
import com.example.house2.AddCart.appDbManager;
import com.example.house2.AppHelper;
import com.example.house2.MainActivity;
import com.example.house2.R;
import com.example.house2.find_pw;
import com.example.house2.partner.partner_Main;
import com.example.house2.shopping;
import com.example.house2.shopping_view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.BootpayAnalytics;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;

public class buying extends AppCompatActivity {

    Button buying_find_adress;
    TextView buying_total_count;
    WebView webView;
    private TextView txt_address;
    private Handler handler;
    Button buyingBtn; // 구매하기
    int pay;
    private int stuck = 10;
    Context context;

    ArrayList<addChatItemClass> list;
    String productName;
    int productNum;
    String buyer;
    int payment;
    String sellerName;
    String productState;

    // 배송정보 입력하는 인풋텍스트
//    buying_recip_name/ 받는분
//            buying_recip_phone // 휴대전화
//    buying_find_adress2 // 주소검색
//            join_find_adress3 // 주소검색2
    EditText buying_recip_name;
    EditText buying_recip_phone;
    EditText buying_recip_adress;
    EditText buying_recip_adress2;

    String recip_name;
    String recip_phone;
    String recip_adress;
    String recip_adress2;

    int paymnetListSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);

        // 서버에 배송정보 전송을 위해서 배송정보를 얻는곳
        buying_recip_name = findViewById(R.id.buying_recip_name);
        buying_recip_phone = findViewById(R.id.buying_recip_phone);
        buying_recip_adress = findViewById(R.id.buying_recip_adress);
        buying_recip_adress2 = findViewById(R.id.buying_recip_adress2);

        // addchart클래스에서 얻어온 정보
        Intent intent = getIntent();
        list = (ArrayList<addChatItemClass>) intent.getSerializableExtra("item_add_list");
        Log.i("장바구니 리스트 넘어왔는지 확인", "onCreate: " + list.get(0).getTitle());

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필
        BootpayAnalytics.init(this, "5e6a9f3702f57e002e4b4d9e");

        appDbManager databaseManager = appDbManager.getInstance(getApplicationContext());
        // 총 금액 표시하기
        buying_total_count = findViewById(R.id.buying_total_count);
        buying_total_count.setText(String.valueOf(databaseManager.sum()));
        handler = new Handler();
        pay = databaseManager.sum();

        // 주소표시할 텍스트뷰
        if(buying_recip_adress2 != null){

        }
        buying_recip_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // WebView 초기화
                init_webView();
            }
        });

        context = this;
        buyingBtn = findViewById(R.id.btn01);
        buyingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_request(v);

            }
        });

        // 결제
        paymnetListSize = list.size()-1;

    }
    public void onClick_request(View v) {
        // 결제호출
        BootUser bootUser = new BootUser().setPhone("010-1234-5678");
        BootExtra bootExtra = new BootExtra().setQuotas(new int[] {0,2,3});

        Bootpay.init(getFragmentManager())
                .setApplicationId("5e6a9f3702f57e002e4b4d9e") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.INICIS) // 결제할 PG 사
                .setMethod(Method.CARD) // 결제수단
                .setContext(this)
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                .setName(list.get(0).getTitle()+" 외 " +paymnetListSize +" 개" ) // 개결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
                .setPrice(pay) // 결제할 금액
                .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", pay) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 0, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {

                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.d("confirm", message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d("done", message);


                        recip_name = buying_recip_name.getText().toString();
                        recip_phone = buying_recip_phone.getText().toString();
                        recip_adress = buying_recip_adress.getText().toString() + buying_recip_adress2.getText().toString();

                        //  결제가 완료되는  부분
                        //  여기서 고객이 결제완료한 결제 정보를 서버에다가 넣어야 한다.
//                      //  <결제 정보 넣는 부분>
//                      //      1. 상품이름
//                      //      2. 갯수
//                      //      3. 구매자
//                      //      4. 결제금액
//                      //      5. 판매자이름
//                      //      6. 구매날짜
                        //      7. 배송 정보

//                        < 어떻게 결제완료가 된 리스트를 서버에 넣을것인지? >
//                        리스트는 리사이클러뷰로 내용이 뿌려지는데 리사이클러뷰는 어레이리스트에 있는 내용을 가지고뿌려주는것
//                        따라서 어레이리스트 안에 있는 내용을 저장하는 것을 for문을 사용하여서 서버에 하나하나 저장해주자

//                          1. addCart에 있는 장바구니 리스트(어레이리스트)를 받아온다.
                        Intent intent2 = getIntent();
                        list = (ArrayList<addChatItemClass>) intent2.getSerializableExtra("item_add_list");
                        Log.i("어레이리스트가 제대로 받아져왔나?", "onDone: " +list.toString());

//                          2. 받아온 어레이 리스트의 index크기 만큼 for문 안에 서버에 저장하는 코드를 쓴다.
                        for(int i = 0 ; i < list.size() ; i ++ ){
                            // 여기서 하나하나 서버로 저장한다.
                            // http 라이브러리를 사용해서 mysql에 저장하기

//                            "nickname TEXT," + // 구매자 아이디
//     *\                 "title TEXT," +    // 가구이름
//     *                 "totalCount INTEGER," + // 총 갯수
//     *                 "sum INTEGER," +  // 가격
//     *                 "purchars TEXT," + // 구매상태? (배송상태로 바꿔야함)
//     *                 "image INTEGER," + // 이미지 링크
//     *                 "date DEFAULT CURRENT_TIMESTAMP);"); // 산 날짜
                            Log.e("포문으로 보냄", "onDone: 포문으로 보냈음 ");
                            productName = list.get(i).getTitle();
                            productNum = list.get(i).getTotalCount();
                            buyer = list.get(i).getName();
                            payment = list.get(i).getSum();
                            sellerName = "part02" ;
                            productState= list.get(i).getPurchars();
                            sendRequest("http://13.125.62.22/h_saleInfoSend.php");

                            Log.e("포문으로 보냄", "onDone: 포문으로 보냈음 " + productName);

                            if (AppHelper.requestQueue == null)
                                AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
                        }

                        Toast.makeText(context, "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        // 여기서 결제가 완료되면 장바구니 리스트에서 사라지게 해야함
                        // 리스트 all delete 시작 ----------------------------------------------------
                        appDbManager databaseManager = appDbManager.getInstance(v.getContext());
                        databaseManager.alldelete(v);
                        // 리스트 all delete 끝 ----------------------------------------------------
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {

                        Log.d("cancel", message);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Log.d("error", message);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                Log.d("close", "close");
                            }
                        })
                .request();

    }


    private void init_webView() {

        // WebView 설정
        webView = (WebView) findViewById(R.id.webView_address);

        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");

        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());

//        /* 2019.03.21 sjwiq200 Cross App Scripting 대비 */
//        webView.getSettings().setDatabaseEnabled(false);
//        webView.getSettings().setAllowFileAccess(false);
//        webView.getSettings().setDomStorageEnabled(false);
//        webView.getSettings().setAppCacheEnabled(false);

        // webview url load. php 파일 주소
        webView.loadUrl("http://13.125.62.22/h_adress.php");
//        webView.loadUrl("http://www.naver.com");


    }


    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    txt_address.setText(String.format("(%s) %s %s", arg1, arg2, arg3));

//                    // WebView를 초기화 하지않으면 재사용할 수 없음
//                    init_webView();
                }
            });
        }
    }

    // -------------------------------------------------------------------------------------
    // http 통신하는 부분 (로그인하는부분 / volley)
    // -------------------------------------------------------------------------------------

    public void sendRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("저장완료", "onResponse: 저장완료");
                        // php로 정보를 보내는게 완료 되면 나오는 곳
                        // 로그인 되나 안되나

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "보내기 실패", Toast.LENGTH_LONG).show();
                        Log.e("에러확인", "onErrorResponse: " + error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //여기다가 서버에 보낼 데이터값을 넣어준다.
                params.put("productName", productName);
                params.put("productNum", String.valueOf(productNum));
                params.put("buyer", buyer);
                params.put("payment", String.valueOf(payment));
                params.put("sellerName", sellerName);
                params.put("productState", productState);
                params.put("recip_name", recip_name);
                params.put("recip_phone", recip_phone);
                params.put("recip_adress", recip_adress);
                return params;
            }
        };

//        // 이전 결과가 있더라도 새로 요청
        AppHelper.requestQueue = Volley.newRequestQueue(this);

        request.setShouldCache(false);

        AppHelper.requestQueue.add(request);

//        Toast.makeText(getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();

    }





}
