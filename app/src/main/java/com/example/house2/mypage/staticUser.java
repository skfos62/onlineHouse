package com.example.house2.mypage;

public class staticUser {
    String id;
    String name;
    String pw;
    String email;
    String nickname;
    String image;

    public staticUser(String id, String name, String pw, String email, String nickname, String image) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.email = email;
        this.nickname = nickname;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


