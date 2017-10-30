package com.mason.kakao.masonsgallary.images;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;

import java.util.Collections;
import java.util.List;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImageVH> {
    private Context context;
    private List<ImageListData> list;
    private ItemChangeListener itemChangeListener;

    public interface ItemChangeListener {
        void onClick(ImageListData listData);
        void onLongClick(ImageListData listData);
    }

    public ImagesAdapter(Context context, ItemChangeListener itemChangeListener) {
        this.context = context;
        this.list = Collections.emptyList();
        this.itemChangeListener = itemChangeListener;
    }

    public void setList(List<ImageListData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final ImageVH holder = new ImageVH(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                itemChangeListener.onClick(list.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                itemChangeListener.onLongClick(list.get(position));
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

    public void changeImageData(ImageListData imageData) {
        int index = list.indexOf(imageData);
        notifyItemChanged(index);
    }

    public void removeImageData(ImageListData imageData) {
        int index = list.indexOf(imageData);
        list.remove(index);
        notifyItemRemoved(index);
    }
}
