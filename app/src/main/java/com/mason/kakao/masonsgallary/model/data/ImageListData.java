package com.mason.kakao.masonsgallary.model.data;

/**
 * Created by kakao on 2017. 10. 30..
 */

public class ImageListData {
    private ImageData imageData;
    private boolean isChecked;

    public ImageListData(ImageData imageData) {
        this.imageData = imageData;
        this.isChecked = false;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public void updateImageData(ImageData imageData) {
        this.imageData.setTag(imageData.getTag());
    }

    public void setImageData(ImageData imageData) {
        this.imageData = imageData;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return imageData.toString() + ", checked(" + isChecked + ")";
    }
}
