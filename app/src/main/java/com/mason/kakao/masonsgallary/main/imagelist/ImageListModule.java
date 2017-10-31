package com.mason.kakao.masonsgallary.main.imagelist;

import com.mason.kakao.masonsgallary.model.ImagesRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kakao on 2017. 10. 31..
 */

@Module
public class ImageListModule {
    @Provides
    ImageListContract.Presenter providePresenter(ImagesRepository repository) {
        return new ImageListPresenterImpl(repository);
    }
}
