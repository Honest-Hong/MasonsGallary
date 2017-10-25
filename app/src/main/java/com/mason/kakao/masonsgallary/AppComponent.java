package com.mason.kakao.masonsgallary;

import com.mason.kakao.masonsgallary.images.ImagesActivityComponent;
import com.mason.kakao.masonsgallary.images.ImagesActivityModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kakao on 2017. 10. 25..
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    ImagesActivityComponent plus(ImagesActivityModule module);
}
