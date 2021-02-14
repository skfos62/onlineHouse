package com.example.house2;

public class community_json_tag {
    private int id;
    private String imageURL;
    private String con;
    private String title;
    private String writer;
    private String writerDate;
    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public community_json_tag(int id, String imageURL, String con, String title, String writer, String writerDate, String tag) {
        this.id = id;
        this.imageURL = imageURL;
        this.con = con;
        this.title = title;
        this.writer = writer;
        this.writerDate = writerDate;
        this.tag = tag;
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
                ", tag='" + tag + '\'' +
                '}';
    }

}
