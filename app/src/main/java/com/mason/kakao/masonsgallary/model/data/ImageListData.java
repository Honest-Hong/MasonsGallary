package com.mason.kakao.masonsgallary.model.data;

/**
 * Created by kakao on 2017. 10. 30..
 * 목록 전용 이미지 데이터
 * 이미지 데이터 객체와 체크 유무를 가지고있다.
 */

public class ImageListData {
    // 이미지 데이터
    private ImageData imageData;
    // 체크 유무
    private boolean isChecked;

    public ImageListData(ImageData imageData) {
        this.imageData = imageData;
        this.isChecked = false;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public void updateImageData(ImageData imageData) {
        imageData.setTag(imageData.getTag());
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
