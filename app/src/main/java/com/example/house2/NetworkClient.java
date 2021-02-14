package com.example.house2;

import android.content.Context;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 망한 레트로핏
 */

public class NetworkClient {
    private static final String BASE_URL = "http://13.125.62.22/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitClient(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

//
    public interface UploadAPIs {
        @Multipart
        @POST("h_imageUP.php")
//        Call<Img_pojo> uploadImage(@Field("image_name") String title, @Field("image") String image);
        Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);
    }

    public interface test {
//        @Multipart
        @POST("h_imageUP.php")
        Call<ResponseBody> commnet(@Query("postid") String postid);
    }

    public interface ApiInterface {
        @FormUrlEncoded
        @POST("h_imageUP.php")
        Call<Img_pojo> uploadImage(@Field("image_name") String title, @Field("image") String image);
    }

}

