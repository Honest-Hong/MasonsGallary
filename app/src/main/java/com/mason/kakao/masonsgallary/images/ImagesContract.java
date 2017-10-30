package com.mason.kakao.masonsgallary.images;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;

/**
 * Created by kakao on 2017. 10. 24..
 */

public interface ImagesContract {
    interface ViewModel {
        void onCreate();
        void changeFilter(Tag tag);
        ObservableArrayList<ImageListData> getList();
        ObservableBoolean showList();
        void onClick(ImageListData listData);
        void onLongClick(ImageListData listData);
        void removeCheckedList();
    }
}
