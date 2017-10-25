package com.mason.kakao.masonsgallary.images;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mason.kakao.masonsgallary.R;
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

    public void setList(List<ImageData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final ImageVH holder = new ImageVH(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                mTagChangeListener.selectTag(list.get(position));
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ImageVH holder, int position) {
        holder.setupView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
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
