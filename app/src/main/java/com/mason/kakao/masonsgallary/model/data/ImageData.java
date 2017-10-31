package com.mason.kakao.masonsgallary.model.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kakao on 2017. 10. 20..
 * 이미지 데이터 클래스
 * Bundle에 저장하기 위해 Parcelable를 구현했다.
 */

public class ImageData implements Parcelable{
    // 이미지 파일 경로
    private String path;
    // 이미지 파일 이름
    private String name;
    // 이미지 파일 생성 시간
    private String date;
    // 이미지 태그
    private Tag tag;

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
        tag = Tag.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(tag.name());
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
}
