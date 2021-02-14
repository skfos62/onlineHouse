package com.example.house2.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.house2.R;
import com.example.house2.login_real;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.house2.login_real.loginIdInfoNick;
import static com.example.house2.mypage.mypage.staticUser;
import static com.example.house2.shopping_view.seller;

public class partner_chatt_view extends AppCompatActivity {
    // 채팅 리스트 관련 선언
    //서버에서 받아오는 변수
    static String roomId;
    String userID;
    String sellerID;
    String chatContent;
    static Context context;
    String getSeller;

    //서버에서 받아오는 변수2
    static String roomId2;
    String userID2;
    String sellerID2;
    String chatContent2;
    static Context context2;
    String getSeller2;

    // 채팅관련 선언들
    EditText et;
    EditText et2;
    TextView msgTV;
    public static String serVicemsg;
    private MyService mBindService;
    partner_chatt_view partner_chatt_view;



    String getWho;
    String getUserId;

    static String inputString;


    // 방넘버
    int RoomNum= 0;
    // 내가 채팅을 요청할사람
    String guestId="파트너사";


    // 리사이클러뷰 관련 선언
    BufferedReader msgReder;

    // 채팅 리사이클러부분
    public static ArrayList<DataItem> dataList;
    public static ArrayList<DataItem> realDsataList = new ArrayList<>();
    public static MyAdapter ReAdapter;
    public static RecyclerView recyclerView;

    // 소켓선언
    // 소켓을 넣는곳
    public static Socket socket;

    public static   Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        MyService.socket = socket;
    }


    // tcp서버에서 데이터가 오는것을 감지하기위해서 브로드캐스트리시버를 사용했다.
    // 브로드캐스트 리시버 관련한 메소드 모음 시작 ---------------------------------------------------------------
    // 서비스랑 액티비티랑 텍스트 왔다갔다 ------------------------------------------------------------------

    @Override
    protected void onResume() {
        super.onResume();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("com.my.app");
//        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
//        registerReceiver(receiver, intentFilter);
//
//        startService(new Intent(this,MyService.class));
        // 이제 받아온 데이터를 소켓 서비스에 넣어야 한다.
        // 소켓연결할때 어떤 아이디로 소켓 연결을 할것인가 결정하는곳

//        Intent intentChat = getIntent();
//        getSeller = intentChat.getStringExtra("seller");
//        String getWho = intentChat.getStringExtra("from");

//
////        if(getWho.equals("user")){
//            MyService.realIDck = staticUser.getNickname();
//        } else {
//            MyService.realIDck = loginIdInfoNick;
//        }


    }


    //ui 작업 쓰레드 -----------------------------------------------------------------------------------
   static Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if(msg.what == 1) {
                // 쓰레드 작업할거
                // 해당 작업을 처리함
                sendRequest("http://13.125.62.22/h_chatRoomContentSave.php");
                if(dataList.get(dataList.size()-1).getRoomNum().equals(roomId)){
                    realDsataList.add(dataList.get(dataList.size()-1));
                    ReAdapter.notifyDataSetChanged();
                    Log.i("화면변경받는 ui ", "run: InputThread : " + realDsataList.size());
                    Log.i("화면변경받는 ui ", "run: InputThread : 룸넘버" + dataList.get(dataList.size()-1).getRoomNum());

                    // 대화내용 저장 하기 --------------------------------------------------------------
                    Gson gson = new Gson();
                    inputString = gson.toJson(realDsataList);
                    System.out.println("inputString= " + inputString);
                    // 대화내용 저장 하기 --------------------------------------------------------------
                    // 저장하기
                    recyclerView.scrollToPosition(ReAdapter.getItemCount()-1);

                } else {
                    Log.i("방번호가 안맞아서 들어온곳 ", "방번호가 안맞는곳" );
                }

//                for(int i = 0; i < dataList.size() ;i++){
//                    // 여기서 방번호를 판별하여서 방에 맞는 데이터를 넣어주자 ㅏ
//                    if(dataList.get(i).getRoomNum().equals(roomId)){
//                        realDsataList.add(dataList.get(i));
//                        ReAdapter.notifyDataSetChanged();
//                        Log.i("화면변경받는 ui ", "run: InputThread : " + realDsataList.size());
//                        Log.i("화면변경받는 ui ", "run: InputThread : 룸넘버" + dataList.get(i).getRoomNum());
//                        recyclerView.scrollToPosition(ReAdapter.getItemCount() - 1);
//                    } else {
//                        Log.i("방번호가 안맞아서 들어온곳 ", "방번호가 안맞는곳" );
//                    }
//                }
            }
            return true;

        }

    });
    //ui 작업 쓰레드 -----------------------------------------------------------------------------------

    // 서버에서 데이터를 받아와서 처리하기 위해서 브로드캐스트 설정
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String state = extras.getString("extra");
            Log.i("채팅화면", "onReceive: 값 받아가지고 왔음");
            // 메인 ui 바꿔주는 쓰레드
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    handler.sendEmptyMessage(1);
//                }
//            });
//            thread.start();
//            // 데이터가 오면 액티비티 내부의 ui화면을 바꿔주기 위해서 넣어주는 쓰레드
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    for(int i = 0; i < dataList.size()-1;i++){
//                        // 여기서 방번호를 판별하여서 방에 맞는 데이터를 넣어주자 ㅏ
//                        if(dataList.get(i).getRoomNum().equals(roomId)){
//                            realDsataList.add(dataList.get(i));
//                            ReAdapter.notifyDataSetChanged();
//                            Log.i("화면변경받는 ui ", "run: InputThread : " + realDsataList.size());
//                            recyclerView.scrollToPosition(ReAdapter.getItemCount() - 1);
//                        } else {
//                            Log.i("방번호가 안맞아서 들어온곳 ", "방번호가 안맞는곳" );
//                        }
//                    }
////                    ReAdapter.notifyDataSetChanged();
////                    Log.i("채팅화면", "run: " + dataList.size());
////                    recyclerView.scrollToPosition(ReAdapter.getItemCount() - 1);
//                }
//            });
        }
    }

    // service connection definition ----------------------------------------------------------------
    //서비스 바인딩하는거를 선언하는 클래스
    // 여기가 찐으로 연결이 되어있는곳
    private ServiceConnection mConnection2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.BindServiceBinder binder = (MyService.BindServiceBinder) service;
            mBindService = binder.getService(); // get service.
            mBindService.registerCallback(mCallback); // callback registration

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBindService = null;
        }
    };

    // call below callback in service. it is running in Activity.
    private final MyService.ICallback mCallback = new MyService.ICallback() {
        @Override
        public void remoteCall() {

//            // 메인 ui 바꿔주는 쓰레드
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    handler.sendEmptyMessage(1);
//
//                }
//            });
//            thread.start();

            Log.d("MainActivity", "called by service");

        }
    };
    // service connection definition ----------------------------------------------------------------

    @Override
    protected void onStop() {
        super.onStop();
        sendRequest("http://13.125.62.22/h_chatRoomContentSave.php");
//       startService(new Intent(this,MyService.class));
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    // 여기서 서비스에 있는 소켓 정보를 미리 알아두자


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        sendRequest("http://13.125.62.22/h_chatRoomContentSave.php");
        // 여기서 인텐트로 채팅 리스트를 띄워볼까?
//        Intent intent = new Intent(getApplicationContext(), partner_chatt.class);
//        startActivity(intent);
    }

    // 브로드캐스트 리시버 관련한 메소드 모음 끝---------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_chatt_view);
        context = getApplicationContext();
//        // 서버에 저장된 채팅 내용 불러오기
//        ResponseChat("http://13.125.62.22/h_chatRoomContentView.php");

        Intent intentChat = getIntent();
        getSeller = intentChat.getStringExtra("sellerID");
        getWho = intentChat.getStringExtra("from");
        getUserId = intentChat.getStringExtra("userID");
        roomId = intentChat.getStringExtra("roomId");
        // 제이슨으로 저장된것!
        chatContent = intentChat.getStringExtra("chatContent");
        Log.i("넘어온인텐트확인", "onCreate: "+getSeller);
        Log.i("넘어온인텐트확인", "onCreate: "+getWho);
        Log.i("넘어온인텐트확인", "onCreate: "+getUserId);
        Log.i("넘어온인텐트확인", "onCreate: "+roomId);
        Log.i("넘어온인텐트확인", "onCreate: "+chatContent);

        // 채팅 시작하는 곳과 관련된 클래스 및 메소드 들 ------------------------------------------------------
//        intent.putExtra("seller",seller);
//        intent.putExtra("test","1");
//        intent.putExtra("seller",seller);
//        intent.putExtra("from","user");
//        intent.putExtra("roomId",roomId);
//        intent.putExtra("chatContent",chatContent);
//        intent.putExtra("userID",userID);


//        if(getWho.equals("user")){
//            login_real.realIDck = staticUser.getNickname();
//        } else {
//            login_real.realIDck = loginIdInfoNick;
//        }
        partner_chatt_view = new partner_chatt_view();
        // 서비스 바인딩 모음
        Log.i("소켓서비스가 시작이되어야한다.", "onCreate: 소켓서비스시작 ");
        // 이게 있어야 서비스랑 바인딩한다!
        Intent Service = new Intent(this, MyService.class);
        Log.i("소켓서비스가 시작이되어야한다.", "onCreate: 소켓서비스시작 되나?  ");

        bindService(Service, mConnection2, Context.BIND_AUTO_CREATE);
        Log.i("소켓서비스가 시작이되어야한다.", "onCreate: 소켓서비스시작 되나?  ");

        // 메인 ui 바꿔주는 쓰레드
//        new test().start();

        // 내용입력하는 에디트텍스트
        et = (EditText) findViewById(R.id.EditText01);
        // 채팅화면에서 보내기 버튼 내용 보내기 시작 --------------------------------------------------------
        ConstraintLayout btn = (ConstraintLayout) findViewById(R.id.Button01);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().toString() != null || !et.getText().toString().equals("")) {
                    try {
                        // 메세지 보내는 메소드
                        /**
                         * <서비스 채팅로직>
                         *     1. 메세지를 서비스로 보낸다.
                         *     2. 서비스에서 tcp서버로 채팅을 보낸다.
                         *     3.
                         *     </서비스>
                         */
                        Log.i("chattActivity", "onClick: 보내기버튼 클릭");
                        if(getWho.equals("user")) {
                            // 채팅 시작하는곳
                            // 유저쪽에서 먼저 채팅을 시작할때
                            serVicemsg =  getUserId +":"+roomId +":"+getSeller +":" + et.getText().toString();
                            Log.i("chattActivity", "onClick: 보내기버튼 클릭 " + serVicemsg);
                            mBindService.myServiceFunc();
                            et.setText(null);
//                            sendRequest("http://13.125.62.22/h_chatRoomContentSave.php");
                        } else if(getWho.equals("seller")) {
                            // 판매자 측에서 채팅 시작할때
                            serVicemsg =  getSeller +":"+roomId +":"+getUserId +":" + et.getText().toString();
                            Log.i("chattActivity", "onClick: 보내기버튼 클릭 " + serVicemsg);
                            mBindService.myServiceFunc();
                            et.setText(null);
//                            sendRequest("http://13.125.62.22/h_chatRoomContentSave.php");
                        }


                    } catch (Exception e){
                    }

                } else {

                }
            }
        });
        // 채팅화면에서 보내기 버튼 내용 보내기 끝-- --------------------------------------------------------


        // 채팅화면에서 구성하는 리사이클러뷰 관련 시작--------------------------------------------------------
        /**
         * <서버에서 받아온 데이터 리사이클러뷰에 뿌려주는 로직>
         *     1. 서버로 보낼때 특정한 규칙을 가지고 보낸다.
         *     2. 받아온 특정한 규칙을 가진 데이터를 spilt한다.
         *     3. spilt한 데이터를 오브젝트로 만든다.
         *     4. 만든 오브젝트를 리사이클러뷰의 어레이리스트 안에 넣어준다.
         *     </서버에서>
         *
         *     <서버에서 만든 리사이클러뷰 안에 내용을
         *     realDsataList 방 넘버에 맞게 여기에 옮겨서 다시 만들자!>
         */
        dataList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);

        // 실제 방번호에 맞는 데이터를 넣어줄곳
        realDsataList = new ArrayList<>();
        // 저장된 대화 내용 불러오기 시작 ----------------------------------------------------------------
        // 여기서 이제 저장된 리스트를 불러서 datalist에 넣어주기
        // 1. 채팅이 저장된것을 받아온다 그리고 gson으로 파싱하여서 리사이클러뷰 arraylist에 넣어준다.
        //    전송할 정보 : 로그인한 유저타입, 로그인한 사람의 아이디

//        chatContent = intentChat.getStringExtra("chatContent");
        try {
            JSONArray jsonArray = new JSONArray(chatContent);
            for(int i = 0 ; i<jsonArray.length()+1; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String RoomNum = jsonObject.getString("RoomNum");
                String content = jsonObject.getString("content");
                String name = jsonObject.getString("name");
                String viewType = jsonObject.getString("viewType");
                if(name.equals(loginIdInfoNick)){
                    realDsataList.add(new DataItem(content, name,1, RoomNum));
                } else {
                    realDsataList.add(new DataItem(content, name,0, RoomNum));
                }
//                realDsataList.add(new DataItem(content, name, Integer.parseInt(viewType), RoomNum));
                // 데이터 아이템 만들기
                // 그리고 리사이클러뷰 어레이리스테 추가하기
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 저장된 대화 내용 불러오기 끝 ----------------------------------------------------------------
        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        ReAdapter = new MyAdapter(realDsataList);
        recyclerView.setAdapter(ReAdapter);// Adapter 등록
//        dataList.add(new DataItem("채팅방에 입장하셨습니다.", null, Code.ViewType.CENTER_CONTENT,String.valueOf(RoomNum)));
        ReAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(ReAdapter.getItemCount()-1);



    // 채팅화면에서 구성하는 리사이클러뷰 관련 끝 -----------------------------------------------------------
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable(){
//                    @Override
//                    public void run() {
//                        // 해당 작업을 처리함
//                        for(int i = 0; i < dataList.size()-1;i++){
//                            // 여기서 방번호를 판별하여서 방에 맞는 데이터를 넣어주자 ㅏ
//                            if(dataList.get(i).getRoomNum().equals(roomId)){
//                                realDsataList.add(dataList.get(i));
//                                ReAdapter.notifyDataSetChanged();
//                                Log.i("화면변경받는 ui ", "run: InputThread : " + realDsataList.size());
//                                recyclerView.scrollToPosition(ReAdapter.getItemCount() - 1);
//                            } else {
//                                Log.i("방번호가 안맞아서 들어온곳 ", "방번호가 안맞는곳" );
//                            }
//                        }
//                    }
//                });
//            }
//        }).start();

    }

    // 1. 대화내용 저장을 위한서버 통신하는 부분 --------------------------------------------------------
    public void ResponseChat(String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
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
                            while (index < results.length()) {
                                roomId2 = results.getJSONObject(index).getString("roomId");
                                userID2 = results.getJSONObject(index).getString("userID");
                                sellerID2 = results.getJSONObject(index).getString("sellerID");
                                chatContent2 = results.getJSONObject(index).getString("chatContent");
                            }
                            // 여기서 이제 chatContent를 realdataList에 넣어야한다.
                            Log.i("gson 확인하기 ", "onErrorResponse: " + chatContent2.toString());

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
//                $seller=$_POST['seller'];
//                $userId=$_POST['userId'];
                // 룸넘버와 대화내용을 받아오기
                // $typeID=$_POST['typeID'];
                //      $Roomid=$_POST['Roomid'];
                params.put("Roomid", roomId);
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


    // 2. 저장된 대화내용을 불러오기 위한 통신하는 부분 --------------------------------------------------------
    public static void sendRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
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

//                        try {
//                            // 제이슨 오브젝트에서 status라는 이름을 가진 어레이를 뽑아내서
//                            JSONObject jObject = new JSONObject(response);
//                            JSONArray results = jObject.getJSONArray("status");
//                            // 하나하나 partner_Main_list_class의 오브젝트로 만들어서 어레이리스트로 만들기
//                            int index = 0;
//                            // jsonobject인 result에서 하나하나 값을 가져와서 partner_Main_list 클래스 오브젝트로 만드는곳
//                            while (index < results.length()) {
//                                roomId = results.getJSONObject(index).getString("roomId");
//                                userID = results.getJSONObject(index).getString("userID");
//                                sellerID = results.getJSONObject(index).getString("sellerID");
//                                chatContent = results.getJSONObject(index).getString("chatContent");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

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
                // 룸넘버와 대화내용을 받아오기
//                $RoomInfo=$_POST['RoomInfo'];
//                $Content=$_POST['Content'];
                params.put("RoomInfo", roomId);
                params.put("Content", inputString);
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
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection2);
    }
}




