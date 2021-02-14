package com.example.house2.chat;

public class DataItem {

    private String content;
    private String name;
    private int viewType;
    private String RoomNum;

    public DataItem(String content, String name, int viewType, String roomNum) {
        this.content = content;
        this.name = name;
        this.viewType = viewType;
        this.RoomNum = roomNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getRoomNum() {
        return RoomNum;
    }

    public void setRoomNum(String roomNum) {
        RoomNum = roomNum;
    }
}
