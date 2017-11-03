package com.mason.kakao.masonsgallary.ui.images.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;

import com.mason.kakao.masonsgallary.base.SimpleObserver;
import com.mason.kakao.masonsgallary.dialog.SelectingTagDialog;
import com.mason.kakao.masonsgallary.model.ImagesRepository;
import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by kakao on 2017. 10. 23..
 */

public class ImageListViewModel {
    public final ObservableArrayList<ImageListItemViewModel> list = new ObservableArrayList<>();
    public final ObservableBoolean showList = new ObservableBoolean(false);
    public final ObservableField<ImageData> detailViewData = new ObservableField<>();
    private ImagesRepository repository;
    private Tag filteredTag = Tag.All;

    public ImageListViewModel(ImagesRepository repository) {
        this.repository = repository;
    }

    public void loadFirst() {
        loadList(Tag.All);
    }

    public void changeFilter(Tag tag) {
        if(tag != filteredTag) {
            filteredTag = tag;
            loadList(filteredTag);
        }
    }

    public void onClick(ImageListItemViewModel viewModel) {
        detailViewData.set(viewModel.getImageData());
//        viewModel.checked.set(!viewModel.checked.get());
    }

    public void onLongClick(final ImageListItemViewModel viewModel) {
//        SelectingTagDialog.newInstance(viewModel.getImageData(), new SelectingTagDialog.OnSelectListener() {
//            @Override
//            public void onSelect(Tag tag) {
//                if (tag == filteredTag) {
//                    return;
//                }
//
//                int index = ImageListViewModel.this.list.indexOf(viewModel);
//                if(filteredTag == Tag.All) {
//                    ImageListViewModel.this.list.set(index, viewModel);
//                } else {
//                    ImageListViewModel.this.list.remove(index);
//                }
//            }
//        }).show(activity.getSupportFragmentManager(), SelectingTagDialog.class.getName());
    }

    public void removeCheckedList() {
        Iterator<ImageListItemViewModel> iterator = list.iterator();
        while(iterator.hasNext()) {
            ImageListItemViewModel listData = iterator.next();
            if(listData.checked.get()) {
                repository.removeImageData(listData.getImageData());
                iterator.remove();
            }
        }
    }

    private void loadList(Tag tag) {
        showList.set(false);
        list.clear();
        repository.getList(tag)
                .map(new Function<List<ImageData>, List<ImageListItemViewModel>>() {
                    @Override
                    public List<ImageListItemViewModel> apply(List<ImageData> imageData) throws Exception {
                        List<ImageListItemViewModel> viewModels = new ArrayList<>();
                        for(ImageData data : imageData) {
                            viewModels.add(new ImageListItemViewModel(data));
                        }
                        return viewModels;
                    }
                })
                .subscribe(new SimpleObserver<List<ImageListItemViewModel>>() {
                    @Override
                    public void onNext(@NonNull List<ImageListItemViewModel> list) {
                        showList.set(true);
                        ImageListViewModel.this.list.addAll(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showList.set(true);
                    }
                });
    }
}
