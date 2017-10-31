package com.mason.kakao.masonsgallary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by kakao on 2017. 10. 20..
 * 기본 액티비티 로직을 가지고있는 클래스
 * onCreate의 처음 부분에 액티비티를 초기화하는 setupActivity를 호출한다
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivity();
    }

    public abstract void setupActivity();
}
