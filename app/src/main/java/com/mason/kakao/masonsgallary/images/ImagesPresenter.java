package com.mason.kakao.masonsgallary.images;

import com.mason.kakao.masonsgallary.MasonApplication;
import com.mason.kakao.masonsgallary.base.SimpleObserver;
import com.mason.kakao.masonsgallary.model.ImagesRepository;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by kakao on 2017. 10. 23..
 */

public class ImagesPresenter implements ImagesContracter.Presenter{
    private ImagesContracter.View mView;
    private ImagesRepository mRepository;

    public ImagesPresenter(ImagesContracter.View mView, ImagesRepository repository) {
        this.mView = mView;
        this.mRepository = repository;
    }

    @Override
    public void loadImages(Tag tag) {
        mView.showLoadingIndicator(true);
        mRepository.getList(tag).subscribe(new SimpleObserver<List<ImageData>>() {
            @Override
            public void onNext(@NonNull List<ImageData> list) {
                mView.showLoadingIndicator(false);
                mView.showImages(list);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mView.showLoadingIndicator(false);
            }
        });
    }

    @Override
    public void changeImageData(ImageData imageData) {
        mRepository.changeImageData(imageData);
    }
}
