package com.mason.kakao.masonsgallary.base;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by kakao on 2017. 10. 20..
 * onNext와 onError만을 오버라이드 하도록 만든 간단한 옵저버
 */

public abstract class SimpleObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {
    }

    @Override
    public abstract void onNext(@NonNull T t);

    @Override
    public abstract void onError(@NonNull Throwable e);

    @Override
    public void onComplete() {
    }
}
