package com.mason.kakao.masonsgallary.images;

import dagger.Subcomponent;

/**
 * Created by kakao on 2017. 10. 25..
 */

@Subcomponent(
    modules = {
        ImagesActivityModule.class
    }
)
public interface ImagesActivityComponent {
    void inject(ImagesActivity imagesActivity);
}
