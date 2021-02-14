package com.example.house2;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */

/**
 *
 * 가구배치하는 프레그먼트
 *
 */
public class search extends Fragment {

    TextView tvArcore;


    public search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // 가구배치 바로가기 누르면 ar코어에 연결된 액티비티가 뜨게 만들기
        tvArcore = getView().findViewById(R.id.tv_arcore);
        tvArcore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), search_arcore.class);
                startActivity(intent);
            }
        });
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

}
