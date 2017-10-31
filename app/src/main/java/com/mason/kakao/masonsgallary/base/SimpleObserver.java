package com.mason.kakao.masonsgallary.base;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;

/**
 * Created by kakao on 2017. 10. 20..
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
