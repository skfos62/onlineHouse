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

import com.bumptech.glide.Glide;
import com.example.house2.R;

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

public class partner_register_product_edit extends AppCompatActivity {

    String SendProductName;
    String SendProductPrice;
    String SendProductCopy;
    String SendProductImage;
    String SendProductCategory;

    // 서버로 보낼 파라미터 선언
    String phpSendProductName;
    String phpSendProductPrice;
    String phpSendProductCopy;
    String phpProductImage;

    // 받아온 정보를 띄워줄 에디트 텍스트 선언
    EditText partner_edit_name;
    EditText partner_edit_price;
    ImageView partner_edit_image;
    EditText partner_edit_copy;
    Spinner partner_edit_category_spinner;
    Button partner_edit_save,partner_edit_cancle;

    // 스피너 관련 선언
    ArrayAdapter arrayAdapter;
    String partnerFuniCategory;

    // 이미지 관련 선언
    Uri selectedImage;
    // 알아낸 파일주소
    String FilePath;

    /**
     * 상품을 수정하는 클래스
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_register_product_edit);
        /**
         * <상품을 수정하는 로직>
         *     1. 앞 액티비티에서 수정할 상품의 정보를 받아온다.
         *     2. 받아온 정보를 에디트 텍스트에 미리 띄워준다.
         *     3. 수정할 정보를 받아서 다시 서버로 보내준다.
         *     4. 서버에서 응답을 받는다.
         *
         *     </상품을>
         */

        // 1. 앞 액티비티에서 수정할 상품의 정보를 받아온다.
        Intent intent = getIntent();
        SendProductName = intent.getStringExtra("productName");
        SendProductCategory = intent.getStringExtra("productCategory");
        SendProductPrice = intent.getStringExtra("productPrice");
        SendProductImage = intent.getStringExtra("productImage");
        SendProductCopy = intent.getStringExtra("productCopy");

        Log.i("인텐트로 값이 넘어왔나 확인", "onCreate: "+SendProductCategory);
        // 확인완료 (값이 잘 넘어옴)

        // 2. 받아온 정보를 에디트 텍스트에 미리 띄워준다.
        partner_edit_name = findViewById(R.id.partner_edit_name);
        partner_edit_price = findViewById(R.id.partner_edit_price);
        partner_edit_image = findViewById(R.id.partner_edit_image);
        partner_edit_copy = findViewById(R.id.partner_edit_copy);
        partner_edit_category_spinner = findViewById(R.id.partner_edit_category_spinner);
        partner_edit_save = findViewById(R.id.partner_edit_save);

        // 취소버튼을 누르면 액티비티가 꺼져야한다.
        partner_edit_cancle = findViewById(R.id.partner_edit_cancle);
        partner_edit_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        partner_edit_name.setText(SendProductName);
        partner_edit_price.setText(SendProductPrice);
        partner_edit_copy.setText(SendProductCopy);
        Glide.with(getApplicationContext()).load(SendProductImage).into(partner_edit_image);

        // 2-1. 이미지 바꾸는 곳
        //     이미지뷰를 클릭하면 수정하고 싶은 이미지로 바뀌어야 한다. 그리고 bitmap형식으로 바꾸어서 서버로 보내주어야한다.

        partner_edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // 2-1-1. 휴대폰에서 이미지를 가져오는 메소드
                pickFromGallery();
            }
        });


        // 2-2. 스피너에 표시할 데이터들
        ArrayList spinnerCategory = new ArrayList();
        spinnerCategory.add("가구");
        spinnerCategory.add("의자");
        spinnerCategory.add("테이블");
        spinnerCategory.add("소품");
        // 2. 스피너 선언을 해주고
        // 3. 해당 스피너의 어댑터에 어레이리스트를 붙여준다.
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,spinnerCategory);
        partner_edit_category_spinner.setAdapter(arrayAdapter);
        // 판매자가 등록한 카테고리로 선택이 되어있어야 한다.
        // if문을 이용해서 스피너에 미리 선택이 되어있는 카테고리 결정하기
        if(SendProductCategory.equals("table")) {
            partner_edit_category_spinner.setSelection(2);
        } else if (SendProductCategory.equals("furniture")){
            partner_edit_category_spinner.setSelection(0);
        } else if (SendProductCategory.equals("chair")) {
            partner_edit_category_spinner.setSelection(1);
        } else if (SendProductCategory.equals("etc")){
            partner_edit_category_spinner.setSelection(3);
        }

        partner_edit_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        // 3. 수정할 정보를 받아서 다시 서버로 보내준다.
        // 3-1. 수정하기 버튼을 누르는곳 여기서 서버로 통신을 해야한다.
        partner_edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 3-1-1. 이미지와 함께 보낼 파라미터들도 여기에서 입력을 해줘야한다.
                //        보낼 파라미터 종류 : Url,이미지 ,판매자 이름, 가구이름, 가격, 가격카테고리, 가구설명
                String imageUploadUrl = "http://13.125.62.22/h_partnerRegisterProductEdit.php";
                // 서버로 보낼 파라미터 선언
                phpSendProductName = partner_edit_name.getText().toString();
                phpSendProductPrice = partner_edit_price.getText().toString();
                phpSendProductCopy = partner_edit_copy.getText().toString();
                // 3-1-2. 서버와 통신하는 메소드이다
                new ImageUpload().execute(imageUploadUrl,FilePath,loginIdInfo,phpSendProductName,phpSendProductPrice,partnerFuniCategory,phpSendProductCopy,SendProductName);

            }
        });


    }

    // 2-1-2. 휴대폰에서 이미지를 가져오는 메소드
    private void pickFromGallery(){
        Intent pickphoto = new Intent(Intent.ACTION_PICK);
        pickphoto.setType("image/*");
        String[] mimeTypes = {"image/jpeg","image/png"};
        pickphoto.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(pickphoto,200);
    }
    // 2-1-3. 이미지를 가져오게 되면 서버로 이미지 파일을 전송하기 위해서 이미지의 절대 경로가 필요하다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200) {
            // 리퀘스트코드가 200일 경우는 안드로이드에서 이미지가 선택이 되었다는말
            selectedImage = data.getData();
            partner_edit_image.setImageURI(selectedImage);
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
            progressDialog = new ProgressDialog(partner_register_product_edit.this);
            progressDialog.setMessage("이미지 업로드중....");
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            try {
                //2-3. 위(2-1)에서 작성한 파라미터의 갯수와 일치해야한다.
                JSONObject jsonObject = mypageImageUploade.ImageUploade(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7]);
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
    // 3-1-2. 서버로 데이터 전송하는 코드 ----------------------------------------------------------------
    public static class mypageImageUploade {

        public static JSONObject ImageUploade(String imageUploadUrl, String sourceImageFile,String sellerName , String productName, String productPrice, String productCategory, String productCopy,String PreproductName ){

            try {
                File sourceFile = new File(sourceImageFile);
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=utf-8");
                String filename = sourceImageFile.substring(sourceImageFile.lastIndexOf("/")+1);

                // OKHTTP3
                // 3-1-3. 위(3-1-2)에서 이미지와 함께 무슨 파라미터를 php에 보낼때 무슨이름을 붙여서 보낼껀지 여기서 결정함
                //      보낼 파라미터 종류 : 가구이름, 가격, 가격카테고리, 이미지, 가구설명
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("uploaded_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                        .addFormDataPart("sellerName", sellerName)
                        .addFormDataPart("productName", productName)
                        .addFormDataPart("productPrice",productPrice)
                        .addFormDataPart("productCategory", productCategory)
                        .addFormDataPart("productCopy", productCopy)
                        .addFormDataPart("PreproductName", PreproductName)
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
