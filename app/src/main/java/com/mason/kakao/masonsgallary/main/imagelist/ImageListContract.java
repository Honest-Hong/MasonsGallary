package com.mason.kakao.masonsgallary.main.imagelist;

import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.List;

/**
 * Created by kakao on 2017. 10. 23..
 */

public interface ImageListContract {
    interface View {
        void showImageList(List<ImageListData> list);
        void showLoadingIndicator(boolean show);
        void onChangeChecked(ImageListData listData);
        void showDeleteMenu(boolean show);
        void onImageRemoved(ImageData listData);
        void onImageTagChanged(ImageData imageData);
        void onShowDetail(ImageListData listData);
        void onChangeTag(Tag tag);
        void setVisible(boolean visible);
    }

    interface Presenter {
        void bindView(View view);
        void loadImages(Tag tag);
        void removeCheckedImages();
        void onImageClick(ImageListData listData);
        void onImageLongClick(ImageListData listData);
    }
}
