package com.mason.kakao.masonsgallary;

import com.mason.kakao.masonsgallary.main.detail.ImageDetailComponent;
import com.mason.kakao.masonsgallary.main.detail.ImageDetailModule;
import com.mason.kakao.masonsgallary.main.imagelist.ImageListComponent;
import com.mason.kakao.masonsgallary.main.imagelist.ImageListModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kakao on 2017. 10. 31..
 */

@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {
    ImageListComponent plus(ImageListModule module);
    ImageDetailComponent plus(ImageDetailModule module);
}
