package com.mason.kakao.masonsgallary.main.imagelist;

import com.mason.kakao.masonsgallary.base.SimpleObserver;
import com.mason.kakao.masonsgallary.model.ImagesRepository;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.ImageListData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * Created by kakao on 2017. 10. 31..
 */

public class ImageListPresenterImpl implements ImageListContract.Presenter {
    private ImageListContract.View view;
    private ImagesRepository repository;
    private List<ImageListData> checkedList = new ArrayList<>();

    public ImageListPresenterImpl(ImagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void bindView(ImageListContract.View view) {
        this.view = view;
    }

    @Override
    public void loadImages(Tag tag) {
        view.showLoadingIndicator(true);
        repository.getList(tag)
                .map(new Function<List<ImageData>, List<ImageListData>>() {
                    @Override
                    public List<ImageListData> apply(List<ImageData> imageData) throws Exception {
                        List<ImageListData> imageListData = new ArrayList<>();
                        for(ImageData data : imageData) {
                            imageListData.add(new ImageListData(data));
                        }
                        return imageListData;
                    }
                })
                .subscribe(new SimpleObserver<List<ImageListData>>() {
                    @Override
                    public void onNext(List<ImageListData> listData) {
                        view.showLoadingIndicator(false);
                        view.showImageList(listData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showLoadingIndicator(false);
                    }
                });
    }

    @Override
    public void removeCheckedImages() {

    }

    @Override
    public void onImageClick(ImageListData listData) {
        if(checkedList.size() > 0) {
            checkItem(listData);
            view.onChangeChecked(listData);
        } else {
            view.onShowDetail(listData);
        }
    }

    @Override
    public void onImageLongClick(ImageListData listData) {
        if(checkedList.size() > 0) {

        } else {
            checkItem(listData);
            view.onChangeChecked(listData);
        }
    }

    private void checkItem(ImageListData listData) {
        boolean check = !listData.isChecked();
        listData.setChecked(check);
        if(check) {
            checkedList.add(listData);
        } else {
            checkedList.remove(listData);
        }
    }
}
