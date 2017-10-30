package com.mason.kakao.masonsgallary.images;

import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;

/**
 * Created by kakao on 2017. 10. 24..
 */

public interface ItemEventListener {
    void onClick(ImageListData listData);
    void onLongClick(ImageListData imageData);
}
