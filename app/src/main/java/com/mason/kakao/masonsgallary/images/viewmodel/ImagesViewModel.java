package com.mason.kakao.masonsgallary.images.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.mason.kakao.masonsgallary.base.SimpleObserver;
import com.mason.kakao.masonsgallary.dialog.SelectingTagDialog;
import com.mason.kakao.masonsgallary.images.ImagesActivity;
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

public class ImagesViewModel {
    public final ObservableArrayList<ImageListItemViewModel> list = new ObservableArrayList<>();
    public final ObservableBoolean showList = new ObservableBoolean(false);
    private ImagesActivity activity;
    private ImagesRepository repository;
    private Tag filteredTag = Tag.All;

    public ImagesViewModel(ImagesActivity activity, ImagesRepository repository) {
        this.activity = activity;
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
        viewModel.checked.set(!viewModel.checked.get());
    }

    public void onLongClick(final ImageListItemViewModel viewModel) {
        SelectingTagDialog.newInstance(viewModel.getImageData(), new SelectingTagDialog.OnSelectListener() {
            @Override
            public void onSelect(Tag tag) {
                if (tag == filteredTag) {
                    return;
                }

                int index = ImagesViewModel.this.list.indexOf(viewModel);
                if(filteredTag == Tag.All) {
                    ImagesViewModel.this.list.set(index, viewModel);
                } else {
                    ImagesViewModel.this.list.remove(index);
                }
            }
        }).show(activity.getSupportFragmentManager(), SelectingTagDialog.class.getName());
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
                        ImagesViewModel.this.list.addAll(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showList.set(true);
                    }
                });
    }
}
