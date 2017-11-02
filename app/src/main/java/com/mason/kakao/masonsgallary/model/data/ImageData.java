package com.mason.kakao.masonsgallary.model.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class ImageData implements Parcelable{
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

    protected ImageData(Parcel in) {
        path = in.readString();
        name = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

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

    @Override
    public String toString() {
        return String.format("path(%s), name(%s), tag(%s), date(%s)", path, name, tag, date);
    }
}
