package com.example.house2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.house2.classfile.shopping_list_class;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.house2.shopping.recy_filter_ck;

public class shopping_list_adapter extends RecyclerView.Adapter<shopping_list_adapter.ViewHolder> {
    Context context;
    private ArrayList<shopping_list_class> mData;

    // 어댑터 생성자
    public shopping_list_adapter(ArrayList<shopping_list_class> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_shop_list, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.i("데이터확인", "onBindViewHolder: " +recy_filter_ck);
            holder.item_shop_list_con.setText(mData.get(position).getFurcopy());
            holder.item_shop_list_title.setText(mData.get(position).getFurname());
            holder.item_shop_list_price.setText(String.valueOf(mData.get(position).getFurprice()));
            Glide.with(context).load(mData.get(position).getFurImage()).into(holder.item_shop_list_image);
//        holder.item_shop_list_image.setImageResource(mData.get(position).getFurImage());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 여기서 뷰페이지로 인텐트 보내기
                    Intent intent = new Intent(v.getContext(), shopping_view.class);
                    intent.putExtra("title", mData.get(position).getFurname());
                    intent.putExtra("sub", mData.get(position).getFurcopy());
                    intent.putExtra("price", mData.get(position).getFurprice());
                    intent.putExtra("Image", mData.get(position).getFurImage());
                    intent.putExtra("sfbnum", mData.get(position).getFurtsfa());
                    intent.putExtra("sellerName", mData.get(position).getSellerName());
                    v.getContext().startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 뷰홀더-레이아웃 연결하기
    // 여기서 findbyId해야함
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_shop_list_image;
        TextView item_shop_list_title;
        TextView item_shop_list_con;
        TextView item_shop_list_price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_shop_list_image = itemView.findViewById(R.id.item_shop_list_image);
            item_shop_list_title = itemView.findViewById(R.id.item_shop_list_title);
            item_shop_list_con = itemView.findViewById(R.id.item_shop_list_con);
            item_shop_list_price = itemView.findViewById(R.id.item_shop_list_price);
        }
    }

}
