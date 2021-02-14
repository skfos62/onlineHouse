package com.example.house2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.kakao.auth.ApiErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;

public class login extends AppCompatActivity {
    ViewFlipper v_fllipper;
    Button login_btn;
    TextView join_btn;
    TextView join_btn_guset;
    Button login_btn_kakao;
    LoginButton login_btn_kakao_real;

    MeV2Response id2;

    //카카오 로그인
    private SessionCallback sessionCallback;
    // 자동로그인
    String loginId, loginPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 카카오톡 로그인
        Log.e("카카오", "7");
        sessionCallback = new SessionCallback();
        Log.e("카카오", "8");
        Session.getCurrentSession().addCallback(sessionCallback); // 현재 세션에 콜백함수 붙임
        Log.e("카카오", "9");
        Session.getCurrentSession().checkAndImplicitOpen();// 자동로그인
        Log.e("카카오", "10");


        // 키해쉬 구하기
        getAppKeyHash();

        // -------------------------------------------------------------------------------------
        // 뷰플리퍼
        // -------------------------------------------------------------------------------------

        // 뷰플리퍼에 들어가는 아이들

        int images[] = {
                R.drawable.loginimage,
                R.drawable.living2882718640,
                R.drawable.cafe870276640
        };

        v_fllipper = findViewById(R.id.flipper);

        for(int image : images) {
            fllipperImages(image);
        }

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        // 자동로그인 관련
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줍니다.
        loginId = auto.getString("inputId",null);
        loginPwd = auto.getString("inputPwd",null);
        Log.i("자동로그인", "onCreate: " + loginId +" " + loginPwd);

        if(loginId !=null && loginPwd != null) {
            Toast.makeText(login.this, loginId +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(login.this, MainActivity.class);
            intent.putExtra("name",loginId);
            intent.putExtra("pw",loginPwd);
            startActivity(intent);
            finish();
        }


        // -------------------------------------------------------------------------------------
        // 로그인 눌렀을때 나오는 화면 (인텐트) / 회원가입하기 눌렀을때 넘어가는 화면
        // -------------------------------------------------------------------------------------
        //카카오 로그인 (가짜 버튼이라 진짜 버튼이 작동하게 해줘야된다)
        login_btn_kakao = (Button)findViewById(R.id.login_btn_kakao);
        login_btn_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn_kakao_real.performClick();
            }
        });
        login_btn_kakao_real = (LoginButton)findViewById(R.id.login_btn_kakao_real);



        // 로그인 눌렀을때 나오는 화면
        login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(),login_real.class);
                startActivity(intent3);
            }
        });

        // 회원가입을 눌렀을때 나오는 액티비티
        join_btn = findViewById(R.id.join_btn);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),join_select.class);
                startActivity(intent);

            }
        });

        // 비회원으로 보기 누르면 나오는 액티비티
        join_btn_guset = findViewById(R.id.join_btn_guset);
        join_btn_guset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    }
    // -------------------------------------------------------------------------------------
    // 카카오톡 로그인
    // -------------------------------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.e("카카오", "1");
            super.onActivityResult(requestCode, resultCode, data);
            return ;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {

            Log.e("카카오", "열림");
            redirectSignupActivity();
//            String id = id2.getNickname();
//            Log.e("카카오아이디", id);
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e("카카오", "4");
                    int result = errorResult.getErrorCode();
                    Log.e("카카오", "5");
                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(MeV2Response result) {
                    String str = result.getNickname();
                    Log.e("유저아이디테스트", str);
//                    Log.d("카카오테스트 : " + result.getId());
                    Logger.d("카카오테스트: " + result.getKakaoAccount().getEmail());
                    Logger.d("카카오테스트: " + result.getKakaoAccount()
                            .getProfile());
                    Intent intent = new Intent(login.this, MainActivity.class);
                    intent.putExtra("name", str);
                    intent.putExtra("profile", result.getProfileImagePath());
                    startActivity(intent);
                    Log.e("정보보내기", "유저닉네임");
                    finish();


                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    // 로그인을 완료했을때 열리는 부분 (이게 없으면 로그인이 완료가 되어도 다음페이지가 열리지 않는다)
    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    // -------------------------------------------------------------------------------------
    // 뷰플리퍼
    // -------------------------------------------------------------------------------------
    private void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(2000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
}
