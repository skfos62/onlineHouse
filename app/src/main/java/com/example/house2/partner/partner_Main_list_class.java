package com.example.house2.partner;

public class partner_Main_list_class {
    String productName;
    int productNum;
    String buyer;
    int payment;
    String sellerName;
    String date;
    String productState;
    String Recipient;
    String RecipientAdress;
    String ReciphoneNum;

    public partner_Main_list_class(String productName, int productNum, String buyer, int payment, String sellerName, String date, String productState, String recipient, String recipientAdress, String reciphoneNum) {
        this.productName = productName;
        this.productNum = productNum;
        this.buyer = buyer;
        this.payment = payment;
        this.sellerName = sellerName;
        this.date = date;
        this.productState = productState;
        this.Recipient = recipient;
        this.RecipientAdress = recipientAdress;
        this.ReciphoneNum = reciphoneNum;
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

    public String getRecipientAdress() {
        return RecipientAdress;
    }

    public void setRecipientAdress(String recipientAdress) {
        RecipientAdress = recipientAdress;
    }

    public String getReciphoneNum() {
        return ReciphoneNum;
    }

    public void setReciphoneNum(String reciphoneNum) {
        ReciphoneNum = reciphoneNum;
    }
}

