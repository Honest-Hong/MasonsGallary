package com.mason.kakao.masonsgallary.base

import io.reactivex.Observer
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable

/**
 * Created by kakao on 2017. 10. 20..
 * onNext와 onError만을 오버라이드 하도록 만든 간단한 옵저버
 */

abstract class SimpleObserver<T> : Observer<T> {
    override fun onSubscribe(@NonNull d: Disposable) {}

    abstract override fun onNext(@NonNull t: T)

    abstract override fun onError(@NonNull e: Throwable)

    override fun onComplete() {}
}
