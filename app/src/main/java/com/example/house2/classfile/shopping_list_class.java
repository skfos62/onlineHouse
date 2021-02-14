package com.example.house2.classfile;

/**
 * 쇼핑 리스트 클래스에 들어가야할 요소
 * 1. 가구이름 shopListName
 * 2. 가구설명 shopListSub
 * 3. 가격 shopLisPrice
 * 4. 드루어블 파일 이름 shopLisFilenum
 * 5. 해당하는 sfb파일 이름 shopLisFileName
 * 6. 판매자 이름
 * 7. 판매자 아이디
 *
 */
public class shopping_list_class {

//    Furname 가구이름
//    Furprice 가구가격 int
//    Furcopy 가구 설명
//    Furcategory 가구 카테고리
//    FurArck ar여부
//    FurImage 가구이미지 주소
//    sfa 해당하는 filename(모델이름)


    String Furname;
    int Furprice;
    String Furcopy;
    String Furcategory;
    String FurArck;
    String FurImage;
    String Furtsfa;
    String sellerName;

    public shopping_list_class(String furname, int furprice, String furcopy, String furcategory, String furArck, String furImage, String furtsfa, String sellerName) {
        Furname = furname;
        Furprice = furprice;
        Furcopy = furcopy;
        Furcategory = furcategory;
        FurArck = furArck;
        FurImage = furImage;
        Furtsfa = furtsfa;
        this.sellerName = sellerName;
    }

    public String getFurname() {
        return Furname;
    }

    public void setFurname(String furname) {
        Furname = furname;
    }

    public int getFurprice() {
        return Furprice;
    }

    public void setFurprice(int furprice) {
        Furprice = furprice;
    }

    public String getFurcopy() {
        return Furcopy;
    }

    public void setFurcopy(String furcopy) {
        Furcopy = furcopy;
    }

    public String getFurcategory() {
        return Furcategory;
    }

    public void setFurcategory(String furcategory) {
        Furcategory = furcategory;
    }

    public String getFurArck() {
        return FurArck;
    }

    public void setFurArck(String furArck) {
        FurArck = furArck;
    }

    public String getFurImage() {
        return FurImage;
    }

    public void setFurImage(String furImage) {
        FurImage = furImage;
    }

    public String getFurtsfa() {
        return Furtsfa;
    }

    public void setFurtsfa(String furtsfa) {
        Furtsfa = furtsfa;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    //
//    String shopListName;
//    String shopListSub;
//    String shopLisPrice;
//    int shopLisFilenum;
//    String shopLisFileName;
//
//    public shopping_list_class(String shopListName, String shopListSub, String shopLisPrice, int shopLisFilenum, String shopLisFileName) {
//        this.shopListName = shopListName;
//        this.shopListSub = shopListSub;
//        this.shopLisPrice = shopLisPrice;
//        this.shopLisFilenum = shopLisFilenum;
//        this.shopLisFileName = shopLisFileName;
//    }
//
//    public String getShopListName() {
//        return shopListName;
//    }
//
//    public void setShopListName(String shopListName) {
//        this.shopListName = shopListName;
//    }
//
//    public String getShopListSub() {
//        return shopListSub;
//    }
//
//    public void setShopListSub(String shopListSub) {
//        this.shopListSub = shopListSub;
//    }
//
//    public String getShopLisPrice() {
//        return shopLisPrice;
//    }
//
//    public void setShopLisPrice(String shopLisPrice) {
//        this.shopLisPrice = shopLisPrice;
//    }
//
//    public int getShopLisFilenum() {
//        return shopLisFilenum;
//    }
//
//    public void setShopLisFilenum(int shopLisFilenum) {
//        this.shopLisFilenum = shopLisFilenum;
//    }
//
//    public String getShopLisFileName() {
//        return shopLisFileName;
//    }
//
//    public void setShopLisFileName(String shopLisFileName) {
//        this.shopLisFileName = shopLisFileName;
//    }
}

