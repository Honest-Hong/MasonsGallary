package com.mason.kakao.masonsgallary.images.adapter;

import com.mason.kakao.masonsgallary.base.BaseVH;
import com.mason.kakao.masonsgallary.databinding.ItemImageBinding;
import com.mason.kakao.masonsgallary.images.viewmodel.ImageListItemViewModel;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class ImageVH extends BaseVH<ImageListItemViewModel>{
    private ItemImageBinding mBinding;

    public ImageVH(ItemImageBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    @Override
    public void setupView(ImageListItemViewModel viewModel) {
        mBinding.setListItemVM(viewModel);
        mBinding.executePendingBindings();
    }
}
