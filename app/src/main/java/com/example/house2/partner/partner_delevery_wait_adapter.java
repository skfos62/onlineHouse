package com.example.house2.partner;

import android.app.Activity;
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

import java.util.ArrayList;

public class partner_delevery_wait_adapter extends RecyclerView.Adapter<partner_delevery_wait_adapter.ViewHolder> {

    Context context;
    private ArrayList<partner_delevery_saleinfo_class> mData;
    int ListCount;

    // 어댑터 생성자 생성
    public partner_delevery_wait_adapter(ArrayList<partner_delevery_saleinfo_class> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner_delevery_wait,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(mData.get(position).getProductState().equals("N")){
            holder.delevery_wait_list_Itemname.setText(mData.get(position).getProductName());
            holder.delevery_wait_list_userName.setText(mData.get(position).getRecipient());
            holder.delevery_wait_list_state.setText("배송준비중");
            holder.delevery_wait_list_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // partner_delevery_wait_edit로 정보를 보내자
                    Intent intent = new Intent(context,partner_delevery_wait_edit.class);
                    intent.putExtra("productName",mData.get(position).getProductName());
                    intent.putExtra("id",String.valueOf(mData.get(position).getId()));
                    intent.putExtra("buyer",mData.get(position).getBuyer());
                    intent.putExtra("payment",String.valueOf(mData.get(position).getPayment()));
                    intent.putExtra("sellerName",mData.get(position).getSellerName());
                    intent.putExtra("date",mData.get(position).getDate());
                    intent.putExtra("productState",mData.get(position).getProductState());
                    intent.putExtra("Recipient",mData.get(position).getRecipient());
                    intent.putExtra("RecipAdress",mData.get(position).getRecipAdress());
                    intent.putExtra("Reciphone",String.valueOf(mData.get(position).getReciphone()));
                    context.startActivity(intent);
                    ((Activity)context).finish();

                }
            });
        } else if(mData.get(position).getProductState().equals("P")) {
            mData.remove(position);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView delevery_wait_list_Itemname;
        TextView delevery_wait_list_state;
        TextView delevery_wait_list_userName;
        Button delevery_wait_list_btn;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            delevery_wait_list_Itemname =itemView.findViewById(R.id.delevery_wait_list_Itemname);
            delevery_wait_list_state = itemView.findViewById(R.id.delevery_wait_list_state);
            delevery_wait_list_userName = itemView.findViewById(R.id.delevery_wait_list_userName);
            delevery_wait_list_btn = itemView.findViewById(R.id.delevery_wait_list_btn);


        }
    }
}
