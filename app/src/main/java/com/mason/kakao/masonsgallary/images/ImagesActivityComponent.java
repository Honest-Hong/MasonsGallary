package com.mason.kakao.masonsgallary.images;

import com.mason.kakao.masonsgallary.PerActivity;

import dagger.Subcomponent;

/**
 * Created by kakao on 2017. 10. 25..
 */

@PerActivity
@Subcomponent(
    modules = {
        ImagesActivityModule.class
    }
)
public interface ImagesActivityComponent {
    void inject(ImagesActivity imagesActivity);
}
