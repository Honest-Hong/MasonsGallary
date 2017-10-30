package com.mason.kakao.masonsgallary.images;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mason.kakao.masonsgallary.images.adapter.ImagesAdapter;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;

import java.util.List;

/**
 * Created by kakao on 2017. 10. 23..
 */

public class ImagesBinder {
    @BindingAdapter("app:items")
    public static void setImageList(RecyclerView recyclerView, List<ImageListData> list) {
        ImagesAdapter adapter = (ImagesAdapter) recyclerView.getAdapter();
        adapter.setList(list);
    }

    @BindingAdapter("app:onLongClick")
    public static void setOnLongClick(View view, final ImageLongClickListener longClickListener) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onLongClick(null);
                return false;
            }
        });
    }
}
