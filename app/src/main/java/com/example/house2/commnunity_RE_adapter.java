package com.example.house2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class commnunity_RE_adapter extends RecyclerView.Adapter<commnunity_RE_adapter.ViewHolder> {

    private ArrayList<community_json_tag> mData = null ;

    // 뷰홀더 클래스 생성
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView item_show_title;
        TextView item_show_con;
        ImageView item_show_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_show_title = itemView.findViewById(R.id.item_show_title);
            item_show_con = itemView.findViewById(R.id.item_show_con);
            item_show_image = itemView.findViewById(R.id.item_show_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        Toast.makeText(v.getContext(), "로그아웃이 왜안돼지.", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    // 생성자에서 데이터 리스트를 객체로 전달 받음
    commnunity_RE_adapter(ArrayList<community_json_tag> list){
        mData = list;
    }


    // 아이템뷰를 위한 뷰홀더 객체 생성
    @NonNull
    @Override
    public commnunity_RE_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_show, parent,false);
        commnunity_RE_adapter.ViewHolder vh = new commnunity_RE_adapter.ViewHolder(view);

        return vh;
    }

    // 포지션에 해당하는 아이템뷰 표시
    @Override
    public void onBindViewHolder(@NonNull commnunity_RE_adapter.ViewHolder holder, final int position) {
        final community_json_tag text = mData.get(position);
        holder.item_show_title.setText(text.getTitle());
        holder.item_show_con.setText(text.getCon());
        Glide.with(holder.item_show_image.getContext()).load(text.getImageURL()).into(holder.item_show_image);
        holder.item_show_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), position+"번 째 이미지!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(),community_view.class);
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
}
