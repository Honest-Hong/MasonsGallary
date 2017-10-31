package com.mason.kakao.masonsgallary.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mason.kakao.masonsgallary.R;
import com.mason.kakao.masonsgallary.base.BaseFragment;
import com.mason.kakao.masonsgallary.dialog.SelectingTagDialog;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;
import com.mason.kakao.masonsgallary.util.TagUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageDetailFragment extends BaseFragment {
    private Context context;

    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.image_tag)
    ImageView imageTag;

    @BindView(R.id.text_detail)
    TextView textDetail;

    private ImageData imageData;

    private ImageDetailEvents imageDetailEvents;

    public static ImageDetailFragment newInstance(ImageData imageData) {
        ImageDetailFragment fragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ImageData.class.getName(), imageData);
        fragment.setArguments(args);
        return fragment;
    }

    public interface ImageDetailEvents {
        void doDeleteFromDetail(ImageData imageData);
        void changeTagFromDetail(ImageData imageData);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  ImageDetailEvents) {
            imageDetailEvents = (ImageDetailEvents)context;
        }
    }

    @Override
    public void init() {
        imageData = getArguments().getParcelable(ImageData.class.getName());
        if(imageData == null) {
            throw new NullPointerException("imageData must not null");
        }
        this.context = getContext();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_image_detail;
    }

    @Override
    public void setupView(View rootView) {
        ButterKnife.bind(this, rootView);

        Picasso.with(context)
                .load(new File(imageData.getPath()))
                .fit()
                .centerInside()
                .into(imageView);

        int drawableId = TagUtil.getDrawableResourceByTag(imageData.getTag());
        if(drawableId != 0) {
            Picasso.with(context)
                    .load(drawableId)
                    .fit()
                    .into(imageTag);
        }
    }

    @OnClick(R.id.button_detail)
    public void showDetail() {

    }

    @OnClick(R.id.button_delete)
    public void doDelete() {
        imageDetailEvents.doDeleteFromDetail(imageData);
    }

    @OnClick(R.id.image_tag)
    public void showTagDialog() {
        SelectingTagDialog.newInstance(imageData, new SelectingTagDialog.OnSelectListener() {
            @Override
            public void onSelect(Tag tag) {
                Picasso.with(context)
                        .load(TagUtil.getDrawableResourceByTag(tag))
                        .fit()
                        .into(imageTag);
                imageDetailEvents.changeTagFromDetail(imageData);
            }
        }).show(getChildFragmentManager(), SelectingTagDialog.class.getName());
    }
}
