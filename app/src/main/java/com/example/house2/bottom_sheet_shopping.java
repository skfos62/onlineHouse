package com.example.house2;

import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.house2.AddCart.addChat;
import com.example.house2.AddCart.appDbManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.house2.mypage.mypage.staticUser;

public class bottom_sheet_shopping extends BottomSheetDialogFragment {
    Button btn01;
    BottomSheetListener mListener;

    // 이미지
    ImageView item_show_image2;
    ImageView item_show_image3;
    ImageView item_show_image4;

    // 데이터 주고받기
    String title;
    String price;
    TextView bottomSheetTitle;
    String ImageResource;

    // 숫자
    int count=0;
    int totalCount;

    // + - 버튼
    Button btnPlus;
    Button btnMinus;

    TextView bottomSheetNum; // + - 사이에 있는 숫자
    TextView bottomSheetCount; // 카운트
    TextView bottomSheetSum; // 합계

    Button btnAddCart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_shopping, container,false);

        //sqlite 데이터 저장하기
        /**
         * message = 현재 로그인한 사람(닉네임)
         */
        appDbManager databaseManager = appDbManager.getInstance(getContext());


        Bundle bundle = this.getArguments();
        title = bundle.getString("title", "0");
        price = bundle.getString("price", "0");
        ImageResource = bundle.getString("ImageResource","0");
        Log.i(TAG, "onCreateView: 번들 테스트 " +title );
        Log.i(TAG, "onCreateView: 번들 테스트 " +ImageResource );
        bottomSheetTitle=  v.findViewById(R.id.bottom_sheet_title);
        bottomSheetTitle.setText(title);

        // 구매하기 버튼 선택할때
        btn01 = v.findViewById(R.id.btn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "띠용", Toast.LENGTH_SHORT).show();

            }
        });
        // 장바구니 버튼 클릭할때
        btnAddCart = v.findViewById(R.id.btn_addCart);
        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 뜸
                show();
                // 쉐어드프리퍼런스에 저장하기
                /**
                 *
                 * message = 현재 로그인한 사람(닉네임)
                 *     mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_addCart +
                 *                 "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                 *                 "nickname TEXT," +
                 *                 "title TEXT," +
                 *                 "totalCount INTEGER," +
                 *                 "sum INTEGER," +
                 *                 "purchars TEXT," +
                 *                 "image INTEGER," +
                 *                 "date DATE);");
                 *
                 */
                ContentValues addRowValue = new ContentValues();
                addRowValue.put("name", staticUser.getName());
                addRowValue.put("nickname", staticUser.getNickname());
                addRowValue.put("title", title);
                addRowValue.put("totalCount", count);
                addRowValue.put("sum", totalCount);
                addRowValue.put("purchars", "N");
                addRowValue.put("image", ImageResource);
                databaseManager.insert(addRowValue);

            }
        });


        // + - 버튼을 누를때 카운트 됨
        bottomSheetCount = v.findViewById(R.id.bottom_sheet_count);
        bottomSheetSum = v.findViewById(R.id.bottom_sheet_sum);
        bottomSheetNum = v.findViewById(R.id.bottom_sheet_num);
        bottomSheetNum.setText(String.valueOf(count));
        btnPlus = v.findViewById(R.id.btn_plus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCount = 0;
                count++;
                bottomSheetNum.setText(String.valueOf(count));
                bottomSheetCount.setText(String.valueOf(count));
                totalCount = count * Integer.parseInt(price);
                bottomSheetSum.setText(String.valueOf(totalCount));
            }
        });
        btnMinus = v.findViewById(R.id.btn_minus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                bottomSheetNum.setText(String.valueOf(count));
                bottomSheetCount.setText(String.valueOf(count));
                totalCount = totalCount - Integer.parseInt(price);
                bottomSheetSum.setText(String.valueOf(totalCount));
                if (count < 0) {
                   count = 0;
                    bottomSheetSum.setText("0");
                    bottomSheetNum.setText("0");
                    bottomSheetCount.setText("0");
                }
            }
        });

        return v;
    }


    public interface BottomSheetListener {
        void onButtonClicked(String text);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("장바구니 추가 완료");
        builder.setMessage("장바구니를 확인해보시겠습니까?");
        builder.setPositiveButton("닫기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("장바구니 바로가기",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 장바구니 바로가기를 눌러야함
                        Intent intent = new Intent(getActivity(), addChat.class);
                        startActivity(intent);//액티비티 띄우기
                    }
                });
        builder.show();
    }


}
