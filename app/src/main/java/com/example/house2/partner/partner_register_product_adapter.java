package com.example.house2.partner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.house2.AppHelper;
import com.example.house2.R;
import com.example.house2.classfile.shopping_list_class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.house2.login_real.loginIdInfo;

public class partner_register_product_adapter extends RecyclerView.Adapter<partner_register_product_adapter.ViewHolder> {

    Context context;
    Intent intent;
    private ArrayList<shopping_list_class> mData;

    //서버에서 받아오는 변수

    String Furname;
    String Furprice;
    String Furcopy;
    String Furcategory;
    String FurArck;
    String FurImage;
    String sfa;
    String sellerName;
    int IntProductNum;
    int IntPayment;

    // php로 보낼 변수
    String phpSendPrice; // 가격
    String phpSendName;

    // 어댑터 생성자
    public partner_register_product_adapter(ArrayList<shopping_list_class> mData){
        this.mData = mData;
    }

    // 콜백 인터페이스 구현
    public interface onDeleteClickListener {
        void onDeleteClick(String callbackCk);

    }
    private onDeleteClickListener mListener;
    public void setDeleteClickListener (onDeleteClickListener listener ) {
        mListener = listener;
    }

    @NonNull
    @Override
    public partner_register_product_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner_resigster_main,parent,false);
        partner_register_product_adapter.ViewHolder vh = new partner_register_product_adapter.ViewHolder(v);
        return vh;
    }
    // 여기서 어떤 데이터를 아이템뷰에 붙일지, 어떤 이벤트를 붙일지 결정한다.
    @Override
    public void onBindViewHolder(@NonNull partner_register_product_adapter.ViewHolder holder, int position) {
        phpSendPrice = String.valueOf(mData.get(position).getFurprice());
        phpSendName = mData.get(position).getFurname();
        holder.item_partner_register_title.setText(mData.get(position).getFurname());
        holder.item_partner_register_category.setText(mData.get(position).getFurcategory());
        holder.item_partner_register_price.setText(String.valueOf(mData.get(position).getFurprice()));
        holder.item_partner_register_copy.setText(mData.get(position).getFurcopy());
        holder.item_partner_register_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,partner_register_product_edit.class);
                intent.putExtra("productName",mData.get(position).getFurname());
                intent.putExtra("productCategory",mData.get(position).getFurcategory().toString());
                intent.putExtra("productPrice",String.valueOf(mData.get(position).getFurprice()));
                intent.putExtra("productCopy",mData.get(position).getFurcopy());
                intent.putExtra("productImage",mData.get(position).getFurImage());
                context.startActivity(intent);
            }
        });
        holder.item_partner_register_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("삭제하기 클릭했음", "onClick");
                // 여기서 삭제하는 php(h_partnerRegisterProductDelete.php)로 보내줘야한다.
                // 보내주는 정보: 판매자 정보, 삭제할 가구 이름, 삭제할 가격
                sendRequest("http://13.125.62.22/h_partnerRegisterProductDelete.php");
//                if(mListener != null) {
//                    mListener.onDeleteClick("new");
//                }
//                notifyDataSetChanged();
                Intent intent = new Intent(context,partner_register_product.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
                ((Activity)context).finish();
                ((Activity)context).overridePendingTransition(0,0);
            }
        });
        Glide.with(context).load(mData.get(position).getFurImage()).into(holder.item_partner_register_image);

    }
    // 리사이클러뷰 갯수
    @Override
    public int getItemCount() {
        return mData.size();
    }
    // 아이템뷰에 있는거를 여기서 연결시켜주어야한다.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_partner_register_image;
        TextView item_partner_register_title;
        TextView item_partner_register_category;
        TextView item_partner_register_price;
        TextView item_partner_register_copy;
        Button item_partner_register_update;
        Button item_partner_register_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_partner_register_image = itemView.findViewById(R.id.item_partner_register_image);
            item_partner_register_title = itemView.findViewById(R.id.item_partner_register_title);
            item_partner_register_category = itemView.findViewById(R.id.item_partner_register_category);
            item_partner_register_price = itemView.findViewById(R.id.item_partner_register_price);
            item_partner_register_copy = itemView.findViewById(R.id.item_partner_register_copy);
            item_partner_register_update = itemView.findViewById(R.id.item_partner_register_update);
            item_partner_register_delete = itemView.findViewById(R.id.item_partner_register_delete);
        }
    }

    // 등록된 가구 리스트 삭제를 위해서 서버에 통신하는 부분 --------------------------------------------------------
    public void sendRequest(String url) {

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 2. 서버와 통신이 완료(성공)되고 응답을 받아오는곳
                        // 확인하는곳
                        Log.i("php리스폰값 확인", "onResponse: " + response);
                        Log.i("php리스폰값 확인", "된거여 만거여1 " );
//                        responeJson = response;
                        // 3. 통신을 하여서 받아온 제이슨을 리사이클러뷰에 뿌려주기 위해서
                        //    제이슨 형식의 데이터를 오브젝트로 바꾸고 -> 어레이리스트에 넣어주어야한다.
                        try {
                            // 제이슨 오브젝트에서 status라는 이름을 가진 어레이를 뽑아내서
                            JSONObject jObject = new JSONObject(response);
                            JSONArray results = jObject.getJSONArray("status");
                            // 하나하나 partner_Main_list_class의 오브젝트로 만들어서 어레이리스트로 만들기
                            int index = 0;
                            // jsonobject인 result에서 하나하나 값을 가져와서 partner_Main_list 클래스 오브젝트로 만드는곳
                            while (index < results.length()) {
                                String Furname;
                                String Furprice;
                                String Furcopy;
                                String Furcategory;
                                String FurArck;
                                String FurImage;
                                String sfa;
                                String sellerName;
                                Furname = results.getJSONObject(index).getString("Furname");
                                Furprice = results.getJSONObject(index).getString("Furprice");
                                Furcopy = results.getJSONObject(index).getString("Furcopy");
                                Furcategory = results.getJSONObject(index).getString("Furcategory");
                                FurArck = results.getJSONObject(index).getString("FurArck");
                                FurImage = results.getJSONObject(index).getString("FurImage");
                                sfa = results.getJSONObject(index).getString("sfa");
                                sellerName = results.getJSONObject(index).getString("sellerName");
                                shopping_list_class partnerMainClass = new shopping_list_class(Furname,Integer.parseInt(Furprice),Furcopy,Furcategory,FurArck,FurImage,sfa,sellerName);
                                // 만든 오브젝트를 리사이클러뷰를 뿌릴 어레이리스트에 넣어주기
                                mData.add(partnerMainClass);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    // 서버와의 통신이 실패 되면 나오는곳
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("에러확인", "onErrorResponse: " + error);
                        Log.i("php리스폰값 확인", "된거여 만거여2 ");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //여기다가 서버에 보낼 데이터값을 넣어준다.
//                보내주는 정보: 판매자 정보, 삭제할 가구 이름, 삭제할 가격
                Log.i("php리스폰값 확인", "된거여 만거여3 " +loginIdInfo);
                Log.i("php리스폰값 확인", "된거여 만거여3 " +phpSendPrice);
                Log.i("php리스폰값 확인", "된거여 만거여3 " +phpSendName);
                params.put("sellerId", loginIdInfo);
                params.put("productPrice", phpSendPrice);
                params.put("productName", phpSendName);
                return params;
            }
        };
        // 이전 결과가 있더라도 새로 요청
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
    // 매출리스트를 받기위한 서버 통신하는 부분 끝 ------------------------------------------------------

}
