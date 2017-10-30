package com.mason.kakao.masonsgallary.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.mason.kakao.masonsgallary.model.data.ImageData;
import com.mason.kakao.masonsgallary.model.data.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kakao on 2017. 10. 20..
 */

public class ImagesRepository {
    private Context context;
    private List<ImageData> list = null;

    public ImagesRepository(Context context) {
        this.context = context;
    }

    public Observable<List<ImageData>> getList(final Tag tag) {
        return Observable.create(new ObservableOnSubscribe<List<ImageData>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<ImageData>> e) throws Exception {
                        if(tag == Tag.All) {
                            e.onNext(searchImageData());
                        } else {
                            if(list == null) {
                                searchImageData();
                            }
                            e.onNext(retrieveImages(tag));
                        }
                    }
                })
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void changeImageData(ImageData imageData) {
    }

    public void removeImageData(ImageData imageData) {
        if(list != null) {
            list.remove(imageData);
        }
    }

    private List<ImageData> searchImageData() {
        if(list != null) {
            return list;
        }

        final String[] projection = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_MODIFIED
        };
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        List<ImageData> result = new ArrayList<>();
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                Tag[] arr = Tag.values();
                Random random = new Random();
                final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                final int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                final int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
                do {
                    String data = cursor.getString(dataColumn);
                    String name = cursor.getString(nameColumn);
                    String date = cursor.getString(dateColumn);
                    int index = Math.abs(random.nextInt()) % (arr.length - 1);
                    result.add(new ImageData(data, name, arr[index], date));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        this.list = result;
        return result;
    }

    private List<ImageData> retrieveImages(Tag tag) {
        if(list == null) {
            return Collections.emptyList();
        }

        List<ImageData> list = new ArrayList<>();
        for(ImageData imageData : this.list) {
            if(imageData.getTag() == tag) {
                list.add(imageData);
            }
        }

        return list;
    }
}
