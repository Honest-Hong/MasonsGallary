package com.mason.kakao.masonsgallary.images;

import android.Manifest;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mason.kakao.masonsgallary.base.SimpleObserver;
import com.mason.kakao.masonsgallary.dialog.SelectingTagDialog;
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

public class ImagesViewModel implements ImagesContract.ViewModel {
    private static final String TAG = "ImagesViewModel";
    private final ObservableArrayList<ImageListData> list = new ObservableArrayList<>();
    private final ObservableBoolean showList = new ObservableBoolean(false);
    private ImagesActivity activity;
    private ImagesRepository repository;
    private Tag filteredTag = Tag.All;

    public ImagesViewModel(ImagesActivity activity, ImagesRepository repository) {
        this.activity = activity;
        this.repository = repository;
    }

    @Override
    public void onCreate() {
        checkPermissions();
    }

    @Override
    public void changeFilter(Tag tag) {
        if(tag != filteredTag) {
            filteredTag = tag;
            loadList(filteredTag);
        }
    }
    @Override
    public ObservableArrayList<ImageListData> getList() {
        return list;
    }

    @Override
    public ObservableBoolean showList() {
        return showList;
    }

    @Override
    public void onClick(ImageListData listData) {
        listData.setChecked(!listData.isChecked());
        int position = list.indexOf(listData);
        list.set(position, listData);
    }

    @Override
    public void onLongClick(final ImageListData listData) {
        SelectingTagDialog.newInstance(listData.getImageData(), new SelectingTagDialog.OnSelectListener() {
            @Override
            public void onSelect(Tag tag) {
                if (tag == filteredTag) {
                    return;
                }

                int index = ImagesViewModel.this.list.indexOf(listData);
                if(filteredTag == Tag.All) {
                    ImagesViewModel.this.list.set(index, listData);
                } else {
                    ImagesViewModel.this.list.remove(index);
                }
            }
        }).show(activity.getSupportFragmentManager(), SelectingTagDialog.class.getName());
    }

    @Override
    public void removeCheckedList() {
        Iterator<ImageListData> iterator = list.iterator();
        while(iterator.hasNext()) {
            ImageListData listData = iterator.next();
            if(listData.isChecked()) {
                repository.removeImageData(listData.getImageData());
                iterator.remove();
            }
        }
    }

    private void loadList(Tag tag) {
        showList.set(false);
        list.clear();
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
                        showList.set(true);
                        ImagesViewModel.this.list.addAll(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showList.set(true);
                    }
                });
    }

    private void checkPermissions() {
        TedPermission.with(activity)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        loadList(filteredTag);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
}
