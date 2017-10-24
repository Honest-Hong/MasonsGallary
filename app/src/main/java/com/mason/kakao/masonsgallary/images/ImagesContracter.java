package com.mason.kakao.masonsgallary.images;

import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.List;

/**
 * Created by kakao on 2017. 10. 23..
 */

public interface ImagesContracter {
    interface View {
        void showImages(List<ImageData> list);
        void showLoadingIndicator(boolean show);
    }

    interface Presenter {
        void loadImages(Tag tag);
        void changeImageData(ImageData imageData);
    }
}
