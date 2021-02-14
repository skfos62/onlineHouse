package com.example.house2.AddCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.house2.R;
import com.example.house2.buying.buying;

import java.util.ArrayList;

import static com.example.house2.mypage.mypage.staticUser;

public class addChat extends AppCompatActivity {
    public static RecyclerView item_add_list;
    addChatAdapter addChatAdapter;
    ArrayList<addChatItemClass> addChatItemClass;
    ImageView item_add_x2;

    // 총갯수
    TextView tv_addchat_count;
    TextView tv_addchat_sum;

    //구매하기버튼
    Button btn01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);

        getAddData();

        addChatAdapter.notifyDataSetChanged();
        item_add_x2=findViewById(R.id.item_add_x2);
        item_add_x2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 사용자가 장바구니에 추가한 리스트를 저장하는 디비(앱내부에 저장하는것임)
        appDbManager databaseManager = appDbManager.getInstance(getApplicationContext());

        tv_addchat_count = findViewById(R.id.tv_addchat_count);
        tv_addchat_count.setText(String.valueOf(databaseManager.count()));
        tv_addchat_sum = findViewById(R.id.tv_addchat_sum);
        tv_addchat_sum.setText(String.valueOf(databaseManager.sum()));

        // 구매하기 버튼
        // 구매하기 버튼을 누르면 사는 페이지로 넘어가게 된다.
        btn01 = findViewById(R.id.btn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 구매하기 페이지로 넘어갈때 장바구니 리스트를 결제하기 페이지에 넘겨야한다.
                // 이유는 장바구니 리스트를 결제 완료 하게 되면 결제완료하는 테이블에 저장이 되어야하기 때문

                //  어레이리스트를 인텐트로 담아서 보내기 위해서는 클래스에 '직렬화'가 필요하다.
                //  (직렬화 찾아보기)

                Intent intent = new Intent(getApplicationContext(), buying.class);
                intent.putExtra("item_add_list",addChatItemClass);
                startActivity(intent);
            }
        });


    }
    // 장바구니 리스트 새로고침 하는곳
    @Override
    protected void onResume() {
        super.onResume();
        getAddData();
        addChatAdapter.notifyDataSetChanged();

    }

    /**
     * 장바구니 리스트를 저장하는 db에
     * 저장될 정보 목록
     *   String nickname;
     *     String title;
     *     int totalCount;
     *     int sum;
     *     String purchars;
     *     int image;
     *     String date;
     */
    public void getAddData()
    {
        appDbManager databaseManager = appDbManager.getInstance(getApplicationContext());
        addChatItemClass = new ArrayList<>();


        String[] columns = new String[] {"name","nickname", "title", "totalCount", "sum", "purchars", "image", "date"};

        Cursor cursor = databaseManager.query(columns, null, null,null,null,null);

        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                //staticUser.getName();
                if(cursor.getString(0).equals("11")) {
                    addChatItemClass currentData = new addChatItemClass(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(6));
                    addChatItemClass.add(currentData);
                    Log.i("데이터베이스확인 저장이 되었나?", "getAddData: " + currentData.getNickname());
                }
            }
            Log.i("데이터베이스확인", "getAddData: " + addChatItemClass.toString());
        }

        // 리사이클러뷰
//        Log.i("데이터베이스확인2", "getAddData: " + addChatItemClass.get(1).getNickname());
        item_add_list = findViewById(R.id.item_add_list);
        item_add_list.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        item_add_list.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // 레이아웃 메니저
        // 장바구니 어레이리스트임
        // 이거를 buying class로 보내서 해당하는 사람들이 결제한 리스트가 뭔지 알아야한다.
        addChatAdapter = new addChatAdapter(addChatItemClass); // 어댑터에 리스트 붙이고
        item_add_list.setAdapter(addChatAdapter); // 리사이클러뷰에 어댑터 장착
        addChatAdapter.notifyDataSetChanged();

    }
}
