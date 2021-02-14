package com.example.house2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.house2.chat.partner_chatt_view;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.house2.mypage.mypage.staticUser;

/**
 *
 * 쇼핑하기 뷰페이지
 * 여기서 리사이클러뷰에서 받아온 인텐트를 가지고 뷰페이지에 보여줄것
 *
 */
public class shopping_view extends AppCompatActivity implements bottom_sheet_shopping.BottomSheetListener {

    ImageView item_view_image;
    TextView item_view_title;
    TextView item_view_price;
    TextView item_view_sub;
    TextView item_view_sellerName;
    Button view_AR;
    Button itemViewPurchase; // 구매하기 버튼
    String sfbNum;
    // 바텀시트
    LinearLayout linearLayout;
    // 인텐트에서 받아온
    // 채팅시작하는 버튼
    Button chatt_start_btn;
    public static String seller;

    //서버에서 받아오는 변수
    String roomId;
    String userID;
    String sellerID;
    String chatContent;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_view);
        context = this;

        item_view_image = findViewById(R.id.item_view_image);
        item_view_title = findViewById(R.id.item_view_title);
        item_view_price = findViewById(R.id.item_view_price);
        item_view_sub = findViewById(R.id.item_view_sub);
        item_view_sellerName = findViewById(R.id.item_view_sellerName);

        // 인텐트로 shopping 클래스에서 해당하는 데이터의 정보 받아오기
        Intent intent = getIntent();
        // 인텐트로 넘어온 판매자

        // 받아온 인텐트를 셋 텍스트 시키기
        Glide.with(getApplicationContext()).load(intent.getExtras().getString("Image")).into(item_view_image);
//        item_view_image.setImageResource(intent.getExtras().getInt("Image"));
        item_view_title.setText(intent.getExtras().getString("title"));
        item_view_price.setText(String.valueOf(intent.getExtras().getInt("price")));
        item_view_sub.setText(intent.getExtras().getString("sub"));
        if(intent.getExtras().getString("sellerName").equals("part02")){
            seller="파트너사";
            item_view_sellerName.setText(seller);

        } else if (intent.getExtras().getString("sellerName").equals("part03")) {
            seller="파트너사2";
            item_view_sellerName.setText(seller);
        } else if (intent.getExtras().getString("sellerName").equals("part01")){
            seller="123";
            item_view_sellerName.setText(seller);
        }

        sfbNum = intent.getExtras().getString("sfbnum");
        Log.i("인텐트넘기기", "onClick: " + String.valueOf(sfbNum));


        // ar 보는 버튼
        // 여기서 인텐트로 해당하는 sfb 이름을 보내줘야한다.

        view_AR = findViewById(R.id.view_AR);
        view_AR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), shopping_arcore.class);
                intent.putExtra("sfbName",sfbNum);
                intent.putExtra("test","1");
                startActivity(intent);
                Log.i("인텐트넘기기", "onClick: " + sfbNum);
            }
        });


        // 아이템 구매하기 버튼 누르면
        // 바텀 시트
        linearLayout = findViewById(R.id.bottom_sheet);
        //init the bottom sheet view
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        itemViewPurchase = findViewById(R.id.item_view_purchase);
        itemViewPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_sheet_shopping bottomSheet = new bottom_sheet_shopping();
                // 번들로 데이터 보내기
                Bundle bundle = new Bundle();
                bundle.putString("title", item_view_title.getText().toString());
                bundle.putString("price", item_view_price.getText().toString());
                bundle.putString("ImageResource", intent.getExtras().getString("Image"));
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");

            }
        });

        // 채팅시작하기 버튼
        chatt_start_btn = findViewById(R.id.chatt_start_btn);
        chatt_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 그리고 여기서 php로 보내서 방을 만들던지 아니면 기존에 있는 방인지 확인하여야 한다.
                // 채팅방이 있는지 없는지 확인하기
                sendRequest("http://13.125.62.22/h_chatRoomInfoCk.php");


                // 여기에서 어떤 판매자한테 채팅을 걸었는지 채팅 액티비티에 보내주어야 한다.
                // 채팅 액티비티로 바로 가는 인텐트
//                //
//                String roomId;
//                String userID;
//                String sellerID;
//                String chatContent;

            }
        });




    }

    @Override
    public void onButtonClicked(String text) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        //
    }

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
                            // jsonobject인 result에서 하나하나 값을 가져와서 partner_Main_list 클래스 오브젝트로 만드는곳
                            roomId = results.getJSONObject(index).getString("roomId");
                            userID = results.getJSONObject(index).getString("userID");
                            sellerID = results.getJSONObject(index).getString("sellerID");
                            chatContent = results.getJSONObject(index).getString("chatContent");
//                            while (index < results.length()) {
//
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("채팅리스트 리스폰값 확인", "onResponse: 끝!" + response);
                        Intent intent2 = new Intent(context, partner_chatt_view.class);
                        intent2.putExtra("sellerID",sellerID);
                        intent2.putExtra("roomId",roomId);
                        intent2.putExtra("chatContent",chatContent);
                        intent2.putExtra("userID",userID);
                        intent2.putExtra("from","user");
                        startActivity(intent2);
//                        if(response.equals("채팅방생성완료")){
//                            Log.i("채팅리스트 리스폰값 확인", "디비에 채팅방 정보 저장");
//                        } else {
//                            try {
//                                // 제이슨 오브젝트에서 status라는 이름을 가진 어레이를 뽑아내서
//                                JSONObject jObject = new JSONObject(response);
//                                JSONArray results = jObject.getJSONArray("status");
//                                // 하나하나 partner_Main_list_class의 오브젝트로 만들어서 어레이리스트로 만들기
//                                int index = 0;
//                                // jsonobject인 result에서 하나하나 값을 가져와서 partner_Main_list 클래스 오브젝트로 만드는곳
//                                while (index < results.length()) {
//                                    roomId = results.getJSONObject(index).getString("roomId");
//                                    userID = results.getJSONObject(index).getString("userID");
//                                    sellerID = results.getJSONObject(index).getString("sellerID");
//                                    chatContent = results.getJSONObject(index).getString("chatContent");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
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
                Log.i("php리스폰값 확인", "된거여 만거여3 " +staticUser.getNickname());
                Log.i("php리스폰값 확인", "된거여 만거여3 " +seller);
                params.put("seller", seller);
                params.put("userId", staticUser.getNickname());
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

}
