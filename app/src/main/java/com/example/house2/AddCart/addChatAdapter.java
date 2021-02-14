package com.example.house2.AddCart;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.house2.R;

import java.util.ArrayList;

public class addChatAdapter extends RecyclerView.Adapter<addChatAdapter.ViewHolder> {

    Context context;
    private ArrayList<addChatItemClass> mData;
    private static final String TAG = "addChatAdapter";
    public static String date;


    // 어댑터 생성자
    public addChatAdapter(ArrayList<addChatItemClass> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext() ;

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_add_chat, parent, false);
        Log.i(TAG, "onCreateViewHolder: 1");
        Log.i(TAG, "onCreateViewHolder: 1" +mData.toString());
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(mData.get(position).getImage()).into(holder.item_add_image);
//        holder.item_add_image.setImageResource(mData.get(position).getImage());
        holder.item_add_image_title.setText(mData.get(position).getTitle());
        holder.item_add_image_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "데이터"+date, Toast.LENGTH_LONG);
            }
        });
        holder.item_add_image_count.setText(String.valueOf(mData.get(position).getTotalCount()));
        holder.item_add_image_sum.setText(String.valueOf(mData.get(position).getSum()));
        holder.item_add_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sqlite 삭제하는 코드 집어넣기 & 새로고침 하는 코드도 넣기
                Log.i(TAG, "onClick: 클릭되니?");
                date = mData.get(position).getDate();
                Toast.makeText(v.getContext(), "데이터"+date, Toast.LENGTH_LONG);
                appDbManager databaseManager = appDbManager.getInstance(v.getContext());
                databaseManager.delete(v);
//                Toast.makeText(context, "삭제하기를 눌렀습니다.", Toast.LENGTH_LONG);

//                Intent intent = new Intent(v.getContext(),addChat.class);
//                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                v.getContext().startActivity(intent);
            }
        });
        Log.i(TAG, "onCreateViewHolder: 2");
        Log.i(TAG, "onCreateViewHolder: 2" +mData.get(position).getSum());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_add_image; // 리사이클러뷰 이미지
        TextView item_add_image_title; // 리사이클러뷰 아이템 이름
        TextView item_add_image_count; // 리사이클러뷰 몇개
        TextView item_add_image_sum;  // 리사이클러뷰 금액
        ImageView item_add_x;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_add_image =itemView.findViewById(R.id.item_add_image);
            item_add_image_title =itemView.findViewById(R.id.item_add_image_title);
            item_add_image_count =itemView.findViewById(R.id.item_add_image_count);
            item_add_image_sum =itemView.findViewById(R.id.item_add_image_sum);
            item_add_x = itemView.findViewById(R.id.item_add_x);
            Log.i(TAG, "onCreateViewHolder: 3");

        }
    }


}
