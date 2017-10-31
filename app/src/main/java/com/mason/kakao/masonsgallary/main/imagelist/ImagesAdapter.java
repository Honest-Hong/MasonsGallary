package com.mason.kakao.masonsgallary.main.imagelist;

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
    private ImageListItemEvent imageListItemEvent;

    public interface ImageListItemEvent {
        void onImageClick(ImageListData imageListData);
        void onImageLongClick(ImageListData imageListData);
    }

    public ImagesAdapter(Context context, ImageListItemEvent imageListItemEvent) {
        this.context = context;
        this.list = Collections.emptyList();
        this.imageListItemEvent = imageListItemEvent;
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
                imageListItemEvent.onImageClick(list.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                imageListItemEvent.onImageLongClick(list.get(position));
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

    public void changeImageData(ImageData imageData) {
        int index = -1;
        for(int i=0; i<list.size(); i++) {
            if(imageData.getPath().equals(list.get(i).getImageData().getPath())) {
                index = i;
                break;
            }
        }
        list.get(index).updateImageData(imageData);
        notifyItemChanged(index);
    }

    public void removeImageData(ImageListData imageData) {
        int index = list.indexOf(imageData);
        list.remove(index);
        notifyItemRemoved(index);
    }

    public void removeImageData(ImageData imageData) {
        int index = -1;
        for(int i=0; i<list.size(); i++) {
            if(imageData.getPath().equals(list.get(i).getImageData().getPath())) {
                index = i;
                break;
            }
        }
        list.remove(index);
        notifyItemRemoved(index);
    }
}
