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
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by kakao on 2017. 10. 23..
 */

public class ImagesViewModel implements ImagesContract.ViewModel, ImageLongClickListener {
    private static final String TAG = "ImagesViewModel";
    private final ObservableArrayList<ImageData> mList = new ObservableArrayList<>();
    private final ObservableBoolean showList = new ObservableBoolean(false);
    private ImagesActivity mActivity;
    private ImagesRepository mRepository;
    private Tag mFilteredTag = Tag.All;

    public ImagesViewModel(ImagesActivity activity, ImagesRepository mRepository) {
        this.mActivity = activity;
        this.mRepository = mRepository;
    }

    @Override
    public void onCreate() {
        checkPermissions();
    }

    @Override
    public void onLongClick(final ImageData imageData) {
        SelectingTagDialog.newInstance(imageData, new SelectingTagDialog.OnSelectListener() {
            @Override
            public void onSelect(Tag tag) {
                if (tag == mFilteredTag) {
                    return;
                }

                int index = mList.indexOf(imageData);
                if(mFilteredTag == Tag.All) {
                    mList.set(index, imageData);
                } else {
                    mList.remove(index);
                }
            }
        }).show(mActivity.getSupportFragmentManager(), SelectingTagDialog.class.getName());
    }

    @Override
    public void changeFilter(Tag tag) {
        if(tag != mFilteredTag) {
            mFilteredTag = tag;
            loadList(mFilteredTag);
        }
    }

    @Override
    public ObservableArrayList<ImageData> getList() {
        return mList;
    }

    @Override
    public ObservableBoolean showList() {
        return showList;
    }

    private void loadList(Tag tag) {
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

    private void checkPermissions() {
        TedPermission.with(mActivity)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        loadList(mFilteredTag);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                    }
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
}
