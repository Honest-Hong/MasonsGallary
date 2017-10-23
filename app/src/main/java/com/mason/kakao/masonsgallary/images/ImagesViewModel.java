package com.mason.kakao.masonsgallary.images;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.mason.kakao.masonsgallary.base.SimpleObserver;
import com.mason.kakao.masonsgallary.model.ImagesRepository;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by kakao on 2017. 10. 23..
 */

public class ImagesViewModel {
    public final ObservableBoolean showList = new ObservableBoolean(false);
    public final ObservableArrayList<ImageData> mList = new ObservableArrayList<>();
    private ImagesRepository mRepository;

    public ImagesViewModel(ImagesRepository mRepository) {
        this.mRepository = mRepository;
    }

    public void loadList(Tag tag) {
        showList.set(false);
        mList.clear();
        mRepository.getList(tag)
                .subscribe(new SimpleObserver<List<ImageData>>() {
                    @Override
                    public void onNext(@NonNull List<ImageData> list) {
                        showList.set(true);
                        mList.addAll(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showList.set(true);
                    }
                });
    }

    public List<ImageData> getList() {
        return mList;
    }
}
