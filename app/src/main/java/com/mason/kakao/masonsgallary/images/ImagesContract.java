package com.mason.kakao.masonsgallary.images;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

/**
 * Created by kakao on 2017. 10. 24..
 */

public interface ImagesContract {
    interface ViewModel {
        void onCreate();
        void onLongClick(final ImageData imageData);
        void changeFilter(Tag tag);
        ObservableArrayList<ImageData> getList();
        ObservableBoolean showList();
    }
}
