package com.mason.kakao.masonsgallary.model.data;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class ImageData {
    private String path;
    private String name;
    private Tag tag;
    private String date;

    public ImageData(String path, String name, Tag tag, String date) {
        this.path = path;
        this.name = name;
        this.tag = tag;
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }
}
