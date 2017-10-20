package com.mason.kakao.masonsgallary;

import android.app.Application;
import android.content.Context;

import com.mason.kakao.masonsgallary.model.ImagesRepository;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class MasonApplication extends Application {
    private ImagesRepository imagesRepository;

    public static MasonApplication get(Context context) {
        return (MasonApplication)context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        imagesRepository = new ImagesRepository(this);
    }

    public ImagesRepository getImagesRepository() {
        return imagesRepository;
    }
}
