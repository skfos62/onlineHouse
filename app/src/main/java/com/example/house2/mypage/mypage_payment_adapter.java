package com.example.house2.mypage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.house2.R;
import com.example.house2.partner.partner_delevery_Info;
import com.example.house2.partner.partner_delevery_saleinfo_class;

import java.util.ArrayList;

public class mypage_payment_adapter extends RecyclerView.Adapter<mypage_payment_adapter.ViewHolder> {

    Context context;
    private ArrayList<partner_delevery_saleinfo_class> mData;
    // 어댑터 생성자
    public mypage_payment_adapter(ArrayList<partner_delevery_saleinfo_class> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public mypage_payment_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mypage_payment_list,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull mypage_payment_adapter.ViewHolder holder, int position) {
        holder.mypage_payment_name.setText(mData.get(position).getProductName());
        if(mData.get(position).getProductState().equals("N")) {
            holder.mypage_payment_state.setText("배송준비중");
            holder.mypage_payment_btn.setVisibility(View.INVISIBLE);
        } else if (mData.get(position).getProductState().equals("P")){
            holder.mypage_payment_state.setText("배송중");
            holder.mypage_payment_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // id값을 넘겨야함
                    Intent intent = new Intent(context, partner_delevery_Info.class);
                    intent.putExtra("id",mData.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }
        holder.mypage_payment_userName.setText(mData.get(position).getRecipient());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 여기서 아이템뷰에 있는아이들을 선언해줘야한다.
    public class ViewHolder extends RecyclerView.ViewHolder {
//        mypage_payment_name // 상품이름
//                mypage_payment_state // 배송상태
//        mypage_payment_userName // 받을고객이름
//                mypage_payment_btn // 배송정보보기 버튼
        TextView mypage_payment_name;
        TextView mypage_payment_state;
        TextView mypage_payment_userName;
        Button mypage_payment_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mypage_payment_name = itemView.findViewById(R.id.mypage_payment_name);
            mypage_payment_state = itemView.findViewById(R.id.mypage_payment_state);
            mypage_payment_userName = itemView.findViewById(R.id.mypage_payment_userName);
            mypage_payment_btn = itemView.findViewById(R.id.mypage_payment_btn);
        }
    }
}
