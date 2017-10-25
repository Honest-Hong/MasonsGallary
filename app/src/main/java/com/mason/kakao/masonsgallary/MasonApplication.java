package com.mason.kakao.masonsgallary;

import android.app.Application;
import android.content.Context;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class MasonApplication extends Application {

    private AppComponent appComponent;

    public static MasonApplication get(Context context) {
        return (MasonApplication)context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
