package com.mason.kakao.masonsgallary.main.detail;

import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

/**
 * Created by kakao on 2017. 10. 31..
 */

public interface ImageDetailContract {
    interface View {
        void onImageTagChanged(Tag tag);
        void onImageRemoved(ImageData imageData);
    }

    interface Presenter {
        void bindView(View view);
        void changeImageTag(ImageData imageData, Tag tag);
        void removeImage(ImageData imageData);
    }
}
