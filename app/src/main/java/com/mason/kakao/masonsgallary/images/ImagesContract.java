package com.mason.kakao.masonsgallary.images;

import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.List;

/**
 * Created by kakao on 2017. 10. 23..
 */

public interface ImagesContract {
    interface View {
        void showImages(List<ImageListData> list);
        void showLoadingIndicator(boolean show);
        void showChecked(ImageListData listData);
        void showDeleteMenu(boolean show);
        void removeListItem(ImageListData listData);
    }

    interface Presenter {
        void loadImages(Tag tag);
        void changeImageData(ImageListData listData);
        void checkImageList(ImageListData listData);
        void removeCheckedImages();
    }
}
