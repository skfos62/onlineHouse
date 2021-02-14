package com.example.house2.partner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.house2.R;
import com.example.house2.classfile.shopping_list_class;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.house2.login_real.loginIdInfo;

public class partner_register_product_detail extends AppCompatActivity {
    /**
     * 상품 등록하는 찐페이지
     * @param savedInstanceState
     */

    Spinner registerSpinnerCategory;
    ArrayAdapter arrayAdapter;
    // 1-1
    EditText partner_funi_name;
    EditText partner_funi_price;
    ImageView partner_funi_image;
    EditText partner_funi_copy;
    Button partner_funi_save;
    Button partner_funi_cancle;
    // 서버에 보낼때 파라미터로 넣어줄 변수 선언
    String partnerFuniName;
    String partnerFuniPrice;
    String partnerFuniImage;
    String partnerFuniCopy;
    String partnerFuniCategory;

    // 이미지 관련 선언
    Uri selectedImage;
    // 알아낸 파일주소
    String FilePath;

    //메인 페이지에서 사용할 클래스
    shopping_list_class partnerRegisterMainClassNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_register_product_detail);
        // 레이아웃에서 스피너 부분에 넣어줄 텍스트
        // 1. 어레이리스트를 만들어서
        ArrayList spinnerCategory = new ArrayList();
        spinnerCategory.add("가구");
        spinnerCategory.add("의자");
        spinnerCategory.add("테이블");
        spinnerCategory.add("소품");
        spinnerCategory.add("선택해주세요.");
        // 2. 스피너 선언을 해주고
        registerSpinnerCategory = findViewById(R.id.partner_register_category_spinner);
        // 3. 해당 스피너의 어댑터에 어레이리스트를 붙여준다.
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,spinnerCategory);
        registerSpinnerCategory.setAdapter(arrayAdapter);
        registerSpinnerCategory.setSelection(arrayAdapter.getCount()-1);
        registerSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 서버에 저장할때 카테고리의 이름은 furniture,chair,table,etc 이기 때문에 여기서 바꿔주어야 한다.
                if(spinnerCategory.get(position).equals("가구")){
                    partnerFuniCategory= "furniture";
                } else if (spinnerCategory.get(position).equals("의자")){
                    partnerFuniCategory= "chair";
                } else if (spinnerCategory.get(position).equals("테이블")){
                    partnerFuniCategory= "table";
                } else if (spinnerCategory.get(position).equals("소품")){
                    partnerFuniCategory= "etc";
                }
                Log.i("스피너에 제대로 찍혔는지 확인하기", "onItemSelected: " + partnerFuniCategory);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /**
         * <판매상품 등록하는 로직>
         *     1. 판매자가 액티비티에서 입력한 값을 서버통신하는곳(sendRequst)에 넣어준다.
         *     2. 넣어준 값을 db에 저장하는 php로 보낸다. (이미지도 포함)
         *     3. 저장이 완료되면 완료되었다는 응답을 준다.
         *     4. php에서 완료되었다는 응답을 받으면 등록이 완료되었다는 토스트 창 띄워주기
         *     </판매상품>
         */

        // 1. 판매자가 액티비티에서 입력한 값을 서버통신하는곳(sendRequst)에 넣어준다.
        // 1-1. 판매자가 입력한 값을 알기 위해서 editText와 spinner를 선언해준다. (위에있음)
        partner_funi_name = findViewById(R.id.partner_funi_name);
        partner_funi_price = findViewById(R.id.partner_funi_price);
        // 1-2. 이미지를 클릭해서 선택한 이미지의 uri가져오기
        partner_funi_image = findViewById(R.id.partner_funi_image);
        partner_funi_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 휴대폰에서 이미지를 가져오는 메소드
                pickFromGallery();
            }
        });
        partner_funi_copy = findViewById(R.id.partner_funi_copy);

        //2. 판매자가 가구의 정보를 등록하고 이 버튼을 눌렀을때 php로 보낸다.
        partner_funi_save = findViewById(R.id.partner_funi_save);
        partner_funi_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 2-1. 이미지와 함께 보낼 파라미터들도 여기에서 입력을 해줘야한다.
                //      보낼 파라미터 종류 : Url,이미지 ,판매자 이름, 가구이름, 가격, 가격카테고리, 가구설명
                String imageUploadUrl = "http://13.125.62.22/h_partnerRegisterProduct.php";
                partnerFuniName = partner_funi_name.getText().toString();
                partnerFuniPrice = partner_funi_price.getText().toString();
                partnerFuniCopy = partner_funi_copy.getText().toString();
                new ImageUpload().execute(imageUploadUrl,FilePath,loginIdInfo,partnerFuniName,partnerFuniPrice,partnerFuniCategory,partnerFuniCopy);


            }
        });
        // 취소하기 버튼을 누르면 액티비티가 사라져야 한다.
        partner_funi_cancle = findViewById(R.id.partner_funi_cancle);
        partner_funi_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    // 1-3. 휴대폰상에서 이미지를 가져오는 메소드
    private void pickFromGallery(){
        Intent pickphoto = new Intent(Intent.ACTION_PICK);
        pickphoto.setType("image/*");
        String[] mimeTypes = {"image/jpeg","image/png"};
        pickphoto.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(pickphoto,200);
    }
    // 1-4. 이미지를 가져오게 되면 서버로 이미지 파일을 전송하기 위해서 이미지의 절대 경로가 필요하다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200) {
            // 리퀘스트코드가 200일 경우는 안드로이드에서 이미지가 선택이 되었다는말
            selectedImage = data.getData();
            partner_funi_image.setImageURI(selectedImage);
            // 이미지 절대 경로 얻기
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            FilePath = imgDecodableString;
            // imgDecoableString = 선택한 이미지의 절대 경로임
        }

    }

    // 2-2. 서버로 데이터 전송하는 전체 코드 --------------------------------------------------------------
    private class ImageUpload extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(partner_register_product_detail.this);
            progressDialog.setMessage("이미지 업로드중....");
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            try {
                //2-3. 위(2-1)에서 작성한 파라미터의 갯수와 일치해야한다.
                JSONObject jsonObject = mypageImageUploade.ImageUploade(params[0],params[1],params[2],params[3],params[4],params[5],params[6]);
                if (jsonObject != null)
                    return jsonObject.getString("result").equals("success");

            } catch (JSONException e) {
                Log.i("TAG", "스밤 왜 에러나고 난리: " + e.getLocalizedMessage());
            }

            return false;
        }
        // 3. 완료가 되면 결과 응답 받아오기
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null)
                progressDialog.dismiss();
            if (aBoolean) {
                // 4. 완료가 되면 가구가 등록되었다는 토스트창 띄우기
                Toast.makeText(getApplicationContext(), "가구 등록", Toast.LENGTH_LONG).show();
                // 여기서 앞의 액티비티 (파트너 목록)에서 띄울 가구 정보를 담아서 주어야 한다.
                Intent intent2 = new Intent(getApplicationContext(),partner_register_product.class);
                startActivity(intent2);
                finish();


            }else {
                Toast.makeText(getApplicationContext(), "가구 등록", Toast.LENGTH_LONG).show();
                // 여기서 앞의 액티비티 (파트너 목록)에서 띄울 가구 정보를 담아서 주어야 한다.
                Intent intent2 = new Intent(getApplicationContext(),partner_register_product.class);
                startActivity(intent2);
                finish();
            }
            FilePath = "";



        }
    }
    // 2-4. 서버로 데이터 전송하는 코드 ------------------------------------------------------------------
    public static class mypageImageUploade {

        public static JSONObject ImageUploade(String imageUploadUrl, String sourceImageFile ,String sellerName, String productName, String productPrice, String productCategory,String productCopy ){

            try {
                File sourceFile = new File(sourceImageFile);
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=utf-8");
                String filename = sourceImageFile.substring(sourceImageFile.lastIndexOf("/")+1);

                // OKHTTP3
                // 2-5. 위(2-1)에서 이미지와 함께 무슨 파라미터를 php에 보낼때 무슨이름을 붙여서 보낼껀지 여기서 결정함
                //      보낼 파라미터 종류 : 판매자 이름, 가구이름, 가격, 가격카테고리, 이미지, 가구설명
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                        .addFormDataPart("sellerName", sellerName)
                        .addFormDataPart("productName", productName)
                        .addFormDataPart("productPrice",productPrice)
                        .addFormDataPart("productCategory", productCategory)
                        .addFormDataPart("productCopy", productCopy)
                        .build();

                Request request = new Request.Builder()
                        .url(imageUploadUrl)
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String res = Objects.requireNonNull(response.body()).string();
                Log.e("TAG", "결과: " + res);
                return new JSONObject(res);

            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e("TAG", "호스트 에러가 나오는곳 : " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e("TAG", "에러가 나오는곳 : " + e.getLocalizedMessage());
            }

            return null;
        }

    }
}
