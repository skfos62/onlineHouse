package com.example.house2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class community_image_tagging extends AppCompatActivity  {
    ImageView view_image;
    Uri uri;
    RelativeLayout relative_layout;
    TextView tag_save;
    ImageView community_tag_x;
    private final int REQUEST_CODE_BRAVO = 100;

    //좌표값
    float x;
    float y;

    String ch;
    Intent intent;
    Bundle bundle;
    String testValue;
    String result;
//    ArrayList<community_image_tagging_obj> tag_obj = new ArrayList<>();
    JSONArray tag_obj2 = new JSONArray();
    JSONObject object = new JSONObject();
    public static ArrayList<String> arr = new ArrayList();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BRAVO) {
//            Toast.makeText(this, "결과가 성공이 아님.", Toast.LENGTH_SHORT).show();
            ch = "갔다왔따";
            String result;
            if (resultCode == RESULT_OK) {
                JSONArray community_image_tagging_obj = new JSONArray();
                result = data.getStringExtra("name");
                String result2 = result.toString();
//                Toast.makeText(this, "Result: " + data.getStringExtra("name"), Toast.LENGTH_SHORT).show();
                System.out.println("결과확인  "+result);

                // 오브젝트 제작후
                // 어레이에 넣어서
                // 다시 오브젝트 로 넣는다
                JSONObject sel = new JSONObject();
                try {
                    sel.put("x",x);
                    sel.put("y",y);
                    sel.put("name",result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                arr.add(sel.toString());
                System.out.println("어레이리스트에담겼니  "+arr.get(0));
                System.out.println("어레이리스트에담겼니  "+arr.size());
                System.out.println("어레이리스트에담겼니  "+arr.toString());
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_image_tagging);
        Intent intent = getIntent();
        //String imageURI = intent.getStringExtra("image");
        uri = intent.getParcelableExtra("image");
        relative_layout = findViewById(R.id.relative_layout);

        // 닫기 누르면 닫히게
        community_tag_x = findViewById(R.id.community_tag_x);
        community_tag_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // -------------------------------------------------------------------------------------
        // 태그 한뒤에 저장하기 버튼을 누르는 곳
        // -------------------------------------------------------------------------------------
        // 태그 한뒤 저장하기를 누르면
        tag_save = findViewById(R.id.tag_save);
        tag_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tag_obj 넘겨야할 어레이리스트
                Intent intent2 = new Intent();
                // 객체 생성자의 인자에 아무 것도 넣지 않는다.
//                intent2.putExtra("tag_obj", tag_obj.toString());
//                bundle.putString(tag_obj.toString());
                setResult(RESULT_OK, intent2);
                finish();
            }
        });
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        view_image = findViewById(R.id.view_image);
        view_image.setImageURI(uri);

        view_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = event.getX();
                y = event.getY();
                System.out.println(x);
                System.out.println(y);
                switch (event.getAction()) {
                    //Down이 발생한 경우
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();

                        //내가 누른 자리의 좌표를 String 값으로 표현할 변수
                        final String str;
                        str = "Coordinate : ( " + (int) x + ", " + (int) y + " )";
                        //터치 한 곳에 이미지를 표현하기 위해 동적으로 ImageView 생성
                        ImageView img = new ImageView(v.getContext());
                        img.setImageResource(R.drawable.tag124);

                        //이미지가 저장될 곳의 x,y좌표를 표현
                        img.setX(x - 40);
                        img.setY(y - 90);
                        //최상단 릴레이티브 레이아웃에 이미지를 Add
                        relative_layout.addView(img);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "이미지를 클릭했삼" + result, Toast.LENGTH_LONG).show();
                                System.out.println(result);
                            }
                        });
                        Intent intent = new Intent(v.getContext(), community_image_tagging_URL.class);
                        startActivityForResult(intent, REQUEST_CODE_BRAVO);
                        break;
                    //Up이 발생한 경우
                    case MotionEvent.ACTION_UP:

                        break;


                }return false;
            }
        });


    }
}
