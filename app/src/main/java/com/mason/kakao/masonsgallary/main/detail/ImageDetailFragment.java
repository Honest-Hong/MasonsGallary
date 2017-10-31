package com.mason.kakao.masonsgallary.main.detail;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mason.kakao.masonsgallary.ExtraKeys;
import com.mason.kakao.masonsgallary.MasonApplication;
import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseFragment;
import com.mason.kakao.masonsgallary.dialog.SelectingTagDialog;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;
import com.mason.kakao.masonsgallary.util.TagUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kakao on 2017. 10. 31..
 */

public class ImageDetailFragment extends BaseFragment implements ImageDetailContract.View {
    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.image_tag)
    ImageView imageTag;

    @Inject
    ImageDetailContract.Presenter presenter;

    private Context context;

    private ImageData imageData;

    private ImageDataChangeListener imageDataChangeListener;

    public interface ImageDataChangeListener {
        void onImageRemoved(ImageData imageData);
        void onImageTagChange(ImageData imageData);
    }

    public static ImageDetailFragment newInstance(ImageData imageData) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ExtraKeys.IMAGE_DATA, imageData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init() {
        this.context = getContext();
        MasonApplication.get(context)
                .getAppComponent()
                .plus(new ImageDetailModule())
                .inject(this);
        presenter.bindView(this);
        imageData = getArguments().getParcelable(ExtraKeys.IMAGE_DATA);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ImageDataChangeListener) {
            imageDataChangeListener = (ImageDataChangeListener) context;
        }
    }

    @Override
    public void setupView(View view) {
        ButterKnife.bind(this, view);

        Picasso.with(context)
                .load(new File(imageData.getPath()))
                .fit()
                .centerInside()
                .into(imageView);

        Picasso.with(context)
                .load(TagUtil.getResourceIdByTag(imageData.getTag()))
                .fit()
                .into(imageTag);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_detail;
    }

    @Override
    public void onImageTagChanged(Tag tag) {
        Picasso.with(context)
                .load(TagUtil.getResourceIdByTag(tag))
                .fit()
                .into(imageTag);
        imageDataChangeListener.onImageTagChange(imageData);
    }

    @Override
    public void onImageRemoved(ImageData imageData) {
        imageDataChangeListener.onImageRemoved(imageData);
    }

    @OnClick(R.id.button_detail)
    public void showDetail() {
    }

    @OnClick(R.id.button_delete)
    public void removeImage() {
        presenter.removeImage(imageData);
    }

    @OnClick(R.id.image_tag)
    public void changeTag() {
        SelectingTagDialog.newInstance(imageData, new SelectingTagDialog.OnSelectListener() {
            @Override
            public void onSelect(Tag tag) {
                presenter.changeImageTag(imageData, tag);
            }
        }).show(getChildFragmentManager(), SelectingTagDialog.class.getName());
    }
}
