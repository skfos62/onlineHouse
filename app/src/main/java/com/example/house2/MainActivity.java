package com.example.house2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.house2.home.home;
import com.example.house2.mypage.mypage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    FrameLayout frameLayout;
    public static String strNickname, strProfile;

    // 회원정보를 담아두는 무언가가 필요하다. 스태틱으로 해놓을까

    // 프래그먼트
    private com.example.house2.home.home home;
    private com.example.house2.mypage.mypage mypage;
    private search search;
    private community community;
    private shopping shopping;
    Intent intent2;
    MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.bottomNavigationView2);
        frameLayout = findViewById(R.id.frameLayout);

        // 프래그먼트 인슐라이제이션
        home = new home();
        mypage = new mypage();
        search = new search();
        community = new community();
        shopping = new shopping();

        Intent intent = getIntent();

        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");

        Log.e("번들도착2", String.valueOf(strNickname));
        Log.e("번들도착2", String.valueOf(strProfile));
        Bundle bundle = new Bundle();
        bundle.putString("name",strNickname);
        bundle.putString("profilePicture",strProfile);
        mypage.setArguments(bundle);

        // 해당하는 프래그먼트를 띄우는 함수를 넣어줘야지 메인에 접속했을때 해당하는 프래그먼트가 뜬다.
        InitializeFragment(home);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // 경우의 수 지정하기
                switch (menuItem.getItemId()){
                    case R.id.navigation_home :
                        // 홈
                        InitializeFragment(home);
                        return true;
                    case R.id.navigation_community:
                        // 구경하기
                        InitializeFragment(community);
                        return true;
                    case R.id.navigation_shopping:
                        // 쇼핑하기
                        InitializeFragment(shopping);
                        return true;
                    case R.id.navigation_mypage :
                        // 마이페이지
                        InitializeFragment(mypage);
                        return true;

                }
                return false;
            }
        });

}


    @Override
    protected void onResume() {
        super.onResume();

    }


    public void InitializeFragment(Fragment fragment) {
        // 프래그먼트 트랜지션?
        // 약간 어댑터 같은건가 프래그먼트를 누를때 해당하는 프레임 레이아웃을 띄운다는?
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
