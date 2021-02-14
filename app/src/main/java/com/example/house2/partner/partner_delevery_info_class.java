package com.example.house2.partner;

public class partner_delevery_info_class {

    //                 * 송장번호를 입력하면 주소, 구매자이름, 송장번호, 결제정보id, 택배사를 저장하는 db(h_partnerDeleveryInfoInsert.php)
//                *  db 이름 : delevery_info
//                *  주소 : adress (text)
//                *  구매자이름 : buyer (text)
//                *  송장번호 : deleveryNum (int)
//         *  결제정보id : saleNum (int)
//         *  택배사 이름 : companyName (text)
    // 받는사람 이름 Recipient;

    String adress;
    String buyer;
    String deleveryNum;
    String saleNum;
    String companyName;
    String Recipient;

    public partner_delevery_info_class(String adress, String buyer, String deleveryNum, String saleNum, String companyName, String recipient) {
        this.adress = adress;
        this.buyer = buyer;
        this.deleveryNum = deleveryNum;
        this.saleNum = saleNum;
        this.companyName = companyName;
        this.Recipient = recipient;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getDeleveryNum() {
        return deleveryNum;
    }

    public void setDeleveryNum(String deleveryNum) {
        this.deleveryNum = deleveryNum;
    }

    public String getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(String saleNum) {
        this.saleNum = saleNum;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRecipient() {
        return Recipient;
    }

    public void setRecipient(String recipient) {
        Recipient = recipient;
    }
}
