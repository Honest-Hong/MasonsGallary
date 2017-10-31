package com.mason.kakao.masonsgallary.main.detail;

import com.mason.kakao.masonsgallary.model.ImagesRepository;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

/**
 * Created by kakao on 2017. 10. 31..
 */

public class ImageDetailPresenterImpl implements ImageDetailContract.Presenter {
    private ImageDetailContract.View view;
    private ImagesRepository repository;

    public ImageDetailPresenterImpl(ImagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void bindView(ImageDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void changeImageTag(ImageData imageData, Tag tag) {
        imageData.setTag(tag);
        repository.changeImageData(imageData);
        view.onImageTagChanged(tag);
    }

    @Override
    public void removeImage(ImageData imageData) {
        repository.removeImageData(imageData);
        view.onImageRemoved(imageData);
    }
}
