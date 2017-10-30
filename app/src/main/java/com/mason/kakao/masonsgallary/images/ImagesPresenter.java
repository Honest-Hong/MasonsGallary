package com.mason.kakao.masonsgallary.images;

import com.mason.kakao.masonsgallary.base.SimpleObserver;
import com.mason.kakao.masonsgallary.model.ImagesRepository;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by kakao on 2017. 10. 23..
 */

public class ImagesPresenter implements ImagesContract.Presenter{
    private ImagesContract.View view;
    private ImagesRepository repository;
    private List<ImageListData> checkedListData = new ArrayList<>();

    public ImagesPresenter(ImagesContract.View view, ImagesRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadImages(Tag tag) {
        view.showLoadingIndicator(true);
        repository.getList(tag)
                .map(new Function<List<ImageData>, List<ImageListData>>() {
                    @Override
                    public List<ImageListData> apply(List<ImageData> imageData) throws Exception {
                        List<ImageListData> listData = new ArrayList<>();
                        for(ImageData data : imageData) {
                            listData.add(new ImageListData(data));
                        }
                        return listData;
                    }
                })
                .subscribe(new SimpleObserver<List<ImageListData>>() {
                    @Override
                    public void onNext(@NonNull List<ImageListData> list) {
                        view.showLoadingIndicator(false);
                        view.showImages(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.showLoadingIndicator(false);
                    }
        });
    }

    @Override
    public void changeImageData(ImageListData listData) {
        repository.changeImageData(listData.getImageData());
    }

    @Override
    public void checkImageList(ImageListData listData) {
        boolean isChecked = !listData.isChecked();
        listData.setChecked(isChecked);
        view.showChecked(listData);
        if(isChecked) {
            checkedListData.add(listData);
        } else {
            checkedListData.remove(listData);
        }
        view.showDeleteMenu(checkedListData.size() > 0);
    }

    @Override
    public void removeCheckedImages() {
        Iterator<ImageListData> iterator = checkedListData.iterator();
        while(iterator.hasNext()) {
            ImageListData listData = iterator.next();
            view.removeListItem(listData);
            repository.removeImageData(listData.getImageData());
            iterator.remove();
        }
        view.showDeleteMenu(false);
    }
}
