package com.mason.kakao.masonsgallary.images.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.databinding.ItemImageBinding;
import com.mason.kakao.masonsgallary.images.TagChangeListener;
import com.mason.kakao.masonsgallary.model.data.ImageData;

import java.util.Collections;
import java.util.List;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImageVH> {
    private Context context;
    private List<ImageData> list;
    private TagChangeListener mTagChangeListener;

    public ImagesAdapter(Context context, TagChangeListener tagChangeListener) {
        this.context = context;
        this.list = Collections.emptyList();
        this.mTagChangeListener = tagChangeListener;
    }

    @Override
    public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_image, parent, false);
        return new ImageVH(binding);
    }

    @Override
    public void onBindViewHolder(ImageVH holder, final int position) {
        holder.setupView(list.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mTagChangeListener.selectTag(list.get(position));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ImageData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void changeImageData(ImageData imageData) {
        int index = list.indexOf(imageData);
        notifyItemChanged(index);
    }

    public void removeImageData(ImageData imageData) {
        int index = list.indexOf(imageData);
        list.remove(index);
        notifyItemRemoved(index);
    }
}
