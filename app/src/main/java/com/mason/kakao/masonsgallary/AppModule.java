package com.mason.kakao.masonsgallary;

import android.app.Application;

import com.mason.kakao.masonsgallary.model.ImagesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kakao on 2017. 10. 25..
 */

@Module
public class AppModule {
    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return this.application;
    }

    @Provides
    @Singleton
    ImagesRepository provideImagesRepository() {
        return new ImagesRepository(application);
    }
}
