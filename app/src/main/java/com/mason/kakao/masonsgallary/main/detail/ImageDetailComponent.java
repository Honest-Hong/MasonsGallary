package com.mason.kakao.masonsgallary.main.detail;

import dagger.Subcomponent;

/**
 * Created by kakao on 2017. 10. 31..
 */

@Subcomponent(
        modules = {
                ImageDetailModule.class
        }
)
public interface ImageDetailComponent {
    void inject(ImageDetailFragment fragment);
}
