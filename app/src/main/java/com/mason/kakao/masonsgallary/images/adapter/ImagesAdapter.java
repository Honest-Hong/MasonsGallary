package com.mason.kakao.masonsgallary.images.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.databinding.ItemImageBinding;
import com.mason.kakao.masonsgallary.images.ImagesContract;
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
    private ImagesContract.ViewModel mViewModel;

    public ImagesAdapter(Context context, ImagesContract.ViewModel viewModel) {
        this.context = context;
        this.list = Collections.emptyList();
        this.mViewModel = viewModel;
    }

    @Override
    public ImageVH onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_image, parent, false);
        binding.setViewModel(mViewModel);
        return new ImageVH(binding);
    }

    @Override
    public void onBindViewHolder(ImageVH holder, final int position) {
        holder.setupView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ImageListData> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
