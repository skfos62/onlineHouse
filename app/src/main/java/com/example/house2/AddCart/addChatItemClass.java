package com.example.house2.AddCart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class addChatItemClass implements Serializable {
    /**
     * 리사이클러뷰에 표현되는 아이템
     *    //Table 생성
     *         mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_addCart +
     *                 "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
     *                 "nickname TEXT," + // 구매자 아이디
     *                 "title TEXT," +    // 가구이름
     *                 "totalCount INTEGER," + // 총 갯수
     *                 "sum INTEGER," +  // 가격
     *                 "purchars TEXT," + // 구매상태? (배송상태로 바꿔야함)
     *                 "image INTEGER," + // 이미지 링크
     *                 "date DEFAULT CURRENT_TIMESTAMP);"); // 산 날짜
     *     }
     */

    String nickname;
    String name;
    String title;
    int totalCount;
    int sum;
    String purchars;
    String image;
    String date;

    public addChatItemClass(String nickname, String name, String title, int totalCount, int sum, String purchars, String image, String date) {
        this.nickname = nickname;
        this.name = name;
        this.title = title;
        this.totalCount = totalCount;
        this.sum = sum;
        this.purchars = purchars;
        this.image = image;
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getPurchars() {
        return purchars;
    }

    public void setPurchars(String purchars) {
        this.purchars = purchars;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
