package com.mason.kakao.masonsgallary.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.view.adapter.holder.ImageVH;

import java.util.Collections;
import java.util.List;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImageVH> {
    private Context context;
    private List<ImageData> list;
    private TagChangeListener tagChangeListener;

    public ImagesAdapter(Context context, TagChangeListener tagChangeListener) {
        this.context = context;
        this.list = Collections.emptyList();
        this.tagChangeListener = tagChangeListener;
    }

    @Override
    public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageVH(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageVH holder, final int position) {
        holder.setupView(list.get(position));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tagChangeListener.selectTag(list.get(position));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface TagChangeListener {
        void selectTag(ImageData imageData);
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
