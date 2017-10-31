package com.mason.kakao.masonsgallary.main.detail;

import com.mason.kakao.masonsgallary.model.ImagesRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kakao on 2017. 10. 31..
 */

@Module
public class ImageDetailModule {
    @Provides
    ImageDetailContract.Presenter providePresenter(ImagesRepository repository) {
        return new ImageDetailPresenterImpl(repository);
    }
}
