package com.example.house2;


import com.google.gson.annotations.SerializedName;

/**
 * Created by Android on 2/17/2018.
 */

public class Img_pojo {


    @SerializedName("image_name")
    private String Title;

    @SerializedName("image")
    private String Image;


    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}