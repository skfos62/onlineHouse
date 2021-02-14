package com.example.house2;

public class community_json {
    private int id;
    private String imageURL;
    private String con;
    private String title;
    private String writer;
    private String writerDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return imageURL;
    }

    public void setImage(String image) {
        this.imageURL = image;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriterDate() {
        return writerDate;
    }

    public void setWriterDate(String writerDate) {
        this.writerDate = writerDate;
    }

    @Override
    public String toString() {
        return "community_json{" +
                "id=" + id +
                ", imageURL='" + imageURL + '\'' +
                ", con='" + con + '\'' +
                ", title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                ", writerDate='" + writerDate + '\'' +
                '}';
    }
}
