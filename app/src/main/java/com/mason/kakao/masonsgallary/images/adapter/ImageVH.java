package com.mason.kakao.masonsgallary.images.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseVH;
import com.mason.kakao.masonsgallary.databinding.ItemImageBinding;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class ImageVH extends BaseVH<ImageListData>{
    private ItemImageBinding mBinding;

    public ImageVH(ItemImageBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    @Override
    public void setupView(ImageListData listData) {
        int drawableId = 0;
        switch(listData.getImageData().getTag()) {
            case Ryan:
                drawableId = R.drawable.tag_ryan;
                break;
            case Muzi:
                drawableId = R.drawable.tag_muzi;
                break;
            case Apeach:
                drawableId = R.drawable.tag_apeach;
                break;
            case Frodo:
                drawableId = R.drawable.tag_frodo;
                break;
            case Neo:
                drawableId = R.drawable.tag_neo;
                break;
            case Tube:
                drawableId = R.drawable.tag_tube;
                break;
            case Jay_G:
                drawableId = R.drawable.tag_jay_g;
                break;
            case Con:
                drawableId = R.drawable.tag_con;
                break;
        }
        if(drawableId == 0) {
            mBinding.imageTag.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else {
            Picasso.with(mBinding.imageTag.getContext())
                    .load(drawableId)
                    .fit()
                    .into(mBinding.imageTag);
        }
        Picasso.with(mBinding.imageView.getContext())
                .load(new File(listData.getImageData().getPath()))
                .fit()
                .centerCrop()
                .into(mBinding.imageView);
        mBinding.setListData(listData);
    }
}
