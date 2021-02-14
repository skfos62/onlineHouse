package com.example.house2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.house2.classfile.shopping_list_class;

import java.util.ArrayList;

import static com.example.house2.shopping.recy_filter_ck;

public class shopping_arcore_list_adapter extends RecyclerView.Adapter<shopping_arcore_list_adapter.ViewHolder> {
    Context context;
    private ArrayList<shopping_list_class> mData;

    // 어댑터 생성자
    public shopping_arcore_list_adapter(ArrayList<shopping_list_class> mData) {
        this.mData = mData;
    }
    // 콜백 인터페이스 구현 ---------------------------------------------------------------------------
    // 인터페이스 선언
    public interface onModelSelectListener {
        void onModelSelected(String Furname, String Furtsfa);
    }
    // 인스턴스(객체)를 담을 변수 선언
    private onModelSelectListener mListener;

    public void setModelSelectListener(onModelSelectListener listener) {
        mListener = listener;
    }

    // 콜백 인터페이스 구현 끝 -------------------------------------------------------------------------

    @NonNull
    @Override
    public shopping_arcore_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext() ;
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_arcore_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull shopping_arcore_list_adapter.ViewHolder holder, int position) {
        holder.item_arcore_image_title.setText(mData.get(position).getFurname());
        holder.item_arcore_image_price.setText(String.valueOf(mData.get(position).getFurprice()));
        Glide.with(context).load(mData.get(position).getFurImage()).into(holder.item_arcore_image);
//        holder.item_shop_list_image.setImageResource(mData.get(position).getFurImage());
        holder.item_arcore_image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "선택하기 버튼 눌렀음", Toast.LENGTH_SHORT).show();
                if(mListener != null) {
                    mListener.onModelSelected(mData.get(position).getFurname(), mData.get(position).getFurtsfa());
                }
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
        ImageView item_arcore_image;
        TextView item_arcore_image_title;
        TextView item_arcore_image_price;
        Button item_arcore_image_select;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_arcore_image = itemView.findViewById(R.id.item_arcore_image);
            item_arcore_image_title = itemView.findViewById(R.id.item_arcore_image_title);
            item_arcore_image_price = itemView.findViewById(R.id.item_arcore_image_price);
            item_arcore_image_select = itemView.findViewById(R.id.item_arcore_image_select);
        }
    }
}
