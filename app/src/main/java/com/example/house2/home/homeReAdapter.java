package com.example.house2.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.house2.R;
import com.example.house2.classfile.shopping_list_class;
import com.example.house2.shopping_view;

import java.util.ArrayList;

public class homeReAdapter extends RecyclerView.Adapter<homeReAdapter.ViewHolder> {

    Context context;
    private ArrayList<shopping_list_class> mData;

    // 어댑터 생성자
    public homeReAdapter(ArrayList<shopping_list_class> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_main_list, parent, false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_main_image_title.setText(mData.get(position).getFurname());
        holder.item_main_image_price.setText(String.valueOf(mData.get(position).getFurprice()));
        Glide.with(context).load(mData.get(position).getFurImage()).into(holder.item_main_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 여기서 뷰페이지로 인텐트 보내기
                Intent intent = new Intent(v.getContext(), shopping_view.class);
                intent.putExtra("title",mData.get(position).getFurname());
                intent.putExtra("sub",mData.get(position).getFurcopy());
                intent.putExtra("price",mData.get(position).getFurprice());
                intent.putExtra("Image",mData.get(position).getFurImage());
                intent.putExtra("sfbnum",mData.get(position).getFurtsfa());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_main_image;
        TextView item_main_image_title;
        TextView item_main_image_price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_main_image = itemView.findViewById(R.id.item_main_image);
            item_main_image_title = itemView.findViewById(R.id.item_main_image_title);
            item_main_image_price = itemView.findViewById(R.id.item_main_image_price);

        }
    }

}
