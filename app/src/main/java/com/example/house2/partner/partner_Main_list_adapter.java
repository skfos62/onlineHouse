package com.example.house2.partner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.house2.R;

import java.util.ArrayList;

public class partner_Main_list_adapter extends RecyclerView.Adapter<partner_Main_list_adapter.ViewHolder> {
    Context context;
    private ArrayList<partner_Main_list_class> mData;

    // 어댑터 생성자
    public partner_Main_list_adapter(ArrayList<partner_Main_list_class> mData) {
        this.mData = mData;
    }


    @NonNull
    @Override
    public partner_Main_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner_sales_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull partner_Main_list_adapter.ViewHolder holder, int position) {
        holder.partner_sales_list_Itemname.setText(mData.get(position).getProductName());
        if(mData.get(position).getProductState().equals("N")){
            holder.partner_sales_list_delevery.setText("배송준비중");
        } else {
            holder.partner_sales_list_delevery.setText("배송중");
            holder.partner_sales_list_delevery.setTextColor(Color.parseColor("#025FAF"));
        }

        holder.partner_sales_list_userName.setText(mData.get(position).getBuyer());
        holder.partner_sales_list_ItemnPrice.setText(String.valueOf(mData.get(position).getPayment()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView partner_sales_list_Itemname;
        TextView partner_sales_list_userName;
        TextView partner_sales_list_delevery;
        TextView partner_sales_list_ItemnPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            partner_sales_list_Itemname = itemView.findViewById(R.id.partner_sales_list_Itemname);
            partner_sales_list_userName = itemView.findViewById(R.id.partner_sales_list_userName);
            partner_sales_list_delevery = itemView.findViewById(R.id.partner_sales_list_delevery);
            partner_sales_list_ItemnPrice = itemView.findViewById(R.id.partner_sales_list_ItemnPrice);

        }
    }
}
