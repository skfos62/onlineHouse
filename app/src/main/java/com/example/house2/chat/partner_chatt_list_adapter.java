package com.example.house2.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.house2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.house2.login_real.loginIdInfoNick;

public class partner_chatt_list_adapter extends RecyclerView.Adapter<partner_chatt_list_adapter.ViewHolder> {
    Context context;
    Intent intent;
    private ArrayList<DataItem> mData;
    ArrayList<DataItem> LastCk;

    public partner_chatt_list_adapter(ArrayList<DataItem> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public partner_chatt_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatt_list,parent,false);
        partner_chatt_list_adapter.ViewHolder vh = new partner_chatt_list_adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull partner_chatt_list_adapter.ViewHolder holder, int position) {
        holder.chatt_list_userName.setText(mData.get(position).getName());
        holder.chatt_list_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 판매자측에서 채팅 시작하는 로직
                // 여기에서는 디비에 접속해서 채팅리스트가 있는지 확인해야한다.

                // 내가 판매자면 sellerID에 내 아이디를 넣어야하고
                // 내가 구매자면 userID에 내 아이디를 넣어야한다.
                // 구매자 -> 뷰타입 0 / 판매자 뷰타입 -> 1
                if(mData.get(position).getViewType() == 0){
                    Intent intent = new Intent(context, partner_chatt_view.class);
                    intent.putExtra("from","user");
                    intent.putExtra("sellerID",mData.get(position).getName());
                    intent.putExtra("roomId",mData.get(position).getRoomNum());
                    intent.putExtra("chatContent",mData.get(position).getContent());
                    intent.putExtra("userID",loginIdInfoNick);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, partner_chatt_view.class);
                    intent.putExtra("from","seller");
                    intent.putExtra("sellerID",loginIdInfoNick);
                    intent.putExtra("roomId",mData.get(position).getRoomNum());
                    intent.putExtra("chatContent",mData.get(position).getContent());
                    intent.putExtra("userID",mData.get(position).getName());
                    context.startActivity(intent);
                }

            }
        });
        // 어레이리스트를 파싱해서 setText를 시키자
        if(mData.get(position).getContent().equals("내용")) {
            holder.chatt_list_text.setText("내용이 없습니다.");
        } else {
            try {
                JSONArray jsonArray = new JSONArray(mData.get(position).getContent());
                for(int i = 0 ; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String RoomNum = jsonObject.getString("RoomNum");
                    String content = jsonObject.getString("content");
                    String name = jsonObject.getString("name");
                    String viewType = jsonObject.getString("viewType");
                    LastCk = new ArrayList<>();
                    if(name.equals(loginIdInfoNick)){
                        LastCk.add(new DataItem(content, name,0, RoomNum));
                    } else {
                        LastCk.add(new DataItem(content, name,1, RoomNum));
                    }

                    // 데이터 아이템 만들기
                    // 그리고 리사이클러뷰 어레이리스테 추가하기

                    holder.chatt_list_text.setText(LastCk.get(LastCk.size()-1).getContent());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView chatt_list_userName; // 고객이름
        TextView chatt_list_text; // 채팅내용
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatt_list_userName = itemView.findViewById(R.id.chatt_list_userName);
            chatt_list_text = itemView.findViewById(R.id.chatt_list_text);
        }
    }
}
