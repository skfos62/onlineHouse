package com.example.house2.mypage;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.house2.R;
import com.example.house2.commnunity_RE_adapter;
import com.example.house2.community_json_tag;
import com.example.house2.community_view;

import java.util.ArrayList;

/**
 * community_json_tag 클래스를 이용했음
 */

public class mypageAdapter extends RecyclerView.Adapter<mypageAdapter.ViewHolder> {

    private ArrayList<community_json_tag> mData = null ;

    // 생성자에서 데이터 리스트를 객체로 전달 받음
    mypageAdapter(ArrayList<community_json_tag> list){
        mData = list;
    }


    @NonNull
    @Override
    public mypageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_mypage_mycon, parent,false);
        mypageAdapter.ViewHolder vh = new mypageAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull mypageAdapter.ViewHolder holder, int position) {
        // 받아온 리스트의 텍스트
        final community_json_tag text = mData.get(position);
        Glide.with(holder.item_mypage_image.getContext()).load(text.getImageURL()).into(holder.item_mypage_image);
        holder.item_mypage_title.setText(text.getTitle());
        holder.item_mypage_title.setText(text.getCon());
        holder.item_mypage_Rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), position+"번 째 이미지!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), community_view.class);
                intent.putExtra("title",text.getTitle());
                intent.putExtra("id",text.getTitle());
                intent.putExtra("con",text.getCon());
                intent.putExtra("Image",text.getImageURL());
                intent.putExtra("Writer",text.getWriter());
                intent.putExtra("WriterDate()",text.getWriterDate());
                intent.putExtra("tag",text.getTag());
                v.getContext().startActivity(intent);
                Toast.makeText(v.getContext(), text.getCon(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰홀더 클래스

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_mypage_image;
        TextView item_mypage_title;
        TextView item_mypage_list_con;
        LinearLayout item_mypage_Rv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_mypage_image = itemView.findViewById(R.id.item_mypage_image);
            item_mypage_title = itemView.findViewById(R.id.item_mypage_title);
            item_mypage_list_con = itemView.findViewById(R.id.item_mypage_list_con);
            item_mypage_Rv = itemView.findViewById(R.id.item_mypage_Rv);


        }
    }
}
