package com.mason.kakao.masonsgallary.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.view.adapter.holder.ImageVH;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kakao on 2017. 10. 20..
 * 이미지 목록 어뎁터
 * 아이템 클릭 이벤트를 전달해준다
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImageVH> {
    private Context context;
    private List<ImageListData> list;
    private ItemEventListener itemEventListener;

    public interface ItemEventListener {
        void onClick(ImageListData imageData);
        void onLongClick(ImageListData imageData);
    }

    public ImagesAdapter(Context context, ItemEventListener itemEventListener) {
        this.context = context;
        this.list = Collections.emptyList();
        this.itemEventListener = itemEventListener;
    }

    @Override
    public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final ImageVH holder = new ImageVH(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                itemEventListener.onClick(list.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                itemEventListener.onLongClick(list.get(position));
                return false;
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(final ImageVH holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 목록 설정 메소드
     * @param list 이미지 목록
     */
    public void setList(List<ImageData> list) {
        this.list = new ArrayList<>();
        for(ImageData imageData : list) {
            this.list.add(new ImageListData(imageData));
        }
        notifyDataSetChanged();
    }

    /**
     * 이미지 데이터 변경 메소드
     * 다시 Bind할 수 있도록 해준다
     * @param imageData 변경된 이미지 데이터
     */
    public void changeImageData(ImageListData imageData) {
        int index = list.indexOf(imageData);
        notifyItemChanged(index);
    }

    /**
     * 이미지 데이터 제거 메소드
     * 목록에서 이미지를 제거시켜준다
     * @param imageData 제거된 이미지 데이터
     */
    public void removeImageData(ImageListData imageData) {
        int index = list.indexOf(imageData);
        list.remove(index);
        notifyItemRemoved(index);
    }
}
