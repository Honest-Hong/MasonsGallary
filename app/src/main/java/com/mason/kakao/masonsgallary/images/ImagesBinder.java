package com.mason.kakao.masonsgallary.images;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.mason.kakao.masonsgallary.images.adapter.ImagesAdapter;
import com.mason.kakao.masonsgallary.model.data.ImageData;

import java.util.List;

/**
 * Created by kakao on 2017. 10. 23..
 */

public class ImagesBinder {
    @BindingAdapter("app:items")
    public static void setImageList(RecyclerView recyclerView, List<ImageData> list) {
        ImagesAdapter adapter = (ImagesAdapter) recyclerView.getAdapter();
        adapter.setList(list);
    }
}
