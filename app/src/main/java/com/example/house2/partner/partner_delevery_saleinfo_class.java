package com.example.house2.partner;

public class partner_delevery_saleinfo_class {

    int id;
    String productName;
    int productNum;
    String buyer;
    int payment;
    String sellerName;
    String date;
    String productState;
    String Recipient;
    String RecipAdress;
    String Reciphone;


    public partner_delevery_saleinfo_class(int id, String productName, int productNum, String buyer, int payment, String sellerName, String date, String productState, String recipient, String recipAdress, String reciphone) {
        this.id = id;
        this.productName = productName;
        this.productNum = productNum;
        this.buyer = buyer;
        this.payment = payment;
        this.sellerName = sellerName;
        this.date = date;
        this.productState = productState;
        Recipient = recipient;
        RecipAdress = recipAdress;
        Reciphone = reciphone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductNum() {
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }

    public String getRecipAdress() {
        return RecipAdress;
    }

    public void setRecipAdress(String recipAdress) {
        RecipAdress = recipAdress;
    }

    public String getReciphone() {
        return Reciphone;
    }

    public void setReciphone(String reciphone) {
        Reciphone = reciphone;
    }
}
