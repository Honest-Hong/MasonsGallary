package com.mason.kakao.masonsgallary.ui.images;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.mason.kakao.masonsgallary.ui.images.adapter.ImagesAdapter;
import com.mason.kakao.masonsgallary.ui.images.viewmodel.ImageListItemViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by kakao on 2017. 10. 23..
 */

public class ImagesBinder {
    @BindingAdapter("app:items")
    public static void setImageList(RecyclerView recyclerView, List<ImageListItemViewModel> list) {
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

    @BindingAdapter("picasso:fitCenterCrop")
    public static void setImage(ImageView imageView, String path) {
        Picasso.with(imageView.getContext())
                .load(new File(path))
                .fit()
                .centerCrop()
                .into(imageView);
    }

    @BindingAdapter("picasso:fit")
    public static void setImage(ImageView imageView, int resourceId) {
        Picasso.with(imageView.getContext())
                .load(resourceId)
                .fit()
                .into(imageView);
    }

    @BindingAdapter("picasso:fitCenterInside")
    public static void setImageInside(ImageView imageView, String path) {
        Picasso.with(imageView.getContext())
                .load(new File(path))
                .fit()
                .centerInside()
                .into(imageView);
    }
}
