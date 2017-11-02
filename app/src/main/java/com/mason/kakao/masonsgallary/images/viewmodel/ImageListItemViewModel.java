package com.mason.kakao.masonsgallary.images.viewmodel;

import android.databinding.ObservableBoolean;

import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.util.TagUtil;

/**
 * Created by kakao on 2017. 11. 2..
 */

public class ImageListItemViewModel {
    private ImageData imageData;
    public final ObservableBoolean checked = new ObservableBoolean(false);

    public ImageListItemViewModel(ImageData imageListData) {
        this.imageData = imageListData;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public String getPath() {
        return imageData.getPath();
    }

    public String getName() {
        return imageData.getName();
    }

    public String getDate() {
        return imageData.getDate();
    }

    public int getTagImage() {
        return TagUtil.getResourceIdByTag(imageData.getTag());
    }
}
