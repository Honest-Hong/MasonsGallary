package com.mason.kakao.masonsgallary.images;

import com.mason.kakao.masonsgallary.model.ImagesRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kakao on 2017. 10. 25..
 */
@Module
public class ImagesActivityModule {
    private ImagesActivity imagesActivity;

    public ImagesActivityModule(ImagesActivity imagesActivity) {
        this.imagesActivity = imagesActivity;
    }

    @Provides
    ImagesContracter.Presenter providePresenter(ImagesRepository imagesRepository) {
        return new ImagesPresenter(imagesActivity, imagesRepository);
    }

    @Provides
    ImagesAdapter provideAdapter() {
        return new ImagesAdapter(imagesActivity, imagesActivity);
    }
}
