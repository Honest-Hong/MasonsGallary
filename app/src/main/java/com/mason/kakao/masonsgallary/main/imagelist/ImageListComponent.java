package com.mason.kakao.masonsgallary.main.imagelist;

import dagger.Subcomponent;

/**
 * Created by kakao on 2017. 10. 31..
 */

@Subcomponent(
        modules = {
                ImageListModule.class
        }
)
public interface ImageListComponent {
    void inject(ImageListFragment fragment);
}
