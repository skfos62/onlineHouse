package com.example.house2.partner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.house2.AppHelper;

import com.example.house2.R;
import com.google.android.gms.common.util.IOUtils;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class partner_delevery_ing_adapter extends RecyclerView.Adapter<partner_delevery_ing_adapter.ViewHolder> {
    Context context;
    private ArrayList<partner_delevery_saleinfo_class> mData;

    // 어댑터 생성자


    public partner_delevery_ing_adapter(ArrayList<partner_delevery_saleinfo_class> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public partner_delevery_ing_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner_delevery_fine,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull partner_delevery_ing_adapter.ViewHolder holder, int position) {
        holder.delevery_fine_list_Itemname.setText(mData.get(position).getProductName());
        holder.delevery_fine_list_userName.setText(mData.get(position).getRecipient());
        holder.delevery_fine_list_state.setText("배송중");
        holder.delevery_fine_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 배송정보 보기 페이지로 넘어가야함
                Intent intent = new Intent(context,partner_delevery_Info.class);
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

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView delevery_fine_list_Itemname;
        TextView delevery_fine_list_state;
        TextView delevery_fine_list_userName;
        Button delevery_fine_list_btn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            delevery_fine_list_Itemname = itemView.findViewById(R.id.delevery_fine_list_Itemname);
            delevery_fine_list_state = itemView.findViewById(R.id.delevery_fine_list_state);
            delevery_fine_list_userName = itemView.findViewById(R.id.delevery_fine_list_userName);
            delevery_fine_list_btn = itemView.findViewById(R.id.delevery_fine_list_btn);
        }
    }



}
