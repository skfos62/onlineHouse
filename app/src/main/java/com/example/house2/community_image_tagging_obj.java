package com.example.house2;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
//Parcelable
public class community_image_tagging_obj implements Serializable {
    float x;
    float y;
    String name;

    public community_image_tagging_obj() {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    protected community_image_tagging_obj(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
        name = in.readString();
    }
//
//    public static final Creator<community_image_tagging_obj> CREATOR = new Creator<community_image_tagging_obj>() {
//        @Override
//        public community_image_tagging_obj createFromParcel(Parcel in) {
//            return new community_image_tagging_obj(in);
//        }
//
//        @Override
//        public community_image_tagging_obj[] newArray(int size) {
//            return new community_image_tagging_obj[size];
//        }
//    };

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeFloat(x);
//        dest.writeFloat(y);
//        dest.writeString(name);
//    }
}
